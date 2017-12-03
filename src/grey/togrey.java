package grey;

import java.awt.image.BufferedImage;

public class togrey {
	public int[] toGrey(BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();
		int[] rgb = new int[3];
		int gray = 0;
		int greyarray[] = new int[w*h];
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				rgb = getPixel(image, w, h, i, j);
				gray = (int)(0.3f*rgb[0] + 0.59f*rgb[1] + 0.11f*rgb[2]);
				greyarray[j+i*h] = gray;
			}
		}
		return greyarray;
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
