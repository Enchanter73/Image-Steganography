package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

import image_information.ImageInformation;
import image_information.Pixel;
import image_information.textOperations;

public class GUI extends JFrame{
	
	private ImagePanel imagePanel;
	private JTextArea message;
	private JTextArea key;
	
	ImageInformation img;
	private File file;
	private JFileChooser fc;
	
	private JButton fileChooser;
	private JButton fileChooser2;
	private JButton encryptionButton;
	private JButton decryptionButton;
	
	private double zoomScale = 1;
	private double dragDistanceX = 0;
	private double dragDistanceY = 0;
	private double mouseX;
	private double mouseY;
	
	GUI() {
		
		File dir = new File("C:\\Users\\selim\\Desktop\\Java sources\\Engineering Project");
		FileFilter filter = new ImageFileFilter();
		
		fc = new JFileChooser(dir);
		fc.setFileFilter(filter);
	
		img = new ImageInformation();
		
		imagePanel = new ImagePanel();
		imagePanel.setLayout(null);
		
		message = new JTextArea();
		key = new JTextArea();
		
		fileChooser = new JButton("Choose cover-image");
		fileChooser.setBounds(560, 30, 200, 100);	
		
		fileChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fc.showOpenDialog(null);
				
				file = fc.getSelectedFile();
				
				zoomScale = 1;
				dragDistanceX = 0;
				dragDistanceY = 0;
				
				imagePanel.setImage(img.getImage(file));
				imagePanel.repaint();
			}
		});
		
		fileChooser2 = new JButton("Choose stego-image");
		fileChooser2.setBounds(560, 230, 200, 100);
		
		fileChooser2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fc.showOpenDialog(null);
				
				file = fc.getSelectedFile();
				
				zoomScale = 1;
				dragDistanceX = 0;
				dragDistanceY = 0;
				
				imagePanel.setImage(img.getImage(file));
				imagePanel.repaint();
			}
		});
		
		encryptionButton = new JButton("Hide the message");
		encryptionButton.setBounds(560, 360, 200, 100);	
		
		encryptionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(message.getText().length() >= (img.getPixels().size()/5)) {
					JOptionPane.showMessageDialog(new JFrame(), "Text is too long for the choosen image");
					System.exit(0);
				}
				else {
					textOperations t = new textOperations(message.getText(), img.getPixels(), img.getChangedPixels());
					img.createImage(t.transformMessage());			
				}		
				imagePanel.repaint();
			}
		});
		
		decryptionButton = new JButton("Get the message");
		decryptionButton.setBounds(560, 690, 200, 100);
		
		decryptionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String s = key.getText();
				String m = "";
				String letter = "";
				String temp = "";
				String[] nums = s.split("[,]");
				ArrayList<Pixel> pixels = img.getPixels();
				
				for(int j=0; j<nums.length; j+=2) {
					int pixelNum = Integer.parseInt(nums[j])/3;
					int reminder = Integer.parseInt(nums[j])%3;
					int length = Integer.parseInt(nums[j+1]);
					
					for(int i=0; i<length; i++) {
						pixelNum = Integer.parseInt(nums[j])/3;
						reminder = Integer.parseInt(nums[j])%3;		
						switch(reminder) {
						case 0:
							temp = Integer.toBinaryString(pixels.get(pixelNum).r);
							letter += temp.substring(temp.length()-1, temp.length());
							break;
						case 1:
							temp = Integer.toBinaryString(pixels.get(pixelNum).g);
							letter += temp.substring(temp.length()-1, temp.length());
							break;
						case 2:
							temp = Integer.toBinaryString(pixels.get(pixelNum).b);
							letter += temp.substring(temp.length()-1, temp.length());
							break;
						}
						int t = Integer.parseInt(nums[j]);
						t++;
						nums[j] = Integer.toString(t);
					}
					char x = (char)Integer.parseInt(letter, 2);
					m += x;
					letter = "";			
				}
				message.append(m);				
				
				imagePanel.repaint();
			}
		});
		
		this.setLayout(null);
		
		message.setBounds(30, 360, 500, 300);
		key.setBounds(30, 690, 500, 100);
		key.setText("Enter the key here after choosing stego-image");

		this.add(message);
		this.add(key);
			
		imagePanel.setBackground(new Color(200,200,200));
		imagePanel.setBounds(30, 30, 500, 300);

		this.add(imagePanel);
		
		this.add(fileChooser);
		this.add(fileChooser2);
		this.add(encryptionButton);
		this.add(decryptionButton);
		
		this.setVisible(true);
		this.setSize(800, 900);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		imagePanel.addMouseWheelListener(new MouseHandler());
		imagePanel.addMouseMotionListener(new MouseHandler());
		imagePanel.addMouseListener(new MouseHandler());
	}
	
	class ImageFileFilter extends FileFilter {
		private final String[] okFileExtensions = new String[] {"jpg", "png"};	
		@Override
		public boolean accept(File file) {
			for (String extension : okFileExtensions) {
				if (file.getName().toLowerCase().endsWith(extension)) {
					return true;
				}
			}
		return false;
		}
		@Override
		public String getDescription() {			
			return null;
		}
	}
	
	class ImagePanel extends JPanel {
		private BufferedImage image;
	    
	    public void setImage(BufferedImage image) {
	    	this.image = image;
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        
	        Graphics2D  g2 = (Graphics2D)g;
			AffineTransform at = new AffineTransform();
			at.scale(zoomScale, zoomScale);
			at.translate(dragDistanceX, dragDistanceY);
			g2.transform(at);
			
	        g.drawImage(image, 0, 0, this);           
	    }
	}
	
	class MouseHandler implements MouseWheelListener, MouseMotionListener, MouseListener {
		@Override
		public void mouseWheelMoved(MouseWheelEvent m) {			
			if(m.getWheelRotation() < 0) {
				zoomScale = zoomScale*(1.1);
				imagePanel.repaint();
			}
			if(m.getWheelRotation() > 0) {
				zoomScale = zoomScale/(1.1);
				imagePanel.repaint();
			}		
		}
		@Override
		public void mouseDragged(MouseEvent m) {
			dragDistanceX = (m.getX() - mouseX) + dragDistanceX;
			dragDistanceY = (m.getY() - mouseY) + dragDistanceY;
			mouseX = m.getX();
			mouseY = m.getY();
			imagePanel.repaint();
		}
		@Override
		public void mouseMoved(MouseEvent m) {}
		public void mouseClicked(MouseEvent m) {}
		public void mouseEntered(MouseEvent m) {}
		public void mouseExited(MouseEvent m) {}
		public void mousePressed(MouseEvent m) {
			mouseX = m.getX();
			mouseY = m.getY();
		}
		@Override
		public void mouseReleased(MouseEvent m) {}	
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.drawRect(38, 67, 501, 301);
		g.drawRect(38, 397, 501, 301);
		g.drawRect(38, 727, 501, 101);
    }

	public static void main(String[] args) {	
		GUI g = new GUI();
		g.repaint();
	}
}
