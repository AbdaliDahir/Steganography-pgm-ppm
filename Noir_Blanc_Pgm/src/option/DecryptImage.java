package option; 

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.*;
 

import java.awt.*;
import java.awt.event.*;

public class DecryptImage extends JFrame implements ActionListener {
	
		// Buttons
		 JButton open = new JButton("Image a Decryptée"), 
				 embed = new JButton("Decrypte"),
				 save = new JButton("Enregistrer Sous"), 
				 reset = new JButton("Videz Tout");
		 
		 // Radio Buttons to choose Type
		 JRadioButton ColorButton   = new JRadioButton("With Color", true),
				 	  GrayButton    = new JRadioButton("Grey Color", false), 
				      BlackWithButton    = new JRadioButton("Black and White Color", false);
		 
		 // Panel in Center of The Window.
		 JScrollPane ImageDecryptPath = new JScrollPane(), 
					 ImageSavePath = new JScrollPane();
		
		// Spinner To Choose Intensity Between 1 and 8 default 4
		static Integer value = 4;
	    static Integer minArrow = 1;
	    static Integer maxArrow = 8;
	    Integer step = 1;
	    SpinnerNumberModel model = new SpinnerNumberModel(value, minArrow, maxArrow, step);
	    JSpinner spinner = new JSpinner(model);
	    
		 /**
		  *  MAgic Number P2 or P3 If pgm format use P2 ppm Use P3
		  *  By Default we using P3 / The image Selected By Default Is ppm check Radio Button In Footer
		  */
	     public static String MagicNumber = "P3";
	    
	     // File Type PPM OR PGM
	 	 public static String fileType = "ppm";
	    
	    
		 // Declaring Images 
		 File sourceImage = null;  
		 
		 // Declaring Path To Save Image It will get Automatic After choosing When u want to Save Image And Name Too
		 public String SavePath = null;
		 
		 // This Is To Know Where Type Of Crypt We use Color/Gray or Black and white.
		 public static String WeUse = "ColorPart";
		 
