import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Flashcard class is a helper class for the LearnSetPanel. It takes a key-def pair and creates a Jpanel which will "flip" the answer upon being clicked. 
 * It is a JPanel, and will implement ActionListener to support button use.
 *
 * <p>
 * Authors: Gregory Cohen and Riya Jonnala.
 *
 * <p>
 * Version: 1.0, 12/11/2025.
 *
 * 
 * @author     Gregory Cohen and Riya Jonalla
 * @since      12/11/2025
 * @version    1.0
 * @see        JPanel
 */
public class Flashcard extends JPanel implements ActionListener {
   /** Private variable to save the key as a JPanel */
   private JLabel keyLabel;
   /** Private variable to save the definition as a JPanel */
   private JLabel defLabel;

   /** Constructor to take the key def pair */
   public Flashcard(String key, String def) {
      // define the labels
      keyLabel = new JLabel(key);
      defLabel = new JLabel(def);
      
      
   }
} 