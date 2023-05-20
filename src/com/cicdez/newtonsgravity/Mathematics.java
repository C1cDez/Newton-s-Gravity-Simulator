package com.cicdez.newtonsgravity;

import java.awt.*;

public class Mathematics {
    public static final double GRAVITATION_CONSTANT_REAL = 6.6743E-11;
    public static final double GRAVITATION_CONSTANT = 1;

    public static double getSquareOfDistance(PhysicalBody a, PhysicalBody b) {
        return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
    }

    public static double calculateForce(PhysicalBody a, PhysicalBody b) {
        return GRAVITATION_CONSTANT * (a.mass * b.mass / getSquareOfDistance(a, b));
    }

    public static Color randomColor() {
        return new Color((int) (Math.random() * (255 * 255 * 255)));
    }
}
