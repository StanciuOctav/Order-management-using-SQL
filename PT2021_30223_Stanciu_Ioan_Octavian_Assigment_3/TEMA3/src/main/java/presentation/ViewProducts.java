package presentation;

import businessLayer.Commands;
import connection.ConnectionFactory;
import dataAccessLayer.AbsractDAO;
import dataAccessLayer.ClientsDAO;
import dataAccessLayer.OrdersDAO;
import dataAccessLayer.ProductsDAO;
import model.Products;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Stanciu Ioan-Octavian 30223
 */
public class ViewProducts extends AbsractDAO<Products> {

    private JFrame frameProducts;
    private JTextField textFieldName;
    private JTextField textFieldPrice;
    private JTextField textFieldIS;
    private JTextField textFieldID;
    private ProductsDAO pdao;
    private Commands commands;

    public ViewProducts(Commands comm, ProductsDAO pdao) {
        this.commands = comm;
        this.pdao = pdao;
        initialize();
    }

    /**
     * <p>
     * Toate textField-urile
     * </p>
     */
    public void textFields() {
        textFieldName = new JTextField();
        textFieldName.setBounds(136, 75, 86, 20);
        frameProducts.getContentPane().add(textFieldName);
        textFieldName.setColumns(10);

        textFieldPrice = new JTextField();
        textFieldPrice.setColumns(10);
        textFieldPrice.setBounds(267, 75, 86, 20);
        frameProducts.getContentPane().add(textFieldPrice);

        textFieldIS = new JTextField();
        textFieldIS.setColumns(10);
        textFieldIS.setBounds(388, 75, 86, 20);
        frameProducts.getContentPane().add(textFieldIS);

        textFieldID = new JTextField();
        textFieldID.setColumns(10);
        textFieldID.setBounds(10, 75, 86, 20);
        frameProducts.getContentPane().add(textFieldID);
    }

    /**
     * <p>
     * Toate label-urile
     * </p>
     */
    public void labels() {
        JLabel labelName = new JLabel("Name:");
        labelName.setHorizontalAlignment(SwingConstants.CENTER);
        labelName.setBounds(136, 50, 86, 14);
        frameProducts.getContentPane().add(labelName);

        JLabel labelPrice = new JLabel("Price:");
        labelPrice.setHorizontalAlignment(SwingConstants.CENTER);
        labelPrice.setBounds(267, 50, 86, 14);
        frameProducts.getContentPane().add(labelPrice);

        JLabel labelInStock = new JLabel("In Stock:");
        labelInStock.setHorizontalAlignment(SwingConstants.CENTER);
        labelInStock.setBounds(388, 50, 86, 14);
        frameProducts.getContentPane().add(labelInStock);

        JLabel labelID = new JLabel("ID:");
        labelID.setHorizontalAlignment(SwingConstants.CENTER);
        labelID.setBounds(10, 50, 86, 14);
        frameProducts.getContentPane().add(labelID);
    }

    /**
     * <p>
     * Tooate componentele declarate si adugate in frame
     * </p>
     */
    private void initialize() {
        frameProducts = new JFrame("PRODUCTS");
        frameProducts.setBounds(100, 100, 500, 500);
        frameProducts.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameProducts.getContentPane().setLayout(null);

        textFields();
        labels();

        JButton buttonAdd = new JButton("ADD");
        buttonAdd.setBounds(74, 146, 89, 23);
        buttonAdd.addActionListener(e -> {
            try {
                this.pdao.insertObject(textFieldName.getText() + " " + textFieldPrice.getText() + " " + textFieldIS.getText());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        });
        frameProducts.getContentPane().add(buttonAdd);

        JButton buttonEdit = new JButton("EDIT");
        buttonEdit.setBounds(201, 146, 89, 23);
        buttonEdit.addActionListener(e -> {
            try {
                this.pdao.updateObject(textFieldID.getText() + " " + textFieldName.getText() + " " + textFieldPrice.getText() + " " + textFieldIS.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        frameProducts.getContentPane().add(buttonEdit);

        JButton buttonDelete = new JButton("DELETE");
        buttonDelete.setBounds(330, 146, 89, 23);
        buttonDelete.addActionListener(e -> {
            try {
                this.commands.deleteProduct(Integer.parseInt(textFieldID.getText()));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        frameProducts.getContentPane().add(buttonDelete);

        JButton buttonView = new JButton("VIEW ALL PRODUCTS");
        buttonView.setBounds(45, 239, 374, 45);
        buttonView.addActionListener(e -> {
            DefaultTableModel model = new DefaultTableModel(0, 4);
            try {
                initilizeTable(model);
                new ViewJTable("PRODUCTS", model);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        frameProducts.getContentPane().add(buttonView);

        frameProducts.setVisible(true);
    }

    /**
     * <p>
     * Popularea tabelului cu informatii despre toate produsele din baza de date
     * </p>
     *
     * @param model Modelul in care se populeaza
     * @throws SQLException Se arunca o exceptie de tip SQL
     */
    private void initilizeTable(DefaultTableModel model) throws SQLException {
        ArrayList<Products> allProducts;
        Connection c = null;
        PreparedStatement s = null;
        ResultSet rs;
        String query = "SELECT * FROM products";
        try {
            c = ConnectionFactory.getConnection();
            s = c.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = s.executeQuery();
            allProducts = listForJTable(rs);
            rs.beforeFirst();
            model = getFieldsName(model, rs);
            for (Products p : allProducts)
                model.addRow(new String[]{String.valueOf(p.getIdproducts()), p.getProducts_name(), String.valueOf(p.getProducts_price()), String.valueOf(p.getProducts_inStock())});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(s);
            ConnectionFactory.close(c);
        }
    }
}
