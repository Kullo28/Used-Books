
package edu.syr.Used_Book.pricing;

public class DepreciatedBookPrice implements BookPrice {
    private final BookPrice bookPrice;

    public DepreciatedBookPrice(BookPrice bookPrice) {
        this.bookPrice = bookPrice;
    }

    @Override
    public double getPrice() {
        return bookPrice.getPrice() * 0.9;
    }
}
