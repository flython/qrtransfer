package org.wowtools.qrtransfer.sender.ui;

import org.wowtools.qrtransfer.common.pojo.FileHead;
import org.wowtools.qrtransfer.common.protobuf.QrPageProto;
import org.wowtools.qrtransfer.common.ui.LogTextArea;
import org.wowtools.qrtransfer.common.util.QRCodeUtil;
import org.wowtools.qrtransfer.sender.logic.Config;
import org.wowtools.qrtransfer.sender.logic.FileReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 主界面
 *
 * @author liuyu
 * @date 2020/9/27
 */
public class SenderMainUi extends JFrame {

    public static final QrCodeCanvas qrCodeCanvas;

    public static final PageTurner pageTurner;

    public static final LogTextArea logTextArea;

    static {
        SenderMainUi ui = new SenderMainUi("MD5 checker");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ui.setSize(screenSize.width, screenSize.height);    //设置Frame的大小
        ui.setLayout(new GridLayout(2,3,10,10));

        List<QrCodeCanvas> qrCodeList = IntStream.range(0, 5).mapToObj(i -> {
            QrCodeCanvas canvas = new QrCodeCanvas(Config.qrCodeWidth, String.valueOf(i).charAt(0));
            //注册对应翻页
            PageTurner pt = new PageTurner(canvas.nextKey);
            canvas.addKeyListener(pt);
            return canvas;
        }).collect(Collectors.toList());
        qrCodeList.forEach(ui::add);
        JPanel controlPanel = new JPanel();
        ui.add(controlPanel);

        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));

//        //日志
        logTextArea = new LogTextArea(35);
        controlPanel.add(logTextArea);

        //关闭按钮
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        ui.setVisible(true);         //设置Frame为可见
        ui.setResizable(false);

    }


    public static void start() throws Exception {
        FileHead head = FileReader.readHead(Config.tar);
        QRCodeUtil.generateQRCodeImage(head.toByte(), qrCodeCanvas.img);
        SenderMainUi.qrCodeCanvas.paint(SenderMainUi.qrCodeCanvas.getGraphics());
        Iterator<QrPageProto.QrPagePb> pages = FileReader.readPage(Config.tar);
        pageTurner.setPages(pages);
        logTextArea.log("文件载入完成, md5:" + head.getMd5() + ", size:" + head.getFileSizeStr());
    }

    public SenderMainUi(String str) {
        super(str);     //调用父类的构造方法
    }
}
