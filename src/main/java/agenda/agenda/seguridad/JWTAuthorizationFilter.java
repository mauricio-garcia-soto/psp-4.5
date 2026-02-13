package agenda.agenda.seguridad;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import static agenda.agenda.seguridad.Constans.*;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String header = request.getHeader(HEADER_AUTHORIZACION_KEY);
            if (header != null && header.startsWith(TOKEN_BEARER_PREFIX)) {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(getSigningKey(SUPER_SECRET_KEY))
                        .build()
                        .parseClaimsJws(header.replace(TOKEN_BEARER_PREFIX, ""))
                        .getBody();

                if (claims.get("authorities") != null) {
                    List<String> authorities = (List<String>) claims.get("authorities");
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            claims.getSubject(), null,
                            authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
    }
}