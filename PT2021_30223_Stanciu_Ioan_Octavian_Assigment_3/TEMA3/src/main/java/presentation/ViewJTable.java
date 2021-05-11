package presentation;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.SystemColor;
import java.sql.*;

/**
 * @author Stanciu Ioan-Octavian 30223
 */
public class ViewJTable {

    /**
     * @param x     Numele tabelei pentru care se afiseaza tabelul
     * @param model Modelul cu care o sa fie populat tabelul
     * @throws SQLException Se arunca o exceptie de tip SQL
     */
    public ViewJTable(String x, DefaultTableModel model) throws SQLException {
        initialize(x, model);
    }

    private void initialize(String x, DefaultTableModel model) {
        JFrame frame = new JFrame("ALL " + x);
        frame.setBounds(100, 100, 500, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JTable table = new JTable();
        table.setBorder(new MatteBorder(1, 1, 1, 1, (Color) SystemColor.inactiveCaption));
        table.setBounds(10, 11, 464, 439);
        table.setModel(model);
        frame.getContentPane().add(table);

        frame.setVisible(true);
    }
}
