package com.wetrack.util;

import org.imgscalr.Scalr;
import org.slf4j.Logger;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by zhanghong on 16/1/4.
 */
public class ImageUtils {
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(ImageUtils.class);


    /**
     *
     * @param filePath 保存的路径
     * @param fileName 文件名
     * @param type png, jpg, ...
     * @param data base64数据
     * @param x 裁剪区域，起始x坐标
     * @param y 裁剪区域，起始y坐标
     * @param w 裁剪区域宽度
     * @param h 裁剪区域高度
     * @param scale 对图片进行缩放比例
     * @param cutFlag 是否需要裁剪和缩放
     * @throws IOException
     */
    public static void saveBase64ImageToFile(String filePath, String fileName,
                                     String type, String data, int x, int y, int w, int h, float scale,boolean cutFlag) throws Exception {
        //去掉前缀base64,
        String baseString = data.substring(data.indexOf("base64,")
                + "base64,".length());
        //把base64字符串解析成字节数组
        byte[] imageByte = DatatypeConverter.parseBase64Binary(baseString);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);

        //生成BufferedImage对象,便于进行裁剪和保存操作
        BufferedImage sourceImage = ImageIO.read(bis);
        bis.close();

        if(sourceImage == null){
            throw new Exception("图像对象为空，保存失败!");
        }

        BufferedImage targetImage = sourceImage;
        if(cutFlag){
            //对原图进行缩放
            BufferedImage scaledImage = Scalr.resize(sourceImage, Scalr.Method.SPEED,
                    (int) (sourceImage.getWidth() / scale),
                    (int) (sourceImage.getHeight() / scale));

            //对缩放后的图进行裁剪
            ImageFilter cropFilter = new CropImageFilter(x, y, w, h);
            Image img = Toolkit.getDefaultToolkit()
                    .createImage(
                            new FilteredImageSource(scaledImage.getSource(),
                                    cropFilter));

            //将处理后的image写入target
            targetImage = new BufferedImage(w, h,
                    BufferedImage.TYPE_INT_RGB);

            Graphics g = targetImage.getGraphics();
            g.drawImage(img, 0, 0, null);// 绘制小图
            g.dispose();
        }

        //写入文件
        File f = new File(filePath, fileName);
        f.setLastModified(System.currentTimeMillis());
        ImageIO.write(targetImage, type, f);

    }

}
