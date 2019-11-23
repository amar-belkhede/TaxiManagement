package connectivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueryDatabase {

    static ResultSet rs;
    Connection c;
    ConnectionClass connectionClass = new ConnectionClass();

    public  ResultSet query(String q){
        try {
            c = connectionClass.getConnection();
            rs = c.createStatement().executeQuery(q);


        } catch (SQLException ex) {
            Logger.getLogger(QueryDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rs;
    }


}
