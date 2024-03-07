package code;
import java.sql.*;
public class connection{
    public  Connection con;
    public  Statement s;
    public  ResultSet rs;
    public  int highest;
    public static String highestplayer;
    
    public String getHighest(){
        try{
            ConnectMySQL c = new ConnectMySQL("jdbc:mysql://localhost:3306/pikachu", "root", "0398445870");
            con = c.ConnectDB();
            if(con!=null){
                System.out.println("Connected to database");
            }
            s = con.createStatement();
            rs = s.executeQuery("SELECT MAX(score) FROM score");
            if(rs.next()){
                highest = rs.getInt(1);
            }
            rs = s.executeQuery("select name from score where score = (select max(score) from score);");
            if(rs.next()){
                highestplayer = rs.getString(1);
            }
            con.close();
            return highest + " - " + highestplayer;
        }
        catch (SQLException ex) {
            System.out.printf("Fail\n" + ex);
        }
        return null;
    }
    
    public void saveScore(int score, String name){
        try{
            ConnectMySQL c = new ConnectMySQL("jdbc:mysql://localhost:3306/pikachu", "root", "0398445870");
            con = c.ConnectDB();
            if(con!=null){
                System.out.println("Connected to database");
            }
            s = con.createStatement();
            s.executeUpdate("INSERT INTO score(score, name) VALUES(" + score + ",'" + name + "');");
            System.out.println("Lưu điểm thành công: " + name + " " + score);
        }
        catch(SQLException e){
            System.out.printf("Lưu điểm không thành công" + e);
        }   
    }
}