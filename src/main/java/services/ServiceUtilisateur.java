package services;

import entities.Utilisateur;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class  ServiceUtilisateur implements IService<Utilisateur>{
    Connection connection;
    public ServiceUtilisateur(){
        connection= MyDatabase.getInstance().getConnection();

    }
    @Override
    public void ajouter(Utilisateur utilisateur) throws SQLException {
        String req = "INSERT INTO utilisateur (nom, prenom, email, tel, mdp, genre, ville, active, role_id) " +
                "VALUES ('" + utilisateur.getNom() + "', '" + utilisateur.getPrenom() + "', '" + utilisateur.getEmail() + "', '" +
                utilisateur.getTel() + "', '" + utilisateur.getMdp() + "', '" + utilisateur.getGenre() + "', '" +
                utilisateur.getVille() + "', " + utilisateur.isActive() + ", " + utilisateur.getRole_id() + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(req);
        System.out.println("Utilisateur ajouté");
    }

    @Override
    public void modifier(Utilisateur utilisateur) throws SQLException {
        String req = "UPDATE utilisateur SET email=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, utilisateur.getEmail());
        preparedStatement.setInt(2, utilisateur.getId());
        preparedStatement.executeUpdate();
        System.out.println("Utilisateur modifié");
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM utilisateur WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        System.out.println("Utilisateur supprimé");
    }

    @Override
    public List<Utilisateur> afficher() throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String req = "SELECT * FROM utilisateur";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(req);
        while (rs.next()) {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setId(rs.getInt("id"));
            utilisateur.setNom(rs.getString("nom"));
            utilisateur.setPrenom(rs.getString("prenom"));
            utilisateur.setEmail(rs.getString("email"));
            utilisateur.setTel(rs.getString("tel"));
            utilisateur.setMdp(rs.getString("mdp"));
            utilisateur.setGenre(rs.getString("genre"));
            utilisateur.setVille(rs.getString("ville"));
            utilisateur.setActive(true); // Définit active à true par défaut
            utilisateur.setRole_id(rs.getInt("role_id"));
            utilisateurs.add(utilisateur);
        }
        return utilisateurs;
    }

    public Utilisateur trouverParId(int id) throws SQLException {
        String req = "SELECT * FROM utilisateur WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setId(rs.getInt("id"));
            utilisateur.setNom(rs.getString("nom"));
            utilisateur.setPrenom(rs.getString("prenom"));
            utilisateur.setEmail(rs.getString("email"));
            utilisateur.setTel(rs.getString("tel"));
            utilisateur.setMdp(rs.getString("mdp"));
            utilisateur.setGenre(rs.getString("genre"));
            utilisateur.setVille(rs.getString("ville"));
            utilisateur.setActive(true);
            return utilisateur;
        } else {
            return null; // Aucun utilisateur trouvé avec cet ID
        }
    }
}
