package com.example.websocket_demo.client;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ClientGUI clientGUI = new ClientGUI("TapTap");
                clientGUI.setVisible(true); //call this line at the end to make sure everything loads properly before it gets displayed to the user
            }
        });
    }
}
