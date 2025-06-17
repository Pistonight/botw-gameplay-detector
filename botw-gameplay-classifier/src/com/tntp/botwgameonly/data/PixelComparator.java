package com.tntp.botwgameonly.data;

public class PixelComparator {
    private int targetRed;
    private int targetGreen;
    private int targetBlue;
    private double threshold;

    public PixelComparator(double threshold) {
        super();
        this.threshold = threshold;
    }

    public void setTarget(int r, int g, int b) {
        this.targetRed = r;
        this.targetGreen = g;
        this.targetBlue = b;
    }

    public boolean isSimilar(int r, int g, int b) {
        int dr = Math.abs(r - targetRed);
        int dg = Math.abs(g - targetGreen);
        int db = Math.abs(b - targetBlue);
        double similarity = Math.sqrt(dr * dr + dg * dg + db * db);
        return similarity < threshold;
    }
}
