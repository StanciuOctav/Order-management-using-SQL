package connection;

import java.sql.*;
import java.util.logging.Logger;

/**
 * @author Stanciu Ioan-Octavian 30223
 */
public class ConnectionFactory {

    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DBURL = "jdbc:mysql://localhost:3306/tema3";
    private static final String USER = "root";
    private static final String PASS = "12345678";

    private static final ConnectionFactory singleInstance = new ConnectionFactory();

    /**
     * <p>
     * Constructor pentru stabilirea conexiunii cu baza de date
     * </p>
     */
    private ConnectionFactory() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * Crearea conexiunii
     * </p>
     *
     * @return Conexiunea de tip Connection
     * @throws SQLException Se arunca exceptie de tip SQL
     */
    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(DBURL, USER, PASS);
    }

    /**
     * <p>
     * Getter pentru conexiune
     * </p>
     *
     * @return Conexiunea de tip Connection
     * @throws SQLException Se arunca o exceptie de tip SQL
     */
    public static Connection getConnection() throws SQLException {
        return singleInstance.createConnection();
    }

    /**
     * <p>
     * Se inchide conexiunea de tip Connection
     * </p>
     *
     * @param connection Conexiunea ce se inchide
     * @throws SQLException Se arunca exceptie de tip SQL
     */
    public static void close(Connection connection) throws SQLException {
        connection.close();
    }

    /**
     * <p>
     * Se inchide conexiunea unui Statement
     * </p>
     *
     * @param statement Conexiunea ce o sa se inchida
     * @throws SQLException Se arunca exceptie de tip SQL
     */
    public static void close(Statement statement) throws SQLException {
        statement.close();
    }

    /**
     * <p>
     * Se inchide conexiunea unui ResultSet
     * </p>
     *
     * @param resultSet Conexiunea ce o sa se inchida
     * @throws SQLException Se arunca o exceptie de tip SQL
     */
    public static void close(ResultSet resultSet) throws SQLException {
        resultSet.close();
    }

}
