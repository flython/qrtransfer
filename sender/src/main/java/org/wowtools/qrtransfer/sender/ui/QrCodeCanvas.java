package org.wowtools.qrtransfer.sender.ui;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 二维码框
 *
 * @author liuyu
 * @date 2020/9/27
 */
public class QrCodeCanvas extends Canvas {
    public final BufferedImage img;

    public final int width;

    public final char nextKey;

    public QrCodeCanvas(int width, char nextKey) {
        this.width = width;
        this.setBounds(0,0,width,width);
        img = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
        this.nextKey = nextKey;
    }

    public void paint(Graphics g) {
        g.drawImage(img, 0, 0 ,512,512,this);
    }
}
