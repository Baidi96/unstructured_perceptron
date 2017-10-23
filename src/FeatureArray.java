package baidi;
import java.io.*;
import java.util.HashSet;
public class FeatureArray {
    public static HashSet<String> create(String string,int n,char prevTag,char tag) {
        HashSet<String> array = new HashSet<>();
        //unigram
        if (n > 0) 
        	array.add("2" + string.charAt(n - 1) + tag);
        array.add("1" + string.charAt(n) + tag);
        if (n < string.length() - 1) 
        	array.add("3" + string.charAt(n + 1) + tag);
        //bigram
        if (n > 0 && n < string.length() - 1) 
        	array.add("4" + string.charAt(n-1)+ string.charAt(n + 1) + tag);
        //trigram
        if (n > 0) 
        	array.add("5" + string.substring(n - 1, n + 1) + tag);
        if (n < string.length() - 1) 
        	array.add("6" + string.substring(n, n + 2) + tag);
        //four
        if (n > 0 && n < string.length() - 1) 
        	array.add("7" + string.substring(n - 1, n + 2) + tag);
        
        //other special tags
        String s1 = "０１２３４５６７８９";
        if(s1.indexOf(string.charAt(n))!=-1)
        	array.add("81" + tag);
        String s2 = "零一二三四五六七八九十百千万亿兆";
        if(s2.indexOf(string.charAt(n))!=-1)
        	array.add("82" + tag);
        String s3 = "《》?‘’…、．，“”『』（）.〈〉！〔〕。%";
        if(s3.indexOf(string.charAt(n))!=-1)
        	array.add("83" + tag);
        String s4 = "ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ";
        if(s4.indexOf(string.charAt(n))!=-1)
        	array.add("84" + tag);
        String s5 = "ａｂｃｄｅｆｇｈｉｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ";
        if(s5.indexOf(string.charAt(n))!=-1)
        	array.add("85" + tag);
        array.add("9" + prevTag + tag);
        return array;
    }
}
