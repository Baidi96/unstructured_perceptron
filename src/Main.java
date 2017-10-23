package baidi;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

import java.util.HashSet;
import java.util.HashMap;


public class Main {
	@SuppressWarnings("unchecked")
	static ArrayList<SentenceArray> trainArray = new ArrayList();
	static HashMap<String, Integer> theta = new HashMap<>();//unstructured model
	static HashMap<String, Double> theta2 = new HashMap<>();// average model
	static HashMap<String, Double> totalTheta2 = new HashMap<>();// average model
	final static int LOOP = 50;
	static int count = 0;
	
	 public static void main(String[] args){
		 //path define
		 String testName = "test/test.txt";
		 //String testAnswerName = "test/test.answer.txt";
		 String trainName = "test/train.txt";
		 String myAnswerName = "test/myanswer_4tag_50_original.txt";
		 String myAnswerNameAverage =  "test/myanswerAverage_4tag_1_original.txt";
		 
		 try {
			run(trainName, testName, myAnswerName);
			//runAverage(trainName, testName, myAnswerNameAverage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 
	//use for insert char in string in specific position in method decode
	 static String insertStringInParticularPosition(String src, String dec, int position){ 
		 StringBuffer stringBuffer = new StringBuffer(src);                               
		 return stringBuffer.insert(position, dec).toString();
	}
	 
	 //get the calculated tag of a word
	 static char getTag(String s,int n,char prevTag){
		 int value = -(1<<30);
		 char tag = '#';
		 char pass = '0';
		 for(int i=0;i<4;i++){
			 int weight = 0;
			 HashSet<String> feature = FeatureArray.create(s,n,prevTag, pass);
			 for(String str:feature)
				 weight +=theta.getOrDefault(str,0);
			 if(weight>value){
				 value = weight;
				 tag = pass;
			 }
			 pass++;
		 }
		 //System.out.println("this tag:"+tag);
		 return tag;
	 }
	 //for average model
	 static char getTag2(String s,int n,char prevTag){
		 int value = -(1<<30);
		 char tag = '#';
		 char pass = '0';
		 for(int i=0;i<4;i++){
			 int weight = 0;
			 HashSet<String> feature = FeatureArray.create(s,n,prevTag, pass);
			 for(String str:feature)
				 weight +=theta2.getOrDefault(str,0.0);
			 if(weight>value){
				 value = weight;
				 tag = pass;
			 }
			 pass++;
		 }
		 //System.out.println("this tag:"+tag);
		 return tag;
	 }
	 // new trial needed structure
	 static int[][] path = new int[10010][4];// 4tags B0 M1 E2 S3
     static int[][] finall = new int[10010][4];// 4tags
	 static double[][] finall2 = new double[10010][4];// 4tags of average model
	 //get tag of a character
	 static String decode(String string) {
		 int len =string.length();
		 String label="";
		 if(len==0) return label;
		 char prevTag = '#';
		 for(int k=0;k<len;k++){
			 char cntBest = getTag(string,k,prevTag);
			 label+=cntBest;
			 prevTag=label.charAt(k);
		 }
		 return label;
		 //new trial, get label of a sentence
		 /*char pass = '0';
	        for (int i=0;i<4;i++) {
	        	int weight = 0;
	        	HashSet<String> feature = FeatureArray.create(string,0, '#', pass);
	            for(String s:feature)
	            	weight+=theta.getOrDefault(s,0);
	            finall[0][i] = weight;
	            pass++;
	        }
	        int len = string.length();
	        for (int i=1;i<len;i++) {
	            for (int j=0;j<4;j++) {
	            	finall[i][j] = -(1<<30);
	                for (int k=0;k<4;k++) {
	                	int weight = 0;
	                	HashSet<String> feature2 = FeatureArray.create(string,i,(char) ('0'+k),(char) ('0'+j));
	                	for(String s1:feature2)
	                		weight+=theta.getOrDefault(s1, 0);
	                	int res = weight;
	                	int sum = finall[i - 1][k] + res;
	                    if (sum - finall[i][j]>0) {
	                        path[i][j] = k;
	                    	finall[i][j] = sum;
	                    }
	                }
	            }
	        }
	        int len2 = string.length() - 1;
	        int max = 0;
	        for (int i=0;i<4;i++) {
	            if (finall[len2][i] > finall[len2][max])
	                max = i;
	        }
	        String str = "";
	        while (len2 >= 0) {
	        	String str2 = String.valueOf((char)(max+'0'));
	        	str = insertStringInParticularPosition(str,str2,0);
	            max = path[len2][max];
	            len2--;
	        };
	        //System.out.println(rev1);
	        //System.out.println(s);
	        return str;*/
	    }
	 
	 ////get tag of a character for average model
	 static String decode2(String string) {
		 //low score
		 int len =string.length();
		 String label="";
		 if(len==0) return label;
		 char prevTag = '#';
		 for(int k=0;k<len;k++){
			 char cntBest = getTag2(string,k,prevTag);
			 label+=cntBest;
			 prevTag=label.charAt(k);
		 }
		 return label;
		 //new trial, high score
		 /*char pass = '0';
	        for (int i=0;i<4;i++) {
	        	double weight = 0;
	        	HashSet<String> feature = FeatureArray.create(string,0, '#', pass);
	            for(String s:feature)
	            	weight+=theta2.getOrDefault(s,0.0);
	            finall2[0][i] = weight;
	            pass++;
	        }
	        int len = string.length();
	        for (int i=1;i<len;i++) {
	            for (int j=0;j<4;j++) {
	            	finall2[i][j] = -(1<<30);
	                for (int k=0;k<4;k++) {
	                	double weight = 0;
	                	HashSet<String> feature2 = FeatureArray.create(string,i,(char) ('0'+k),(char) ('0'+j));
	                	for(String s1:feature2)
	                		weight+=theta2.getOrDefault(s1, 0.0);
	                	double res = weight;
	                	double sum = finall2[i - 1][k] + res;
	                    if (sum - finall2[i][j]>0) {
	                        path[i][j] = k;
	                    	finall2[i][j] = sum;
	                    }
	                }
	            }
	        }
	        int len2 = string.length() - 1;
	        int max = 0;
	        for (int i=0;i<4;i++) {
	            if (finall2[len2][i] > finall2[len2][max])
	                max = i;
	        }
	        String str = "";
	        while (len2 >= 0) {//len2 = string.length() - 1;
	        	String str2 = String.valueOf((char)(max+'0'));
	        	str = insertStringInParticularPosition(str,str2,0);
	            max = path[len2][max];
	            len2--;
	        };
	        //System.out.println(rev1);
	        //System.out.println(s);
	        return str;*/
	    }
	 
	 //train and test 
	 static void run(String trainName,String testName,String myAnswer)throws IOException{
		 //train
		 BufferedReader reader = null;
		 final File trainFile = new File(trainName);
		 reader = new BufferedReader(new FileReader(trainFile));
		 String line = null;
		 while((line = reader.readLine()) != null){
			 trainArray.add(new SentenceArray(line));
		 }
		 reader.close();
		 for(int i=0;i<LOOP;i++){
			 int size = trainArray.size();
			 for(int j=0;j<size;j++){
				 SentenceArray thisOne = trainArray.get(j);
				 char prevTag = '#';
				 int len = thisOne.words.length();
				 if(len==0) continue;
				 for(int k=0;k<len;k++){
					 char cntBest = getTag(thisOne.words,k,prevTag);
					 if(cntBest!=thisOne.label.charAt(k)){//classification error
						 HashSet<String> feature1 = FeatureArray.create(thisOne.words,k,prevTag,cntBest);
						 Iterator<String> it1 = feature1.iterator();
						 while(it1.hasNext()){
							 String str = it1.next();
							 Integer num = theta.getOrDefault(str, 0)-1;
							 if(num==0) theta.remove(str);
							 else theta.put(str, num);
						 }
						HashSet<String> feature2 = FeatureArray.create(thisOne.words,k,prevTag,thisOne.label.charAt(k));
						Iterator<String> it2 = feature2.iterator();
						while(it2.hasNext()){
							String str = it2.next();
							Integer num = theta.getOrDefault(str, 0)+1;
							//if(num==0) theta.remove(str);
							theta.put(str, num);
						}
					 }
					 prevTag = thisOne.label.charAt(k);
				 }
			 }
		 }
		 /*BufferedWriter writer = null;
		 final File trainResult = new File("test/trainResult.txt");
		 writer = new BufferedWriter(new FileWriter(trainResult));
		 for(String s:theta.keySet())
			 writer.write(s+" "+theta.get(s)+"\n");
		 writer.close();*/
		 
		 //test
		 final File testFile = new File(testName);
		 BufferedReader reader2 = null;
		 reader2 = new BufferedReader(new FileReader(testFile));
		 BufferedWriter writer2 = null;
		 final File myAnswerFile = new File(myAnswer);
		 writer2 = new BufferedWriter(new FileWriter(myAnswerFile));
		 while((line = reader2.readLine()) != null){
			 int len2 = line.length();
			 String strtag = decode(line);
			 for(int p=0;p<len2;p++){
				 if(strtag.charAt(p)=='0' ||  strtag.charAt(p)=='3'){
					 if(p>0) writer2.write(" ");
				 }
				 writer2.write(line.charAt(p));
			 }
			 writer2.write("\n");
		 }
		 reader2.close();
		 writer2.close();
	 }
	//train and test of average model
		 static void runAverage(String trainName,String testName,String myAnswer)throws IOException{
			 //train
			 BufferedReader reader = null;
			 final File trainFile = new File(trainName);
			 reader = new BufferedReader(new FileReader(trainFile));
			 String line = null;
			 while((line = reader.readLine()) != null){
				 trainArray.add(new SentenceArray(line));
			 }
			 reader.close();
			 for(int i=0;i<LOOP;i++){
				 int size = trainArray.size();
				 for(int j=0;j<size;j++){
					 SentenceArray thisOne = trainArray.get(j);
					 //added
					 //char prevTag = '#';
					 int len = thisOne.words.length();
					 if(len==0) continue;
					 // unstructed average,too slow
					 /*for(int k=0;k<len;k++){
						 char cntBest = getTag(thisOne.words,k,prevTag);
						 if(cntBest!=thisOne.label.charAt(k)){//classification error
							 HashSet<String> feature1 = FeatureArray.create(thisOne.words,k,prevTag,cntBest);
							 Iterator<String> it1 = feature1.iterator();
							 while(it1.hasNext()){
								 String str = it1.next();
								 Double num = theta2.getOrDefault(str, 0.0)-1;
								 if(num==0) theta2.remove(str);
								 else theta2.put(str, num);
							 }
							HashSet<String> feature2 = FeatureArray.create(thisOne.words,k,prevTag,thisOne.label.charAt(k));
							Iterator<String> it2 = feature2.iterator();
							while(it2.hasNext()){
								String str = it2.next();
								Double num = theta2.getOrDefault(str, 0.0)+1;
								//if(num==0) theta.remove(str);
								theta2.put(str, num);
							}
						 }
						 prevTag = thisOne.label.charAt(k);
					 }*/
					 
					 String strtag = decode2(thisOne.words);
					 for(int k=0;k<len;k++){
					   HashSet<String> feature1 = null;
					   if(k==0){
						   feature1 = FeatureArray.create(thisOne.words,0,'#',strtag.charAt(0));
					   }
					   else if(k>0){
					       feature1 = FeatureArray.create(thisOne.words,k,strtag.charAt(k-1),strtag.charAt(k));
					   }
					   Iterator<String> it1 = feature1.iterator();
					   while(it1.hasNext()){
						   String str = it1.next();
						   double num = theta2.getOrDefault(str, 0.0)-1;
						   if(num==0) theta2.remove(str);
						   else theta2.put(str, num);
					   }
					 }
					 for(int k=0;k<len;k++){
						   HashSet<String> feature2 = null ;
						   if(k==0){
							   feature2 = FeatureArray.create(thisOne.words,0,'#',thisOne.label.charAt(0));
						   }
						   else if(k>0){
						       feature2 = FeatureArray.create(thisOne.words,k,thisOne.label.charAt(k-1),thisOne.label.charAt(k));
						   }
						   Iterator<String> it2 = feature2.iterator();
						   while(it2.hasNext()){
							   String str = it2.next();
							   double num = theta2.getOrDefault(str, 0.0)+1;
							   //if(num==0) theta2.remove(str);
							   theta2.put(str, num);
						   }
						 }
					 
					 
					/*HashSet<String> feature2 = FeatureArray.create(thisOne.words,k,prevTag,thisOne.label.charAt(k));
					Iterator<String> it2 = feature2.iterator();
					while(it2.hasNext()){
			      		String str = it2.next();
						Integer num = theta.getOrDefault(str, 0)+1;
						//if(num==0) theta.remove(str);
						theta.put(str, num);
					}*/
					 /*for(String s: theta2.keySet()){
						 Double num = theta2.get(s)+totalTheta2.getOrDefault(s, 0.0);
						 if(num==0) totalTheta2.remove(s);
						 else totalTheta2.put(s, num);
					 }
					 count++;*/
				 }
				 //these parts shall be in the loop above but too slow to run
				 for(String s: theta2.keySet()){
					 Double num = theta2.get(s)+totalTheta2.getOrDefault(s, 0.0);
					 if(num==0) totalTheta2.remove(s);
					 else totalTheta2.put(s, num);
				 }
				 count++;

			 }
			 /*BufferedWriter writer = null;
			 final File trainResult = new File("test/trainResult.txt");
			 writer = new BufferedWriter(new FileWriter(trainResult));
			 for(String s:theta2.keySet())
				 writer.write(s+" "+theta2.get(s)+"\n");
			 writer.close();*/
			 
			 //test
			 for(String s:totalTheta2.keySet()){
				 double num = totalTheta2.get(s);
				 num /=count;
				 totalTheta2.put(s, num);
			 }
			 theta2 = totalTheta2;
			 final File testFile = new File(testName);
			 BufferedReader reader2 = null;
			 reader2 = new BufferedReader(new FileReader(testFile));
			 BufferedWriter writer2 = null;
			 final File myAnswerFile = new File(myAnswer);
			 writer2 = new BufferedWriter(new FileWriter(myAnswerFile));
			 while((line = reader2.readLine()) != null){
				 int len2 = line.length();
				 String strtag = decode2(line);
				 for(int p=0;p<len2;p++){
					 if(strtag.charAt(p)=='0' ||  strtag.charAt(p)=='3'){
						 if(p>0) writer2.write(" ");
					 }
					 writer2.write(line.charAt(p));
				 }
				 writer2.write("\n");
			 }
			 reader2.close();
			 writer2.close();
		 }
}
