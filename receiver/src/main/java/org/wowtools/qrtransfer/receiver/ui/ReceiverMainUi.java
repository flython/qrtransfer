package org.wowtools.qrtransfer.receiver.ui;

import org.wowtools.qrtransfer.common.ui.LogTextArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;

/**
 * @author liuyu
 * @date 2020/9/28
 */
public class ReceiverMainUi extends JFrame {

    public static final LogTextArea logTextArea;

    public static final JLabel stateText;

    private static final ReceiverMainUi ui = new ReceiverMainUi();

    static {
        ui.setSize(500, 600);

        JPanel panel = new JPanel(new FlowLayout());
        ui.add(panel,BorderLayout.NORTH);

        //开始按钮
        StartButton startButton = new StartButton();
        panel.add(startButton);

        //自动按钮
        AutoButton autoButton = new AutoButton();
        panel.add(autoButton);

        //进度文本框
        stateText = new JLabel("等待开始");
        ui.add(stateText, BorderLayout.SOUTH);
        stateText.setHorizontalAlignment(JLabel.LEFT);

        //日志
        logTextArea = new LogTextArea(35);
        ui.add(logTextArea, BorderLayout.CENTER);

        //关闭按钮
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public static void start() {
        ui.setVisible(true);
    }
}
