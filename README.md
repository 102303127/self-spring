# 手写Spring源码项目介绍

该项目用于自我学习过程中的实践

[spring-aop项目结构](spring模块/src/main/java/com/zhang/aop-tree.txt)

[spring-bean项目结构](spring模块/src/main/java/com/zhang/bean-tree.txt)

[spring-context项目结构](spring模块/src/main/java/com/zhang/context-tree.txt)

[springmvc项目结构](springmvc模块/src/main/java/com/zhang/web/tr.txt)

- 飞书文档：https://zquk8ow8fg6.feishu.cn/wiki/ActQw0AK7iPvvvkPkhlcpCIjnRh?from=from_copylink

## 项目介绍
项目分为spring模块和springmvc模块
### spring模块

包括ioc模块，aop模块，jdbc模块(没有完善)
实现了xml注册Bean的部分，注解和xml上逻辑大体相同（时间不太够用就简单实现注解的）

- xml依赖于XmlBeanDefinitionReader实现注册Bean

- 注解主要依赖于ClassPathBeanDefinitionScanner实现扫描注册Bean(没有实现)

### springmvc模块

  由于自己实现的ioc没有实现注解，mvc模块依赖于org.springframework中的spring的ioc和web模块
- springmvc-starter


## Ioc模块

### 1.加载并注册BeanDefinitions

![加载并注册BeanDefinitions时序图](spring模块/png/加载并注册BeanDefinitions时序图.png)

### 2.Spring中预先加载单例Bean(包括getBean())

![img.png](spring模块/png/getBean().png)




### 3.部分原理
#### 1. 解决循环依赖

   在对Bean的属性填充populateBean()

   循环依赖的解决：三级缓存

    在DefaultSingletonBeanRegistry设置有三个map用于实现三级缓存，解决循环依赖问题
   - singletonObjects         第一级缓存，用于保存实例化、注入、初始化完成的bean实例
   - earlySingletonObjects  第二级缓存，存放原始的 bean 对象（尚未填充属性），用于解决循环依赖
   - singletonFactories       第三级缓存，用于保存bean创建工厂，以便于后面扩展有机会创建代理对象。

   其中第三级缓存中添加ObjectFactory对象，对象可能需要经过Aop，通过getEarlyBeanReference方法获取代理对象
   如果不实现Aop功能，二级缓存是足够解决循环依赖的
#### 2. 解析xml文件
- spring 源码中解析xml是实现了BeanDefinitionDocumentReader接口的默认实现类
  可以通过parseBeanDefinitionElement方法解析单个bean标签，
  也可以通过parseBeanDefinitions方法解析多个bean标签
- 这里简化实现，所有解析均在DefaultBeanDefinitionDocumentReader(BeanDefinitionDocumentReader的默认实现类)中实现。
#### 3. 解析占位符
- 用PropertyResourceConfigurer实现对@Value注解的解析填充
  该类实现方法已弃用
  新版本使用 org.springframework.context.support.PropertySourcesPlaceholderConfigurer ，
  通过利用 org.springframework.core.env.Environment
  AND org.springframework.core.env.PropertySource
  机制来更灵活。
#### 4. 包扫描
- 通过扫描指定包，把带有@Component注解的类注册到ioc中作为Bean
  ClassPathBeanDefinitionScanner #doScan----->
  ClassPathScanningCandidateComponentProvider #findCandidateComponents         ##查找指定基础包路径下带有Component注解的类
#### 5. Bean 的生命周期
   - 时序图中也有流程
   
![img.png](spring模块/png/img.png)
   



## Aop模块

## 1.Aop代理注册流程

![img.png](spring模块/png/Aop创建代理流程时序图.png)

## 2.部分原理

1. ### 把代理对象到ioc容器

> 容器在初始化过程中会**预先实例化**单例Bean,调用getBean()

- 在Spring Ioc完成代理对象target的实例化、填充、初始化。然后在**初始化后置处理器**中进行介入，通过BeanPostProcess会判断是否需要增强(Aop代理对象)

2. ### 调用方法wrapIfNecessary

> 通过后置处理器进入AbstractAutoProxyCreator中调用wrapIfNecessary

- 1.判断是不是基础设施类（如Advice、Pointcut等），如果是直接返回Bean
- 2.而后通过getAdvicesAndAdvisorsForBean()方法获取bean的所有Advisor，其中重要的方法**findCandidateAdvisors()**   遍历容器中的Advisor获取所有切面，判断是否匹配细节交给AopUtil实现
- 3.找到所有Advisor之后就开始着手创建代理(createProxy),根据需要通过proxyFactory.**getProxy()**创建对应的代理，赋予target等属性

