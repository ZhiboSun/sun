package interpolation;

import java.awt.image.BufferedImage;

public class bilinear_interpolation {
	public int bilinear_interpolation(BufferedImage srcimage, int height, int width,
			double x0, double y0, int x, int y) {
			//双线性插值法  
			int left = (int) Math.floor(x0);
		    int right = (int) Math.ceil(x0);
		    int top = (int) Math.ceil(y0);
		    int bottom = (int) Math.floor(y0);
		    if(left<0){
		    	left=0;
		    }
		    if(bottom<0){
		    	bottom=0;
		    }
		    if (right>=width) {
				right=width-1;
			}
		    if (top>=height) {
				top=height-1;
			}
		    int rgb1[]= new int[3];
		    rgb1[0]=(srcimage.getRGB(left, bottom)>>16)&0xff;
		    rgb1[1]=(srcimage.getRGB(left, bottom)>>8)&0xff;
		    rgb1[2]=srcimage.getRGB(left, bottom)&0xff;         
		    int rgb2[]= new int[3];
		    rgb2[0]=(srcimage.getRGB(left, top)>>16)&0xff;
		    rgb2[1]=(srcimage.getRGB(left, top)>>8)&0xff;
		    rgb2[2]=srcimage.getRGB(left, top)&0xff;
		    int rgb3[]= new int[3];
		    rgb3[0]=(srcimage.getRGB(right, bottom)>>16)&0xff;
		    rgb3[1]=(srcimage.getRGB(right, bottom)>>8)&0xff;
		    rgb3[2]=srcimage.getRGB(right, bottom)&0xff;
		    int rgb4[]= new int[3];
		    rgb4[0]=(srcimage.getRGB(right, top)>>16)&0xff;
		    rgb4[1]=(srcimage.getRGB(right, top)>>8)&0xff;
		    rgb4[2]=srcimage.getRGB(right, top)&0xff;
		    
		    double rk1 = rgb2[0]- rgb1[0];
		    double rk2 = rgb4[0]- rgb3[0];
		    
		    double dety = y0 - bottom;
		    
		    double rf3 = rgb1[0]+dety*rk1;
		    double rf4 = rgb3[0]+dety*rk2;
		    
		    double detx = x0 - left;
		    double rk3 = rf4-rf3;
		    
		    int rf5 = (int) (rf3+detx*rk3);
		    ////r
		    double gk1 = rgb2[1]- rgb1[1];
		    double gk2 = rgb4[1]- rgb3[1];                       
		    
		    double gf3 = rgb1[1]+dety*gk1;
		    double gf4 = rgb3[1]+dety*gk2;
         
		    double gk3 = gf4-gf3;
		    
		    int gf5 = (int) (gf3+detx*gk3);
		    ///g
		    double bk1 = rgb2[2]- rgb1[2];
		    double bk2 = rgb4[2]- rgb3[2];
		                          
		    double bf3 = rgb1[2]+dety*bk1;
		    double bf4 = rgb3[2]+dety*bk2;
		    
		    double bk3 = bf4-bf3;
		    
		    int bf5 = (int) (bf3+detx*bk3);
		    //b
		    
		    int newrgb=(rf5<<16)+(gf5<<8)+(bf5);
		    return newrgb;
	}
}
