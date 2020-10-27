package com.market.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    // https://juejin.im/post/6844903960906563591


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**");

    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/index").setViewName("index.html");
    }

    /**
     * 比如，我们想自定义静态资源映射目录的话，只需重写addResourceHandlers方法即可。
     * 注：如果继承WebMvcConfigurationSupport类实现配置时必须要重写该方法
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/webapp/assets/");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/webapp/");
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("");
        viewResolver.setSuffix(".html");
        viewResolver.setCache(false);
        viewResolver.setContentType("text/html;charset=UTF-8");
        viewResolver.setOrder(0);
        registry.viewResolver(viewResolver);
    }


}