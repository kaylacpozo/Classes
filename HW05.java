import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.image.*;
import java.io.*;
import java.util.Random;
import javax.swing.*;

public class HW05 {
	private static int WIDTH = 400;
	private static int HEIGHT = 400;

	public static void main(String[] args){
		JFrame frame = new Frame(WIDTH,HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

class Frame extends JFrame{
	private final JFileChooser chooser;
	private BufferedImage image;
	String result;
	
	//Constructor
	public Frame(int width, int height){
		//Sets up frame's attributes
		this.setTitle("CAP 3027 2015 - HW05 - Kayla Pozo");
		this.setSize(width, height);
		
		//Adds menu to the frame
		addMenu();
		
		//Setup chooser dialog
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
	}
	
	private void addMenu() {
		//Setup the frame's menu bar
		JMenu fileMenu = new JMenu("File");
		
		//Directed random walk plant?
		JMenuItem randomWalk = new JMenuItem("Directed random walk plant");
		randomWalk.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				//prompt user for image's size
				String result = JOptionPane.showInputDialog("Size of the image?");
				int size = Integer.parseInt(result);
				
				//prompt user for number of stems
				String stems1 = JOptionPane.showInputDialog("Number of stems?");
				int stems = Integer.parseInt(stems1);
				System.out.println(stems);
				
				//prompt user for number of steps
				String steps1 = JOptionPane.showInputDialog("Number of steps?");
				int steps = Integer.parseInt(steps1);
				System.out.println(steps);
				
				//prompt user for the transmission probability[0.0,1.0]
				String alpha1 = JOptionPane.showInputDialog("Transmission probability [0.0,1.0]?");
				double alpha = Double.parseDouble(alpha1);
				System.out.println(alpha);
				
				//prompt user for the maximum rotation increment[0.0,1.0]
				String theta1 = JOptionPane.showInputDialog("Maximum rotation increment [0.0,1.0]?");
				double deltaTheta = Double.parseDouble(theta1);
				System.out.println(deltaTheta);
				
				//prompt user for the growth segment increment
				String rho1 = JOptionPane.showInputDialog("Growth segment increment?");
				double deltaRho = Double.parseDouble(rho1);
				System.out.println(deltaRho);

				//run the directed random walk plant algorithm discussed in class
				randomWalk(size, stems, steps, alpha, deltaTheta, deltaRho);
			}
		});
		
		fileMenu.add(randomWalk);
		
		//Exit
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				System.exit(0);
			}
		});
		
		fileMenu.add(exitItem);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);
	}
	
	private void randomWalk(int size,int stems,int steps,double alpha,double deltaTheta,double deltaRho) {
		image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
		
		//Set the background color to white
		for(int x=0; x<size;x++) { 
			for(int y=0;y<size; y++) {
				image.setRGB(x, y, 0xFFFFFFFF);
			}
		}
		
		//Repeat steps for number of stems
		for(int j=0; j<stems;j++){
			double theta=(Math.PI/2);
			double rho=1;
			double tau;
			double beta=1-alpha;
			int direction;
			int lastDirection = 0;
			double stepX=size/2;
			double stepY=size/2;
			double nextStepX = 0;
			double nextStepY = 0;
			
			image.setRGB((int)stepX, (int)stepY, 0xFF000000);
			
			//Generate each new step
			for(int i=1;i<steps+1;i++){
				if(i==1){
					if (new Random().nextDouble()>.5) {
						direction=1;
					}
					else {
						direction=-1;
					}
				
					rho=rho+deltaRho;
					theta=theta+(deltaTheta*(new Random().nextDouble())*direction);
				
					nextStepX=(rho*Math.cos(theta))+stepX;
					nextStepY=-(rho*Math.sin(theta))+stepY;
					
					lastDirection=direction;
				}
				
				else{
					stepX=nextStepX;
					stepY=nextStepY;
					
					if (lastDirection==-1) {
						tau=alpha;
					}
					else {
						tau=beta;
					}
				
					if (new Random().nextDouble()>tau) {
						direction=1;
					}
					else {
						direction=-1;
					}
				
					rho=rho+deltaRho;
					theta=theta+(deltaTheta*(new Random().nextDouble())*direction);
				
					nextStepX=(rho*Math.cos(theta))+stepX;
					nextStepY=-(rho*Math.sin(theta))+stepY;
					
					lastDirection=direction;
					
				}
				
				//Connect previous point with new point
				Graphics2D g2d = (Graphics2D) image.createGraphics();
				g2d.setColor(Color.BLACK);
				g2d.draw(new Line2D.Double(stepX,stepY,nextStepX,nextStepY));
			}
		
		}
		
		//display the image
		displayBufferedImage(image);
	}
	

	
	public void displayBufferedImage(BufferedImage image) {
		this.setContentPane(new JScrollPane(new JLabel(new ImageIcon(image))));
		
		this.validate();
	}
}