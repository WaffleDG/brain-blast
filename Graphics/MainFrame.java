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
   /** Private reference to CardLayout which holds the screens */
   private static CardLayout cl = new CardLayout();
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
      this.setResizable(false);
      
      // prepare and add the mainPanel
      prepareMainPanel();
      
      this.add(mainPanel);
      
      this.setVisible(true);
   }
   
   /**
    * This function will load all of the other screens (panels) and group them into one.
    */
   private static void prepareMainPanel() {
      // the implemetation will be adding all of the other panels to the mainPanel "Container" and relying on this 
      // panel for it's CardLayout.
      mainPanel = new JPanel(cl);
      
      // then add the panels
      mainPanel.add(new HomePanel(), "home"); // this is the first added, by default will be visible first.
      //mainPanel.add(new FindSet(), "catalog");
      
      // how to swap
      // cl.show(mainPanel, "test");
   
      mainPanel.setVisible(true);
   };
   
   /** 
    * switchScreen() takes a string which denotes the target screen and switches to it.
    * This should only be called from panel classes in the same folder.
    * 
    * key:
    *    HomePanel - "home"
    *    EditSetPanel (create) - "edit"
    *    FindPanel - "catalog"
    */
   public static void switchScreen(String target) {
      System.out.println("recieved: " + target);
      if (target.equals("edit")) {
         mainPanel.add(new EditSetPanel(), "edit");
      }
      
      cl.show(mainPanel, target);
   }
   
   /** 
    * the loadEditSet method creates a new EditSetPanel with the "edit" target and sets it 
    * to the visible screen. This overrides what was previously held.
    */
   public static void loadEditSet(String setName) {
      System.out.println("recieved: load " + setName);
      mainPanel.add(new EditSetPanel(setName), "edit");
      
      cl.show(mainPanel, "edit");
   }
   
   // this is for testing (until we add the BrainBlast.java class!)
   public static void main(String[] args) {
      new MainFrame();
   }
}