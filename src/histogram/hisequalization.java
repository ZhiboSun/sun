package histogram;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import grey.*;

public class hisequalization {
	public static int[] hist;
	static togrey Togrey = new togrey();
	static int height = 0;
	static int width = 0;
	public static void main(String arg[]){
		String src = "E:\\����ͼ����\\��ҵ��\\test1.jpg";
		hisequalization h = new hisequalization();
		hist = statistics(src);
		hisequalization(src);//ֱ��ͼ���⻯
		h.showhist(hist);//��ʾֱ��ͼ
		
		String src1 = "E:\\����ͼ����\\��ҵ��\\ֱ��ͼ���⻯.jpg";
		hist = statistics(src1);
		h.showhist(hist);//��ʾֱ��ͼ
			
	 }
	 private  void showhist(int[] hist) {
			// TODO Auto-generated method stub
		 	JFrame f=new JFrame("ֱ��ͼ");
			f.setSize(width,height+30);
			MyPanel panel = new MyPanel();
			panel.setBounds(0,0,500,500);
	        f.add(panel);//�������panel  
			f.setVisible(true);//ʹ����ɼ���
		 	
	 }
	 public class MyPanel extends javax.swing.JPanel {  
	     private static final long serialVersionUID = 1L;  
	     public void paint(Graphics g) {
	    	Graphics2D g2d = (Graphics2D) g;   
	 		g2d.setPaint(Color.LIGHT_GRAY);  //����ɫ  
	 		g2d.fillRect(0, 0, width, height);    //�����ߴ�
	 		
	 		//XY������
	 		//drawLine(int x1, int y1, int x2, int y2) 
	 		//x1,y1  Ϊ��������
	 		//x2,y2  Ϊ�յ������
	 		g2d.setPaint(Color.BLACK);
	 		g2d.drawLine(50, 50, 50, height-50);    
	 		g2d.drawLine(50, height-50, width-50, height-50); 
	 		
	 		int maxFrequency = -1;  //�ҵ�ֱ��ͼ������ֵ  
	 		for (int i = 0; i < hist.length; i++) {
	 			if(hist[i]>maxFrequency)
	 				maxFrequency = hist[i];
	 		}
	 		
	 		//XY��title
	 		g2d.drawString("0", 50, height-30);
	 		g2d.drawString("255", width-80, height-30);
	 		g2d.drawString("0", 20, height-50);
	 		g2d.drawString(""+maxFrequency, 20, 50);
	 		
	 		//��ͼ
	 		double xunit = (width-100.0)/256.0d;
	 		double yunit = (height-100.0)/maxFrequency;
   
	 		for(int i=0; i<hist.length; i++) {    
	 		   double xp = 50+xunit*i;
	 		   double yp = yunit*hist[i];
	 		   //����һ���� Rectangle2D���������ʼ��Ϊ��λ�� (0, 0)����С (0, 0)��
	 		   //Rectangle2D.Double(double x, double y, double w, double h)
	 		   Rectangle2D rectangle2d = new Rectangle2D.
	 				Double(xp,height-50-yp,xunit,yp);
	 		   g2d.fill(rectangle2d);
	 		}         
	 		g2d.setPaint(Color.RED);    
	 		g2d.drawString("", 100, 270);
	     }  
	 }  
	private static int[] statistics(String src) {		
		// TODO Auto-generated method stub
		int[] temphist = new int[256];
		try {
			File srcFile = new File(src);
			BufferedImage srcImage = ImageIO.read(srcFile);
			height = srcImage.getHeight();
			width = srcImage.getWidth();
			int[] greyarray = new int[width*height];
            greyarray = Togrey.toGrey(srcImage);//�ҶȻ�
            for(int i=0;i<width*height;i++){
            	temphist[greyarray[i]]++; 
            }//ͳ�ƻҶ�����
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temphist;	
	}
	private static void hisequalization(String src) {
		// TODO Auto-generated method stub
		File srcFile = new File(src);
		BufferedImage srciImage;
		try {
			srciImage = ImageIO.read(srcFile);
			int height = srciImage.getHeight();
			int width = srciImage.getWidth();
			int[] greyarray = new int[width*height];
	        greyarray = Togrey.toGrey(srciImage);//�ҶȻ�
			BufferedImage greyImage =new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);//���⻯֮��
			BufferedImage greyImage1 =new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);//ԭͼ
			
			double a = (double)255/(width*height);  
	        double[] c = new double [256];  
	        double[] temphist = new double [256]; 
	        temphist[0] = hist[0];
	        c[0] = (int)(a*hist[0]+0.5);  
	        for(int i=1; i<256; i++){
	        	temphist[i] = hist[i]+temphist[i-1];
	            c[i] = (int)(a*temphist[i]+0.5); 
	            System.out.println(c[i]);
	        }  
	        for(int i=0; i<width; i++){  
	            for(int j=0; j<height; j++){
	            	//ԭͼ
	            	greyarray[i*height+j] = 255<<24|greyarray[i*height+j]<<16|greyarray[i*height+j]<<8|greyarray[i*height+j];
	            	greyImage1.setRGB(i, j, greyarray[i*height+j]);
	            	
	            	//���⻯֮��
	                int grey = greyarray[i*height+j]&0x0000ff;  
	                int hist = (int)(c[grey]);  
	                greyarray[i*height+j] = 255<<24|hist<<16|hist<<8|hist;  
	                greyImage.setRGB(i, j, greyarray[i*height+j]); 
	            }  
	        }
	        
	        writeHighQuality(greyImage1, "E:\\����ͼ����\\��ҵ��\\�Ҷ�ͼ.jpg");
	        writeHighQuality(greyImage, "E:\\����ͼ����\\��ҵ��\\ֱ��ͼ���⻯.jpg");
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static boolean writeHighQuality(BufferedImage im, String fileFullPath) {  
        try {  
            /*������ļ���*/  
       	 if(im != null){
       		 ImageIO.write(im, "JPG", new File(fileFullPath));   
       	 }
            return true;  
        }catch (Exception e) {  
            return false;  
        } 
	}
}
