package services;
import utils.PasswordHasher;
import controllers.RegistreController;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import utils.MyDatabase;
import java.sql.*;
import java.sql.Connection;
import java.sql.SQLException;

public class ServiceRegistre {
    private Connection connection;
    private RegistreController registreController;

    public ServiceRegistre(RegistreController registreController) {
        connection = MyDatabase.getInstance().getConnection();
        this.registreController = registreController;
    }

    public boolean registerUser(String nom, String prenom, String email, String mdp, String confMdp, String ville, String tel, String genre, String nomFichierImage) {
        if (!validateFields(nom, prenom, email, mdp, confMdp, ville, tel, genre)) {
            return false;
        }

        String mdpHash = PasswordHasher.hashPassword(mdp);

        try {
            String sql = "INSERT INTO utilisateur (nom, prenom, email, mdp, ville, tel, genre, image, role_id, active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, nom);
            st.setString(2, prenom);
            st.setString(3, email);
            st.setString(4, mdpHash);
            st.setString(5, ville);
            st.setString(6, tel);
            st.setString(7, genre);
            st.setString(8, nomFichierImage != null ? nomFichierImage : null);
            st.setInt(9, 1);
            st.setBoolean(10, true);

            int rowsAffected = st.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean validateFields(String nom, String prenom, String email, String mdp, String confMdp, String ville, String tel, String genre) throws IllegalArgumentException {
        boolean isValid = true;

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty() || confMdp.isEmpty() || ville.isEmpty() || tel.isEmpty() || genre.isEmpty()) {
            throw new IllegalArgumentException("Tous les champs obligatoires doivent être remplis.");
        }

        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Adresse email invalide.");
        }

        if (emailExists(email)) {
            throw new IllegalArgumentException("Adresse email déjà utilisée.");
        }

        if (mdp.length() < 6) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères.");
        }

        if (!mdp.equals(confMdp)) {
            throw new IllegalArgumentException("Le mot de passe et sa confirmation ne correspondent pas.");
        }

        if (!isValidPhoneNumber(tel)) {
            throw new IllegalArgumentException("Numéro de téléphone doit contenir au moins 8 caractères.");
        }

        return isValid;
    }
    private boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    private boolean emailExists(String email) {
        try {
            String sql = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }}

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.length() >= 8 && phoneNumber.matches("[0-9]+");
    }


}
