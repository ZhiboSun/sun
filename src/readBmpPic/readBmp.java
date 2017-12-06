package readBmpPic;

import java.awt.AWTException;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.BufferedInputStream;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class readBmp  extends javax.swing.JFrame {
	private JFrame f; 
	private JLabel label;
	public int width;//bmpͼƬ�Ŀ��
	public int height;//bmpͼƬ�ĸ߶�
	public int[][]red;
	public int[][]green;
	public int[][]blue;//BMP��������,��һ�����ص�RGB�������������� 
	public float[][]grey;
	
	public static void main(String arg[]){
		readBmp rBmp = new readBmp();
		String path = "E:\\����ͼ����\\��ҵһ\\241.bmp";
		rBmp.init(path);
	}

	public float[][] init(String path) {
		// TODO Auto-generated method stub
		try {
			java.io.FileInputStream fileInputStream = new java.io.FileInputStream(path);
			java.io.BufferedInputStream bis= new java.io.BufferedInputStream(fileInputStream);
			
			// ���������ֽ��������õ��ļ�ͷ����Ϣͷ������ 
			byte[]headOfPic = new byte[14];
			bis.read(headOfPic,0,14);
			
			byte[]infoOfPic= new byte[40];
			bis.read(infoOfPic,0,40);
			
			// ����BMP�ļ������ݣ������ֽ�����ת��Ϊint���� ,�õ�λͼ���ݵĿ�͸�  
			width = ChangeInt(infoOfPic, 7);  
			height = ChangeInt(infoOfPic, 11);
			System.out.println("width:"+width+"   height:"+height);
			grey = getInf(bis);// ���ÿ��Խ�����λͼ���ݶ�ȡ��byte����ķ���    
			
			fileInputStream.close();  
	        bis.close();  
	  
	        showUI(); // ����BMP��������ʾͼ�� 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//ͨ��BMP�ļ���ַ�����ļ�����������
		return grey;
	}

	public int ChangeInt(byte[] infoOfPic, int i) {
		// TODO Auto-generated method stub
		int number = (int)((infoOfPic[i] & 0xff) << 24)
				|((infoOfPic[i-1] & 0xff) << 16)
				|((infoOfPic[i-2] & 0xff) << 8)
				|(infoOfPic[i-3] & 0xff);
		return number;
	}
	
	public float[][] getInf(BufferedInputStream bis) {
		// TODO Auto-generated method stub
		red = new int[height][width];  
        green = new int[height][width];  
        blue = new int[height][width];  
        grey  = new float[height][width];
        //24λbmp�洢�ǰ�BGR BGR������ ���
        //bmpλͼ�ڴ洢ʱ��ͼƬ���ݲ��ֵ��ֽ���һ����4�ı�������������ĵı�������ÿһ��Ҫ�����ֽڡ�
        //int skipwidth = 4 - width * 3 % 4;
        int skipwidth = 0;
        if(width*3%4!=0)
        {
        	skipwidth=4-width*3%4;
        }
       
        try {
			for(int i = height-1;i>=0;i--){
				for (int j = 0; j < width; j++) {
					// ���������ʱ��һ��Ҫע�Ȿ����������RGB����ʾ��  
			        // �����ڴ洢��ʱ������windows��С�δ洢���������ڴ�����BGR˳��  
						blue[i][j] = bis.read();  
						green[i][j] = bis.read();  
						red[i][j] = bis.read();
						grey[i][j] = (blue[i][j]*299 + green[i][j]*587 + red[i][j]*114) / 1000;
				}
				if (skipwidth!=0) {  
			        bis.skip(skipwidth);  
			    }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return grey;
	}
	

	public void showUI() {
		// TODO Auto-generated method stub
		f=new JFrame("BMPͼ�����");
		f.setSize(width,height);
		f.setDefaultCloseOperation(3);
		f.setResizable(false);
		f.setLocationRelativeTo(null);
		
		MyPanel panel = new MyPanel();
		
		panel.setBounds(0,0,width,height);
        f.add(panel);//�������panel  
        f.setVisible(true);//ʹ����ɼ���
        
    	label = new JLabel("");// ������ǩ
        label.setBounds(10, 10, 150, 20);// ���ñ�ǩ��С��λ��
        panel.add(label);// Ӧ�ñ�ǩ
        
        //�ѱ�ǩ�������
        f.getLayeredPane().setLayout(null);
        f.getLayeredPane().add(label,new Integer(Integer.MAX_VALUE));
        
        f.addMouseMotionListener(new MouseMotionListener(){
        	public void mouseMoved(MouseEvent me){
        	//�����굱ǰλ�õĺᣬ������
	        	int mouseX = (int)me.getPoint().getX();
	        	int mouseY =(int) me.getPoint().getY();
	        	//��øô���rgbֵ
	        	int[] pixrgb = getPixel(mouseX,mouseY);
	        
	        	String outputsString = "R:"+pixrgb[0]+";G:"+pixrgb[1]+";B:"+pixrgb[2];
	        	System.out.println(outputsString);
	        	
	        	label.setText(outputsString);
	        	label.setLocation(mouseX + 15, mouseY);// ���±�ǩ��λ�� 	 
	        	label.setForeground(Color.red);//label������ɫ
        	}
        	
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
        });
	}
	
	public int[] getPixel(int x,int y){
		  int[] pixrgb=new int[3];
		  try {
			  Robot cm = new Robot();
		      Color color = cm.getPixelColor(x, y);  
			  
			  pixrgb[0]=color.getRed();
			  pixrgb[1]=color.getGreen();
			  pixrgb[2]=color.getBlue();
			  
		  } catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		  }
		  return pixrgb;
	} 

	  public class MyPanel extends javax.swing.JPanel {  
	        private static final long serialVersionUID = 1L;  
	        public void paint(Graphics g) {  
	            for (int i = 0; i < height; i++) {  
	                for (int j = 0; j < width; j++) { 
							g.setColor(new Color(red[i][j],green[i][j],blue[i][j]));   //ԭͼ              	
		                	//g.setColor(new Color((int)grey[i][j],(int)grey[i][j],(int)grey[i][j])); //�Ҷ�ͼ
		                    g.fillRect(j, i, 1, 1);  	                
	                }  
	            }  
	        }  
	    }  
}