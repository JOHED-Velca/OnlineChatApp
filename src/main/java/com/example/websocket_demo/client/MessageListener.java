package com.example.websocket_demo.client;

import com.example.websocket_demo.Message;

import java.util.ArrayList;

public interface MessageListener {
    void onMessageReceive(Message message);
    void onActiveUsersUpdate(ArrayList<String> users);
}
