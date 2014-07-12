package poker

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import poker.repository.PlayerRepository
import poker.service.PokerUserDetailsService

/**
 * Created by matt on 05/07/2014.
 */
@Configuration
@EnableWebMvcSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    PlayerRepository playerRepository

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity
            .ignoring()
                .antMatchers("/players/**")
                .antMatchers("/js/**", "/css/**")
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login").permitAll()
                .and()
            .logout().permitAll()
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new PokerUserDetailsService(playerRepository);
    }
}