import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

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
   /** The width of the screen. */
   public static final int WIDTH = 800;
   /** The height of the screen. */
   public static final int HEIGHT = 600;

   /**
    * Constructor for the frame.
    */
   public MainFrame() {
      // graphics specifications:
      this.setSize(WIDTH, HEIGHT);
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      this.setTitle("BrainBlast!");
      
      // prepare and add the mainPanel
      prepareMainPanel();
      
      this.add(mainPanel);
   }
   
   /**
    * This function will load all of the other screens (panels) and group them into one.
    */
   public static void prepareMainPanel() {
      // the implemetation will be adding all of the other panels to the mainPanel "Container" and relying on this 
      // panel for it's CardLayout.
      CardLayout cl = new CardLayout();
      mainPanel = new JPanel(cl);
      
      // then add the panels
      mainPanel.add(new HomePanel(), "Home"); // this is the first added, by default will be visible first.
   };
   
}