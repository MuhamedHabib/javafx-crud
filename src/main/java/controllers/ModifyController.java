package controllers;

import entities.Seance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.ServiceSeance;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModifyController implements Initializable {

    public Button image;
    @FXML
    private Button update;
    @FXML
    private Button cancel;
    @FXML
    private TextField titre;
    @FXML
    private TextField motdepasse;
    @FXML
    private TextField duree;
    @FXML
    private TextField lien;

    @FXML
    private TextField id_type;
    @FXML
    private Text textimage;
    @FXML
    private TextField id;
    @FXML
    private TextField id_user;
    @FXML
    private ImageView imageView;
    private int SeanceId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void initData(int sid) {
        this.SeanceId = sid;
        ServiceSeance serviceEvent = new ServiceSeance();
        Seance seance = serviceEvent.getById(sid);

        id.setText(Integer.toString(sid));
        textimage.setText(seance.getImage());
        id_user.setText(Integer.toString(seance.getUser_id()));
        id_type.setText(Integer.toString(seance.getType_id()));
        titre.setText(seance.getTitle());
        lien.setText(seance.getLien());
        duree.setText(seance.getDuree());

        System.out.println(sid);
    }

    @FXML
    private void update(ActionEvent event) {
        ServiceSeance srec = new ServiceSeance();
        String image = textimage.getText();
        Integer id_user = Integer.parseInt(this.id_user.getText());
        Integer id_typ = Integer.parseInt(this.id_type.getText());
        String dure = duree.getText();
        String path = lien.getText();
        String title = titre.getText();
        String pwd = motdepasse.getText();
        String idStr = id.getText();
        int id = Integer.parseInt(idStr);

        Seance seance = new Seance(title, dure, path, pwd, id_typ, id_user, image);
        try {
            srec.updateOne(id, seance);
            System.out.println("updated");

        } catch (SQLException ex) {
            System.out.println("error");

            Logger.getLogger(ModifyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void ADD(ActionEvent event) {
        // Add action implementation here
    }

    @FXML
    private void HomeAction(ActionEvent event) throws IOException {
        Parent root  = FXMLLoader.load(getClass().getResource("/fxml/OrderedEventList.fxml"));
        Stage window =(Stage) cancel.getScene().getWindow();
        window.setScene(new Scene(root, 1000, 680));
    }
}
