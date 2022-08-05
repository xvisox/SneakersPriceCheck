package pl.xvisox.tools;

public class Offer {
    private final String price;
    private final String size;

    public Offer(String price, String size) {
        this.price = price;
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public String getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "Offer{" + "price='" + price + '\'' + ", size='" + size + '\'' + '}';
    }
}
