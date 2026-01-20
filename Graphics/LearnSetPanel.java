import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.KeyStroke;
import javax.swing.JComponent;
import javax.swing.AbstractAction;
import javax.swing.JSeparator;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Screen for studying a set using flashcards.
 *
 * <p>
 * Authors: Gregory Cohen and Riya Jonnala.
 *
 * <p>
 * Version: 1.0, 1/5/26.
 *
 * 
 * @author     Gregory Cohen and Riya Jonnala
 * @since      1/5/26
 * @version    1.0
 * @see        JPanel
 */
public class LearnSetPanel extends JPanel implements ActionListener {
   /** Private list of keys */
   private ArrayList<String> keys;
   /** Private list of definitions */
   private ArrayList<String> defs;
   /** Private label for progress */
   private JLabel progressLabel;
   /** Private label for the flashcards header */
   private JLabel flashcardsLabel;
   /** Private panel for the card */
   private JPanel cardPanel;
   /** Private flashcard reference */
   private Flashcard currentCard;
   /** Private flag for left key state */
   private boolean leftHeld;
   /** Private flag for right key state */
   private boolean rightHeld;
   /** Private flag for space key state */
   private boolean spaceHeld;
   /** Private index for current card */
   private int currentIndex;
   /** Private name of the set */
   private String setName;
   
