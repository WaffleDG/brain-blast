import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JSeparator;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Dimension;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.io.File;


/**
 * This frame can be viewed in BrainBlast. This is a JPanel and is the screen which is going 
 * to be viewed. It has a private JPanel which can be accessed from the HomePanel and allows users to view existing saved flashcard sets. 
 * 
 *
 * <p>
 * Authors: Gregory Cohen and Riya Jonnala.
 * 
 * <p>
 * Version: 1.0, 11/21/2025.
 *
 * @author     Gregory Cohen and Riya Jonnala
 * @since      11/21/2025
 * @version    1.0
 * @see        JPanel
 */

public class FindSet extends JPanel implements ActionListener {
   /** Private list for set buttons */
   private ArrayList<JButton> setButtons;
   /** Private list of set names */
   private ArrayList<String> setNames;
   /** Private registry for loading sets */
   private SetRegistry registry;
   /** Private panel that holds set buttons */
   private JPanel panelOfSets;
   /** Private search field */
   private JTextField searchField;
   /** Wrapper panel for centering the grid in the scroll view */
   private JPanel gridWrapper;
   /** Scroll pane for the grid */
   private JScrollPane scrollPane;
   
   /** Grid layout column count for the catalog */
   private static final int SET_COLUMNS = 3;
   /** Grid gap size for the catalog */
   private static final int SET_GAP = 10;
   /** Size for each set button (square) */
   private int buttonSize;
   
   /**
    * FindSet constructor - builds the catalog view as a JScrollPane.
    */
   public FindSet() {
      // compute a button size so three columns fill most of the screen
      int availableW = MainFrame.WIDTH - 40;
      buttonSize = (availableW - (SET_COLUMNS - 1) * SET_GAP) / SET_COLUMNS;
      if (buttonSize < 140) {
         buttonSize = 140;
      }
      // prepare storage and load current set names
      setButtons = new ArrayList<JButton>();
      registry = new SetRegistry();
      setNames = registry.getSetNames();
      
      // apply shared panel styling
      UIStyle.stylePanel(this);
      
      // set the layout to be a vertical layout
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      
      // padding
      this.add(Box.createVerticalStrut(5));
      
      // title label at the top
      JLabel title = new JLabel("Find Set");
      UIStyle.styleTitle(title);
      title.setAlignmentX(CENTER_ALIGNMENT);
      this.add(title);
      this.add(Box.createVerticalStrut(6));
      this.add(buildSeparator());
      this.add(Box.createVerticalStrut(8));
      
      // search panel
      JPanel searchPanel = new JPanel();
      searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
      UIStyle.stylePanel(searchPanel);
      
      JButton backButton = new JButton("Back");
      backButton.setActionCommand("back");
      backButton.addActionListener(this);
      UIStyle.styleButton(backButton, 90, 32);
      backButton.setFocusable(false);
      backButton.setFocusPainted(false);
      
      // search label and text field
      JLabel searchLabel = new JLabel("Search:");
      UIStyle.styleLabel(searchLabel);
      searchField = new JTextField(20);
      searchField.setMaximumSize(searchField.getPreferredSize());
      searchField.setFont(UIStyle.BODY_FONT);
      searchField.setActionCommand("search");
      searchField.addActionListener(this);
      
      // search button to trigger filter
      JButton searchButton = new JButton("Go");
      searchButton.setActionCommand("search");
      searchButton.addActionListener(this);
      UIStyle.styleButton(searchButton, 60, 32);
      searchButton.setFocusable(false);
      searchButton.setFocusPainted(false);
      
      // group the search controls so the whole set can be centered
      JPanel searchGroup = new JPanel();
      searchGroup.setLayout(new BoxLayout(searchGroup, BoxLayout.X_AXIS));
      UIStyle.stylePanel(searchGroup);
      searchGroup.add(searchLabel);
      searchGroup.add(Box.createHorizontalStrut(5));
      searchGroup.add(searchField);
      searchGroup.add(Box.createHorizontalStrut(5));
      searchGroup.add(searchButton);
      
      searchPanel.add(Box.createHorizontalStrut(5));
      searchPanel.add(backButton);
      searchPanel.add(Box.createHorizontalGlue());
      searchPanel.add(searchGroup);
      searchPanel.add(Box.createHorizontalGlue());
      searchPanel.add(Box.createHorizontalStrut(5));
      
      // limit the height of the search panel
      searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchPanel.getPreferredSize().height));
      this.add(searchPanel);
      this.add(Box.createVerticalStrut(8));
      this.add(buildSeparator());
      this.add(Box.createVerticalStrut(10));
      
      // Create panel for sets
      panelOfSets = new JPanel();
      panelOfSets.setLayout(new GridLayout(0, SET_COLUMNS, SET_GAP, SET_GAP)); // automatic rows, fixed columns
      UIStyle.styleCardPanel(panelOfSets);
      
      // Wrapper keeps the grid from stretching to fill empty space
      gridWrapper = new JPanel();
      gridWrapper.setLayout(new BoxLayout(gridWrapper, BoxLayout.Y_AXIS));
      UIStyle.styleCardPanel(gridWrapper);
      JPanel gridRow = new JPanel();
      gridRow.setLayout(new BoxLayout(gridRow, BoxLayout.X_AXIS));
      UIStyle.styleCardPanel(gridRow);
      // horizontal glue keeps the grid centered
      gridRow.add(Box.createHorizontalGlue());
      gridRow.add(panelOfSets);
      gridRow.add(Box.createHorizontalGlue());
      gridRow.setAlignmentX(CENTER_ALIGNMENT);
      // keep the grid anchored to the top of the scroll view
      gridWrapper.add(gridRow);
      gridWrapper.add(Box.createVerticalGlue());
      
      // build initial buttons from existing set names
      rebuildButtons("");
      
      // Create a JScrollPane by passing in the JPanel as the view
      scrollPane = new JScrollPane(gridWrapper);
      scrollPane.setPreferredSize(new Dimension(MainFrame.WIDTH - 40, MainFrame.HEIGHT - 120));
      scrollPane.getViewport().setBackground(UIStyle.CARD_BG);
      scrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(UIStyle.SOFT_OUTLINE));
      
      // Customize scroll bar policies (defaults are AS_NEEDED)
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // can always scroll up and down
      scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // if needed, scroll right and left
      scrollPane.getVerticalScrollBar().setUnitIncrement(16);
      

      // add scroll pane
      this.add(scrollPane);
      this.add(Box.createVerticalStrut(5));
      
      // ensure the initial view starts at the top
      SwingUtilities.invokeLater(() -> {
         scrollPane.getVerticalScrollBar().setValue(0);
         scrollPane.getHorizontalScrollBar().setValue(0);
      });
      
      // ready to display
      this.setVisible(true);
   }


