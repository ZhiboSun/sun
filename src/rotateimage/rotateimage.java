package rotateimage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import interpolation.bilinear_interpolation;
import interpolation.cubic_spline_interpolation;

public class rotateimage {
	public static void RotIamge(String srcImage, String des1, String des2, String des3, double angle ) {
		double sita = angle*Math.PI/180;
		
		File srcFile = new File(srcImage);
		if (!srcFile.exists()) {
			System.out.println("找不到源文件");
		}
		try {
			BufferedImage srcimage = ImageIO.read(srcFile);
			int height = srcimage.getHeight();
			int width = srcimage.getWidth();
			System.out.println(width+"*"+height);
			
			//原图像的四个顶点旋转中心坐标(a,b)
			double a = (width-1)/2.0;
			double b = (height-1)/2.0;
			
			//旋转后的四个顶点坐标

		    double x1 = -a * Math.cos(sita) - b * Math.sin(sita);
		    double y1 = -a * Math.sin(sita) + b * Math.cos(sita);

		    double x2 = a * Math.cos(sita) - b * Math.sin(sita);
		    double y2 = a * Math.sin(sita) + b * Math.cos(sita);

		    double x3 = a * Math.cos(sita) + b * Math.sin(sita);
		    double y3 = a * Math.sin(sita) - b * Math.cos(sita);

		    double x4 = -a * Math.cos(sita) + b * Math.sin(sita);
		    double y4 = -a * Math.sin(sita) - b * Math.cos(sita);
		    
		    //新图像的宽度和高度
		    int newheight = (int) Math.round(Math.max(Math.abs(y1-y3), Math.abs(y4-y2)));
		    int newwidth  = (int) Math.round(Math.max(Math.abs(x1-x3), Math.abs(x4-x2)));
			System.out.println(newwidth+"*"+newheight);

		    //新图像的旋转中心(c,d)
		    double c = (newwidth-1)/2.0;
		    double d = (newheight-1)/2.0;
		    
		    double f1 = -c * Math.cos(sita) - d * Math.sin(sita) + a;  
		    double f2 =  c * Math.sin(sita) - d * Math.cos(sita) + b;  
		    
            BufferedImage result_image1 = new BufferedImage(newwidth, newheight, BufferedImage.TYPE_3BYTE_BGR);
            BufferedImage result_image2 = new BufferedImage(newwidth, newheight, BufferedImage.TYPE_3BYTE_BGR);
            BufferedImage result_image3 = new BufferedImage(newwidth, newheight, BufferedImage.TYPE_3BYTE_BGR);

            int newpicwidth = result_image1.getWidth();
            int newpicheight = result_image1.getHeight();
            for(int i = 0; i< newpicwidth; i++){
            	for (int j = 0; j < newpicheight; j++) {
            		double x0 =  i * Math.cos(sita) + j * Math.sin(sita) + f1;
            		double y0 = -i * Math.sin(sita) + j * Math.cos(sita) + f2;
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
			e.printStackTrace();
		}
	}

	public static void nearestNeighbor(BufferedImage srcimage, int height, int width, BufferedImage result_image, int i,
			int j, int x, int y) {
		if (x > 1 && x < width && y > 1 && y < height){
			result_image.setRGB(i, j, srcimage.getRGB(x, y));     
		}else {
			result_image.setRGB(i, j, 0);
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
	public static void main(String args[]) {
		String src = "E:\\数字图象处理\\作业二\\timg.jpg";
		String des1 = "E:\\数字图象处理\\作业二\\旋转后nearestNeighbor的图片1.jpg"; 
		String des2 = "E:\\数字图象处理\\作业二\\旋转后bilinear_interpolation的图片2.jpg";
		String des3 = "E:\\数字图象处理\\作业二\\旋转后cubic_spline_interpolation的图片3.jpg";
		double angle = 55.55;
		RotIamge(src, des1, des2, des3, angle);
	}
}
