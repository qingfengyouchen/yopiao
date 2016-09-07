package com.zx.stlife.tools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.base.modules.util.ExceptionUtils;
import com.base.modules.util.FileUtilsEx;

/**
 * 
 * @author micheal
 * 
 */
public class ImageUtils {

	public static class ThumbnailConfig {
		private int width = -1;
		private int height = -1;
		private boolean autoThumb = false;
		private boolean autoCompress = false;
		private float compressQuality = 1.0f;

		public int getWidth() {
			return width;
		}

		public ThumbnailConfig setWidth(int width) {
			this.width = width;
			return this;
		}

		public int getHeight() {
			return height;
		}

		public ThumbnailConfig setHeight(int height) {
			this.height = height;
			return this;
		}

		public boolean isAutoThumb() {
			return autoThumb;
		}

		public ThumbnailConfig setAutoThumb(boolean autoThumb) {
			this.autoThumb = autoThumb;
			return this;
		}

		public boolean isAutoCompress() {
			return autoCompress;
		}

		public ThumbnailConfig setAutoCompress(boolean autoCompress) {
			this.autoCompress = autoCompress;
			return this;
		}

		public float getCompressQuality() {
			return compressQuality;
		}

		public ThumbnailConfig setCompressQuality(float compressQuality) {
			this.compressQuality = compressQuality;
			return this;
		}
	}

	/**
	 * 
	 * @param in
	 * @param compress
	 * @param compressQuality
	 */
	public static void compress(InputStream in, String compress, float compressQuality) {
		BufferedImage image = null;
		try {

			image = ImageIO.read(in);
			compress(image, compress, compressQuality);

		} catch (Exception e) {
			throw ExceptionUtils.toUnchecked(e);
		}
	}

	private static void compress(BufferedImage image, String compress, float compressQuality) {
		Assert.notNull(image, "image must not null");
		Assert.hasText(compress, "compress must not blank");

		OutputStream out = null;
		try {
			out = new FileOutputStream(compress);
			String formatName = compress.substring(compress.lastIndexOf(".") + 1);
			ImageIO.write(image, formatName, new File(compress));

//			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//			JPEGEncodeParam encodeParam = encoder.getDefaultJPEGEncodeParam(image);
//			encodeParam.setQuality(compressQuality, true);
//			encoder.encode(image, encodeParam);
		} catch (Exception e) {
			throw ExceptionUtils.toUnchecked(e);
		} finally {
			IOUtils.freeQuietly(out);
		}
	}

	/**
	 *
	 * @param in
	 * @param thumbnail
	 * @param width
	 * @param height
	 */
	public static void createThumbnail(InputStream in, String thumbnail, int width, int height) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(in);

			int imageWidth = image.getWidth();
			int imageHeight = image.getHeight();
			float scale = 0;

			if (imageWidth > width) {
				scale = (float) imageWidth / (float) width;
				width = Math.round((float) imageWidth / scale);
			}

			if (imageHeight > height && imageWidth > imageHeight) {
				scale = (float) imageHeight / (float) height;
				height = Math.round((float) imageHeight / scale);
			}

			if (imageWidth < width && imageHeight < height) {
				width = imageWidth;
				height = imageHeight;
			}
			createThumbnailInternal(image, thumbnail, width, height);
		} catch (Exception e) {
			throw ExceptionUtils.toUnchecked(e);
		}
	}

	private static void createThumbnailInternal(BufferedImage image, String thumbnail, int imageWidth, int imageHeight) throws IOException {
		Assert.hasText(thumbnail, "thumbnail must has text");

		BufferedImage thumb = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = thumb.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(image, 0, 0, imageWidth, imageHeight, null);
		g.dispose();

		String format = FileUtilsEx.getFileExtensionWithoutDot(thumbnail);
		ImageIO.write(thumb, StringUtils.isBlank(format) ? "jpg" : format, new File(thumbnail));
	}

	/**
	 * 
	 * @param in
	 * @param thumbnail
	 * @param config
	 */
	public static void createThumbnail(InputStream in, String thumbnail, ThumbnailConfig config) {
		createThumbnail(in, thumbnail, config.getWidth(), config.getHeight());
	}

	/**
	 * 
	 * @param dest
	 * @param watermark
	 */
	public static void watermark(File dest, File watermark) {
		BufferedImage destImage = null;
		try {
			destImage = ImageIO.read(dest);
			BufferedImage watermarkImage = ImageIO.read(watermark);

			int destImageWidth = destImage.getWidth();
			int destImageHeight = destImage.getHeight();

			// int pointX = Math.max(destImageWidth - watermarkImage.getWidth(),
			// 0);
			// int pointY = Math.max(destImageHeight -
			// watermarkImage.getHeight(), 0);
			int pointX = (destImageWidth - watermarkImage.getWidth());
			int pointY = (destImageHeight - watermarkImage.getHeight());
			int width = Math.min(destImageWidth, watermarkImage.getWidth());
			int height = Math.min(destImageHeight, watermarkImage.getHeight());

			Graphics g = destImage.getGraphics();
			g.drawImage(watermarkImage, pointX, pointY, width, height, null);
			// must dispose
			g.dispose();

			String format = FileUtilsEx.getFileExtensionWithoutDot(dest.getName());
			ImageIO.write(destImage, StringUtils.isBlank(format) ? "jpg" : format, dest);
		} catch (IOException e) {
			throw ExceptionUtils.toUnchecked(e);
		}
	}

	/**
	 * 
	 * @param dest
	 * @param watermark
	 */
	public static void watermark(String dest, String watermark) {
		watermark(new File(dest), new File(watermark));
	}
	
	
	/**
	 * 压缩为jpg图片
	 * @param path 原图片地址
	 * @param newPath 新图片地址
	 * @param quality 压缩质量(0< X <1)，一般0.5图片质量较好
	 */
	public static void compressionJPG(String path, String newPath, float quality){
		try {
			RenderedImage rendImage = ImageIO.read(new File(path));  
			
			 // 得到指定Format图片的writer  
            ImageWriter writer = null;  
            Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");  // 得到迭代器  
            if (iter.hasNext()) {  
                writer = (ImageWriter) iter.next();  // 得到writer 
            }  
            
            // 准备输出文件  
            ImageOutputStream ios = ImageIO.createImageOutputStream(new File(newPath));  
            writer.setOutput(ios);  
            
//          ImageWriteParam iwparam = new MyImageWriteParam();
            JPEGImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());  
            iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);  // 设置可否压缩 
            iwparam.setCompressionQuality(quality); // 设置压缩质量参数
            
            // 写图片  
            writer.write(null, new IIOImage(rendImage, null, null), iwparam);  
  
            // 最后清理  
            ios.flush();  
            writer.dispose();  
            ios.close();  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
