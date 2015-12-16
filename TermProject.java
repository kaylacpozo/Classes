import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class TermProject extends JFrame {
	static jPanel2 contentPane;
	JPanel main;
	JPanel file;
	JLabel name;
	int brushSize;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					TermProject frame = new TermProject();
					frame.setVisible(true);
					frame.setTitle("CAP 3027 2015 - Term Project - Kayla Pozo");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

// Create frame
	public TermProject() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 400, 700, 600);
		main = new JPanel();
		setContentPane(main);
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		file = new JPanel();
		// file.setLayout(new BorderLayout());
		//name = new JLabel();
		//name.setText("Instructions: ");
		//file.setSize(0, 0);
		//file.add(name, BorderLayout.NORTH);
		//main.add(file);
		contentPane = new jPanel2();
		main.add(contentPane);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setBackground(new Color(153, 76, 0));

		// setContentPane(contentPane);
		contentPane.addMouseListener(contentPane);
		contentPane.addMouseMotionListener(contentPane);

//		Toolkit toolkit = Toolkit.getDefaultToolkit();
//		Image cursorImage = toolkit.getImage("images/brush.png");
//		Point cursorHotSpot = new Point(0, 0);
//		Cursor customCursor = toolkit.createCustomCursor(cursorImage, cursorHotSpot, "Cursor");
//       // set
//		setCursor(customCursor);
		addMenu();
	}
	
	private void addMenu(){
		JMenu fileMenu = new JMenu("File");
		JMenuItem pixel = new JMenuItem("Pixelize Canvas");
		pixel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				restartApplication();
			}
		});
		
		fileMenu.add(pixel);
				
		//Exit
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
						
		fileMenu.add(exitItem);
				
		//attach a menu to a menu bar
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);
	}
	
	private void restartApplication(){
		TermProject.this.repaint();
	}
	
	
}

class StoreDataStructure {
    Point p;
    Color c;
    
    public StoreDataStructure(Point  p , Color c) {
        this.p =  p;
        this.c = c;
    }
    
    public Point getP() {
        return p;
    }
    
    public void setP(Point p) {
        this.p = p;
    }

    public Color getC() {
        return c;
    }

    public void setC(Color c) {
        this.c = c;
    }
}

