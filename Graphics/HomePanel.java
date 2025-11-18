import javax.swing.JPanel;
import javax.swing.JLabel;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics;

/**
 * Defines the Home panel for Brain Blast! This will feature buttons to allow the user to change frames to the
 * "Set creation" screen and the "see other created sets" screen. This is an implementation of a JPanel.
 * It will be loaded in the JFrame that defines the window.
 *
 * <p>
 * Authors: Gregory Cohen and Riya Jonnala.
 *
 * <p>
 * Version: 1.0, 11/17/2025.
 *
 * 
 * @author     Gregory Cohen and Riya Jonalla
 * @since      11/16/2025
 * @version    1.0
 * @see        JPanel
 */
public class HomePanel extends JPanel {
   /** variable to save the background image for this panel. */
   private static BufferedImage backgroundImage;

   /**
    * The HomePanel will be the exact same every time, featuring a background, screen, and buttons as described above.
    */
   public HomePanel() {
      // we're going to load the assets of this image.
      loadAssets();
      
      this.add(new JLabel("to be done"));
      
      this.setVisible(true);
   }
   
   /**
    * This will load all assets (images) for this panel.
    */
   private static void loadAssets() {
      // as with anything that deals with files, IOExceptions need to be caught.
      try {
         // create the file
         File bgSource = new File("Assets/HomePanel Background.png");
         
         // read it as a BufferedImage
         backgroundImage = ImageIO.read(bgSource);
         
         return;
      }
      catch (IOException ioe) {
         System.out.println("Error loading assets");
         ioe.printStackTrace();
      }
   }
   
   /** 
    * Override for the method that controls the panel's appearance to hard-code a background image.
    */
   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g); // there's other stuff that swing needs to handle.
      
      // add the image in the top left corner with the width and height
      g.drawImage(backgroundImage, 0, 0, MainFrame.WIDTH, MainFrame.HEIGHT, this);
   }
}