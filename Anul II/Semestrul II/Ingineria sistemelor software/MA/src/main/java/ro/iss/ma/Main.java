package ro.iss.ma;

import ro.iss.ma.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ro.iss.ma.repository.AngajatRepo;
import ro.iss.ma.repository.SarcinaRepo;
import ro.iss.ma.repository.SefRepo;
import ro.iss.ma.service.Service;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception
    {
        String url="jdbc:postgresql://localhost:5432/MonitorizareAngajatiISS";
        String username = "postgres";
        String password = "postgres";
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("/hello-view.fxml")); //URL
        AnchorPane root = loader.load();
        LoginController controller = loader.getController();
        controller.setService(  new Service(new SefRepo(url, username, password), new AngajatRepo(url, username, password), new SarcinaRepo(url,username,password))); // replace with actual service initialization
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.setResizable(false);
        controller.setStage(stage);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
