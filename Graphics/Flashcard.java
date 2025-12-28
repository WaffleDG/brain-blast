import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
   private JLabel cardLabel;
   /** Private variable for the key text */
   private String keyText;
   /** Private variable for the definition text */
   private String defText;
   /** Private variable to keep track of which side is showing */
   private boolean showingKey;

   /** Constructor to take the key def pair */
   public Flashcard(String key, String def) {
      // save text
      keyText = key;
      defText = def;
      showingKey = true;
      
      // simple vertical layout
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      
      // create and add the label
      cardLabel = new JLabel(keyText);
      cardLabel.setAlignmentX(CENTER_ALIGNMENT);
      this.add(cardLabel);
      
      // give the card a visible box so it is easy to click
      this.setBorder(BorderFactory.createEtchedBorder());
      this.setPreferredSize(new Dimension(600, 180));
      
      // clicking the panel will flip the card
      this.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            flip();
         }
      });
   }
   
   /**
    * flip swaps between the key and definition.
    */
   public void flip() {
      showingKey = !showingKey;
      if (showingKey) {
         cardLabel.setText(keyText);
      }
      else {
         cardLabel.setText(defText);
      }
   }
   
   /** Override for actionPerformed (unused, but required for ActionListener). */
   @Override
   public void actionPerformed(ActionEvent e) {
      // no button actions right now
   }
} 
