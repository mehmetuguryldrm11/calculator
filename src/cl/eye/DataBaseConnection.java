package cl.eye;

import java.sql.Connection;
import java.sql.DriverManager;
public class DataBaseConnection {
    public Connection databaseLink;

    public Connection getConnection(){
        String databaseName = "muydb";
        String databaseUser = "muy";
        String databasePassword = "putyourpasswordhere";
        String url = "jdbc:mysql://localhost/" + databaseName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return databaseLink;
    }
}
