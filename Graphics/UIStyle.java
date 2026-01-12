import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
   
   /**
    * stylePanel applies the standard background color.
    */
   public static void stylePanel(JPanel panel) {
      panel.setBackground(BG);
      panel.setOpaque(true);
   }
   
   /**
    * styleCardPanel applies a lighter background for card-like containers.
    */
   public static void styleCardPanel(JPanel panel) {
      panel.setBackground(CARD_BG);
      panel.setOpaque(true);
   }
   
   /**
    * styleTitle applies a bold font to titles.
    */
   public static void styleTitle(JLabel label) {
      label.setFont(TITLE_FONT);
      label.setForeground(ACCENT);
   }
   
   /**
    * styleLabel applies the standard body font.
    */
   public static void styleLabel(JLabel label) {
      label.setFont(BODY_FONT);
      label.setForeground(Color.DARK_GRAY);
   }
   
   /**
    * styleButton applies a consistent button look.
    */
   public static void styleButton(JButton button) {
      button.setFont(BUTTON_FONT);
      button.setBackground(ACCENT);
      button.setForeground(ACCENT_TEXT);
      button.setFocusPainted(false);
      button.setOpaque(true);
      button.setContentAreaFilled(true);
      button.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createLineBorder(BUTTON_OUTLINE, 2),
         BorderFactory.createEmptyBorder(6, 10, 6, 10)
      ));
      button.setMargin(new Insets(0, 0, 0, 0));
   }
   
   /**
    * styleButton applies a consistent button look and size.
    */
   public static void styleButton(JButton button, int width, int height) {
      styleButton(button);
      Dimension size = new Dimension(width, height);
      button.setPreferredSize(size);
      button.setMinimumSize(size);
      button.setMaximumSize(size);
   }
}
