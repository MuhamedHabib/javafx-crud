package utils;
import org.mindrot.jbcrypt.BCrypt;


public class PasswordHasher {
    public static String hashPassword(String password) {
        // Génère un hash bcrypt pour le mot de passe avec un sel par défaut
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(13));
        // Remplace l'identifiant de version $2a$ par $2y$
        if (hashed.startsWith("$2a$")) {
            hashed = "$2y$" + hashed.substring(4);
        }
        return hashed;
    }

}
