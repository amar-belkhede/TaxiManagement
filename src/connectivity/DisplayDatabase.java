package connectivity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;

public class DisplayDatabase {

    public ObservableList<ObservableList> getData() {
        return data;
    }
    //Tableview and data
    private  ObservableList<ObservableList> data;




    //Connection database
    public  void buildData(TableView tableview, String SQL){
        if(!tableview.getColumns().isEmpty())
            tableview.getColumns().clear();
        Connection c ;
        ConnectionClass connectionClass = new ConnectionClass();

        data = FXCollections.observableArrayList();
        //  data.clear();
        //  data.removeAll(data);

        try{
            c = connectionClass.getConnection();
            //  String SQL = "SELECT * from " + tableName;
            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);



            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));

                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });


                // Add all columns to tableview

                tableview.getColumns().addAll(col);


            }


            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column

                    row.add(rs.getString(i));

                }

                //System.out.println("Row [1] added "+row );
                data.add(row);

            }
            //FINALLY ADDED TO TableView
            tableview.setItems(data);


        }catch(Exception e){
            System.out.println("Error on Building Data");
        }
    }
}
