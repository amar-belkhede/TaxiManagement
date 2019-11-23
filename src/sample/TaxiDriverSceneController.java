package sample;

import connectivity.ConnectionClass;
import connectivity.DeleteDatabase;
import connectivity.DisplayDatabase;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaxiDriverSceneController {
    public TextField taxiNum;
    public TextField taxiMake;
    public TextField taxiModel;
    public Button addTaxiBtn;
    public TableView<?> taxiTableView;
    public Label warnMsg;

    DisplayDatabase displayTaxi = new DisplayDatabase();
    DeleteDatabase deleteDatabase = new DeleteDatabase();
    int id;
    boolean update = false;



    public void addTaxi(ActionEvent actionEvent) {


        try {
            String number = taxiNum.getText();
            String make = taxiMake.getText();
            String model = taxiModel.getText();

            if(number==null || number.isEmpty()){
                taxiNum.requestFocus();
                warnMsg.setText("please enter taxi number");
                return;
            }

            if(make==null || make.isEmpty()){
                taxiMake.requestFocus();
                warnMsg.setText("please enter taxi make");
                return;
            }
            if(model==null || model.isEmpty()){
                taxiModel.requestFocus();
                warnMsg.setText("please enter taxi model");
                return;
            }

            Connection c;
            ConnectionClass connectionClass = new ConnectionClass();
            c = connectionClass.getConnection();
            if(update==false){
                String query = "insert into TaxiTable (Number,Make,Model) values ('" + number + "','" + make + "','" + model + "');";
                c.createStatement().execute(query);
                c.close();
            }else{
                String query = "Update TaxiTable set Number='"+number+"',Make='"+make+"',Model='"+model+"' Where Id='"+id+"';";
                c.createStatement().execute(query);
                c.close();
                update=false;
            }


            taxiNum.clear();
            taxiMake.clear();
            taxiModel.clear();
            addTaxiBtn.setText("Add Taxi");

            displayTaxi.buildData(taxiTableView, "Select * from taxiTable Order By(Id) desc;");
            taxiNum.requestFocus();


        } catch (SQLException ex) {
            Logger.getLogger(TaxiDriverSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }


    }



    @FXML
    private void deleteTaxi(ActionEvent event) {
        int index = taxiTableView.getSelectionModel().getSelectedIndex();
        ObservableList data = displayTaxi.getData();
        ObservableList<String> items = (ObservableList) data.get(index);
        deleteDatabase.deleteRecord(Integer.parseInt(items.get(0)), "TaxiTable");

        displayTaxi.buildData(taxiTableView, "Select * from TaxiTable Order By(Id) desc;");
    }

    public void updateTaxi(ActionEvent actionEvent) {
        int index = taxiTableView.getSelectionModel().getFocusedIndex();
        ObservableList<ObservableList> data = displayTaxi.getData();
        ObservableList<String> itemData = data.get(index);

        id = Integer.parseInt(itemData.get(0));

        taxiNum.setText(itemData.get(1));
        taxiMake.setText(itemData.get(2));
        taxiModel.setText(itemData.get(3));

        displayTaxi.buildData(taxiTableView, "Select * from TaxiTable Order By(Id) desc;");
        update=true;
        addTaxiBtn.setText("Update");
    }
}
