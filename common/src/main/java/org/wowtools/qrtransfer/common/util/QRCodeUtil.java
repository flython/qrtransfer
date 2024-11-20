package org.wowtools.qrtransfer.common.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.datamatrix.encoder.SymbolShapeHint;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuyu
 * @date 2020/9/28
 */
public class QRCodeUtil {

    private static final Map<DecodeHintType, Object> decode_hints =  new HashMap<>();
    private static final Map<EncodeHintType, Object> encode_hints =  new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(QRCodeUtil.class);

    static {
        decode_hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        decode_hints.put(DecodeHintType.TRY_HARDER, Boolean.FALSE);
        decode_hints.put(DecodeHintType.POSSIBLE_FORMATS, BarcodeFormat.QR_CODE);
//        decode_hints.put(DecodeHintType.ALSO_INVERTED, Boolean.TRUE);
//        decode_hints.put(DecodeHintType.PURE_BARCODE, Boolean.FALSE);

//        encode_hints.put(EncodeHintType.QR_VERSION,40);
        encode_hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
    };

    public static void generateQRCodeImage(byte[] bts, BufferedImage img) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int width = img.getWidth();
        BitMatrix bitMatrix = qrCodeWriter.encode(Base64.getEncoder().encodeToString(bts), BarcodeFormat.QR_CODE, width, width);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                img.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
    }

    public static byte[] parseQRCodeImage(BufferedImage img) {
        String str = null;
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(img);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Result result = new QRCodeReader().decode(binaryBitmap, decode_hints);//解码
            str = result.getText();//TODO 先解析base64，后续改为直接解析byte
            byte[] res = Base64.getDecoder().decode(str);
            return res;
        } catch (NotFoundException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
            return null;
        }
    }



}
