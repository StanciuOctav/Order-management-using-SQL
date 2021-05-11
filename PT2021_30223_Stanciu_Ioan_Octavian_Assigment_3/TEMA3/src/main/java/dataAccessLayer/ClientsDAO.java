package dataAccessLayer;

import connection.ConnectionFactory;
import model.Clients;

import java.sql.*;

/**
 * @author Stanciu Ioan-Octavian 30223
 */
public class ClientsDAO extends AbsractDAO<Clients> {

    public ClientsDAO() {
    }

    /**
     * <p>
     * Se updateaza campul de clients_total_bill
     * </p>
     *
     * @param id     Id-ul clientului
     * @param tPrice Noul pret
     * @throws SQLException Se arunca o exceptie de tip SQL
     */
    public void updateClientTotalBill(int id, float tPrice) throws SQLException {
        Connection c = null;
        PreparedStatement s = null;
        String query = "UPDATE clients SET clients_total_bill = ? WHERE idclients = ?;";
        try {
            c = ConnectionFactory.getConnection();
            s = c.prepareStatement(query);
            s.setFloat(1, tPrice);
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
