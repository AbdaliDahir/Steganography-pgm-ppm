package option;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CryptImage extends JFrame implements ActionListener {
	 
	
	// Declaring Buttons
	 JButton open = new JButton("Principale Image"),
			 open2 = new JButton("Hidden Image"),
			 embed = new JButton("Encrypting"),
			 save = new JButton("Save To"), 
			 reset = new JButton("Reset");
	 
	 // Declaring Type of images
	 JRadioButton ColorButton   = new JRadioButton("With Color", true),
			 	  GrayButton    = new JRadioButton("Grey Color", false), 
			      BlackWithButton    = new JRadioButton("Black and White Color", false);
	 
	 // To show Images Path
	 JScrollPane ImageBasePath = new JScrollPane(),
				 ImageCachePath = new JScrollPane(),
				 ImageSavePath = new JScrollPane();
	
	 
	 /**
	  *  MAgic Number P2 or P3 If pgm format use P2. if ppm Use P3
	  *  By Default we using P3 / The image Selected By Default Is ppm. check Radio Button In Footer
	  */
     public static String MagicNumber = "P3";
    
     // File Type PPM OR PGM
 	 public static String fileType = "ppm";
    
 	 // Get Max Value for Intensity max === 255 => rgb(255,255,255)
 	 public static int MaxRgb = 255;
    
	 // Declaring Images 
	 File sourceImage = null; 
	 File  sourceImage2 = null;
	 
	 /**
	  *  Declaring Path Where Save Image 
	  *  the extension Automatic. u need just declare Name
	  *
	  **/
	 public String SavePath = null;
	 
	 /**
	  *  This Is To Know The Type Of Crypt We use Color/Gray or Black and white.
	  *  Why did not we use  FileType?
	  *  Maybe if we have just color/gray. ppm => color && pgm => gray - but we have 2 type of pgm blackWhite and gray different formula :)
	  *  so we go for this
	  **/
	 public static String WeUse = "ColorPart";
	  
	 
	 // Interface
	 public CryptImage() {
	    super("Steganography - Crypted Image in other Image");
	    assembleInterface();  
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
	    p.add(open2);
	    p.add(save);
	    p.add(embed);   
	    p.add(reset); 
		
	    this.getContentPane().add(p, BorderLayout.NORTH);
	    open.addActionListener(this);
	    open2.addActionListener(this);
	    embed.addActionListener(this);
	    save.addActionListener(this);   
	    reset.addActionListener(this);
	    ColorButton.addActionListener(this);
	    GrayButton.addActionListener(this);
	    BlackWithButton.addActionListener(this);  
	    
	    // INTERFACE FOR ENCRYPT
	    // Top of Window "NORTH" 
	    p = new JPanel(new GridLayout(1,1));
	    ButtonGroup bgroup = new ButtonGroup();
		bgroup.add(ColorButton);
		bgroup.add(GrayButton);
		bgroup.add(BlackWithButton);
	    p.add(ColorButton);
		p.add(GrayButton); 
		p.add(BlackWithButton);
	    p.setBorder(BorderFactory.createTitledBorder("Choose Imag Type :"));  
	    this.getContentPane().add(p, BorderLayout.SOUTH);
	    
	      
	    // Window CENTER
	    p = new JPanel(new GridLayout(0,1)); 
	    p.add(new JScrollPane(ImageBasePath));  
	    p.add(new JScrollPane(ImageCachePath));
	    p.add(new JScrollPane(ImageSavePath)); 
	    addPath();
	    p.setBorder(BorderFactory.createTitledBorder("Debugin Dashoard :")); 
	    this.getContentPane().add(p, BorderLayout.CENTER);
	    
	    // BOX To Show Image Original 
	    // Text 
	    JLabel label = new JLabel();
	    label.setText("<html><p> We using .ppm format for Colored Image and .pgm format for Grys Image By Default we selected Colored Image</p></html>");
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
	    else if(o == open2) {
	        openImage2();
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
			BeforeCrypt(); 
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
	    }
	     
	 }
 
	 // Show FileDialog To Choose Images and When Save Image Crypted etc ...
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
	 
	// Image Hidden
	 private File openImage2() {
	    java.io.File f = showFileDialog(true);  
	    return sourceImage2 = f; 
	 }
	 
	 /**
	  *  If The User start to Choosing Images
	  *  Disable option to change Type
	  *  Example : They can't choose pgm Images With Type color
	  **/
	 private void checkImg() { 
        if(sourceImage != null || sourceImage2 != null) {
	    	ColorButton.setEnabled(false);
	    	GrayButton.setEnabled(false);
	    	BlackWithButton.setEnabled(false);
	    }
	 }
	 
	 
	 /**
	  *  Show The New Path selected After Choosing Imgs.
	  **/
	 private void addPath() {
		 JLabel I1 = new JLabel("Principal Image Path : " + sourceImage + "\n");
		 ImageBasePath.getViewport().add(I1);
		 JLabel I2 = new JLabel("Hidden Image Path : " + sourceImage2 + "\n");
		 ImageCachePath.getViewport().add(I2);
		 JLabel S3 = new JLabel("Save To : " + SavePath + "\n");
	  	  ImageSavePath.getViewport().add(S3);
	 }
	 
	/**
	 *  Some Validation Before Start Crypt
	 *  Check if there is imgs if not show Message Dialog 
	 *  If everything is good Invoke CryptImage Method after finished Create file using method createFile
	 **/
	 private void BeforeCrypt() {   
		 if(sourceImage == null || sourceImage2 == null || SavePath == null) { 
		  // Use showMessageDialog to show error with 4 parms (componnet, message, title, Typemessage)
	       JOptionPane.showMessageDialog(this, "check if you added images. and the save path!", 
	         "Nothing to Crypte", JOptionPane.ERROR_MESSAGE); 
	      } else {
	    	  String FilePath = sourceImage.toString();
	 		  String FilePath2 = sourceImage2.toString(); 
	    	  String[][] matrix;
			try {
				matrix = CryptImage.CryptImage(FilePath,FilePath2);
				CryptImage.createFile(matrix, SavePath); 
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   
	      }
	 }
	 
	 /** 
	  *  Start Encrypting Image This Method take 2 @param ( principal Image, Hidden Image) / Paths (Type String)   
	  *  The 3 types is there 
	  * @param filename
	  * @param filename2
	  * @return
	  * @throws FileNotFoundException
	  */
	 public static String[][] CryptImage(String filename,String filename2) throws FileNotFoundException {
		  
		    Scanner scanner = new Scanner(new File(filename));
		    Scanner scan = new Scanner(new File(filename2));
		    
		    scanner.next(); // magic number
		    scan.next(); // magic number
		    
		    
			    int width = scanner.nextInt();
			    int height = scanner.nextInt();
			    int MaxRgb = scanner.nextInt();
			     
			    int width1 = scan.nextInt();
			    int height2 = scan.nextInt();
			    int max2 = scan.nextInt()+1;
			    
			    String[][] image = new String[height][width];
			    
			    for (int i = 0; i < height; ++i)
			    {
			      for (int j = 0; j < width; ++j)
			      { 
			    	  if(WeUse == "ColorPart") { 
			    		  System.out.println(max2);
			    		  System.out.println(MaxRgb);
			    	  	int var1 = scanner.nextInt();
			    		int var2 = scanner.nextInt();
			    		int var3 = scanner.nextInt();
			    		 
			    		int var4 = scan.nextInt();
			    		int var5 = scan.nextInt();
			    		int var6 = scan.nextInt(); 
			    		
			    		String general =  (var1 - ((var1 % max2) - var4)) + " " + (var2 - ((var2 % max2) - var5)) + " " + (var3 - ((var3 % max2) - var6));
			    		 
						image[i][j] = general;
						
						
			    	  } else if(WeUse == "GrayPart") {
			    		// normalize to 255
				        int value = scanner.nextInt(); 
				        int value2 = scan.nextInt(); 
				        
				        image[i][j] = (value - ((value % max2) - value2)) + " ";
				        
					  } else if (WeUse == "BlacWhitePart") {
						  
						  int value = scanner.nextInt(); 
					      int value2 = scan.nextInt(); 
					        
						if((value % 2 != 0 && value2 % 2 !=0 ) || (value % 2 == 0 && value2 % 2 ==0 )) { 
				        	image[i][j] = value + ""; 
					    } else if ((value % 2 == 0 && value2 % 2 !=0 )|| (value % 2 != 0 && value2 % 2 ==0 )) {
					    	image[i][j] = value + 1 + "";
					    } else if (value == 255 && value2 == 0 ) {
					    	image[i][j] = value - 1 + "";
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
	    pw.println(MaxRgb);
	    
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
	    JOptionPane.showMessageDialog(null, "Your Img Is Cryted With Success!", "The image has been encrypted", JOptionPane.INFORMATION_MESSAGE); 
	 }
	
	/**
	 * When Click Button save Image
	 * you need to select 2 images(principal && hidden) Before trying to save. 
	 */
	private void saveImage() {
	 java.io.File f = showFileDialog(false);
	 String name = f.getName(); 
	    
     if(sourceImage == null || sourceImage2 == null) {
       JOptionPane.showMessageDialog(this, "No Image has been embedded!", 
         "Nothing to save", JOptionPane.ERROR_MESSAGE); 
      } else {
    	  SavePath = f.toString(); 
      } 
	}

	// Reset to default
	private void resetInterface() { 
	    sourceImage = null;   
		sourceImage2 = null;
		SavePath = null;
		ColorButton.setEnabled(true);
	    GrayButton.setEnabled(true);
	    BlackWithButton.setEnabled(true);
	}
 
	public static void main(String arg[]) {
	   new CryptImage(); 
	}
}