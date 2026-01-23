import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

/**
 * The frame which is going to be viewed as the app BrainBlast. This is a JFrame and is the screen which is going 
 * to be viewed. It has a private JPanel which will be the controller for all other panels, including the HomePanel
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
   public static final int WIDTH = 1024;
   /** The height of the screen. */
   public static final int HEIGHT = 768;

   // Constructor for the frame
   public MainFrame() {
      // graphics specifications:
      // keep a fixed-size window for layout stability
      this.setSize(WIDTH, HEIGHT);
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      this.setTitle("BrainBlast!");
      this.setResizable(false);
      
      // prepare and add the mainPanel
      prepareMainPanel();
      
      // attach the card container to the frame
      this.add(mainPanel);
      
      // show the window
      this.setVisible(true);
   }
   
   // This function will load all of the other screens (panels) and group them into one.

   private static void prepareMainPanel() {
      // the implementation adds all screens to the main panel container
      // the CardLayout lets us swap screens by name
      mainPanel = new JPanel(cl);
      
      // then add the panels
      mainPanel.add(new HomePanel(), "home"); // this is the first added, by default will be visible first.
      mainPanel.add(new FindSet(), "catalog");
   
      // mark ready for display
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
      // debug log to track navigation
      System.out.println("recieved: " + target);
      if (target.equals("edit")) {
         // create a fresh edit screen for a new set
         mainPanel.add(new EditSetPanel(), "edit");
      }
      else if (target.equals("catalog")) {
         // reload catalog each time to reflect new/renamed sets
         mainPanel.add(new FindSet(), "catalog");
      }
      
      // flip to the requested card
      cl.show(mainPanel, target);
   }
   
   /** 
    * the loadEditSet method creates a new EditSetPanel with the "edit" target and sets it 
    * to the visible screen. This overrides what was previously held.
    */
   public static void loadEditSet(String setName) {
      // debug log so we know which set is being edited
      System.out.println("recieved: load " + setName);
      // overwrite the "edit" card with the requested set
      mainPanel.add(new EditSetPanel(setName), "edit");
      
      // show the edit screen
      cl.show(mainPanel, "edit");
   }
   
   // the loadLearnSet method creates a new LearnSetPanel and sets it to the visible screen.

   public static void loadLearnSet(String setName) {
      // debug log so we know which set is being studied
      System.out.println("received: learn " + setName);
      // overwrite the "learn" card with the requested set
      mainPanel.add(new LearnSetPanel(setName), "learn");
      
      // show the learn screen
      cl.show(mainPanel, "learn");
   }
   
   // the loadMatchSet method creates a new MatchPanel and sets it to the visible screen.
   public static void loadMatchSet(String setName) {
      // debug log so we know which set is being matched
      System.out.println("received: match " + setName);
      // overwrite the "match" card with the requested set
      mainPanel.add(new MatchPanel(setName), "match");
      
      // show the match screen
      cl.show(mainPanel, "match");
   }
   
   // the loadQuizSet method creates a new QuizPanel and sets it to the visible screen.
   public static void loadQuizSet(String setName) {
      // debug log so we know which set is being quizzed
      System.out.println("recieved: quiz " + setName);
      // overwrite the "quiz" card with the requested set
      mainPanel.add(new QuizPanel(setName), "quiz");
      
      // show the quiz screen
      cl.show(mainPanel, "quiz");
   }
   
   // this is for testing (preferred entry point is BrainBlast.main)
   public static void main(String[] args) {
      // standalone entry for quick testing
      new MainFrame();
   }
}
