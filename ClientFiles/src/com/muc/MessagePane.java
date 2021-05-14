package com.muc;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MessagePane extends JPanel implements MessageListener {

    private final String login;

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JTextField inputField = new JTextField();

    public MessagePane(ChatClient client, String login) {
        this.login = login;

        client.addMessageListener(this);

        setLayout(new BorderLayout());
        JList<String> messageList = new JList<>(listModel);
        add(new JScrollPane(messageList), BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);

        inputField.addActionListener(e -> {
            try {
                String text = inputField.getText();
                client.msg(login, text);
                listModel.addElement("You: " + text);
                inputField.setText("");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    @Override
    public void onMessage(String fromLogin, String msgBody) {
        if (login.equalsIgnoreCase(fromLogin)) {
            String line = fromLogin + ": " + msgBody;
            listModel.addElement(line);
        }
    }
}
