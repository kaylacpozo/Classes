import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.*;
import java.util.ArrayList;


public class HW07 {
	private static final int WIDTH = 400;
	private static final int HEIGHT = 400;
	
	//EDT calls worker thread
	public static void main(String[] args) {
		SwingUtilities.invokeLater( new Runnable(){
			public void run(){
				createAndShowGUI();
			}
		});
	}
	private static void createAndShowGUI()
	{
		JFrame frame = new Frame(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                 
		frame.setVisible( true );
	}
}

class Frame extends JFrame {
	private final JFileChooser chooser;
	private BufferedImage image = null;
	private static File file = null;
	private static ArrayList<String> matrices = new ArrayList<String>();
	private static ArrayList<Double> probability = new ArrayList<Double>();
	private static ArrayList<Double> determinants = new ArrayList<Double>();
	private static ArrayList<AffineTransform> matrix = new ArrayList<AffineTransform>();
	private static int countLines = 0;
	private static int width = 0;
	private static int height = 0;
	private static int backColor;
	private static int foreColor;
	private static int n = 0;
	private static Graphics2D g2d = null;
	private static double x = 0;
	private static double y = 0;
	private static Point2D.Double pt1;
	private static Point2D.Double pt2;
	private static double sumDet = 0;
	private static boolean checkIfPIsGiven = false;
	private static BufferedImage imageNew = null;
	

	public  Frame( int width, int height) {
	
		this.setTitle("CAP 3027 2015 - HW07 - Kayla Pozo");
		this.setSize( width, height);
		
		addMenu();

		chooser = new JFileChooser();
		chooser.setCurrentDirectory( new File("."));
	}
	
		private void addMenu() {
		//setup the frame's menu bar

		JMenu fileMenu = new JMenu("File");
		
		//Load IFS description
		JMenuItem loadDescription = new JMenuItem("Load IFS description");
		loadDescription.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent event ) {
				loadDescription();
			}
		});
		
		fileMenu.add( loadDescription );
				
		//Configure image
		JMenuItem configure = new JMenuItem("Configure image");
		configure.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent event ) {
				configure();
			}
		});
		
		fileMenu.add( configure );
				
		//Display IFS
		JMenuItem display = new JMenuItem("Display IFS");
		display.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent event ) {
				display();
			}
		});
		
		fileMenu.add( display );
				
		//Save
		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent event ) {
				saveFile();
			}
		});
		
		fileMenu.add( saveItem );
		
		//Exit
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent event ) {
				System.exit( 0 );
			}
		});
		
		fileMenu.add( exitItem );

		JMenuBar menuBar =  new JMenuBar();
		menuBar.add( fileMenu );
		this.setJMenuBar( menuBar );
	}

	//Load IFS Description
	public void loadDescription() {
		if ( chooser.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION ) {
			file = chooser.getSelectedFile();		
		}
		String line = "";
	
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
		    while( ( line = reader.readLine() ) != null) {
		    	countLines++;
		        matrices.add(line);
		    }
		    getMatrix();
		    reader.close();
		}

		catch ( IOException e ) {
		   JOptionPane.showMessageDialog(Frame.this,e);
		}
	}
	
	//Get transform matrix
	public void getMatrix() {
		for(int i = 0; i < countLines; i++) {
			Scanner sc = new Scanner(matrices.get(i));
			double m00 = sc.nextDouble();
			double m01 = sc.nextDouble();
			double m10 = sc.nextDouble();
			double m11 = sc.nextDouble();
			double m02 = sc.nextDouble();
			double m12 = sc.nextDouble();
			
			//Creates Affine transform
			AffineTransform newAff = new AffineTransform(m00,m10,m01,m11,m02,m12); 
			matrix.add(newAff);
			
			//Is probability given
			if(sc.hasNextDouble() == true) {
				probability.add(sc.nextDouble());
				checkIfPIsGiven = true;
			}
		}
	}


	//Configure size & colors
	public void configure() {
		String imageHeight = (String)JOptionPane.showInputDialog("Image height?");
		height = Integer.parseInt (imageHeight);
		String imageWidth = (String)JOptionPane.showInputDialog("Image width?");
		width = Integer.parseInt (imageWidth);
		
		String backgroundColor = JOptionPane.showInputDialog("Background color of the image?");
		backColor=(int) Long.parseLong(backgroundColor.substring(2,backgroundColor.length()),16);
		
		String foregroundColor = JOptionPane.showInputDialog("Foreground color of the image?");
		foreColor=(int) Long.parseLong(foregroundColor.substring(2,foregroundColor.length()),16);
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g2d = (Graphics2D) image.createGraphics();
		
		//Sets background color
		Color c = new Color(backColor);
		g2d.setColor(c);
		g2d.fill(new Rectangle(0,0,width,height));
		Color c2 = new Color(foreColor);
		g2d.setColor(c2);
	}	
	
	//DisplayIFS
	public void display() {
		String input = (String)JOptionPane.showInputDialog("Number of generations?");
		n = Integer.parseInt (input);
		
		getPosition();
		
		//Check if probabilities need to be calculated
		if(checkIfPIsGiven == false) {
			getDeterminant();
			for(int i = 0; i < countLines; i++) {
				probability.add(determinants.get(i)/sumDet);
			}
		}
		
		//For the number of generations, transform the image
		for(int i = 0; i < n; i++) {
			transforms();	
		}

		imageNew = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				imageNew.setRGB(  i, j ,   image.getRGB(  i , height - j - 1));
			}
		}
		displayBufferedImage(imageNew);
		
	}
	
	//Initial points->random
	public void getPosition() {
		x = .5;
		y = .5;
		pt1 = new Point2D.Double(x,y);
		pt2 = new Point2D.Double(x,y);
	}
	
	//Get weights/probabilities based on determinants
	public void getDeterminant() {
		for(int i = 0; i < countLines; i++) {
			if((matrix.get(i).getDeterminant()) > 0) {
				determinants.add(matrix.get(i).getDeterminant());
				sumDet += matrix.get(i).getDeterminant();
			}
			
			else {
				determinants.add(0.01);
				sumDet += .01;
			}
		}
	}
	
	//Applies affine transforms
	public void transforms() {
		double rand = Math.random();
		double prev = 0;
		
		for(int i = 0; i < countLines; i++) {
			double store = probability.get(i);		
			
			if(rand <= store + prev) {
				AffineTransform tea = matrix.get(i);
				
				tea.transform(pt1, pt2);
				
				if((pt2.getX()) <= 1 && pt2.getY() <= 1 && (pt2.getY()) >= 0 && (pt2.getX()) >= 0 ) {
					image.setRGB((int)(pt2.getX()*width),(int)(pt2.getY()*height),foreColor);
				}
				
				pt1 = pt2;
				break;
			}
			prev += store;

		}
		
	}
	
	//Displays image
	public void displayBufferedImage(BufferedImage image) {
		this.setContentPane(new JScrollPane(new JLabel(new ImageIcon(image))));
		this.validate();
	}
	
	//Saves
	public void saveFile() {
		String inputName = (String)JOptionPane.showInputDialog("Name file");
		inputName += ".png";
		
		File outputFile = new File(inputName);
		try {
		   javax.imageio.ImageIO.write( imageNew, "png", outputFile );
		}
		catch ( IOException e ) {
		   JOptionPane.showMessageDialog(Frame.this, "Error saving file","oops!", JOptionPane.ERROR_MESSAGE );
		}
	}
}
