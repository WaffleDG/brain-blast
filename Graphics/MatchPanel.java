import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JSeparator;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.Timer;

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
   /** Button width for match choices. */
   private static final int MATCH_BUTTON_W = 220;
   /** Button height for match choices. */
   private static final int MATCH_BUTTON_H = 130;
   /** Number of steps for incorrect fade animation. */
   private static final int INCORRECT_FADE_STEPS = 12;
   /** Delay in ms between incorrect fade steps. */
   private static final int INCORRECT_FADE_DELAY_MS = 40;
   
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
   /** Private list of key text panes for color updates */
   private ArrayList<CenteredWrapTextPane> keyTextPanes;
   /** Private list of def text panes for color updates */
   private ArrayList<CenteredWrapTextPane> defTextPanes;
   
   /** Private list of pair indices for keys */
   private ArrayList<Integer> keyPairIndex;
   /** Private list of pair indices for defs */
   private ArrayList<Integer> defPairIndex;
   
   /** Private selected key button index */
   private int selectedKey;
   /** Private selected def button index */
   private int selectedDef;
   /** Private flag for pending round reset */
   private boolean waitingForNextRound;
   /** Timer for flashing incorrect selections */
   private Timer incorrectTimer;
   /** Timer for delaying the next round */
   private Timer nextRoundTimer;
   
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
      // stack sections vertically
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      UIStyle.stylePanel(this);
      
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
      UIStyle.styleTitle(titleLabel);
      titleLabel.setAlignmentX(CENTER_ALIGNMENT);
      this.add(titleLabel);
      
      this.add(Box.createVerticalStrut(6));
      this.add(buildSeparator());
      this.add(Box.createVerticalStrut(10));
      
      if (keys.size() == 0) {
         // empty set message and back button
         // no match game makes sense if the set has no cards
        JLabel emptyLabel = new JLabel("This set is empty.");
         UIStyle.styleLabel(emptyLabel);
        emptyLabel.setAlignmentX(CENTER_ALIGNMENT);
        this.add(emptyLabel);
         this.add(Box.createVerticalStrut(10));
         
        JButton backButton = new JButton("Back");
        backButton.setActionCommand("back");
        backButton.addActionListener(this);
        backButton.setAlignmentX(CENTER_ALIGNMENT);
        backButton.setFocusable(false);
        backButton.setFocusPainted(false);
         UIStyle.styleButton(backButton, 120, 34);
        this.add(backButton);
         
         // show the panel even when empty
         this.setVisible(true);
         return;
      }
      
      // instruction label for the user
      // this explains what a "match" is for new users
      instructionLabel = new JLabel("Match each key with its definition.");
      UIStyle.styleLabel(instructionLabel);
      instructionLabel.setAlignmentX(CENTER_ALIGNMENT);
      this.add(instructionLabel);
      
      this.add(Box.createVerticalStrut(8));
      this.add(buildSeparator());
      this.add(Box.createVerticalStrut(10));
      
      // panel for the two columns
      // box layout keeps key/def columns centered
      JPanel bodyPanel = new JPanel();
      bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.X_AXIS));
      UIStyle.stylePanel(bodyPanel);
      
      // key column on the left
      keyPanel = new JPanel();
      // 4 rows and 2 columns for 8 keys
      keyPanel.setLayout(new GridLayout(4, 2, 5, 5));
      Dimension keyPanelSize = new Dimension(2 * MATCH_BUTTON_W + 5, 4 * MATCH_BUTTON_H + 3 * 5);
      keyPanel.setPreferredSize(keyPanelSize);
      keyPanel.setMinimumSize(keyPanelSize);
      keyPanel.setMaximumSize(keyPanelSize);
      UIStyle.styleCardPanel(keyPanel);
      
      // definition column on the right
      defPanel = new JPanel();
      // 4 rows and 2 columns for 8 definitions
      defPanel.setLayout(new GridLayout(4, 2, 5, 5));
      Dimension defPanelSize = new Dimension(2 * MATCH_BUTTON_W + 5, 4 * MATCH_BUTTON_H + 3 * 5);
      defPanel.setPreferredSize(defPanelSize);
      defPanel.setMinimumSize(defPanelSize);
      defPanel.setMaximumSize(defPanelSize);
      UIStyle.styleCardPanel(defPanel);
      
      bodyPanel.add(Box.createHorizontalGlue());
      bodyPanel.add(keyPanel);
      bodyPanel.add(Box.createHorizontalStrut(5));
      bodyPanel.add(defPanel);
      bodyPanel.add(Box.createHorizontalGlue());
      
      // add the two-column body to the screen
      this.add(bodyPanel);
      
      this.add(Box.createVerticalStrut(8));
      this.add(buildSeparator());
      this.add(Box.createVerticalStrut(10));
      
      // feedback label shows correct/incorrect
      feedbackLabel = new JLabel(" ");
      UIStyle.styleLabel(feedbackLabel);
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
      UIStyle.styleButton(backButton, 120, 34);
      this.add(backButton);
      // extra padding so the bottom button doesn't feel cramped
      this.add(Box.createVerticalStrut(10));
      
      // initialize selection state
      // -1 means nothing is selected yet
      selectedKey = -1;
      selectedDef = -1;
      waitingForNextRound = false;
      
      // load the first round
      loadRound();
      
      // show the panel once ready
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
      keyTextPanes = new ArrayList<CenteredWrapTextPane>();
      defTextPanes = new ArrayList<CenteredWrapTextPane>();
      keyPairIndex = new ArrayList<Integer>();
      defPairIndex = new ArrayList<Integer>();
      selectedKey = -1;
      selectedDef = -1;
      waitingForNextRound = false;
      
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
         keyButton.setActionCommand("key:" + i);
         keyButton.addActionListener(this);
         keyButton.setFocusable(false);
         keyButton.setFocusPainted(false);
         keyButton.setOpaque(true);
         keyButton.setPreferredSize(new Dimension(MATCH_BUTTON_W, MATCH_BUTTON_H));
         keyButton.setMinimumSize(new Dimension(MATCH_BUTTON_W, MATCH_BUTTON_H));
         keyButton.setMaximumSize(new Dimension(MATCH_BUTTON_W, MATCH_BUTTON_H));
         UIStyle.styleButton(keyButton);
         
         // center and wrap the key text inside the button
         keyButton.setLayout(new GridBagLayout());
         CenteredWrapTextPane keyText = buildMatchText(keys.get(pairIndex));
         keyText.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
               // forward clicks on the text to the button
               keyButton.doClick();
            }
         });
         GridBagConstraints keyGbc = new GridBagConstraints();
         keyGbc.gridx = 0;
         keyGbc.gridy = 0;
         keyGbc.anchor = GridBagConstraints.CENTER;
         keyButton.add(keyText, keyGbc);
         
         // keep text panes so we can update colors on highlight
         keyTextPanes.add(keyText);
         // store the default color once
         // we use it to reset highlights on incorrect matches
         if (defaultColor == null) {
            defaultColor = keyButton.getBackground();
         }
         
         // keep index mapping so we can check matches later
         keyButtons.add(keyButton);
         keyPairIndex.add(pairIndex);
         keyPanel.add(keyButton);
      }
      
      // build definition buttons
      // action command stores its position in the definition column
      for (int i = 0; i < defOrder.size(); i++) {
         int pairIndex = defOrder.get(i);
         JButton defButton = new JButton();
         defButton.setActionCommand("def:" + i);
         defButton.addActionListener(this);
         defButton.setFocusable(false);
         defButton.setFocusPainted(false);
         defButton.setOpaque(true);
         defButton.setPreferredSize(new Dimension(MATCH_BUTTON_W, MATCH_BUTTON_H));
         defButton.setMinimumSize(new Dimension(MATCH_BUTTON_W, MATCH_BUTTON_H));
         defButton.setMaximumSize(new Dimension(MATCH_BUTTON_W, MATCH_BUTTON_H));
         UIStyle.styleButton(defButton);
         
         // center and wrap the definition text inside the button
         defButton.setLayout(new GridBagLayout());
         CenteredWrapTextPane defText = buildMatchText(defs.get(pairIndex));
         defText.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
               // forward clicks on the text to the button
               defButton.doClick();
            }
         });
         GridBagConstraints defGbc = new GridBagConstraints();
         defGbc.gridx = 0;
         defGbc.gridy = 0;
         defGbc.anchor = GridBagConstraints.CENTER;
         defButton.add(defText, defGbc);
         
         // keep text panes so we can update colors on highlight
         defTextPanes.add(defText);
         // keep index mapping so we can check matches later
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
         keyTextPanes.get(selectedKey).setForeground(Color.BLACK);
         defTextPanes.get(selectedDef).setForeground(Color.BLACK);
         
         feedbackLabel.setText("Correct!");
      }
      else {
         // incorrect match: fade red back to the default color
         JButton keyButton = keyButtons.get(selectedKey);
         JButton defButton = defButtons.get(selectedDef);
         int keyIndex = selectedKey;
         int defIndex = selectedDef;
         
         keyButton.setBackground(Color.RED);
         defButton.setBackground(Color.RED);
         keyTextPanes.get(keyIndex).setForeground(Color.WHITE);
         defTextPanes.get(defIndex).setForeground(Color.WHITE);
         
         // temporarily disable just the chosen buttons so the flash is visible
         keyButton.setEnabled(false);
         defButton.setEnabled(false);
         
         feedbackLabel.setText("Try again.");
         
         if (incorrectTimer != null && incorrectTimer.isRunning()) {
            incorrectTimer.stop();
         }
         int[] step = new int[] { 0 };
         incorrectTimer = new Timer(INCORRECT_FADE_DELAY_MS, evt -> {
            step[0]++;
            double t = Math.min(1.0, step[0] / (double) INCORRECT_FADE_STEPS);
            Color fadeColor = blend(Color.RED, defaultColor, t);
            keyButtons.get(keyIndex).setBackground(fadeColor);
            defButtons.get(defIndex).setBackground(fadeColor);
            if (t >= 1.0) {
               keyButtons.get(keyIndex).setEnabled(true);
               defButtons.get(defIndex).setEnabled(true);
               keyTextPanes.get(keyIndex).setForeground(UIStyle.ACCENT_TEXT);
               defTextPanes.get(defIndex).setForeground(UIStyle.ACCENT_TEXT);
               ((Timer) evt.getSource()).stop();
            }
         });
         incorrectTimer.start();
      }
      
      // clear selections so the user can choose again
      selectedKey = -1;
      selectedDef = -1;
      
      // check if all matches are complete
      // if they are, load the next round
      if (allMatched() && !waitingForNextRound) {
         feedbackLabel.setText("Round complete! Loading next...");
         waitingForNextRound = true;
         // disable any remaining buttons while we wait to reshuffle
         for (int i = 0; i < keyButtons.size(); i++) {
            keyButtons.get(i).setEnabled(false);
         }
         for (int i = 0; i < defButtons.size(); i++) {
            defButtons.get(i).setEnabled(false);
         }
         if (nextRoundTimer != null && nextRoundTimer.isRunning()) {
            nextRoundTimer.stop();
         }
         nextRoundTimer = new Timer(1000, evt -> {
            waitingForNextRound = false;
            loadRound();
         });
         nextRoundTimer.setRepeats(false);
         nextRoundTimer.start();
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
      
      // no enabled buttons means the round is complete
      return true;
   }
   
   /**
    * buildMatchText creates a wrapped text pane that fits inside a match button.
    */
   private CenteredWrapTextPane buildMatchText(String text) {
      CenteredWrapTextPane textPane = new CenteredWrapTextPane(text, MATCH_BUTTON_W - 24);
      textPane.setFont(UIStyle.BODY_FONT);
      textPane.setForeground(UIStyle.ACCENT_TEXT);
      fitTextToHeight(textPane, MATCH_BUTTON_H - 20);
      return textPane;
   }
   
   /**
    * fitTextToHeight shrinks the font until the text fits inside the target height.
    */
   private void fitTextToHeight(CenteredWrapTextPane textPane, int maxHeight) {
      float size = textPane.getFont().getSize2D();
      while (textPane.getPreferredSize().height > maxHeight && size > 10f) {
         size -= 1f;
         textPane.setFont(textPane.getFont().deriveFont(size));
      }
   }
   
   /**
    * buildSeparator creates a thin divider for the layout.
    */
   private JSeparator buildSeparator() {
      JSeparator separator = new JSeparator();
      separator.setMaximumSize(new Dimension(MainFrame.WIDTH - 160, 2));
      separator.setForeground(UIStyle.SOFT_OUTLINE);
      return separator;
   }
   
   /**
    * blend creates a color between start and end using t (0.0-1.0).
    */
   private Color blend(Color start, Color end, double t) {
      int r = (int) Math.round(start.getRed() + (end.getRed() - start.getRed()) * t);
      int g = (int) Math.round(start.getGreen() + (end.getGreen() - start.getGreen()) * t);
      int b = (int) Math.round(start.getBlue() + (end.getBlue() - start.getBlue()) * t);
      return new Color(r, g, b);
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
         // clicking the same key deselects it
         if (selectedKey == index) {
            keyButtons.get(index).setBackground(defaultColor);
            keyTextPanes.get(index).setForeground(UIStyle.ACCENT_TEXT);
            selectedKey = -1;
            return;
         }
         
         // reset previous key highlight
         // this prevents two yellow keys at once
         if (selectedKey != -1) {
            keyButtons.get(selectedKey).setBackground(defaultColor);
            keyTextPanes.get(selectedKey).setForeground(UIStyle.ACCENT_TEXT);
         }
         
         // mark the new selection
         selectedKey = index;
         keyButtons.get(index).setBackground(Color.YELLOW);
         keyTextPanes.get(index).setForeground(Color.BLACK);
         
         // check for a match if both sides are selected
         checkMatch();
      }
      else if (message.startsWith("def:")) {
         // parse the definition index
         int index = Integer.parseInt(message.substring("def:".length()));
         
         // ignore clicks on disabled buttons
         if (!defButtons.get(index).isEnabled()) {
            return;
         }
         // clicking the same definition deselects it
         if (selectedDef == index) {
            defButtons.get(index).setBackground(defaultColor);
            defTextPanes.get(index).setForeground(UIStyle.ACCENT_TEXT);
            selectedDef = -1;
            return;
         }
         
         // reset previous def highlight
         // this prevents two yellow definitions at once
         if (selectedDef != -1) {
            defButtons.get(selectedDef).setBackground(defaultColor);
            defTextPanes.get(selectedDef).setForeground(UIStyle.ACCENT_TEXT);
         }
         
         // mark the new selection
         selectedDef = index;
         defButtons.get(index).setBackground(Color.YELLOW);
         defTextPanes.get(index).setForeground(Color.BLACK);
         
         // check for a match if both sides are selected
         checkMatch();
      }
   }

}
