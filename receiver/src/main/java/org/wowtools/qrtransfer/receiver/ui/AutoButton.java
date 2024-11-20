package org.wowtools.qrtransfer.receiver.ui;

import org.wowtools.qrtransfer.receiver.logic.Config;
import org.wowtools.qrtransfer.receiver.logic.Receiver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * @author liuyu
 * @date 2020/9/28
 */
public class AutoButton extends JButton {

    public AutoButton() throws HeadlessException {
        super("切换手动");
        this.setLayout(null);
        this.setBounds(0, 0, 100, 30);
    }

    private final AutoButton ts = this;

    private final ActionListener onclick = e -> {
        doSwitch();
    };

    {
        this.addActionListener(onclick);
    }


    public void doSwitch(){
        if (Config.autoPressKey){
            this.setText("切换成自动");
        } else {
            this.setText("切换手动");
        }
        Config.autoPressKey = !Config.autoPressKey;
    }

}
