package com.zhang.web.servlet.handler;


import com.zhang.web.exception.MvcException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author zhang
 * @date 2024/7/28
 * @Description
 */
public abstract class AbstractHandlerMethodMapping<T> extends AbstractHandlerMapping
        implements InitializingBean{

    private static final String SCOPED_TARGET_NAME_PREFIX = "scopedTarget.";


    private final MappingRegistry mappingRegistry = new MappingRegistry();

    protected int order;

    public void setOrder(int order){
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    //2.
    @Override
    public void afterPropertiesSet() throws Exception {
        initHandlerMethods();
    }



    /**
     * 找到所有的handler方法
     *
     * 从Spring容器中获取所有bean，并遍历每个bean的名称。
     */
    protected void initHandlerMethods() throws Exception {
        //从容器中获取所有object类型名
        // 获取所有bean
        final ApplicationContext context = obtainApplicationContext();
        final String[] beanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context, Object.class);
        for (String beanName : beanNames) {
            // 抽象,过滤
            // (在RequestMappingHandlerMapping中根据Controller和RequestMapping注解过滤)
            if (!beanName.startsWith(SCOPED_TARGET_NAME_PREFIX) &&
                    isHandler(getApplicationContext().getType(beanName))){
                // 探测类中定义的handler方法
                detectHandlerMethods(beanName);
            }
        }
    }



    /**
     * 查找出该处理器的处理器方法
     *
     * @param name 处理器名称
     */
    protected void detectHandlerMethods(String name) throws Exception {
        // 获取handler的Class对象
        final ApplicationContext context = obtainApplicationContext();
        final Class<?> handlerType = context.getType(name);
        Object bean = context.getBean(name);


        if (handlerType != null) {
            // 获取handler的真实Class对象（假若handler是CGLIB代理生成的子类，则获取原始类的Class对象）
            Class<?> userType = ClassUtils.getUserClass(handlerType);

            // 获取当前类下的所有方法
            final Method[] methods = handlerType.getDeclaredMethods();
            List<HandlerMethod> handlerMethods = new ArrayList<>();

            // 获得类上的路径
            String path = "";
            if (AnnotatedElementUtils.hasAnnotation(handlerType, RequestMapping.class)) {
                final RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(handlerType, RequestMapping.class);
                String[] values = requestMapping.value();
                for (String value : values) {
                    path = value.isEmpty() ? "" : value;
                }
            }


            // 对方法进行遍历
            for (Method method : methods) {
                // 收集
                final HandlerMethod handlerMethod = new HandlerMethod(bean, method);
                // 获得方法上的路径
                RequestMapping requestMapping =
                        (RequestMapping) getMappingForMethod(method, userType);
                String[] values = requestMapping.value();
                String childPath = "";
                for (String value : values) {
                    childPath = value.equals("") ? "" : value;
                }

                handlerMethod.setRequestMethods(requestMapping.method());
                // 拼接
                handlerMethod.setPath(path + childPath);

                handlerMethods.add(handlerMethod);

            }


            // 注册HandlerMethod
            if (!ObjectUtils.isEmpty(handlerMethods)) {
                for (HandlerMethod handlerMethod : handlerMethods) {
                    registerMapper(handlerMethod);
                }
            }

        }

    }








    protected abstract T getMappingForMethod(Method method, Class<?> userType);


    @Override
    protected HandlerMethod getHandlerInternal(HttpServletRequest request) throws Exception {
        return lookupHandlerMethod(request);
    }




    /**
     * 根据HttpServletRequest请求对象查找Handler方法。
     *
     * @param request HTTP请求对象
     * @return 返回查找到的处理器方法，若未找到则返回null
     * @throws Exception 查找处理器方法时发生的异常
     */
    protected HandlerMethod lookupHandlerMethod(HttpServletRequest request) throws Exception {
        // 1.获取请求路径 请求类型
        HandlerMethod handlerMethod = null;
        final String requestPath = request.getRequestURI();
        final Map<String, Set<HandlerMethod>> fuzzyMatchingPath = mappingRegistry.getFuzzyMatchingPath();
        final Map<String, Set<HandlerMethod>> accurateMatchingPath = mappingRegistry.getAccurateMatchingPath();
        boolean flag = false;
        // 2.精确匹配当中如果没有则说明在模糊匹配,需要遍历模糊匹配的key来进行正则表达式查找
        if (!accurateMatchingPath.containsKey(requestPath)){
            // 遍历模糊匹配
            Set<String> paths = fuzzyMatchingPath.keySet();
            // 对路径进行排序
            paths = paths.stream().sorted((o1, o2) -> -(o1.compareTo(o2))).collect(Collectors.toCollection(LinkedHashSet::new));
            //  可能后面能匹配上
            for (String path : paths) {
                // 能匹配成功则还需要匹配请求类型
                // 一旦匹配到，则直接返回
                if (Pattern.compile(path).matcher(requestPath).matches()) {
                    flag = true;
                    Set<HandlerMethod> handlerMethods = fuzzyMatchingPath.get(path);
                    handlerMethod = getHandlerMethod(handlerMethods,request);
                    if (!ObjectUtils.isEmpty(handlerMethod)){
                        return handlerMethod;
                    }
                }
            }
        }
        // 3.精确匹配直接查找
        if (accurateMatchingPath.containsKey(requestPath)){
            flag = true;
            handlerMethod = getHandlerMethod(accurateMatchingPath.get(requestPath),request);
            if (!ObjectUtils.isEmpty(handlerMethod)){
                return handlerMethod;
            }
        }
        if (flag){
            // 请求类型不匹配
            throw new MvcException(requestPath + "请求类型不匹配");
        }

        // 404
        return null;
    }

    /**
     * 判断是否是 Handler
     * 模板方法交给子类实现
     *
     * @param beanType
     * @return
     */
    protected abstract boolean isHandler(Class<?> beanType);



    protected void registerMapper(HandlerMethod handlerMethod) throws Exception {
        mappingRegistry.register(handlerMethod);
    }








    // 保存HandlerMethod
    class MappingRegistry{

        // 精确路径
        Map<String, Set<HandlerMethod>> accurateMatchingPath = new HashMap<>();

        // 模糊路径 fuzzy matching
        Map<String,Set<HandlerMethod>> fuzzyMatchingPath = new HashMap<>();


        public void register(HandlerMethod handlerMethod) throws Exception{
            // 获取请求路径
            String path = handlerMethod.getPath();
            if (path.contains("{") && path.contains("}")){
                // /order/get/{id} -> /order/get/1
                path = path.replaceAll("\\{\\w+\\}", "(\\\\w+)");
                register(fuzzyMatchingPath,path,handlerMethod);
            }else {
                // 根据请求路径的不同分别保存HandlerMethod
                register(accurateMatchingPath,path,handlerMethod);
            }
        }

        private void register(Map<String, Set<HandlerMethod>> mapPath, String path, HandlerMethod handlerMethod) throws Exception{
            // /order/get/{id} -> /order/get/1

            if (!mapPath.containsKey(path)) {
                mapPath.put(path,new HashSet<>());
            }
            mapPath.get(path).add(handlerMethod);
        }

        public Map<String, Set<HandlerMethod>> getFuzzyMatchingPath() {
            return fuzzyMatchingPath;
        }

        public Map<String, Set<HandlerMethod>> getAccurateMatchingPath() {
            return accurateMatchingPath;
        }
    }


}
