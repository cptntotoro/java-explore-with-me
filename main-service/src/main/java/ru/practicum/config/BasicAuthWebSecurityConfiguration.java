package ru.practicum.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class BasicAuthWebSecurityConfiguration {

    @Autowired
    private AppBasicAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                // Доступ только для не зарегистрированных пользователей
                .antMatchers("/sign-up", "/login").not().fullyAuthenticated()
                // Доступ только для пользователей с ролью Администратор
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/index2").hasRole("USER")
//                .antMatchers("/news").hasRole("USER")
                // Доступ разрешен всем пользователей
                .antMatchers("/", "/resources/**", "/css/**", "/images/**").permitAll()
                // Все остальные страницы требуют аутентификации
                .anyRequest().authenticated()
                .and()
                // Настройка для входа в систему
                .formLogin()
                .loginPage("/login")
                // Перенаправление на главную страницу после успешного входа
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .logoutSuccessUrl("/");

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
