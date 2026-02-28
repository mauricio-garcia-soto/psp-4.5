package agenda.agenda.seguridad;

import agenda.agenda.entidades.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
class WebSecurityConfig {

    @Autowired
    JWTAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        // Login accesible para todos
                        .requestMatchers(HttpMethod.POST, Constans.LOGIN_URL).permitAll()

                        // DELETE: solo ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/contactos/**")
                        .hasAuthority("ROLE_" + Usuario.Rol.ADMIN)

                        // POST: ADMIN y USER (VIEWER denegado)
                        .requestMatchers(HttpMethod.POST, "/contactos/**")
                        .hasAnyAuthority(
                                "ROLE_" + Usuario.Rol.ADMIN,
                                "ROLE_" + Usuario.Rol.USER)

                        // PUT: ADMIN y USER (VIEWER denegado)
                        .requestMatchers(HttpMethod.PUT, "/contactos/**")
                        .hasAnyAuthority(
                                "ROLE_" + Usuario.Rol.ADMIN,
                                "ROLE_" + Usuario.Rol.USER)

                        // GET y cualquier otra cosa: cualquier usuario autenticado
                        .anyRequest().authenticated()
                )
                .addFilterAfter(jwtAuthorizationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}