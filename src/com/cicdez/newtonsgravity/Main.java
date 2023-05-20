package com.cicdez.newtonsgravity;

public class Main {
    public static void main(String[] args) {
        Screen screen = new Screen(false);
        Thread thread = new Thread(screen, "Screen");
        thread.start();
    }
}
