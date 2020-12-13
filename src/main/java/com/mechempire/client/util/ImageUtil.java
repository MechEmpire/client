package com.mechempire.client.util;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * package: com.mechempire.client.util
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/13 下午5:52
 */
public class ImageUtil {

    /**
     * 图片对象转换
     *
     * @param image awt 图像对象
     * @return javax 图像对象
     */
    public static Image awtImageToJavaxImage(java.awt.Image image) {
        try {
            if (!(image instanceof RenderedImage)) {
                BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
                        image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                Graphics g = bufferedImage.createGraphics();
                g.drawImage(image, 0, 0, null);
                g.dispose();
                image = bufferedImage;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write((RenderedImage) image, "png", out);
            out.flush();
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            return new Image(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}