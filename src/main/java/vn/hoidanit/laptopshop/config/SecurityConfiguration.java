package vn.hoidanit.laptopshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

import jakarta.servlet.DispatcherType;
import vn.hoidanit.laptopshop.service.CustomUserDetailsService;
import vn.hoidanit.laptopshop.service.UserService;
import vn.hoidanit.laptopshop.service.userinfo.CustomOAuth2UserService;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public UserDetailsService userDetailsService(UserService userService) {
                return new CustomUserDetailsService(userService);// ghi đè lên UserDetailsService bằng
                                                                 // CustomUserDetailsService
                                                                 // (do mình tạo ra để lấy database)
        }

        @Bean
        public DaoAuthenticationProvider authProvider(
                        PasswordEncoder passwordEncoder,
                        UserDetailsService userDetailsService) {

                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
                authProvider.setUserDetailsService(userDetailsService);
                authProvider.setPasswordEncoder(passwordEncoder);
                // authProvider.setHideUserNotFoundExceptions(false); tang tinh bao mat

                return authProvider;
        }

        @Bean
        public AuthenticationSuccessHandler customSuccessHandler() {
                return new CustomSuccessHandler();
        }

        @Bean
        public AuthenticationFailureHandler customFailureHandler() {
                return new CustomOAuth2FailureHandler();
        }

        @Bean
        public SpringSessionRememberMeServices rememberMeServices() {
                SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
                // optionally customize
                rememberMeServices.setAlwaysRemember(true);
                return rememberMeServices;
        }

        @Bean
        SecurityFilterChain filterChain(HttpSecurity http,
                        UserService userService) throws Exception {
                // v6. lamda cho phep viet ngan gon nhu ben duoi
                http
                                .authorizeHttpRequests(authorize -> authorize
                                                .dispatcherTypeMatchers(DispatcherType.FORWARD,
                                                                DispatcherType.INCLUDE)
                                                .permitAll()

                                                .requestMatchers("/", "/login", "/product/**", "/register",
                                                                "/products/**",
                                                                "/client/**", "/css/**", "/js/**", "/images/**")
                                                .permitAll()

                                                .requestMatchers("/admin/**").hasRole("ADMIN")

                                                .anyRequest().authenticated())

                                .oauth2Login(oauth2 -> oauth2.loginPage("/login")
                                                .successHandler(customSuccessHandler())
                                                .failureHandler(customFailureHandler())
                                                .userInfoEndpoint(user -> user
                                                                .userService(new CustomOAuth2UserService(userService))))

                                .sessionManagement((sessionManagement) -> sessionManagement
                                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                                                .invalidSessionUrl("/logout?expired")
                                                .maximumSessions(1)// gioi han 1 tk dang nhap tren bao nhieu thiet bi
                                                .maxSessionsPreventsLogin(false))// nguoi thu 2 da nguoi truoc ra

                                .logout(logout -> logout.deleteCookies("JSESSIONID").invalidateHttpSession(true))

                                .rememberMe(r -> r.rememberMeServices(rememberMeServices()))

                                .formLogin(formLogin -> formLogin
                                                .loginPage("/login")
                                                .failureUrl("/login?error")
                                                .successHandler(customSuccessHandler())
                                                .permitAll())
                                .exceptionHandling(ex -> ex.accessDeniedPage("/access-deny"));

                return http.build();
        }

}
