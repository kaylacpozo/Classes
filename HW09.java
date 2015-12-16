import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import java.awt.geom.*;

public class HW09{
	private static final int WIDTH = 600;
	private static final int HEIGHT = 450;
	
	public static void main(String[] args){
		print1("In main");
		SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					createAndShowGUI();
				}
		});
	}
	
	public static void print1(String a) {
		System.out.println(a);
	}
	
	private static void createAndShowGUI(){
		print1("in CAG1");
		JFrame frame = new ImageFrame(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

class ImageFrame extends JFrame{
	private static int WIDTH = 600;
	private static int HEIGHT = 450;
	private static BufferedImage image = null;
	private JFileChooser chooser;
	private Graphics2D g2;
	private double realMin, imgMin, realMax, imgMax;
	private double zrealMin, zimgMin, zrealMax, zimgMax;
	private double mu_real, mu_i;
	
	public ImageFrame(int width, int height){
		print2("in constructor");
		WIDTH = width;
		HEIGHT = height;
		setTitle("CAP 3027 2015 - HW09 - Kayla Pozo");
		setSize(width, height);
		
		//add a menu to the frame
		addMenu();
		
		//setup the chooser dialog
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
	}
	
	public static void print2(String a) {
		System.out.println(a);
	}
		
	private void addMenu(){
		//setup the frame's menu bar
		JMenu fileMenu = new JMenu("File");

		//Mandelbrot
		JMenuItem mItem = new JMenuItem("Mandelbrot");
		mItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				realMin = -2.0;
				imgMin = 1.5;
				realMax = 2.0;
				imgMax = -1.5;
				
				zrealMin = -2.0;
				zimgMin = 1.5;
				zrealMax = 2.0;
				zimgMax = -1.5;				
							
				
				mandelbrot(-2.0, 1.5, 2.0, -1.5);
				
				final AreaSelectPanel panel = new AreaSelectPanel(image);
				final JButton button = new JButton( "Select" );
			
				button.addActionListener( new ActionListener() {
					public void actionPerformed( ActionEvent event ) {
						button.setText( "Selected " + panel.getUpperLeft() + " to " +
							panel.getLowerRight() );
						print2("button pushed");
					}
				} );
				
				getContentPane().add( panel, BorderLayout.CENTER );
				getContentPane().add( button, BorderLayout.SOUTH );
				
				repaint();
				System.out.println("mama we made it");
			}
		});
		fileMenu.add(mItem);
		
		//Julia
		JMenuItem jItem = new JMenuItem("Julia");
		jItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				realMin = -2.0;
				imgMin = 1.5;
				realMax = 2.0;
				imgMax = -1.5;
				
				zrealMin = -2.0;
				zimgMin = 1.5;
				zrealMax = 2.0;
				zimgMax = -1.5;			
				
				julia(-2.0, 1.5, 2.0, -1.5);
			}
		});
		fileMenu.add(jItem);
		
		//Save image
		JMenuItem saveItem = new JMenuItem("Save image");
		saveItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				saveImage();
			}
		});
		fileMenu.add(saveItem);
		
		//Exit
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				System.exit(0);
			}
		});
		fileMenu.add(exitItem);
		
		//attach a menu to a menu bar
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);
	}
	
	private void mandelbrot(double initA, double initB, double endA, double endB){
		double horizDistance = Math.abs(endA - initA)/601.0;
		double vertDistance = Math.abs(endB - initB)/451.0;
		
		double w = WIDTH/(Math.abs(initA - endA));
		double h = HEIGHT/(Math.abs(initB - endB));
		
		Color[] colors = interpolateColors();
		
		image = new BufferedImage(600, 450, BufferedImage.TYPE_INT_RGB);
		g2 = (Graphics2D)image.createGraphics();
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, 600, 450);
		
		int tMax = 100, t = 0;
		double z_real = 0.0, z_i = 0.0;
		for(int i = 0; i <= 450; i++){
			if(i != 0){
				initB -= vertDistance;
			}
			for(int j = 0; j <= 600; j++){
				if(j != 0){
					initA += horizDistance;
				}
				z_real = 0.0;
				z_i = 0.0;
				t = 0;
				while(t < tMax){
					double tempZ = z_real;
					z_real = (z_real * z_real) - (z_i * z_i) + initA;
					z_i = (2.0 * tempZ * z_i) + initB;
					if(((z_real * z_real) + (z_i * z_i)) >= 4)
						break;
					else ++t;
				}
				if(t == tMax){
					g2.setColor(Color.BLACK);		
				}
				else if(t % 2 == 0){
					g2.setColor(Color.BLACK);
				}
				else{
					g2.setColor(colors[t]);
				}
				
				g2.draw(new Line2D.Double((initA - zrealMin) * w, (initB - zimgMin) * (-h), (initA - zrealMin) * w, (initB - zimgMin) * (-h)));
			}	
			initA = zrealMin;
		}
		
		displayBufferedImage(image);
	}
	
	private void julia(double initA, double initB, double endA, double endB){
		String mu = "";
		mu = JOptionPane.showInputDialog("Input value of mu");
		String[] strs = mu.split("- |\\+");
		mu_real = Double.parseDouble(strs[0]);
		mu_i = Double.parseDouble(strs[1].substring(0, strs[1].length() - 1));

		
		double horizDistance = Math.abs(endA - initA)/601.0;
		double vertDistance = Math.abs(endB - initB)/451.0;
		
		double w = WIDTH/(Math.abs(initA - endA));
		double h = HEIGHT/(Math.abs(initB - endB));
		
		Color[] colors = interpolateColors();
		
		image = new BufferedImage(600, 450, BufferedImage.TYPE_INT_RGB);
		g2 = (Graphics2D)image.createGraphics();
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, 600, 450);
		
		int tMax = 100, t = 0;
		double z_real = initA, z_i = initB;
		for(int i = 0; i <= 450; i++){
			if(i != 0){
				z_i -= vertDistance;
			}
			
			for(int j = 0; j <= 600; j++){
				if(j != 0){
					z_real += horizDistance;
				}
				
				t = 0;
				double tempZr = z_real;
				double tempZi = z_i;
				while(t < tMax){
					double tempZ = tempZr;
					tempZr = (tempZr * tempZr) - (tempZi * tempZi) + mu_real;
					tempZi = (2.0 * tempZ * tempZi) + mu_i;
					if(Math.sqrt((tempZr * tempZr) + (tempZi * tempZi)) >= 2){
						break;
					}
					else{
						++t;
					}
				}
				if(t == tMax){
					g2.setColor(Color.BLACK);
				}
				else if(t % 2 == 0){
					g2.setColor(Color.BLACK);
				}
				else{
					g2.setColor(colors[t]);
				}
								
				g2.draw(new Line2D.Double((z_real - zrealMin) * w, (z_i - zimgMin) * (-h), (z_real - zrealMin) * w, (z_i - zimgMin) * (-h)));
			}
			z_real = initA;
		}
                
		displayBufferedImage(image);
	}
	
	private void saveImage(){
		String inputName = (String)JOptionPane.showInputDialog("Name file");
		inputName += ".png";
		
		File outputFile = new File(inputName);
		
		try{
   			javax.imageio.ImageIO.write( image, "png", outputFile );
		}
		catch ( IOException e ){
   			JOptionPane.showMessageDialog(ImageFrame.this, "Error saving file", "oops!", JOptionPane.ERROR_MESSAGE );
		}	
	}
	
	private Color[] interpolateColors(){
		int color_init = 0xffff0000;
		int color_n = 0xff00ff00;
		int color_end = 0xff006400;
		
		Color[] array = new Color[100];
		array[0] = new Color(color_init);
		array[20] = new Color(color_n);
		array[80] = new Color(color_end);
		
		double stepSize = 1/50.0;
		
		double startRed = (double)((color_init >>> 16) & 0xFF);
		double startGreen = (double)((color_init >>> 8) & 0xFF);
		double startBlue = (double)(color_init & 0xFF);
		
		double midRed = (double)((color_n >>> 16) & 0xFF);
		double midGreen = (double)((color_n >>> 8) & 0xFF);
		double midBlue = (double)(color_n & 0xFF);
		
		double endRed = (double)((color_end >>> 16) & 0xFF);
		double endGreen = (double)((color_end >>> 8) & 0xFF);
		double endBlue = (double)(color_end & 0xFF);
		
		double s1r = startRed, s1g = startGreen, s1b = startBlue;
		double dr = (midRed - startRed)*stepSize;
		double dg = (midGreen - startGreen)*stepSize;
		double db = (midBlue - startBlue)*stepSize;
		
		for(int i = 1; i < 20; i++){
			s1r += dr;
			s1g += dg;
			s1b += db;
			
			array[i] = new Color((int)s1r, (int)s1g, (int)s1b);
		}
		
		s1r = midRed;
		s1g = midGreen;
		s1b = midBlue;
		dr = (endRed - midRed)*stepSize;
		dg = (endGreen - midGreen)*stepSize;
		db = (endBlue - midBlue)*stepSize;
		for(int i = 21; i < 80; i++){
			s1r += dr;
			s1g += dg;
			s1b += db;
			
			array[i] = new Color((int)s1r, (int)s1g, (int)s1b);
		}
		return array;
	}
	public void displayBufferedImage(BufferedImage image){
		this.setContentPane(new JLabel(new ImageIcon(image)));
		this.validate();
	}
}
	class AreaSelectPanel extends JPanel {
		private final Color OUTLINE_COLOR = Color.BLACK;
		// panel size
		private final int WIDTH, MAX_X;
		private final int HEIGHT, MAX_Y;
		// image displayed on panel
		private BufferedImage image;
		private Graphics2D g2d;
		// current selection
		private int x = -1;
		private int y = -1;
		private int w = 0;
		private int h = 0;
	
		//------------------------------------------------------------------------
		// constructor
		public AreaSelectPanel( BufferedImage image ) {
			System.out.println("In areaSelectPanel");
			this.image = image;			
			g2d = image.createGraphics();
			g2d.setXORMode( OUTLINE_COLOR );
			// define panel characteristics
			WIDTH = image.getWidth();
			HEIGHT = image.getHeight();
			Dimension size = new Dimension( WIDTH, HEIGHT );
			setMinimumSize( size );
			setMaximumSize( size );
			setPreferredSize( size );
			MAX_X = WIDTH - 1;
			MAX_Y = HEIGHT - 1;
			
			addMouseListener( new MouseAdapter() {
				public void mousePressed( MouseEvent event ) {
					clearSelection( event.getPoint() );
				}
			} );
			
			addMouseMotionListener( new MouseMotionAdapter() {
				public void mouseDragged(MouseEvent event) {
					updateSelection( event.getPoint() );
				}
			} );
		}
	
		//------------------------------------------------------------------------
		// accessors - get points defining the area selected
		Point2D.Double getUpperLeft() {
			return getUpperLeft( new Point2D.Double() );
		}
	 
		Point2D.Double getUpperLeft( Point2D.Double p ) {
			if ( w < 0 )
				if ( h < 0 )
					p.setLocation( (x+w)/((double) MAX_X), (y+h)/((double) MAX_Y) );
				else
					p.setLocation( (x+w)/((double) MAX_X), y/((double) MAX_Y) );
			else if ( h < 0 )
				p.setLocation( x/((double) MAX_X), (y+h)/((double) MAX_Y) );
			else
				p.setLocation( x/((double) MAX_X), y/((double) MAX_Y) );
			return p;
		}

		Point2D.Double getLowerRight() {
			return getLowerRight( new Point2D.Double() );
		}
	 
		Point2D.Double getLowerRight( Point2D.Double p ) {
			if ( w < 0 )
				if ( h < 0 )
					p.setLocation( x/((double) MAX_X), y/((double) MAX_Y) );
				else
					p.setLocation( x/((double) MAX_X), (y+h)/((double) MAX_Y) );
			else if ( h < 0 )
				p.setLocation( (x+w)/((double) MAX_X), y/((double) MAX_Y) );
			else
				p.setLocation( (x+w)/((double) MAX_X), (y+h)/((double) MAX_Y) );
			return p;
		}
	 
		//------------------------------------------------------------------------
		// change background image
		public void setImage( BufferedImage src ) {
			g2d.setPaintMode();
			g2d.drawImage( src,
					0, 0, MAX_X, MAX_Y,
					0, 0, (src.getWidth() - 1), (src.getHeight() - 1),
					OUTLINE_COLOR, null );
			g2d.setXORMode( OUTLINE_COLOR );
			x = -1;
			y = -1;
			w = 0;
			h = 0;

		}
	 
		//------------------------------------------------------------------------
		// behaviors

		public void paintComponent( Graphics g ) {
			super.paintComponent( g );
			g.drawImage( image, 0, 0, null );
		}
	 
		private void clearSelection( Point p ) {
			// erase old selection
			drawSelection();
			// begin new selection
			x = (p.x < 0) ? 0 : ( (p.x < WIDTH) ? p.x : MAX_X );
			y = (p.y < 0) ? 0 : ( (p.y < HEIGHT) ? p.y : MAX_Y );
			w = 0;
			h = 0;
			drawSelection();
		}
		
		private void updateSelection( Point p ) {
			// erase old selection
			drawSelection();

			// modify current selection
			int px = (p.x < 0) ? 0 : ( (p.x < WIDTH) ? p.x : MAX_X );
			int py = (p.y < 0) ? 0 : ( (p.y < HEIGHT) ? p.y : MAX_Y );
			w = px - x;
			h = py - y;
			drawSelection();
		}
		private void drawSelection() {
			if ( w < 0 )
				if ( h < 0 )
					g2d.drawRect( (x+w), (y+h), -w, -h );
				else
					g2d.drawRect( (x+w), y, -w, h );
			else if ( h < 0 )
				g2d.drawRect( x, (y+h), w, -h );
			else
				g2d.drawRect( x, y, w, h );
			repaint();
		}
	}