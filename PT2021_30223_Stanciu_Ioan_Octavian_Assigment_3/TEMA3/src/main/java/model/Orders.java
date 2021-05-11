package model;

/**
 * @author Stanciu Ioan-Octavian 30223
 */
public class Orders {
    private int idorders;
    private int idclients;
    private int idproducts;
    private int nrProducts;

    public Orders() {
    }

    /**
     * @param idorders   id-ul comenzii
     * @param idclients  id-ul clientului
     * @param idproducts id-ul produsului
     * @param nrProducts numarul de produse comandate
     */
    public Orders(int idorders, int idclients, int idproducts, int nrProducts) {
        this.idorders = idorders;
        this.idclients = idclients;
        this.idproducts = idproducts;
        this.nrProducts = nrProducts;
    }

    public int getIdorders() {
        return idorders;
    }

    public int getIdclients() {
        return idclients;
    }

    public int getIdproducts() {
        return idproducts;
    }

    public int getNrProducts() {
        return nrProducts;
    }

    public void setIdorders(int idorders) {
        this.idorders = idorders;
    }

    public void setIdclients(int idclients) {
        this.idclients = idclients;
    }

    public void setIdproducts(int idproducts) {
        this.idproducts = idproducts;
    }

    public void setNrProducts(int nrProducts) {
        this.nrProducts = nrProducts;
    }
}
