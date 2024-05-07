package com.hb0730.security.config;

import com.hb0730.base.utils.PasswordUtil;
import com.hb0730.security.annotation.AnonymousAccess;
import com.hb0730.security.filter.JwtTokenAuthenticationFilter;
import com.hb0730.security.handler.TokenAccessDeniedHandler;
import com.hb0730.security.handler.TokenAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/3/23
 */
@Configuration
// web支持
@EnableWebSecurity
// 对注解@PreAuthorize, @PostAuthorize, @PreFilter, @PostFilter生效
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {
    private final UserDetailsService userDetailsService;
    private final CorsConfigurationSource corsConfigurationSource;
    private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final TokenAuthenticationEntryPoint tokenAuthenticationEntryPoint;
    private final JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter;
    private final ApplicationContext applicationContext;


    /**
     * 安全过滤链
     *
     * @param http .
     * @return .
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        Map<RequestMethodEnum, Set<String>> anonymousUrl = getAnonymousUrl();

        http
                // csrf 禁用
                .csrf(AbstractHttpConfigurer::disable)
                // 跨域配置
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                // 防止iframe 造成的跨域攻击
                .headers(
                        (headers) ->
                                headers.frameOptions(
                                        HeadersConfigurer.FrameOptionsConfig::disable
                                )
                )
                // 禁用session
                .sessionManagement(
                        sessionManagement ->
                                sessionManagement
                                        .sessionCreationPolicy(
                                                org.springframework.security.config.http.SessionCreationPolicy.STATELESS
                                        )
                )
                //禁用form表单登录
                .formLogin(AbstractHttpConfigurer::disable)
                //禁用httpBasic
                .httpBasic(AbstractHttpConfigurer::disable)
                //异常处理
                .exceptionHandling(
                        exceptionHandling ->
                                exceptionHandling
                                        .authenticationEntryPoint(tokenAuthenticationEntryPoint)
                                        .accessDeniedHandler(tokenAccessDeniedHandler)
                )
                // userDetailsService
                .userDetailsService(userDetailsService)
                // logout
                .logout(
                        logout -> logout.logoutUrl("/auth/logout")
                                .logoutSuccessHandler(logoutSuccessHandler)
                )
                // 认证管理
                .authorizeHttpRequests(
                        authorizeRequests ->
                                // swagger文件
                                authorizeRequests
                                        // swagger 文档
                                        .requestMatchers(
                                                "/swagger-ui.html",
                                                "/swagger-ui/**",
                                                "/swagger-resources/**",
                                                "/webjars/**",
                                                "/api-docs/**",
                                                "/*/api-docs/**"
                                        ).permitAll()
                                        // 静态资源
                                        .requestMatchers(
                                                HttpMethod.GET,
                                                "/*.html",
                                                "/*/*.html",
                                                "/*/*.css",
                                                "/*/*.js",
                                                "/css/**",
                                                "/js/**",
                                                "/images/**",
                                                "/webjars/**",
                                                "/*/favicon.ico"
                                        ).permitAll()
                                        // 对于login开放
                                        .requestMatchers("/auth/login").permitAll()
                                        //vue options请求开放
                                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                        //文件开放
                                        .requestMatchers("/file/**", "/avatar/**").permitAll()
                                        //匿名访问
                                        .requestMatchers(anonymousUrl.get(RequestMethodEnum.ALL).toArray(new String[0])).permitAll()
                                        .requestMatchers(HttpMethod.GET, anonymousUrl.get(RequestMethodEnum.GET).toArray(new String[0])).permitAll()
                                        .requestMatchers(HttpMethod.HEAD, anonymousUrl.get(RequestMethodEnum.HEAD).toArray(new String[0])).permitAll()
                                        .requestMatchers(HttpMethod.POST, anonymousUrl.get(RequestMethodEnum.POST).toArray(new String[0])).permitAll()
                                        .requestMatchers(HttpMethod.PUT, anonymousUrl.get(RequestMethodEnum.PUT).toArray(new String[0])).permitAll()
                                        .requestMatchers(HttpMethod.PATCH, anonymousUrl.get(RequestMethodEnum.PATCH).toArray(new String[0])).permitAll()
                                        .requestMatchers(HttpMethod.DELETE, anonymousUrl.get(RequestMethodEnum.DELETE).toArray(new String[0])).permitAll()
                                        .requestMatchers(HttpMethod.OPTIONS, anonymousUrl.get(RequestMethodEnum.OPTIONS).toArray(new String[0])).permitAll()
                                        .requestMatchers(HttpMethod.TRACE, anonymousUrl.get(RequestMethodEnum.TRACE).toArray(new String[0])).permitAll()
                                        // 其他请求需要认证
                                        .anyRequest().authenticated()
                )
                // 添加jwt过滤器
                .addFilterBefore(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // 解析JWT，重新设置上下文
                .addFilterBefore(jwtTokenAuthenticationFilter, LogoutFilter.class)

        ;
        return http.build();
    }

    /**
     * 认证管理
     *
     * @param configuration .
     * @return .
     * @throws Exception .
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * 密码编码器
     *
     * @return .
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordUtil.defaultPasswordEncoder();
    }

    /**
     * 权限默认前缀
     *
     * @return .
     */
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }


    private Map<RequestMethodEnum, Set<String>> getAnonymousUrl() {
        Map<RequestMethodEnum, Set<String>> map = new java.util.HashMap<>(9);
        Set<String> all = new java.util.HashSet<>();
        Set<String> get = new java.util.HashSet<>();
        Set<String> head = new java.util.HashSet<>();
        Set<String> post = new java.util.HashSet<>();
        Set<String> put = new java.util.HashSet<>();
        Set<String> patch = new java.util.HashSet<>();
        Set<String> delete = new java.util.HashSet<>();
        Set<String> options = new java.util.HashSet<>();
        Set<String> trace = new java.util.HashSet<>();
        RequestMappingHandlerMapping mappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        //获取所有的url映射
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> methodEntry : handlerMethods.entrySet()) {
            HandlerMethod method = methodEntry.getValue();
            AnonymousAccess anonymousAccess = method.getMethodAnnotation(AnonymousAccess.class);
            if (null == anonymousAccess) {
                continue;
            }
            Set<String> patterns = methodEntry.getKey().getPatternValues();
            RequestMethodEnum requestMethod = getRequestMethod(methodEntry.getKey().getMethodsCondition().getMethods());
            switch (requestMethod) {
                case ALL -> all.addAll(patterns);
                case GET -> get.addAll(patterns);
                case HEAD -> head.addAll(patterns);
                case POST -> post.addAll(patterns);
                case PUT -> put.addAll(patterns);
                case PATCH -> patch.addAll(patterns);
                case DELETE -> delete.addAll(patterns);
                case OPTIONS -> options.addAll(patterns);
                case TRACE -> trace.addAll(patterns);
            }
        }
        map.put(RequestMethodEnum.ALL, all);
        map.put(RequestMethodEnum.GET, get);
        map.put(RequestMethodEnum.HEAD, head);
        map.put(RequestMethodEnum.POST, post);
        map.put(RequestMethodEnum.PUT, put);
        map.put(RequestMethodEnum.PATCH, patch);
        map.put(RequestMethodEnum.DELETE, delete);
        map.put(RequestMethodEnum.OPTIONS, options);
        map.put(RequestMethodEnum.TRACE, trace);
        return map;
    }

    private RequestMethodEnum getRequestMethod(Set<RequestMethod> methods) {
        if (methods.isEmpty()) {
            return RequestMethodEnum.ALL;
        }
        RequestMethodEnum requestMethod = RequestMethodEnum.ALL;
        for (RequestMethod method : methods) {
            requestMethod = switch (method) {
                case GET -> RequestMethodEnum.GET;
                case HEAD -> RequestMethodEnum.HEAD;
                case POST -> RequestMethodEnum.POST;
                case PUT -> RequestMethodEnum.PUT;
                case PATCH -> RequestMethodEnum.PATCH;
                case DELETE -> RequestMethodEnum.DELETE;
                case OPTIONS -> RequestMethodEnum.OPTIONS;
                case TRACE -> RequestMethodEnum.TRACE;
                default -> RequestMethodEnum.ALL;
            };
        }
        return requestMethod;
    }

    /**
     * 请求方法
     */
    enum RequestMethodEnum {
        ALL, GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE
    }

}