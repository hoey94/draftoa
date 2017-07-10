package com.starsoft.core.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
/***
 * 二维码工具
 * @author lenovo
 *
 */
public class QrCodeUtil {
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;
	private static final String path = "d:/systemInfo/qrcodeImage/";
	private static final String resultPath="/systemInfo/qrcodeImage/";
	// 图片宽度的一般  
    private static final int IMAGE_WIDTH = 50;  
    private static final int IMAGE_HEIGHT = 50;  
    private static final int IMAGE_HALF_WIDTH = IMAGE_WIDTH / 2;  
    private static final int FRAME_WIDTH = 2; 
	
	public static void main(String[] args) {
//		generateUrlImage("http://jiuye.ncwu.edu.cn/jiuye/AndroidClient.apk","zsjy");
		Map<String,String> map=new HashMap<String,String> ();
		map.put("NAME", "新疆大枣批发");
		map.put("TEL", "13253566856");
		map.put("ADR", "郑州市南四环大学路百荣世贸商城A5区12排276号");
		map.put("EMAIL", "715210092@qq.com");
		map.put("ORG", "新疆红鑫源厂家直销");
		map.put("TITLE", "总经理");
		//map.put("URL", "http://www.zhxwl.cn");
		map.put("NOTE", "新疆和田大枣、哈密枣、骏枣、健康情枣！");
//		generateBusinessCardImage(map);
//		generate4eduQrImage(qrMap);
//	    encode("http://www.jiaxiaozhushou.com/webClientVersion.do", 300, 300, "F:\\logo.gif", "F:\\2013-01.jpg");
	}
	/***
	 * 用网址生产二维码信息
	 * @param url
	 */
	public static final String generateUrlImage(String url,String fileNameEn){
		if(fileNameEn==null||fileNameEn.equals("")){
			fileNameEn=StringUtil.generator();
		}
		String fileName=fileNameEn+".jpg";
		try {
		     MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		     Map hints = new HashMap();
		     hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		     BitMatrix bitMatrix = multiFormatWriter.encode(url, BarcodeFormat.QR_CODE, 400, 400,hints);
		     File file1 = new File(path,fileName);
		     writeToFile(bitMatrix, "jpg", file1);
		 } catch (Exception e) {
		     e.printStackTrace();
		 }
		return resultPath+fileName;
	}
	
