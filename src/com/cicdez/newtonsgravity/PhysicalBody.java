package com.cicdez.newtonsgravity;

import java.awt.*;

public class PhysicalBody {
    public final double mass;
    public final double radius;
    public double x, y;
    public double vx, vy;   //Velocity
    public final Color color;
    public final boolean canMove;

    public PhysicalBody(double mass, double radius, double x, double y,
                        double vx, double vy, Color color, boolean canMove) {
        this.mass = mass;
        this.radius = radius;
        this.x = x; this.y = y;
        this.vx = vx; this.vy = vy;
        this.color = color;
        this.canMove = canMove;
    }
    public PhysicalBody(double mass, double radius, double x, double y, double vx, double vy) {
        this(mass, radius, x, y, vx, vy, Mathematics.randomColor(), true);
    }

    public void update(double forceX, double forceY, double dt) {
        if (canMove) {
            double ax = forceX / mass, ay = forceY / mass;
            vx += ax * dt; vy += ay * dt;
            x += vx * dt; y += vy * dt;
        }
    }
}
