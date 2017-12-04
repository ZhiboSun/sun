package histogram;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

public class HistogramComparisonFilter {
	static String[] allpath = new String[1000];
	public static void main(String arg[]){
		String src = "E:\\数字图象处理\\图片集\\test.jpg";
		double[] srcbins = new double[16*16*16];
		double[] destbins = new double[16*16*16];
		HashMap<String,Double> similarity_coefficient= new HashMap<String,Double>();

		srcbins = statistics(src);
		getFileName();
		for (int i = 0; i < allpath.length; i++) {
			if(allpath[i]==null)
				break;
			destbins = statistics(allpath[i]);
			similarity_coefficient.put(getFileNameWithSuffix(allpath[i]), 
					calculateBhattacharyya(srcbins,destbins));
		}
		
		//hashmap排序
		List<Map.Entry<String, Double>> list = 
				new ArrayList<Map.Entry<String, Double>>(similarity_coefficient.entrySet());  
	    Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			@Override
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				//return o1.getValue().compareTo(o2.getValue());  
		        return o2.getValue().compareTo(o1.getValue());  
	
			}    
	    });  
        for (Entry<String, Double> mapping : list) {  
            System.out.println(mapping.getKey() + " 相似系数: " + mapping.getValue());  
        }  
	}
	
	//保留文件名及后缀 
    public static String getFileNameWithSuffix(String pathandname) {         
        int start = pathandname.lastIndexOf("\\");  
        if (start != -1 ) {  
            return pathandname.substring(start + 1);  
        } else {  
            return null;  
        }         
    }     
    
	public static String[] getFileName() {
	     String path = "E:\\数字图象处理\\图片集\\测试集"; // 路径
	     
	     File f = new File(path);
	     if (!f.exists()) {
	         System.out.println(path + " not exists");
	         }
	 
	         File fa[] = f.listFiles();
	         for (int i = 0; i < fa.length; i++) {
	             File fs = fa[i]; 	
	             allpath[i] = fs.getAbsolutePath();
	     }
		 return allpath;
	}
	
	private static double calculateBhattacharyya(double[] srcBins, double[] destBins) {
		double[] mixedData = new double[srcBins.length];
		for(int i=0; i<srcBins.length; i++ ) {
			mixedData[i] = Math.sqrt(srcBins[i] * destBins[i]);
		}
		
		// The values of Bhattacharyya Coefficient 
		// ranges from 0 to 1,
		double similarity = 0;
		for(int i=0; i<mixedData.length; i++ ) {
			similarity += mixedData[i];
		}
		
		// The degree of similarity
		return similarity;
	}
	private static double[] statistics(String src) {	
		double[] bins = new double[16*16*16];
		try {
			
			for (int i = 0; i < bins.length; i++) {
				bins[i] = 0;
			}
			File srcFile = new File(src);
			BufferedImage srciImage = ImageIO.read(srcFile);
			int height = srciImage.getHeight();
			int width = srciImage.getWidth();
			int[] rgb = new int[3];
			int level = 16;
			int binIndex = 0;
			
            for(int i=0;i<width;i++){
            	for (int j = 0; j < height; j++) {
            		rgb = getPixel(srciImage, width, height, i, j);
            		binIndex = rgb[0]/level + rgb[1]/level*(256/level) + rgb[2]/level*(256/level)*(256/level);
            		bins[binIndex]++;
				}    	 
            }//统计灰度数量
            
            //归一化直方图数据
            for (int i = 0; i < bins.length; i++) {
				bins[i] = bins[i]/(width*height);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bins;	
	}
	

	private static int[] getPixel(BufferedImage srcImage, int width, int height, int x0, int y0) {
        if (x0 >= width) {
        	x0 = width - 1;
        }
        if (x0 < 0) {
        	x0 = 0;
        }
        if (y0 < 0) {
        	y0 = 0;
        }
        if (y0 >= height) {
        	y0 = height - 1;
        }
        int srcrgb = srcImage.getRGB(x0, y0);
        int[] rgb = new int[3];
        rgb[0] = (srcrgb >> 16) & 0xff;
        rgb[1] = (srcrgb >> 8) & 0xff;
        rgb[2] = srcrgb & 0xff;
        return rgb;
    }
}
