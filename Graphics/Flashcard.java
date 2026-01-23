import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.border.LineBorder;
import javax.swing.Scrollable;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
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
   /** Fixed card width for wrapping. */
   private static final int CARD_WIDTH = 720;
   /** Fixed card height for layout. */
   private static final int CARD_HEIGHT = 230;
   /** Width used when wrapping text inside the card. */
   private static final int TEXT_WIDTH = 640;
   /** Minimum height to allow vertical centering when text is short. */
   private static final int TEXT_MIN_HEIGHT = 180;
   
   /** Private variable to save the key as a JPanel */
   private CenteredWrapTextPane cardText;
   /** Private variable for the key text */
   private String keyText;
   /** Private variable for the definition text */
   private String defText;
   /** Private variable to keep track of which side is showing */
   private boolean showingKey;

   /** Constructor to take the key def pair */
   public Flashcard(String key, String def) {
      // save text for both sides of the card
      keyText = key;
      defText = def;
      // start on the key side by default
      showingKey = true;
      
      // simple vertical layout
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      // white background to feel like a physical flashcard
      this.setBackground(Color.WHITE);
      this.setOpaque(true);
      
      // create and add a text area so long text can wrap
      cardText = new CenteredWrapTextPane(keyText, TEXT_WIDTH);
      // seed with the key text
      cardText.setText(keyText);
      // keep this as read-only content
      cardText.setEditable(false);
      // keep the text transparent so the wrapper controls background
      cardText.setOpaque(false);
      cardText.setFont(UIStyle.BODY_FONT.deriveFont(16f));
      // ensure text contrasts with the white card background
      cardText.setForeground(Color.BLACK);
      // remove default margins so centered text is truly centered
      cardText.setMargin(new Insets(0, 0, 0, 0));
      cardText.setFocusable(false);
      
      // put the text area in a scroll pane for overflow
      CenteredScrollPanel textWrapper = new CenteredScrollPanel(cardText);
      JScrollPane textScroll = new JScrollPane(textWrapper);
      textScroll.setBorder(null);
      textScroll.setOpaque(false);

      // keep the viewport white so text stays visible
      textScroll.getViewport().setOpaque(true);
      textScroll.getViewport().setBackground(Color.WHITE);

      // never allow horizontal scroll; wrap instead
      textScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      
      // only show vertical scroll when content is long
      textScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      
      this.add(textScroll);
      
      // give the card a visible box so it is easy to click
      this.setBorder(BorderFactory.createCompoundBorder(
         new LineBorder(UIStyle.ACCENT, 2, true),
         BorderFactory.createEmptyBorder(10, 10, 10, 10)
      ));
      // keep size consistent across cards
      this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
      
      // clicking the panel or its text flips the card
      MouseAdapter flipListener = new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            // flip the card in place
            flip();
         }
      };
      // let the background, text, and viewport all respond
      this.addMouseListener(flipListener);
      cardText.addMouseListener(flipListener);
      textScroll.getViewport().addMouseListener(flipListener);
   }
   
   /**
    * flip swaps between the key and definition.
    */
   public void flip() {
      // swap the side that is showing
      showingKey = !showingKey;
      if (showingKey) {
         // show the key
         cardText.setText(keyText);
      }
      else {
         // show the definition
         cardText.setText(defText);
      }
   }
   
   /**
    * CenteredScrollPanel centers short text and allows scrolling when text is long.
    * This wrapper exists because a plain JScrollPane won't vertically center short content.
    * It implements Scrollable hints so the viewport behaves like a fixed-height card.
    */
   private static class CenteredScrollPanel extends JPanel implements Scrollable {
      /** The wrapped text pane that handles word wrapping. */
      private final CenteredWrapTextPane textPane;
      
      /**
       * Builds a white, centered container for the wrapped text.
       * The GridBagLayout lets us keep the text centered even when it's short.
       */
      private CenteredScrollPanel(CenteredWrapTextPane textPane) {
         super(new GridBagLayout());
         this.textPane = textPane;
         setOpaque(true);
         setBackground(Color.WHITE);
         
         // Center the text pane in both axes.
         GridBagConstraints gbc = new GridBagConstraints();
         gbc.gridx = 0;
         gbc.gridy = 0;
         gbc.anchor = GridBagConstraints.CENTER;
         add(textPane, gbc);
      }
      
      @Override
      public Dimension getPreferredSize() {
         // Respect the wrapped text height, but never shrink below the minimum.
         Dimension pref = textPane.getPreferredSize();
         int h = Math.max(TEXT_MIN_HEIGHT, pref.height);
         // Width is fixed so the card layout remains consistent.
         return new Dimension(TEXT_WIDTH, h);
      }
      
      @Override
      public Dimension getPreferredScrollableViewportSize() {
         // Gives the scroll pane a stable viewport for consistent centering.
         return new Dimension(TEXT_WIDTH, TEXT_MIN_HEIGHT);
      }
      
      @Override
      public int getScrollableUnitIncrement(java.awt.Rectangle visibleRect, int orientation, int direction) {
         // Small step size keeps scroll feeling like line-by-line movement.
         return 16;
      }
      
      @Override
      public int getScrollableBlockIncrement(java.awt.Rectangle visibleRect, int orientation, int direction) {
         // Larger step size for faster page-like scrolling.
         return 48;
      }
      
      @Override
      public boolean getScrollableTracksViewportWidth() {
         // Always match the viewport width to avoid horizontal scrolling.
         return true;
      }
      
      @Override
      public boolean getScrollableTracksViewportHeight() {
         // If the viewport is taller than content, stretch so text stays centered.
         if (getParent() instanceof javax.swing.JViewport) {
            int viewportH = ((javax.swing.JViewport) getParent()).getHeight();
            return viewportH > getPreferredSize().height;
         }
         // Otherwise allow the scroll pane to handle the height.
         return false;
      }
   }
   
   /** Override for actionPerformed (unused, but required for ActionListener). */
   @Override
   public void actionPerformed(ActionEvent e) {
      // no button actions right now
   }
} 
