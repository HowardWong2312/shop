package io.dubai.common.utils;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

@Slf4j
@UtilityClass
public class QRCodeUtils {

    /**
     * 默认宽度
     */
    private static final Integer WIDTH = 140;
    /**
     * 默认高度
     */
    private static final Integer HEIGHT = 140;

    /**
     * LOGO 默认宽度
     */
    private static final Integer LOGO_WIDTH = 22;
    /**
     * LOGO 默认高度
     */
    private static final Integer LOGO_HEIGHT = 22;

    /**
     * 图片格式
     */
    private static final String IMAGE_FORMAT = "png";
    private static final String CHARSET = "utf-8";
    /**
     * 原生转码前面没有 data:image/png;base64 这些字段，返回给前端是无法被解析
     */
    private static final String BASE64_IMAGE = "data:image/png;base64,%s";

    /**
     * 生成二维码，使用默认尺寸
     *
     * @param content 内容
     * @return
     */
    public String getBase64QRCode(String content) {
        return getBase64Image(content, WIDTH, HEIGHT, null, null, null);
    }

    /**
     * 生成二维码，使用默认尺寸二维码，插入默认尺寸logo
     *
     * @param content 内容
     * @param logoUrl logo地址
     * @return
     */
    public String getBase64QRCode(String content, String logoUrl) {
        return getBase64Image(content, WIDTH, HEIGHT, logoUrl, LOGO_WIDTH, LOGO_HEIGHT);
    }

    /**
     * 生成二维码
     *
     * @param content    内容
     * @param width      二维码宽度
     * @param height     二维码高度
     * @param logoUrl    logo 在线地址
     * @param logoWidth  logo 宽度
     * @param logoHeight logo 高度
     * @return
     */
    public String getBase64QRCode(String content, Integer width, Integer height, String logoUrl, Integer logoWidth, Integer logoHeight) {
        return getBase64Image(content, width, height, logoUrl, logoWidth, logoHeight);
    }

    private String getBase64Image(String content, Integer width, Integer height, String logoUrl, Integer logoWidth, Integer logoHeight) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BufferedImage bufferedImage = crateQRCode(content, width, height, logoUrl, logoWidth, logoHeight);
        try {
            ImageIO.write(bufferedImage, IMAGE_FORMAT, os);
        } catch (IOException e) {
            log.error("[生成二维码，错误{}]", e);
        }
        // 转出即可直接使用
        return String.format(BASE64_IMAGE, Base64.getEncoder().encodeToString(os.toByteArray()));
    }

    /**
     * 生成二维码
     *
     * @param content    内容
     * @param width      二维码宽度
     * @param height     二维码高度
     * @param logoUrl    logo 在线地址
     * @param logoWidth  logo 宽度
     * @param logoHeight logo 高度
     * @return
     */
    private BufferedImage crateQRCode(String content, Integer width, Integer height, String logoUrl, Integer logoWidth, Integer logoHeight) {
        if (!StringUtils.isEmpty(content)) {
            ServletOutputStream stream = null;
            HashMap<EncodeHintType, Comparable> hints = new HashMap<>(4);
            // 指定字符编码为utf-8
            hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
            // 指定二维码的纠错等级为中级
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            // 设置图片的边距
            hints.put(EncodeHintType.MARGIN, 2);
            try {
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
                BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                    }
                }
                if (!StringUtils.isEmpty(logoUrl)) {
                    insertLogo(bufferedImage, width, height, logoUrl, logoWidth, logoHeight);
                }
                return bufferedImage;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    try {
                        stream.flush();
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 二维码插入logo
     *
     * @param source     二维码
     * @param width      二维码宽度
     * @param height     二维码高度
     * @param logoUrl    logo 在线地址
     * @param logoWidth  logo 宽度
     * @param logoHeight logo 高度
     * @throws Exception
     */
    private void insertLogo(BufferedImage source, Integer width, Integer height, String logoUrl, Integer logoWidth, Integer logoHeight) throws Exception {
        // logo 源可为 File/InputStream/URL
        Image src = ImageIO.read(new URL(logoUrl));
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (width - logoWidth) / 2;
        int y = (height - logoHeight) / 2;
        graph.drawImage(src, x, y, logoWidth, logoHeight, null);
        Shape shape = new RoundRectangle2D.Float(x, y, logoWidth, logoHeight, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }


    /**
     * 获取二维码
     *
     * @param content 内容
     * @param output  输出流
     * @throws IOException
     */
    public void getQRCode(String content, OutputStream output) throws IOException {
        BufferedImage image = crateQRCode(content, WIDTH, HEIGHT, null, null, null);
        ImageIO.write(image, IMAGE_FORMAT, output);
    }

    /**
     * 获取二维码
     *
     * @param content 内容
     * @param logoUrl logo资源
     * @param output  输出流
     * @throws Exception
     */
    public void getQRCode(String content, String logoUrl, OutputStream output) throws Exception {
        BufferedImage image = crateQRCode(content, WIDTH, HEIGHT, logoUrl, LOGO_WIDTH, LOGO_HEIGHT);
        ImageIO.write(image, IMAGE_FORMAT, output);
    }

    /**
     * 获取二维码
     * 输入URL
     * @throws Exception
     */
    public static String getQRCode(String encode) throws WriterException, IOException {
        int width = 200;
        int height = 200;
        // 生成图片格式
        String format = "png";
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 调整二维码精度
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(encode,
                BarcodeFormat.QR_CODE, width, height, hints);

        //oss
        MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig();
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig);
        String ss= new Date().getTime() +".png";
        File file = new File(ss);
        ImageIO.write(image, "jpeg", file);
        MultipartFile multipartFile = fileToMultipartFile(file);
        //MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
        String url = OssUtils.uploadToOss(multipartFile);
        return url;
    }

    public static MultipartFile fileToMultipartFile(File file) {
        FileItem fileItem = createFileItem(file);
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        return multipartFile;
    }

    private static FileItem createFileItem(File file) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem("textField", "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }

}
