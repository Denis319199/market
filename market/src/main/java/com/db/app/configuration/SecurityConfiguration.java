package com.db.app.configuration;

import com.db.app.auth.JwtFilter;
import com.db.app.configuration.properties.CorsProperties;
import com.db.app.configuration.properties.UnsecuredEndpointsProperties;
import com.db.service.JwtService;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final JwtService jwtService;
  private final CorsProperties corsProperties;
  private final UnsecuredEndpointsProperties unsecuredEndpointsProperties;

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.cors()
        .and()
        .csrf()
        .disable()
        .formLogin()
        .disable()
        .logout()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.authorizeRequests()
        .antMatchers(unsecuredEndpointsProperties.getSwaggerPaths())
        .permitAll()
        .antMatchers("/**")
        .permitAll()
        .anyRequest()
        .authenticated();

    http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  JwtFilter jwtFilter() {
    return new JwtFilter(jwtService);
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList(corsProperties.getOrigins()));
    configuration.setAllowedMethods(Arrays.asList(corsProperties.getMethods()));
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(Arrays.asList(corsProperties.getHeaders()));

    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration(corsProperties.getUri(), configuration);
    return source;
  }
}
