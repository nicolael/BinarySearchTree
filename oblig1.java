import java.io.*;
import java.util.*;

class oblig1{
	public static void main(String[]args){

	Scanner scan = new Scanner(System.in);
	String command="";
	Tree bst = new Tree();
	bst.read_file();
	
	/* kun til testformal
	bst.insert("6");
	bst.insert("3");
	bst.insert("8");
	bst.insert("2");
	bst.insert("4");
	bst.insert("9");
	bst.insert("5");
	//bst.inorder();
	*/

	/*
	* Kommandolokka for menyen
	*/
	while(!command.equals("0")){

		System.out.println("\n------------------Mandatory assigment 1------------------\n");
	
		System.out.println("\nMenu");
		System.out.println("Enter  1 to insert a word");
		System.out.println("Enter  2 to search for a word");
		System.out.println("Enter  3 to remove a word");
		System.out.println("Enter  4 to show statistics");
		System.out.println("Enter  5 to print the tree");
		System.out.println("Enter  0 to exit the program");
		System.out.println("");
		System.out.print("command : ");
		command = scan.next().toLowerCase();

		if(command.equals("1")){
			boolean quit=false;
			while(!quit){
				System.out.print("Insert a word : ");
				String word = scan.next();
				bst.insert(word);
				Node new_word = bst.contains(word,bst.root);
				if(new_word!=null){
					System.out.println();
					System.out.println("The word '"+new_word.nodeData()+"' was inserted in the tree!");
				}
				System.out.print("\nGo back to menu (y|n)? ");
				String answer=scan.next().toLowerCase();
				if(answer.equals("y")){
					quit=true;
				}
			}
		}if(command.equals("2")){

			boolean quit = false;
			while(!quit){
				System.out.print("\nSearch for : ");
				String word = scan.next();
				long startTid = System.currentTimeMillis();
				Node found = bst.contains(word,bst.root);
				if(found!=null){
					long stopTime = System.currentTimeMillis();
     				long elapsedTime = stopTime - startTid;
      				
					System.out.println("\nThe word '"+found.nodeData()+"' was found in the dictionary!\n");
					System.out.println("\n Tidsforbruk i millisek : "+elapsedTime);
				}else{
					System.out.println("\nCould not match your search!..Looking for similar words\n");
					int counter=0;
					
					long startTime = System.currentTimeMillis();
					String [] tst= bst.similarOne(word);
					String [] tst2=bst.letterReplaced(word);
					String [] tst3=bst.letterRemoved(word);
					String [] tst4=bst.frontEndMiddle(word);

					long endTime = System.currentTimeMillis();
					long duration = endTime - startTime;
					String [] results = bst.joinResults(tst,tst2,tst3,tst4);
					boolean match=false;
					
					for(String s:results){
						found = bst.contains(s,bst.root);
						counter++;
						if(found!=null){
							match=true;
							System.out.println("Found :  "+found.nodeData());
						}				
					}
					if(!match){
						System.out.println("\nCould not find any similar words in de dictionary, try again");
					}else{
						System.out.println("\nTime spended generating similar words : "+duration+" milliseconds\n");
					}
					
				}
				System.out.print("\nGo back to menu (y|n)? ");
				String answer=scan.next().toLowerCase();
				if(answer.equals("y")){
					quit=true;
				}
			}

		}if(command.equals("3")){
			
			boolean quit=false;
			while(!quit){
				System.out.print("\nType inn the word you want to delete : ");
				String word = scan.next();
				Node found = bst.contains(word,bst.root);

				if(found!=null){
					bst.remove(word,bst.root);
					System.out.println("\nThe word '"+word+"' was successfully deleted");
				}else{
					System.out.println("\nThe word was not found, try again");
				}
				System.out.print("\nGo back to menu (y|n)? ");
				String answer=scan.next().toLowerCase();
				
				if(answer.equals("y")){
					quit=true;
				}
			}

		}if(command.equals("4")){
			boolean quit = false;
			while(!quit){

				int result = bst.depth(bst.root);
				Node max = bst.findMax(bst.root);
				Node min = bst.findMin(bst.root);
				System.out.println("\n------------------Statistics------------------");
				System.out.println("\nAlphabetically last  word in tree : "+max.nodeData());
				System.out.println("\nAlphabetically first word in tree : " + min.nodeData());
				System.out.println("\nAmount of nodes : "+bst.count(bst.root));
				System.out.println("\nTree's height : " + result);
				double averageDepth = (double) bst.total/bst.noder;
				String formattedData = String.format("%.03f", averageDepth);
			    System.out.println("\nAverage depth of all nodes : "+formattedData);
			    bst.noder=0;
			    bst.total=0;
				int height = bst.depth(bst.root);
				System.out.println();
				System.out.printf("   %5s   %5s ", "LEVEL", "NODES");
				System.out.println();

				for(int k=0; k<height; k++){
					int nodes = bst.levelCount(bst.root,0,k);
					System.out.format("   %2d     %4d",k, nodes);
					System.out.print("\n");
				}
				
				System.out.print("\nGo back to menu (y|n)? ");
				String answer=scan.next().toLowerCase();
				if(answer.equals("y")){
					quit=true;
				}
			}
		}if(command.equals("5")){
			bst.inorder();
		}
	}

	}	
}
class Tree{
	Node root;
	int curr=-1; 
	int depth;
	int total;
	int noder;

