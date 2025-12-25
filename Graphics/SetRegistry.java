import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Helper class for listing all saved sets.
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
public class SetRegistry {
   /**
    * getSetNames finds all .txt files in the Sets directory and returns their base names.
    */
   public static ArrayList<String> getSetNames() {
      ArrayList<String> results = new ArrayList<String>();
      
      // find the sets directory
      File setsDir = new File(Paths.SETS_DIR);
      if (!setsDir.exists() || !setsDir.isDirectory()) {
         return results;
      }
      
      // list all files inside
      File[] files = setsDir.listFiles();
      if (files == null) {
         return results;
      }
      
      // sort for a consistent order
      Arrays.sort(files);
      for (int i = 0; i < files.length; i++) {
         File f = files[i];
         if (!f.isFile()) {
            continue;
         }
         
         String name = f.getName();
         // only include .txt files
         if (!name.endsWith(".txt")) {
            continue;
         }
         
         // trim off the .txt extension
         results.add(name.substring(0, name.length() - 4));
      }
      
      return results;
   }
}
