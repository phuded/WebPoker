package poker

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
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
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/players/**").permitAll()
            .antMatchers("/**").authenticated()

        http
            .formLogin()
            .loginPage("/login").permitAll()
            .and()
            .logout().permitAll()
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())

        /*.inMemoryAuthentication()
                                    .withUser("matt").password("password").roles("USER")
                                    .and()
                                    .withUser("cathy").password("password").roles("USER");*/
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new PokerUserDetailsService(playerRepository);
    }
}