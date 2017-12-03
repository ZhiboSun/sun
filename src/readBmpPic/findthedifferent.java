package readBmpPic;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedInputStream;
import java.io.IOException;

import readBmpPic.readBmp.MyPanel;

public class findthedifferent  extends javax.swing.JFrame {
		public int width;//bmp图片的宽度
		public int height;//bmp图片的高度
		public int[][]red;
		public int[][]green;
		public int[][]blue;//BMP数据数组,即一个像素的RGB分量的数据数组 
		
		public static void main(String arg[]){
			findthedifferent find_different = new findthedifferent();
			find_different.init();
		}

		public void init() {
			// TODO Auto-generated method stub
			try {
				java.io.FileInputStream fileInputStream = new java.io.FileInputStream("E:\\数字图象处理\\作业一\\24.bmp");
				java.io.BufferedInputStream bis= new java.io.BufferedInputStream(fileInputStream);
				
				java.io.FileInputStream fileInputStream1 = new java.io.FileInputStream("E:\\数字图象处理\\作业一\\24test.bmp");
				java.io.BufferedInputStream bis1= new java.io.BufferedInputStream(fileInputStream1);
				
				bis.skip(14);
				bis1.skip(14);
				
				byte[]infoOfPic= new byte[40];
				bis.read(infoOfPic,0,40);
				bis1.skip(40);
				
				// 翻译BMP文件的数据，即将字节数据转化为int数据 ,得到位图数据的宽和高  
				width = ChangeInt(infoOfPic, 7);  
				height = ChangeInt(infoOfPic, 11);
				System.out.println("找不同！");
				
				finddifferent(bis , bis1);
				
				fileInputStream.close();  
				fileInputStream1.close();  
		        bis.close();  
		        bis1.close(); 
		        
		        showUI(); // 创建BMP对象来显示图画 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//通过BMP文件地址创建文件输入流对象
		}

		public int ChangeInt(byte[] infoOfPic, int i) {
			// TODO Auto-generated method stub
			int number = (int)((infoOfPic[i] & 0xff) << 24)
					|((infoOfPic[i-1] & 0xff) << 16)
					|((infoOfPic[i-2] & 0xff) << 8)
					|(infoOfPic[i-3] & 0xff);
			return number;
		}
		
		public void finddifferent(BufferedInputStream bis , BufferedInputStream bis1) {
			// TODO Auto-generated method stub
			red = new int[height][width];  
	        green = new int[height][width];  
	        blue = new int[height][width];  
			
	        //24位bmp存储是按BGR BGR。。。 存的
	        //bmp位图在存储时，图片数据部分的字节数一定是4的倍数，如果不够四的倍数，在每一行要补齐字节。
	        int skipwidth = 4 - width * 3 % 4;
	        
	        try {
				for(int i = height-1;i>=0;i--){
					for (int j = 0; j < width; j++) {
						// 这里遍历的时候，一定要注意本来像素是有RGB来表示，  
				        // 但是在存储的时候由于windows是小段存储，所以在内存中是BGR顺序。  
							blue[i][j] = Math.abs(bis.read()- bis1.read());  
							green[i][j] = Math.abs(bis.read() - bis1.read());  
							red[i][j] = Math.abs(bis.read()- bis1.read());
					}
					if (skipwidth!=0) {  
				        bis.skip(skipwidth); 
				        bis1.skip(skipwidth);
				    }
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		public void showUI() {
			// TODO Auto-generated method stub
			this.setTitle("BMP图像解析");
			this.setSize(width,height);
			this.setDefaultCloseOperation(3);
			this.setResizable(false);
			this.setLocationRelativeTo(null);
			MyPanel panel = new MyPanel();  
	        java.awt.Dimension di = new java.awt.Dimension(width, height);//设置panel大小  
	        panel.setPreferredSize(di);  
	        this.add(panel);//窗体添加panel  
	        this.setVisible(true);//使窗体可见。  
		}
		  public class MyPanel extends javax.swing.JPanel {  
		        private static final long serialVersionUID = 1L;  
		        public void paint(Graphics g) {    
		            for (int i = 0; i < height; i++) {  
		                for (int j = 0; j < width; j++) {  
		                    g.setColor(new Color(red[i][j],green[i][j],blue[i][j])); 
		                    g.fillRect(j, i, 1, 1);  
		                }  
		            }  
		        }  
		    }  
}

