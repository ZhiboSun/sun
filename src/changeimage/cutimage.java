package changeimage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class cutimage {
		public static void main(String args[]){
		String src = "E:\\数字图象处理\\作业二\\test.jpeg";
		String dest1 = "E:\\数字图象处理\\作业二\\剪切后的图片.jpg";
		
		//裁减四个顶点（x1,y1）（x1,y2）（x2,y1）（x2,y2）
		int x1 = 0;
		int x2 = 500;
		int y1 = 0;
		int y2 = 300;
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
		BufferedImage destimage1 = cutimage(srcimage,x1,x2,y1,y2
				);
		writeHighQuality(destimage1, dest1);
	}

	private static BufferedImage cutimage(BufferedImage srcimage, int x1, int x2, int y1, int y2) {
		// TODO Auto-generated method stub
		int width = srcimage.getWidth();
		int height = srcimage.getHeight();
		int newwidth = Math.abs(x2-x1);
		int newheight = Math.abs(y2-y1);
		BufferedImage tempBufferedImage = new BufferedImage(newwidth, newheight, BufferedImage.TYPE_INT_RGB);
		if(x1>=0&&x1<=width&&x2>=0&&x2<=width&&y1>=0&&y1<=height&&x1>=0&&y2<=height){
			for (int i = 0; i < newwidth; i++) {
				for (int j = 0; j < newheight; j++) {
					tempBufferedImage.setRGB(i, j, srcimage.getRGB(i-x1, j-y1));
				}
			}
		}else {
			System.out.println("输入坐标有误！");
		}			
		return tempBufferedImage;
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
