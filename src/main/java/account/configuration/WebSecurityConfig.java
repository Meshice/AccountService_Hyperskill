package account.configuration;

import account.auth.RestAccessDeniedHandler;
import account.auth.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    RestAccessDeniedHandler restAccessDeniedHandler;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().authenticationEntryPoint(restAuthenticationEntryPoint);
        http.exceptionHandling().accessDeniedHandler(restAccessDeniedHandler);
        http.csrf().disable().headers().frameOptions().disable();
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                .antMatchers(HttpMethod.GET, "/api/empl/payment").hasAnyRole("USER", "ACCOUNTANT")
                .mvcMatchers("/api/acct/payments").hasRole("ACCOUNTANT")
                .mvcMatchers("/api/admin/user", "/api/admin/user/*","/api/admin/user/").hasRole("ADMINISTRATOR")
                .mvcMatchers("/api/security/events").hasRole("AUDITOR")
                .mvcMatchers("/api/auth/changepass").authenticated();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


}
