import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Helper class for listing and loading any saved set.
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
   /** Private file for the loaded set */
   private File setFile;
   /** Private list of keys for the loaded set */
   private ArrayList<String> keys;
   /** Private list of definitions for the loaded set */
   private ArrayList<String> defs;
   
   /**
    * Constructor for a SetRegistry instance.
    */
   public SetRegistry() {
      // initialize empty lists so callers can use them immediately
      keys = new ArrayList<String>();
      defs = new ArrayList<String>();
   }

   /**
    * getSetNames finds all .txt files in the Sets directory and returns their base names.
    */
   public ArrayList<String> getSetNames() {
      // build a list of base file names from the Sets directory
      ArrayList<String> results = new ArrayList<String>();
      
      // find the sets directory
      File setsDir = new File(Paths.SETS_DIR);
      if (!setsDir.exists() || !setsDir.isDirectory()) {
         // no directory means there are no saved sets
         return results;
      }
      
      // list all files inside
      File[] files = setsDir.listFiles();
      if (files == null) {
         // directory couldn't be read
         return results;
      }
      
      // sort for a consistent order
      Arrays.sort(files);
      for (int i = 0; i < files.length; i++) {
         File f = files[i];
         if (!f.isFile()) {
            // skip folders or non-files
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
      
      // return the collected base names
      return results;
   }
   
   /**
    * loadFile takes a filePath and creates or gets the corresponding file.
    */
   public void loadFile(String filePath) {
      // create the File object
      setFile = new File(filePath);
      
      // debug info for file resolution
      System.out.println("Trying to load: " + setFile.getAbsolutePath());
      System.out.println("Exists? " + setFile.exists());
      System.out.println("Is file? " + setFile.isFile());
      
      // create the ArrayList objects
      keys = new ArrayList<String>();
      defs = new ArrayList<String>();
      
      // create the actual file if not created already
      try {
         if (setFile.createNewFile()) { // this will only return true if the file is created.
            // add a standard "key" "def" to the lists.
            keys.add("Key");
            defs.add("Definition");
         }
         else { // if the file already exists we have to read it
            // create a new scanner to read file
            Scanner fileReader = new Scanner(setFile);
            
            // while there is another line to read
            while (fileReader.hasNextLine()) {
               String thisLine = fileReader.nextLine();
               
               // if there is nothing in the line, continue.
               if (thisLine.length() == 0) {
                  continue;
               }
               
               // split by the first tab only; anything after stays in the definition
               String[] parts = thisLine.split("\\t", 2);
               
               // the first object will be the key, and the second object will be the definition (if present).
               keys.add(parts[0]);
               if (parts.length > 1) {
                  defs.add(parts[1]);
               }
               else {
                  defs.add("");
               }
            }
            
            // close the scanner
            fileReader.close();
         }
      }
      catch (IOException ioe) {
         // log the error so we can diagnose file issues
         ioe.printStackTrace();
         System.err.println("Warning: failed to read or create set file: " + setFile.getPath());
         // initialize defaults so UI can still be shown
         if (keys.isEmpty()) keys.add("Key");
         if (defs.isEmpty()) defs.add("Definition");
         // do not terminate the app; show editable empty set instead
      }
   }
   
   /**
    * loadSet reads a set from the Sets directory by name.
    */
   public void loadSet(String setName) {
      // delegate to loadFile using the standard folder
      loadFile(Paths.SETS_DIR + "/" + setName + ".txt");
   }
   
   /**
    * getSetFile returns the most recently loaded file.
    */
   public File getSetFile() {
      // expose the file for saving/renaming
      return setFile;
   }
   
   /**
    * getKeys returns the keys from the most recently loaded set.
    */
   public ArrayList<String> getKeys() {
      // expose the loaded keys
      return keys;
   }
   
   /**
    * getDefs returns the definitions from the most recently loaded set.
    */
   public ArrayList<String> getDefs() {
      // expose the loaded definitions
      return defs;
   }
}
