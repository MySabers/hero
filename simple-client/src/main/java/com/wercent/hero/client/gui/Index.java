package com.wercent.hero.client.gui;

import com.wercent.hero.common.message.UserRequestMessage;
import io.netty.channel.Channel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Index implements Gui{

    private final JFrame frame = new JFrame("Index");

    private boolean autoClose = false;

    @Override
    public void process(Channel channel) {
        channel.writeAndFlush(new UserRequestMessage());
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
                if (autoClose) {
                    channel.close();
                }
                super.windowClosing(e);
            }
        });
    }


    private void buildLeft(JPanel panel, Channel channel) {
        setBorderAndTitle(panel, "在线好友列表", 150, frame.getHeight());
    }

    private void buildRight(JPanel panel, Channel channel) {
        JPanel top = new JPanel();
        JPanel bottom = new JPanel();
        panel.add(top, BorderLayout.NORTH);
        panel.add(bottom, BorderLayout.SOUTH);
        setBorderAndTitle(top, "聊天内容", frame.getWidth() - 150, frame.getHeight() - 200);
        setBorderAndTitle(bottom, "输入框", frame.getWidth() - 150, 160);
    }

    private void setBorderAndTitle(JPanel panel, String title, int width, int height) {
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setPreferredSize(new Dimension(width, height));
    }




    @Override
    public void destroy() {
    }
}
