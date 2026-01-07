import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.KeyStroke;
import javax.swing.JComponent;
import javax.swing.AbstractAction;
import javax.swing.JSlider;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;

/**
 * Screen for quizzing a set with multiple choice and written answers.
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
public class QuizPanel extends JPanel implements ActionListener {
   /** Private list of keys */
   private ArrayList<String> keys;
   /** Private list of definitions */
   private ArrayList<String> defs;
   /** Private list of question order */
   private ArrayList<Integer> order;
   
   /** Private label for the title */
   private JLabel titleLabel;
   /** Private label for progress */
   private JLabel progressLabel;
   /** Private label for the instruction */
   private JLabel instructionLabel;
   /** Private label for the question */
   private JLabel questionLabel;
   /** Private label for feedback */
   private JLabel feedbackLabel;
   
   /** Private panel for multiple choice buttons */
   private JPanel choicePanel;
   /** Private button list for choices */
   private ArrayList<JButton> choiceButtons;
   
   /** Private panel for written response */
   private JPanel writtenPanel;
   /** Private text field for written answer */
   private JTextField answerField;
   /** Private submit button for written answer */
   private JButton submitButton;
   
   /** Private next button */
   private JButton nextButton;
   
   /** Private index for current question */
   private int currentIndex;
   /** Private score counter */
   private int score;
   /** Private current correct answer */
   private String currentAnswer;
   /** Private flag for current mode */
   private boolean currentIsMultipleChoice;
   /** Private flag for question direction */
   private boolean currentAskKey;
   /** Private index for the correct choice button */
   private int correctChoiceIndex;
   /** Private default color for choice buttons */
   private Color defaultChoiceColor;
   /** Private total number of questions */
   private int totalQuestions;
   /** Private flag for allowing multiple choice */
   private boolean allowMultipleChoice;
   /** Private flag for allowing written response */
   private boolean allowWritten;
   /** Private slider for question count */
   private JSlider questionSlider;
   /** Private checkbox for multiple choice */
   private JCheckBox mcqCheck;
   /** Private checkbox for free response */
   private JCheckBox frqCheck;
   
   /**
    * Constructor for the quiz screen.
    */
   public QuizPanel(String setName) {
      // use a vertical layout so each section stacks top-to-bottom
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      
      // top bar with a back button in the corner
      JPanel topBar = new JPanel();
      topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
      
      JButton topBackButton = new JButton("Back");
      topBackButton.setActionCommand("back");
      topBackButton.addActionListener(this);
      topBackButton.setFocusable(false);
      topBackButton.setFocusPainted(false);
      
      topBar.add(topBackButton);
      topBar.add(Box.createHorizontalGlue());
      topBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, topBar.getPreferredSize().height));
      this.add(topBar);
      
      // load the set data from file
      // SetRegistry handles reading and parsing the set text file
      SetRegistry registry = new SetRegistry();
      registry.loadSet(setName);
      keys = registry.getKeys();
      defs = registry.getDefs();
      
      // padding to give breathing room at the top
      this.add(Box.createVerticalStrut(10));
      
      // title for this set
      // keeping the set name visible helps with context
      titleLabel = new JLabel("Quiz: " + setName);
      titleLabel.setAlignmentX(CENTER_ALIGNMENT);
      this.add(titleLabel);
      
      this.add(Box.createVerticalStrut(10));
      
      if (keys.size() == 0) {
         // empty set message and back button
         // do not try to quiz if there are no cards
         JLabel emptyLabel = new JLabel("This set is empty.");
         emptyLabel.setAlignmentX(CENTER_ALIGNMENT);
         this.add(emptyLabel);
         this.add(Box.createVerticalStrut(10));
         
         // back button to return to the catalog
         JButton emptyBackButton = new JButton("Back");
         emptyBackButton.setActionCommand("back");
         emptyBackButton.addActionListener(this);
         emptyBackButton.setAlignmentX(CENTER_ALIGNMENT);
         emptyBackButton.setFocusable(false);
         emptyBackButton.setFocusPainted(false);
         this.add(emptyBackButton);
         
         this.setVisible(true);
         return;
      }
      
      progressLabel = new JLabel();
      progressLabel.setAlignmentX(CENTER_ALIGNMENT);
      this.add(progressLabel);
      
      this.add(Box.createVerticalStrut(10));
      
      // instruction label frames the question
      instructionLabel = new JLabel(" ");
      instructionLabel.setAlignmentX(CENTER_ALIGNMENT);
      this.add(instructionLabel);
      
      this.add(Box.createVerticalStrut(5));
      
      // question label is where the prompt will go
      questionLabel = new JLabel();
      questionLabel.setAlignmentX(CENTER_ALIGNMENT);
      this.add(questionLabel);
      
      this.add(Box.createVerticalStrut(10));
      
      // multiple choice panel
      // grid layout keeps the buttons in a clean 2x2 arrangement
      choicePanel = new JPanel();
      choicePanel.setLayout(new GridLayout(2, 2, 10, 10));
      choicePanel.setMaximumSize(new Dimension(MainFrame.WIDTH - 40, 140));
      choiceButtons = new ArrayList<JButton>();
      for (int i = 0; i < 4; i++) {
         // create the button and wire it to the action listener
         JButton choiceButton = new JButton("Choice");
         // larger text to make answers easier to read and click
         choiceButton.setFont(new Font(choiceButton.getFont().getName(), Font.BOLD, 18));
         choiceButton.setActionCommand("choice:" + i);
         choiceButton.addActionListener(this);
         choiceButton.setFocusable(false);
         choiceButton.setFocusPainted(false);
         choiceButton.setOpaque(true);
         choiceButton.setContentAreaFilled(true);
         
         // store the default background color once
         if (defaultChoiceColor == null) {
            defaultChoiceColor = choiceButton.getBackground();
         }
         choiceButtons.add(choiceButton);
         choicePanel.add(choiceButton);
      }
      this.add(choicePanel);
      
      this.add(Box.createVerticalStrut(10));
      
      // written response panel
      // a simple row layout with a field and a submit button
      writtenPanel = new JPanel();
      writtenPanel.setLayout(new BoxLayout(writtenPanel, BoxLayout.X_AXIS));
      
      // text field for the user's written answer
      answerField = new JTextField(20);
      answerField.setMaximumSize(answerField.getPreferredSize());
      answerField.setActionCommand("submit");
      answerField.addActionListener(this);
      
      // submit button for written answers
      submitButton = new JButton("Submit");
      submitButton.setActionCommand("submit");
      submitButton.addActionListener(this);
      submitButton.setFocusable(false);
      submitButton.setFocusPainted(false);
      
      writtenPanel.add(answerField);
      writtenPanel.add(Box.createHorizontalStrut(10));
      writtenPanel.add(submitButton);
      this.add(writtenPanel);
      
      this.add(Box.createVerticalStrut(10));
      
      // feedback label shows correct/incorrect
      feedbackLabel = new JLabel(" ");
      feedbackLabel.setAlignmentX(CENTER_ALIGNMENT);
      this.add(feedbackLabel);
      
      this.add(Box.createVerticalStrut(10));
      
      // navigation panel for next (back is in the top bar)
      JPanel navPanel = new JPanel();
      navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.X_AXIS));
      
      // next button moves to the next question
      nextButton = new JButton("Next");
      nextButton.setActionCommand("next");
      nextButton.addActionListener(this);
      nextButton.setFocusable(false);
      nextButton.setFocusPainted(false);
      
      // push the next button to the right side of the row
      navPanel.add(Box.createHorizontalGlue());
      navPanel.add(nextButton);
      this.add(navPanel);
      
      // create a question order and start at the beginning
      // this keeps the quiz order randomized
      buildQuestionOrder();
      currentIndex = 0;
      totalQuestions = order.size();
      allowMultipleChoice = true;
      allowWritten = true;
      
      // ask the user how they want the quiz to run
      // this uses a small panel with a slider and checkboxes
      configureQuiz();
      score = 0;
      loadQuestion();
      setupKeyBinds();
      
      this.setVisible(true);
   }
   
   /**
    * buildQuestionOrder creates a shuffled list of indices.
    */
   private void buildQuestionOrder() {
      // start with 0..n-1 indices
      // each index corresponds to a key/definition pair
      order = new ArrayList<Integer>();
      for (int i = 0; i < keys.size(); i++) {
         order.add(i);
      }

      // shuffle the order so the quiz is not always the same
      Collections.shuffle(order);
   }
   
   /**
    * configureQuiz asks the user for question count and type.
    */
   private void configureQuiz() {
      // build a small setup panel
      JPanel setupPanel = new JPanel();
      setupPanel.setLayout(new BoxLayout(setupPanel, BoxLayout.Y_AXIS));
      
      // slider for question count
      questionSlider = new JSlider(1, order.size(), order.size());
      questionSlider.setMajorTickSpacing(1);
      questionSlider.setPaintTicks(true);
      questionSlider.setPaintLabels(false);
      
      // label with a live number next to it
      JPanel countRow = new JPanel();
      countRow.setLayout(new BoxLayout(countRow, BoxLayout.X_AXIS));
      
      JLabel sliderLabel = new JLabel("Number of questions: ");
      JLabel sliderValue = new JLabel(String.valueOf(questionSlider.getValue()));
      
      questionSlider.addChangeListener(e -> {
         sliderValue.setText(String.valueOf(questionSlider.getValue()));
      });
      
      countRow.add(sliderLabel);
      countRow.add(sliderValue);
      countRow.setAlignmentX(LEFT_ALIGNMENT);
      
      questionSlider.setAlignmentX(LEFT_ALIGNMENT);
      
      setupPanel.add(countRow);
      setupPanel.add(questionSlider);
      setupPanel.add(Box.createVerticalStrut(10));
      
      // checkboxes for question type
      mcqCheck = new JCheckBox("Multiple Choice");
      frqCheck = new JCheckBox("Free Response");
      mcqCheck.setSelected(true);
      frqCheck.setSelected(true);
      
      mcqCheck.setAlignmentX(LEFT_ALIGNMENT);
      frqCheck.setAlignmentX(LEFT_ALIGNMENT);
      
      setupPanel.add(new JLabel("Question types:"));
      setupPanel.add(mcqCheck);
      setupPanel.add(frqCheck);
      
      // show the dialog and wait for input
      int result = JOptionPane.showConfirmDialog(
         this,
         setupPanel,
         "Quiz Setup",
         JOptionPane.OK_CANCEL_OPTION,
         JOptionPane.PLAIN_MESSAGE
      );
      
      // if the user cancels, keep defaults (full length, both types)
      if (result != JOptionPane.OK_OPTION) {
         totalQuestions = order.size();
         allowMultipleChoice = true;
         allowWritten = true;
         return;
      }
      
      // use the slider value for total questions
      totalQuestions = questionSlider.getValue();
      
      // read checkbox state
      allowMultipleChoice = mcqCheck.isSelected();
      allowWritten = frqCheck.isSelected();
      
      // if both are unchecked, default to both
      if (!allowMultipleChoice && !allowWritten) {
         allowMultipleChoice = true;
         allowWritten = true;
      }
   }
   
   /**
    * loadQuestion sets up the UI for the current question.
    */
   private void loadQuestion() {
      // check if quiz is finished
      // if we are out of questions, show the results instead
      if (currentIndex >= totalQuestions) {
         showResults();
         return;
      }
      
      // reset feedback and next button
      // the user must answer before continuing
      feedbackLabel.setText(" ");
      nextButton.setEnabled(false);
      
      // decide question type: alternate multiple choice and written
      // even = multiple choice, odd = written response
      if (allowMultipleChoice && !allowWritten) {
         currentIsMultipleChoice = true;
      }
      else if (!allowMultipleChoice && allowWritten) {
         currentIsMultipleChoice = false;
      }
      else {
         if (currentIndex % 2 == 0) {
            currentIsMultipleChoice = true;
         }
         else {
            currentIsMultipleChoice = false;
         }
      }
      
      // randomly choose which side to ask using Math.random()
      // true = ask key, false = ask definition
      if (Math.random() < 0.5) {
         currentAskKey = true;
      }
      else {
         currentAskKey = false;
      }
      
      // pick the pair for this question using the shuffled order
      int pairIndex = order.get(currentIndex);
      // set the prompt and answer based on the direction
      String prompt;
      if (currentAskKey) {
         prompt = keys.get(pairIndex);
         currentAnswer = defs.get(pairIndex);
      }
      else {
         prompt = defs.get(pairIndex);
         currentAnswer = keys.get(pairIndex);
      }
      
      // update the question label based on the direction
      if (currentAskKey) {
         instructionLabel.setText("Match the key to its definition.");
         questionLabel.setText("Define: " + prompt);
      }
      else {
         instructionLabel.setText("Match the definition to its key.");
         questionLabel.setText("Key for: " + prompt);
      }
      
      // show progress so the user knows where they are
      progressLabel.setText((currentIndex + 1) + " / " + totalQuestions);
      
      // show the correct UI for this question type
      if (currentIsMultipleChoice) {
         setupMultipleChoice();
      }
      else {
         setupWritten();
      }
   }
   
   /**
    * setupMultipleChoice builds the choice buttons for this question.
    */
   private void setupMultipleChoice() {
      // show multiple choice and hide written response
      choicePanel.setVisible(true);
      writtenPanel.setVisible(false);
      
      // build a list of choices (correct + distractors)
      // also track used answers to avoid duplicates
      ArrayList<String> choices = new ArrayList<String>();
      HashSet<String> used = new HashSet<String>();
      choices.add(currentAnswer);
      used.add(currentAnswer.toLowerCase());
      
      // choose distractors from the same side as the answer
      // choose up to 4 choices, but not more than the set size
      int maxChoices = Math.min(4, keys.size());
      while (choices.size() < maxChoices) {
         // pick a random index from the set
         int idx = (int) (Math.random() * keys.size());
         String option;
         if (currentAskKey) {
            option = defs.get(idx);
         }
         else {
            option = keys.get(idx);
         }
         
         // skip duplicates so all options are unique
         if (used.contains(option.toLowerCase())) {
            continue;
         }
         
         used.add(option.toLowerCase());
         choices.add(option);
      }
      
      // shuffle the choices so the correct answer is not always first
      Collections.shuffle(choices);
      
      // update the buttons
      // hide extra buttons if there are fewer than 4 choices
      for (int i = 0; i < choiceButtons.size(); i++) {
         JButton btn = choiceButtons.get(i);
         if (i < choices.size()) {
            btn.setText(choices.get(i));
            btn.setEnabled(true);
            btn.setVisible(true);
            // reset background so previous highlights do not carry over
            btn.setBackground(defaultChoiceColor);
         }
         else {
            btn.setVisible(false);
         }
      }
      
      // track which button is correct so we can highlight it later
      correctChoiceIndex = -1;
      for (int i = 0; i < choiceButtons.size(); i++) {
         JButton btn = choiceButtons.get(i);
         if (!btn.isVisible()) {
            continue;
         }
         if (btn.getText().equals(currentAnswer)) {
            correctChoiceIndex = i;
            break;
         }
      }
   }
   
   /**
    * setupWritten shows the written input and clears the field.
    */
   private void setupWritten() {
      // show written response and hide multiple choice
      choicePanel.setVisible(false);
      writtenPanel.setVisible(true);
      
      // reset input state
      // make sure the user can type a fresh answer
      answerField.setText("");
      answerField.setEditable(true);
      submitButton.setEnabled(true);
   }
   
   /**
    * checkAnswer compares a response to the current answer.
    */
   private boolean checkAnswer(String response) {
      // normalize spacing and casing before comparison
      String guess = response.trim();
      String answer = currentAnswer.trim();
      
      return guess.equalsIgnoreCase(answer);
   }
   
   /**
    * handleAnswer processes an answer and updates score/feedback.
    */
   private void handleAnswer(String response) {
      // check if the answer is correct
      boolean correct = checkAnswer(response);
      if (correct) {
         // add to score and show positive feedback
         score++;
         feedbackLabel.setText("Correct!");
      }
      else {
         // show the correct answer so the user can learn
         feedbackLabel.setText("Incorrect. Answer: " + currentAnswer);
      }
      
      // enable next so the user can continue
      nextButton.setEnabled(true);
   }
   
   /**
    * showResults displays the final score and hides quiz controls.
    */
   private void showResults() {
      // show the final score and hide input controls
      questionLabel.setText("Quiz complete!");
      instructionLabel.setText(" ");
      progressLabel.setText("Score: " + score + " / " + totalQuestions);
      feedbackLabel.setText(" ");
      
      choicePanel.setVisible(false);
      writtenPanel.setVisible(false);
      nextButton.setEnabled(false);
   }
   
   /**
    * setupKeyBinds adds Enter as a shortcut for Next.
    */
   private void setupKeyBinds() {
      // use WHEN_IN_FOCUSED_WINDOW so the key works anywhere in this panel
      InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
      ActionMap actionMap = this.getActionMap();
      
      inputMap.put(KeyStroke.getKeyStroke("ENTER"), "next");
      
      actionMap.put("next", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            // only move forward if Next is enabled
            if (nextButton.isEnabled()) {
               currentIndex++;
               loadQuestion();
            }
         }
      });
   }
   
   /** Override for actionPerformed */
   @Override
   public void actionPerformed(ActionEvent e) {
      String message = e.getActionCommand();
      
      if (message.equals("next")) {
         // move to the next question
         currentIndex++;
         loadQuestion();
      }
      else if (message.equals("back")) {
         // return to the catalog view
         MainFrame.switchScreen("catalog");
      }
      else if (message.equals("submit")) {
         // handle written response
         // lock the field so the answer cannot be edited
         String response = answerField.getText();
         answerField.setEditable(false);
         submitButton.setEnabled(false);
         handleAnswer(response);
      }
      else if (message.startsWith("choice:")) {
         // handle multiple choice selection
         JButton source = (JButton) e.getSource();
         String response = source.getText();
         
         // disable all choices after selection
         // prevents clicking multiple answers
         for (int i = 0; i < choiceButtons.size(); i++) {
            choiceButtons.get(i).setEnabled(false);
         }
        
         // color the chosen button red if incorrect
         if (!response.trim().equalsIgnoreCase(currentAnswer.trim())) {
            source.setBackground(Color.RED);
         }
         
         // color the correct answer green
         if (correctChoiceIndex >= 0 && correctChoiceIndex < choiceButtons.size()) {
            choiceButtons.get(correctChoiceIndex).setBackground(Color.GREEN);
         }
         
         handleAnswer(response);
      }
   }
}