	/***
	 * 生产4edu信息
	 * @param url
	 */
	public static final String generate4eduQrImage(Map<String, Object> map){
		String fileName="";
		String content = "";
		if(map.containsKey("imageId")){
			fileName = (String)map.get("imageId") + ".jpg";
		}
		if(map.containsKey("url")){
			content = (String)map.get("url");
		}
		try {
		     MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		     Map hints = new HashMap();
		     hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		     BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400,hints);
		     int[] rec = bitMatrix.getEnclosingRectangle();  
		     int resWidth = rec[2] + 1;  
		     int resHeight = rec[3] + 1;  
		     BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);  
		     resMatrix.clear();  
		     for (int i = 0; i < resWidth; i++) {  
		         for (int j = 0; j < resHeight; j++) {  
		             if (bitMatrix.get(i + rec[0], j + rec[1])) { 
		                  resMatrix.set(i, j); 
		             } 
		         }  
		     } 
		     File file1 = new File(path,fileName);
		     writeToFile(resMatrix, "jpg", file1);
		    // addLogo_QRCode(file1,logofile,new LogoConfig());
		 } catch (Exception e) {
		     e.printStackTrace();
		 }
		return resultPath+fileName;
	}
	
	
	/***
	 * 生产二维码名片信息
	 * @param url
	 */
	public static final String generateBusinessCardImage(Map<String,String> map){
		String fileName=StringUtil.generator()+".jpg";
		try {
	 		 String NAME=map.containsKey("NAME")?map.get("NAME"):"";//姓名
			 String TEL=map.containsKey("TEL")?map.get("TEL"):"";//联系电话
			 String ADR=map.containsKey("ADR")?map.get("ADR"):"";//联系地址
			 String EMAIL=map.containsKey("EMAIL")?map.get("EMAIL"):"";//联系邮件地址
			 String ORG=map.containsKey("ORG")?map.get("ORG"):"";//所属单位
			 String TITLE=map.containsKey("TITLE")?map.get("TITLE"):"";//所在职位
			 String URL=map.containsKey("URL")?map.get("URL"):"";//个人网站地址
			 String NOTE=map.containsKey("NOTE")?map.get("NOTE"):"";//备注信息
			 String content="BEGIN:VCARD\nVERSION:3.0\n";
			 if(!NAME.equals("")){
				 content=content+"N:"+NAME+"\n";
			 }
			 if(!TEL.equals("")){
				 content=content+"TEL:"+TEL+"\n";
			 }
			 if(!ADR.equals("")){
				 content=content+"ADR:"+ADR+"\n";
			 }
			 if(!EMAIL.equals("")){
				 content=content+"EMAIL:"+EMAIL+"\n";
			 }
			 if(!ORG.equals("")){
				 content=content+"ORG:"+ORG+"\n";
			 }
			 if(!TITLE.equals("")){
				 content=content+"TITLE:"+TITLE+"\n";
			 }
			 if(!URL.equals("")){
				 content=content+"URL:"+URL+"\n";
			 }
			 if(!NOTE.equals("")){
				 content=content+"NOTE:"+NOTE+"\n";
			 }
			 content=content+"END:VCARD";
		     MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		     Map hints = new HashMap();
		     hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		     BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400,hints);
		     File file1 = new File(path,fileName);
		     writeToFile(bitMatrix, "jpg", file1);
		     File logofile=new File("D:\\logo.jpg");
		    // addLogo_QRCode(file1,logofile,new LogoConfig());
		 } catch (Exception e) {
		     e.printStackTrace();
		 }
		return resultPath+fileName;
	}
	/***
	 * 
	 * @param content
	 * @param width
	 * @param height
	 * @param srcImagePath
	 * @param destImagePath
	 */
	public static void encode(String content, int width, int height,
			String srcImagePath, String destImagePath) {
		try {
			ImageIO.write(genBarcode(content, width, height, srcImagePath),
					"jpg", new File(destImagePath));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}
	/***
	 * 生产网址图片，
	 * @param content
	 * @param width
	 * @param height
	 * @param srcImagePath logo图标
	 * @return
	 * @throws WriterException
	 * @throws IOException
	 */
	private static BufferedImage genBarcode(String content, int width,  
            int height, String srcImagePath) throws WriterException,  
            IOException {  
        // 读取源图像  
        BufferedImage scaleImage = scale(srcImagePath, IMAGE_WIDTH,  
                IMAGE_HEIGHT, true);  
        int[][] srcPixels = new int[IMAGE_WIDTH][IMAGE_HEIGHT];  
        for (int i = 0; i < scaleImage.getWidth(); i++) {  
            for (int j = 0; j < scaleImage.getHeight(); j++) {  
                srcPixels[i][j] = scaleImage.getRGB(i, j);  
            }  
        }  
   
        Map<EncodeHintType, Object> hint = new HashMap<EncodeHintType, Object>();  
        hint.put(EncodeHintType.CHARACTER_SET, "utf-8");  
        hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  
        // 生成二维码  
        // 二维码写码器  
        MultiFormatWriter mutiWriter = new MultiFormatWriter(); 
        BitMatrix matrix = mutiWriter.encode(content, BarcodeFormat.QR_CODE,  
                width, height, hint);  
   
        // 二维矩阵转为一维像素数组  
        int halfW = matrix.getWidth() / 2;  
        int halfH = matrix.getHeight() / 2;  
        int[] pixels = new int[width * height];  
   
        for (int y = 0; y < matrix.getHeight(); y++) {  
            for (int x = 0; x < matrix.getWidth(); x++) {  
                // 读取图片  
                if (x > halfW - IMAGE_HALF_WIDTH  
                        && x < halfW + IMAGE_HALF_WIDTH  
                        && y > halfH - IMAGE_HALF_WIDTH  
                        && y < halfH + IMAGE_HALF_WIDTH) {  
                    pixels[y * width + x] = srcPixels[x - halfW  
                            + IMAGE_HALF_WIDTH][y - halfH + IMAGE_HALF_WIDTH];  
                }   
                // 在图片四周形成边框  
                else if ((x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH  
                        && x < halfW - IMAGE_HALF_WIDTH + FRAME_WIDTH  
                        && y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH  
                        + IMAGE_HALF_WIDTH + FRAME_WIDTH)  
                        || (x > halfW + IMAGE_HALF_WIDTH - FRAME_WIDTH  
                                && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH  
                                && y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH  
                                + IMAGE_HALF_WIDTH + FRAME_WIDTH)  
                        || (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH  
                                && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH  
                                && y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH  
                                - IMAGE_HALF_WIDTH + FRAME_WIDTH)  
                        || (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH  
                                && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH  
                                && y > halfH + IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH  
                                + IMAGE_HALF_WIDTH + FRAME_WIDTH)) {  
                    pixels[y * width + x] = 0xfffffff;  
                } else {  
                    // 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；  
                    pixels[y * width + x] = matrix.get(x, y) ? 0xff000000 
                            : 0xfffffff;  
                }  
            }  
        }  
   
        BufferedImage image = new BufferedImage(width, height,  
                BufferedImage.TYPE_INT_RGB);  
        image.getRaster().setDataElements(0, 0, width, height, pixels);  
   
        return image;  
    }  
   
    /** 
     * 把传入的原始图像按高度和宽度进行缩放，生成符合要求的图标 
     *  
     * @param srcImageFile 
     *            源文件地址 
     * @param height 
     *            目标高度 
     * @param width 
     *            目标宽度 
     * @param hasFiller 
     *            比例不对时是否需要补白：true为补白; false为不补白; 
     * @throws IOException 
     */ 
    private static BufferedImage scale(String srcImageFile, int height,  
            int width, boolean hasFiller) throws IOException {  
        double ratio = 0.0; // 缩放比例  
        File file = new File(srcImageFile);  
        BufferedImage srcImage = ImageIO.read(file);  
        Image destImage = srcImage.getScaledInstance(width, height,  
                BufferedImage.SCALE_SMOOTH);  
        // 计算比例  
        if ((srcImage.getHeight() > height) || (srcImage.getWidth() > width)) {  
            if (srcImage.getHeight() > srcImage.getWidth()) {  
                ratio = (new Integer(height)).doubleValue()  
                        / srcImage.getHeight();  
            } else {  
                ratio = (new Integer(width)).doubleValue()  
                        / srcImage.getWidth();  
            }  
            AffineTransformOp op = new AffineTransformOp(  
                    AffineTransform.getScaleInstance(ratio, ratio), null);  
            destImage = op.filter(srcImage, null);  
        }  
        if (hasFiller) {// 补白  
            BufferedImage image = new BufferedImage(width, height,  
                    BufferedImage.TYPE_INT_RGB);  
            Graphics2D graphic = image.createGraphics();  
            graphic.setColor(Color.white);  
            graphic.fillRect(0, 0, width, height);  
            if (width == destImage.getWidth(null))  
                graphic.drawImage(destImage, 0,  
                        (height - destImage.getHeight(null)) / 2,  
                        destImage.getWidth(null), destImage.getHeight(null),  
                        Color.white, null);  
            else 
                graphic.drawImage(destImage,  
                        (width - destImage.getWidth(null)) / 2, 0,  
                        destImage.getWidth(null), destImage.getHeight(null),  
                        Color.white, null);  
            graphic.dispose();  
            destImage = image;  
        }  
        return (BufferedImage) destImage;  
    }  
	/***
	 * 获取二维码网址信息
	 * @param url
	 */
	public static final String getUrlImageContent(String imgpath){
		String resultContent="";
		try {
			MultiFormatReader formatReader = new MultiFormatReader();
			File file = new File(imgpath);
			BufferedImage image = ImageIO.read(file);
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			Binarizer binarizer = new HybridBinarizer(source);
			BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
			Map hints = new HashMap();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			Result result = formatReader.decode(binaryBitmap, hints);
			resultContent= result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultContent;
	}
	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}

	public static void writeToFile(BitMatrix matrix, String format, File file)
			throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, file)) {
			throw new IOException("Could not write an image of format "
					+ format + " to " + file);
		}
	}

	public static void writeToStream(BitMatrix matrix, String format,
			OutputStream stream) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, stream)) {
			throw new IOException("Could not write an image of format "
					+ format);
		}
	}
	
	/**
	 * 给二维码图片添加Logo
	 * 
	 * @param qrPic
	 * @param logoPic
	 */
	public static void addLogo_QRCode(File qrPic, File logoPic, LogoConfig logoConfig) {
		try {
			if (!qrPic.isFile() || !logoPic.isFile()) {
				System.out.print("file not find !");
			}else{
				/**
				 * 读取二维码图片并建立绘图对象
				 */
				BufferedImage image = ImageIO.read(qrPic);
				Graphics2D g = image.createGraphics();
				/**
				 * 读取Logo圖片
				 */
				BufferedImage logo = ImageIO.read(logoPic);
				int widthLogo = logo.getWidth(), heightLogo = logo.getHeight();
				// 计算图片放置位置
				int x = (image.getWidth() - widthLogo) / 2;
				int y = (image.getHeight() - logo.getHeight()) / 2;
				// 开会绘制图片
				g.drawImage(logo, x, y, widthLogo, heightLogo, null);
				g.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
				g.setStroke(new BasicStroke(logoConfig.getBorder()));
				g.setColor(logoConfig.getBorderColor());
				g.drawRect(x, y, widthLogo, heightLogo);
				g.dispose();
				ImageIO.write(image, "jpeg", new File("D:/newPic.jpg"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
