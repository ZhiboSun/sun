package readBmpPic;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedInputStream;
import java.io.IOException;

import readBmpPic.readBmp.MyPanel;

public class findthedifferent  extends javax.swing.JFrame {
		public int width;//bmpͼƬ�Ŀ��
		public int height;//bmpͼƬ�ĸ߶�
		public int[][]red;
		public int[][]green;
		public int[][]blue;//BMP��������,��һ�����ص�RGB�������������� 
		
		public static void main(String arg[]){
			findthedifferent find_different = new findthedifferent();
			find_different.init();
		}

		public void init() {
			// TODO Auto-generated method stub
			try {
				java.io.FileInputStream fileInputStream = new java.io.FileInputStream("E:\\����ͼ����\\��ҵһ\\24.bmp");
				java.io.BufferedInputStream bis= new java.io.BufferedInputStream(fileInputStream);
				
				java.io.FileInputStream fileInputStream1 = new java.io.FileInputStream("E:\\����ͼ����\\��ҵһ\\24test.bmp");
				java.io.BufferedInputStream bis1= new java.io.BufferedInputStream(fileInputStream1);
				
				bis.skip(14);
				bis1.skip(14);
				
				byte[]infoOfPic= new byte[40];
				bis.read(infoOfPic,0,40);
				bis1.skip(40);
				
				// ����BMP�ļ������ݣ������ֽ�����ת��Ϊint���� ,�õ�λͼ���ݵĿ�͸�  
				width = ChangeInt(infoOfPic, 7);  
				height = ChangeInt(infoOfPic, 11);
				System.out.println("�Ҳ�ͬ��");
				
				finddifferent(bis , bis1);
				
				fileInputStream.close();  
				fileInputStream1.close();  
		        bis.close();  
		        bis1.close(); 
		        
		        showUI(); // ����BMP��������ʾͼ�� 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//ͨ��BMP�ļ���ַ�����ļ�����������
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
			
	        //24λbmp�洢�ǰ�BGR BGR������ ���
	        //bmpλͼ�ڴ洢ʱ��ͼƬ���ݲ��ֵ��ֽ���һ����4�ı�������������ĵı�������ÿһ��Ҫ�����ֽڡ�
	        int skipwidth = 4 - width * 3 % 4;
	        
	        try {
				for(int i = height-1;i>=0;i--){
					for (int j = 0; j < width; j++) {
						// ���������ʱ��һ��Ҫע�Ȿ����������RGB����ʾ��  
				        // �����ڴ洢��ʱ������windows��С�δ洢���������ڴ�����BGR˳��  
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
			this.setTitle("BMPͼ�����");
			this.setSize(width,height);
			this.setDefaultCloseOperation(3);
			this.setResizable(false);
			this.setLocationRelativeTo(null);
			MyPanel panel = new MyPanel();  
	        java.awt.Dimension di = new java.awt.Dimension(width, height);//����panel��С  
	        panel.setPreferredSize(di);  
	        this.add(panel);//�������panel  
	        this.setVisible(true);//ʹ����ɼ���  
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

