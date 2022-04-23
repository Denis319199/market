package com.db.app.configuration;

import com.db.app.auth.JwtFilter;
import com.db.app.configuration.properties.CorsProperties;
import com.db.app.configuration.properties.UnsecuredEndpointsProperties;
import com.db.service.JwtService;
import com.db.service.UsersService;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
  private final UnsecuredEndpointsProperties unsecuredEndpointsProperties;
  private final CorsProperties corsProperties;

  private UserDetailsService userDetailsService;
  private  UsersService usersService;

  @Autowired
  @Lazy
  void setUserDetailsService(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Autowired
  @Lazy
  void setUsersService(UsersService usersService) {
    this.usersService = usersService;
  }

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
        .antMatchers(unsecuredEndpointsProperties.getEndpoints())
        .permitAll()
        .antMatchers(HttpMethod.GET, unsecuredEndpointsProperties.getGetMethodEndpoints())
        .permitAll()
        .antMatchers(unsecuredEndpointsProperties.getSwaggerPaths())
        .permitAll()
        .anyRequest()
        .authenticated();

    http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  JwtFilter jwtFilter() {
    return new JwtFilter(jwtService, usersService, unsecuredEndpointsProperties);
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

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider());
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }
}
