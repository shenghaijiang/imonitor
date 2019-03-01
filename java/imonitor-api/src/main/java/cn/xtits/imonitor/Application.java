package cn.xtits.imonitor;

import cn.xtits.xtf.web.springmvc.ExceptionHandlerResolver;
import cn.xtits.xtf.web.springmvc.JsonMessageConverter;
import cn.xtits.xtf.web.springmvc.RequestContextInterceptor;
import cn.xtits.xtf.web.springmvc.RequestLogInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@Configuration
public class Application extends WebMvcConfigurerAdapter {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * 配置拦截器
     *
     * @param registry
     * @author lance
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestContextInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new RequestLogInterceptor()).addPathPatterns("/**");

    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new ExceptionHandlerResolver());
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.clear();
        converters.add(new JsonMessageConverter());
    }
}
