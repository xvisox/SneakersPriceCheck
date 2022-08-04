package pl.xvisox.tools;

import java.text.DecimalFormat;

public abstract class PriceTool {
    private static final double SHIPPING = 4.2;
    public static final DecimalFormat df = new DecimalFormat("0.00");

    public static int calculatePirce(int currentPrice) {
        return (int) ((currentPrice / 1.05) * 0.88 - SHIPPING);
    }
}
