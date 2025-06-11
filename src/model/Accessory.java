package model;

public class Accessory extends Product {

    private String instrumentType;

    public Accessory(String productName, double price, int stock, String instrumentType) {

        super(productName, price, stock);

        this.instrumentType = instrumentType;
    }


    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }
}
