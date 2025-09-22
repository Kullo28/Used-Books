
package edu.syr.Used_Book.pricing;

public class BasicBookPrice implements BookPrice {
    private final double price;

    public BasicBookPrice(double price) {
        this.price = price;
    }

    @Override
    public double getPrice() {
        return price;
    }
}