	void insert(String data){
		root = insertNode(root,data);
	}
	/*
	* Metoden for å sette inn elementer i treet
	*/
	Node insertNode(Node n, String data){

		if(n==null){
			n= new Node(data); //sjekker om rooten er null
		}else{
			if(data.compareTo(n.word)<0){
				n.left = insertNode(n.left, data);
			}else{
				n.right = insertNode(n.right,data);
			}
		}

		return n;
	}
	/*
	* Metode som søker etter noden med dataen vi er ute etter
	*/
	Node contains(String word, Node n){
	
		while(n!=null){
			int result = word.compareTo(n.word);
			//System.out.println(result);
			if(result<0){
				return contains(word,n.left);
			}else if(result>0){
				return contains(word,n.right);
			}
			return n; //we have a match
		}
		return null;
	}
	/*
	* Metode for å fjerne et element fra treet
	*/
	Node remove(String word, Node n){

		if(n==null){
			return n; //not found
		}
		int result = word.compareTo(n.word);
		if(result<0){
			n.left=remove(word,n.left);
		}else if(result>0){
			n.right=remove(word,n.right);
		}else if(n.left!=null&&n.right!=null){ //har to løvnoder
			n.word = findMin(n.right).word;
			n.right = remove(n.word,n.right);
		}else{
			n=(n.left!=null) ? n.left : n.right;
		}

		return n;
	}

	public void inorder(){ //inorder traversal
         inorder(root);
    }
    /*
    * Traverserer og Printer ut treet
    */
    public void inorder(Node n){ //inorder traversal
         if (n != null)
         {
             inorder(n.left);
             System.out.println(n.nodeData());
             inorder(n.right);
         }
     }

