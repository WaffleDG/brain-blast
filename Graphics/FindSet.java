import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
   /** Private panel that holds set buttons */
   private JPanel panelOfSets;
   /** Private search field */
   private JTextField searchField;
   
   /**
    * FindSet constructor - builds the catalog view as a JScrollPane.
    */
   public FindSet() {
      setButtons = new ArrayList<JButton>();
      setNames = SetRegistry.getSetNames();
      
      // set the layout to be a vertical layout
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      
      // padding
      this.add(Box.createVerticalStrut(5));
      
      // title label at the top
      JLabel title = new JLabel("Find Set");
      title.setAlignmentX(CENTER_ALIGNMENT);
      this.add(title);
      
      // search panel
      JPanel searchPanel = new JPanel();
      searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
      
      JButton backButton = new JButton("Back");
      backButton.setActionCommand("back");
      backButton.addActionListener(this);
      
      // search label and text field
      JLabel searchLabel = new JLabel("Search:");
      searchField = new JTextField(20);
      searchField.setMaximumSize(searchField.getPreferredSize());
      searchField.setActionCommand("search");
      searchField.addActionListener(this);
      
      // search button to trigger filter
      JButton searchButton = new JButton("Go");
      searchButton.setActionCommand("search");
      searchButton.addActionListener(this);
      
      searchPanel.add(Box.createHorizontalStrut(5));
      searchPanel.add(backButton);
      searchPanel.add(Box.createHorizontalStrut(10));
      searchPanel.add(searchLabel);
      searchPanel.add(Box.createHorizontalStrut(5));
      searchPanel.add(searchField);
      searchPanel.add(Box.createHorizontalStrut(5));
      searchPanel.add(searchButton);
      searchPanel.add(Box.createHorizontalStrut(5));
      
      // limit the height of the search panel
      searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchPanel.getPreferredSize().height));
      this.add(searchPanel);
      
      // Create panel for sets
      panelOfSets = new JPanel();
      panelOfSets.setLayout(new GridLayout(0, 5)); // automatic rows, 5 columns
      
      // build initial buttons from existing set names
      rebuildButtons("");
      
      // Create a JScrollPane by passing in the JPanel as the view
      JScrollPane scrollPane = new JScrollPane(panelOfSets);
      scrollPane.setPreferredSize(new Dimension(MainFrame.WIDTH - 20, MainFrame.HEIGHT - 80));
      
      // Customize scroll bar policies (defaults are AS_NEEDED)
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // can always scroll up and down
      scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // if needed, scroll right and left
      
      // add scroll pane
      this.add(scrollPane);
      this.add(Box.createVerticalStrut(5));
      
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
         MainFrame.switchScreen("home");
      }
      else {
         String setName = message;
         // clicking a set will open a popout action menu
         showActionMenu(setName);
      }
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
         JButton setButton = new JButton(setName);
         setButton.setActionCommand(setName);
         setButton.addActionListener(this);
         
         setButtons.add(setButton);
         panelOfSets.add(setButton);
         anyAdded = true;
      }
      
      if (!anyAdded) {
         // show a message if nothing matches
         panelOfSets.add(new JLabel("No sets found."));
      }
      
      // refresh the grid so changes appear
      panelOfSets.revalidate();
      panelOfSets.repaint();
   }
   
   /**
    * showActionMenu opens a popout menu for the selected set.
    */
   private void showActionMenu(String setName) {
      String[] options = {"Learn", "Quiz", "Edit", "Delete", "Cancel"};
      
      // Bringing up a dialog with options is so much fun!
      // we provide lots of input parameters :o
      int choice = JOptionPane.showOptionDialog(
         this,
         "Choose an action for: " + setName,
         "Set Options",
         JOptionPane.DEFAULT_OPTION,
         JOptionPane.PLAIN_MESSAGE,
         null,
         options,
         options[0]
      );
      
      if (choice == 2) {
         // edit
         MainFrame.loadEditSet(setName);
      }
      else if (choice == 3) {
         // delete
         int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete the set \"" + setName + "\"?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
         );
         
         if (confirm == JOptionPane.YES_OPTION) {
            File setFile = Paths.setFile(setName);
            if (setFile.exists() && setFile.delete()) {
               // refresh list after delete
               setNames = SetRegistry.getSetNames();
               rebuildButtons(searchField.getText());
            }
            else {
               JOptionPane.showMessageDialog(this, "Could not delete the set.", "Delete Failed", JOptionPane.ERROR_MESSAGE);
            }
         }
      }
      else if (choice == 0 || choice == 1) {
         // placeholder for learn/quiz
         JOptionPane.showMessageDialog(this, "Coming soon!", "Not Implemented", JOptionPane.INFORMATION_MESSAGE);
      }
   }

}
