package pikachu;
import java.sql.*;

public class ConnectMySQL {
    private String db_url;
    private String username;
    private String password;

    public ConnectMySQL(String db_url, String username, String password) {
        this.db_url = db_url;
        this.username = username;
        this.password = password;
    }
    public Connection ConnectDB(){
        Connection conn = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(db_url, username, password);
        }catch(Exception e){
            e.printStackTrace();
        }
        return conn;
    }
}
