/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import entities.Seance;
import entities.TypeSeance;
import entities.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mask.RequieredFieldsValidators;
import mask.TextFieldMask;
import services.ServiceSeance;
import utils.MyDatabase;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author user
 */
public class AddController implements Initializable {

    public Button image;
    public Button cancel;
    public TextField lien;
    public TextField password;
    public TextField duree;
    public TextField titre;
    public ComboBox<TypeSeance> cmbIdtype;

    @FXML
    public ComboBox<Utilisateur> cmbIduser;
    private File selectedImageFile;

    @FXML
    private Button add;
    @FXML
    private Text textimage;
    @FXML
    private ImageView imageView1;




    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeComboBox();
        setMask();
        setValidations();
        initializeTypeComboBox();
    }

    private void setMask() {
        TextFieldMask.onlyNumbersAndLettersNotSpaces(password,20);
        TextFieldMask.onlyNumbersAndLettersNotSpaces(lien,20);
        TextFieldMask.onlyLetters(titre,100);

    }
    private void setValidations() {
        /*RequieredFieldsValidators.toTextField(titre);
        RequieredFieldsValidators.toJFXComboBox(cmbIduser);
        RequieredFieldsValidators.toTextField(lien);*/
    }

    @FXML
    private void addimage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (.jpg, *.png)", ".jpg", "*.png");
    fileChooser.getExtensionFilters().add(extFilter);
    
    selectedImageFile = fileChooser.showOpenDialog(null);
  String fileExtension = selectedImageFile.getName().substring(selectedImageFile.getName().lastIndexOf("."));
      /*  File newFile = new File(selectedImageFile.getParent(), uniqueId.toString() + fileExtension);
        selectedImageFile.renameTo(newFile);
        selectedImageFile = newFile;*/
        
        Image image = new Image(selectedImageFile.toURI().toString());
        imageView1.setImage(image);
        String imageName = selectedImageFile.getName();
        textimage.setText(imageName);
    }

    @FXML
 private void add(ActionEvent event) {
    ServiceSeance srec = new ServiceSeance();


    String title = titre.getText();
    if (title == null || title.trim().isEmpty()) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Le titre ne peut pas être vide");
        alert.showAndWait();
        return;
    }
        String dure = duree.getText();
        if (dure == null || dure.trim().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("La duree ne peut pas être vide");
            alert.showAndWait();
            return;
        }
        String path = lien.getText();
        if (path == null || path.trim().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Le lien ne peut pas être vide");
            alert.showAndWait();
            return;
        }
        String pwd = password.getText();
        if (pwd == null || pwd.trim().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Le mot de passe ne peut pas être vide");
            alert.showAndWait();
            return;
        }
    

    
    

    
    String image = (selectedImageFile != null) ? selectedImageFile.getName() : null;
        Integer id_user=AutocompleteComboBox.getComboBoxValue(cmbIduser).getId();
       // Integer id_type=AutocompleteComboBox.getComboBoxValue(cmbIdtype).getId();
    // Get the name of the selected image file if it exists
    Seance seance = new Seance(title,dure,path,pwd, id_user,null, image);
    
    try {
        srec.createOne(seance);
    } catch (SQLException ex) {
        Logger.getLogger(AddController.class.getName()).log(Level.SEVERE, null, ex);
    }
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/OrderedEventList.fxml"));
        Parent root = loader.load();
        OrderedEventList controller = loader.getController();
        controller.initialize(null, null); // Call the initialize method of the controller to refresh the table view
        Scene scene = new Scene(root);
        Stage stage = (Stage) add.getScene().getWindow(); // Get the current stage
        stage.setScene(scene);
        stage.show();
    } catch (IOException ex) {
        Logger.getLogger(OrderedEventList.class.getName()).log(Level.SEVERE, null, ex);
    }
}
    private void initializeComboBox() {
        loadComboBox();
        AutocompleteComboBox.autoCompleteComboBoxPlus(cmbIdtype, (typedText, itemToCompare) -> itemToCompare.toString().toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.toString().equals(typedText));

    }
    private void initializeTypeComboBox() {
        loadTypeComboBox();
        AutocompleteComboBox.autoCompleteComboBoxPlus(cmbIdtype, (typedText, itemToCompare) -> itemToCompare.toString().toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.toString().equals(typedText));

    }
   /* private void setMask() {
        TextFieldMask.onlyDoubleNumbers10Integers(prix_e);
        TextFieldMask.onlyNumbersAndLettersNotSpaces(emp,20);
        TextFieldMask.onlyNumbersAndLettersNotSpaces(nom,20);
        TextFieldMask.onlyLetters(description,100);
        TextFieldMask.onlyLetters(type,100);
    }*/


private void loadComboBox() {
        ArrayList<Utilisateur> list = new ArrayList<>();
        try {
            String sql = "SELECT id, nom FROM utilisateur";
            PreparedStatement preparedStatement = MyDatabase.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstname = resultSet.getString("nom");
                list.add(new Utilisateur(id, firstname));
            }
        } catch (SQLException ex) {
            // Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    ObservableList<Utilisateur> bloglist = FXCollections.observableArrayList(list);
        cmbIduser.setItems(bloglist);
    }

    private void loadTypeComboBox() {
        ArrayList<TypeSeance> list = new ArrayList<>();
        try {
            String sql = "SELECT id, nom_type FROM type_seance";
            PreparedStatement preparedStatement = MyDatabase.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String type = resultSet.getString("nom_type");
                list.add(new TypeSeance(id, type));
            }
        } catch (SQLException ex) {
             //Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ObservableList<TypeSeance> typelist = FXCollections.observableArrayList(list);
        cmbIdtype.setItems(typelist);
    }

    public void retour(ActionEvent event)throws Exception {
        Parent root  = FXMLLoader.load(getClass().getResource("/fxml/OrderedEventList.fxml"));

        Stage window =(Stage) cancel.getScene().getWindow();
        window.setScene(new Scene(root, 920, 885));
    }
}

    

