import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * Helper class for shared UI styling across panels.
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
 */
public class UIStyle {
   /** Base background color for panels. */
   public static final Color BG = new Color(244, 235, 248);
   /** Lighter card background color. */
   public static final Color CARD_BG = new Color(244, 235, 248);
   /** Main accent color for buttons. */
   public static final Color ACCENT = new Color(77, 49, 154);
   /** Light accent color for button text. */
   public static final Color ACCENT_TEXT = new Color(255, 255, 255);
   /** Secondary accent color for soft outlines. */
   public static final Color SOFT_OUTLINE = new Color(210, 198, 232);
   /** Dark outline for pixel-style buttons. */
   public static final Color BUTTON_OUTLINE = new Color(52, 36, 98);
   
   /** Title font for headings. */
   public static final Font TITLE_FONT = new Font("Monospaced", Font.BOLD, 20);
   /** Body font for labels. */
   public static final Font BODY_FONT = new Font("Monospaced", Font.PLAIN, 14);
   /** Button font for actions. */
   public static final Font BUTTON_FONT = new Font("Monospaced", Font.BOLD, 14);
   
   /** Cached dialog icon for popups. */
   private static Icon dialogIcon;
   
   /**
    * stylePanel applies the standard background color.
    */
   public static void stylePanel(JPanel panel) {
      // apply the shared background so screens look consistent
      panel.setBackground(BG);
      panel.setOpaque(true);
   }
   
   /**
    * styleCardPanel applies a lighter background for card-like containers.
    */
   public static void styleCardPanel(JPanel panel) {
      // card panels use a lighter background to stand out
      panel.setBackground(CARD_BG);
      panel.setOpaque(true);
   }
   
   /**
    * styleTitle applies a bold font to titles.
    */
   public static void styleTitle(JLabel label) {
      // titles are bold and use the accent color
      label.setFont(TITLE_FONT);
      label.setForeground(ACCENT);
   }
   
   /**
    * styleLabel applies the standard body font.
    */
   public static void styleLabel(JLabel label) {
      // regular labels get the base body styling
      label.setFont(BODY_FONT);
      label.setForeground(Color.DARK_GRAY);
   }
   
   /**
    * styleButton applies a consistent button look.
    */
   public static void styleButton(JButton button) {
      // apply the shared button font and colors
      button.setFont(BUTTON_FONT);
      button.setBackground(ACCENT);
      button.setForeground(ACCENT_TEXT);
      button.setFocusPainted(false);
      button.setOpaque(true);
      button.setContentAreaFilled(true);
      // add a simple outlined border for the pixel look
      button.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createLineBorder(BUTTON_OUTLINE, 2),
         BorderFactory.createEmptyBorder(6, 10, 6, 10)
      ));
      // keep margins tight so buttons align cleanly in grids
      button.setMargin(new Insets(0, 0, 0, 0));
   }
   
   /**
    * styleButton applies a consistent button look and size.
    */
   public static void styleButton(JButton button, int width, int height) {
      // apply the base button styling first
      styleButton(button);
      // lock the button size so layouts stay predictable
      Dimension size = new Dimension(width, height);
      button.setPreferredSize(size);
      button.setMinimumSize(size);
      button.setMaximumSize(size);
   }
   
   /**
    * showOptionDialog shows a themed option dialog with the app icon.
    */
   public static int showOptionDialog(Component parent, Object message, String title, int optionType, Object[] options, Object initialValue) {
      applyDialogTheme();
      return JOptionPane.showOptionDialog(
         parent,
         message,
         title,
         optionType,
         JOptionPane.PLAIN_MESSAGE,
         getDialogIcon(),
         options,
         initialValue
      );
   }
   
   /**
    * showConfirmDialog shows a themed confirm dialog with the app icon.
    */
   public static int showConfirmDialog(Component parent, Object message, String title, int optionType) {
      applyDialogTheme();
      return JOptionPane.showConfirmDialog(
         parent,
         message,
         title,
         optionType,
         JOptionPane.PLAIN_MESSAGE,
         getDialogIcon()
      );
   }
   
   /**
    * showMessageDialog shows a themed message dialog with the app icon.
    */
   public static void showMessageDialog(Component parent, Object message, String title, int messageType) {
      applyDialogTheme();
      JOptionPane.showMessageDialog(parent, message, title, messageType, getDialogIcon());
   }
   
   /**
    * applyDialogTheme adjusts the OptionPane colors and fonts.
    */
   private static void applyDialogTheme() {
      UIManager.put("OptionPane.background", BG);
      UIManager.put("Panel.background", BG);
      UIManager.put("OptionPane.messageForeground", ACCENT);
      UIManager.put("OptionPane.messageFont", BODY_FONT);
      UIManager.put("OptionPane.buttonFont", BUTTON_FONT);
   }
   
   /**
    * getDialogIcon loads the Brain Blast logo for dialogs.
    */
   private static Icon getDialogIcon() {
      if (dialogIcon != null) {
         return dialogIcon;
      }
      try {
         File logoFile = new File(SetRegistry.ASSETS_DIR, "Brain Blast Logo.png");
         BufferedImage img = ImageIO.read(logoFile);
         if (img != null) {
            dialogIcon = scaleIconToFit(img, 64, 64);
         }
      }
      catch (IOException ioe) {
         dialogIcon = null;
      }
      return dialogIcon;
   }
   
   /**
    * scaleIconToFit keeps the image aspect ratio while fitting a box.
    */
   private static Icon scaleIconToFit(BufferedImage image, int maxW, int maxH) {
      int imgW = image.getWidth();
      int imgH = image.getHeight();
      double scale = Math.min(maxW / (double) imgW, maxH / (double) imgH);
      int w = (int) Math.round(imgW * scale);
      int h = (int) Math.round(imgH * scale);
      Image scaled = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
      return new ImageIcon(scaled);
   }
}
