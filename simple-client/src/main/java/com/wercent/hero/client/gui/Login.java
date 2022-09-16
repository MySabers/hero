package com.wercent.hero.client.gui;

import com.wercent.hero.common.message.login.LoginRequestMessage;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class Login implements Gui {

    private final JFrame frame = new JFrame("Login");

    public AtomicBoolean LOGIN = new AtomicBoolean(false);
    public CountDownLatch WAIT_FOR_LOGIN = new CountDownLatch(1);

    private final Index index;


    /**
     * 如果是程序自动关闭，则不用关闭channel， 否则关闭channel
     */
    private boolean autoClose = false;

    private Channel channel;

    public Login(Index index) {
        this.index = index;
    }

    @Override
    public void process(Channel channel) {
        this.channel = channel;
        frame.setSize(300, 160);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        /* 创建面板，这个类似于 HTML 的 div 标签
         * 我们可以创建多个面板并在 JFrame 中指定位置
         * 面板中我们可以添加文本字段，按钮及其他组件。
         */
        JPanel panel = new JPanel();
        // 添加面板
        frame.add(panel);
        /*
         * 调用用户定义的方法并添加组件到面板
         */
        placeComponents(panel);

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


    private void placeComponents(JPanel panel) {

        /* 布局部分我们这边不多做介绍
         * 这边设置布局为 null
         */
        panel.setLayout(null);
        // 创建 JLabel
        JLabel userLabel = new JLabel("User:");
        /* 这个方法定义了组件的位置。
         * setBounds(x, y, width, height)
         * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
         */
        userLabel.setBounds(10,20,80,25);
        panel.add(userLabel);
        /*
         * 创建文本域用于用户输入
         */
        JTextField userText = new JTextField(20);
        userText.setBounds(100,20,165,25);
        panel.add(userText);
        // 输入密码的文本域
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10,50,80,25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100,50,165,25);
        panel.add(passwordText);
        // 创建登录按钮
        JButton loginButton = new JButton("login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);
        loginButton.addActionListener(e -> {
            String username = userText.getText();
            String password = String.valueOf(passwordText.getPassword());
            channel.writeAndFlush(new LoginRequestMessage(username, password));
            try {
                WAIT_FOR_LOGIN.await();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            if (!LOGIN.get()) {
                JOptionPane.showMessageDialog(null, "用户名或密码不正确!");
            } else {
                this.destroy();
                index.process(channel);
            }
        });
    }

    @Override
    public void destroy() {
        autoClose = true;
        frame.dispose();
    }

}
