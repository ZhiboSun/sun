package changeimage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class translate_image {
	public static void main(String args[]){
		String src = "E:\\数字图象处理\\作业二\\test.jpeg";
		String dest1 = "E:\\数字图象处理\\作业二\\平移后的图片.jpg";
		
		File srcImage = new File(src);
		if(!srcImage.exists()){
			System.out.println("图片源文件不存在！");
		}
		BufferedImage srcimage = null;
		try {
			srcimage = ImageIO.read(srcImage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int x0 = -100;
		int y0 = -100;
		BufferedImage destimage1 = translate(srcimage,x0,y0);
		writeHighQuality(destimage1, dest1);
	}


	private static BufferedImage translate(BufferedImage srcimage ,int x0 ,int y0) {
		// TODO Auto-generated method stub
		int width = srcimage.getWidth();
		int height = srcimage.getHeight();
		BufferedImage temBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (i-x0>=0&&i-x0<srcimage.getWidth()&&j-y0>=0&&j-y0<srcimage.getHeight()) {
					temBufferedImage.setRGB(i, j, srcimage.getRGB(i-x0, j-y0));
				}else {
					temBufferedImage.setRGB(i, j, 0);
				}
				
			}
		}
		return temBufferedImage;
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
}
