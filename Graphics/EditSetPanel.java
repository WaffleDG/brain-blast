import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import javax.swing.JFrame;

/**
 * Screen for creating or editing new sets for Brain Blast! This will provide the user the ability to create a new set, entering as many keyword-definition pairs as they want!
 * K-D pairs will not be stored in a map; and instead will be stored in two lists for their ability to be shuffled. Again, this is an implementation of a JPanel.
 * Sets will be saved with each flashcard on  key <tab> definition
 * 
 * <p>
 * Authors: Gregory Cohen and Riya Jonnala.
 *
 * <p>
 * Version: 1.0, 11/21/2025.
 *
 * 
 * @author     Gregory Cohen and Riya Jonalla
 * @since      11/21/2025
 * @version    1.0
 * @see        JPanel
 */
public class EditSetPanel extends JPanel {
   /** Private variable to keep track of the file */
   private File setFile;
   
   /** Private variable for each of the definitions of the set */
   private ArrayList<String> definitionsList; 
   /** Private variable for each of the keys of the set */
   private ArrayList<String> keysList;
   
   /** Private static final variable for the directory */
   private static final String dir = "../Sets/";
   
   /**
    * Constructor without filePath for when the file is just created - standard name is Unnamed Set #
    */
   public EditSetPanel() {
      // call the other constructor with this file.
      this(newUnnamedFileName());
   }
   

   /**
    * Constructor which takes a file name, reads the file or creates one if not already existing, loads the set and the graphics from that.
    */
   public EditSetPanel(String fileName) {
      String filePath = dir + fileName + ".txt";
      loadFile(filePath);
      
      /*
               Edit Set '<>'
        |---------------------------|  
        | close    rename     save  |
        | ------------------------- |
        |   key        definition   |
            
            box             box     
            box             box
                    ... 
                Add another
      */
      
     // set the layout to be a vertical layout
     this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
     
     // padding
     this.add(Box.createVerticalStrut(5));
     
     ////////////////////////////////////////////// HEADER //////////////////////////////////////////////
     JPanel header = new JPanel();
     
     // horizontal layout for this one
     header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
     
     // creating two JButtons: one to close and one to save
     JButton closeButton = new JButton("close"); // TO DO: make an icon ************************************************
     JButton saveButton = new JButton("save"); // TO DO: make an icon **************************************************
     
     // creating the text field
     JTextField renameFileBox = new JTextField(20);
     renameFileBox.setMaximumSize(renameFileBox.getPreferredSize()); // setting the max size to the preferred
     renameFileBox.setText(fileName);
     
     JLabel renameLabel = new JLabel("Set Name:");
     
     // fill the header panel
     header.add(closeButton);
     // space
     header.add(Box.createHorizontalGlue());
     header.add(renameLabel);
     header.add(renameFileBox);
     // space
     header.add(Box.createHorizontalGlue());
     header.add(saveButton);
     
     header.setVisible(true);
     
     ////////////////////////////////////////////// BODY //////////////////////////////////////////////
     
     // to do lol
     
     ////////////////////////////////////////////// COMPILE //////////////////////////////////////////////
     
     this.add(header);
     this.add(new JSeparator(SwingConstants.HORIZONTAL));
     
     this.setVisible(true);
   }
  
   /**
    * newUnnamedFileName finds the next unique file name with the start "Unnamed Set " as a txt.
    */
   public static String newUnnamedFileName() {
      // finding a fileName that will work -- checks if 1 exists, if not, checks if 2 exists, and so on
      // keeping track of counter and file name
      int counter = 1;
      String fileName = "Unnamed Set "; // trailing space
      
      // setting first file name
      File newFile = new File(dir + fileName + counter + ".txt");
      
      while (newFile.exists()) { // if and while it already exists,
         // increment counter
         counter++;
         // test with new file
         newFile = new File(dir + fileName + counter + ".txt");
      }
      
      return (fileName + counter + ".txt");
   }
  
   /**
    * loadFile takes a filePath and creates or gets the corresponding file.
    */
   private void loadFile(String filePath) {
      // create the File object
      setFile = new File(filePath);
      
      // create the ArrayList objects
      keysList = new ArrayList<String>();
      definitionsList = new ArrayList<String>();
      
      // create the actual file if not created already
      try {
         if (setFile.createNewFile()) { // this will only return true if the file is created.
            // return; nothing else to load
            return;  
         }
         else { // if the file already exists we have to read it
            // create a new scanner to read file
            Scanner fileReader = new Scanner(setFile);
            
            // while there is another line to read
            while (fileReader.hasNextLine()) {
               // get the next line and split it into parts separated by a tab "\t": inputted as "\\t" so .split can read it
               String[] parts = fileReader.nextLine().split("\\t");
               
               // the first object will be the key, and the second object will be the definition.
               keysList.add(parts[0]);
               definitionsList.add(parts[1]);
            }
         }
      }
      catch (IOException ioe) {
         ioe.printStackTrace();
         
         // do not load program if there's an error with the file
         System.exit(0);
      }
   }
   
   public static void main(String[] args) {
      JFrame testFrame = new JFrame("BrainBlast - testing debug");
      EditSetPanel esp = new EditSetPanel("Names to Remember");
      
      testFrame.setSize(800, 600);
      testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      testFrame.add(esp);
      
      testFrame.setVisible(true);
      
   }
}  