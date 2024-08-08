package com.zhang.auto;

import com.zhang.web.annotation.EnableWebMvc;
import com.zhang.web.servlet.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.util.ObjectUtils;


@EnableWebMvc
public class WebAutoConfiguration extends ApplicationObjectSupport {


    private static final int M = 1024 * 1024;

    @Bean
    public Tomcat tomcat() throws LifecycleException {
        Integer port = null;
        final Tomcat tomcat = new Tomcat();
        port = ObjectUtils.isEmpty(port) ? 8080 : port;
        tomcat.setPort(port);
        tomcat.setHostname("localhost");
        tomcat.setBaseDir(".");
        Context context = tomcat.addWebapp("/", System.getProperty("user.dir") + "/src/main");
        final ApplicationContext applicationContext = obtainApplicationContext();
        final Wrapper wrapper =
                tomcat.addServlet(context, "zhang-webmvc",  new DispatcherServlet((applicationContext)));
        wrapper.addMapping("/");
        tomcat.getConnector();
        tomcat.start();
        logger.info("Tomcat initialized with port: "+port);
        tomcat.getServer().await();
        return tomcat;
    }

}
