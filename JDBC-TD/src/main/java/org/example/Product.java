package org.example;

import java.time.Instant;

public class Product {
    public int id;
    public String name;
    public double price;
    public Instant creationDatetime;

    public Product(int int1, String string, Instant instant, Category category) {
      
    }

    public String toString() {
        return "Product: id=" + id + ", name=" + name + ", price=" + price + ", created=" + creationDatetime;
    }
}

