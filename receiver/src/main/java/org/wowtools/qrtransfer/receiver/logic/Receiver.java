package org.wowtools.qrtransfer.receiver.logic;

import org.wowtools.qrtransfer.common.pojo.FileHead;
import org.wowtools.qrtransfer.common.util.Constant;
import org.wowtools.qrtransfer.receiver.pojo.Page;
import org.wowtools.qrtransfer.receiver.ui.ReceiverMainUi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Base64;
import java.util.concurrent.locks.LockSupport;

/**
 * 消息接收器
 *
 * @author liuyu
 * @date 2020/9/28
 */
public class Receiver {

    private static final Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    private static final int KeyEvent_NextPage;
    private static final int KeyEvent_BeforePage;
    private static final int KeyEvent_Empty = Integer.MAX_VALUE;

    static {
        KeyEvent_NextPage = KeyEvent.getExtendedKeyCodeForChar(Constant.Key_NextPage);
        KeyEvent_BeforePage = KeyEvent.getExtendedKeyCodeForChar(Constant.Key_BeforePage);
    }

    private static long startTime;
    private static long endTime = -1;


    private static void setState(String text) {
        text += " cost:" + (System.currentTimeMillis() - startTime) + "ms";
        ReceiverMainUi.stateText.setText(text);
    }

    private static void setPageState(FileHead fileHead) {
        long startPageTime = System.currentTimeMillis();
        String baseText = "总计:" + fileHead.getFileSizeStr();
        new Thread(() -> {
            while (endTime < 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                StringBuilder sb = new StringBuilder(baseText);
                long percent = FileWriter.getSize() * 100 / fileHead.getFileSize();
                sb.append("已完成:").append(percent).append("%");
                if (percent > 0) {
                    long cost = System.currentTimeMillis() - startPageTime;
                    float speed = FileWriter.getSize() * 1000f / 1024 / cost;
                    sb.append(" 平均速度:").append(String.format("%.3f", speed)).append("kb/s");
                    long remain = (100 - percent) / percent * (cost / 1000);
                    sb.append(" 剩余:").append(remain).append("秒");

                }
                setState(sb.toString());
            }
            setState("校验md5..");
            while (null == FileWriter.getMd5()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
            log("md5,src:" + fileHead.getMd5() + ",tar:" + FileWriter.getMd5());
            if (!fileHead.getMd5().equals(FileWriter.getMd5())) {
                setState("md5不一致，请手工验证是否需要重新下载");
            } else {
                setState("完成");
            }
        }).start();
    }


    public static synchronized void start() {
        //读文件头
        startTime = System.currentTimeMillis();
        setState("读取头文件..");
        ReceiverMainUi.stateText.setText("");
        log("读取头");
        FileHead fileHead;
        byte[] headBytes = null;
        while (true) {
            try {
                byte[] bytes = CaptureScreen.encode();
                if (null == bytes) {
                    log("未检测到二维码，重试");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    continue;
                }
                fileHead = new FileHead(bytes);
                headBytes = bytes;
                break;
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                log("读取头失败，重试");
            }
        }
        log("读取头完成");
        setPageState(fileHead);
        int oldPageNum = -1;
        int keyEvent = KeyEvent_NextPage;
        while (true) {
            Page page;
            try {
                if (KeyEvent_Empty != keyEvent && Config.autoPressKey) {
                    robot.keyPress(keyEvent);
                    robot.keyRelease(keyEvent);
                    log("按下 " + KeyEvent.getKeyText(keyEvent));
                }
//                if (Config.pageDelay > 0) {
//                    Thread.sleep(Config.pageDelay);
//                }
                byte[] bytes = CaptureScreen.encode();
                if (null == bytes) {//未读出二维码
                    log("resize重试");
                    bytes = CaptureScreen.encodeBoofcv();
                    if (null == bytes && Config.autoPressKey){
                        String s = showInputDialog("重试请取消，或手动输入Base64校正,取消后到重新打开自动模式前不会再弹出此窗口");
                        if (s != null){
                            bytes = Base64.getDecoder().decode(s);
                        }
                    }
                    //仍然为空只能进入手动模式了
                    if (bytes == null){                 log("未读出到二维码,已自动切换成手动模式，请读取成功或fix后切换成自动模式");
                        Config.autoPressKey = false;
                        keyEvent = KeyEvent_Empty;
                        continue;
                    }
                }
                if (oldPageNum == -1) {//判断二维码是否是头信息
                    if (headBytes.length == bytes.length) {
                        boolean eq = true;
                        for (int i = 0; i < headBytes.length; i++) {
                            if (headBytes[i] != bytes[i]) {
                                eq = false;
                            }
                        }
                        if (eq) {
                            keyEvent = KeyEvent_NextPage;
                            continue;
                        }
                    }
                }
                page = PageParser.parse(bytes);
                log("page " + page.getPageNum() + ", size " + page.getBytes().length);
                if (page.getPageNum() == oldPageNum) {
                    keyEvent = KeyEvent_Empty;
                    Thread.sleep(Config.pageDelay);
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                keyEvent = KeyEvent_Empty;
                continue;
            }
            if (page.getPageNum() != oldPageNum + 1) {//跳页处理
                log("发生跳页，解析到第" + oldPageNum + "页，当前解析页" + page.getPageNum());
                if (page.getPageNum() > oldPageNum + 1) {
                    keyEvent = KeyEvent_BeforePage;
                } else {
                    keyEvent = KeyEvent_NextPage;
                }
                continue;
            } else {//写入文件
                FileWriter.write(page.getBytes());
            }
            oldPageNum = page.getPageNum();
            if (null != page.getMsg() && page.getMsg().equals(Constant.Flag_End)) {
                break;
            }
            log("page " + page.getPageNum());
            keyEvent = KeyEvent_NextPage;
        }
        FileWriter.close();
        endTime = System.currentTimeMillis();
        log("end " + fileHead.toString());
    }

    private static void log(String str) {
        ReceiverMainUi.logTextArea.log(str);
    }

    // 显示输入框并阻塞当前线程，直到用户点击按钮
    static String showInputDialog(String message) {
        // 使用 JOptionPane 显示输入对话框并返回用户输入的内容
        String input = JOptionPane.showInputDialog(null, message);
        return input;
    }
}
