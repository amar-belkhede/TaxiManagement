package sample;

import connectivity.ConnectionClass;
import connectivity.QueryDatabase;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateAccountSceneController {
    public TextField cLoginId;
    public TextField cPassword;
    public TextField cName;
    public TextField cContact;
    public TextField cAddress;
    public Label warnMsq;

    Connection c;
    ConnectionClass connectionClass = new ConnectionClass();
    QueryDatabase queryDatabase = new QueryDatabase();

    public void addAccount(ActionEvent actionEvent) throws SQLException {
        String loginId = cLoginId.getText();
        String password = cPassword.getText();
        String name = cName.getText();
        String contact = cContact.getText();
        String address = cAddress.getText();

        if(loginId==null || loginId.isEmpty()){
            cLoginId.requestFocus();
            warnMsq.setText("please enter loginId");
            return;
        }

        if(password==null || password.isEmpty()){
            cPassword.requestFocus();
            warnMsq.setText("please enter password");
            return;
        }

        if(name==null || name.isEmpty()){
            cName.requestFocus();
            warnMsq.setText("please enter name");
            return;
        }

        if(contact==null || contact.isEmpty()){
            cContact.requestFocus();
            warnMsq.setText("please enter contact");
            return;
        }

        if(address==null || address.isEmpty()){
            cAddress.requestFocus();
            warnMsq.setText("please enter address");
            return;
        }
        checkAvailable();
        String query = "insert into LoginTable (LoginId, Password, Name, Contact, Address) values ('"+loginId+"','"+password+"','"+name+"','"+contact+"','"+address+"');";
        c=connectionClass.getConnection();
        try {
            c.createStatement().execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void checkAvailable() throws SQLException {
        String loginId = cLoginId.getText();
        ResultSet rs;
        rs = queryDatabase.query("select LoginId from LoginTable where LoginId='"+loginId+"';");
        if(rs.next()){
            warnMsq.setText("LoginId is already taken");
            return;
        }
        else{
            warnMsq.setText(loginId+" is available");
        }
    }

}
