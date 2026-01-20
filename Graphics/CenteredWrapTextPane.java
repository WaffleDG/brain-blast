import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.Dimension;

/**
 * Helper class for making easily-centered text-layers, which we use for like everything soooo
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
 * @see        JTextPane
 */
public class CenteredWrapTextPane extends JTextPane {
   /** Maximum width used when wrapping text. */
   private int maxWidth;
   
   /**
    * Build a centered, wrapped text pane for fixed-width layouts.
    */
   public CenteredWrapTextPane(String text, int maxWidth) {
      super();
      // store the wrap width so layout can measure correctly
      this.maxWidth = maxWidth;
      // keep this as display-only text
      setEditable(false);
      setFocusable(false);
      setOpaque(false);
      // set the initial content
      setText(text);
      // align text once the document exists
      applyCenteredStyle();
   }
   
   /**
    * Update the maximum wrap width and refresh layout.
    */
   public void setMaxWidth(int maxWidth) {
      // update the width used by getPreferredSize
      this.maxWidth = maxWidth;
      // re-run layout so containers measure us again
      revalidate();
   }
   
   /**
    * Override setText so alignment is re-applied after updates.
    */
   @Override
   public void setText(String t) {
      // treat null as an empty string so we never show "null"
      super.setText(t == null ? "" : t);
      // re-center paragraphs whenever content changes
      applyCenteredStyle();
   }
   
   /**
    * Return a preferred size that locks wrapping to maxWidth.
    */
   @Override
   public Dimension getPreferredSize() {
      // fallback to default sizing if no width is configured
      if (maxWidth <= 0) {
         return super.getPreferredSize();
      }
      // set an artificial width so the layout engine wraps lines
      setSize(new Dimension(maxWidth, Short.MAX_VALUE));
      // ask Swing for the height it needs at that width
      Dimension pref = super.getPreferredSize();
      // report the fixed width and the computed height
      return new Dimension(maxWidth, pref.height);
   }
   
   /**
    * Apply centered paragraph alignment to the entire document.
    */
   private void applyCenteredStyle() {
      // update paragraph attributes on the whole document
      StyledDocument doc = getStyledDocument();
      SimpleAttributeSet attrs = new SimpleAttributeSet();
      StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_CENTER);
      doc.setParagraphAttributes(0, doc.getLength(), attrs, false);
   }
}
