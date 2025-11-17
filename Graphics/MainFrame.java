import javax.swing.JFrame;
import javas.swing.JPanel;

/**
 * The frame which is going to be viewed as the app BrainBlast. This is a JFrame and is the screen which is going 
 * to be viewed. It has a private JPanel which will be the controller for all of the other panels, including the HomePanel
 * and the SetPanel.
 *
 * <p>
 * Authors: Gregory Cohen and Riya Jonnala.
 * 
 * <p>
 * Version: 1.0, 11/17/2025.
 *
 * @author     Gregory Cohen and Riya Jonnala
 * @since      11/17/2025
 * @version    1.0
 * @see        JFrame
 */
public class MainFrame extends JFrame {
   /** Private panel which is going to hold all the other panels. */
   private static JPanel mainPanel;

   /**
    * Constructor for the frame.
    */
   public MainFrame() {
      // graphics specifications:
      this.setSize(800,600);
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      this.setTitle("BrainBlast!: Home");
      
      // prepare and add the mainPanel
      prepareMainPanel();
      
      this.add(mainPanel);
   }
   
   /**
    * This function will load all of the other screens (panels) and group them into one.
    */
    public static void prepareMainPanel() {
      
    };
   
}