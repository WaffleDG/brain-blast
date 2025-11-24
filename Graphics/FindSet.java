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
//make it a JScrollPanel

//button to click to access an existing set

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
      //for (int i = 0; i < length of list of sets; i++) {
         //if (searchedName.toUppercase().equals(name of list of sets[i]) {
         return true; 
         // }
      // }
   }
   
// OR


//find it on the page (all existing sets pop up)
   //click on a set (each is a button)
      //when clicked, open to see options of what you can do with the set (can edit, quiz, review)
         //each is a button which then leads to a new panel 
 





}