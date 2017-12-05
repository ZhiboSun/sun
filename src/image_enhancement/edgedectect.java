package image_enhancement;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import grey.*;

public class edgedectect {
	public static void image_enhance(String pathname, String des1 , String des2){
		try {
			togrey Togrey = new togrey();
			File srcFile = new File(pathname);
			BufferedImage srcImage = ImageIO.read(srcFile);
			int height = srcImage.getHeight();
			int width = srcImage.getWidth();
			int[] greyarray = new int[width*height];
			
			BufferedImage result_image1 = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            BufferedImage result_image2 = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            greyarray = Togrey.toGrey(srcImage);
            
            int newgrey = 0;
            for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					if(i!=0&&j!=0&&i!=width-1&&j!=height-1){
						newgrey = prewitt(greyarray,i,j,width,height);
						result_image1.setRGB(i, j, newgrey<<16|newgrey<<8|newgrey);	
						newgrey = laplace(greyarray,i,j,width,height);
						result_image2.setRGB(i, j, newgrey<<16|newgrey<<8|newgrey);
					}
										
				}
			}
            writeHighQuality(result_image1, des1);
            writeHighQuality(result_image2, des2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int prewitt(int[] greyarray, int x, int y, int width, int height) {
		int sx =-greyarray[y-1+(x-1)*height]-greyarray[y+(x-1)*height]-greyarray[y+1+(x-1)*height]
				+greyarray[y-1+(x+1)*height]+greyarray[y+(x+1)*height]+greyarray[y+1+(x+1)*height];  
		int sy = greyarray[y-1+(x-1)*height]+greyarray[y-1+(x)*height]+greyarray[y+1+(x+1)*height]
				-greyarray[y+1+(x-1)*height]-greyarray[y+1+(x)*height]-greyarray[y+1+(x+1)*height];    
        int s  = Math.abs(sx)+Math.abs(sy);  
		return s;
	}
	public static int laplace(int[] greyarray, int x, int y, int width, int height) {
		int sx =-greyarray[y-1+(x-1)*height]-2*greyarray[y+(x-1)*height]-greyarray[y+1+(x-1)*height]
				+greyarray[y-1+(x+1)*height]+2*greyarray[y+(x+1)*height]+greyarray[y+1+(x+1)*height];  
		int sy = greyarray[y-1+(x-1)*height]+2*greyarray[y-1+(x)*height]+greyarray[y+1+(x+1)*height]
				-greyarray[y+1+(x-1)*height]-2*greyarray[y+1+(x)*height]-greyarray[y+1+(x+1)*height];  
        int s  = Math.abs(sx)+Math.abs(sy);  
		return s;
	}
	public static boolean writeHighQuality(BufferedImage im, String fileFullPath) {  
        try {  
            /*输出到文件流*/  
       	 if(im != null){
       		 ImageIO.write(im, "JPG", new File(fileFullPath));   
       		 System.out.println("边沿检测完成");
       	 }
            return true;  
        }catch (Exception e) {  
            return false;  
        } 
	}
	
	public static void main(String args[]) {
		String src = "E:\\数字图象处理\\作业四\\test.jpg";
		String des1 = "E:\\数字图象处理\\作业四\\prewitt.jpg"; 
		String des2 = "E:\\数字图象处理\\作业四\\Laplace.jpg";
		image_enhance(src,des1,des2);
	}
}
