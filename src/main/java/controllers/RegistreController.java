package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceRegistre;
import utils.PasswordHasher;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.xml.bind.ValidationException;


public class RegistreController implements Initializable {
    @FXML
    public ImageView logoView;
    @FXML
    public TextField nomView;
    @FXML
    public TextField prenomView;
    @FXML
    public TextField emailView;
    @FXML
    public TextField villeView;
    @FXML
    public TextField telView;
    @FXML
    public RadioButton hommeView;
    @FXML
    public RadioButton femmeView;
    @FXML
    public Button insertView;
    @FXML
    public ImageView iconeView;
    @FXML
    public Button inscrireView;
    @FXML
    public PasswordField confmdpView;
    @FXML
    public PasswordField mdpView;
    private String nomFichierImageSelectionne;

    private boolean hommeSelected = false;
    private boolean femmeSelected = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File logoFile = new File("images/logo.png");
        Image logo = new Image(logoFile.toURI().toString());
        logoView.setImage(logo);
        // Créer un ToggleGroup pour les boutons radio homme et femme
        ToggleGroup genreToggleGroup = new ToggleGroup();
        hommeView.setToggleGroup(genreToggleGroup);
        femmeView.setToggleGroup(genreToggleGroup);

        hommeView.setOnAction(event -> {
            if (hommeView.isSelected()) {
                femmeView.setSelected(false); // Désélectionner l'autre bouton s'il est sélectionné
            }
        });

        femmeView.setOnAction(event -> {
            if (femmeView.isSelected()) {
                hommeView.setSelected(false); // Désélectionner l'autre bouton s'il est sélectionné
            }
        });

        ServiceRegistre serviceRegistre = new ServiceRegistre(this); // Passer une référence à ce contrôleur
        insertView.setOnAction(actionEvent -> selectImage());
        inscrireView.setOnAction(actionEvent -> handleInscription(serviceRegistre)); // Passer le serviceRegistre lors de la gestion de l'inscription
    }
    @FXML
    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            // Afficher l'image sélectionnée dans l'interface utilisateur
            Image image = new Image(selectedFile.toURI().toString());
            iconeView.setImage(image);

            nomFichierImageSelectionne = selectedFile.getName();
            System.out.println("Nom du fichier sélectionné : " + nomFichierImageSelectionne);
        }
    }

    // Méthode pour naviguer vers la vue de connexion
    public void goToLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = (Stage) inscrireView.getScene().getWindow(); // Récupère la fenêtre actuelle
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleInscription(ServiceRegistre serviceRegistre) {
        // Récupérer les valeurs des autres champs
        String nom = nomView.getText();
        String prenom = prenomView.getText();
        String email = emailView.getText();
        String mdp = mdpView.getText();
        String confMdp = confmdpView.getText();
        String ville = villeView.getText();
        String tel = telView.getText();
        String genre = "";

        if (hommeView.isSelected()) {
            genre = "Homme";
        } else if (femmeView.isSelected()) {
            genre = "Femme";
        } else {
            showAlert(Alert.AlertType.WARNING, "Erreur de validation", "Erreur", "Veuillez sélectionner un genre.");
            return; // Sortir de la méthode si aucun genre n'est sélectionné
        }

        try {
            if (nomFichierImageSelectionne == null) {
                if (serviceRegistre.registerUser(nom, prenom, email, mdp, confMdp, ville, tel, genre, null)) {
                    showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Inscription avec succès", "Utilisateur enregistré avec succès dans la base de données.");
                    goToLoginView();
                } else {
                    showAlert(Alert.AlertType.WARNING, "Erreur", "Echec d'inscription ", "Échec de l'enregistrement de l'utilisateur dans la base de données.");
                }
            } else {
                if (serviceRegistre.registerUser(nom, prenom, email, mdp, confMdp, ville, tel, genre, nomFichierImageSelectionne)) {
                    showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Inscription avec succès", "Utilisateur enregistré avec succès dans la base de données.");
                    goToLoginView();
                } else {
                    showAlert(Alert.AlertType.WARNING, "Erreur", "Echec d'inscription ", "Échec de l'enregistrement de l'utilisateur dans la base de données.");
                }
            }
        } catch (IllegalArgumentException e) {
        showAlert(Alert.AlertType.WARNING, "Erreur de validation", "Erreur", e.getMessage());
    }
    }





    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
