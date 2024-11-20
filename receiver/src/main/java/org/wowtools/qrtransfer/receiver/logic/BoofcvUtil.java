package org.wowtools.qrtransfer.receiver.logic;

import boofcv.abst.fiducial.QrCodeDetector;
import boofcv.alg.fiducial.qrcode.QrCode;
import boofcv.factory.fiducial.FactoryFiducial;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class BoofcvUtil {
    static String parser(BufferedImage image) {
        // 将BufferedImage转换为GrayU8图像
        GrayU8 input = ConvertBufferedImage.convertFrom(image, (GrayU8) null);

        // 创建QrCodeDetector实例
        QrCodeDetector<GrayU8> detector = FactoryFiducial.qrcode(null, GrayU8.class);

        // 检测二维码区域
        detector.process(input);

        List<QrCode> detections = detector.getDetections();
        StringBuilder sb = new StringBuilder();
        for (QrCode qr : detections)
            // The message encoded in the marker
            sb.append(qr.message);
        return sb.toString();
    }

    public static byte[] parseQRCodeImage(BufferedImage img) {
        return Base64.getDecoder().decode(parser(img));
    }
}
