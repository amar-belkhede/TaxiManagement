package sample;

import connectivity.ConnectionClass;
import connectivity.DeleteDatabase;
import connectivity.DisplayDatabase;
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

public class ExpenseSceneController {
    public DatePicker eDate;
    public TextField servDescrip;
    public TextField servAmount;
    public TextField tNum;
    public TextField dName;
    public TableView<?> expenseTableView;
    public Button addServBtn;
    public Label warnMsg;

    DisplayDatabase displayExpense = new DisplayDatabase();
    DeleteDatabase deleteDatabase = new DeleteDatabase();


    public void btnAddServ(ActionEvent actionEvent) {


            try {
                LocalDate date = eDate.getValue();
                String sDescrp = servDescrip.getText();
                try{
                    Integer.parseInt(servAmount.getText());
                }catch(NumberFormatException e){
                    servAmount.requestFocus();
                    warnMsg.setText("only number are required");
                    return;

                }
                String sAmount = servAmount.getText();
                String taxiN = tNum.getText();
                String driverN = dName.getText();
                if(date==null ){
                    eDate.requestFocus();
                    warnMsg.setText("please enter date");
                    return;
                }

                if(sDescrp==null || sDescrp.isEmpty()){
                    servDescrip.requestFocus();
                    warnMsg.setText("please enter description");
                    return;
                }
                if(sAmount==null || sAmount.isEmpty()){
                    servAmount.requestFocus();
                    warnMsg.setText("please enter amount");
                    return;
                }
                if(taxiN==null || taxiN.isEmpty()){
                    tNum.requestFocus();
                    warnMsg.setText("please enter taxi number");
                    return;
                }
                if(driverN==null || driverN.isEmpty()){
                    dName.requestFocus();
                    warnMsg.setText("please enter driver name");
                    return;
                }

                Connection c;
                ConnectionClass connectionClass = new ConnectionClass();
                c = connectionClass.getConnection();

                String query = "insert into TransactionTable (date,Type,Description,Amount) values ('"+date+"','Debit','"+sDescrp+"','"+sAmount+"');";
                PreparedStatement ps = c.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
                ps.execute();
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int tID = Integer.parseInt(rs.getString(1));



                query = "insert into ExpenseTable (date,Description,TaxiNum,Driver,Amount,TId) values ('"+date+"','"+sDescrp+"','"+taxiN+"','"+driverN+"','"+sAmount+"','"+tID+"');";
                c.createStatement().execute(query);
                c.close();

                displayExpense.buildData(expenseTableView,"Select * from ExpenseTable Order By(Id) desc;");

                servDescrip.clear();
                servAmount.clear();
                tNum.clear();
                dName.clear();
                dName.requestFocus();


            } catch (SQLException ex) {
                Logger.getLogger(ExpenseSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }


    }

    public void deleteExpense(ActionEvent actionEvent) {
        int index = expenseTableView.getSelectionModel().getSelectedIndex();
        ObservableList data = displayExpense.getData();
        ObservableList<String> items = (ObservableList<String>) data.get(index);
        deleteDatabase.deleteRecord(Integer.parseInt(items.get(0)),"ExpenseTable");
        displayExpense.buildData(expenseTableView,"Select * from ExpenseTable Order By(Id) desc;");

    }
}
