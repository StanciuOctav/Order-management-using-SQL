package presentation;

import businessLayer.Commands;
import dataAccessLayer.*;

import java.sql.SQLException;

/**
 * @author Stanciu Ioan-Octavian 30223
 */
public class Controller {
    private Commands commands;
    private ClientsDAO cdao;
    private ProductsDAO pdao;

    /**
     * @param commands Comenzile pentru comenzi
     * @param cdao     Clienti
     * @param pdao     Produse
     */
    public Controller(Commands commands, ClientsDAO cdao, ProductsDAO pdao) {
        this.commands = commands;
        this.cdao = cdao;
        this.pdao = pdao;
    }

    /**
     * <p>
     * Metoda ce trebuie rulata pentru aplicatie
     * </p>
     *
     * @param args Argumente
     * @throws SQLException Se arunca o exceptie de tip SQL
     */
    public static void main(String[] args) throws SQLException {
        ClientsDAO cdao = new ClientsDAO();
        ProductsDAO pdao = new ProductsDAO();
        OrdersDAO odao = new OrdersDAO();
        Commands commands = new Commands(cdao, pdao, odao);
        new ViewClients(commands, cdao);
        new ViewProducts(commands, pdao);
        new ViewOrders(commands);
    }
}
