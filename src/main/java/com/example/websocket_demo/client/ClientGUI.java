package com.example.websocket_demo.client;

import com.example.websocket_demo.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ClientGUI extends JFrame implements MessageListener {
    private JPanel connectedUsersPanel, messagePanel;
    private MyStompClient myStompClient;
    private String username;

    public ClientGUI(String username) throws ExecutionException, InterruptedException {
        super("User: " + username);
        this.username = username;
        myStompClient = new MyStompClient(this, username);

        setSize(1218, 685);
        setLocationRelativeTo(null); //loads the GUI in the center of screen
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(ClientGUI.this, "Do you really want to leave?", "Exit", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    myStompClient.disconnectedUser(username);
                    ClientGUI.this.dispose();
                }
            }
        });

        getContentPane().setBackground(Utilities.PRIMARY_COLOR);
        addGuiComponents();
    }

    private void addGuiComponents() {
        addConnectedUserComponents();
        addChatComponents();
    }

    private void addConnectedUserComponents() {
        connectedUsersPanel = new JPanel();
        connectedUsersPanel.setBorder(Utilities.addPadding(10, 10, 10, 10));
        connectedUsersPanel.setLayout(new BoxLayout(connectedUsersPanel, BoxLayout.Y_AXIS));
        connectedUsersPanel.setBackground(Utilities.SECONDARY_COLOR);
        connectedUsersPanel.setPreferredSize(new Dimension(200, getHeight()));

        JLabel connectedUsersLabel = new JLabel("Connected Users");
        connectedUsersLabel.setFont(new Font("Inter", Font.BOLD, 18));
        connectedUsersLabel.setForeground(Utilities.TEXT_COLOR);
        connectedUsersPanel.add(connectedUsersLabel);

        add(connectedUsersPanel, BorderLayout.WEST);
    }

    private void addChatComponents() {
        JPanel chatPanel = new JPanel(); //JPanel default layout manager is "flow"
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setBackground(Utilities.TRANSPARENT_COLOR);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(Utilities.TRANSPARENT_COLOR);
        chatPanel.add(messagePanel, BorderLayout.CENTER);

        messagePanel.add(createChatMessageComponent(new Message("TapTap", "Hello World")));

        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(Utilities.addPadding(10, 10, 10, 10));
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(Utilities.TRANSPARENT_COLOR);

        JTextField inputField = new JTextField();
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String input = inputField.getText();

                    //edge case: empty message (prevent empty messages)
                    if (input.isEmpty()) return;

                    inputField.setText("");

                    myStompClient.sendMessage(new Message(username, input));
                }
            }
        });
        inputField.setBackground(Utilities.SECONDARY_COLOR);
        inputField.setForeground(Utilities.TEXT_COLOR);
        inputField.setBorder(Utilities.addPadding(0,10,0,10));
        inputField.setFont(new Font("Inter", Font.PLAIN, 16));
        inputField.setPreferredSize(new Dimension(inputPanel.getWidth(), 50));
        inputPanel.add(inputField, BorderLayout.CENTER); //if not provided the second parameter, then it goes to the center by default
        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        add(chatPanel, BorderLayout.CENTER);
    }

    private JPanel createChatMessageComponent(Message message) {
        JPanel chatMessage = new JPanel();
        chatMessage.setBackground(Utilities.TRANSPARENT_COLOR);
        chatMessage.setLayout(new BoxLayout(chatMessage, BoxLayout.Y_AXIS));
        chatMessage.setBorder(Utilities.addPadding(20, 20, 20, 20));

        JLabel usernameLabel = new JLabel(message.getUser());
        usernameLabel.setFont(new Font("Inter", Font.BOLD, 18));
        usernameLabel.setForeground(Utilities.TEXT_COLOR);
        chatMessage.add(usernameLabel);

        JLabel messageLabel = new JLabel(message.getMessage());
        messageLabel.setFont(new Font("Inter", Font.PLAIN, 15));
        messageLabel.setForeground(Utilities.TEXT_COLOR);
        chatMessage.add(messageLabel);

        return chatMessage;
    }

    @Override
    public void onMessageReceive(Message message) {
        messagePanel.add(createChatMessageComponent(message));
        revalidate();
        repaint();
    }

    @Override
    public void onActiveUsersUpdate(ArrayList<String> users) {
        //remove the current user list panel (which should be the second component in the panel)
        //the user list panel doesn't get added until after and this is mainly fore when the users get updated
        if (connectedUsersPanel.getComponents().length >= 2) {
            connectedUsersPanel.remove(1);
        }

        JPanel usersListPanel = new JPanel();
        usersListPanel.setBackground(Utilities.TRANSPARENT_COLOR);
        usersListPanel.setLayout(new BoxLayout(usersListPanel, BoxLayout.Y_AXIS));

        for (String user : users) {
            JLabel username = new JLabel();
            username.setText(user);
            username.setForeground(Utilities.TEXT_COLOR);
            username.setFont(new Font("Inter", Font.BOLD, 16));
            usersListPanel.add(username);
        }

        connectedUsersPanel.add(usersListPanel);
        revalidate();
        repaint();
    }
}
