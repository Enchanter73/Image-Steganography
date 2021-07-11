package image_information;

import java.util.ArrayList;

public class textOperations {
	String s;
	ArrayList<Pixel> pixels;
	ArrayList<Pixel> changedPixels;
	ArrayList<String> message;
	ArrayList<Integer> messageLength;
	
	String key = "";
	
	public textOperations(String message, ArrayList<Pixel> p, ArrayList<Pixel> cp) {
		this.s = message;
		this.pixels = p;
		this.changedPixels = cp;
	}
	
	public ArrayList<Pixel> transformMessage() {
		message = new ArrayList<String>();
		messageLength = new ArrayList<Integer>();
		char[] c = s.toCharArray();
		
		for(char ch: c) {
			message.add(Integer.toBinaryString(ch));
			messageLength.add(Integer.toBinaryString(ch).length());
		}
		hideMessage();
		return pixels;
	}
	
	public void hideMessage() {
		for(int i=0; i<message.size(); i++) {
			int r = (int)(Math.random()*pixels.size()*3-1);
			r = (int)Math.floor(r);
			
			if(checkPixel(r, i) && (r + messageLength.get(i) < pixels.size())) {
				key += r + "," + messageLength.get(i) + ",";
				for(int j=0; j<messageLength.get(i); j++) {
					int pixelNum = r/3;
					int reminder = r%3;
					switch(reminder) {
						case 0:
							int newR = changeBit(pixels.get(pixelNum).r, message.get(i).substring(j, j+1));
							//System.out.println("r:"+ pixels.get(pixelNum).r);
							pixels.get(pixelNum).r = newR;
							//System.out.println("r:"+ pixels.get(pixelNum).r);
							break;
						case 1:
							int newG = changeBit(pixels.get(pixelNum).g, message.get(i).substring(j, j+1));
							//System.out.println("g:"+ pixels.get(pixelNum).g);
							pixels.get(pixelNum).g = newG;
							//System.out.println("g:"+ pixels.get(pixelNum).g);
							break;
						case 2:
							int newB = changeBit(pixels.get(pixelNum).b, message.get(i).substring(j, j+1));
							//System.out.println("b:"+ pixels.get(pixelNum).b);
							pixels.get(pixelNum).b = newB;
							//System.out.println("b:"+ pixels.get(pixelNum).b);
							break;
					}
					r++;
				}
				
			}
			else {
				//System.out.println("WRONG INDEX");
				i--;
			}
			
		}	
		System.out.println(key);
	}
	
	boolean checkPixel(int r, int i) {
		int size = messageLength.get(i);
		int temp = r;
		for(int j=0; j<size; j++) {
			int pixelNum = temp/3;
			int reminder = temp%3;
			switch(reminder) {
				case 0:
					if(changedPixels.get(pixelNum).r == 1) {
						return false;
					}
					break;
				case 1:
					if(changedPixels.get(pixelNum).g == 1) {
						return false;
					}
					break;
				case 2:
					if(changedPixels.get(pixelNum).b == 1) {
						return false;
					}
					break;
			}
			temp++;
		}
		for(int j=0; j<size; j++) {
			int pixelNum = r/3;
			int reminder = r%3;
			switch(reminder) {
				case 0:
					changedPixels.get(pixelNum).r = 1;
					break;
				case 1:
					changedPixels.get(pixelNum).g = 1;
					break;
				case 2:
					changedPixels.get(pixelNum).b = 1;
					break;
			}
			r++;
		}
		return true;
	}
	
	int changeBit(int colorValue, String messageValue) {
		String newValue;
		newValue = Integer.toBinaryString(colorValue);
		
		String substring = newValue.substring(0, newValue.length()-1);
		newValue = substring + messageValue;

		
		return Integer.parseInt(newValue, 2);
	}
}
