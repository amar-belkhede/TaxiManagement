package connectivity;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteDatabase {



    public void deleteRecord(int id, String tableName) {
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();

        String query = "Delete from "+tableName+" where id='"+id+"';";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in building Data");
        }
    }
}