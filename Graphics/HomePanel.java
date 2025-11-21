import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
   /** variable for the logo image */
   private static BufferedImage logoImage;

   /**
    * The HomePanel will be the exact same every time, featuring a background, screen, and buttons as described above.
    */
   public HomePanel() {
      // we're going to load the assets of this image.
      loadAssets();
      
      // set the layout to be a vertical layout
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      
      // add some vertical glue to center objects in the layout
      this.add(Box.createVerticalGlue());
      
      ///// Body of HomePanel
      // adding the logoImage, as a smaller scaled instance
      JLabel logoIcon = new JLabel(new ImageIcon(logoImage.getScaledInstance(320, 240, Image.SCALE_SMOOTH)));
      // align it to center
      logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
      this.add(logoIcon);
      
      // adding the create set button
      JButton createSet = new JButton("Create a Set");
      createSet.setPreferredSize(new Dimension(200, 50));
      createSet.setMaximumSize(createSet.getPreferredSize());
      createSet.setAlignmentX(Component.CENTER_ALIGNMENT);
      this.add(createSet);
      
      // adding the view sets button
      JButton viewSets = new JButton("View Sets");
      viewSets.setPreferredSize(new Dimension(200, 50));
      viewSets.setMaximumSize(createSet.getPreferredSize());
      viewSets.setAlignmentX(Component.CENTER_ALIGNMENT);
      this.add(viewSets);
      
      
      // add more vertical glue to center
      this.add(Box.createVerticalGlue());
      
      this.setVisible(true);
   }
   
   /**
    * This will load all assets (images) for this panel.
    */
   private static void loadAssets() {
      // as with anything that deals with files, IOExceptions need to be caught.
      try {
         /// background image
         // create the file
         File bgSource = new File("Assets/HomePanel Background.png");
         
         // read it as a BufferedImage
         backgroundImage = ImageIO.read(bgSource);
         
         /// logo image
         // create the file
         File logoSource = new File("Assets/Brain Blast Logo.png");
         
         // read the file as a bufferedImage
         logoImage = ImageIO.read(logoSource);
         
         return;
      }
      catch (IOException ioe) {
         System.out.println("Error loading assets");
         ioe.printStackTrace();
         
         // do not load program w/o assets
         System.exit(0);
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