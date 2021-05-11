package dataAccessLayer;

import connection.ConnectionFactory;
import model.Products;

import java.sql.*;

/**
 * @author Stanciu Ioan-Octavian 30223
 */
public class ProductsDAO extends AbsractDAO<Products> {

    public ProductsDAO() {
    }

    /**
     * <p>
     * Updateaza field-ul de products_inStock al unui produs ce are id-ul id
     * </p>
     *
     * @param id     Id-ul produsului
     * @param number Noul numar de produse in stoc
     * @throws SQLException Se arunca o exceptie de tip SQL
     */
    public void updateProductInStock(int id, int number) throws SQLException {

        Connection c = null;
        PreparedStatement s = null;
        String query = "UPDATE products SET products_inStock = ? WHERE idproducts = ?;";
        try {
            c = ConnectionFactory.getConnection();
            s = c.prepareStatement(query);
            s.setInt(1, number);
            s.setInt(2, id);
            s.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(s);
            ConnectionFactory.close(c);
        }

    }
}
