package histogram;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import grey.*;

public class hisequalization {
	public static int[] hist;
	static togrey Togrey = new togrey();
	public static void main(String arg[]){
		String src = "E:\\����ͼ����\\��ҵ��\\test.jpg";
		hisequalization h = new hisequalization();
		hist = statistics(src);
		hisequalization(src);//ֱ��ͼ���⻯
		h.showhist(hist);//��ʾֱ��ͼ
			
			
	 }
	 private  void showhist(int[] hist) {
			// TODO Auto-generated method stub
		 	JFrame f=new JFrame("ֱ��ͼ");
			f.setSize(500,500);
			MyPanel panel = new MyPanel();
			panel.setBounds(0,0,500,500);
	        f.add(panel);//�������panel  
			f.setVisible(true);//ʹ����ɼ���
		 	
	  }
	 public class MyPanel extends javax.swing.JPanel {  
	     private static final long serialVersionUID = 1L;  
	     public void paint(Graphics g) {
	    	Graphics2D g2d = (Graphics2D) g;  
	    	int size = 300;  
	 		g2d.setPaint(Color.BLACK);    
	 		g2d.fillRect(0, 0, size, size);    
	 		g2d.setPaint(Color.WHITE);    
	 		g2d.drawLine(5, 250, 265, 250);       
	 		g2d.drawLine(5, 250, 5, 5);             
	 		g2d.setPaint(Color.GREEN);    
	 		int max = -1;  //�ҵ�ֱ��ͼ������ֵ  
	 		for (int i = 0; i < hist.length; i++) {
	 			if(hist[i]>max)
	 				max = hist[i];
	 		}
	 		float rate = 200.0f/((float)max);    
	 		int offset = 2;    
	 		for(int i=0; i<hist.length; i++) {    
	 		    int frequency = (int)(hist[i] * rate);    
	 		    g2d.drawLine(5 + offset + i, 250, 5 + offset + i, 250-frequency);    

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
			BufferedImage srciImage = ImageIO.read(srcFile);
			int height = srciImage.getHeight();
			int width = srciImage.getWidth();
			int[] greyarray = new int[width*height];
            greyarray = Togrey.toGrey(srciImage);//�ҶȻ�
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
	        c[0] = (a*hist[0]);  
	        for(int i=1; i<256; i++){  
	            c[i] = c[i-1]+(int)(a*hist[i]);  
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
	        
/*	        hisequalization temp = new hisequalization();
	        hist = statistics("E:\\����ͼ����\\��ҵ��\\ֱ��ͼ���⻯.jpg");
	        temp.showhist(hist);*/
	        
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
       		 System.out.println("���ؼ�����");
       	 }
            return true;  
        }catch (Exception e) {  
            return false;  
        } 
	}
}
