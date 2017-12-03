package interpolation;

import java.awt.image.BufferedImage;

public class cubic_spline_interpolation {
	//有四种插值模型可选type
	public final static int TRIANGLE__INTERPOLATION = 1;
    public final static int BELL__INTERPOLATION = 2;
    public final static int BSPLINE__INTERPOLATION = 4;
    public final static int CATMULLROOM__INTERPOLATION = 8;
    public final static double B = 0.0;
    public final static double C = 0.5; // constant
    private int type = 4;
    
    private double bellInterpolation( double x )
    {
        double f = ( x / 2.0 ) * 1.5;
        if( f > -1.5 && f < -0.5 )
        {
            return( 0.5 * Math.pow(f + 1.5, 2.0));
        }
        else if( f > -0.5 && f < 0.5 )
        {
            return 3.0 / 4.0 - ( f * f );
        }
        else if( ( f > 0.5 && f < 1.5 ) )
        {
            return( 0.5 * Math.pow(f - 1.5, 2.0));
        }
        return 0.0;
    }
     
    private double bspLineInterpolation( double f )
    {
        if( f < 0.0 )
        {
            f = -f;
        }
 
        if( f >= 0.0 && f <= 1.0 )
        {
            return ( 2.0 / 3.0 ) + ( 0.5 ) * ( f* f * f ) - (f*f);
        }
        else if( f > 1.0 && f <= 2.0 )
        {
            return 1.0 / 6.0 * Math.pow( ( 2.0 - f  ), 3.0 );
        }
        return 1.0;
    }
     
    private double triangleInterpolation( double f )
    {
        f = f / 2.0;
        if( f < 0.0 )
        {
            return ( f + 1.0 );
        }
        else
        {
            return ( 1.0 - f );
        }
    }
     
    private double CatMullRomInterpolation( double f )
    {
        if( f < 0.0 )
        {
            f = Math.abs(f);
        }
        if( f < 1.0 )
        {
            return ( ( 12 - 9 * B - 6 * C ) * ( f * f * f ) +
                ( -18 + 12 * B + 6 *C ) * ( f * f ) +
                ( 6 - 2 * B ) ) / 6.0;
        }
        else if( f >= 1.0 && f < 2.0 )
        {
            return ( ( -B - 6 * C ) * ( f * f * f )
                + ( 6 * B + 30 * C ) * ( f *f ) +
                ( - ( 12 * B ) - 48 * C  ) * f +
                8 * B + 24 * C)/ 6.0;
        }
        else
        {
            return 0.0;
        }
    } 
    
    
    public int cubic_spline_interpolation(BufferedImage srcimage, int height, int width,
			double x0, double y0, int x, int y){
    	double rgbCoffeData = 0.0;
    	double[] rgbData = new double[3];
    	for(int m=-1; m<3; m++)
        {
            for(int n=-1; n<3; n++)
            {
                int[] rgb = getPixel(srcimage, width, height, (int)Math.floor(x0+m), (int)Math.floor(y0+n));
                double f1 = 0.0d;
                double f2 = 0.0d;
                double detx  = x0 - Math.floor(x0);
                double dety  = y0 - Math.floor(y0);
                if(type == TRIANGLE__INTERPOLATION)
                {
                    f1  = triangleInterpolation( ((double) m ) - detx );
                    f2 = triangleInterpolation ( -(( (double) n ) - dety ) );  
                }
                else if(type == BELL__INTERPOLATION)
                {
                    f1  = bellInterpolation( ((double) m ) - detx );
                    f2 = bellInterpolation ( -(( (double) n ) - dety ) );  
                }
                else if(type == BSPLINE__INTERPOLATION)
                {
                    f1  = bspLineInterpolation( ((double) m ) - detx );
                    f2 = bspLineInterpolation ( -(( (double) n ) - dety ) );   
                }
                else
                {
                    f1  = CatMullRomInterpolation( ((double) m ) - detx );
                    f2 = CatMullRomInterpolation ( -(( (double) n ) - dety ) );                            
                }
                // sum of weight
                rgbCoffeData += f2*f1;
                // sum of the RGB values
                rgbData[0] += rgb[0] * f2 * f1;
                rgbData[1] += rgb[1] * f2 * f1;
                rgbData[2] += rgb[2] * f2 * f1;
            }
        }
    	int ta = 255;
        // get Red/green/blue value for sample pixel
        int tr = (int) (rgbData[0]/rgbCoffeData);
        int tg = (int) (rgbData[1]/rgbCoffeData);
        int tb = (int) (rgbData[2]/rgbCoffeData);
        int newrgb = (ta << 24) | tr << 16
                | (tg << 8 )| tb;
        return newrgb;
    }
    private int[] getPixel(BufferedImage srcImage, int width, int height,
            int x0, int y0) {
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
        //System.out.println(x0+"   "+y0+"   "+width+"   "+height+"   ");
        int srcrgb = srcImage.getRGB(x0, y0);
        int[] rgb = new int[3];
        rgb[0] = (srcrgb >> 16) & 0xff;
        rgb[1] = (srcrgb >> 8) & 0xff;
        rgb[2] = srcrgb & 0xff;
        return rgb;
    }
    
}
