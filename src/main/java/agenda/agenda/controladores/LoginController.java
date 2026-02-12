package agenda.agenda.controladores;

import agenda.agenda.seguridad.Constans;
import agenda.agenda.seguridad.JWTAuthenticationConfig;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class LoginController {
    @Autowired
    JWTAuthenticationConfig jwtAuthtenticationConfig;
    @PostMapping("login")
    public String login(
            @RequestParam("user") String username,
            @RequestParam("encryptedPass") String encryptedPass)
            throws BadRequestException {
        if(! (username.equals(Constans.USER) &&
                encryptedPass.equals(Constans.PASS))){
            throw new BadRequestException();
        }
        String token =
                jwtAuthtenticationConfig.getJWTToken(username);
        return token;
    }
}
