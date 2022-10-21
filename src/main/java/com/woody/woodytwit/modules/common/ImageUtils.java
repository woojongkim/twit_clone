package com.woody.woodytwit.modules.common;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;

@Slf4j
public class ImageUtils {
  private final static String BASE64_PREFIX = "data:image/jpeg;base64,";

  public static String resizeDataURI(String dataURI, int width) throws IOException {
    byte[] decode = Base64.getDecoder().decode(dataURI.substring(BASE64_PREFIX.length()));

    log.info("input: {}kb", decode.length/1024);

    BufferedImage image = ImageIO.read(new ByteArrayInputStream(decode));
    BufferedImage resize = Scalr.resize(image, Method.ULTRA_QUALITY, Mode.FIT_TO_WIDTH, width);
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ImageIO.write(resize, "jpeg", byteArrayOutputStream);
    byte[] resizeBytes = byteArrayOutputStream.toByteArray();

    log.info("output: {}kb", resizeBytes.length/1024);
    return BASE64_PREFIX + Base64.getEncoder().encodeToString(resizeBytes);
  }
}
