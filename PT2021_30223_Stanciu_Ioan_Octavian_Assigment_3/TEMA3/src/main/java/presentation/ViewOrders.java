package presentation;


import businessLayer.Commands;
import connection.ConnectionFactory;
import dataAccessLayer.AbsractDAO;
import model.Orders;
import model.Products;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * @author Stanciu Ioan-Octavian 30223
 */
public class ViewOrders extends AbsractDAO<Orders> {

    private JFrame frmOrders;
    private JTextField textField;
    private Commands commands;

    public ViewOrders(Commands commands) throws SQLException {
        this.commands = commands;
        initialize();
    }

    /**
     * <p>
     * Toate label-urile
     * </p>
     */
    public void labels() {
        JLabel lblNewLabel = new JLabel("CLIENTS:");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(203, 11, 68, 32);
        frmOrders.getContentPane().add(lblNewLabel);

        JLabel lblProducts = new JLabel("PRODUCTS:");
        lblProducts.setHorizontalAlignment(SwingConstants.CENTER);
        lblProducts.setBounds(203, 116, 68, 32);
        frmOrders.getContentPane().add(lblProducts);

        JLabel lblNewLabel_1 = new JLabel("NR PRODUCTS");
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_1.setBounds(42, 230, 89, 45);
        frmOrders.getContentPane().add(lblNewLabel_1);
    }

    /**
     * <p>
     * Toate textField-urile
     * </p>
     */
    public void textFields() {
        textField = new JTextField();
        textField.setBounds(42, 264, 86, 20);
        frmOrders.getContentPane().add(textField);
        textField.setColumns(10);
    }

    /**
     * <p>
     * Tooate componentele declarate si adugate in frame
     * </p>
     *
     * @throws SQLException
     */
    private void initialize() throws SQLException {
        frmOrders = new JFrame("ORDERS");
        frmOrders.setBounds(100, 100, 500, 500);
        frmOrders.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmOrders.getContentPane().setLayout(null);

        labels();
        textFields();

        JButton buttonView = new JButton("VIEW ALL ORDERS");
        buttonView.addActionListener(e -> {
            DefaultTableModel model = new DefaultTableModel(0, 4);
            try {
                initilizeTable(model);
                new ViewJTable("ORDERS", model);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        buttonView.setBounds(315, 264, 159, 164);
        frmOrders.getContentPane().add(buttonView);

        JList listClients = new JList();
        listClients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        String[] allClients = this.commands.getClients();
        DefaultListModel modelClients = new DefaultListModel();
        for (int i = 0; i < allClients.length; i++)
            modelClients.addElement(allClients[i]);
        listClients.setModel(modelClients);
        listClients.setBounds(45, 58, 110, 152);

        JScrollPane scrollPaneClients = new JScrollPane();
        scrollPaneClients.setBounds(10, 38, 464, 67);
        scrollPaneClients.setViewportView(listClients);
        listClients.setLayoutOrientation(JList.VERTICAL);
        frmOrders.getContentPane().add(scrollPaneClients);

        JList listProducts = new JList();
        String[] allProducts = this.commands.getProducts();
        DefaultListModel modelProducts = new DefaultListModel();
        for (int i = 0; i < allProducts.length; i++)
            modelProducts.addElement(allProducts[i]);
        listProducts.setModel(modelProducts);
        listProducts.setBounds(341, 84, 62, 142);
        listProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPaneProducts = new JScrollPane();
        scrollPaneProducts.setBounds(10, 152, 464, 67);
        scrollPaneProducts.setViewportView(listProducts);
        frmOrders.getContentPane().add(scrollPaneProducts);

        JButton buttonBill = new JButton("BILL");
        buttonBill.setBounds(10, 405, 89, 23);
        buttonBill.addActionListener(e -> {
            String[] clientFields = modelClients.getElementAt(listClients.getSelectedIndex()).toString().split(" ");
            try {
                FileWriter f = new FileWriter("E:\\1. Facultate\\AN 2\\SEM 2\\Tehnici de programare fundamentale - Tudor Cioara\\PT2021_30223_Stanciu_Ioan_Octavian_Assigment_3\\src\\bills\\" + clientFields[1] + ".txt");
                f.write("Client: " + clientFields[1] + " ; CNP: " + clientFields[2] + "\n");
                ArrayList<Orders> orders = this.commands.getOrdersWithClientID(Integer.parseInt(clientFields[0]));
                float sum = 0f;
                for (Orders o : orders) {
                    Products p = this.commands.getProduct(o.getIdproducts());
                    f.write("Want to buy " + o.getNrProducts() + " " + p.getProducts_name() + " for the price of " + p.getProducts_price() * o.getNrProducts() + "\n");
                    sum += p.getProducts_price() * o.getNrProducts();
                }
                f.write("TOTAL: " + sum + "\n");
                f.close();
            } catch (IOException | SQLException ioException) {
                ioException.printStackTrace();
            }

        });
        frmOrders.getContentPane().add(buttonBill);

        JButton buttonOrder = new JButton("ORDER");
        buttonOrder.setBounds(182, 263, 89, 23);
        buttonOrder.addActionListener(e -> {
            String[] clientFields = (modelClients.getElementAt(listClients.getSelectedIndex()).toString().split(" "));
            String[] productFields = (modelProducts.getElementAt(listProducts.getSelectedIndex()).toString().split(" "));
            try {
                this.commands.createOrder(Integer.parseInt(clientFields[0]), Integer.parseInt(productFields[0]), Integer.parseInt(textField.getText()));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        frmOrders.getContentPane().add(buttonOrder);

        frmOrders.setVisible(true);
    }

    /**
     * <p>
     * Popularea tabelului cu informatii despre toate comenzile din baza de date
     * </p>
     *
     * @param model Modelul in care se populeaza
     * @throws SQLException Se arunca o exceptie de tip SQL
     */
    private void initilizeTable(DefaultTableModel model) throws SQLException {
        ArrayList<Orders> allOrders = null;
        Connection c = null;
        PreparedStatement s = null;
        ResultSet rs = null;
        String query = "SELECT * FROM orders";
        try {
            c = ConnectionFactory.getConnection();
            s = c.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = s.executeQuery();
            allOrders = listForJTable(rs);
            rs.beforeFirst();
            model = getFieldsName(model, rs);
            for (Orders o : allOrders)
                model.addRow(new String[]{String.valueOf(o.getIdorders()), String.valueOf(o.getIdclients()), String.valueOf(o.getIdproducts()), String.valueOf(o.getNrProducts())});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(s);
            ConnectionFactory.close(c);
        }
    }
}
