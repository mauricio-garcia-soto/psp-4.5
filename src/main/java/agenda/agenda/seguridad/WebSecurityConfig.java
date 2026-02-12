package agenda.agenda.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableWebSecurity
@Configuration
class WebSecurityConfig{
    @Autowired
    JWTAuthorizationFilter jwtAuthorizationFilter;
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws
            Exception {
        http
                .csrf((csrf) -> csrf
                        .disable())
                .authorizeHttpRequests( authz -> authz
                        .requestMatchers(HttpMethod.POST,Constans.LOGIN_URL).permitAll()
                        .anyRequest().authenticated())
                .addFilterAfter(jwtAuthorizationFilter,
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
