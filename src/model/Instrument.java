package model;

public class Instrument extends Product {


    private String type;

    public Instrument(String productName, double price, int stock, String type) {


        super(productName, price, stock);

        this.type = type;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
