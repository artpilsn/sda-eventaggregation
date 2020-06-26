package pl.sdacademy.eventaggregation.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;


    public SecurityConfig(@Qualifier("customUserDetailsService") final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login-user", "/register", "/css/styles.css").permitAll()
                .antMatchers("/h2").permitAll()
                .antMatchers("/**")
                .authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin().loginPage("/login-user").successForwardUrl("/home")
                .and()
                .logout()
                .and()
                .headers().frameOptions().disable();
    }

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return userDetailsService;
    }
}
