import java.util.ArrayList;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.PrintStream;
import java.io.FileNotFoundException;

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
 * Version: 2.0, 12/11/2025.
 *
 * 
 * @author     Gregory Cohen and Riya Jonalla
 * @since      12/11/2025
 * @version    2.0
 * @see        JPanel
 */
public class EditSetPanel extends JPanel implements ActionListener {
   /** Private variable to keep track of the file */
   private File setFile;
   
   /** Private variable for each of the definitions text areas of the set */
   private ArrayList<JTextArea> definitionsList; 
   /** Private variable for each of the key text areas of the set */
   private ArrayList<JTextArea> keysList;
   
   /** Private variable to keep track of the rename text field*/
   private JTextField renameFileBox;
   
   /** Private variable to keep track of the initial keys */
   private ArrayList<String> initKeys;
   /** Private variable to keep track of the initial defs */
   private ArrayList<String> initDefs;
   /** Private variable for the keyPanel */
   private JPanel keyPanel;
   /** Private variable for the defPanel */
   private JPanel defPanel;
   
   /** Private static final variable for the directory */
   private static final String dir = Paths.SETS_DIR + "/";
   
   /**
    * Constructor without filePath for when the file is just created - standard name is Unnamed Set #
    */
   public EditSetPanel() {
      // call the other constructor with this file.
      this(newUnexistingFileName());
   }
   


   /**
    * Constructor which takes a file name, reads the file or creates one if not already existing, loads the set and the graphics from that.
    */
   public EditSetPanel(String fileName) {
      String filePath = dir + fileName + ".txt";
      SetRegistry registry = new SetRegistry();
      registry.loadFile(filePath);
      setFile = registry.getSetFile();
      initKeys = registry.getKeys();
      initDefs = registry.getDefs();
      
      keysList = new ArrayList<JTextArea>();
      definitionsList = new ArrayList<JTextArea>();
      
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
      this.setMaximumSize(new Dimension(MainFrame.HEIGHT, MainFrame.WIDTH));
     
      // padding
      this.add(Box.createVerticalStrut(5));
     
      ////////////////////////////////////////////// HEADER //////////////////////////////////////////////
      JPanel header = new JPanel();
     
      // horizontal layout for this one
      header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
     
      // creating two JButtons: one to close and one to save
      JButton closeButton = new JButton("close"); // TO DO: make an icon ************************************************
      JButton saveButton = new JButton("save"); // TO DO: make an icon **************************************************
      
      // link them to the action listener
      closeButton.setActionCommand("close");
      closeButton.addActionListener(this);
      saveButton.setActionCommand("save");
      saveButton.addActionListener(this);
      
      // creating the text field
      renameFileBox = new JTextField(20);
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
      
      // create panels for the keys and definitions
      keyPanel = new JPanel();
      keyPanel.setLayout(new BoxLayout(keyPanel, BoxLayout.Y_AXIS));
      defPanel = new JPanel();
      defPanel.setLayout(new BoxLayout(defPanel, BoxLayout.Y_AXIS));
      
      // padding
      keyPanel.add(Box.createVerticalStrut(5));
      defPanel.add(Box.createVerticalStrut(5));
      
      // label the panels
      JLabel keyLabel = new JLabel("KEY");
      keyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      JLabel defLabel = new JLabel("DEFINITION");
      defLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      keyPanel.add(keyLabel);
      defPanel.add(defLabel);
      
      // ADDING A BOTTOM GLUE! This glue will stay at the bottom using the position function
      // of the add method. add(element, position); where position is the .getComponent - 1
      keyPanel.add(Box.createVerticalGlue());
      defPanel.add(Box.createVerticalGlue());      
      
      // for each key-value pair (standard for to allow for indexing through both arrayLists),
      for (int i = 0; i < initKeys.size(); i++) {
         addPairData(initKeys.get(i), initDefs.get(i));
      }      
      rebuildPanels();
      
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
      
      // create a button to clear empty rows
      JButton clearButton = new JButton("Clear Empty");
      clearButton.setActionCommand("clear");
      clearButton.addActionListener(this);
      
      footer.add(addButton);
      footer.add(Box.createHorizontalStrut(10));
      footer.add(clearButton);
      
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
      addPairData(key, definition);
      rebuildPanels();
   }
   
   /**
    * addPairData creates and stores a new pair without touching the panel layout.
    */
   private void addPairData(String key, String definition) {
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
      
      // add these areas to the lists
      keysList.add(keyArea);
      definitionsList.add(defArea);
   }
   
   /**
    * rebuildPanels redraws the key/definition columns based on the saved pairs.
    */
   private void rebuildPanels() {   
      // clear existing components
      keyPanel.removeAll();
      defPanel.removeAll();
      
      // re-add labels
      JLabel keyLabel = new JLabel("KEY");
      keyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      JLabel defLabel = new JLabel("DEFINITION");
      defLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      keyPanel.add(keyLabel);
      defPanel.add(defLabel);
      
      // add bottom glue
      keyPanel.add(Box.createVerticalGlue());
      defPanel.add(Box.createVerticalGlue());
      
      // add each pair
      for (int i = 0; i < keysList.size(); i++) {
         addPairComponents(keysList.get(i), definitionsList.get(i), i);
      }
      
      // revalidate/repaint the panels.
      keyPanel.revalidate();
      defPanel.revalidate();
      keyPanel.repaint();
      defPanel.repaint();
   }
   
