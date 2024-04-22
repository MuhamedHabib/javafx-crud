package controllers;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import entities.Seance;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import services.ServiceSeance;
import utils.MyDatabase;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class OrderedEventList implements Initializable {

    public AnchorPane rootTotalQuotes;
    @FXML
    private TextField messageContentTextField;
    @FXML
    private FlowPane flowpane;
    @FXML
    private Button ajouter;

    @FXML
    private Button ajoutertxtblog;
    @FXML
    private Button export;

    @FXML
    private TextField txtSearchRecent;
    @FXML
    private TextField txtSrch;

    @FXML
    private Label labelTotalUsersInblog;
    @FXML
    private Label labelTotalblogItems;
    @FXML
    private Label labelthisMonthEvents;
    @FXML
    private Label blogItemsbyTitle;

    /**
     * Initializes the controller class.
     */
    private ObservableList<Seance> itemsblog;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        counterRecords();
        getExistingTitlesInblog();
        counterForthisMonthAddedblogItems();
        ServiceSeance cr = new ServiceSeance();


        itemsblog = RecupBase(txtSearchRecent.getText());
        updateUIWithFilteredEvents();
        ajouter.setOnAction(event -> {
            itemsblog = RecupBase(txtSearchRecent.getText());
            updateUIWithFilteredEvents();
        });
        txtSearchRecent.textProperty().addListener((observable, oldValue, newValue) -> {
            itemsblog = RecupBase(newValue);
            updateUIWithFilteredEvents();
        });

        export.setOnAction(event -> {
            createPdfFromObservableList(itemsblog);
        });


        ObservableList<Seance> list = RecupBase();

        FilteredList<Seance> filteredList = new FilteredList<>(list, p -> true);
        txtSrch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(activite -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (activite.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (activite.getDuree().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (activite.getLien().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (activite.getPassword().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
            flowpane.getChildren().clear();
            filteredList.forEach(activite -> {
                VBox vBox = createVBox(activite);
                flowpane.getChildren().add(vBox);
            });
        });

    }

    private void updateUIWithFilteredEvents() {
        flowpane.getChildren().clear();
        for (Seance m : itemsblog) {
            VBox vBox = createVBox(m);
            flowpane.getChildren().add(vBox);
        }
    }


    public static ObservableList<Seance> RecupBase(String filter) {
        ObservableList<Seance> list = FXCollections.observableArrayList();
        java.sql.Connection cnx = MyDatabase.getInstance().getConnection();
        String sql = "SELECT * FROM seance WHERE titre  LIKE ? or duree like ? ORDER BY titre DESC";

        try {
            PreparedStatement st = cnx.prepareStatement(sql);
            st.setString(1, "%" + filter + "%");
            st.setString(2, "%" + filter + "%");
            ResultSet R = st.executeQuery();

            while (R.next()) {
                Seance m = new Seance();
                m.setId(R.getInt("id"));
                m.setTitle(R.getString("titre"));
                m.setDuree(R.getString("duree"));
                m.setLien(R.getString("lien"));
                m.setPassword(R.getString("mot_de_passe"));
                m.setType_id(R.getInt("type_seance_id"));
                m.setUser_id(R.getInt("utilisateurs_id"));
                m.setImage(R.getString("image"));


                list.add(m);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return list;
    }

    public static ObservableList<Seance> RecupBase() {
        ObservableList<Seance> list = FXCollections.observableArrayList();
        java.sql.Connection cnx = MyDatabase.getInstance().getConnection();
        String sql = "SELECT * FROM seance ORDER BY titre DESC";

        try {
            PreparedStatement st = cnx.prepareStatement(sql);

            ResultSet R = st.executeQuery();

            while (R.next()) {
                Seance m = new Seance();
                m.setId(R.getInt("id"));
                m.setTitle(R.getString("titre"));
                m.setDuree(R.getString("duree"));
                m.setLien(R.getString("lien"));
                m.setPassword(R.getString("mot_de_passe"));
                m.setType_id(R.getInt("type_seance_id"));
                m.setUser_id(R.getInt("utilisateurs_id"));
                m.setImage(R.getString("image"));


                list.add(m);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return list;
    }

    private VBox createVBox(Seance m) {
        VBox vBox = new VBox();
        vBox.setPrefSize(800, 100);

        vBox.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px; -fx-padding: 10px;");

        Label title = new Label("titre: " + m.getTitle());
        Label duree = new Label("durée: " + m.getDuree());
        Label lien = new Label("lien: " + m.getLien());
        Label password = new Label("mot de passe: " + m.getPassword());
        Label typeid = new Label("id type seance: " + m.getType_id());
        Label userid = new Label("id utilisateur: " + m.getUser_id());
        String imagePath = "src/main/java/img/" + m.getImage(); // Assumes image is stored in src/img folder
        Image image = new Image(new File(imagePath).toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(250); // set the width to 200 pixels
        imageView.setFitHeight(250); // set the height to 200 pixels


        Button editButton = new Button("Edit");
        editButton.setFont(new Font(20));
        editButton.setTextFill(Color.BLUEVIOLET);
        // Set the button color to blue
        editButton.setStyle("-fx-background-color: blue;");
        // Set the button shape to rounded rectangle
        editButton.setStyle("-fx-background-radius: 10;");


        editButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modifier.fxml"));
                Parent root = loader.load();
                ModifyController controller = loader.getController();
                int EventId = (int) editButton.getUserData(); // Get the message ID from the user data of the edit button

                controller.initData(EventId);

                // Set the preferred size of the root element
                ((Region) root).setPrefSize(630, 680);

                // Set the scene
                Scene scene = ajoutertxtblog.getScene();
                scene.setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        editButton.setUserData(m.getId()); // Set the message ID as user data of the edit button

        Button deleteButton = new Button("Delete");
        deleteButton.setFont(new Font(20));
        deleteButton.setTextFill(Color.INDIANRED);
        // Set the button color to blue
        deleteButton.setStyle("-fx-background-color: blue;");
        // Set the button shape to rounded rectangle
        deleteButton.setStyle("-fx-background-radius: 10;");

        deleteButton.setOnAction(event -> {
            // Create a confirmation dialog box
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure you want to delete this event?");

            // Show the confirmation dialog box and wait for a response
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // User confirmed the deletion, delete the event
                ServiceSeance srec = new ServiceSeance();
                srec.supprimerOne(m.getId());
                ((FlowPane) vBox.getParent()).getChildren().remove(vBox);
            }
        });

        vBox.getChildren().addAll(userid, title, duree, lien, password, imageView, editButton, deleteButton);
        return vBox;
    }

    private void counterRecords() {
        try {
            String sql = "SELECT (SELECT COUNT(*) FROM seance) AS Items, (SELECT COUNT(distinct utilisateurs_id) FROM seance) AS subscribeUsers";
            PreparedStatement preparedStatetent = MyDatabase.getInstance().getConnection().prepareStatement(sql);
            ResultSet rs = preparedStatetent.executeQuery();

            while (rs.next()) {
                labelTotalblogItems.setText(String.valueOf(rs.getInt(1)));
                labelTotalUsersInblog.setText(String.valueOf(rs.getInt(2)));
            }
        } catch (SQLException ex) {
            // Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void counterForthisMonthAddedblogItems() {
        try {
            String sql = "SELECT count(*) FROM reservation WHERE date >= DATE_FORMAT(NOW(), '%Y-%m-01') AND date <= LAST_DAY(NOW());";
            PreparedStatement preparedStatetent = MyDatabase.getInstance().getConnection().prepareStatement(sql);
            ResultSet rs = preparedStatetent.executeQuery();

            while (rs.next()) {
                labelthisMonthEvents.setText(String.valueOf(rs.getInt(1)));
            }
        } catch (SQLException ex) {
            // Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int getExistingTitlesInblog() {
        int count = 0;
        try {
            String sql = "SELECT count(distinct title) FROM blog ;";
            PreparedStatement preparedStatetent = MyDatabase.getInstance().getConnection().prepareStatement(sql);
            ResultSet rs = preparedStatetent.executeQuery();

            while (rs.next()) {
                count = rs.getInt(1);
            }
            blogItemsbyTitle.setText(String.valueOf(count));
        } catch (SQLException ex) {
            //  Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;

    }

    public void createPdfFromObservableList(ObservableList<Seance> blogs) {
        try {
            // Create PDF document
            //b document = new Document(PageSize.A4, 50, 50, 50, 50);
            //PdfWriter.getInstance(document, new FileOutputStream("article_list.pdf"));
            //document.open();

            // Create table
            PdfPTable table = new PdfPTable(3); // 3 columns
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Add table headers
            PdfPCell cell = new PdfPCell(new Paragraph("Article ID"));
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Article Name"));
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Article Price"));
            cell.setPadding(5);
            table.addCell(cell);


            // Add table data
            for (Seance blog : blogs) {
                table.addCell(String.valueOf(blog.getId()));
                table.addCell(blog.getTitle());
                table.addCell(String.valueOf(blog.getLien()));
            }

            // Add table to PDF document
            //document.add(table);

            // Close PDF document
            //document.close();
        } catch (Exception e/*| com.itextpdf.text.DocumentException e*/) {
            e.printStackTrace();
        }

    }


    @FXML
    public void ajouterAction(ActionEvent event) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/add.fxml"));

        javafx.stage.Stage window = (javafx.stage.Stage) ajouter.getScene().getWindow();
        window.setScene(new javafx.scene.Scene(root, 630, 680));
    }

    public void addaction(javafx.scene.input.MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/add.fxml"));

        javafx.stage.Stage window = (javafx.stage.Stage) ajouter.getScene().getWindow();
        window.setScene(new javafx.scene.Scene(root, 630, 680));
    }

    @FXML
    public void ajouterActionblog(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/add.fxml"));

        javafx.stage.Stage window = (javafx.stage.Stage) ajouter.getScene().getWindow();
        window.setScene(new javafx.scene.Scene(root, 630, 680));
    }

    public void homeAction(javafx.scene.input.MouseEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/add.fxml"));//MainView

        javafx.stage.Stage window = (javafx.stage.Stage) ajouter.getScene().getWindow();
        window.setScene(new javafx.scene.Scene(root, 1000, 680));
    }

    public void exportAction(ActionEvent event) {
    }
}
