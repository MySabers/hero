package com.wercent.hero.client.gui;

import io.netty.channel.Channel;

import javax.swing.*;

public interface Gui {
    /**
     * GUI 内容
     * @param channel netty channel
     */
    void process(Channel channel);

    /**
     * 销毁方法
     */
    void destroy();

}
