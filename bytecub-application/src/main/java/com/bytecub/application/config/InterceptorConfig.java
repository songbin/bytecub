package com.bytecub.application.config;

import com.bytecub.common.constants.BCConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author dell
 */
@Configuration
@Slf4j
public class InterceptorConfig extends WebMvcConfigurationSupport {

    /**
     * 注册拦截所有的请求
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(swaggerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
 //      registry.addInterceptor(authenticationInterceptor())
//                .addPathPatterns("/" + BCConstants.URL_PREFIX.MGR + "**")
 //               .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");

        registry.addInterceptor(openApiInterceptor())
                .addPathPatterns("/" + BCConstants.URL_PREFIX.OPEN_API + "**")
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
    }


    @Bean
    SwaggerInterceptor swaggerInterceptor() {
        return new SwaggerInterceptor();
    }
    /**
     *  拦截器BEAN
     * @return
     */
    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }
    @Bean
    public OpenApiInterceptor openApiInterceptor() {
        return new OpenApiInterceptor();
    }
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
