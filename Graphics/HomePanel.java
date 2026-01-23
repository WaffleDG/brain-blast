import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Defines the Home panel for Brain Blast! This will feature buttons to allow the user to change frames to the
 * "Create a Set" screen and the "View Sets" screen. This is an instance of a JPanel.
 * It will be loaded in the JFrame that defines the window. It implements ActionListener for button support.
 *
 * <p>
 * Authors: Gregory Cohen and Riya Jonnala.
 *
 * <p>
 * Version: 1.0, 11/17/2025.
 *
 * 
 * @author     Gregory Cohen and Riya Jonnala
 * @since      11/16/2025
 * @version    1.0
 * @see        JPanel
 */
public class HomePanel extends JPanel implements ActionListener {
   /** variable to save the background image for this panel. */
   private static BufferedImage backgroundImage;
   /** variable for the logo image */
   private static BufferedImage logoImage;

   // The HomePanel will be the exact same every time, featuring a background, screen, and buttons as described above.
   public HomePanel() {
      // load images needed for this screen before layout
      loadAssets();
      
      // use a vertical box layout for easy stacking
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      
      // add some vertical glue to center objects in the layout
      this.add(Box.createVerticalGlue());
      
      // Body of HomePanel
      // add the logo image, scaled without stretching
      JLabel logoIcon = new JLabel(scaleIconToFit(logoImage, 420, 300));
      // center the logo within the column
      logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
      this.add(logoIcon);
      
      // add the create set button
      JButton createSet = new JButton("Create a Set");
      createSet.setPreferredSize(new Dimension(260, 60));
      createSet.setMaximumSize(createSet.getPreferredSize());
      createSet.setAlignmentX(Component.CENTER_ALIGNMENT);
      createSet.setActionCommand("edit");
      UIStyle.styleButton(createSet, 260, 60);
      createSet.setFocusable(false);
      createSet.setFocusPainted(false);
      this.add(createSet);
      
      // add some space between buttons
      this.add(Box.createRigidArea(new Dimension(200, 8)));
      
      // add the view sets button
      JButton viewSets = new JButton("View Sets");
      viewSets.setPreferredSize(new Dimension(260, 60));
      viewSets.setMaximumSize(createSet.getPreferredSize());
      viewSets.setAlignmentX(Component.CENTER_ALIGNMENT);
      viewSets.setActionCommand("catalog");
      UIStyle.styleButton(viewSets, 260, 60);
      viewSets.setFocusable(false);
      viewSets.setFocusPainted(false);
      this.add(viewSets);
      
      // wire up button actions to this panel
      createSet.addActionListener(this); // so this class will listen
      viewSets.addActionListener(this);
      
      // add more vertical glue to center
      this.add(Box.createVerticalGlue());
      
      // show the panel once initialized
      this.setVisible(true);
   }
   
   // This will load all assets (images) for this panel.

   private static void loadAssets() {
      // as with anything that deals with files, IOExceptions need to be caught.
      try {
         // background image
         // create the file
         File bgSource = new File(Paths.ASSETS_DIR, "HomePanel Background.png");
         
         // read it as a BufferedImage
         backgroundImage = ImageIO.read(bgSource);
         
         // logo image
         // create the file
         File logoSource = new File(Paths.ASSETS_DIR, "Brain Blast Logo.png");
         
         // read the file as a bufferedImage
         logoImage = ImageIO.read(logoSource);
         
         // assets loaded successfully
         return;
      }
      catch (IOException ioe) {
         // log the issue so we know why the UI failed
         System.out.println("Error loading assets");
         ioe.printStackTrace();
         
         // do not load program w/o assets
         System.exit(0);
      }
   }
   
   // Override for the actionPerformed method which listens for the messages that are sent by the buttons
   @Override
   public void actionPerformed(ActionEvent e) {
      // forward the action command to the main screen manager
      System.out.println("sent: " + e.getActionCommand());
      
      // let MainFrame switch the visible card
      MainFrame.switchScreen(e.getActionCommand());
   }
   
   //Override for the method that controls the panel's appearance to hard-code a background image.
   @Override
   protected void paintComponent(Graphics g) {
      // let Swing paint the background and child components
      super.paintComponent(g); // there's other stuff that swing needs to handle.
      
      // draw the background image scaled to the panel size
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2.drawImage(backgroundImage, 0, 0, MainFrame.WIDTH, MainFrame.HEIGHT, this);
      g2.dispose();
   }
   
   // scaleIconToFit keeps the logo's aspect ratio while fitting a box.
   private static ImageIcon scaleIconToFit(BufferedImage image, int maxW, int maxH) {
      int imgW = image.getWidth();
      int imgH = image.getHeight();
      double scale = Math.min(maxW / (double) imgW, maxH / (double) imgH);
      int w = (int) Math.round(imgW * scale);
      int h = (int) Math.round(imgH * scale);
      Image scaled = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
      return new ImageIcon(scaled);
   }
}
