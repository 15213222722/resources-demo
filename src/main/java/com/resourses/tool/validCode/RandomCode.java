package com.resourses.tool.validCode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.resourses.tool.common.CommUtil;

/**
 * 
 * <p>
 * Title: RandomCode.java
 * </p>
 * 
 * <p>
 * Description: 图片验证码生成工具类，用来前端登录注册等验证码控制
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016
 * </p>
 * 
 * <p>
 * Company: 吾空网络股份有限公司 www.wukow.com
 * </p>
 * 
 * @author dgh
 * 
 * @date 2016-2-9
 * 
 * @version wukow_o2o v1.0 2016版
 */
public class RandomCode {
	// 验证码字符个数
	static int codeCount = 4;
	// 验证码干扰线数
	static int lineCount = 60;
	// 验证码图片Buffer
	static BufferedImage buffImg = null;
	static Graphics g = null;
	static Random random = new Random();

	/**
	 * 生成验证码第一步,可设置验证码相关参数
	 * 
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 */
	public static BufferedImage creatImage1(String w, String h) {
		int width = 86;// 默认宽度
		int height = 40;// 默认高度
		if (!CommUtil.null2String(w).equals("")) {
			width = CommUtil.null2Int(w);
		}
		if (!CommUtil.null2String(h).equals("")) {
			height = CommUtil.null2Int(h);
		}
		creatImage2(width, height);
		return buffImg;
	}

	/**
	 * 生成验证码第二步，开始生成验证码
	 * 
	 * @param width
	 * @param height
	 */
	public static void creatImage2(int width, int height) {
		int fontWidth = width / codeCount;// 字体的宽度
		int fontHeight = height - 5;// 字体的高度
		int codeY = height - 8;
		// 图像buffer
		buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g = buffImg.getGraphics();
		// Graphics2D g = buffImg.createGraphics();
		// 设置背景色
		g.setColor(getRandColor(230, 250));
		g.fillRect(0, 0, width, height);

		// 设置字体
		Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
		g.setFont(font);
		// 设置干扰线
		for (int i = 0; i < lineCount; i++) {
			int xs = random.nextInt(width);
			int ys = random.nextInt(height);
			int xe = xs + random.nextInt(width);
			int ye = ys + random.nextInt(height);
			g.setColor(getRandColor(1, 250));
			g.drawLine(xs, ys, xe, ye);
		}

		// 添加噪点
		float yawpRate = 0.01f;// 噪声率
		int area = (int) (yawpRate * width * height);
		for (int i = 0; i < area; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);

			buffImg.setRGB(x, y, random.nextInt(255));
		}

		String str1 = CommUtil.randomString(4);// 得到随机字符
		for (int i = 0; i < codeCount; i++) {
			String strRand = str1.substring(i, i + 1);
			g.setColor(getRandColor(1, 229));
			g.drawString(strRand, i * fontWidth + 3, codeY);
		}
	}

	// 得到随机字符
	public static String randomStr(int n) {
		String str1 = "1234567890";
		String str2 = "";
		int len = str1.length() - 1;
		double r;
		for (int i = 0; i < n; i++) {
			r = (Math.random()) * len;
			str2 = str2 + str1.charAt((int) r);
		}
		return str2;
	}

	// 得到随机颜色
	public static Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 输出到页面
	 * 
	 * @param request
	 * @param response
	 * @param w
	 * @param h
	 * @throws IOException
	 */
	public static void createCodeImg(HttpServletRequest request, HttpServletResponse response, String w, String h)
			throws IOException {
		creatImage1(w, h);
		// 图象生效
		g.dispose();
		ServletOutputStream responseOutputStream = response.getOutputStream();
		// 输出图象到页面
		ImageIO.write(buffImg, "JPEG", responseOutputStream);
		// 以下关闭输入流！
		responseOutputStream.flush();
		responseOutputStream.close();
	}

	/**
	 * 输出图片到本地
	 * 
	 * @param w
	 * @param h
	 * @throws IOException
	 */
	public static void createCodeImg(String w, String h) throws IOException {
		creatImage1(w, h);
		// 图象生效
		g.dispose();
		File file = new File("f:/code.JPG");
		// 输出图象到文件
		ImageIO.write(buffImg, "JPG", file);
	}

}
