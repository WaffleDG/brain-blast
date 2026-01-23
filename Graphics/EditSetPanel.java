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
import java.awt.BorderLayout;
import java.awt.Font;
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
   
   //Constructor without filePath for when the file is just created - standard name is Unnamed Set #
   public EditSetPanel() {
      // call the other constructor with this file.
      this(newUnexistingFileName());
   }

   /**
    * Constructor which takes a file name, reads the file or creates one if not already existing,
    * loads the set and the graphics from that.
    */
   public EditSetPanel(String fileName) {
      // build a path for the requested set file
      String filePath = dir + fileName + ".txt";
      // load existing data or create a new file
      SetRegistry registry = new SetRegistry();
      registry.loadFile(filePath);
      setFile = registry.getSetFile();
      initKeys = registry.getKeys();
      initDefs = registry.getDefs();
      
      // create storage for the editable text areas
      keysList = new ArrayList<JTextArea>();
      definitionsList = new ArrayList<JTextArea>();
      
      // apply shared panel styling
      UIStyle.stylePanel(this);
      
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
      this.setMaximumSize(new Dimension(MainFrame.WIDTH, MainFrame.HEIGHT));
     
      // padding
      this.add(Box.createVerticalStrut(5));
      
      // title label to match other screens
      JLabel title = new JLabel("Edit Set");
      UIStyle.styleTitle(title);
      title.setAlignmentX(Component.CENTER_ALIGNMENT);
      this.add(title);
      this.add(Box.createVerticalStrut(5));
     
      ////////////////////////////////////////////// HEADER //////////////////////////////////////////////

      JPanel header = new JPanel();
      UIStyle.stylePanel(header);
     
      // horizontal layout for this one
      header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
     
      // creating two JButtons: one to close and one to save
      JButton closeButton = new JButton("close");
      JButton saveButton = new JButton("save");
      UIStyle.styleButton(closeButton, 90, 32);
      UIStyle.styleButton(saveButton, 90, 32);
      closeButton.setFocusable(false);
      closeButton.setFocusPainted(false);
      saveButton.setFocusable(false);
      saveButton.setFocusPainted(false);
      
      // link them to the action listener
      closeButton.setActionCommand("close");
      closeButton.addActionListener(this);
      saveButton.setActionCommand("save");
      saveButton.addActionListener(this);
      
      // creating the text field
      renameFileBox = new JTextField(20);
      renameFileBox.setMaximumSize(renameFileBox.getPreferredSize()); // setting the max size to the preferred
      renameFileBox.setFont(UIStyle.BODY_FONT);
      renameFileBox.setText(fileName);
     
      JLabel renameLabel = new JLabel("Set Name:");
      UIStyle.styleLabel(renameLabel);
     
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
      
      /////////////////////////////////////////// BODY //////////////////////////////////////////////
     
      // create a panel to hold the key and definition panels
      JPanel bodyPanel = new JPanel();
      bodyPanel.setLayout(new BorderLayout());
      UIStyle.stylePanel(bodyPanel);
      bodyPanel.setAlignmentY(Component.TOP_ALIGNMENT);
      
      // columns panel stays at the top of the scroll view
      JPanel columnsPanel = new JPanel();
      columnsPanel.setLayout(new BoxLayout(columnsPanel, BoxLayout.X_AXIS));
      UIStyle.stylePanel(columnsPanel);
      
      // create panels for the keys and definitions
      keyPanel = new JPanel();
      keyPanel.setLayout(new BoxLayout(keyPanel, BoxLayout.Y_AXIS));
      defPanel = new JPanel();
      defPanel.setLayout(new BoxLayout(defPanel, BoxLayout.Y_AXIS));
      UIStyle.styleCardPanel(keyPanel);
      UIStyle.styleCardPanel(defPanel);
      
      // keep the columns the same width so the divider stays centered
      // let height grow naturally for scrolling
      int colW = 320;
      keyPanel.setMinimumSize(new Dimension(colW, 0));
      defPanel.setMinimumSize(new Dimension(colW, 0));
      keyPanel.setMaximumSize(new Dimension(colW, Integer.MAX_VALUE));
      defPanel.setMaximumSize(new Dimension(colW, Integer.MAX_VALUE));
      keyPanel.setAlignmentY(Component.TOP_ALIGNMENT);
      defPanel.setAlignmentY(Component.TOP_ALIGNMENT);
      
      // for each key-value pair (standard for to allow for indexing through both arrayLists),
      for (int i = 0; i < initKeys.size(); i++) {
         addPairData(initKeys.get(i), initDefs.get(i));
      }      
      // build the panels from the stored text areas
      rebuildPanels();
      
      // compile columnsPanel
      columnsPanel.add(Box.createHorizontalGlue());
      columnsPanel.add(keyPanel);
      columnsPanel.add(Box.createHorizontalStrut(10));
      JSeparator midSep = new JSeparator(SwingConstants.VERTICAL);
      midSep.setMaximumSize(new Dimension(2, Integer.MAX_VALUE));
      columnsPanel.add(midSep);
      columnsPanel.add(Box.createHorizontalStrut(10));
      columnsPanel.add(defPanel);
      columnsPanel.add(Box.createHorizontalGlue());
      
      // let the columns fill the viewport so the divider spans the full height
      bodyPanel.add(columnsPanel, BorderLayout.CENTER);

      // create a scrollPane for body panel
      JScrollPane bodyScroll = new JScrollPane(bodyPanel);
      bodyScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      bodyScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      bodyScroll.getViewport().setBackground(UIStyle.BG);
      bodyScroll.setBorder(javax.swing.BorderFactory.createLineBorder(UIStyle.SOFT_OUTLINE));
      bodyScroll.setAlignmentX(Component.CENTER_ALIGNMENT);
      
      ////////////////////////////////////////////// FOOTER //////////////////////////////////////////////
      
      JPanel footer = new JPanel();
      footer.setLayout(new BoxLayout(footer, BoxLayout.X_AXIS));
      UIStyle.stylePanel(footer);
      footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, header.getPreferredSize().height));
      
      // create a button to add another
      JButton addButton = new JButton("Add Another");
      addButton.setActionCommand("add");
      addButton.addActionListener(this);
      UIStyle.styleButton(addButton, 140, 32);
      addButton.setFocusable(false);
      addButton.setFocusPainted(false);
      
      // create a button to clear empty rows
      JButton clearButton = new JButton("Clear Empty");
      clearButton.setActionCommand("clear");
      clearButton.addActionListener(this);
      UIStyle.styleButton(clearButton, 140, 32);
      clearButton.setFocusable(false);
      clearButton.setFocusPainted(false);
      
      footer.add(Box.createHorizontalGlue());
      footer.add(addButton);
      footer.add(Box.createHorizontalStrut(10));
      footer.add(clearButton);
      footer.add(Box.createHorizontalGlue());
      
      ////////////////////////////////////////////// COMPILE //////////////////////////////////////////////
      
      this.add(Box.createVerticalStrut(5));
      this.add(header);
      this.add(Box.createVerticalStrut(5));

      this.add(bodyScroll);
      
      this.add(Box.createVerticalStrut(5));
      this.add(footer);
      this.add(Box.createVerticalStrut(5));
     
      // show the panel once initialized
      this.setVisible(true);
   }


   /**
    * This method adds new text areas to the key and definition, and should be triggered solely by the
    * constructor and the add another button action.
    */
   private void addPair(String key, String definition) {
      // add the data and then rebuild the panels to show it
      addPairData(key, definition);
      rebuildPanels();
   }
   
   // addPairData creates and stores a new pair without touching the panel layout.
   private void addPairData(String key, String definition) {
      // create new JTextAreas for the key and value
      JTextArea keyArea = new JTextArea(4, 20);
      // let the text area grow inside its scroll pane
      keyArea.setLineWrap(true);
      keyArea.setWrapStyleWord(true);
      keyArea.setFont(UIStyle.BODY_FONT);
      keyArea.setText(key);
      
      JTextArea defArea = new JTextArea(4, 20);
      // let the text area grow inside its scroll pane
      defArea.setLineWrap(true);
      defArea.setWrapStyleWord(true);
      defArea.setFont(UIStyle.BODY_FONT);
      defArea.setText(definition);
      
      // add these areas to the lists
      keysList.add(keyArea);
      definitionsList.add(defArea);
   }
   
   // rebuildPanels redraws the key/definition columns based on the saved pairs.
   private void rebuildPanels() {   
      // clear existing components
      keyPanel.removeAll();
      defPanel.removeAll();
      
      // add headers inside the scrollable area so they move with content
      JLabel keyLabel = new JLabel("KEY");
      JLabel defLabel = new JLabel("DEFINITION");
      UIStyle.styleLabel(keyLabel);
      UIStyle.styleLabel(defLabel);
      keyLabel.setFont(UIStyle.BODY_FONT.deriveFont(Font.BOLD, 18f));
      defLabel.setFont(UIStyle.BODY_FONT.deriveFont(Font.BOLD, 18f));
      keyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      defLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      keyPanel.add(Box.createVerticalStrut(5));
      keyPanel.add(keyLabel);
      defPanel.add(Box.createVerticalStrut(5));
      defPanel.add(defLabel);
      
      // add each pair
      for (int i = 0; i < keysList.size(); i++) {
         addPairComponents(keysList.get(i), definitionsList.get(i));
      }
      
      // glue keeps content pinned to the top while allowing the panel to stretch
      keyPanel.add(Box.createVerticalGlue());
      defPanel.add(Box.createVerticalGlue());
      
      // lock in the column widths while keeping the computed heights for scrolling
      int colW = keyPanel.getMinimumSize().width;
      Dimension keySize = keyPanel.getLayout().preferredLayoutSize(keyPanel);
      Dimension defSize = defPanel.getLayout().preferredLayoutSize(defPanel);
      if (colW <= 0) {
         colW = keySize.width;
      }
      keyPanel.setPreferredSize(new Dimension(colW, keySize.height));
      defPanel.setPreferredSize(new Dimension(colW, defSize.height));
      
      // revalidate/repaint the panels
      keyPanel.revalidate();
      defPanel.revalidate();
      keyPanel.repaint();
      defPanel.repaint();
   }
   
   // addPairComponents adds the visual components for a single pair
   private void addPairComponents(JTextArea keyArea, JTextArea defArea) {   
      // padding
      keyPanel.add(Box.createVerticalStrut(5));
      defPanel.add(Box.createVerticalStrut(5));
   
      // line separator
      JSeparator keySep = new JSeparator(SwingConstants.HORIZONTAL);
      JSeparator defSep = new JSeparator(SwingConstants.HORIZONTAL);

      // keep separators from stretching and pushing content down
      keySep.setMaximumSize(new Dimension(Integer.MAX_VALUE, keySep.getPreferredSize().height));
      defSep.setMaximumSize(new Dimension(Integer.MAX_VALUE, defSep.getPreferredSize().height));
      keyPanel.add(keySep);
      defPanel.add(defSep);
      
      // because JTextAreas do not allow scrolling by default, we have to manually override it. Yay.
      JScrollPane keySP = new JScrollPane(keyArea);
      JScrollPane defSP = new JScrollPane(defArea);
      keySP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      defSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      keySP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      defSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      
      // give each row a consistent height so rows don't collapse
      Dimension rowSize = new Dimension(340, 110);
      keySP.setPreferredSize(rowSize);
      defSP.setPreferredSize(rowSize);
      keySP.setMinimumSize(rowSize);
      defSP.setMinimumSize(rowSize);
      keySP.setMaximumSize(rowSize);
      defSP.setMaximumSize(rowSize);
      keySP.getViewport().setBackground(UIStyle.CARD_BG);
      defSP.getViewport().setBackground(UIStyle.CARD_BG);
      
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

   // saveFile does just that! Save to file. It will handle renaming and writing to file.
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
      
      // if the file has been renamed (names !=), and already exists
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
         
         // open the file for writing
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
         
         // close the output stream to flush data
         pr.close();
      }
      catch (FileNotFoundException fnfe) {
         // report errors so they are visible during testing
         fnfe.printStackTrace();
      }
      
   }


   // This is an overload for the newUnexistingFileName which takes nothing. Default: Unnamed Set
   public static String newUnexistingFileName() {
      // default base name for new sets
      return newUnexistingFileName("Unnamed Set");
   }
   
   // newUnexistingFileName finds the next unique file name with the start "baseFileName" as a txt
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
      
      // return the first unused base name
      return (fileName + counter);
   }

   
   /** Override for actionPerformed */
   @Override
   public void actionPerformed(ActionEvent e) {
      String message = e.getActionCommand();
      
      if (message.equals("add")) {
         // add a fresh pair of boxes
         this.addPair("Key", "Definition");
      }
      else if (message.equals("clear")) {
         // remove empty rows to keep things tidy
         clearEmptyRows();
      }
      else if (message.equals("save")) {
         // save and notify the user
         this.saveFile();
         UIStyle.showMessageDialog(this, "Changes Saved!", "Saved", javax.swing.JOptionPane.INFORMATION_MESSAGE);
      }
      else if (message.equals("close")) {
         // TO DO: make panel to do are you sure?
         MainFrame.switchScreen("home");
      }
   }
   
   // clearEmptyRows removes any pairs that have no key and no definition.
   private void clearEmptyRows() {
      for (int i = keysList.size() - 1; i >= 0; i--) {
         String key = keysList.get(i).getText().trim();
         String def = definitionsList.get(i).getText().trim();
         
         if (key.length() == 0 && def.length() == 0) {
            // remove pairs that are completely empty
            keysList.remove(i);
            definitionsList.remove(i);
         }
      }
      
      if (keysList.size() == 0) {
         // keep at least one row available
         addPairData("Key", "Definition");
      }
      
      // rebuild the UI to reflect removals
      rebuildPanels();
   }
   
}
