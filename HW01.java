import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import java.util.Random;

public class HW01 {
	private static final int WIDTH = 401;
	private static final int HEIGHT = 401;
	
	
	public static void main(String[] args) {
		JFrame frame = new ImageFrame(WIDTH,HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true); 
	}
}

//#############################################################################

class ImageFrame extends JFrame {
	private final JFileChooser chooser;
	private BufferedImage bufferedImage;
	public int centerX=(401/2);
	public int centerY=(401/2);
	
	//===================================================================
	//constructor
	
	public ImageFrame(int width, int height) {
		// --------------------------------------------------------------
		// setup the frame's attributes
		
		this.setTitle("CAP 3027 2015 - HW01 - Kayla Pozo");
		this.setSize(width,height);
		
		// add a menu to the frame
		addMenu();
		
		// --------------------------------------------------------------
		//setup the file chooser dialog
		
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
	}
	
	private void addMenu() {
		// ----------------------------------------------------------------
		// setup the frame's menu bar
		
		// === File menu
		
		JMenu fileMenu = new JMenu("File");
		
		// --- Infinite Plane
		
		JMenuItem walkInfinite = new JMenuItem("Drunken walk on an infinite plane");
		walkInfinite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				infinite();
			}
		});
		
		fileMenu.add(walkInfinite);
		
		// --- Bounded Plane
		
		JMenuItem walkBounded = new JMenuItem("Drunken walk on a bounded plane");
		walkBounded.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				bounded();
			}
		});
		
		fileMenu.add(walkBounded);
		
		// --- Toroidal Plane
		
		JMenuItem walkToroidal = new JMenuItem("Drunken walk on a toroidal plane");
		walkToroidal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				toroidal();
			}
		});
		
		fileMenu.add(walkToroidal);
		
		// --- Exit
		
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new
		ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		
		fileMenu.add(exitItem);
		
		// === attach menu to a menu bar
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);
		
	}
	
	//-----------------------------------------------------------------------
	//Infinite
	private void infinite() {
		String result = JOptionPane.showInputDialog("Number of steps the drunk takes?");
		int i = Integer.parseInt(result);

		//Constructing initial buffered image
		bufferedImage = new BufferedImage(401,401,BufferedImage.TYPE_INT_ARGB);
		for(int x=0; x<401;x++) { 
			for(int y=0;y<401; y++) {
				bufferedImage.setRGB(x, y, 0xFFFFFFEE);
			}
		}
		
		bufferedImage.setRGB(centerX,centerY,0xFF000000);
		
		int x=centerX;
		int y=centerY;
		
		//Constructing final image
		for(int j=0; j<i; j++) {
			Random randXvalue = new Random();
			int randX = randXvalue.nextInt(3)-1;
			
			Random randYvalue = new Random();
			int randY = randYvalue.nextInt(3)-1;
			
			x=x+randX;
			y=y+randY;
			
			bufferedImage.setRGB((x),(y),0xFF000000);
			
			if(j==(i-1)) {
				bufferedImage.setRGB((x),(y),0xFFFF0000);
			}
		}
		
		displayBufferedImage(bufferedImage);
	}
	
	//-----------------------------------------------------------------------
	//Bounded
	private void bounded() {
		String result = JOptionPane.showInputDialog("Number of steps the drunk takes?");
		int i = Integer.parseInt(result);
		
		//Constructing initial buffered image
		bufferedImage = new BufferedImage(403,403,BufferedImage.TYPE_INT_ARGB);
		for(int x=0; x<401;x++){ 
			for(int y=0;y<401; y++) {
				bufferedImage.setRGB(x, y, 0xFFFFFFEE);
			}
		}
				
		bufferedImage.setRGB(centerX,centerY,0xFF000000);
				
		int x=centerX;
		int y=centerY;
				
		//Constructing final image
		for(int j=0; j<i; j++) {
			Random randXvalue = new Random();
			int randX = randXvalue.nextInt(3)-1;
					
			Random randYvalue = new Random();
			int randY = randYvalue.nextInt(3)-1;
					
			x=x+randX;
			y=y+randY;
			
			//If drunk gets to the bound keep the steps within 
			if(x<0){
				x=-x;
			}
			
			if(x>401){
				x=401-(x-401);
			}
			
			if(y>401){
				y=401-(y-401);
			}
			
			if(y<0){
				y=-y;
			}
			
			bufferedImage.setRGB((x),(y),0xFF000000);
					
			if(j==(i-1)) {
				bufferedImage.setRGB((x),(y),0xFFFF0000);
			}
		}
				
		displayBufferedImage(bufferedImage);
	}
	
	//-----------------------------------------------------------------------
	//Toroidal
	private void toroidal() {
		String result = JOptionPane.showInputDialog("Number of steps the drunk takes?");
		int i = Integer.parseInt(result);
		
		//Constructing initial buffered image
		bufferedImage = new BufferedImage(403,403,BufferedImage.TYPE_INT_ARGB);
		for(int x=0; x<401;x++){ 
			for(int y=0;y<401; y++) {
				bufferedImage.setRGB(x, y, 0xFFFFFFEE);
			}
		}
				
		bufferedImage.setRGB(centerX,centerY,0xFF000000);
				
		int x=centerX;
		int y=centerY;
				
		//Constructing final image
		for(int j=0; j<i; j++) {
			Random randXvalue = new Random();
			int randX = randXvalue.nextInt(3)-1;
					
			Random randYvalue = new Random();
			int randY = randYvalue.nextInt(3)-1;
					
			x=x+randX;
			y=y+randY;
			
			//Bringing x and y values back around the edges
			if(x<0){
				x=401-x;
			}
			
			if(x>401){
				x=x-401;
			}
			
			if(y>401){
				y=y-401;
			}
			
			if(y<0){
				y=401-y;
			}
					
			bufferedImage.setRGB((x),(y),0xFF000000);
					
			//Making the last "step" red
			if(j==(i-1)) {
				bufferedImage.setRGB((x),(y),0xFFFF0000);
			}
		}
				
		displayBufferedImage(bufferedImage);
	}
	
	
	//-----------------------------------------------------------------------
	//Display BufferedImage
	
	public void displayBufferedImage(BufferedImage bufferedImage) {
		this.setContentPane(new JScrollPane(new JLabel(new ImageIcon(bufferedImage))));
		
		this.validate();
	}
}