import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.*;

public class HW02 {
	private static final int WIDTH = 512;
	private static final int HEIGHT = 512;
	
	public static void main(String[] args) {
		JFrame frame = new Frame(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

class Frame extends JFrame {
	private final JFileChooser chooser;
	private BufferedImage image;
	
	//constructor
	
	public Frame(int width, int height) {
		// setup the frame's attributes
		this.setTitle("CAP 3027 2015 - HW02 - Kayla Pozo");
		this.setSize(width,height);
		
		addMenu();
		
		//setup the chooser dialog
		
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
	}
	
	private void addMenu() {
		//setup frame's menu bar
		
		//=== File menu
		
		JMenu fileMenu = new JMenu("File");
		
		//Open
		
		JMenuItem bilinearImage = new JMenuItem("Bilinear gradient");
		bilinearImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String result = JOptionPane.showInputDialog("Size of the image?");
				int size = Integer.parseInt(result);
				
				createBufferedImage(size);
			}
		});
		
		fileMenu.add(bilinearImage);
		
		// Exit
		
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		
		fileMenu.add(exitItem);
		
		//attach menu to a menu bar
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);
	}
	
	private void createBufferedImage(int size) {
		int blueConst = 0xFF0000FF;
		int black = 0xFF000000;
		int greenConst = 0xFF00FF00;
		int redConst = 0xFFFF0000;
		
		float red1, green1, blue1, red2, green2, blue2;
		int red, green, blue;
		long intARGB1, intARGB2;
		int argb;
	
		
		image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
		//NW corner is blue
		image.setRGB(0, 0, blueConst);
		//NE corner is black
		image.setRGB((size-1), 0, black);
		//SW corner is green
		image.setRGB(0, (size-1), greenConst);
		//SE corner is red
		image.setRGB(size-1, size-1, redConst);

		
		//Pixels top to bottom from left edge
		for(int i=0; i<size-2; i++) {
			intARGB2 = image.getRGB(0, (size-1));
			red2 = (intARGB2>>>16) & 0xFF;
			green2 = (intARGB2>>>8) & 0xFF;
			blue2 = intARGB2 & 0xFF;
			
			intARGB1 = image.getRGB(0, i);
			red1 = (int)(intARGB1>>>16) & 0xFF;
			green1 = (int)(intARGB1>>>8) & 0xFF;
			blue1 = (int)intARGB1 & 0xFF;
			
			//interpolation
			red2 = (red2-red1)/(size-1-i) +red1;
			green2 = (green2-green1)/(size-1-i) + green1;
			blue2 = (blue2-blue1)/(size-1-i) + blue1;
			
			red=Math.round(red2);
			green=Math.round(green2);	
			blue=Math.round(blue2);	
			
			
			argb = 0xFF000000 |(red<<16) | (green<<8) | blue;
			image.setRGB(0, i+1,argb);		
		}
	
		//Pixels top to bottom from right edge
		for(int i=0; i<size-2; i++) {
			intARGB2 = image.getRGB((size-1), (size-1));
			red2 = (int)(intARGB2>>>16) & 0xFF;
			green2 = (int)(intARGB2>>>8) & 0xFF;
			blue2 = (int)intARGB2 & 0xFF;
			
			intARGB1 = image.getRGB((size-1), i);
			red1 = (int)(intARGB1>>>16) & 0xFF;
			green1 = (int)(intARGB1>>>8) & 0xFF;
			blue1 = (int)intARGB1 & 0xFF;
			
			//interpolation
			red2 = (red2-red1)/(size-1-i) + red1;
			green2 = (green2-green1)/(size-1-i) + green1;
			blue2 = (blue2-blue1)/(size-1-i) + blue1;
			
			red=Math.round(red2);
			green=Math.round(green2);	
			blue=Math.round(blue2);	
			
			argb = 0xFF000000 | (red<<16) | (green<<8) | blue;
			image.setRGB((size-1), i+1, argb);
		}
		
		//Pixels left to right
		for(int i=0; i<(size-1); i++) {
			for(int j=0; j<(size-1); j++) {
				//extraction
				intARGB2 = image.getRGB(size-1, i); 
				red2 = (int)(intARGB2>>>16) & 0xFF;
				green2 = (int)(intARGB2>>>8) & 0xFF;
				blue2 = (int)intARGB2 & 0xFF;
				
				intARGB1 = image.getRGB(j, i);
				red1 = (int)(intARGB1>>>16) & 0xFF;
				green1 = (int)(intARGB1>>>8) & 0xFF;
				blue1 = (int)intARGB1 & 0xFF;
				
				//interpolation
				red2 = (red2-red1)/(size-1-j) + red1;
				green2 = (green2-green1)/(size-1-j) + green1;
				blue2 = (blue2-blue1)/(size-1-j) + blue1;
				
				red=Math.round(red2);
				green=Math.round(green2);	
				blue=Math.round(blue2);	
				
				argb = 0xFF000000 | (red<<16) | (green<<8) | blue;
				image.setRGB((j+1), i, argb);
			}
			
		}
		
		
		displayBufferedImage(image);
	}
	
	public void displayBufferedImage(BufferedImage image){
		this.setContentPane(new JScrollPane(new JLabel(new ImageIcon(image))));
		
		this.validate();
	}
}
