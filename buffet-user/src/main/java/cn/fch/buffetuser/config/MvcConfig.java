package cn.fch.buffetuser.config;

import cn.fch.buffetuser.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: BuffetOrder
 * @description: 资源映射
 * @CreatedBy: fch
 * @create: 2022-10-15 21:12
 **/
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/User/**")
                .excludePathPatterns("/User/RegUser", "/Food/LoginUser");
    }
}
