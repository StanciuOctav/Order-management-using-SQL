package dataAccessLayer;

import connection.ConnectionFactory;
import model.Orders;
import model.Products;

import java.sql.*;
import java.util.ArrayList;

/**
 * @author Stanciu Ioan-Octavian 30223
 */
public class OrdersDAO extends AbsractDAO<Orders> {
    public OrdersDAO() {
    }

    /**
     * <p>
     * Returneaza toate comenzile ce contin clientul cu idClient
     * </p>
     *
     * @param idClient id-ul clientului
     * @return ArrayList<Orders>
     * @throws SQLException se aruncao exceptie de tip SQL
     */
    public ArrayList<Orders> returnOrdersClient(int idClient) throws SQLException {
        ArrayList<Orders> orders = new ArrayList<>();
        Connection co = null;
        PreparedStatement s = null;
        ResultSet rs = null;
        String query = "SELECT * FROM orders WHERE idclients =?";
        try {
            co = ConnectionFactory.getConnection();
            s = co.prepareStatement(query);
            s.setInt(1, idClient);
            rs = s.executeQuery();
            while (rs.next()) {
                orders.add(new Orders(Integer.parseInt(rs.getString(1)), Integer.parseInt(rs.getString(2)), Integer.parseInt(rs.getString(3)), Integer.parseInt(rs.getString(4))));
            }
            return orders;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(s);
            ConnectionFactory.close(co);
        }
        return null;
    }

    /**
     * <p>
     * Returneaza toate comenzile ce contin produsul cu id-ul idProduct
     * </p>
     *
     * @param idProduct id-ul produsului
     * @return ArrayList<Orders>
     * @throws SQLException se arunca exceptie de tip SQL
     */
    public ArrayList<Orders> returnOrdersProduct(int idProduct) throws SQLException {
        ArrayList<Orders> orders = new ArrayList<>();
        Connection co = null;
        PreparedStatement s = null;
        ResultSet rs = null;
        String query = "SELECT * FROM orders WHERE idproducts =?";
        try {
            co = ConnectionFactory.getConnection();
            s = co.prepareStatement(query);
            s.setInt(1, idProduct);
            rs = s.executeQuery();
            while (rs.next()) {
                orders.add(new Orders(Integer.parseInt(rs.getString(1)), Integer.parseInt(rs.getString(2)), Integer.parseInt(rs.getString(3)), Integer.parseInt(rs.getString(4))));
            }
            return orders;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(s);
            ConnectionFactory.close(co);
        }
        return null;
    }

    /**
     * <p>
     * Returneaza produsul cu id-ul idProduct
     * </p>
     *
     * @param idProduct Id-ul produsului
     * @return Products
     * @throws SQLException Se arunca o exceptie de tip SQL
     */
    public Products returnSingleProduct(int idProduct) throws SQLException {
        Connection co = null;
        PreparedStatement s = null;
        ResultSet rs = null;
        String query = "SELECT * FROM products WHERE idproducts=?";
        try {
            co = ConnectionFactory.getConnection();
            s = co.prepareStatement(query);
            s.setInt(1, idProduct);
            rs = s.executeQuery();
            while (rs.next()) {
                return new Products(Integer.parseInt(rs.getString(1)), rs.getString(2), Float.parseFloat(rs.getString(3)), Integer.parseInt(rs.getString(4)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(s);
            ConnectionFactory.close(co);
        }
        return null;
    }
}