class jPanel2 extends JPanel implements MouseListener, MouseMotionListener {
	String result = JOptionPane.showInputDialog("Enter the size of the brush");
	int brushSize = Integer.parseInt(result);
	Color c = null;
	int sX = 0;
	int sY = 0;
	int curX = 0;
	int curY = 0;
	boolean dragging = false;
	int red = 0;
	int blue = 0;
	int green = 0;
	boolean isWhiteTouched = true;
	List l = new ArrayList<StoreDataStructure>();

	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.lightGray);
		g.fillOval(20, 20, 140, 100);

		g.setColor(Color.red);
		g.fillOval(40, 50, 20, 20);

		g.setColor(new Color(255, 229, 204));// brown
		g.fillOval(70, 60, 20, 20);

		g.setColor(Color.pink);
		g.fillOval(80, 30, 20, 20);

		g.setColor(Color.green);
		g.fillOval(100, 60, 20, 20);

		g.setColor(Color.blue);
		g.fillOval(40, 80, 20, 20);

		g.setColor(Color.black);
		g.fillOval(130, 60, 20, 20);

		g.setColor(Color.white);
		g.fillOval(80, 90, 20, 20);

		g.setColor(Color.white);
		g.fillOval(200, 150, 300, 300);

		Iterator<StoreDataStructure> i = l.iterator();
		while (i.hasNext()) {
			StoreDataStructure s = i.next();
			Point pt = s.getP();
			Color c = s.getC();
			g.setColor(c);
			g.fillRect(pt.x, pt.y, 5, 5);

		}
	}

	Color resultantColor(int red, int blue, int green, int red1, int blue1, int green1) {
		int finalRed = (red + red1) / 2;
		int finalBlue = (blue + blue1) / 2;
		int finalGreen = (green + green1) / 2;
		red = finalRed;
		blue = finalBlue;
		green = finalGreen;
		return new Color(finalRed, finalGreen, finalBlue);
	}

	public void mouseClicked(MouseEvent e) {
		double maxX = 0;
		double maxY = 0;
		double distance = 0;
		int x = e.getX();
		int y = e.getY();

		// for red
		maxX = 50;
		maxY = 60;
		distance = getDistance(maxX, maxY, x, y);
		if (distance <= 10) {
			if (isWhiteTouched) {
				red = 255;
				blue = green = 0;
				isWhiteTouched = false;
				c = Color.red;
			} 
			else {
				c = resultantColor(red, blue, green, 255, 0, 0);
			}
		}

		// for brown
		maxX = 80;
		maxY = 70;
		distance = getDistance(maxX, maxY, x, y);
		if (distance <= 10) {
			if (isWhiteTouched) {
				red = 255;
				blue = 204;
				green = 229;
				isWhiteTouched = false;
				c = new Color(255, 229, 204);
			} 
			else {
				c = resultantColor(red, blue, green, 255, 229, 204);
			}
		}

		// for pink
		maxX = 90;
		maxY = 40;
		distance = getDistance(maxX, maxY, x, y);
		if (distance <= 10) {
			if (isWhiteTouched) {
				red = 255;
				blue = 204;
				green = 204;
				isWhiteTouched = false;
				c = Color.pink;
			} 
			else {
				c = resultantColor(red, blue, green, 255, 204, 204);
			}
		}

		// for green
		maxX = 110;
		maxY = 70;
		distance = getDistance(maxX, maxY, x, y);
		if (distance <= 10) {
			if (isWhiteTouched) {
				c = Color.green;
				isWhiteTouched = false;
				red = 0;
				blue = 0;
				green = 255;
			} 
			else {
				c = resultantColor(red, blue, green, 0, 255, 0);
			}
		}

		// for blue
		maxX = 50;
		maxY = 90;
		distance = getDistance(maxX, maxY, x, y);
		if (distance <= 10) {
			if (isWhiteTouched) {
				c = Color.blue;
				isWhiteTouched = false;
				red = 0;
				blue = 255;
				green = 0;
			} 
			else {
				c = resultantColor(red, blue, green, 0, 0, 255);
			}
		}

		// for black
		maxX = 140;
		maxY = 70;
		distance = getDistance(maxX, maxY, x, y);
		if (distance <= 10) {
			if (isWhiteTouched) {
				c = Color.black;
				isWhiteTouched = false;
				red = blue = green = 0;
			} 
			else{
				c = resultantColor(red, blue, green, 0, 0, 0);
			}
		}

		// for white
		maxX = 90;
		maxY = 100;
		distance = getDistance(maxX, maxY, x, y);
		if (distance <= 10) {

			c = Color.white;
			red = blue = green = 0;
			isWhiteTouched = true;

		}
		
		//for canvas
		maxX = 210;
		maxY = 160;
		distance = getDistance(maxX, maxY, x, y);
		if(x >= 210 && x <= 490 && y >= 160 && y <= 440 ) {
			c = JColorChooser.showDialog(null, "Pick your paint", c);
		}
	}

	double getDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}


	public void mouseEntered(MouseEvent e) {

	}


	public void mouseExited(MouseEvent e) {
		
	}

	
	public void mousePressed(MouseEvent event) {
		Point point = event.getPoint();

	//	System.out.println("mousePressed at " + point);

		sX = point.x;

		sY = point.y;

		dragging = true;

	}

	
	public void mouseReleased(MouseEvent e) {
		dragging = false;
	}

	
	public void mouseDragged(MouseEvent event) {
		Point p = event.getPoint();
		double maxX = 350;
		double maxY = 300;
		double distance = 0;
		curX = p.x;
		curY = p.y;
		distance = getDistance(maxX, maxY, curX, curY);
		if (dragging) {
			Graphics gg = getGraphics();
			// ======================================
			// sets the color of gg to be red.
			if (distance <= 150) {
				gg.setColor(c);
				// ======================================
				// fill a rectangle on the screen
				// using the new Graphics variable, which is set to be RED
				gg.fillOval(curX, curY, brushSize, brushSize);
				StoreDataStructure str = new StoreDataStructure(p, c);
				l.add(str);
			}

		}

	}

	public void mouseMoved(MouseEvent e) {
		
	}

}
