package agenda.agenda.repositorios;

import agenda.agenda.entidades.PasswordEncryptor;
import agenda.agenda.entidades.Usuario;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UsuarioRepository {
    public List<Usuario> getUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario("aitor",
                PasswordEncryptor.encrypt("1234"), Usuario.Rol.ADMIN));
        usuarios.add(new Usuario("alicia",
                PasswordEncryptor.encrypt("1111"), Usuario.Rol.USER));
        usuarios.add(new Usuario("viewer",
                PasswordEncryptor.encrypt("2222"), Usuario.Rol.VIEWER)); // NUEVO USUARIO
        return usuarios;
    }
}