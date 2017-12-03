package zoomImage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import interpolation.bilinear_interpolation;
import interpolation.cubic_spline_interpolation;

public class zoom_Image {
	public BufferedImage zoomImage(String src , Double zoomsize, String des1, String des2 , String des3){
		BufferedImage result_image = null; 
		
		File srcImage = new File(src);
		if(!srcImage.exists()){
			System.out.println("图片源文件不存在！");
		}
		
		try {
			BufferedImage srcimage = ImageIO.read(srcImage);
			int height = srcimage.getHeight();
			int width = srcimage.getWidth();
			
			int newwidth = (int) (width * zoomsize);  
            int newheight = (int) (height * zoomsize);
            
            BufferedImage result_image1 = new BufferedImage(newwidth, newheight, BufferedImage.TYPE_3BYTE_BGR);
            BufferedImage result_image2 = new BufferedImage(newwidth, newheight, BufferedImage.TYPE_3BYTE_BGR);
            BufferedImage result_image3 = new BufferedImage(newwidth, newheight, BufferedImage.TYPE_3BYTE_BGR);
            
            int newpicwidth = result_image1.getWidth();
            int newpicheight = result_image1.getHeight();
            
            for(int i = 0; i< newpicwidth; i++){
            	for (int j = 0; j < newpicheight; j++) {
            		double x0 = i/zoomsize;
            		double y0 = j/zoomsize;
                    int x = (int) Math.round(x0);  
                    int y = (int) Math.round(y0);      
                    nearestNeighbor(srcimage, height, width, result_image1, i, j, x, y);
            		if (x > 1 && x < width && y > 1 && y < height){
            			bilinear_interpolation inp = new bilinear_interpolation();
            			int newrgb2 = inp.bilinear_interpolation(srcimage, height, width, x0, y0, x, y);
            			result_image2.setRGB(i, j, newrgb2);                  
            		}else {
            			result_image2.setRGB(i, j, 0);
            		}
            		if (x > 1 && x < width && y > 1 && y < height){
            			cubic_spline_interpolation inp = new cubic_spline_interpolation();
            			int newrgb3 = inp.cubic_spline_interpolation(srcimage, height, width, x0, y0, x, y);
            			result_image3.setRGB(i, j, newrgb3);                  
            		}else {
            			result_image3.setRGB(i, j, 0);
            		}
				}
			}
            writeHighQuality(result_image1, des1);
            writeHighQuality(result_image2, des2);
            writeHighQuality(result_image3, des3);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		
		return result_image;
	}
	
	public static void nearestNeighbor(BufferedImage srcimage, int height, int width, BufferedImage result_image, int i,
			int j, int x, int y) {
		if (x > 1 && x < width && y > 1 && y < height){
			result_image.setRGB(i, j, srcimage.getRGB(x, y));     
		}else {
			result_image.setRGB(i, j, 0);
		}
	}
	
	public boolean writeHighQuality(BufferedImage im, String fileFullPath) {  
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
	
	public static void main(String[] args) {  
         
        String inputFoler = "E:\\数字图象处理\\作业二\\test.jpeg" ;   
         /*这儿填写你存放要缩小图片的文件夹全地址*/  
        String outputFolder = "E:\\数字图象处理\\作业二\\缩放后的图片.jpg";    
        /*这儿填写你转化后的图片存放的文件夹*/  
        
        double size = 2;
        zoom_Image zoom = new zoom_Image();
		String des1 = "E:\\数字图象处理\\作业二\\缩放后的图片1.jpg"; 
		String des2 = "E:\\数字图象处理\\作业二\\缩放后的图片2.jpg";
		String des3 = "E:\\数字图象处理\\作业二\\缩放后的图片3.jpg";

        boolean testOut = zoom.writeHighQuality(zoom.zoomImage(inputFoler , size, des1, des2, des3), outputFolder);  
        if(testOut){
            System.out.println("图像变换成功！");
        }  
    }  
}
