package services;
import iservices.IService;
import entities.Seance;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.util.List;

public class ServiceSeance implements IService<Seance> {


    Connection cnx;

    public ServiceSeance() {
        cnx = MyDatabase.getInstance().getConnection();
    }

    /**
     * @param seance
     * @throws SQLException
     */
    @Override
    public void add(Seance seance) throws SQLException {
        Statement st = cnx.createStatement();
        String query = "insert into seance (id,titre,duree,lien,mot_de_passe,utilisateurs_id, type_seance_id, image)values(NULL, '" + seance.getTitle() + "', '" + seance.getDuree() + "', '" + seance.getLien() + "', '" + seance.getPassword() + "', '" + seance.getUser_id() + "', '" + seance.getType_id() + "', '" + seance.getImage() + "')";
        st.executeUpdate(query);

    }

    @Override
    public List<Seance> read() throws SQLException {

        List<Seance> ls = new ArrayList<Seance>();
        Statement st = cnx.createStatement();
        String req = "select * from seance order by id";
        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            int id = rs.getInt("id");
            String titre = rs.getString("titre");
            String duree = rs.getString("duree");
            String lien = rs.getString("lien");
            String mot_de_passe = rs.getString("mot_de_passe");
            Integer type_seance_id = rs.getInt("type_seance_id");
            Integer utilisateur_id = rs.getInt("utilisateurs_id");
            String image = rs.getString("image");

            Seance p = new Seance(id, titre, duree,
                    lien, mot_de_passe, type_seance_id, utilisateur_id, image);
            ls.add(p);
        }
        return ls;
    }
    @Override
    public void update(Seance t) throws SQLException {

        Statement st = cnx.createStatement();


        String query = "UPDATE `seance` SET `utilisateurs_id` = '" + t.getUser_id() + "',`titre` = '" + t.getTitle()
                + "', `duree` = '" + t.getDuree() + "', `image` = '" + t.getImage()
                + "', `type_seance_id` = '" + t.getType_id() + "', `mot_de_passe` = '" + t.getPassword() +" ', `lien` ='" +t.getLien()
                + "' WHERE `seance`.`id` = " + t.getId() + " ;";
        st.executeUpdate(query);
    }
    @Override
    public void delete(Long id) throws SQLException {

        Statement st =cnx.createStatement();
        String query = "DELETE FROM `seance` WHERE `seance`.`id` = '" + id + "'";
        st.executeUpdate(query);

    }

    @Override
    public void updateOne(int id, Seance seance) throws SQLException {
        String req = "UPDATE `seance` SET `titre`=?,`duree`=?, `lien`,`mot_de_passe`, `type_seance_id`, `utilisateurs_id`=?, `image`=? WHERE `id`=?";


        PreparedStatement st = cnx.prepareStatement(req);


        st.setString(1, seance.getTitle());
        st.setString(2, seance.getDuree());
        st.setString(3, seance.getLien());
        st.setString(4, seance.getPassword());
        st.setInt(5, seance.getType_id());
        st.setInt(6, seance.getUser_id());
        if (seance.getImage() != null && !seance.getImage().isEmpty()) {
            st.setString(7, seance.getImage());
        } else {
            st.setNull(7, Types.VARCHAR);
        }
        st.setInt(8, id);

        st.executeUpdate();
        System.out.println("seance mis à jour !");

    }

    @Override
    public void createOne(Seance t) throws SQLException {
        String qry = "INSERT INTO `seance` (`titre`, `duree`, `lien`, `mot_de_passe`, `type_seance_id`, `utilisateurs_id`, `image` ) VALUES ('" + t.getTitle()+ "','" + t.getDuree() + "','" + t.getLien() + "','" + t.getPassword()  + "','" + 8  + "','" + t.getUser_id()  +"','" + t.getImage()  + "')";
        try {
            Statement stm = cnx.createStatement();
            stm.executeUpdate(qry);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void supprimerOne(int id) {
        try {
            String req = "DELETE FROM `seance` WHERE id = ?";
            PreparedStatement st = cnx.prepareStatement(req);
            st.setInt(1, id);
            st.executeUpdate();
            System.out.println("seance deleted !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Seance getById(int id) {
        try {
            String req = "SELECT * FROM seance WHERE id=?";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String titre = rs.getString("titre");
                String duree = rs.getString("duree");
                String lien = rs.getString("lien");
                String pwd = rs.getString("mot_de_passe");
                Integer type_id = rs.getInt("type_seance_id");
                Integer userId = rs.getInt("utilisateurs_id");
                String image = rs.getString("image");
                return new Seance(titre, duree, lien, pwd, type_id, userId, image);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }



}