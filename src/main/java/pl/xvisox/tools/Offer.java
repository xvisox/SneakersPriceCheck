package pl.xvisox.tools;

import org.jetbrains.annotations.NotNull;

public class Offer implements Comparable<Offer> {
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

    @Override
    public int compareTo(@NotNull Offer o) {
        return o.getSize().compareTo(this.getSize());
    }
}
