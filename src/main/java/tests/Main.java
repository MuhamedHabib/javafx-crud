package tests;

import entities.Seance;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.ServiceSeance;

import java.sql.SQLException;
import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root;
        root = FXMLLoader.load(getClass().getResource("/fxml/OrderedEventList.fxml"));
        //root = FXMLLoader.load(getClass().getResource("/gui/OrderedEventList.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Ordered ListView : Liste des seance");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
        /*testServiceSeance();*/
    }




    private static void testServiceSeance() {
        ServiceSeance serviceSeance = new ServiceSeance();

        // Test adding a Seance
        Seance seanceToAddWithAllParams = new Seance(null, "Title", "00:35:34", "Link", "Password", 1, 2, "Image");
        try {
            serviceSeance.add(seanceToAddWithAllParams);
            System.out.println("Seance added successfully.");
        } catch (SQLException e) {
            System.err.println("Error adding Seance: " + e.getMessage());
        }

        // Test reading Seances
        try {
            List<Seance> seances = serviceSeance.read();
            System.out.println("Seances:");
            for (Seance seance : seances) {
                System.out.println(seance);
            }
        } catch (SQLException e) {
            System.err.println("Error reading Seances: " + e.getMessage());
        }

        // Test updating a Seance
        // Test updating a Seance
        Seance seanceToUpdate = new Seance(3, "Updated Title", "00:35:35", "Updated Link", "Updated Password", 1, 3, "Updated Image");

        try {
            serviceSeance.update(seanceToUpdate);
            System.out.println("Seance updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error updating Seance: " + e.getMessage());
        }

        // Test deleting a Seance
        long seanceIdToDelete = 1;
        try {
            serviceSeance.delete(seanceIdToDelete);
            System.out.println("Seance deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error deleting Seance: " + e.getMessage());
        }
    }
}