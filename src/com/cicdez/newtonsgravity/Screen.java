package com.cicdez.newtonsgravity;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Screen extends JFrame implements Runnable {
    public static final int WIDTH = 1300, HEIGHT = 700;
    public static final Color BACKGROUND = new Color(13, 41, 74);
    public static final double DT = 0.01;
    public static final double ZOOM = 1;
    public static final int CENTER_X = (int) (WIDTH * ZOOM / 2),
            CENTER_Y = (int) (HEIGHT * ZOOM / 2);

    private final boolean enableTrajectories;

    public final BufferedImage screen = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
    public final BufferedImage trajectories = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

    public final List<PhysicalBody> physicalBodies = new ArrayList<>();

    public Screen(boolean enableTrajectories) {
        super("Gravitation Simulation");
        this.enableTrajectories = enableTrajectories;
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

    @Override
    public void paint(Graphics graphics) {
        updateBodies();
        drawBodies(screen.createGraphics(), trajectories.createGraphics());
        graphics.drawImage(screen, 0, 0, WIDTH, HEIGHT, null);
        if (enableTrajectories)
            graphics.drawImage(trajectories, 0, 0, WIDTH, HEIGHT, null);
    }

    public void updateBodies() {
        for (PhysicalBody body : physicalBodies) {
            for (PhysicalBody otherBody : physicalBodies) {
                if (body == otherBody) continue;
                double force = Mathematics.calculateForce(body, otherBody);
                double forceX = force * (otherBody.x - body.x),
                        forceY = force * (otherBody.y - body.y);
                updateBody(body, forceX, forceY, DT);
                updateBody(otherBody, -forceX, -forceY, DT);
            }
        }
    }
    public void updateBody(PhysicalBody body, double fx, double fy, double dt) {
        body.update(fx, fy, dt);
    }

    public void drawBodies(Graphics2D graphics, Graphics2D trajectory) {
        graphics.setColor(BACKGROUND);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        for (PhysicalBody body : physicalBodies) {
            double rx = body.x / ZOOM, ry = body.y / ZOOM;
            double rr = body.radius / ZOOM;
            graphics.setColor(body.color);
            graphics.fillOval((int) (rx - rr), (int) (ry - rr), (int) (rr * 2), (int) (rr * 2));
            if (enableTrajectories) {
                trajectory.setColor(body.color);
                trajectory.fillOval((int) (rx - 1.5 / ZOOM), (int) (ry - 1.5 / ZOOM),
                        3, 3);
            }
        }
    }

    @Override
    public void run() {
        makeBodies();
        while (true) {
            this.repaint();
        }
    }

    public void makeBodies() {
        for (int i = 0; i < 250; i++) {
//            if (i == 0) physicalBodies.add(new PhysicalBody(2500, 20,
//                    CENTER_X, CENTER_Y, 0, 0));
            physicalBodies.add(new PhysicalBody(Math.random() * 10, 5,
                    Math.random() * WIDTH * ZOOM, Math.random() * HEIGHT * ZOOM, 0, 0));
        }
    }
}
