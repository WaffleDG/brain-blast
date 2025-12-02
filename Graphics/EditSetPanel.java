import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JSeparator;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintStream;
import java.io.IOException;


import javax.swing.JFrame; // testing - to remove

/**
 * Screen for creating or editing new sets for Brain Blast! This will provide the user the ability to create a new set, entering as many keyword-definition pairs as they want!
 * K-D pairs will not be stored in a map; and instead will be stored in two lists for their ability to be shuffled. Again, this is an instance of a JPanel. It implements
 * ActionListener to support button use.
 * Sets will be saved with each flashcard by  key <tab> definition
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
public class EditSetPanel extends JPanel implements ActionListener {
   /** Private variable to keep track of the file */
   private File setFile;
   
   /** Private variable for each of the definitions of the set */
   private ArrayList<String> definitionsList; 
   /** Private variable for each of the keys of the set */
   private ArrayList<String> keysList;
   
   /** Private variable for the keyPanel */
   private JPanel keyPanel;
   /** Private variable for the defPanel */
   private JPanel defPanel;
   
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
      header.add(Box.createHorizontalStrut(5));
      header.add(closeButton);
      // space
      header.add(Box.createHorizontalGlue());
      header.add(renameLabel);
      header.add(renameFileBox);
      // space
      header.add(Box.createHorizontalGlue());
      header.add(saveButton);
      header.add(Box.createHorizontalStrut(5));
      
      // limit the height of the header.
      header.setMaximumSize(new Dimension(Integer.MAX_VALUE, header.getPreferredSize().height));
      
      ////////////////////////////////////////////// BODY //////////////////////////////////////////////
     
      // create a panel to hold the key and definition panels
      JPanel bodyPanel = new JPanel();
      bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.X_AXIS));
      
      // create panels for the keys and for the definitions
      keyPanel = new JPanel();
      keyPanel.setLayout(new BoxLayout(keyPanel, BoxLayout.Y_AXIS));
      defPanel = new JPanel();
      defPanel.setLayout(new BoxLayout(defPanel, BoxLayout.Y_AXIS));
      
      // label the panels
      JLabel keyLabel = new JLabel("KEY");
      keyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      JLabel defLabel = new JLabel("DEFINITION");
      defLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      
      keyPanel.add(keyLabel);
      defPanel.add(defLabel);
     
      // for each key-value pair (standard for to allow for indexing through both arrayLists),
      for (int i = 0; i < keysList.size(); i++) {
         addPair(keysList.get(i), definitionsList.get(i));
        
      }
     
      // compile bodyPanel
      bodyPanel.add(Box.createHorizontalGlue());
      bodyPanel.add(keyPanel);
      bodyPanel.add(Box.createHorizontalGlue());
      bodyPanel.add(new JSeparator(SwingConstants.VERTICAL));
      bodyPanel.add(Box.createHorizontalGlue());
      bodyPanel.add(defPanel);
      bodyPanel.add(Box.createHorizontalGlue());
      
      // create a scrollpane for body panel
      JScrollPane bodyScroll = new JScrollPane(bodyPanel);
      bodyScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      
      ////////////////////////////////////////////// FOOTER //////////////////////////////////////////////
      
      JPanel footer = new JPanel();
      footer.setLayout(new BoxLayout(footer, BoxLayout.X_AXIS));
      
      // create a button to add another
      JButton addButton = new JButton("Add Another");
      addButton.setActionCommand("add");
      addButton.addActionListener(this);
      
      footer.add(addButton);
      
      ////////////////////////////////////////////// COMPILE //////////////////////////////////////////////
      
      this.add(Box.createVerticalStrut(5));
      this.add(header);
      this.add(Box.createVerticalStrut(5));

      this.add(bodyScroll);
      
      this.add(Box.createVerticalStrut(5));
      this.add(footer);
      this.add(Box.createVerticalStrut(5));
     
      this.setVisible(true);
   }
   
   
   
   /**
    * This method adds new text areas to the key and definition, and should be triggered solely by the
    * constructor and the add another button action.
    */
   private void addPair(String key, String definition) {
      // line separator
      keyPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
      defPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
      
      // create new JTextAreas for the key and value
      JTextArea keyArea = new JTextArea(10, 20);
      keyArea.setMaximumSize(keyArea.getPreferredSize());
      keyArea.setLineWrap(true);
      keyArea.setWrapStyleWord(true);
      keyArea.setText(key);
      
      JTextArea defArea = new JTextArea(10, 20);
      defArea.setMaximumSize(defArea.getPreferredSize());
      defArea.setLineWrap(true);
      defArea.setWrapStyleWord(true);
      defArea.setText(definition);
      
      // because JTextAreas do not allow scrolling by default, we have to manually override it. Yay.
      JScrollPane keySP = new JScrollPane(keyArea);
      JScrollPane defSP = new JScrollPane(defArea);
      
      keySP.setMaximumSize(keySP.getPreferredSize());
      defSP.setMaximumSize(defSP.getPreferredSize());
      
      // padding
      keyPanel.add(Box.createVerticalStrut(5));
      defPanel.add(Box.createVerticalStrut(5));
      
      // add each scroll pane to their respective panels.
      keyPanel.add(keySP);
      defPanel.add(defSP);
      
      // padding
      keyPanel.add(Box.createVerticalStrut(5));
      defPanel.add(Box.createVerticalStrut(5));
      
      // revalidate/repaint the panels.
      keyPanel.revalidate();
      defPanel.revalidate();
      keyPanel.repaint();
      defPanel.repaint();
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
      
      return (fileName + counter);
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
            // add a standard "key" "def" to the lists.
            keysList.add("Key");
            definitionsList.add("Definition");
            
            // save to file
            PrintStream pr = new PrintStream(filePath);
            pr.println("Key\tDefinition");
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
   
   
   
   /** Override for actionPerformed */
   @Override
   public void actionPerformed(ActionEvent e) {
      String message = e.getActionCommand();
      
      if (message.equals("add")) {
         this.addPair("Key", "Definition");
      }
      else if (message.equals("save")) {
         // TO DO: Make a save method!
      }
      else if (message.equals("close")) {
         // TO DO: make panel to do are you sure?
         MainFrame.switchScreen("home");
      }
   }
   
   
   
   // this is for testing, to remove
   public static void main(String[] args) {
      JFrame testFrame = new JFrame("BrainBlast - testing debug");
      EditSetPanel esp = new EditSetPanel("Names to Remember");
      
      testFrame.setSize(800, 600);
      testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      testFrame.add(esp);
      
      testFrame.setVisible(true);
      
   }
}  