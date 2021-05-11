package businessLayer;

import connection.ConnectionFactory;
import dataAccessLayer.*;
import model.*;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * @author Stanciu Ioan-Octavian 30223
 */
public class Commands {
    private final ClientsDAO cdao;
    private final ProductsDAO pdao;
    private final OrdersDAO odao;

    /**
     * <p>
     * Constructor ce contine cele 3 clase pentru dataAccess
     * </p>
     *
     * @param cdao Clasa de clienti pentru a accesa datele din baza de date
     * @param pdao Clasa de produse pentru a accesa datele din baza de date
     * @param odao Clasa de comnezi pentru a accesa datele din baza de date
     */
    public Commands(ClientsDAO cdao, ProductsDAO pdao, OrdersDAO odao) {
        this.cdao = cdao;
        this.pdao = pdao;
        this.odao = odao;
    }

    /**
     * <p>
     * Creaza o comanda in tabela "orders", se incrementeaza costul total al clientului si se decrementeaza numarul de produse ramase in stoc
     * </p>
     *
     * @param idclient     Id-ul clientului
     * @param idProduct    Id-ul produsului
     * @param nrOfProducts Numarul total de produse care doresc sa fie comandate
     * @throws Exception Se arunca exceptie
     */
    public void createOrder(int idclient, int idProduct, int nrOfProducts) throws Exception {
        Clients c = this.cdao.findByID(idclient);
        Products p = this.pdao.findByID(idProduct);
        if (nrOfProducts > p.getProducts_inStock())
            JOptionPane.showMessageDialog(null, "NOT ENOUGH PRODUCTS IN STOCK", "WARNING!", JOptionPane.WARNING_MESSAGE);
        else {
            this.cdao.updateClientTotalBill(idclient, c.getClients_total_bill() + nrOfProducts * p.getProducts_price());
            this.pdao.updateProductInStock(idProduct, p.getProducts_inStock() - nrOfProducts);
            this.odao.insertObject(idclient + " " + idProduct + " " + nrOfProducts);
        }
    }

    /**
     * <p>
     * Se sterge un client, se sterg toate comenzilel efectuate de acesta si se incrementeaza la loc numarul de produse ramase in stoc
     * </p>
     *
     * @param idclient Id-ul clientului
     * @throws Exception Se arunca o exceptie
     */
    public void deleteClient(int idclient) throws Exception {
        ArrayList<Orders> orders = this.odao.returnOrdersClient(idclient);
        for (Orders o : orders) {
            Products p = this.pdao.findByID(o.getIdproducts());
            this.pdao.updateObject(p.getIdproducts() + " " + p.getProducts_name() + " " + p.getProducts_price() + " " + p.getProducts_inStock());
            this.odao.deleteObject(o.getIdorders());
        }
        this.cdao.deleteObject(idclient);
    }

    /**
     * <p>
     * Se sterge un produs, se sterg toate comenzile ce contin acest produs, se decrementeaza costul total tuturor clientilor ce au comandat acest produs
     * </p>
     *
     * @param idproduct Id-ul produsului
     * @throws Exception Se arunca o exceptie
     */
    public void deleteProduct(int idproduct) throws Exception {
        ArrayList<Orders> orders = this.odao.returnOrdersProduct(idproduct);
        for (Orders o : orders) {
            Clients c = this.cdao.findByID(o.getIdclients());
            this.cdao.updateObject(c.getIdclients() + " " + c.getClients_name() + " " + c.getClients_CNP());
            this.odao.deleteObject(o.getIdorders());
        }
        this.pdao.deleteObject(idproduct);
    }

    /**
     * <p>
     * Se returneaza toti clientii din tabela "clients"
     * </p>
     *
     * @return String[] ce contine toti clientii
     * @throws SQLException Se arunca exceptie SQL
     */
    public String[] getClients() throws SQLException {
        ArrayList<String> allClients = new ArrayList<>();
        Connection co = null;
        PreparedStatement s = null;
        ResultSet rs = null;
        String query = "SELECT * FROM clients";
        try {
            co = ConnectionFactory.getConnection();
            s = co.prepareStatement(query);
            rs = s.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                String x = "";
                for (int i = 1; i < rsmd.getColumnCount(); i++)
                    x = x + rs.getString(i) + " ";
                allClients.add(x);
            }
            String[] allClientsFinal = new String[allClients.size()];
            for (int i = 0; i < allClients.size(); i++)
                allClientsFinal[i] = allClients.get(i);
            return allClientsFinal;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(s);
            ConnectionFactory.close(co);
        }
        return null;
    }

    /**
     * <p>
     * Se returneaza toate produsele din tabela "products"
     * </p>
     *
     * @return String[] cu toate produsele
     * @throws SQLException Se arunca exceptie de tip SQL
     */
    public String[] getProducts() throws SQLException {
        ArrayList<String> allClients = new ArrayList<>();
        Connection co = null;
        PreparedStatement s = null;
        ResultSet rs = null;
        String query = "SELECT * FROM products";
        try {
            co = ConnectionFactory.getConnection();
            s = co.prepareStatement(query);
            rs = s.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                String x = "";
                for (int i = 1; i < rsmd.getColumnCount(); i++)
                    x = x + rs.getString(i) + " ";
                allClients.add(x);
            }
            String[] allClientsFinal = new String[allClients.size()];
            for (int i = 0; i < allClients.size(); i++)
                allClientsFinal[i] = allClients.get(i);
            return allClientsFinal;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(s);
            ConnectionFactory.close(co);
        }
        return null;
    }

    /**
     * <p>
     * Se returneaza toate comenzile care contin clientul cu id-ul idClient
     * </p>
     *
     * @param idClient Id-ul clientului
     * @return ArrayList cu toate comenzile
     * @throws SQLException Se arunca exceptie de tip SQL
     */
    public ArrayList<Orders> getOrdersWithClientID(int idClient) throws SQLException {
        return this.odao.returnOrdersClient(idClient);
    }

    /**
     * <p>
     * Returneaza produsul cu id-ul idProduct pornind din tabela orders
     * </p>
     *
     * @param idProduct Id-ul produsului
     * @return Produsul din orders ce are id-ul idProduct
     * @throws SQLException Se arunca exceptie de tip SQL
     */
    public Products getProduct(int idProduct) throws SQLException {
        return this.odao.returnSingleProduct(idProduct);
    }
}
