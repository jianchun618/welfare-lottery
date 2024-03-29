package com.fmbank.welfarelottery.service.Impl;

import com.fmbank.welfarelottery.service.IFileFormatConvertService;
import com.luciad.imageio.webp.WebPReadParam;
import com.luciad.imageio.webp.WebPWriteParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Slf4j
@Component
public class FileFormatConvertServiceImpl implements IFileFormatConvertService {
    /**
     * JPG/PNG格式图片转成webp格式
     *
     * @param oldFile 待转换文件
     * @param newFile 转换后文件
     * @return boolean
     */
    @Override
    public boolean jpgToWebp(String oldFile, String newFile) {
        boolean result = false;
        ImageWriter writer = null;
        FileImageOutputStream fileImageOutputStream = null;
        try {
            //获取原始文件的编码
            BufferedImage image = ImageIO.read(new File(oldFile));
            //创建WebP ImageWriter实例
            writer = ImageIO.getImageWritersByMIMEType("image/webp").next();
            //配置编码参数
            WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
            //设置压缩模式
            writeParam.setCompressionMode(WebPWriteParam.MODE_EXPLICIT);
            // 设置无损
            writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSLESS_COMPRESSION]);
            //配置ImageWriter输出
            fileImageOutputStream = new FileImageOutputStream(new File(newFile));
            writer.setOutput(fileImageOutputStream);
            //进行编码，重新生成新图片
            writer.write(null, new IIOImage(image, null, null), writeParam);
            log.info("jpg文件转成webp格式成功");
            result = true;
        } catch (FileNotFoundException e) {
            log.error("文件转换失败,文件不存在-[{}]", e.getMessage(), e);
        } catch (Exception e) {
            log.error("文件转换失败-[{}]", e.getMessage(), e);
        } finally {
            if (!ObjectUtils.isEmpty(fileImageOutputStream)) {
                try {
                    fileImageOutputStream.close();
                } catch (IOException e) {
                    log.error("关闭异常-[{}]", e.getMessage(), e);
                }
            }
        }
        return result;
    }

    /**
     * webp格式图片转成JPG/PNG格式
     *
     * @param oldFile 待转换文件
     * @param newFile 转换后文件
     * @return boolean
     */
    @Override
    public boolean webpToJpg(String oldFile, String newFile) {
        boolean result = false;
        FileImageInputStream fileImageInputStream = null;
        try {
            //创建WebP ImageReader实例
            ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();
            //配置解码参数
            WebPReadParam readParam = new WebPReadParam();
            readParam.setBypassFiltering(true);
            //在ImageReader设置读取的原文件
            fileImageInputStream = new FileImageInputStream(new File(oldFile));
            reader.setInput(fileImageInputStream);
            //解码图像
            BufferedImage image = reader.read(0, readParam);
            //设置输入文件的格式和文件名
            //这里也可以使用其他图片格式，但是格式和文件名后缀要保持一致
            ImageIO.write(image, "png", new File(newFile));
            log.info("webp文件转成png格式成功");
            result = true;
        } catch (Exception e) {
            log.info("文件转换失败-[{}]", e.getMessage(), e);
        } finally {
            if (!ObjectUtils.isEmpty(fileImageInputStream)) {
                try {
                    fileImageInputStream.close();
                } catch (IOException e) {
                    log.error("关闭异常-[{}]", e.getMessage(), e);
                }
            }
        }
        return result;
    }


}
