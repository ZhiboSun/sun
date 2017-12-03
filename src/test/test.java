package test;

import java.awt.Button;  
import java.awt.FlowLayout;  
import java.awt.Frame;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.awt.event.WindowAdapter;  
import java.awt.event.WindowEvent;
import readBmpPic.readBmp;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;  
  
public class test {  
  
    private Frame f;  
    private Button but;  
      
    public test() {  
        init();  
    }  
  
    private void init(){  
        f=new Frame("数字图象处理");  
        f.setBounds(300, 200, 600, 500);  
        f.setLayout(new FlowLayout());  
          
        but=new Button("打开图片");   
        f.add(but);  
          
        event();  
          
        f.setVisible(true);  
    }  
  
    private void event(){  
        f.addWindowListener(new WindowAdapter() {  
            @Override  
            public void windowClosing(WindowEvent e) {  
                System.exit(0);  
            }  
          
        });  
 
        but.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	fileChooser();
            }
        });       
    }  
	public void fileChooser() {
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "bmp", "jpg", "gif","bmp");
	    chooser.setFileFilter(filter);
	    //打开选择器面板
	    int returnVal = chooser.showOpenDialog(new JPanel());  
                      //保存文件从这里入手，输出的是文件名
	    if(returnVal == JFileChooser.APPROVE_OPTION) {	     
	            String srcString = chooser.getSelectedFile().getAbsolutePath();
	            readBmp readBmp = new readBmp();
	            readBmp.init(srcString);
	    }
	}
    public static void main(String[] args) {  
        // TODO Auto-generated method stub  
        new test();  
    }  
  
}  
