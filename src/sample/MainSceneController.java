package sample;

import connectivity.ConnectionClass;
import connectivity.QueryDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainSceneController {
    public ToggleButton orderBtn;
    public ToggleButton registerDriverBtn;
    public ToggleButton registerTaxiBtn;
    public ToggleButton expenseBtn;
    public BorderPane rootLayout;
    public ToggleButton loginBtn;
    public Label warnMsg;
    public ToggleButton logoutBtn;
    public ToggleButton createAccountBtn;

    Connection c;
    ConnectionClass connectionClass = new ConnectionClass();


    QueryDatabase queryDatabase = new QueryDatabase();





    public  void changeScene(String scenePath){
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(scenePath));
//            Parent root1 = (Parent) fxmlLoader.load();
//            Stage stage = new Stage();
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.initStyle(StageStyle.UNDECORATED);
//            stage.setTitle("Taxi Management");
//            stage.setScene(new Scene(root1));
//            stage.show();
//        }catch(Exception e) {
//            e.printStackTrace();
//        }

        FXMLLoader loader;
        loader = new FXMLLoader(getClass().getResource(scenePath));
        AnchorPane pane = new AnchorPane();
        try{
            pane = (AnchorPane) loader.load();
            rootLayout.setCenter(pane);
        }
        catch(Exception e){
        }

    }



    public void setOrderScene(ActionEvent actionEvent) throws SQLException {
        ResultSet status = queryDatabase.query("select Status from LoginTable where Status = 'login';");
        if(status.next()) {
            warnMsg.setText("");
            changeScene("OrderScene.fxml");
        }else{warnMsg.setText("please Login");
            changeScene("LoginScene.fxml");
        }
    }

    public void setDriverScene(ActionEvent actionEvent) throws SQLException {
        ResultSet status = queryDatabase.query("select Status from LoginTable where Status = 'login';");
        if(status.next()) {
            warnMsg.setText("");
            changeScene("DriverScene.fxml");
        }else{warnMsg.setText("please Login");
            changeScene("LoginScene.fxml");
        }
    }

    public void setRegisterScene(ActionEvent actionEvent) throws SQLException {
        ResultSet status = queryDatabase.query("select Status from LoginTable where Status = 'login';");
        if(status.next()) {
            warnMsg.setText("");
            changeScene("TaxiDriverScene.fxml");
        }else{warnMsg.setText("please Login");
            changeScene("LoginScene.fxml");
        }
    }

    public void setExpenceScene(ActionEvent actionEvent) throws SQLException {
        ResultSet status = queryDatabase.query("select Status from LoginTable where Status = 'login';");
        if(status.next()){
            warnMsg.setText("");
            changeScene("ExpenseScene.fxml");
        }else{
            warnMsg.setText("please Login");
            changeScene("LoginScene.fxml");
        }
    }

    public void setLoginScene(ActionEvent actionEvent) {
        warnMsg.setText("");
            changeScene("LoginScene.fxml");
    }


    public void setLogoutScene(ActionEvent actionEvent) {
        String queryLogout="update LoginTable set Status='logout' where Status='login';";
        c=connectionClass.getConnection();
        changeScene("LoginScene.fxml");
        warnMsg.setText("Logout successful");
        try {
            c.createStatement().execute(queryLogout);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCreateAccountScene(ActionEvent actionEvent) {
        changeScene("CreateAccountScene.fxml");
    }
}
