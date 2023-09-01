package com.cicdez.newtonsgravity;

import javax.swing.*;
import java.awt.*;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.List;

public class Screen extends JFrame implements Runnable {
    public static final int FPS = 1500;
    public static final int WIDTH = 1800, HEIGHT = 900;
    public static final Color BACKGROUND = new Color(13, 41, 74);
    public static final double DT = 0.01;
    public static final double ZOOM = 1;
    public static final int CENTER_X = (int) (WIDTH * ZOOM / 2),
            CENTER_Y = (int) (HEIGHT * ZOOM / 2);

    private final boolean enableTrajectories;

    private VolatileImage screen;
    private VolatileImage trajectory;

    public final List<PhysicalBody> physicalBodies = new ArrayList<>();

    public Screen(boolean enableTrajectories) {
        super("Newton's Gravitation Simulation");
        this.enableTrajectories = enableTrajectories;
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        
        this.screen = this.getGraphicsConfiguration().createCompatibleVolatileImage(WIDTH, HEIGHT);
        this.trajectory = this.getGraphicsConfiguration().createCompatibleVolatileImage(WIDTH, HEIGHT);
    }

    public void render() {
        if (screen.validate(this.getGraphicsConfiguration()) == VolatileImage.IMAGE_INCOMPATIBLE) {
            screen = this.getGraphicsConfiguration().createCompatibleVolatileImage(WIDTH, HEIGHT);
        }
        if (trajectory.validate(this.getGraphicsConfiguration()) == VolatileImage.IMAGE_INCOMPATIBLE) {
            trajectory = this.getGraphicsConfiguration().createCompatibleVolatileImage(WIDTH, HEIGHT);
        }
        updateBodies();
        drawBodies(screen.createGraphics(), trajectory.createGraphics());
        this.getGraphics().drawImage(screen, 0, 0, WIDTH, HEIGHT, null);
    }

    public void updateBodies() {
        for (PhysicalBody body : physicalBodies) {
            for (PhysicalBody otherBody : physicalBodies) {
                if (body == otherBody) continue;
                double force = Mathematics.calculateForce(body, otherBody);
                double forceX = force * (otherBody.x - body.x),
                        forceY = force * (otherBody.y - body.y);
                body.update(forceX, forceY, DT);
                otherBody.update(-forceX, -forceY, DT);
            }
        }
    }

    public void drawBodies(Graphics2D graphics, Graphics2D trajectoryGraphics) {
        graphics.setColor(BACKGROUND);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        for (PhysicalBody body : physicalBodies) {
            double rx = body.x / ZOOM, ry = body.y / ZOOM;
            double rr = body.radius / ZOOM;
            graphics.setColor(body.color);
            graphics.fillOval((int) (rx - rr), (int) (ry - rr), (int) (rr * 2), (int) (rr * 2));
            if (enableTrajectories) {
                trajectoryGraphics.setColor(body.color);
                trajectoryGraphics.fillOval((int) (rx - 1.5 / ZOOM), (int) (ry - 1.5 / ZOOM),
                        3, 3);
                graphics.drawImage(trajectory, 0, 0, WIDTH, HEIGHT, null);
            }
        }
    }

    @Override
    public void run() {
        makeBodies();
        double interval = 1e9 / FPS;
        double nextInterval = System.nanoTime() + interval;
        while (true) {
            render();
            double remaining = (nextInterval - System.nanoTime()) / 1e6;
            if (remaining < 0) remaining = 0;
            try {
                Thread.sleep((long) remaining);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            nextInterval += interval;
        }
    }

    public void makeBodies() {
        for (int i = 0; i < 100; i++) {
            physicalBodies.add(new PhysicalBody(1, 10, Math.random() * WIDTH, Math.random() * HEIGHT, 0, 0));
        }
    }
}
