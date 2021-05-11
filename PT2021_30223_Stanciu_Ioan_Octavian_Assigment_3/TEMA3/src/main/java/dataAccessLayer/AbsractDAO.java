package dataAccessLayer;

import connection.ConnectionFactory;

import javax.swing.table.DefaultTableModel;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

/**
 * @author Stanciu Ioan-Octavian 30223
 */
public class AbsractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbsractDAO.class.getName());
    private final Class<T> type;

    /**
     * Se initializeaza tipul type de tip Class<T>
     */
    @SuppressWarnings("unchecked")
    public AbsractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * <p>
     * Se returneaza query-ul pentru a selecta un rand in functie de coloana
     * </p>
     *
     * @param field Numele coloanei din tabela
     * @return String
     */
    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT");
        sb.append(" * ");
        sb.append("FROM ");
        sb.append(type.getSimpleName().toLowerCase(Locale.ROOT));
        sb.append(" WHERE " + field + " = ?");
        return sb.toString();
    }

    /**
     * <p>
     * Creaza o lista cu toate obiectele din resultSet
     * </p>
     *
     * @param resultSet Unde sunt stocate datele executate de un query
     * @return List<T>
     */
    public List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();

        try {
            while (resultSet.next()) {
                T instance = type.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    Object value = resultSet.getObject(field.getName());
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * <p>
     * Returneaza un obiect dupa id-ul sau
     * </p>
     *
     * @param id Id-ul obiectului
     * @return T
     * @throws Exception Se arunca exceptie
     */
    public T findByID(int id) throws Exception {
        Connection c = null;
        PreparedStatement s = null;
        ResultSet rs = null;
        String query = createSelectQuery("id" + type.getSimpleName().toLowerCase(Locale.ROOT));
        try {
            c = ConnectionFactory.getConnection();
            s = c.prepareStatement(query);
            s.setInt(1, id);
            rs = s.executeQuery();
            return createObjects(rs).get(0);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findByID " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(s);
            ConnectionFactory.close(c);
        }
        return null;
    }

    /**
     * <p>
     * Se sterge obiectul cu id-ul id
     * </p>
     *
     * @param id Id-ul obiectului
     * @throws SQLException Se arunca exceptie de tip SQL
     */
    public void deleteObject(int id) throws SQLException {
        Connection c = null;
        PreparedStatement s = null;
        ResultSet rs = null;
        String query = "DELETE FROM " + type.getSimpleName().toLowerCase(Locale.ROOT) + " WHERE id" + type.getSimpleName().toLowerCase(Locale.ROOT) + " = ?";
        try {
            c = ConnectionFactory.getConnection();
            s = c.prepareStatement(query);
            s.setInt(1, id);
            s.executeUpdate();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findByID " + e.getMessage());
        } finally {
            ConnectionFactory.close(s);
            ConnectionFactory.close(c);
        }
    }

    /**
     * <p>
     * Se creeaza query de insert
     * </p>
     *
     * @return String
     */
    public String stringInsert() {
        String typeName = type.getSimpleName().toLowerCase(Locale.ROOT);
        if (typeName.equals("clients")) {
            return "INSERT INTO clients (idclients, clients_name, clients_CNP, clients_age,clients_total_bill) VALUES (?,?,?,?,?)";
        }
        if (typeName.equals("orders")) {
            return "INSERT INTO orders (idorders, idclients, idproducts, nrProducts) VALUES (?,?,?,?);";
        }
        if (typeName.equals("products")) {
            return "INSERT INTO products (idproducts, products_name, products_price, products_inStock) VALUES (?,?,?,?)";
        }
        return null;
    }

    /**
     * <p>
     * Se seteaza datele ce urmeaza sa fie introduse in tabela in query-ul de insert
     * </p>
     *
     * @param s         PreparedStatemenet
     * @param allFields String[] cu valorile ce trebuie introduse in tabela
     * @return PreparedStatement
     * @throws Exception Se arunca exceptie de tip Exception
     */
    public PreparedStatement setInsertStatementFields(PreparedStatement s, String[] allFields) throws Exception {
        String typeName = type.getSimpleName().toLowerCase(Locale.ROOT);
        if (typeName.equals("clients")) {
            s.setInt(1, newID("clients"));
            s.setString(2, allFields[0]);
            s.setString(3, allFields[1]);
            s.setInt(4, Integer.parseInt(allFields[2]));
            s.setFloat(5, 0f);
            return s;
        }
        if (typeName.equals("orders")) {
            s.setInt(1, newID("orders"));
            s.setInt(2, Integer.parseInt(allFields[0]));
            s.setInt(3, Integer.parseInt(allFields[1]));
            s.setInt(4, Integer.parseInt(allFields[2]));
            return s;
        }
        if (typeName.equals("products")) {
            s.setInt(1, newID("products"));
            s.setString(2, allFields[0]);
            s.setFloat(3, Float.parseFloat(allFields[1]));
            s.setInt(4, Integer.parseInt(allFields[2]));
            return s;
        }
        return null;
    }

    /**
     * <p>
     * Ajuta la determinarea urmatorului id ce trebuie introdus in tabela
     * </p>
     *
     * @param table Numele tabelei unde se face cautarea
     * @return INT
     * @throws Exception Se arunca o exceptie
     */
    public int newID(String table) throws Exception {
        Connection c = null;
        PreparedStatement s = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + table;
        try {
            c = ConnectionFactory.getConnection();
            s = c.prepareStatement(query);
            rs = s.executeQuery();
            int newID = 1;
            while (rs.next()) {
                if (rs.getInt(1) != newID)
                    return newID;
                else
                    newID++;
            }
            return newID;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(s);
            ConnectionFactory.close(c);
        }
        return 0;
    }

    /**
     * <p>
     * Metoda care introduce in tabela obiect
     * </p>
     *
     * @param fields Valorile obiectului ce urmeaza sa fie introdus
     * @throws SQLException Se arunca o exceptie de tip SQL
     */
    public void insertObject(String fields) throws SQLException {
        String[] allFields = fields.split(" ");
        String query = stringInsert();
        Connection c = null;
        PreparedStatement s = null;
        try {
            c = ConnectionFactory.getConnection();
            s = c.prepareStatement(query);
            s = setInsertStatementFields(s, allFields);
            s.executeUpdate();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findByID " + e.getMessage());
        } finally {
            ConnectionFactory.close(s);
            ConnectionFactory.close(c);
        }
    }

    /**
     * <p>
     * Metoda ce construieste query-ul pentru update in tabela
     * </p>
     *
     * @return String
     */
    public String stringUpdate() {
        String typeName = type.getSimpleName().toLowerCase(Locale.ROOT);
        if (typeName.equals("clients")) {
            return "UPDATE clients SET clients_name = ?, clients_CNP = ?, clients_age = ? WHERE idclients = ?";
        }
        if (typeName.equals("products")) {
            return "UPDATE products SET products_name = ?, products_price = ?, products_inStock = ? WHERE idproducts = ?;";
        }
        return null;
    }

    /**
     * <p>
     * Se seteaza in query-ul de update valorile noi ale obiectului
     * </p>
     *
     * @param s         PreparedStatement unde se seteaza valorile
     * @param allFields Unde sunt stocate noile valori
     * @return PreparedStatement
     * @throws Exception Se arunca o exceptie
     */
    public PreparedStatement setUpdateStatementFields(PreparedStatement s, String[] allFields) throws Exception {
        String typeName = type.getSimpleName().toLowerCase(Locale.ROOT);
        if (typeName.equals("clients")) {
            s.setString(1, allFields[1]);
            s.setString(2, allFields[2]);
            s.setInt(3, Integer.parseInt(allFields[3]));
            s.setInt(4, Integer.parseInt(allFields[0]));
            return s;
        }
        if (typeName.equals("products")) {
            s.setString(1, allFields[1]);
            s.setFloat(2, Float.parseFloat(allFields[2]));
            s.setInt(3, Integer.parseInt(allFields[3]));
            s.setInt(4, Integer.parseInt(allFields[0]));
            return s;
        }
        return null;
    }

    /**
     * <p>
     * Metoda ce face update la un obiect
     * </p>
     *
     * @param fields Noile valori cu care urmeaza sa fie updatat obiectul
     * @throws SQLException Se arunca o exceptie de tip SQL
     */
    public void updateObject(String fields) throws SQLException {
        String[] allFields = fields.split(" ");
        String query = stringUpdate();
        Connection c = null;
        PreparedStatement s = null;
        try {
            c = ConnectionFactory.getConnection();
            s = c.prepareStatement(query);
            s = setUpdateStatementFields(s, allFields);
            s.executeUpdate();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findByID " + e.getMessage());
        } finally {
            ConnectionFactory.close(s);
            ConnectionFactory.close(c);
        }
    }

    /**
     * <p>
     * Se returneaza lista cu toate datele stocate in rs
     * </p>
     *
     * @param rs ResultSet ce continte toate datele dintr-o tabela
     * @return ArrayList
     */
    public ArrayList<T> listForJTable(ResultSet rs) {
        ArrayList<T> lista = (ArrayList<T>) createObjects(rs);
        return lista;
    }

    /**
     * <p>
     * Returneaza modelul pentru JTable populata cu datele din resultSet
     * </p>
     *
     * @param model     Modelul pentru tabel
     * @param resultSet ResultSet unde sunt stocate datele despre obiecte
     * @return DefaultTableModel
     */
    public DefaultTableModel getFieldsName(DefaultTableModel model, ResultSet resultSet) {
        ArrayList<String> allFields = new ArrayList<>();
        try {
            while (resultSet.next()) {
                for (Field field : type.getDeclaredFields()) {
                    allFields.add(field.getName());
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (allFields.size() == 5) {
            String[] data = new String[5];
            for (int i = 0; i < allFields.size(); i++)
                data[i] = allFields.get(i);
            model.addRow(data);
        } else if (allFields.size() == 4) {
            String[] data = new String[4];
            for (int i = 0; i < allFields.size(); i++)
                data[i] = allFields.get(i);
            model.addRow(data);
        }
        return model;
    }
}
