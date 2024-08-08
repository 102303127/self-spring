package com.zhang.web.servlet.adapter;


import com.zhang.web.convert.*;
import com.zhang.web.convert.config.Convert;
import com.zhang.web.convert.config.ConvertComposite;
import com.zhang.web.convert.config.ConvertHandler;
import com.zhang.web.resolver.*;
import com.zhang.web.resolver.HandlerMethodReturnValueHandlerComposite;
import com.zhang.web.resolver.HttpHeadersReturnValueHandler;
import com.zhang.web.resolver.RequestResponseBodyMethodProcessor;
import com.zhang.web.servlet.HandlerMethodReturnValueHandler;
import com.zhang.web.servlet.ControllerAdviceBean;
import com.zhang.web.servlet.HandlerAdapter;
import com.zhang.web.servlet.HandlerMethodArgumentResolver;
import com.zhang.web.servlet.handler.HandlerMethod;
import com.zhang.web.servlet.handler.ServletInvocableMethod;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author zhang
 * @date 2024/7/29
 * @Description
 */
public class RequestMappingHandlerAdapter extends ApplicationObjectSupport implements HandlerAdapter, InitializingBean {

    private int order = Ordered.LOWEST_PRECEDENCE;

    public static final ReflectionUtils.MethodFilter MODEL_ATTRIBUTE_METHODS = method ->
            (!AnnotatedElementUtils.hasAnnotation(method, RequestMapping.class));


    private HandlerMethodArgumentResolverComposite argumentResolvers;

    private ConvertComposite convertComposite;

    private HandlerMethodReturnValueHandlerComposite returnValueHandlers;

    private final Map<ControllerAdviceBean, Set<Method>> modelAttributeAdviceCache = new LinkedHashMap<>();

    private ConfigurableBeanFactory beanFactory;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }



    @Override
    public void handler(HttpServletRequest req, HttpServletResponse res, HandlerMethod handler) throws Exception {
        final ServletWebRequest servletWebRequest = new ServletWebRequest(req, res);
        final ServletInvocableMethod invocableMethod = new ServletInvocableMethod();
        invocableMethod.setHandlerMethod(handler);
        invocableMethod.setConvertComposite(convertComposite);
        invocableMethod.setReturnValueHandlerComposite(returnValueHandlers);
        invocableMethod.setResolverComposite(argumentResolvers);

        invocableMethod.invokeAndHandle(servletWebRequest,handler);

    }


    public ConfigurableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void afterPropertiesSet() {
        // 首先执行此操作，它可能会添加 ResponseBody 建议 bean
        initControllerAdviceCache();

        if (this.argumentResolvers == null) {
            List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
            this.argumentResolvers = new HandlerMethodArgumentResolverComposite();
            argumentResolvers.addResolvers(resolvers);
        }
        if (this.convertComposite == null) {
            Map<Class, ConvertHandler> defaultConvert = getDefaultConvert();
            this.convertComposite = new ConvertComposite();
            convertComposite.addConvertMap(defaultConvert);
        }
        if (this.returnValueHandlers == null) {
            List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
            returnValueHandlers = new HandlerMethodReturnValueHandlerComposite();
            returnValueHandlers.addMethodReturnValueHandlers(handlers);
        }
    }



    private void initControllerAdviceCache() {
        if (getApplicationContext() == null) {
            return;
        }
        List<ControllerAdviceBean> adviceBeans = ControllerAdviceBean.findAnnotatedBeans(getApplicationContext());
        for (ControllerAdviceBean adviceBean : adviceBeans) {
            Class<?> beanType = ClassUtils.getUserClass(adviceBean.getClass());
            Set<Method> attrMethods = MethodIntrospector.selectMethods(beanType, MODEL_ATTRIBUTE_METHODS);
            if (!attrMethods.isEmpty()) {
                this.modelAttributeAdviceCache.put(adviceBean, attrMethods);
            }
        }


    }

    // 初始化类型转换器
    public Map<Class, ConvertHandler> getDefaultConvert(){
        final Map<Class, ConvertHandler> convertMap = new HashMap<>();
        convertMap.put(Integer.class,getConvertHandler(new IntegerConvert(Integer.class)));
        convertMap.put(int.class,getConvertHandler(new IntegerConvert(Integer.class)));
        convertMap.put(String.class,getConvertHandler(new StringConvert(String.class)));
        convertMap.put(Long.class,getConvertHandler(new LongConvert(Long.class)));
        convertMap.put(long.class,getConvertHandler(new LongConvert(Long.class)));
        convertMap.put(Float.class,getConvertHandler(new FloatConvert(Float.class)));
        convertMap.put(float.class,getConvertHandler(new FloatConvert(Float.class)));
        convertMap.put(Boolean.class,getConvertHandler(new BooleanConvert(Boolean.class)));
        convertMap.put(boolean.class,getConvertHandler(new BooleanConvert(Boolean.class)));
        convertMap.put(Byte.class,getConvertHandler(new ByteConvert(Byte.class)));
        convertMap.put(byte.class,getConvertHandler(new ByteConvert(Byte.class)));
        convertMap.put(Short.class,getConvertHandler(new ShortConvert(Short.class)));
        convertMap.put(short.class,getConvertHandler(new ShortConvert(Short.class)));
        convertMap.put(Date.class,getConvertHandler(new DateConvert(Date.class)));
        convertMap.put(Map.class,getConvertHandler(new MapConvert(HashMap.class)));
        convertMap.put(Collection.class,getConvertHandler(new CollectionConvert(Collection.class)));
        convertMap.put(List.class,getConvertHandler(new ListConvert(ArrayList.class)));
        convertMap.put(Set.class,getConvertHandler(new SetConvert(HashSet.class)));
        return convertMap;
    }


    private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>(30);

        // Annotation-based argument resolution
        resolvers.add(new RequestParamMethodArgumentResolver());
        resolvers.add(new RequestParamMapMethodArgumentResolver());
        resolvers.add(new PathVariableMethodArgumentResolver());
        resolvers.add(new RequestHeaderMethodArgumentResolver());
        resolvers.add(new RequestHeaderMapMethodArgumentResolver());

        // Type-based argument resolution
        resolvers.add(new ServletRequestMethodArgumentResolver());
        resolvers.add(new ServletResponseMethodArgumentResolver());
        // Catch-all
        resolvers.add(new RequestParamMethodArgumentResolver());

        return resolvers;
    }

    protected ConvertHandler getConvertHandler(Convert convert){
        try {
            final Method method = convert.getClass().getDeclaredMethod("convert", Object.class);
            return new ConvertHandler(convert,method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers() {
        ArrayList<HandlerMethodReturnValueHandler> handlerMethodReturnValueHandlers = new ArrayList<>();
        handlerMethodReturnValueHandlers.add(new RequestResponseBodyMethodProcessor());
        handlerMethodReturnValueHandlers.add(new HttpHeadersReturnValueHandler());
        return handlerMethodReturnValueHandlers;
    }


    /**
     * 判断给定的处理器方法是否支持
     *
     * @param handlerMethod 处理器方法
     */
    @Override
    public boolean support(HandlerMethod handlerMethod) {
        return AnnotatedElementUtils.hasAnnotation(handlerMethod.getMethod(), RequestMapping.class);
    }





}
