import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Screen for matching keys to definitions in groups of 5.
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
public class MatchPanel extends JPanel implements ActionListener {
   /** Private list of keys */
   private ArrayList<String> keys;
   /** Private list of definitions */
   private ArrayList<String> defs;
   
   /** Private label for title */
   private JLabel titleLabel;
   /** Private label for instructions */
   private JLabel instructionLabel;
   /** Private label for feedback */
   private JLabel feedbackLabel;
   
   /** Private panel for key buttons */
   private JPanel keyPanel;
   /** Private panel for definition buttons */
   private JPanel defPanel;
   
   /** Private list of key buttons */
   private ArrayList<JButton> keyButtons;
   /** Private list of def buttons */
   private ArrayList<JButton> defButtons;
   
   /** Private list of pair indices for keys */
   private ArrayList<Integer> keyPairIndex;
   /** Private list of pair indices for defs */
   private ArrayList<Integer> defPairIndex;
   
   /** Private selected key button index */
   private int selectedKey;
   /** Private selected def button index */
   private int selectedDef;
   
   /** Private color for default buttons */
   private Color defaultColor;
   /** Private name of the set */
   private String setName;
   
   /**
    * Constructor for the match screen.
    */
   public MatchPanel(String setName) {
      // save the name so we can return to Learn mode
      this.setName = setName;
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      
      // load the set data from file
      // SetRegistry reads the .txt file and splits each line into key/definition
      SetRegistry registry = new SetRegistry();
      registry.loadSet(setName);
      keys = registry.getKeys();
      defs = registry.getDefs();
      
      // padding
      this.add(Box.createVerticalStrut(10));
      
      // title for this set
      titleLabel = new JLabel("Match: " + setName);
      titleLabel.setAlignmentX(CENTER_ALIGNMENT);
      this.add(titleLabel);
      
      this.add(Box.createVerticalStrut(10));
      
      if (keys.size() == 0) {
         // empty set message and back button
         // no match game makes sense if the set has no cards
         JLabel emptyLabel = new JLabel("This set is empty.");
         emptyLabel.setAlignmentX(CENTER_ALIGNMENT);
         this.add(emptyLabel);
         this.add(Box.createVerticalStrut(10));
         
         JButton backButton = new JButton("Back");
         backButton.setActionCommand("back");
         backButton.addActionListener(this);
         backButton.setAlignmentX(CENTER_ALIGNMENT);
         backButton.setFocusable(false);
         backButton.setFocusPainted(false);
         this.add(backButton);
         
         this.setVisible(true);
         return;
      }
      
      // instruction label for the user
      // this explains what a "match" is for new users
      instructionLabel = new JLabel("Match each key with its definition.");
      instructionLabel.setAlignmentX(CENTER_ALIGNMENT);
      this.add(instructionLabel);
      
      this.add(Box.createVerticalStrut(10));
      
      // panel for the two columns
      // box layout keeps key/def columns centered
      JPanel bodyPanel = new JPanel();
      bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.X_AXIS));
      
      // key column on the left
      keyPanel = new JPanel();
      // 4 rows and 2 columns for 8 keys
      keyPanel.setLayout(new GridLayout(4, 2, 5, 5));
      keyPanel.setMaximumSize(new Dimension(350, 260));
      
      // definition column on the right
      defPanel = new JPanel();
      // 4 rows and 2 columns for 8 definitions
      defPanel.setLayout(new GridLayout(4, 2, 5, 5));
      defPanel.setMaximumSize(new Dimension(350, 260));
      
      bodyPanel.add(Box.createHorizontalGlue());
      bodyPanel.add(keyPanel);
      bodyPanel.add(Box.createHorizontalStrut(10));
      bodyPanel.add(defPanel);
      bodyPanel.add(Box.createHorizontalGlue());
      
      this.add(bodyPanel);
      
      this.add(Box.createVerticalStrut(10));
      
      // feedback label shows correct/incorrect
      feedbackLabel = new JLabel(" ");
      feedbackLabel.setAlignmentX(CENTER_ALIGNMENT);
      this.add(feedbackLabel);
      
      this.add(Box.createVerticalStrut(10));
      
      // back button at the bottom
      // keep navigation away from the matching area
      JButton backButton = new JButton("Back");
      backButton.setActionCommand("back");
      backButton.addActionListener(this);
      backButton.setAlignmentX(CENTER_ALIGNMENT);
      backButton.setFocusable(false);
      backButton.setFocusPainted(false);
      this.add(backButton);
      
      // initialize selection state
      // -1 means nothing is selected yet
      selectedKey = -1;
      selectedDef = -1;
      
      // load the first round
      loadRound();
      
      this.setVisible(true);
   }
   
   /**
    * loadRound fills the panels with 8 random pairs.
    */
   private void loadRound() {
      // clear panels and lists
      keyPanel.removeAll();
      defPanel.removeAll();
      keyButtons = new ArrayList<JButton>();
      defButtons = new ArrayList<JButton>();
      keyPairIndex = new ArrayList<Integer>();
      defPairIndex = new ArrayList<Integer>();
      selectedKey = -1;
      selectedDef = -1;
      
      // reset feedback
      feedbackLabel.setText(" ");
      
      // pick up to 8 unique indices from the set
      // we shuffle all indices then take the first eight
      ArrayList<Integer> roundIndices = new ArrayList<Integer>();
      for (int i = 0; i < keys.size(); i++) {
         roundIndices.add(i);
      }
      
      // shuffle and take the first 8
      Collections.shuffle(roundIndices);
      int count = 8;
      if (roundIndices.size() < count) {
         count = roundIndices.size();
      }
      
      ArrayList<Integer> chosen = new ArrayList<Integer>();
      for (int i = 0; i < count; i++) {
         chosen.add(roundIndices.get(i));
      }
      
      // build separate orders for keys and defs
      // this is the scrambling so they do not line up
      ArrayList<Integer> keyOrder = new ArrayList<Integer>(chosen);
      ArrayList<Integer> defOrder = new ArrayList<Integer>(chosen);
      Collections.shuffle(keyOrder);
      Collections.shuffle(defOrder);
      
      // build key buttons
      // each button's action command stores its position in the column
      for (int i = 0; i < keyOrder.size(); i++) {
         int pairIndex = keyOrder.get(i);
         JButton keyButton = new JButton();
         keyButton.setLayout(new BorderLayout());
         keyButton.setActionCommand("key:" + i);
         keyButton.addActionListener(this);
         keyButton.setFocusable(false);
         keyButton.setFocusPainted(false);
         keyButton.setOpaque(true);
         keyButton.setPreferredSize(new Dimension(160, 70));
         keyButton.setMinimumSize(new Dimension(160, 70));
         keyButton.setMaximumSize(new Dimension(160, 70));
         
         // text area inside the button so text can wrap without resizing the button
         JTextArea keyText = new JTextArea(keys.get(pairIndex));
         keyText.setLineWrap(true);
         keyText.setWrapStyleWord(true);
         keyText.setEditable(false);
         keyText.setFocusable(false);
         keyText.setOpaque(false);
         keyText.setBackground(keyButton.getBackground());
         keyText.setAlignmentX(CENTER_ALIGNMENT);
         keyText.setAlignmentY(CENTER_ALIGNMENT);
         // forward clicks on the text area to the button
         keyText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               keyButton.doClick();
            }
         });
         keyButton.add(keyText, BorderLayout.CENTER);
         
         // store the default color once
         // we use it to reset highlights on incorrect matches
         if (defaultColor == null) {
            defaultColor = keyButton.getBackground();
         }
         
         keyButtons.add(keyButton);
         keyPairIndex.add(pairIndex);
         keyPanel.add(keyButton);
      }
      
      // build definition buttons
      // action command stores its position in the definition column
      for (int i = 0; i < defOrder.size(); i++) {
         int pairIndex = defOrder.get(i);
         JButton defButton = new JButton();
         defButton.setLayout(new BorderLayout());
         defButton.setActionCommand("def:" + i);
         defButton.addActionListener(this);
         defButton.setFocusable(false);
         defButton.setFocusPainted(false);
         defButton.setOpaque(true);
         defButton.setPreferredSize(new Dimension(160, 70));
         defButton.setMinimumSize(new Dimension(160, 70));
         defButton.setMaximumSize(new Dimension(160, 70));
         
         // text area inside the button so text can wrap without resizing the button
         JTextArea defText = new JTextArea(defs.get(pairIndex));
         defText.setLineWrap(true);
         defText.setWrapStyleWord(true);
         defText.setEditable(false);
         defText.setFocusable(false);
         defText.setOpaque(false);
         defText.setBackground(defButton.getBackground());
         defText.setAlignmentX(CENTER_ALIGNMENT);
         defText.setAlignmentY(CENTER_ALIGNMENT);
         // forward clicks on the text area to the button
         defText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               defButton.doClick();
            }
         });
         defButton.add(defText, BorderLayout.CENTER);
         
         defButtons.add(defButton);
         defPairIndex.add(pairIndex);
         defPanel.add(defButton);
      }
      
      // refresh the panels so Swing redraws the new buttons
      keyPanel.revalidate();
      keyPanel.repaint();
      defPanel.revalidate();
      defPanel.repaint();
   }
   
   /**
    * checkMatch checks if the selected key and definition match.
    */
   private void checkMatch() {
      // make sure both sides are selected
      // otherwise we can't tell if there is a match yet
      if (selectedKey == -1 || selectedDef == -1) {
         return;
      }
      
      // map the selected buttons back to the original pair index
      int keyPair = keyPairIndex.get(selectedKey);
      int defPair = defPairIndex.get(selectedDef);
      
      if (keyPair == defPair) {
         // correct match: turn green and disable buttons
         // disabled buttons show that this pair is done
         JButton keyButton = keyButtons.get(selectedKey);
         JButton defButton = defButtons.get(selectedDef);
         keyButton.setBackground(Color.GREEN);
         defButton.setBackground(Color.GREEN);
         keyButton.setEnabled(false);
         defButton.setEnabled(false);
         
         feedbackLabel.setText("Correct!");
      }
      else {
         // incorrect match: reset colors
         // the user can try again without penalty
         JButton keyButton = keyButtons.get(selectedKey);
         JButton defButton = defButtons.get(selectedDef);
         keyButton.setBackground(defaultColor);
         defButton.setBackground(defaultColor);
         
         feedbackLabel.setText("Try again.");
      }
      
      // clear selections so the user can choose again
      selectedKey = -1;
      selectedDef = -1;
      
      // check if all matches are complete
      // if they are, load the next round
      if (allMatched()) {
         feedbackLabel.setText("Round complete! Loading next...");
         loadRound();
      }
   }
   
   /**
    * allMatched checks if every button is disabled.
    */
   private boolean allMatched() {
      // if any key button is still enabled, the round is not done
      for (int i = 0; i < keyButtons.size(); i++) {
         if (keyButtons.get(i).isEnabled()) {
            return false;
         }
      }
      
      return true;
   }
   
   /** Override for actionPerformed */
   @Override
   public void actionPerformed(ActionEvent e) {
      String message = e.getActionCommand();
      
      if (message.equals("back")) {
         // return to the learn screen for this set
         MainFrame.loadLearnSet(setName);
         return;
      }
      
      if (message.startsWith("key:")) {
         // parse the key index
         int index = Integer.parseInt(message.substring("key:".length()));
         
         // ignore clicks on disabled buttons
         if (!keyButtons.get(index).isEnabled()) {
            return;
         }
         
         // reset previous key highlight
         // this prevents two yellow keys at once
         if (selectedKey != -1) {
            keyButtons.get(selectedKey).setBackground(defaultColor);
         }
         
         selectedKey = index;
         keyButtons.get(index).setBackground(Color.YELLOW);
         
         checkMatch();
      }
      else if (message.startsWith("def:")) {
         // parse the definition index
         int index = Integer.parseInt(message.substring("def:".length()));
         
         // ignore clicks on disabled buttons
         if (!defButtons.get(index).isEnabled()) {
            return;
         }
         
         // reset previous def highlight
         // this prevents two yellow definitions at once
         if (selectedDef != -1) {
            defButtons.get(selectedDef).setBackground(defaultColor);
         }
         
         selectedDef = index;
         defButtons.get(index).setBackground(Color.YELLOW);
         
         checkMatch();
      }
   }
}
