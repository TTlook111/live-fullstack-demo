package com.LiveFullstack.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS 全局配置
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 允许所有来源
                .allowedOriginPatterns("*")
                // 允许携带凭证（Cookie / Authorization 头等）
                .allowCredentials(true)
                // 常见 HTTP 方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                // 允许所有请求头
                .allowedHeaders("*")
                // 暴露响应头给前端
                .exposedHeaders("*")
                // 预检请求缓存时间（秒），减少 OPTIONS 请求次数
                .maxAge(3600);
    }
}
