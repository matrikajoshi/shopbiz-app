package self.edu.shopbiz.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import self.edu.shopbiz.service.impl.UserDetailsServiceImpl;

import static self.edu.shopbiz.security.SecurityConstants.SIGN_UP_URL;

/**
 * Created by mpjoshi on 10/15/19.
 */

@Configuration
@EnableWebSecurity
@Order(1)
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenUtil tokenUtil;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService,
                             BCryptPasswordEncoder bCryptPasswordEncoder,
                             TokenUtil tokenUtil) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenUtil = tokenUtil;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.GET, "/products/**")
                .antMatchers("/images/**", "/error/**", "/error", SIGN_UP_URL, "/resources/**", "/static/**", "/public/**", "/webui/**", "/h2-console/**"
                , "/configuration/**", "/swagger-ui/**", "/swagger-resources/**", "/api-docs", "/api-docs/**", "/actuator/health"
                , "/*.html", "/**/*.html" ,"/**/*.css","/**/*.js","/**/*.png ","/**/*.jpg", "/**/*.gif ", "/**/*.svg", "/**/*.ico", "/**/*.ttf","/**/*.woff","/**/*.otf"
                );

        web.ignoring()
                .antMatchers(HttpMethod.GET, "/products/**")
                .antMatchers( "/images/**", "/error/**", "/error", SIGN_UP_URL, "/resources/**", "/static/**", "/public/**", "/webui/**",
                        "/swagger-ui/**", "/swagger-resources/**", "/api-docs", "/api-docs/**",
                        "/h2-console/**", "/actuator/health");
    }

    @Bean
    public RestAuthenticationEntryPoint restfulServicesEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new MyAuthenticationFailureHandler();
    }

    /**
     * Lets users know that authentication failed
     * @return
     */
    @Bean
    public RestAuthenticationAccessDeniedHandler deniedHandler() {
        return new RestAuthenticationAccessDeniedHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .exceptionHandling()
            .authenticationEntryPoint(restfulServicesEntryPoint())
            .accessDeniedHandler(deniedHandler())
            .and()
            .anonymous().and()
            .csrf().disable()
            .addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class)
            .addFilterBefore(new JWTAuthorizationFilter(tokenUtil), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new JWTAuthenticationFilter("/login",
                            authenticationManager(),
                            tokenUtil,
                            customAuthenticationFailureHandler()),
                            UsernamePasswordAuthenticationFilter.class)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

}
