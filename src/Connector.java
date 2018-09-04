
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author nikol
 */
public class Connector {

    private static Connector instance = null;
    private Connection connection;
    private PreparedStatement ps;
    private final String url = "jdbc:mysql://localhost/cs103_pz02";
    private final String user = "root";
    private final String pass = "";
    private String query = "";
    private ObservableList<Warehouse> warehouses = FXCollections.observableArrayList();

    public static Connector getInstance() {
        if (instance == null) {
            instance = new Connector();
        }
        return instance;
    }

    private Connector() {

        try {
            connection = (Connection) DriverManager.getConnection(url, user, pass);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnnection() throws SQLException {
        connection.close();
    }

    public void getAllWarehouses(){
        warehouses.clear();
        query = "SELECT * FROM `warehouse`";
        Statement statement;
        try {
            statement = (Statement) connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                
                int id = resultSet.getInt(1);
                String city = resultSet.getString(2);
                String address = resultSet.getString(3);
                float latitude = resultSet.getFloat(4);
                float longitude = resultSet.getFloat(5);
                int quantity = resultSet.getInt(6);

                Warehouse warehouse = new Warehouse(id, city, address, latitude, longitude, quantity, false);
                warehouses.add(warehouse);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateQuantity(int id, int quantity){
        System.out.println(id);
        query = "UPDATE `warehouse` SET `quantity`= ? WHERE `warehouse`.`id`=?";
        PreparedStatement statement;
        try {
            statement = (PreparedStatement) connection.prepareStatement(query);
            statement.setInt(1, quantity);
            statement.setInt(2, id);
            statement.execute();
            } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(ObservableList<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

}