    /*
    *Metoden som leser inn dataen fra filen og setter det i treet 
    */
	public void read_file(){

		File file = new File("dictionary.txt"); //leser tekstfilen
		int count_lines = 0;
		try{
			Scanner input = new Scanner(file);
			while(input.hasNextLine()){
				String line = input.nextLine();
				insert(line); //kaller på metoden
				count_lines++;								
			}
			input.close();
		}catch(FileNotFoundException fne){
		
			System.out.println("dictionary.txt was not found, please locate the file")
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
		System.out.println(count_lines + " words were successfully inserted into the data structure!");
	}
	


	//Regner ut hoyden på treet
	public int depth(Node n){ 
	
		if (n != null){
			curr++;
		    total+=curr;
		    noder++;
		         
		    // samler inn hvis curr er hoyere 
		    if (curr > depth) {
		        depth = curr;
		    }
		 
		    // traverserer treet
		    depth(n.left);
		    depth(n.right);
		         
		    curr--;
		}
		    return depth;
	
	}
	//Finner det storste elementet (alfabetisk) i treet
	public Node findMax(Node n){ 
		if(n!=null){
			while(n.right!=null){
				n=n.right;
			}
		}
		return n;
	}
	//Finner den minste elementet i trett
	public Node findMin(Node n){
		if(n!=null){
			while(n.left!=null){
				n=n.left;
			}
		}
		return n;
	}

	public String[] similarOne(String word){
	    char[] word_array = word.toCharArray();
	    char[] tmp;
	    String[] words = new String[word_array.length-1];
	    for(int i = 0; i < word_array.length - 1; i++){
	        tmp = word_array.clone();
	        words[i] = swap(i, i+1, tmp);
		}
	    return words;
	}
	public String swap(int a, int b, char[] word){
	    char tmp = word[a];
	    word[a] = word[b];
	    word[b] = tmp;
	    return new String(word);
	}
	public String[] letterReplaced(String word){

		char [] tmp = word.toCharArray();
		String abc = "abcdefghijklmnopqrstuvwxyz";
		String[] words = new String[tmp.length*26];
		char temp=' ';
		int t=0;

		for(int i=0; i<tmp.length; i++){
			temp=tmp[i];
			
			for(int j=0; j<abc.length(); j++){
				tmp[i] = abc.charAt(j);
				String b = new String(tmp);
				words[t++]=b;
			}
			tmp[i]=temp;
		
		}
		return words;
	}
	public  String[] letterRemoved(String input) {
	
		String abc = "abcdefghijklmnopqrstuvwxyz";
		String word = input;
		char [] chars = word.toCharArray();	
		char [] tmp = new char[chars.length+1];
		String[] words = new String[(chars.length+1)*26];
		int index=0;
	
		for(int i=0; i<=chars.length; i++){
			
			for(int j=0; j<abc.length();j++){
				tmp[i] = abc.charAt(j);
				int teller=i+1;
				for(int c=i; c<chars.length; c++){
					if(teller<tmp.length){
					tmp[teller]=chars[c];
					}
					teller++;
				}
				String b = new String(tmp);
				words[index++]=b;
			}
			if(i+1<tmp.length){
				char temp = tmp[i+1];
				tmp[i]=temp;
			}
		}
		return words;
	}

	/*
	*A word identical to the input, except one letter has been added in front, at the end, 
	*or somewhere in the middle of the word. 
	*(Example search term: heiy; generated words: eiy, hiy, hey and hei
	*/
	public String [] frontEndMiddle(String word){
		char [] chars = word.toCharArray();
		char [] tmp = new char[chars.length-1];
		String [] words = new String[chars.length];

		for(int i=0; i<chars.length; i++){
			
			char temp=chars[i];
			for(int j=i; j<tmp.length; j++){
				
				tmp[j]=chars[j+1];
			}
			String b = new String(tmp);
			words[i] = b;
			if(i<tmp.length){
				tmp[i]=temp;
			}
		}

		return words;
	}
	/*
	*Samler inn alle resultatene fra metodene som finner lignende ord
	*/
	public String[] joinResults(String[]a,String[]b,String[]c,String[]d){
		String [] results = new String[a.length+b.length+c.length+d.length];
		int k=0;
		for(String s : a){
			results[k++]=s;
		} 
		for(String t : b){
			results[k++]=t;
		}
		for(String u : c){
			results[k++]=u;
		}
		for(String v :d){
			results[k++]=v;
		}
		return results;
	}
	
	/*
	*Metode som teller antall noder i treet
	*/
	int count(Node n){
		int c=1;
		if(n==null){
			return 0;
		}else{

			c+=count(n.left);
			c+=count(n.right);

			return c;
		}
	}
	/*
	*Metode som teller antall noder i en gitt hoyde
	*/
	public int levelCount(Node n, int curr, int d){

		if(n==null){
			return 0;
		}
		if(curr== d){
			return 1;
		}

		return levelCount(n.left,curr+1,d)+levelCount(n.right,curr+1,d);
	}

}
//Node klassen, inneholder dataen som skal inn i noden samt en venstre og høyre subnode
class Node{
	String word; //plass holder for strengen
	Node left; //venstre node
	Node right; // hoyre node

	Node(String word){
		this.word = word;
	}

	/*
	* Metode som returnerer noden sin data
	*/
	String nodeData(){ 
		return word;
	}

}