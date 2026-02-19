package com.sipacademy.stockmanager.configuration;

import com.sipacademy.stockmanager.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationSuccessHandler successHandler) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/superAdmin/**","/profile/**","/home","/about","/contact","/service").permitAll()
                .requestMatchers("/admin/**", "/agent/**", "/role/**", "/actualite/**", "/sitting/**").hasAuthority("SUPERADMIN")
                .requestMatchers("/article/**", "/provider/**").hasAuthority("ADMIN")
                .requestMatchers("/userAgent/**").hasAuthority("AGENT")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login").permitAll()
                .usernameParameter("email") // Utilise "email" pour l'authentification
                .passwordParameter("password")
                .successHandler(successHandler) // Utilise le gestionnaire personnalisé
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout").permitAll()
            );
        return http.build();
    }

    /*
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/superAdmin/**").permitAll()
                .requestMatchers("/admin/**","/agent/**","/role/**","/actualite/**").hasAuthority("SUPERADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login").permitAll()
                .usernameParameter("email") // Utilise "email" pour l'authentification
                .passwordParameter("password")
                .defaultSuccessUrl("/homeSuperAdmin", true)
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout").permitAll()
            );
        return http.build();
    }*/

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
        return (web) -> web.ignoring().requestMatchers("/backoffice/**","/uploads/**","/frontoffice/**");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
}




/*
package com.sipacademy.stockmanager.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {


	 @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .authorizeHttpRequests()
	            .anyRequest().permitAll() // Autoriser toutes les requêtes sans authentification
	            .and()
	            .csrf().disable(); // Désactiver CSRF si nécessaire
	        return http.build();
	    }
	/*
    private final DataSource dataSource;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

	@Value("${spring.queries.roles-query}")
    private String rolesQuery;


    @Autowired
    public SecurityConfiguration(DataSource dataSource, BCryptPasswordEncoder bCryptPasswordEncoder) {
		super();
		this.dataSource = dataSource;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    	http

              .csrf(csrf -> csrf.disable())
              .authorizeHttpRequests(auth -> auth
                      .requestMatchers("/superAdmin/**","/login").permitAll()
                      .requestMatchers("/admin/**","/agent/**","/actualite/**","/home","/role/**").hasAuthority("SUPERADMIN")
                      //.requestMatchers("/article/**").hasAnyAuthority("USER", "SUPERADMIN")

                      // .requestMatchers("/providers/**").hasAuthority("ADMIN")
                      //.requestMatchers("/articles/**").hasAuthority("USER")
                      .anyRequest().authenticated()
                      )

              .formLogin(formLogin -> formLogin
                      .loginPage("/login")
                      .failureUrl("/login?Invalide-connexion")
                      .defaultSuccessUrl("/home",true) // page d'accueil après login avec succès
                      .usernameParameter("email") // paramètres d'authentifications login et password
                      .passwordParameter("password")
                      )

             .logout(logout -> logout
                      .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // route de deconnexion ici /logut
                      .logoutSuccessUrl("/login")
                  );
             return http.build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
        return (web) -> web.ignoring().requestMatchers("/backoffice/**","/uploads/**");
    }

 // laisser l'accès aux ressources



}
*/