3. ### 代理调用过程

- 在调用target目标方法时，首先进入DynamicAdvisedInterceptor#intercept
  - 通过调用DefaultAdvisorChainFactory#**getInterceptorsAndDynamicInterceptionAdvice**获取与指定配置、方法和目标类相关的拦截器和动态拦截建议，最终返回**拦截器列表。**
  - 如果拦截器链不为空，则调用proceed()，依次执行拦截器链中的拦截器(这一步执行通知Advice)或执行目标方法

4. ### 核心类

- AspectJExpressionPointcut
  - *AspectJ切点，实现*Pointcut, ClassFilter, MethodMatcher*接口*
  - 判断给定的类是否与切点表达式匹配。
  - 用于根据 AspectJ 切点表达式创建一个新的 AspectJExpressionPointcut 实例。
- AspectJExpressionPointcutAdvisor
  - Advisor是Pointcut和Advice的组合
  - 专门处理基于AspectJ的通知+切点的
- AbstractAutoProxyCreator
  - Aop创建动态代理的入口及主要执行
  - 根据需要包装给定的bean，如果bean满足AOP切点条件，则返回代理对象，否则返回原bean。
  - 默认子类DefaultAdvisorAutoProxyCreator用于实现模板方法getAdvicesAndAdvisorsForBean()，获取所有适用于当前Bean 的 Advisors
- DefaultAdvisorChainFactory
  - 实现AdvisorChainFactory接口
  - 通过方法getInterceptorsAndDynamicInterceptionAdvice(),获取与指定配置、方法和目标类相关的拦截器和动态拦截建议。
- MethodInterceptor(org.aopalliance.intercept.MethodInterceptor)
  - 主要实现了两个方法拦截器
  - AfterReturningAdviceInterceptor#invoke()
  - MethodBeforeAdviceInterceptor#invoke()

## Jdbc模块

JdbcTemplate继承了JdbcAccessor和接口JdbcOperation。

JdbcAccessor主要是对DataSource进行管理和配置。

JdbcOperation主要是通过JDBC操作数据库的基本操作方法。









## MVC模块

该模块要依赖于org.springframework中的spring的ioc和web模块

![img.png](springmvc主要组件关系图.png)

### 1.初始化父子容器

- ContextLoadListener创建了一个Spring容器作为父容器，里面主要管理Service和数据。

- DispatcherServlet创建了一个Springmvc容器，作为Spring容器的子容器，管理Controller层。

  子容器可以获取到父容器中的对象，Controller里面可以注入Service层级的对象。


  在对容器初始化中会初始化其中的Bean,其中初始化**WebMvcConfigurationSupport**时会对其中定义的**HandlerMapping**，**HandlerAdapter**等容器Bean进行初始化

  ```java
  ## com.zhang.web.config.WebMvcConfigurationSupport
  ...
  @Bean
      public HandlerMapping handlerMapping(){
          final RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
          requestMappingHandlerMapping.setOrder(0);
          final InterceptorRegistry registry = new InterceptorRegistry();
          getIntercept(registry);
          // todo 通过 registry 获取 MappedInterceptor
          // 获取拦截器
          final List<MappedInterceptor> interceptors = registry.getInterceptors();
          requestMappingHandlerMapping.addHandlerInterceptors(interceptors);
          // 添加拦截器
          return requestMappingHandlerMapping;
      }
  ...
  ```


### 2.把@RequestMapping上的路径注册给程序并放入缓存中的

spring在启动的时候会初始化AbstractHandlerMethodMapping类，他实现了InitializingBean的接口

对HandlerMapping初始化结束之后调用**afterPropertiesSet**()进行对方法中的路径进行解析存储**MappingRegistry**中。



### 3.处理请求

![img_2.png](spring模块/png/img_2.png)



# 五.webmvc-starter

1. ## 项目结构

```Plain
└─main
    ├─java
    │  └─com
    │      └─zhang
    │              MvcAutoConfiguration.java         ## 自动装配类
    │              
    └─resources
        └─META-INF
                spring.factories                          
```

2. ## 依赖
```XML
<dependencies>

    <!-- 用来打包starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <!-- 手写springmvc -->
    <dependency>
        <groupId>com.zhang</groupId>
        <artifactId>springmvc-07-27</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>

</dependencies>

````


























