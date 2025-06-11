package model;

public class Customer extends Person {

    private int id;
    private String address;

    public Customer(int id, String name, String phone, String email, String address) {
        super(name, phone, email);
        this.id = id;
        this.address = address;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return getName();
    }
}