package model;

/**
 * @author Stanciu Ioan-Octavian 30223
 */
public class Products {
    private int idproducts;
    private String products_name;
    private float products_price;
    private int products_inStock;

    public Products() {
    }

    /**
     * @param idproducts       Id-ul produsului
     * @param products_name    Numele produsului
     * @param products_price   Pretul produsului
     * @param products_inStock Numarul de produse in stoc
     */
    public Products(int idproducts, String products_name, float products_price, int products_inStock) {
        this.idproducts = idproducts;
        this.products_name = products_name;
        this.products_price = products_price;
        this.products_inStock = products_inStock;
    }

    public int getIdproducts() {
        return idproducts;
    }

    public String getProducts_name() {
        return products_name;
    }

    public float getProducts_price() {
        return products_price;
    }

    public int getProducts_inStock() {
        return products_inStock;
    }

    public void setIdproducts(int idproducts) {
        this.idproducts = idproducts;
    }

    public void setProducts_name(String products_name) {
        this.products_name = products_name;
    }

    public void setProducts_price(float products_price) {
        this.products_price = products_price;
    }

    public void setProducts_inStock(int products_inStock) {
        this.products_inStock = products_inStock;
    }
}
