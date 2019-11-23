package sample;

import connectivity.ConnectionClass;
import connectivity.DeleteDatabase;
import connectivity.DisplayDatabase;
import connectivity.QueryDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderSceneController {


    public DatePicker oDate;
    public TextField cName;
    public TextField pLoc;
    public TextField dLoc;
    public TextField oAmount;
    public Button submitBtn;
    public Label warnMsg;
    public ComboBox<String> cTNumList;
    public TableView<?> oTableView;

    LocalDate date;
    String tNumList;
    String custName;
    String pLocation;
    String dLocation;
    double amount;

    ObservableList<String> taxiList = FXCollections.observableArrayList();
    DisplayDatabase busTableData = new  DisplayDatabase();
    DeleteDatabase deleteDatabase = new DeleteDatabase();

    int id;
    boolean update = false;


    private boolean getOFields() {
        date = oDate.getValue();
        tNumList = cTNumList.getValue();
        custName = cName.getText();
        pLocation = pLoc.getText();
        dLocation = dLoc.getText();
        try{
            Integer.parseInt(oAmount.getText());
        }catch(NumberFormatException e){
            warnMsg.setText("only number are required");
            oAmount.requestFocus();
            return false;
        }
        String amt = oAmount.getText();



        if (date == null) {
            warnMsg.setText("Pls select a Date.");
            oDate.requestFocus();
            return false;
        }

        if (tNumList == null || tNumList.isEmpty()) {
            warnMsg.setText("Pls select a Taxi.");
            cTNumList.requestFocus();
            return false;
        }

        if (custName == null || custName.isEmpty()) {
            warnMsg.setText("Pls select a Taxi.");
            cName.requestFocus();
            return false;
        }
        if (pLocation == null || pLocation.isEmpty()) {
            warnMsg.setText("Pls enter the PickUp Location.");
            pLoc.requestFocus();
            return false;
        }

        if (dLocation == null || dLocation.isEmpty()) {
            warnMsg.setText("Pls enter the drop Location.");
            dLoc.requestFocus();
            return false;
        }

        if (amt == null || amt.isEmpty()) {
            warnMsg.setText("Pls enter Amount.");
            oAmount.requestFocus();
            return false;
        } else {
            amount = Double.parseDouble(amt);
        }
        if (amount <= 0) {
            warnMsg.setText("Amount cannot be less then or equal to zero.");
            oAmount.requestFocus();
            return false;
        }
        warnMsg.setText("");
        return true;
    }


    private void resetFields() {

        warnMsg.setText("");
        oDate.setValue(null);
        cTNumList.setValue("");
        taxiList.clear();
        cName.clear();
        pLoc.clear();
        dLoc.clear();
        oAmount.clear();
        submitBtn.setText("Submit");

    }

    public void checkAvail(ActionEvent actionEvent) {
        LocalDate date = oDate.getValue();
        QueryDatabase queryDatabase = new QueryDatabase();
        if(date!=null){
            taxiList.clear();
            ResultSet rs= queryDatabase.query("select Number from taxitable where Number not in (Select TaxiNum from ordertable where date='"+date+"');");
            if(rs!=null){
                try {
                    while(rs.next()){
                        taxiList.add(rs.getString(1));
                    }
                    if(taxiList.isEmpty()){
                        warnMsg.setText("All Taxies are reserved on this date.");
                        return;
                    }else{
                        warnMsg.setText("");
                    }
                    cTNumList.setItems(taxiList);
                } catch (SQLException ex) {
                    Logger.getLogger(OrderSceneController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }else{
                warnMsg.setText("All Taxies are reserved on this date.");
            }
        }

    }



    public void submitOrder(ActionEvent actionEvent) {

            if (!getOFields()) {
                return;
            } else {
                Connection c;
                ConnectionClass connectionClass = new ConnectionClass();
                try {
                    c = connectionClass.getConnection();
                    if (update==false){
                        String sql = "insert into transactiontable (Date,Type,Description,Amount) values ('" + date + "','Credit','Booking of customer" + custName + "','" + amount + "');";
                        PreparedStatement preparedStmt = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                        preparedStmt.execute();
                        ResultSet rs1 = preparedStmt.getGeneratedKeys();
                        rs1.next();
                        int TId = Integer.parseInt(rs1.getString(1));
                        update =false;
                        sql = "Insert into ordertable (Date, TaxiNum,Customer_Name,Pickup_add,Destination_Add,Amount,TId) Values ('" + date + "','" + tNumList + "','" + custName + "','" + pLocation + "','" + dLocation + "','" + amount + "','" + TId + "');";
                        c.createStatement().execute(sql);
                        c.close();
                    }else{
                        String query = "Update ordertable set Date='"+date+"',TaxiNum='"+tNumList+"',Customer_Name='"+custName+"',Pickup_add='"+pLocation+"',Destination_Add='"+dLocation+"',Amount='"+amount+"' Where Id='"+id+"';";
                        c.createStatement().execute(query);
                        c.close();
                    }




                } catch (SQLException ex) {
                    Logger.getLogger(OrderSceneController.class.getName()).log(Level.SEVERE, null, ex);
                }

                resetFields();
                busTableData.buildData(oTableView, "Select * from ordertable Order By(Id) desc;");
            }


    }

    public void checkDate(ActionEvent actionEvent) {
        if(oDate.getValue()==null){
            oDate.requestFocus();
            warnMsg.setText("Kindly select a date first.");
        }else{
            warnMsg.setText("");
        }
    }

    public void deleteOrder(ActionEvent actionEvent) {
        int index = oTableView.getSelectionModel().getSelectedIndex();
        ObservableList data = busTableData.getData();
        ObservableList<String> items = (ObservableList) data.get(index);
        deleteDatabase.deleteRecord(Integer.parseInt(items.get(0)), "ordertable");

        busTableData.buildData(oTableView, "Select * from ordertable Order By(Id) desc;");
    }

    public void updateOrder(ActionEvent actionEvent) {
        int index = oTableView.getSelectionModel().getFocusedIndex();
        ObservableList<ObservableList> data = busTableData.getData();
        ObservableList<String> itemData = data.get(index);

        id = Integer.parseInt(itemData.get(0));

        //oDate.setValue(LocalDate.parse(itemData.get(1)));
        cTNumList.setValue(itemData.get(2));
        cName.setText(itemData.get(3));
        pLoc.setText(itemData.get(4));
        dLoc.setText(itemData.get(5));
        oAmount.setText(itemData.get(6));

        busTableData.buildData(oTableView, "Select * from ordertable Order By(Id) desc;");
        update=true;
        submitBtn.setText("Update");
    }

    public boolean numberOrNot(String input)
    {
        try {

            Integer.parseInt(input);
        }catch (NumberFormatException e)
        {
            warnMsg.setText("only number are allowed");
            return false;
        }
        return true;
    }
}
