package baidi;
import java.io.*;
import java.util.ArrayList;
public class SentenceArray {
    public String label;
    public String words;
    public SentenceArray(String line) {
        String[] wordsArray = line.split("  ");
        label = "";
        words = "";
        //four tags B M E S
        for (int i = 0; i < wordsArray.length; i++) {
            wordsArray[i] = wordsArray[i].replaceAll(" ","");
            if (wordsArray[i].length() == 0) continue;
            else if (wordsArray[i].length() == 1) 
                label += "3";
            else {
                label += "0";
                for (int j=1;j<wordsArray[i].length()-1;j++) 
                    label += "1";
                label += "2";
            }
            words+= wordsArray[i];
            //System.out.println(words+" "+label);
        }
        System.out.println(words+" "+label);
        
        //two tags 0 1
        /*for (int i = 0; i < wordsArray.length; i++) {
            wordsArray[i] = wordsArray[i].replaceAll(" ","");
            if (wordsArray[i].length() == 0)
                continue;
            else if (wordsArray[i].length() == 1) 
                label += "0";
            else {
                label += "0";
                for (int j=1;j<wordsArray[i].length()-1;j++) 
                    label += "1";
                label += "1";
            }
            words+= wordsArray[i];
            System.out.println(words+" "+label);
        }*/
    }

}
