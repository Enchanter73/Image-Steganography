package image_information;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageInformation {
	
	ArrayList<Pixel> pixels = new ArrayList<Pixel>();
	ArrayList<Pixel> changedPixels = new ArrayList<Pixel>();
	
	ImageIcon image;
	BufferedImage img;
	BufferedImage newImg;
	
	int HEIGHT = 0;
	int WIDTH = 0;

	void readImage(File f) {
		try {		
			//Reading the image
		    img = ImageIO.read(f);
		    
		    HEIGHT = img.getHeight();
		    WIDTH = img.getWidth();
		    
		    System.out.println(WIDTH + " " + HEIGHT);
		    
		    for (int y = 0; y < HEIGHT; y++) {
		    	for (int x = 0; x < WIDTH; x++) {
		    		//Retrieving contents of a pixel
		            int p = img.getRGB(x,y);

		            //Retrieving the A R G B values
		            int a = (p>>24)&0xff;
		            int r = (p>>16)&0xff;
		            int g = (p>>8)&0xff;
		            int b = p&0xff;
		            
		            pixels.add(new Pixel(r, g, b));
		            changedPixels.add(new Pixel(0,0,0));
		            //System.out.println(r + " " + g + " " + b);
		    	}
		    }
		    System.out.println(pixels.size());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createImage(ArrayList<Pixel> newImage) {
		newImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		File f = new File("stego-image.jpg");
		
        int[] myPixels = new int[newImage.size()*3];
        int counter = 0;
	    for (int i=0; i<newImage.size()*3; i+=3) {
	    	myPixels[i] = newImage.get(counter).r;
	    	myPixels[i+1] = newImage.get(counter).g;
	    	myPixels[i+2] = newImage.get(counter).b;
	    	counter++;
		}
	    
	    WritableRaster raster = newImg.getRaster();
        raster.setPixels(0, 0, WIDTH, HEIGHT, myPixels);     
        
	    try {
			ImageIO.write(newImg, "png", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getImage(File f) {
		readImage(f);
		return img;
	}
	
	public ArrayList<Pixel> getPixels() {
		return pixels;
	}
	
	public ArrayList<Pixel> getChangedPixels() {
		return changedPixels;
	}
}