   /**
    * Constructor for the learn screen.
    */
   public LearnSetPanel(String setName) {
      // save the name so we can open match mode later
      this.setName = setName;
      // vertical layout stacks title, card, and controls
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      UIStyle.stylePanel(this);
      
      // load the set data from file
      SetRegistry registry = new SetRegistry();
      registry.loadSet(setName);
      keys = registry.getKeys();
      defs = registry.getDefs();
      
      // padding
      this.add(Box.createVerticalStrut(8));
      
      // title for this set
      JLabel title = new JLabel("Learn: " + setName);
      UIStyle.styleTitle(title);
      title.setFont(UIStyle.TITLE_FONT.deriveFont(Font.BOLD, 26f));
      title.setAlignmentX(CENTER_ALIGNMENT);
      this.add(title);
      
      this.add(Box.createVerticalStrut(6));
      this.add(buildSeparator());
      this.add(Box.createVerticalStrut(10));
      
      if (keys.size() == 0) {
         // empty set message and back button
         JLabel emptyLabel = new JLabel("This set is empty.");
         UIStyle.styleLabel(emptyLabel);
         emptyLabel.setAlignmentX(CENTER_ALIGNMENT);
         this.add(emptyLabel);
         this.add(Box.createVerticalStrut(10));
         
         JButton backButton = new JButton("Back");
         backButton.setActionCommand("back");
         backButton.addActionListener(this);
         backButton.setAlignmentX(CENTER_ALIGNMENT);
         UIStyle.styleButton(backButton, 120, 34);
         backButton.setFocusable(false);
         backButton.setFocusPainted(false);
         this.add(backButton);
         
         // show the panel even if it is empty
         this.setVisible(true);
         return;
      }
      
      flashcardsLabel = new JLabel("Flashcards");
      UIStyle.styleLabel(flashcardsLabel);
      flashcardsLabel.setFont(UIStyle.BODY_FONT.deriveFont(Font.BOLD, 18f));
      flashcardsLabel.setForeground(UIStyle.ACCENT);
      flashcardsLabel.setAlignmentX(CENTER_ALIGNMENT);
      this.add(flashcardsLabel);
      
      // progress label shows which card you're on
      progressLabel = new JLabel();
      UIStyle.styleLabel(progressLabel);
      progressLabel.setFont(UIStyle.BODY_FONT.deriveFont(Font.BOLD, 16f));
      progressLabel.setAlignmentX(CENTER_ALIGNMENT);
      this.add(progressLabel);
      
      this.add(Box.createVerticalStrut(8));
      this.add(buildSeparator());
      this.add(Box.createVerticalStrut(10));
      
      cardPanel = new JPanel();
      // center the flashcard inside the panel
      cardPanel.setLayout(new GridBagLayout());
      cardPanel.setMaximumSize(new Dimension(MainFrame.WIDTH - 60, 280));
      cardPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
      UIStyle.styleCardPanel(cardPanel);
      cardPanel.setAlignmentX(CENTER_ALIGNMENT);
      // clicking the card area flips the card
      cardPanel.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            // flip the current card when the card area is clicked
            if (currentCard != null) {
               currentCard.flip();
            }
         }
      });
      this.add(cardPanel);
      
      this.add(Box.createVerticalStrut(8));
      this.add(buildSeparator());
      this.add(Box.createVerticalStrut(8));
      this.add(Box.createVerticalGlue());
      
      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
      UIStyle.stylePanel(buttonPanel);
      buttonPanel.setAlignmentX(CENTER_ALIGNMENT);
      
      JPanel navRow = new JPanel();
      navRow.setLayout(new BoxLayout(navRow, BoxLayout.X_AXIS));
      UIStyle.stylePanel(navRow);
      
      JPanel modeRow = new JPanel();
      modeRow.setLayout(new BoxLayout(modeRow, BoxLayout.X_AXIS));
      UIStyle.stylePanel(modeRow);
      
      // navigation buttons
      JButton prevButton = new JButton("Prev");
      prevButton.setActionCommand("prev");
      prevButton.addActionListener(this);
      prevButton.setFocusable(false);
      prevButton.setFocusPainted(false);
      UIStyle.styleButton(prevButton, 120, 40);
      prevButton.setFont(UIStyle.BUTTON_FONT.deriveFont(Font.BOLD, 16f));
      
      JButton nextButton = new JButton("Next");
      nextButton.setActionCommand("next");
      nextButton.addActionListener(this);
      nextButton.setFocusable(false);
      nextButton.setFocusPainted(false);
      UIStyle.styleButton(nextButton, 120, 40);
      nextButton.setFont(UIStyle.BUTTON_FONT.deriveFont(Font.BOLD, 16f));
      
      // shuffle button
      JButton shuffleButton = new JButton("Shuffle");
      shuffleButton.setActionCommand("shuffle");
      shuffleButton.addActionListener(this);
      shuffleButton.setFocusable(false);
      shuffleButton.setFocusPainted(false);
      UIStyle.styleButton(shuffleButton, 130, 40);
      shuffleButton.setFont(UIStyle.BUTTON_FONT.deriveFont(Font.BOLD, 16f));
      
      // match button
      JButton matchButton = new JButton("Match");
      matchButton.setActionCommand("match");
      matchButton.addActionListener(this);
      matchButton.setFocusable(false);
      matchButton.setFocusPainted(false);
      UIStyle.styleButton(matchButton, 120, 40);
      matchButton.setFont(UIStyle.BUTTON_FONT.deriveFont(Font.BOLD, 16f));
      
      // back to catalog
      JButton backButton = new JButton("Back");
      backButton.setActionCommand("back");
      backButton.addActionListener(this);
      backButton.setFocusable(false);
      backButton.setFocusPainted(false);
      UIStyle.styleButton(backButton, 110, 40);
      backButton.setFont(UIStyle.BUTTON_FONT.deriveFont(Font.BOLD, 16f));
      
      // add navigation buttons on the top row (Prev, Next)
      navRow.add(Box.createHorizontalGlue());
      navRow.add(prevButton);
      navRow.add(Box.createHorizontalStrut(18));
      navRow.add(nextButton);
      navRow.add(Box.createHorizontalGlue());
      
      // add mode buttons on the bottom row (Back, Shuffle, Match)
      modeRow.add(Box.createHorizontalGlue());
      modeRow.add(backButton);
      modeRow.add(Box.createHorizontalStrut(12));
      modeRow.add(shuffleButton);
      modeRow.add(Box.createHorizontalStrut(12));
      modeRow.add(matchButton);
      modeRow.add(Box.createHorizontalGlue());
      
      buttonPanel.add(navRow);
      buttonPanel.add(Box.createVerticalStrut(8));
      buttonPanel.add(modeRow);
      
      this.add(buttonPanel);
      
      // set up initial state
      currentIndex = 0;
      updateCard();
      setupKeyBinds();
      
      // keep focus on the panel so space flips the card and doesn't focus on the buttons
      this.requestFocusInWindow();
      
      // show the panel once ready
      this.setVisible(true);
   }
   
   /**
    * updateCard will rebuild the card area and progress label.
    */
   private void updateCard() {
      // clear the old card before adding a new one
      cardPanel.removeAll();
      
      // get the current key/definition and build the flashcard
      String key = keys.get(currentIndex);
      String def = defs.get(currentIndex);
      currentCard = new Flashcard(key, def);
      // add the flashcard to the centered panel
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.anchor = GridBagConstraints.CENTER;
      cardPanel.add(currentCard, gbc);
      
      // update progress label
      progressLabel.setText((currentIndex + 1) + " / " + keys.size());
      
      // refresh the card panel
      cardPanel.revalidate();
      cardPanel.repaint();
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
    * shuffleCards shuffles the cards while keeping pairs together.
    */
   private void shuffleCards() {
      // build a list of indices so key/def pairs stay together
      ArrayList<Integer> order = new ArrayList<Integer>();
      for (int i = 0; i < keys.size(); i++) {
         order.add(i);
      }
      
      // shuffle the order list
      Collections.shuffle(order, new Random());
      
      // rebuild keys/defs in the shuffled order
      ArrayList<String> newKeys = new ArrayList<String>();
      ArrayList<String> newDefs = new ArrayList<String>();
      
      for (int i = 0; i < order.size(); i++) {
         int idx = order.get(i);
         newKeys.add(keys.get(idx));
         newDefs.add(defs.get(idx));
      }
      
      // overwrite the lists with the new order
      keys = newKeys;
      defs = newDefs;
      // reset to the first card
      currentIndex = 0;
   }
   
   /**
    * setupKeyBinds adds arrow key and spacebar actions for navigation and flip.
    * The held flags make sure holding a key does not spam actions.
    */
   private void setupKeyBinds() {
      // Using Input and Action maps to bind keys
      // WHEN_IN_FOCUSED_WINDOW makes the keys work without clicking on a specific component
      InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
      ActionMap actionMap = this.getActionMap();
      
      // key press bindings (fire once, then wait for release)
      inputMap.put(KeyStroke.getKeyStroke("LEFT"), "prev");
      inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "next");
      inputMap.put(KeyStroke.getKeyStroke("SPACE"), "flip");
      
      // key release bindings (reset held flags)
      inputMap.put(KeyStroke.getKeyStroke("released LEFT"), "prevReleased");
      inputMap.put(KeyStroke.getKeyStroke("released RIGHT"), "nextReleased");
      inputMap.put(KeyStroke.getKeyStroke("released SPACE"), "flipReleased");
      
      actionMap.put("prev", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            // only trigger once per key press
            if (leftHeld) {
               return;
            }
            // mark the key as held so it won't repeat
            leftHeld = true;
            // move backwards if possible
            if (currentIndex > 0) {
               currentIndex--;
               updateCard();
            }
         }
      });
      
      actionMap.put("prevReleased", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            // release the key so the next press can work
            leftHeld = false;
         }
      });
      
      actionMap.put("next", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            // only trigger once per key press
            if (rightHeld) {
               return;
            }
            // mark the key as held so it won't repeat
            rightHeld = true;
            // move forwards if possible
            if (currentIndex < keys.size() - 1) {
               currentIndex++;
               updateCard();
            }
         }
      });
      
      actionMap.put("nextReleased", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            // release the key so the next press can work
            rightHeld = false;
         }
      });
      
      actionMap.put("flip", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            // only trigger once per key press
            if (spaceHeld) {
               return;
            }
            // mark the key as held so it won't repeat
            spaceHeld = true;
            // flip the current card if it exists
            if (currentCard != null) {
               currentCard.flip();
            }
         }
      });
      
      actionMap.put("flipReleased", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            // release the key so the next press can work
            spaceHeld = false;
         }
      });
   }
   
   /** Override for actionPerformed */
   @Override
   public void actionPerformed(ActionEvent e) {
      String message = e.getActionCommand();
      
      if (message.equals("prev")) {
         // move backwards if possible
         if (currentIndex > 0) {
            currentIndex--;
            updateCard();
         }
      }
      else if (message.equals("next")) {
         // move forwards if possible
         if (currentIndex < keys.size() - 1) {
            currentIndex++;
            updateCard();
         }
      }
      else if (message.equals("shuffle")) {
         // randomize the order and restart
         shuffleCards();
         updateCard();
      }
      else if (message.equals("match")) {
         // switch to match mode for this set
         MainFrame.loadMatchSet(setName);
      }
      else if (message.equals("back")) {
         // return to the catalog view
         MainFrame.switchScreen("catalog");
      }
      
      // return focus to the panel so keybinds keep working
      this.requestFocusInWindow();
   }
}
