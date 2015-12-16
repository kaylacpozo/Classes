import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.image.*;
import java.io.*;
import java.util.Random;
import javax.swing.*;

public class HW05b {
	private static int WIDTH = 400;
	private static int HEIGHT = 400;
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					createAndShowGUI();
				}
			}
		);
	}
	
	private static void createAndShowGUI(){
		JFrame frame = new Frame(WIDTH,HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

class Frame extends JFrame{
	private final JFileChooser chooser;
	private BufferedImage image;
	String result;
	int startColor=0xFF654321;
	int finalColor=0xFF32CD32;
	
	double startWidth=6.0;
	double endWidth=0.5;
	
	//Constructor
	public Frame(int width, int height){
		//Sets up frame's attributes
		this.setTitle("CAP 3027 2015 - HW05b - Kayla Pozo");
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
				
				//prompt user for number of steps
				String steps1 = JOptionPane.showInputDialog("Number of steps?");
				int steps = Integer.parseInt(steps1);
				
				//prompt user for the transmission probability[0.0,1.0]
				String alpha1 = JOptionPane.showInputDialog("Transmission probability [0.0,1.0]?");
				double alpha = Double.parseDouble(alpha1);
				
				//prompt user for the maximum rotation increment[0.0,1.0]
				String theta1 = JOptionPane.showInputDialog("Maximum rotation increment [0.0,1.0]?");
				double deltaTheta = Double.parseDouble(theta1);
				
				//prompt user for the growth segment increment
				String rho1 = JOptionPane.showInputDialog("Growth segment increment?");
				double deltaRho = Double.parseDouble(rho1);
				
				image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
				
				//Generate color
				Color[] Color;
				Color = new Color[steps];
				
				BasicStroke[] BasicStroke;
				BasicStroke = new BasicStroke[steps];
				
				interpolate(Color, BasicStroke, image, startColor, finalColor, startWidth, endWidth, steps);
				
				//Run the directed random walk plant algorithm discussed in class
				randomWalk(Color, BasicStroke, image, size, stems, steps, alpha, deltaTheta, deltaRho);
			}
		});
		
		fileMenu.add(randomWalk);
		
		//Configure begin and end colors
		JMenuItem chooseColor = new JMenuItem("Configure colors");
		chooseColor.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				String beginColor = JOptionPane.showInputDialog("Stem starting color [IntRGB]?");
				startColor=(int) Long.parseLong(beginColor.substring(2,beginColor.length()),16);
				
				String endColor = JOptionPane.showInputDialog("Stem ending color [IntRGB]?");
				finalColor = (int) Long.parseLong(endColor.substring(2, endColor.length()),16);
					
			}
		});
			
		fileMenu.add(chooseColor);
		
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
	
	
	
	private void interpolate(Color[] Color, BasicStroke[] BasicStroke, BufferedImage image, int startColor, int finalColor, double startWidth, double endWidth, int steps){
		Color[0]=new Color(startColor);
		Color[steps-1]=new Color(finalColor);
		int red2,green2,blue2,red1,green1,blue1,red,green,blue,rgb;
		
		for(int i=1; i<steps-1; i++) {
			red2 = (finalColor>>>16) & 0xFF;
			green2 = (finalColor>>>8) & 0xFF;
			blue2 = finalColor & 0xFF;
			
			
			red1 = (startColor>>>16) & 0xFF;
			green1 = (startColor>>>8) & 0xFF;
			blue1 = startColor & 0xFF;
			
			//Color Interpolation
			red1 = (red2-red1)/(steps-i+1) +red1;
			green1 = (green2-green1)/(steps-i+1) + green1;
			blue1 = (blue2-blue1)/(steps-i+1) + blue1;
			
			red=Math.round(red1);
			green=Math.round(green1);	
			blue=Math.round(blue1);	
			
			rgb = 0xFF000000 |(red<<16) | (green<<8) | blue;
			Color stepColor = new Color(rgb);
			
			
			Color[i]=stepColor;
		}
		
		
		BasicStroke[0]=new BasicStroke((float) startWidth);
		BasicStroke[steps-1]=new BasicStroke((float) endWidth);
		
		for(int i=1; i<steps-1; i++) {
			//Stroke width interpolation
			startWidth=(((endWidth-startWidth)/(steps-1-i))+startWidth);
			
			BasicStroke stroke = new BasicStroke((float) startWidth);
			
			BasicStroke[i]=stroke;
		}

	}
		

	private void randomWalk(Color[] Color, BasicStroke[] BasicStroke, BufferedImage image, int size,int stems,int steps,double alpha,double deltaTheta,double deltaRho) {		
		new Thread (new Runnable(){
			public void run(){//defines task
				//final BufferedImage image = makeImage();
				
				//Set the background color to black
				for(int x=0; x<size;x++) { 
					for(int y=0;y<size; y++) {
						image.setRGB(x, y, 0xFF000000);
					}
				}
				
				double theta=(Math.PI/2);
				double rho=1;
				double tau;
				double beta=1-alpha;
				int direction;
				int lastDirection = 0;
				double stepX=size/2;
				double stepY=size/2;
				double nextStepX;
				double nextStepY;
				double[][][] Steps=new double[steps][stems][2];
				double[][][] Direction = new double[steps][stems][1];
				double[][][] Rho = new double[steps][stems][1];
				double[][][] Theta = new double[steps][stems][1];		
						
				image.setRGB((int)stepX, (int)stepY, 0xFF000000);
					
				//Create steps for stems
				for(int i=1; i<steps+1;i++){
					//Generate each new step for each stem
					for(int j=0; j<stems;j++){
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
							stepX=Steps[i-2][j][0];
							stepY=Steps[i-2][j][1];
							
							if ((Direction[i-2][j][0])==-1) {
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
						
							rho=(Rho[i-2][j][0])+deltaRho;
							theta=(Theta[i-2][j][0])+(deltaTheta*(new Random().nextDouble())*direction);
						
							nextStepX=(rho*Math.cos(theta))+stepX;
							nextStepY=-(rho*Math.sin(theta))+stepY;
							
							lastDirection=direction;
							
						}
						
						//Connect previous point with new point
						Graphics2D g2d = (Graphics2D) image.createGraphics();
						
						RenderingHints hint = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						
						g2d.setColor(Color[i-1]);
						g2d.setRenderingHints(hint);
						g2d.setStroke(BasicStroke[i-1]);
						g2d.draw(new Line2D.Double(stepX,stepY,nextStepX,nextStepY));
						
						Steps[i-1][j][0]=nextStepX;
						Steps[i-1][j][1]=nextStepY;
						Direction[i-1][j][0]=lastDirection;
						Rho[i-1][j][0]=rho;
						Theta[i-1][j][0]=theta;
					}
					
					//Display the image
					displayBufferedImage(image);
				}
				
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						displayBufferedImage(image);
					}
				});
			}
		}).start();
		
	}
	
	
	public void displayBufferedImage(BufferedImage image) {
		this.setContentPane(new JScrollPane(new JLabel(new ImageIcon(image))));
		
		this.validate();
	}
}