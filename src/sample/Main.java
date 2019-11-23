package sample;

import connectivity.ConnectionClass;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;

public class Main extends Application {
    Connection c;
    ConnectionClass connectionClass = new ConnectionClass();


    @Override
    public void start(Stage primaryStage) throws Exception{
        c=connectionClass.getConnection();
        c.createStatement().execute("update LoginTable set Status='logout' where Status='login';");
        Parent root = FXMLLoader.load(getClass().getResource("MainSceneTwo.fxml"));
        primaryStage.setTitle("Taxi Management System");
        Scene scene = new Scene(root,1000,600);
        primaryStage.setScene(scene);
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