		 // The Max Intensity Value
		 public static int INtMax = value;
		 
		  
		 // Interface
		 public DecryptImage() {
		    super("Steganography - Decrypted");
		    assembleInterface(); 
		    // this.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().
		    // getMaximumWindowBounds());
		    this.setSize(700, 400);
		    this.setLocationRelativeTo(null);
		    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);   
		    this.setVisible(true); 
		    this.validate();
	    }
		 
		
		// MAKE INTERFACE End Add Events
		private void assembleInterface() {
			 
		    JPanel p = new JPanel(new FlowLayout());
		    p.add(open); 
		    p.add(save);
		    p.add(embed);   
		    p.add(reset); 
			
		    // Window Top Buttons
		    this.getContentPane().add(p, BorderLayout.NORTH);
		    open.addActionListener(this); 
		    embed.addActionListener(this);
		    save.addActionListener(this);   
		    reset.addActionListener(this);
		    open.setMnemonic('O'); 
		    embed.setMnemonic('E');
		    save.setMnemonic('S');
		    reset.setMnemonic('R'); 
		    
		    // INTERFACE FOR ENCRYPT
		    // ADD Btton to bottom of Window "SOUTH" 
		    p = new JPanel(new GridLayout(1,1));
		    ButtonGroup bgroup = new ButtonGroup();
			bgroup.add(ColorButton);
			bgroup.add(GrayButton);
			bgroup.add(BlackWithButton);
			spinner.setFont(new Font("Tahome", Font.PLAIN, 16));
			spinner.setBounds(60, 90, 95, 27);
			p.add(spinner); 
		    p.add(ColorButton);
			p.add(GrayButton); 
			p.add(BlackWithButton); 
		    ColorButton.addActionListener(this);
		    GrayButton.addActionListener(this);
		    BlackWithButton.addActionListener(this);
		    p.setBorder(BorderFactory.createTitledBorder("Choose Imag Type :"));  
		    this.getContentPane().add(p, BorderLayout.SOUTH);
		    
		    
		   
		    // CENTER Path
		    p = new JPanel(new GridLayout(0,1)); 
		    p.add(new JScrollPane(ImageDecryptPath));  
		    p.add(new JScrollPane(ImageSavePath)); 
		    addPath();
		    p.setBorder(BorderFactory.createTitledBorder("Debugin Dashoard :")); 
		    this.getContentPane().add(p, BorderLayout.CENTER);
		    
		 
		    // BOX To Show Image Original 
		    // Text 
		    JLabel label = new JLabel();
		    label.setText("<html>"
		    		+ "<ul><li> "
		    		+ "We using .ppm format for Colored Image and .pgm format for Grys Image By Default we selected Colored Image"
		    		+ "</li><li>"
		    		+ "Choose the Max intensity Value it's need to be betwen 0 and 8 "
		    		+ "</li></ul>"
		    		+ "</html>"); 
		    p.add(label);
		    this.getContentPane().add(p, BorderLayout.CENTER);
		      
		}
		 
		 // EVENTS Click Call Method
		 public void actionPerformed(ActionEvent ae) {
		    Object o = ae.getSource(); 
		    if(o == open) {
		    	openImage(); 
		    	checkImg();
		    	addPath();
		    }  
		    else if(o == save) {
		       saveImage();
		       addPath();
		    }
		    else if(o == reset) { 
		       resetInterface(); 
		       addPath();
		    }
		    else if(o == embed){
		    	SpinnerValue();
				BeforeDecrypt(); 
			}  
		    else if (ColorButton.isSelected()) { 
		    	fileType = "ppm";  
		    	MagicNumber = "P3";
		    	WeUse = "ColorPart";
		    } else if(GrayButton.isSelected()) {
		    	fileType = "pgm";
		    	MagicNumber = "P2";
		    	WeUse = "GrayPart";
		    } else if(BlackWithButton.isSelected()) {
		    	fileType = "pgm";
		    	MagicNumber = "P2";
		    	WeUse = "BlacWhitePart";
		    	INtMax = 1; 
		    	// Disable Intensity Field In Black and White
		    	JFormattedTextField spin=((JSpinner.DefaultEditor)spinner.getEditor()).getTextField();
		    	spin.setEnabled(false); 
		    }
		     
		 }
	 
		 // Show FileDialog To Choose Images and When Save Image Crypted etc...
		 private java.io.File showFileDialog(final boolean open) {
		    JFileChooser fc = new JFileChooser("Open an image");
		    javax.swing.filechooser.FileFilter ff = new javax.swing.filechooser.FileFilter() {
		       public boolean accept(java.io.File f) {
		          String name = f.getName().toLowerCase();
		          if(open)
		             return f.isDirectory() || name.endsWith("."+fileType);
		          
		             return f.isDirectory() || name.endsWith("."+fileType) ;
		          }
		       public String getDescription() {
		          if(open)
		             return "Image (*."+ fileType +")";
		          	 return "Image (*."+ fileType +")";
		          }
		       };
		    fc.setAcceptAllFileFilterUsed(false);
		    fc.addChoosableFileFilter(ff);
		 
		    java.io.File f = null;
		   if(open && fc.showOpenDialog(this) == fc.APPROVE_OPTION)
		       f = fc.getSelectedFile();
		    else if(!open && fc.showSaveDialog(this) == fc.APPROVE_OPTION)
		       f = fc.getSelectedFile();
		    return f;
		}
	 	 
		 // Image Base
		 private File openImage() {
		    java.io.File f = showFileDialog(true); 
		    return sourceImage = f;  
		 }
		 
		 /**
		  *  If The User start to Choosing Images
		  *  Disable option to change Type
		  *  Example : They can't choose pgm Images With Type color
		  **/
		 private void checkImg() { 
	        if(sourceImage != null) {
		    	ColorButton.setEnabled(false);
		    	GrayButton.setEnabled(false);
		    	BlackWithButton.setEnabled(false);
		    }
		 }
		 
		 /**
		  *  Show The New Path selected After Choosing Imgs.
		  **/
		 private void addPath() {
			 JLabel I1 = new JLabel("Image Base Path : " + sourceImage + "\n");
			 ImageDecryptPath.getViewport().add(I1); 
			 JLabel S3 = new JLabel("Image Save To : " + SavePath + "\n");
		  	  ImageSavePath.getViewport().add(S3);
		 }
		 
		 /**
		  * Get Intensity Max Before start Decrypting 
		  * By Default It's 4
		  * For Black and White it's 1 
		  */
		 private void SpinnerValue() {
			 INtMax = (Integer) spinner.getValue();
			 System.out.println(INtMax);
		 }

		 /**
		 *  Some Validation Before Start Decrypt
		 *  Check if there is img && path to save if not show Message Dialog 
		 *  If everything is good Invoke DeryptImage Method after finished Createfile using method createFile
		 **/
		 private void BeforeDecrypt() {   
			 System.out.println(INtMax);
			 if(sourceImage == null || SavePath == null) {  
		       JOptionPane.showMessageDialog(this, "check if you added image. and the save path!", 
		         "Nothing to Crypte", JOptionPane.ERROR_MESSAGE); 
		      } else {
		    	  String FilePath = sourceImage.toString(); 
		    	  String[][] matrix;
				try {
					matrix = DecryptImage.DeryptImage(FilePath);
					DecryptImage.createFile(matrix, SavePath); 
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				   
		      }
		 }
		 
		 
		/** 
		 *  Start Encrypting Image This Method take 1 @param (Crypt Image) / Paths (Type String)   
		 *  The 3 types is there
		 * @param filename
		 * @return
		 * @throws FileNotFoundException
		 **/
		 public static String[][] DeryptImage(String filename) throws FileNotFoundException {
			  
			    Scanner scanner = new Scanner(new File(filename)); 
			    
			    scanner.next(); // magic number 
			    
			    
				    int width = scanner.nextInt();
				    int height = scanner.nextInt();
				    int max = scanner.nextInt(); 
				    
				    String[][] image = new String[height][width];
				    
				    for (int i = 0; i < height; ++i)
				    {
				      for (int j = 0; j < width; ++j)
				      { 
				    	  if(WeUse == "ColorPart") { 
				    		int var1 = scanner.nextInt();
				    		int var2 = scanner.nextInt();
				    		int var3 = scanner.nextInt();
				    		String general =  (var1 % INtMax)  + " " + (var2 % INtMax)  + " " + (var3 % INtMax) ; 
							image[i][j] = general;  
								
				    	  } else if(WeUse == "GrayPart") {
					    		int value = scanner.nextInt(); 
						        image[i][j] = value % INtMax + "";  
						  } else if (WeUse == "BlacWhitePart") { 
					        int value = scanner.nextInt(); 
					        if(value % 2 != 0) { 
					        	image[i][j] = INtMax + ""; 
						    } else {
						    	image[i][j] = 0 + "";
						    }
						  }
				      }
				    }
				    return image;
				  
			  }
		   
		 /**
		 * After Encrypting CreateFile to save Img 2 @param (crypted array returned by Method CryptImage() , where save img)
		 * @param image
		 * @param SavePath
		 * @throws FileNotFoundException
		 **/
		 public static void createFile(String[][] image, String SavePath) throws FileNotFoundException { 
			  
		    PrintWriter pw = new PrintWriter(SavePath + "." + fileType);
		    
		    int width = image[0].length;
		    int height = image.length;
		    
		    // magic number, width, height, and MAXVAL
		    pw.println(MagicNumber);
		    pw.println(width + " " + height);
		    pw.println(INtMax-1);
		    
		    // print out the data, limiting the line lengths to 595 characters
		    int lineLength = 0;
		    for (int i = 0; i < height; ++i)
		    {
		      for (int j = 0; j < width; ++j)
		      {
		        String value = image[i][j];   
		        pw.print(value + " ");
		      }
		    }
		    
		    pw.close();   
		    success();
		}
		
		 /**
		 *  Show Message To User after Image Saved
		 **/
		 public static void success() {
			// Use showMessageDialog to show error with 4 parms (componnet, message, title, Typemessage)
		       JOptionPane.showMessageDialog(null, "Your Img Is Cryted With Success!", 
		         "The image has been encrypted", JOptionPane.INFORMATION_MESSAGE); 
		 }

		 /**
		 * When Click Button save Image
		 * you need to select 2 images(principal && hidden) Before trying to save. 
		 */
		private void saveImage() {
		 java.io.File f = showFileDialog(false);
		 String name = f.getName(); 
		    
	     if(sourceImage == null) {
	       JOptionPane.showMessageDialog(this, "No Image has been embedded!", 
	         "Nothing to save", JOptionPane.ERROR_MESSAGE); 
	      } else {
	    	  SavePath = f.toString(); 
	      } 
		}

		// Reset to default
		private void resetInterface() { 
		    sourceImage = null;   
			SavePath = null;
			ColorButton.setEnabled(true);
		    GrayButton.setEnabled(true);
		    BlackWithButton.setEnabled(true);
		}
	 
		public static void main(String arg[]) {
		   new DecryptImage(); 
		}
}

