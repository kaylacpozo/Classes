import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import java.util.Random;

public class HW03 {
	private static int WIDTH = 400;
	private static int HEIGHT = 400;
	
	public static void main(String[] args) {
		JFrame frame = new Image(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

class Image extends JFrame {
	private final JFileChooser chooser;
	private BufferedImage image;
	
	public Image(int width, int height) {
		this.setTitle("CAP 3027 2015 - HW03 - Kayla Pozo");
		this.setSize(width, height);
		
		addMenu();
		
		//setup chooser dialog
		
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
	}
	
	private void addMenu() {
		JMenu fileMenu = new JMenu("File");
		
		//Toroid
		JMenuItem toroidCrystal = new JMenuItem("Crystal (toroid)");
		toroidCrystal.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				toroid();
			}
		});
		
		fileMenu.add(toroidCrystal);
		
		//Bounded
		JMenuItem boundedCrystal = new JMenuItem("Crystal (bounded plane)");
		boundedCrystal.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				bounded();
			}
		});
		
		fileMenu.add(boundedCrystal);
		
		//Exit
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		
		fileMenu.add(exitItem);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);
	}
	
	private void toroid() {
		String imageSize = JOptionPane.showInputDialog("Size of the image?");
		int size = Integer.parseInt(imageSize);
		
		String numberSeeds = JOptionPane.showInputDialog("Number of seeds?");
		int seeds = Integer.parseInt(numberSeeds);
		
		String numberParticles = JOptionPane.showInputDialog("Number of particles?");
		int particles = Integer.parseInt(numberParticles);
		
		String maxSteps = JOptionPane.showInputDialog("Number of maximum steps?");
		int steps = Integer.parseInt(maxSteps);
		
		createToroidImage(size, seeds, particles, steps);
		displayBufferedImage(image);
	}
	
	private void bounded() {
		String imageSize = JOptionPane.showInputDialog("Size of the image?");
		int size = Integer.parseInt(imageSize);
		
		String numberSeeds = JOptionPane.showInputDialog("Number of seeds?");
		int seeds = Integer.parseInt(numberSeeds);
		
		String numberParticles = JOptionPane.showInputDialog("Number of particles?");
		int particles = Integer.parseInt(numberParticles);
		
		String maxSteps = JOptionPane.showInputDialog("Number of maximum steps?");
		int steps = Integer.parseInt(maxSteps);
		
		createBoundedImage(size, seeds, particles,steps);
		displayBufferedImage(image);
	}
	
	private void createToroidImage(int size, int seeds, int particles, int steps){
		image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
		for(int x=0; x<size;x++) { 
			for(int y=0;y<size; y++) {
				image.setRGB(x, y, 0xFFFFFFFF);
			}
		}
		
		for(int i=0; i<seeds;i++) { 
			Random randX = new Random();
			int x = randX.nextInt(size);
			
			Random randY = new Random();
			int y = randY.nextInt(size);
			
			image.setRGB(x, y, 0xFFFF0000);
		}
		
		for(int i=0; i<particles;i++) { 
			Random randX = new Random();
			int x = randX.nextInt(size);
			
			Random randY = new Random();
			int y = randY.nextInt(size);
			
			outerloop:
			for(int s=0; s<steps; s++) {	
				Random randXvalue = new Random();
				int xStep = randXvalue.nextInt(3)-1;
						
				Random randYvalue = new Random();
				int yStep = randYvalue.nextInt(3)-1;
						
				x=x+xStep;
				y=y+yStep;
				
				if(x<0){
					x=size-1;
				}
			
				if(x>size-1){
					x=0;
				}
			
				if(y>size-1){
					y=0;
				}
				
				if(y<0){
					y=size-1;
				}
				
				//Checking perimeter
				for(int j=-1; j<2; j++) {
					for(int k=-1; k<2; k++) {
						//Check x perimeter within bounds
						int xPerimeter=x+j;
						if(xPerimeter<0){
							xPerimeter=size+xPerimeter;
						}
					
						if(xPerimeter>size-1){
							xPerimeter=xPerimeter-size;
						}
					
						//Check y perimeter within bounds
						int yPerimeter=y+k;
						if(yPerimeter>size-1){
							yPerimeter=yPerimeter-size;
						}
						
						if(yPerimeter<0){
							yPerimeter=size+yPerimeter;
						}
						
						if(image.getRGB(xPerimeter, yPerimeter)!=0xFFFFFFFF) {
							image.setRGB(x, y, 0xFF000000);
							break outerloop;
						}
					}
				}
			}
		}
	}
	
	private void createBoundedImage(int size, int seeds, int particles, int steps){
		image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
		for(int x=0; x<size;x++) { 
			for(int y=0;y<size; y++) {
				image.setRGB(x, y, 0xFFFFFFFF);
			}
		}
		
		for(int i=0; i<seeds;i++) { 
			Random randX = new Random();
			int x = randX.nextInt(size);
			
			Random randY = new Random();
			int y = randY.nextInt(size);
			
			image.setRGB(x, y, 0xFFFF0000);
		}
		
		for(int i=0; i<particles;i++) { 
			Random randX = new Random();
			int x = randX.nextInt(size);
			
			Random randY = new Random();
			int y = randY.nextInt(size);
			
			outerloop:
			for(int s=0; s<steps; s++) {	
				Random randXvalue = new Random();
				int xStep = randXvalue.nextInt(3)-1;
						
				Random randYvalue = new Random();
				int yStep = randYvalue.nextInt(3)-1;
						
				x=x+xStep;
				y=y+yStep;
				
				//If pixel gets to the bound, keep the steps within 
				if(x<0){
					x=0;
				}
				
				if(x>size-1){
					x=size-1;
				}
				
				if(y>size-1){
					y=size-1;
				}
				
				if(y<0){
					y=0;
				}
				
				//Checking perimeter
				for(int j=-1; j<2; j++) {
					for(int k=-1; k<2; k++) {
						//Check x perimeter within bounds
						int xPerimeter=x+j;
						if(xPerimeter<0){
							xPerimeter=size+xPerimeter;
						}
					
						if(xPerimeter>size-1){
							xPerimeter=xPerimeter-size;
						}
					
						//Check y perimeter within bounds
						int yPerimeter=y+k;
						if(yPerimeter>size-1){
							yPerimeter=yPerimeter-size;
						}
						
						if(yPerimeter<0){
							yPerimeter=size+yPerimeter;
						}
						
						if(image.getRGB(xPerimeter, yPerimeter)!=0xFFFFFFFF) {
							image.setRGB(x, y, 0xFF000000);
							break outerloop;
						}
					}
				}
			}
		}
	}
	
	public void displayBufferedImage(BufferedImage image) {
		this.setContentPane(new JScrollPane(new JLabel(new ImageIcon(image))));
		
		this.validate();
	}
}