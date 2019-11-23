package sample;

import connectivity.ConnectionClass;
import connectivity.QueryDatabase;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginSceneController {
    public TextField loginId;
    public TextField password;
    public Label warnMsg;

    QueryDatabase queryDatabase = new QueryDatabase();

    Connection c;
    ConnectionClass connectionClass = new ConnectionClass();


    public void signUp(ActionEvent actionEvent) throws SQLException {

        String id = loginId.getText();
        String pass = password.getText();


        if (id==null || id.isEmpty())
        {
            loginId.requestFocus();
            warnMsg.setText("enter Login Id");
            return;

        }
        if (pass==null || pass.isEmpty())
        {
            password.requestFocus();
            warnMsg.setText("enter password");
            return;
        }


        ResultSet rs = queryDatabase.query( "SELECT * FROM LoginTable Where LoginId='"+id+"' and Password='"+pass+"';");
        if(rs.next()){

            String queryLogin ="update LoginTable set Status='login' where LoginId='"+id+"';";
            String queryLogout = "update LoginTable set Status='logout' where LoginId!='"+id+"';";
            try {
                c = connectionClass.getConnection();
                c.createStatement().execute(queryLogin);
                c.createStatement().execute(queryLogout);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            warnMsg.setText("Login Successful");


        }else{
            warnMsg.setText("Invalid user id / password");
        }

    }

}
