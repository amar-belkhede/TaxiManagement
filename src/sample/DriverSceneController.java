package sample;

import connectivity.ConnectionClass;
import connectivity.DeleteDatabase;
import connectivity.DisplayDatabase;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DriverSceneController {
    public TextField driveName;
    public TextField dContact;
    public TextField driveAdd;
    public Button addDriverBtn;
    public TableView<?> driverTableView;
    public Label errorMsg;

    DisplayDatabase displayDriver =new DisplayDatabase();
    DeleteDatabase deleteDatabase = new DeleteDatabase();

    boolean update = false;
    int id;


    public void addDriver(ActionEvent actionEvent) {


            try {
                String name = driveName.getText();
                String contact = dContact.getText();
                String address = driveAdd.getText();

                if(name==null || name.isEmpty()){
                    driveName.requestFocus();
                    errorMsg.setText("Please enter name");
                    return;
                }

                if(contact==null || contact.isEmpty()){
                    dContact.requestFocus();
                    errorMsg.setText("Please enter contact");
                    return;
                }
                if(address==null || address.isEmpty()){
                    driveAdd.requestFocus();
                    errorMsg.setText("Please enter address");
                    return;
                }

                Connection c;
                ConnectionClass connectionClass = new ConnectionClass();
                c = connectionClass.getConnection();
                if(update == true){



                    String query = "Update drivertable set Name='"+name+"',Contact='"+contact+"',Address='"+address+"' Where Id='"+id+"';";
                    c.createStatement().execute(query);
                    c.close();

                }
                else{
                    String query = "insert into driverTable (Name,contact,address) values ('"+name+"','"+contact+"','"+address+"');";
                    c.createStatement().execute(query);
                    c.close();}

                driveName.clear();
                dContact.clear();
                driveAdd.clear();
                driveName.requestFocus();
                addDriverBtn.setText("Add");
                update = false;
                displayDriver.buildData(driverTableView, "Select * from driverTable Order By(Id) desc;");

            } catch (SQLException ex) {
                Logger.getLogger(DriverSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }


    }

    public void deleteDriver(ActionEvent actionEvent) {
        int index = driverTableView.getSelectionModel().getSelectedIndex();
        ObservableList data = displayDriver.getData();
        ObservableList<String> items = (ObservableList) data.get(index);
        deleteDatabase.deleteRecord(Integer.parseInt(items.get(0)), "DriverTable");

        displayDriver.buildData(driverTableView, "Select * from DriverTable Order By(Id) desc;");
    }

    public void UpdateDriver(ActionEvent actionEvent) {
        int index = driverTableView.getSelectionModel().getFocusedIndex();
        ObservableList<ObservableList> data = displayDriver.getData();
        ObservableList<String> itemData = data.get(index);

        id = Integer.parseInt(itemData.get(0));

        driveName.setText(itemData.get(1));
        dContact.setText(itemData.get(2));
        driveAdd.setText(itemData.get(3));

        displayDriver.buildData(driverTableView, "Select * from DriverTable Order By(Id) desc;");
        update=true;
        addDriverBtn.setText("Update");
    }
}
