package com.wercent.hero.client.gui;

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
        frame.setSize(700, 500);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.setLayout(new BorderLayout());
        JPanel left = new JPanel();
        JPanel right = new JPanel();

        right.setLayout(new BorderLayout());

        // 添加面板
//        frame.add(panel);
        /*
         * 调用用户定义的方法并添加组件到面板
         */
//        placeComponents(panel);

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


    @Override
    public void destroy() {

    }
}
