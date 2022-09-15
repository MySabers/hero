package com.wercent.hero.client.gui;

import com.google.common.base.Strings;
import com.wercent.hero.common.message.ChatRequestMessage;
import com.wercent.hero.common.message.ChatResponseMessage;
import com.wercent.hero.common.message.UserRequestMessage;
import com.wercent.hero.common.message.UserResponseMessage;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Component
public class Index implements Gui{

    private final JFrame frame = new JFrame("Index");
    private final JList<String> friends = new JList<>();

    private final DefaultListModel<String> messageModel = new DefaultListModel<>();

    /**
     * 发送接受消息列表
     */
    private final JList<String> messages = new JList<>();

    public void refreshFriend(UserResponseMessage userResponseMessage) {
        if (userResponseMessage.isSuccess()) {
            DefaultListModel<String> friendsModel = new DefaultListModel<>();
            userResponseMessage.getUsers().forEach((name, state) -> {
                friendsModel.addElement(name + ": " + state);
            });
            friends.setModel(friendsModel);
        }
    }

    public void addMessage(ChatResponseMessage crm) {
        if (crm.isSuccess()) {
            messageModel.addElement(crm.getFrom() + ": " + crm.getContent());
            messages.setModel(messageModel);
        }
    }

    @Override
    public void process(Channel channel) {
        frame.setSize(700, 500);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // 左侧好友栏
        frame.setLayout(new BorderLayout());
        JPanel left = new JPanel();
        this.buildLeft(left, channel);

        // 右侧内容栏
        JPanel right = new JPanel();
        right.setLayout(new BorderLayout());
        buildRight(right, channel);

        frame.add(left, BorderLayout.WEST);
        frame.add(right, BorderLayout.CENTER);


        // 设置界面可见
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                channel.close();
                super.windowClosing(e);
            }
        });
    }


    private void buildLeft(JPanel panel, Channel channel) {
        channel.writeAndFlush(new UserRequestMessage());
        setBorderAndTitle(panel, "在线好友列表", 150, frame.getHeight());
        JScrollPane scrollPane = new JScrollPane(friends);
        scrollPane.setPreferredSize(new Dimension(
                (int) (panel.getPreferredSize().getWidth() - 14),
                (int) (panel.getPreferredSize().getHeight() - 75)
        ));
        panel.add(scrollPane);
    }

    private void buildRight(JPanel panel, Channel channel) {
        JPanel top = new JPanel();
        JPanel bottom = new JPanel();
        panel.add(top, BorderLayout.NORTH);
        panel.add(bottom, BorderLayout.SOUTH);
        setBorderAndTitle(top, "聊天内容", frame.getWidth() - 150, frame.getHeight() - 200);
        setBorderAndTitle(bottom, "输入框", frame.getWidth() - 150, 160);
        buildMessageContent(top, channel);
        buildMessageInput(bottom, channel);
    }


    private void buildMessageContent(JPanel panel, Channel channel) {
        JScrollPane scrollPane = new JScrollPane(messages);
        scrollPane.setPreferredSize(new Dimension(
                (int) (panel.getPreferredSize().getWidth() - 30),
                (int) (panel.getPreferredSize().getHeight() - 35)
        ));
        panel.add(scrollPane);
    }

    private void buildMessageInput(JPanel panel, Channel channel) {
        JTextArea inputMessage = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(inputMessage);
        scrollPane.setPreferredSize(new Dimension(
                (int) (panel.getPreferredSize().getWidth() - 30),
                (int) (panel.getPreferredSize().getHeight() - 60)
        ));
        inputMessage.setEditable(true);
        inputMessage.setLineWrap(true);
        panel.add(scrollPane);
        JButton button = new JButton("发送");
        panel.add(button);

        button.addActionListener(e -> {
            String inputMessageText = inputMessage.getText();
            if (!Strings.isNullOrEmpty(inputMessageText)) {
                channel.writeAndFlush(new ChatRequestMessage(inputMessageText));
                inputMessage.setText("");
            }
        });
    }


    private void setBorderAndTitle(JPanel panel, String title, int width, int height) {
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setPreferredSize(new Dimension(width, height));
    }

    @Override
    public void destroy() {
        frame.dispose();
    }
}