   /**
    * addPairComponents adds the visual components for a single pair.
    */
   private void addPairComponents(JTextArea keyArea, JTextArea defArea, int index) {   
      // get the position so i don't have to use the same code many times
      int position = keyPanel.getComponentCount() - 1;
      
      // padding
      keyPanel.add(Box.createVerticalStrut(5), position);
      defPanel.add(Box.createVerticalStrut(5), position);
      position++;
   
      // line separator
      keyPanel.add(new JSeparator(SwingConstants.HORIZONTAL), position);
      defPanel.add(new JSeparator(SwingConstants.HORIZONTAL), position);
      position++; // keep track
      
      // because JTextAreas do not allow scrolling by default, we have to manually override it. Yay.
      JScrollPane keySP = new JScrollPane(keyArea);
      JScrollPane defSP = new JScrollPane(defArea);
      
      keySP.setMaximumSize(keySP.getPreferredSize());
      defSP.setMaximumSize(defSP.getPreferredSize());
      
      // padding
      keyPanel.add(Box.createVerticalStrut(5), position);
      defPanel.add(Box.createVerticalStrut(5), position);
      position++;
      
      // add each scroll pane to their respective panels.
      keyPanel.add(keySP, position);
      defPanel.add(defSP, position);
      position++;
      
      // padding
      keyPanel.add(Box.createVerticalStrut(5), position);
      defPanel.add(Box.createVerticalStrut(5), position);
      
      // add matching spacing so rows stay aligned
      keyPanel.add(Box.createVerticalStrut(5), position);
      defPanel.add(Box.createVerticalStrut(5), position);
      
      // revalidate/repaint the panels.
      keyPanel.revalidate();
      defPanel.revalidate();
      keyPanel.repaint();
      defPanel.repaint();
   }
   
   
   /**
    * saveFile does just that! Save to file. It will handle renaming and writing to file.
    */
   public void saveFile() {
      // sanitize the renameFileTextBox
      String renameFileText = renameFileBox.getText();
      
      // i'm just going to make it alphanumeric because that seems good
      for (int i = renameFileText.length() - 1; i >= 0 ; i--) {
         char thisChar = renameFileText.charAt(i);
         
         // if it's not alphanumeric,
         if (!Character.isLetterOrDigit(thisChar) && thisChar != ' ') {
            renameFileText = renameFileText.substring(0, i) + renameFileText.substring(i + 1); // remove it
         }
      }
   
      // rename file (if applicable): get the file that corresponds to the text saved in the renameFileBox
      File renamedFile = new File(dir + renameFileText + ".txt");
      
      // if the file has been renamed (names !=), and already exists,
      if (!(renamedFile.getName().equals(setFile.getName())) && renamedFile.exists()) {
         // we need to find the file that doesn't.
         // luckily, I made a method for that!
         renamedFile = new File(dir + newUnexistingFileName(renameFileText) + ".txt");
      }
   
      // Create a new printStream to write to the file starting at the beginning and 
      // overwriting the text already there.
      // as with all things with files, we must try-catch exceptions
      try {
         // first, rename the file.
         if (setFile.renameTo(renamedFile)) { 
            // we'll make the set file = to the renamed one to ensure we are writing correctly.
            setFile = renamedFile;
         }
         
         // update the rename box to the actual file name
         String savedName = setFile.getName();
         if (savedName.endsWith(".txt")) {
            savedName = savedName.substring(0, savedName.length() - 4);
         }
         renameFileBox.setText(savedName);
         
         PrintStream pr = new PrintStream(setFile);
         
         // for each key-def text area pair
         for (int i = 0; i < keysList.size(); i++) {
            // get sanitized input
            // get the key as a string WITHOUT TABS
            String key = keysList.get(i).getText().replace("\t", " ");
            // get the definition as a string w/o \t
            String def = definitionsList.get(i).getText().replace("\t", " ");
            
            // if there is nothing in the key or definition, we don't have to track it.
            if (key.length() == 0 && def.length() == 0) {
               continue;
            }
            
            // and print it!
            pr.println(key + "\t" + def);
         }
         
         pr.close();
      }
      catch (FileNotFoundException fnfe) {
         fnfe.printStackTrace();
      }
      
   }
    
   
   
   /** 
    * this is an overload for the newUnexistingFileName which takes nothing. Default: Unnamed Set
    */
   public static String newUnexistingFileName() {
      return newUnexistingFileName("Unnamed Set");
   }
   
   /**
    * newUnexistingFileName finds the next unique file name with the start "baseFileName" as a txt.
    */
   public static String newUnexistingFileName(String baseFileName) {
      // finding a fileName that will work -- checks if 1 exists, if not, checks if 2 exists, and so on
      // keeping track of counter and file name
      int counter = 1;
      String fileName = baseFileName + " "; // trailing space
      
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
  
  
  
   
   
   
   /** Override for actionPerformed */
   @Override
   public void actionPerformed(ActionEvent e) {
      String message = e.getActionCommand();
      
      if (message.equals("add")) {
         this.addPair("Key", "Definition");
      }
      else if (message.equals("clear")) {
         clearEmptyRows();
      }
      else if (message.equals("save")) {
         this.saveFile();
      }
      else if (message.equals("close")) {
         // TO DO: make panel to do are you sure?
         MainFrame.switchScreen("home");
      }
   }
   
   /**
    * clearEmptyRows removes any pairs that have no key and no definition.
    */
   private void clearEmptyRows() {
      for (int i = keysList.size() - 1; i >= 0; i--) {
         String key = keysList.get(i).getText().trim();
         String def = definitionsList.get(i).getText().trim();
         
         if (key.length() == 0 && def.length() == 0) {
            keysList.remove(i);
            definitionsList.remove(i);
         }
      }
      
      if (keysList.size() == 0) {
         addPairData("Key", "Definition");
      }
      
      rebuildPanels();
   }
   
   
   
   // (test main removed) central startup is MainFrame.main
}  
