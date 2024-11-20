package org.wowtools.qrtransfer.common.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author liuyu
 * @date 2020/9/30
 */
public class DisclaimerUi extends JFrame {

    private final String text =
            "开始校验文件MD5……";


    public DisclaimerUi(String title, ActionListener onAgree) throws HeadlessException {
        super(title);
        this.setSize(700, 400);
        DisclaimerUi ui = this;
        JButton agreeButton = new JButton("确认开始");
        this.add(agreeButton, BorderLayout.SOUTH);
        agreeButton.addActionListener((e) -> {
            ui.setVisible(false);
            onAgree.actionPerformed(e);
        });

        TextArea textArea = new TextArea();
        this.add(textArea, BorderLayout.CENTER);
        textArea.setText(text);

        this.setVisible(true);

        //关闭按钮
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

}