/*               find set
               search by name
     ________________________________
     | ____ ____ ____ ____ ____  || | //has all existing sets in it, can scroll to click one (5 in a row, 100 in a column)
     | |__| |__| |__| |__| |___| || |
     | ____ ____ ____ ____ ____  || |
     | |__| |__| |__| |__| |__|  || |
     | ____ ____ ____ ____ ____  || |
     | |__| |__| |__| |__| |__|  || |
     | ____ ____ ____ ____ ____  || |
     | |__| |__| |__| |__| |__|  || |
     | ____ ____ ____ ____ ____  || |
     | |__| |__| |__| |__| |__|  || |
     |______________________________|
     
*/

//find it on the page (all existing sets pop up in the grid with the title in the grid box
// (potential cover pic for each grid box later))
    //click on a set (each is a button)
    //open to see options of what you can do with the set (can edit, quiz (given key or given definition), review)
    //each is a button which then leads to a new panel

//button to click to access an existing set

    // OR

//search by name

    //not needed if using FlowLayout
    //counts number of sets based on number of saved names in the list of set names
   /** Override for actionPerformed */
   @Override
   public void actionPerformed(ActionEvent e) {
      String message = e.getActionCommand();
      
      if (message.equals("search")) {
         // filter the grid by the search field text
         rebuildButtons(searchField.getText());
      }
      else if (message.equals("back")) {
         // return to the home screen
         MainFrame.switchScreen("home");
      }
      else {
         String setName = message;
         // clicking a set will open a popout action menu
         showActionMenu(setName);
      }
   }
   
   /**
    * buildSeparator creates a thin divider for the layout.
    */
   private JSeparator buildSeparator() {
      JSeparator separator = new JSeparator();
      separator.setMaximumSize(new Dimension(MainFrame.WIDTH - 140, 2));
      separator.setForeground(UIStyle.SOFT_OUTLINE);
      return separator;
   }
   
   /**
    * rebuildButtons will refresh the grid based on the filter text.
    */
   private void rebuildButtons(String filterText) {
      // normalize the filter so matching is case-insensitive
      String filter = filterText.toLowerCase().trim();
      
      // clear current buttons and rebuild
      panelOfSets.removeAll();
      setButtons.clear();
      
      boolean anyAdded = false;
      
      for (int i = 0; i < setNames.size(); i++) {
         String setName = setNames.get(i);
         // skip names that do not contain the filter
         if (filter.length() > 0 && setName.toLowerCase().indexOf(filter) == -1) {
            continue;
         }
         
         // create a button for each matching set
         JButton setButton = new JButton();
         setButton.setActionCommand(setName);
         setButton.addActionListener(this);
         UIStyle.styleButton(setButton, buttonSize, buttonSize);
         setButton.setFocusable(false);
         setButton.setFocusPainted(false);
         setButton.setOpaque(true);
         
         // center and wrap the set name inside the square button
         setButton.setLayout(new GridBagLayout());
         CenteredWrapTextPane nameText = new CenteredWrapTextPane(setName, buttonSize - 24);
         nameText.setFont(UIStyle.BODY_FONT.deriveFont(16f));
         nameText.setForeground(UIStyle.ACCENT_TEXT);
         nameText.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
               // forward clicks on the text to the button
               setButton.doClick();
            }
         });
         GridBagConstraints gbc = new GridBagConstraints();
         gbc.gridx = 0;
         gbc.gridy = 0;
         gbc.anchor = GridBagConstraints.CENTER;
         setButton.add(nameText, gbc);
         
         // track and add the button to the grid
         setButtons.add(setButton);
         panelOfSets.add(setButton);
         anyAdded = true;
      }
      
      if (!anyAdded) {
         // show a message if nothing matches
         JLabel emptyLabel = new JLabel("No sets found.");
         UIStyle.styleLabel(emptyLabel);
         panelOfSets.add(emptyLabel);
      }
      
      // size the grid so the scroll pane can calculate the correct scroll range
      int totalItems = panelOfSets.getComponentCount();
      int rows = (int) Math.ceil(totalItems / (double) SET_COLUMNS);
      if (rows < 1) {
         rows = 1;
      }
      int prefW = SET_COLUMNS * buttonSize + (SET_COLUMNS - 1) * SET_GAP;
      int prefH = rows * buttonSize + (rows - 1) * SET_GAP;
      panelOfSets.setPreferredSize(new Dimension(prefW, prefH));
      panelOfSets.setMinimumSize(new Dimension(prefW, prefH));
      panelOfSets.setMaximumSize(new Dimension(prefW, prefH));
      
      // refresh the grid so changes appear
      panelOfSets.revalidate();
      panelOfSets.repaint();
      
      // jump to the top after filtering so results are visible
      if (scrollPane != null) {
         SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(0);
            scrollPane.getHorizontalScrollBar().setValue(0);
         });
      }
   }

   /**
    * showActionMenu opens a popout menu for the selected set.
    */
   private void showActionMenu(String setName) {
      // reverse order so the GUI shows learn -> cancel
      String[] options = {"Cancel", "Delete", "Edit", "Quiz", "Learn"};
      
      // Bringing up a dialog with options is so much fun!
      // we provide lots of input parameters :o
      int choice = UIStyle.showOptionDialog(
         this,
         "Choose an action for: " + setName,
         "Set Options",
         JOptionPane.DEFAULT_OPTION,
         options,
         options[4] // default to Learn
      );
      
      if (choice == 4) {
         // learn
         MainFrame.loadLearnSet(setName);
      }
      else if (choice == 3) {
         // quiz
         MainFrame.loadQuizSet(setName);
      }
      else if (choice == 2) {
         // edit
         MainFrame.loadEditSet(setName);
      }
      else if (choice == 1) {
         // delete
         Object[] deleteOptions = {"No", "Yes"};
         int confirm = UIStyle.showOptionDialog(
            this,
            "Delete the set \"" + setName + "\"?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            deleteOptions,
            deleteOptions[0]
         );
         
         if (confirm == 1) {
            // attempt to delete the file and refresh if successful
            File setFile = Paths.setFile(setName);
            if (setFile.exists() && setFile.delete()) {
               // refresh list after delete
               setNames = registry.getSetNames();
               rebuildButtons(searchField.getText());
            }
            else {
               // show an error so the user knows it failed
               UIStyle.showMessageDialog(this, "Could not delete the set.", "Delete Failed", JOptionPane.ERROR_MESSAGE);
            }
         }
      }
      // no action for cancel
   }

}
