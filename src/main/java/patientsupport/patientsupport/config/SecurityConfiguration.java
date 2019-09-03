package patientsupport.patientsupport.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DataSource dataSource;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .jdbcAuthentication()
            .usersByUsernameQuery(usersQuery)
            .authoritiesByUsernameQuery(rolesQuery)
            .dataSource(dataSource)
            .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers("/resources/**", "/static/**", 
            "/css/**", "/js/**", "/img/**", "/fonts/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
        .authorizeRequests()
        .antMatchers("/").permitAll()
        .antMatchers("/login").permitAll()
        .antMatchers("/home").authenticated()
        .antMatchers("/countries/**").hasAuthority("ADMIN")
        .antMatchers("/zones/**").hasAuthority("ADMIN")
        .antMatchers("/cities/**").hasAuthority("ADMIN")
        .antMatchers("/documenttypes/**").hasAuthority("ADMIN")
        .antMatchers("/eventypes/**").hasAuthority("ADMIN")
        .antMatchers("/healthoperatortypes/**").hasAuthority("ADMIN")
        .antMatchers("/insurancetypes/**").hasAuthority("ADMIN")
        .antMatchers("/labtypes/**").hasAuthority("ADMIN")
        .antMatchers("/stages/**").hasAuthority("ADMIN")
        .antMatchers("/status/**").hasAuthority("ADMIN")
        .antMatchers("/statusreasons/**").hasAuthority("ADMIN")
        .antMatchers("/therapies/**").hasAuthority("ADMIN")
        .antMatchers("/departments/**").hasAuthority("ADMIN")
        .antMatchers("/roles/**").hasAuthority("ADMIN")
        .and()
        .csrf().disable()
        .formLogin().loginPage("/login").failureUrl("/login?error")
            .defaultSuccessUrl("/home")
            .usernameParameter("email")
            .passwordParameter("password")
        .and()
        .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/")
        .and()
        //.exceptionHandling().accessDeniedPage("/access-denied.html");
        .exceptionHandling().accessDeniedHandler(accessDeniedHandler());
    }


    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
    
    @Bean
    public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver, SpringSecurityDialect sec) {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.addDialect(sec); // Enable use of "sec"
        return templateEngine;
    }
}