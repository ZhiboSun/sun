package changeimage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class mirrorimage {
	public static void main(String args[]){
		String src = "E:\\数字图象处理\\作业二\\test.jpeg";
		String dest1 = "E:\\数字图象处理\\作业二\\水平镜像后的图片.jpg";
		String dest2 = "E:\\数字图象处理\\作业二\\垂直镜像后的图片.jpg";
		
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
		BufferedImage destimage1 = hmirror(srcimage);
		BufferedImage destimage2 = vmirror(srcimage);
		writeHighQuality(destimage1, dest1);
		writeHighQuality(destimage2, dest2);
	}

	private static BufferedImage vmirror(BufferedImage srcimage) {
		// TODO Auto-generated method stub
		int width = srcimage.getWidth();
		int height = srcimage.getHeight();
		BufferedImage temBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				temBufferedImage.setRGB(i, height-j-1, srcimage.getRGB(i, j));
			}
		}
		return temBufferedImage;
	}

	private static BufferedImage hmirror(BufferedImage srcimage) {
		// TODO Auto-generated method stub
		int width = srcimage.getWidth();
		int height = srcimage.getHeight();
		BufferedImage temBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				temBufferedImage.setRGB(width-i-1, j, srcimage.getRGB(i, j));
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
