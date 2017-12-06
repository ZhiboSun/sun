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
	public int width;//bmp图片的宽度
	public int height;//bmp图片的高度
	public int[][]red;
	public int[][]green;
	public int[][]blue;//BMP数据数组,即一个像素的RGB分量的数据数组 
	public float[][]grey;
	
	public static void main(String arg[]){
		readBmp rBmp = new readBmp();
		String path = "E:\\数字图象处理\\作业一\\241.bmp";
		rBmp.init(path);
	}

	public float[][] init(String path) {
		// TODO Auto-generated method stub
		try {
			java.io.FileInputStream fileInputStream = new java.io.FileInputStream(path);
			java.io.BufferedInputStream bis= new java.io.BufferedInputStream(fileInputStream);
			
			// 建立两个字节数组来得到文件头和信息头的数据 
			byte[]headOfPic = new byte[14];
			bis.read(headOfPic,0,14);
			
			byte[]infoOfPic= new byte[40];
			bis.read(infoOfPic,0,40);
			
			// 翻译BMP文件的数据，即将字节数据转化为int数据 ,得到位图数据的宽和高  
			width = ChangeInt(infoOfPic, 7);  
			height = ChangeInt(infoOfPic, 11);
			System.out.println("width:"+width+"   height:"+height);
			grey = getInf(bis);// 调用可以将整个位图数据读取成byte数组的方法    
			
			fileInputStream.close();  
	        bis.close();  
	  
	        showUI(); // 创建BMP对象来显示图画 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//通过BMP文件地址创建文件输入流对象
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
        //24位bmp存储是按BGR BGR。。。 存的
        //bmp位图在存储时，图片数据部分的字节数一定是4的倍数，如果不够四的倍数，在每一行要补齐字节。
        //int skipwidth = 4 - width * 3 % 4;
        int skipwidth = 0;
        if(width*3%4!=0)
        {
        	skipwidth=4-width*3%4;
        }
       
        try {
			for(int i = height-1;i>=0;i--){
				for (int j = 0; j < width; j++) {
					// 这里遍历的时候，一定要注意本来像素是有RGB来表示，  
			        // 但是在存储的时候由于windows是小段存储，所以在内存中是BGR顺序。  
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
		f=new JFrame("BMP图像解析");
		f.setSize(width,height);
		f.setDefaultCloseOperation(3);
		f.setResizable(false);
		f.setLocationRelativeTo(null);
		
		MyPanel panel = new MyPanel();
		
		panel.setBounds(0,0,width,height);
        f.add(panel);//窗体添加panel  
        f.setVisible(true);//使窗体可见。
        
    	label = new JLabel("");// 创建标签
        label.setBounds(10, 10, 150, 20);// 设置标签大小和位置
        panel.add(label);// 应用标签
        
        //把标签放在最顶层
        f.getLayeredPane().setLayout(null);
        f.getLayeredPane().add(label,new Integer(Integer.MAX_VALUE));
        
        f.addMouseMotionListener(new MouseMotionListener(){
        	public void mouseMoved(MouseEvent me){
        	//获得鼠标当前位置的横，纵坐标
	        	int mouseX = (int)me.getPoint().getX();
	        	int mouseY =(int) me.getPoint().getY();
	        	//获得该处的rgb值
	        	int[] pixrgb = getPixel(mouseX,mouseY);
	        
	        	String outputsString = "R:"+pixrgb[0]+";G:"+pixrgb[1]+";B:"+pixrgb[2];
	        	System.out.println(outputsString);
	        	
	        	label.setText(outputsString);
	        	label.setLocation(mouseX + 15, mouseY);// 更新标签的位置 	 
	        	label.setForeground(Color.red);//label字体颜色
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
							g.setColor(new Color(red[i][j],green[i][j],blue[i][j]));   //原图              	
		                	//g.setColor(new Color((int)grey[i][j],(int)grey[i][j],(int)grey[i][j])); //灰度图
		                    g.fillRect(j, i, 1, 1);  	                
	                }  
	            }  
	        }  
	    }  
}