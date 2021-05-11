package presentation;

import businessLayer.Commands;
import connection.ConnectionFactory;
import dataAccessLayer.AbsractDAO;
import dataAccessLayer.ClientsDAO;
import model.Clients;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;

/**
 * @author Stanciu Ioan-Octavian 30223
 */
public class ViewClients extends AbsractDAO<Clients> {

    private JFrame frameClients;
    private JTextField textFieldName;
    private JTextField textFieldCNP;
    private JTextField textFieldAge;
    private JTextField textFieldTB;
    private JTextField textFieldID;
    private ClientsDAO cdao;
    private Commands commands;

    public ViewClients(Commands commands, ClientsDAO cdao) {
        this.cdao = cdao;
        this.commands = commands;
        initialize();
    }

    /**
     * <p>
     * Toate textField-urile
     * </p>
     */
    private void textFields() {
        textFieldName = new JTextField();
        textFieldName.setBounds(100, 75, 86, 20);
        frameClients.getContentPane().add(textFieldName);
        textFieldName.setColumns(10);

        textFieldCNP = new JTextField();
        textFieldCNP.setColumns(10);
        textFieldCNP.setBounds(196, 75, 86, 20);
        frameClients.getContentPane().add(textFieldCNP);

        textFieldAge = new JTextField();
        textFieldAge.setColumns(10);
        textFieldAge.setBounds(292, 75, 86, 20);
        frameClients.getContentPane().add(textFieldAge);

        textFieldTB = new JTextField();
        textFieldTB.setColumns(10);
        textFieldTB.setBounds(388, 75, 86, 20);
        frameClients.getContentPane().add(textFieldTB);

        textFieldID = new JTextField();
        textFieldID.setColumns(10);
        textFieldID.setBounds(10, 75, 86, 20);
        frameClients.getContentPane().add(textFieldID);
    }

    /**
     * <p>
     * Toate label-urile
     * </p>
     */
    private void labels() {
        JLabel labelName = new JLabel("Name:");
        labelName.setHorizontalAlignment(SwingConstants.CENTER);
        labelName.setBounds(100, 50, 86, 14);
        frameClients.getContentPane().add(labelName);

        JLabel labelCNP = new JLabel("CNP:");
        labelCNP.setHorizontalAlignment(SwingConstants.CENTER);
        labelCNP.setBounds(196, 50, 86, 14);
        frameClients.getContentPane().add(labelCNP);

        JLabel labelAge = new JLabel("Age:");
        labelAge.setHorizontalAlignment(SwingConstants.CENTER);
        labelAge.setBounds(292, 50, 86, 14);
        frameClients.getContentPane().add(labelAge);

        JLabel labelTotalBill = new JLabel("Total Bill:");
        labelTotalBill.setHorizontalAlignment(SwingConstants.CENTER);
        labelTotalBill.setBounds(388, 50, 86, 14);
        frameClients.getContentPane().add(labelTotalBill);

        JLabel labelID = new JLabel("ID:");
        labelID.setHorizontalAlignment(SwingConstants.CENTER);
        labelID.setBounds(10, 50, 86, 14);
        frameClients.getContentPane().add(labelID);
    }

    /**
     * /**
     * <p>
     * Declararea si punerea tuturor componentelor in frame
     * </p>
     */
    private void initialize() {
        frameClients = new JFrame("CLIENTS");
        frameClients.setBounds(100, 100, 500, 500);
        frameClients.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameClients.getContentPane().setLayout(null);

        textFields();
        labels();

        JButton buttonAdd = new JButton("ADD");
        buttonAdd.setBounds(42, 146, 89, 23);
        buttonAdd.addActionListener(e -> {
            try {
                this.cdao.insertObject(textFieldName.getText() + " " + textFieldCNP.getText() + " " + textFieldAge.getText());
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(null, "CNP-ul introdus este deja in baza de date", "ATENTIE!",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        frameClients.getContentPane().add(buttonAdd);

        JButton buttonEdit = new JButton("EDIT");
        buttonEdit.setBounds(187, 146, 89, 23);
        buttonEdit.addActionListener(e -> {
            try {// trebuie introdus si id ul pentru a putea face update
                this.cdao.updateObject(textFieldID.getText() + " " + textFieldName.getText() + " " + textFieldCNP.getText() + " " + textFieldAge.getText());
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, "CNP-ul introdus este deja in baza de date", "ATENTIE!",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        frameClients.getContentPane().add(buttonEdit);

        JButton buttonDelete = new JButton("DELETE");
        buttonDelete.setBounds(330, 146, 89, 23);
        buttonDelete.addActionListener(e -> {
            try {
                this.commands.deleteClient(Integer.parseInt(textFieldID.getText()));
            } catch (Exception throwables) {
                throwables.printStackTrace();
            }
        });
        frameClients.getContentPane().add(buttonDelete);

        JButton buttonView = new JButton("VIEW ALL CLIENTS");
        buttonView.setBounds(45, 239, 374, 45);
        buttonView.addActionListener(e -> {
            try {
                DefaultTableModel model = new DefaultTableModel(0, 5);
                initilizeTable(model);
                new ViewJTable("CLIENTS", model);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        frameClients.getContentPane().add(buttonView);

        frameClients.setVisible(true);
    }

    /**
     * <p>
     * Popularea tabelului cu informatii despre toti clientii din baza de date
     * </p>
     *
     * @param model Modelul in care se populeaza
     * @throws SQLException Se arunca o exceptie de tip SQL
     */
    private void initilizeTable(DefaultTableModel model) throws SQLException {
        ArrayList<Clients> allClients = null;
        Connection c = null;
        PreparedStatement s = null;
        ResultSet rs = null;
        String query = "SELECT * FROM clients";
        try {
            c = ConnectionFactory.getConnection();
            s = c.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = s.executeQuery();
            allClients = listForJTable(rs);
            rs.beforeFirst();
            model = getFieldsName(model, rs);
            for (Clients cl : allClients)
                model.addRow(new String[]{String.valueOf(cl.getIdclients()), cl.getClients_name(), cl.getClients_CNP(), String.valueOf(cl.getClients_age()), String.valueOf(cl.getClients_total_bill())});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(s);
            ConnectionFactory.close(c);
        }
    }
}
