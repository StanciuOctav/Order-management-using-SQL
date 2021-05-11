package model;

/**
 * @author Stanciu Ioan-Octavian 30223
 */
public class Clients {

    private int idclients;
    private String clients_name;
    private String clients_CNP;
    private int clients_age;
    private float clients_total_bill;

    public Clients() {
    }

    /**
     * @param idclients          Id-ul clientului
     * @param clients_name       Numele clientului
     * @param clients_CNP        CNP-ul clientului
     * @param clients_age        Varsta clientului
     * @param clients_total_bill Suma totala ce o are de platit clientul
     */
    public Clients(int idclients, String clients_name, String clients_CNP, int clients_age, float clients_total_bill) {
        this.idclients = idclients;
        this.clients_name = clients_name;
        this.clients_CNP = clients_CNP;
        this.clients_age = clients_age;
        this.clients_total_bill = clients_total_bill;
    }

    public int getIdclients() {
        return idclients;
    }

    public String getClients_name() {
        return clients_name;
    }

    public String getClients_CNP() {
        return clients_CNP;
    }

    public int getClients_age() {
        return clients_age;
    }

    public float getClients_total_bill() {
        return clients_total_bill;
    }

    public void setIdclients(int idclients) {
        this.idclients = idclients;
    }

    public void setClients_name(String clients_name) {
        this.clients_name = clients_name;
    }

    public void setClients_CNP(String clients_CNP) {
        this.clients_CNP = clients_CNP;
    }

    public void setClients_age(int clients_age) {
        this.clients_age = clients_age;
    }

    public void setClients_total_bill(float clients_total_bill) {
        this.clients_total_bill = clients_total_bill;
    }
}
