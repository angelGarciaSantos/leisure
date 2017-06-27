package es.udc.fi.tfg.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import es.udc.fi.tfg.interceptor.RequestInterceptor;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "es.udc.fi.tfg")
public class AppConfig extends WebMvcConfigurerAdapter{
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(new RequestInterceptor()).addPathPatterns("/private/**");
    }

}
