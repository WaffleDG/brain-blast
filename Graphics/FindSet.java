import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;


/**
 * This frame can be viewed in BrainBlast. This is a JPanel and is the screen which is going 
 * to be viewed. It has a private JPanel which can be accessed from the HomePanel and allows users to view existing saved flashcard sets. 
 * 
 *
 * <p>
 * Authors: Gregory Cohen and Riya Jonnala.
 * 
 * <p>
 * Version: 1.0, 11/21/2025.
 *
 * @author     Gregory Cohen and Riya Jonnala
 * @since      11/21/2025
 * @version    1.0
 * @see        JFrame
 */

public class FindSet extends JFrame {


    public FindSet() { //FindSet constructor - makes FindSet a JScrollPane
        setTitle("Find Set Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); //where should the location be??

        // Create JPanel
        JPanel panelOfSets = new JPanel();
        panelOfSets.setLayout(new GridLayout(100, 5)); // Example: a panel with many rows
        for (int i = 0; i < 100; i++) { //need to make sure that the buttons only come up based on how many sets exist
            panelOfSets.add(new JButton("Button " + (i + 1)));
        }

        // Creates a JScrollPane by passing in the JPanel as the view
        JScrollPane scrollPane = new JScrollPane(panelOfSets);

        // Customize scroll bar policies (defaults are AS_NEEDED)
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); //can always scroll up and down
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); //if needed, scroll right and left

        // Adds the JScrollPane to your frame
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }


/*               find set
               search by name
     ________________________________
     | ____ ____ ____ ____ ____  || | //has all existing sets in it, can scroll to click one (5 in a row, 100 in a column)
     | |__| |__| |__| |__| |___| || |
     | ____ ____ ____ ____ ____  || |
     | |__| |__| |__| |__| |__|  || |
     | ____ ____ ____ ____ ____  || |
     | |__| |__| |__| |__| |__|  || |
     | ____ ____ ____ ____ ____  || |
     | |__| |__| |__| |__| |__|  || |
     | ____ ____ ____ ____ ____  || |
     | |__| |__| |__| |__| |__|  || |
     |______________________________|
     
*/

//find it on the page (all existing sets pop up in the grid with the title in the grid box
// (potential cover pic for each grid box later))
    //click on a set (each is a button)
    //open to see options of what you can do with the set (can edit, quiz (given key or given definition), review)
    //each is a button which then leads to a new panel

//button to click to access an existing set

    // OR

//search by name
   
   
   //scanner input to take name of the set (needs to be exact in terms of spacing, caps doesn't matter)
      public void getASearchedName () {
         Scanner console = new Scanner(System.in);
         System.out.print("Which set are you looking for?");
         String searchedName = console.nextLine();
         //call match on searchedName
     }
   public boolean match (String searchedName) {
       //check against existing list of set names (in folder)


       // String directoryPath = "path/to/your/folder"; // Replace with our actual folder path
       // File folder = new File(directoryPath);

       // if (folder.exists() && folder.isDirectory()) {
       // File[] files = folder.listFiles();
       // }
       // Scanner sc = new Scanner(new File("______")); //gp through file of file names
       //while (sc.hasNext()) {


      for (int i = 0; i < existingSets.size(); i++) {
         if (searchedName.toUppercase().equals(//name of list of sets(i)) {
            return true;
         }
      }
   }



}
