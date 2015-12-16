import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class HW04 {
	private static int WIDTH = 400;
	private static int HEIGHT = 400;
	
	public static void main(String[] args) {
		JFrame frame = new DisplayFrame(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

class DisplayFrame extends JFrame {
	private final JFileChooser chooser;
	private BufferedImage sourceImage;
	private BufferedImage image;
	int size;
	
	//Constructor
	public DisplayFrame(int width, int height) {
		//Sets up the frames attributes
		this.setTitle("CAP 3027 2015 - HW04 - Kayla Pozo");
		this.setSize(width, height);
		
		//Adds menu to the frame
		addMenu();
		
		//setup chooser dialog
		
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
	}
	
	private void addMenu() {
		//Setup the frame's menu bar
		JMenu fileMenu = new JMenu("File");
		
		//Opens image
		JMenuItem loadImage = new JMenuItem("Load source image");
		loadImage.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				open();
			}
		});
		
		fileMenu.add(loadImage);
		
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
	
	//Chooses a file, loads it, and displays the image
	private void open() {
		File file = getFile();
		if (file != null) {
			displayFile(file);
			createImage(sourceImage);
		}
	}
	
	//Opens a file selected by user
	private File getFile() {
		File file = null;
		
		if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
		}
		
		return file;
	}
	
	//Displays selected file in the frame
	private void displayFile(File file) {
		try {
			sourceImage = ImageIO.read(file);
			displayImage(sourceImage);
		}
		
		catch(IOException exception){
			JOptionPane.showMessageDialog(this, exception);
		}

	}
	
	//Creates the square buffered image (crops) from the source image
	private void createImage(BufferedImage sourceImage) {
		if (sourceImage.getWidth() > sourceImage.getHeight()) {
			size = sourceImage.getHeight();
		}
		else {
			size = sourceImage.getWidth();
		}
		
		sourceImage = sourceImage.getSubimage(0, 0, size, size);

		image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
		
		for(int x=0; x<size;x++) { 
			for(int y=0;y<size; y++) {
				image.setRGB(x, y, 0xFF000000);
			}
		}
		
		float sumR=0, sumG=0, sumB=0;
		float num=size*size;
		
		for(int x=0; x<size; x++) {
			for(int y=0; y<size; y++) {
				sumR += (sourceImage.getRGB(x, y)>>>16) & 0xFF;
				sumG += (sourceImage.getRGB(x, y)>>>8) & 0xFF;
				sumB += (sourceImage.getRGB(x, y)) & 0xFF;
			}
		}
		
		int avgR=(int) (sumR/num);
		int avgG=(int) (sumG/num);
		int avgB=(int) (sumB/num);
		
		
		Color averageColor = new Color(avgR, avgG, avgB);

		Graphics2D g2d = (Graphics2D) image.createGraphics();
		Ellipse2D.Double circle = new Ellipse2D.Double(0,0,size,size);
		g2d.setColor(averageColor);
		g2d.fill(circle);
		
		String result = JOptionPane.showInputDialog("Minimum circle diameter?");
		int diameter = Integer.parseInt(result);
		
		checkDiameter(diameter);
	}
	
	//Recursively checks the radius of circles against the given diameter
	private void checkDiameter(int diameter) {
		int quadSize=size/2;
		while(quadSize/2>=diameter){
			for(int width=0; width<size-(quadSize)+1; width=width+(quadSize)-1){
				for(int height=0; height<size-(quadSize)+1; height=height+(quadSize)-1){
					pixelAvg(width,height,quadSize);
				}
			}
			quadSize=quadSize/2;
		}
		displayImage(image);
	}
	
	//Conducts the pixel value average for the specified region
	private void pixelAvg(int i, int j, int size) {
		float sumR=0;
		float sumG=0;
		float sumB=0;
		float num=size*size;
		
		for(int x=i; x<i+size; x++){
			for(int y=j; y<j+size; y++){
				sumR += (sourceImage.getRGB(x, y)>>>16) & 0xFF;
				sumG += (sourceImage.getRGB(x, y)>>>8) & 0xFF;
				sumB += (sourceImage.getRGB(x, y)) & 0xFF;
			}
		}
		
		int avgR=(int) (sumR/num);
		int avgG=(int) (sumG/num);
		int avgB=(int) (sumB/num);
		
		Color averageColor = new Color(avgR, avgG, avgB);

		Graphics2D g2d = (Graphics2D) image.createGraphics();
		Ellipse2D.Double circle = new Ellipse2D.Double(i,j,size,size);

		g2d.setColor(averageColor);
		g2d.fill(circle);
	}
	
	//Displays the final buffered image
	public void displayImage(BufferedImage sourceImage) {
		this.setContentPane(new JScrollPane(new JLabel(new ImageIcon(sourceImage))));
		
		this.validate();
	}
}


