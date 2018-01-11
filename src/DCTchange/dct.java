package DCTchange;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import grey.*;

public class dct {
	public static int width = 0;//图片的宽度
	public static int height = 0;//图片的高度
	public static float[][] grey;
	public static DCT2D DCT2d = new DCT2D();
	public static int ta = 255;
	public static int newrgb = 0;
	public static void main(String[] args) {

		try {
			File srcFile  = new File("E:\\数字图象处理\\作业五\\test3.jpg");
			if (!srcFile.exists()) {
				System.out.println("找不到源文件");
			}
			BufferedImage srcimage = ImageIO.read(srcFile);
			height = srcimage.getHeight();
			width = srcimage.getWidth();
			if (height!=width) {
				System.out.println("输入图片不是正方形");
			}
			double srcgreyr[][]=new double[width][height];
			double srcgreyg[][]=new double[width][height];
			double srcgreyb[][]=new double[width][height];
			
			double greyr[][]=new double[width][height];
			double greyg[][]=new double[width][height];
			double greyb[][]=new double[width][height];
			
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					srcgreyr[i][j]=getPixel(srcimage, width, height, i, j)[0];
					srcgreyg[i][j]=getPixel(srcimage, width, height, i, j)[1];
					srcgreyb[i][j]=getPixel(srcimage, width, height, i, j)[2];
				}
			}
			greyr = DCT2d.DCTconvertion(srcgreyr);
			greyg = DCT2d.DCTconvertion(srcgreyg);
			greyb = DCT2d.DCTconvertion(srcgreyb);
			BufferedImage desimagedct = new BufferedImage(width,height, BufferedImage.TYPE_3BYTE_BGR);
			
			
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) { 
					newrgb =(ta << 24) | ((int)greyr[i][j]<< 16)|  ((int)greyg[i][j]<< 8)|(int)greyb[i][j];
					desimagedct.setRGB(i, j, newrgb);
				}
			}
			//输出频域滤波之后的频谱图像
			writeHighQuality(desimagedct, "E:\\数字图象处理\\作业五\\DCT频谱图像.jpg");
			
			//输出频域滤波之后的空域图像
			BufferedImage desifftimage = new BufferedImage(width,height, BufferedImage.TYPE_3BYTE_BGR);
			desifftimage = IDCT(greyr,greyg,greyb);
			writeHighQuality(desifftimage, "E:\\数字图象处理\\作业五\\IDCT之后的图像.jpg");
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
	private static BufferedImage IDCT(double[][] greyr, double[][] greyg, double[][] greyb) throws IOException {
		greyr = DCT2d.IDCTconvertion(greyr);
		greyg = DCT2d.IDCTconvertion(greyg);
		greyb = DCT2d.IDCTconvertion(greyb);
		BufferedImage desimage = new BufferedImage(width,height, BufferedImage.TYPE_3BYTE_BGR);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				newrgb =(ta << 24) | ((int)greyr[i][j]<< 16)|  ((int)greyg[i][j]<< 8)|(int)greyb[i][j];
				desimage.setRGB(i, j, newrgb);
			}
		}
		return desimage;
	}
	
}
