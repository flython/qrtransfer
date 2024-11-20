package org.wowtools.qrtransfer.receiver.logic;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ResizeImgUtil {

    public static BufferedImage resizeImage(BufferedImage original, int targetWidth, int targetHeight) {
        // 创建目标大小的 BufferedImage
        BufferedImage resized = new BufferedImage(targetWidth, targetHeight, original.getType());

        // 获取 Graphics2D 对象来执行缩放操作
        Graphics2D g2d = resized.createGraphics();

        // 设置高质量的图像缩放
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 执行缩放
        g2d.drawImage(original, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();

        return resized;
    }
}
