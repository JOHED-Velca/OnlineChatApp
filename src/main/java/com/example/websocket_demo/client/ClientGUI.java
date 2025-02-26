package com.example.websocket_demo.client;

import com.example.websocket_demo.Message;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class ClientGUI extends JFrame {
    public ClientGUI(String username) {
        super("User: " + username);

        setSize(1218, 685);
        setLocationRelativeTo(null); //loads the GUI in the center of screen
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
}
