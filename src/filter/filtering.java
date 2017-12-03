package filter;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class filtering{
	public static void fliter(String srcpath ,String despath1, String despath2) {
		File srcFile  = new File(srcpath);
		if (!srcFile.exists()) {
			System.out.println("找不到源文件");
		}
		try {
			BufferedImage srcimage = ImageIO.read(srcFile);
			int height = srcimage.getHeight();
			int width = srcimage.getWidth();
			
			BufferedImage desimge1 = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			BufferedImage desimge2 = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
				desimge1.setRGB(j, i, average_filtering(srcimage,i,j));	
				desimge2.setRGB(j, i, median_filtering(srcimage,i,j));	
				
				}
			}
            writeHighQuality(desimge1, despath1);
            writeHighQuality(desimge2, despath2);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
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
	private static int[] getPixel(BufferedImage srcImage, int height, int width, int x0, int y0) {
        if (x0 >= height) {
        	x0 = height - 1;
        }
        if (x0 < 0) {
        	x0 = 0;
        }
        if (y0 < 0) {
        	y0 = 0;
        }
        if (y0 >= width) {
        	y0 = width - 1;
        }
        int srcrgb = srcImage.getRGB(y0,x0);
        int[] rgb = new int[3];
        rgb[0] = (srcrgb >> 16) & 0xff;
        rgb[1] = (srcrgb >> 8) & 0xff;
        rgb[2] = srcrgb & 0xff;
        return rgb;
    }
	//均值滤波
	public static int average_filtering(BufferedImage bufferedImage, int x, int y) {
		int newrgb =0;
		try {
			int r = 0;
			int g = 0;
			int b = 0;
			int[] rgb = new int[3];
			for(int i=-1; i<=1; i++){
				for (int j = -1; j <=1; j++) {
					rgb = getPixel(bufferedImage, bufferedImage.getHeight(), bufferedImage.getWidth() , x+i, y+j);
					r=r+rgb[0];
					g=g+rgb[1];
					b=b+rgb[2];
				}
			}
			r=r/9;
			b=b/9;
			g=g/9;
			int ta = 255;
			newrgb =(ta << 24) | (r << 16) | g << 8 | b;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newrgb;
	}
	//中值滤波
	public static int median_filtering(BufferedImage bufferedImage, int x, int y) {
		int newrgb=0;
		try {
			int r[] = new int[9];
			int g[] = new int[9];
			int b[] = new int[9];
			int[] rgb = new int[3];
			int k = 0;
			for(int i=-1; i<=1; i++){
				for (int j = -1; j <=1; j++) {
					rgb = getPixel(bufferedImage, bufferedImage.getHeight(), bufferedImage.getWidth() , x+i, y+j);
					r[k] = rgb[0];
					g[k] = rgb[1];
					b[k] = rgb[2];
					k++;
				}
			}
			int newr = bubbleSort(r);
			int newg = bubbleSort(g);
			int newb = bubbleSort(b);
			int ta = 255;
			newrgb = (ta << 24) |  (newr << 16) | newg << 8 | newb;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newrgb;		
	}
	public static int bubbleSort(int[] numbers) {   
	    int temp = 0;
	    for (int i = 1; i < numbers.length; i++) {
			for (int j = 1; j < numbers.length-i; j++) {
				if (numbers[j]>numbers[j+1]) {
					temp = numbers[j+1];
					numbers[j+1] = numbers[j];
					numbers[j] = temp;
				}
			}
		}
	    return numbers[(numbers.length+1)/2];
	}  
	public static void main(String args[]) {
		String src1 = "E:\\数字图象处理\\作业三\\椒盐噪声.jpg";
		String src2 = "E:\\数字图象处理\\作业三\\高斯噪声.jpg";

		String des1 = "E:\\数字图象处理\\作业三\\均值滤波椒盐后的图片1.jpg"; 
		String des2 = "E:\\数字图象处理\\作业三\\中值滤波椒盐后的图片2.jpg";
		String des3 = "E:\\数字图象处理\\作业三\\均值滤波高斯后的图片1.jpg"; 
		String des4 = "E:\\数字图象处理\\作业三\\中值滤波高斯后的图片2.jpg";
		fliter(src1,des1,des2);
		fliter(src2,des3,des4);
	}
}
