import java.io.File;

/**
 * Helper class for common file paths in BrainBlast.
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
public class Paths {
   /** Directory for saved sets. */
   public static final String SETS_DIR = "Sets";
   /** Directory for assets. */
   public static final String ASSETS_DIR = "Graphics/Assets";

   /**
    * Builds a File object for a set file based on the base name.
    */
   public static File setFile(String setName) {
      // map the set name to a .txt file in the Sets directory
      return new File(SETS_DIR + "/" + setName + ".txt");
   }
}
