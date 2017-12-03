package Fourier;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.xml.bind.annotation.XmlElementDecl.GLOBAL;

import FFT.*;
import readBmpPic.readBmp;
import readBmpPic.readBmp.MyPanel;

public class Fourier {
	public static int width = 0;//图片的宽度
	public static int height = 0;//图片的高度
	public static int newwidth = 0;//频域图片的宽度
	public static int newheight = 0;//频域图片的高度
	public static float[][] gray;
	public static FFT2D FFT2d = new FFT2D();
	
	public static void main(String[] args) {

		try {
			File srcFile  = new File("E:\\数字图象处理\\作业五\\test.jpg");
			if (!srcFile.exists()) {
				System.out.println("找不到源文件");
			}
			BufferedImage srcimage = ImageIO.read(srcFile);
			height = srcimage.getHeight();
			width = srcimage.getWidth();
			int grayr[][]=new int[width][height];
			int grayg[][]=new int[width][height];
			int grayb[][]=new int[width][height];
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					grayr[i][j]=getPixel(srcimage, width, height, i, j)[0];
					grayg[i][j]=getPixel(srcimage, width, height, i, j)[1];
					grayb[i][j]=getPixel(srcimage, width, height, i, j)[2];
				}
			}

			double a = Math.log(width)/Math.log(2);
			double b = Math.log(height)/Math.log(2);
			newwidth = (int) Math.pow(2,(int) Math.ceil(a));
			newheight = (int) Math.pow(2,(int) Math.ceil(b));
			Complex[][] Complexgrayr = new Complex[newwidth][newheight];
			Complex[][] Complexgrayg = new Complex[newwidth][newheight];
			Complex[][] Complexgrayb = new Complex[newwidth][newheight];
			for (int i = 0; i < newwidth; i++) {
				for (int j = 0; j < newheight; j++) {
					if(i>=width|j>=height){
						Complexgrayr[i][j]= new Complex(0,0);
						Complexgrayg[i][j]= new Complex(0,0);
						Complexgrayb[i][j]= new Complex(0,0);
					}else{
						Complexgrayr[i][j] = new Complex(grayr[i][j], 0);
						Complexgrayg[i][j] = new Complex(grayg[i][j], 0);
						Complexgrayb[i][j] = new Complex(grayb[i][j], 0);
					}
				}
			}
			
			Complex[][] gr = FFT2d.fft2d(Complexgrayr);
			Complex[][] gg = FFT2d.fft2d(Complexgrayg);
			Complex[][] gb = FFT2d.fft2d(Complexgrayb);
			
			BufferedImage desimage = new BufferedImage(newwidth,newheight, BufferedImage.TYPE_3BYTE_BGR);
			BufferedImage desimageILPF = new BufferedImage(newwidth,newheight, BufferedImage.TYPE_3BYTE_BGR);
			Complex rgb[] = new Complex[3];
			int newrgb=0;
			int ii=0;
			int jj=0;
			for (int i = 0; i < newwidth; i++) {
				for (int j = 0; j < newheight; j++) {
					if (i < newwidth / 2) {  
	                    ii = i + newwidth / 2;  
	                } else {  
	                    ii = i - newwidth / 2;  
	                }  
	                if (j < newheight / 2) {  
	                    jj = j + newheight / 2;  
	                } else {  
	                    jj = j - newheight / 2;  
	                } 
	                rgb = BLPF(ii,jj,gr[i][j],gg[i][j],gb[i][j]);
	                int ta = 255;
	                gr[i][j]=rgb[0];
	                gg[i][j]=rgb[1];
	                gb[i][j]=rgb[2];
					newrgb =(ta << 24) | ((int)rgb[0].abs()/100 << 16)|  ((int)rgb[1].abs()/100 << 8)|(int) rgb[2].abs()/100;
	                desimageILPF.setRGB(ii, jj, newrgb);
					desimage.setRGB(ii, jj, newrgb);
				}
			}
			//writeHighQuality(desimage, "E:\\数字图象处理\\作业五\\fft1.jpg");
			writeHighQuality(desimageILPF, "E:\\数字图象处理\\作业五\\BLPF.jpg");
			IFFT(gr,gg,gb);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static int[] getPixel(BufferedImage srcImage, int width, int height, int x0, int y0) {
        if (x0 >= width) {
        	x0 = width - 1;
        }
        if (x0 < 0) {
        	x0 = 0;
        }
        if (y0 < 0) {
        	y0 = 0;
        }
        if (y0 >= height) {
        	y0 = height - 1;
        }
        int srcrgb = srcImage.getRGB(x0,y0);
        int[] rgb = new int[3];
        rgb[0] = (srcrgb >> 16) & 0xff;
        rgb[1] = (srcrgb >> 8) & 0xff;
        rgb[2] = srcrgb & 0xff;
        return rgb;
    }
	public static boolean writeHighQuality(BufferedImage im, String fileFullPath) {  
        try {  
            /*输出到文件流*/  
       	 if(im != null){
       		 ImageIO.write(im, "JPG", new File(fileFullPath));               
       	 }
            return true;  
        }catch (Exception e) {  
            return false;  
        } 
	}
	private static void IFFT(Complex[][] gr, Complex[][] gg, Complex[][] gb) throws IOException {
		Complex[][] newgr = FFT2d.ifft2d(gr);
		Complex[][] newgg = FFT2d.ifft2d(gg);
		Complex[][] newgb = FFT2d.ifft2d(gb);
		BufferedImage desimageILPF1 = new BufferedImage(newwidth,newheight, BufferedImage.TYPE_3BYTE_BGR);
		int newrgb=0;
		for (int i = 0; i < newwidth; i++) {
			for (int j = 0; j < newheight; j++) {
				int ta = 255;
				newrgb =(ta << 24) | ((int)newgr[i][j].abs() << 16)|  ((int)newgg[i][j].abs() << 8)|(int) newgb[i][j].abs();
				desimageILPF1.setRGB(i, j, newrgb);
			}
		}
		//writeHighQuality(desimage, "E:\\数字图象处理\\作业五\\fft1.jpg");
		writeHighQuality(desimageILPF1, "E:\\数字图象处理\\作业五\\BLPF1.jpg");
	}
	//几种滤波器
	//理想低通滤波
	public static Complex[] ILPF(int ii, int jj, Complex gr, Complex gg, Complex gb){
		int f = 800;//截止频率
		if(ii*ii+jj*jj>f*f){
			gr = gg = gb = new Complex(0,0);
		}
		Complex rgb[] = new Complex[3];
		rgb[0] = gr;
		rgb[1] = gg;
		rgb[2] = gb;
		return rgb;
	}
	//巴特沃斯低通滤波
	public static Complex[] BLPF(int ii, int jj, Complex gr, Complex gg, Complex gb){
		int D0 = 80;//截止频率
		double D = Math.sqrt(ii*ii+jj*jj);//距离
		int n = 1;//滤波器阶数
		double H = 1/(1+Math.pow((D/D0),2*n));
		gr = gr.times(H);
		gg = gg.times(H);
		gb = gb.times(H);
		Complex rgb[] = new Complex[3];
		rgb[0] = gr;
		rgb[1] = gg;
		rgb[2] = gb;
		return rgb;
	}
	
}
