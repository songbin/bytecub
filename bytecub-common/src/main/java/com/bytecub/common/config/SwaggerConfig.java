package com.bytecub.common.config;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger config
 * @author songbin
 * @version Id: SwaggerConfig.java, v 0.1 2018/10/30   Exp $$
 */

@Configuration
@EnableSwagger2
@Slf4j
public class SwaggerConfig implements EnvironmentAware {
   // private RelaxedPropertyResolver propertyResolver;
    @Value("${swagger.basePackage:}")
    private String basePackage;
    @Autowired(required = false)
    private List<Parameter> defaultParameters;
    static String spliter = ";";
    @Override
    public void setEnvironment(final Environment environment) {
       // this.propertyResolver = new RelaxedPropertyResolver(environment, "swagger.");
    }

    /**
     * 装载swagger
     * @return Docket实例
     */
    @Bean
    public Docket swaggerSpringfoxDocket() {
        log.debug("Starting Swagger");
        String[] baseUris = null;
        if(!StringUtils.isEmpty(basePackage)){
            baseUris = basePackage.split(";");
        }
        StopWatch watch = new StopWatch();
        watch.start();
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        ticketPar.name("token").description("登录token")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build(); //header中的ticket参数非必填，传空也可以
        pars.add(ticketPar.build());    //根据每个方法名也知道当前方法在设置什么参数

        Docket swaggerSpringMvcPlugin = new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(pars)
                // .genericModelSubstitutes(ResponseEntity.class)
                .apiInfo(apiInfo()).select().apis(basePackage(basePackage))
                .paths(PathSelectors.any()).build();
        watch.stop();
        log.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
        return swaggerSpringMvcPlugin;
    }

    @SuppressWarnings("deprecation")
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("api swagger document")
                .description("前后端联调swagger api 文档")
                .version("2.1.5.5")
                .build();
    }
    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return input -> declaringClass(input).transform(handlerPackage(basePackage)).or(true);
    }

    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage)     {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage.split(spliter)) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }
}
