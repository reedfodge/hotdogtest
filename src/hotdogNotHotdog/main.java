package hotdogNotHotdog;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class main {
	static Image image;
	static JLabel label;
	
	
	//The following getters and setters make more sense later
	
	/**
	 * Sets selected image
	 * @param image
	 */
	public void setImage(Image image) {
		this.image = image;
	}
	
	/**
	 * Gets selected image
	 * @return
	 */
	public static Image getImage() {
		return image;
	}
	
	/**
	 * Sets label with selected image
	 * @param label
	 */
	public void setLabel(JLabel label) {
		this.label = label;
	}
	
	/**
	 * Gets label with selected image
	 * @return
	 */
	public JLabel getLabel() {
		return label;
	}
	
	
	
	/**
	 * Does all the work
	 */
	public void createGUI() {
		//Create Frame
		JFrame frame = new JFrame("Hotdog or Not Hotdog?");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 600);
		
		final JFileChooser fc = new JFileChooser();

		//Create Menu Bar
		JMenuBar mb = new JMenuBar();
		JMenu m1 = new JMenu("File");
		JMenu m2 = new JMenu("Help");
		mb.add(m1);
		mb.add(m2);
		JMenuItem m11 = new JMenuItem("Upload File");
		
		//Adds action for Upload Image
		m11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fc.addChoosableFileFilter(new FileNameExtensionFilter("Image files",
				          new String[] { "png", "jpg", "jpeg", "gif" }));
				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			        try {
			          setImage(ImageIO.read(fc.getSelectedFile()));
			          ImageIcon icon = new ImageIcon((Image)getImage());
			          JLabel label = new JLabel();
			          label.setIcon(icon);
			          setLabel(label);
			          frame.getContentPane().add(BorderLayout.CENTER, getLabel());
			          frame.revalidate();
			          frame.repaint();
			        } catch (Exception ex) {
			          ex.printStackTrace();
			        }
			      }
			}
		});
		
		
		//Adds action to perform test 
		JButton button = new JButton("Test");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean res = testImage();
				isTrue(res);
			}
		});
		frame.getContentPane().add(BorderLayout.SOUTH, button);
		
		m1.add(m11);
		
		//Adds action to open instructions window
		JMenuItem m21 = new JMenuItem("Instructions");
		m2.add(m21);
		m21.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showHelp();
			}
		});
		
		//Adds action to reset GUI 
		JMenuItem m12 = new JMenuItem("Reset");
		m12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(label);
				frame.repaint();
				frame.revalidate();
			}
		});
		m1.add(m12);
		
		//Create Dialogue
		frame.getContentPane().add(BorderLayout.NORTH, mb);
		frame.setVisible(true);
	}
	
	/**
	 * Initializes GUI that gives instructions of how to use app
	 */
	public static void showHelp() {
		//Initialize Instruction GUI
		JFrame helpFrame = new JFrame("Instructions");
		helpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		helpFrame.setSize(1000, 100);
		JLabel text = new JLabel("Instructions: Select File -> Upload Image to upload an image. Click the Test button to determine if your uploaded image is a hotdog");
		helpFrame.getContentPane().add(BorderLayout.CENTER, text);
		helpFrame.setVisible(true);
	}
	
	/**
	 * Converts Image to BufferedImage for manipulation
	 * Credit: Sri Harsha Chilakapati on StackOverflow
	 * @param img
	 * @return Buffered Image 
	 */
	public static BufferedImage toBufferedImage(Image img) {
		if(img instanceof BufferedImage) {
			return (BufferedImage)img;
		}
		
		BufferedImage bimage = new BufferedImage(img.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		
		return bimage;
	}
	
	/**
	 * Tests image uploaded to GUI to determine if it is a hot dog
	 * @return If images are the same
	 */
	public static boolean testImage() {
		Image image = getImage();
		boolean totalRes = false;
		double total = 0;
		BufferedImage testImage = toBufferedImage(image);
		BufferedImage controlImage = null;
		try {
			controlImage = ImageIO.read(new File("hotdog.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(testImage.getHeight() == controlImage.getHeight() && testImage.getWidth() == controlImage.getWidth()) {
			for(int y = 0; y < testImage.getHeight(); y++) {
				for(int x = 0; x < testImage.getWidth(); x++) {
					//Uses Mean Square Error to determine difference in images
					total += Math.pow(controlImage.getRGB(x, y) - testImage.getRGB(x, y), 2);
				}
			}
		}
		
		if(total != 0) {
			totalRes = false;
		}
		else {
			totalRes = true;
		}
		
		return totalRes;
		
		
	}
	
	/**
	 * Initializes GUI with result of if image is a hotdog
	 * @param res
	 */
	public static void isTrue(boolean res) {
		JFrame frame = new JFrame("Result");
		JLabel t = new JLabel("This is a hot dog. Congratulations!");
		JLabel f = new JLabel("This is not a hot dog. Bummer.");
		frame.setSize(300, 100);
		if(res == true) {
			frame.getContentPane().add(BorderLayout.CENTER, t);
		}
		else {
			frame.getContentPane().add(BorderLayout.CENTER, f);
		}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
