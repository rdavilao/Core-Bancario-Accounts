package ec.edu.espe.corebancario.accounts;

import ec.edu.espe.corebancario.accounts.security.JwtAuthorizationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
public class AccountsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsApplication.class, args);
    }
    
    @EnableWebSecurity
    @Configuration
    @CrossOrigin(origins = "*")
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable()
                    .addFilterAfter(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
                            "/configuration/security", "/swagger-ui.html", "/webjars/**", "/actuator/**")
                    .permitAll().anyRequest().authenticated();
        }
    }
}
