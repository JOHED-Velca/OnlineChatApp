package com.example.websocket_demo.client;

import com.example.websocket_demo.Message;

import java.util.concurrent.ExecutionException;

public class ClientGUI {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyStompClient myStompClient = new MyStompClient("TapTap");
        myStompClient.sendMessage(new Message("TapTap", "Hello World!"));
    }
}
