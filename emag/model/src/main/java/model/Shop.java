package model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    private int id;
    private String name;
    private Seller owner;
    private List<Product> products;

    public Shop(int id, String name, Seller owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.products = new ArrayList<Product>();
    }
}
