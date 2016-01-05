//Oct.6, 1998   by Gregor v. Bochmann & Yaoping Wang
// revised by G.v. Bochmann, March 7, 2009
import java.io.*;
import java.util.*;
import java.util.regex.*;
//Extended for part 5 of the Assignment
public class Lexer {
	public final int BEGIN = 1;
	public final int END = 2;
	public final int ASSIGN = 3;
	public final static int PLUS = 4;
	public final static int MINUS = 5;
	public final static int IDENT = 6;
	public final int SEMICOLON = 7;
	public final int EOF = 8;
	public final static int MULTIPLY=9;
	public final static int DIVIDE= 10;
	public final static int EXPONENT= 11;
	public final static int CLOSE = 12;
	public final static int OPEN = 13;
    public final static int NUMBER = 14;
	public String idName;  // identifier name
	public Integer idNum = new Integer(0);
	public int token;  //holds the next token
	private boolean endOfFile; // whether end of file has been encountered

	private BufferedReader input; // input file buffer
	private char c;  //holds the next character
	

	public Lexer(String inputFile){
		try{input = new BufferedReader(new FileReader(inputFile));} 
		catch(IOException ee) {ee.printStackTrace();}
	}

	public void start() throws IOException {
		try {nextChar(); getNextToken();}
			catch (EndOfFileEncountered e) {token = EOF;}
	}

	public void getNextToken()throws IOException {
		if (endOfFile){token = EOF; return;} 
		String terminalString ="";
		try {
			disposeSpace();
			if(Character.isLetter(c)){ //first character is a letter, get whole alphanumeric string
				terminalString += c;
				nextChar();
				while(Character.isLetterOrDigit(c)){terminalString += c; nextChar();}
				idName = terminalString;
				token = checkKeywords(terminalString);
				} 
			 else if (Character.isDigit(c))
             {
                 terminalString += c;
                 nextChar();
                 while(Character.isDigit(c))
                 {
                     terminalString += c;
                     nextChar();
                 }
                 token = NUMBER;
                 idName = terminalString;
                 idNum = Integer.parseInt(terminalString);
             }
			else if (c == '+') {token = PLUS; nextChar();} 
			else if (c == '-') {token = MINUS; nextChar();} 
			else if (c == '*') {//check if next char is '*' to determine multiplication or an exponent ":="
				nextChar();
				if (c == '*') {token = EXPONENT; nextChar();}
				else {token = MULTIPLY; nextChar();}
			}
			else if (c == '/') {token = DIVIDE; nextChar();}
			else if (c == '(') {token = OPEN; nextChar();}
			else if (c == ')') {token = CLOSE; nextChar();}
			else if (c == ';') {token = SEMICOLON; nextChar();} 
			else if (c == ':') { //check that next char is '=' to find assignment token ":=" 
						nextChar(); 
						if (c == '=') {token = ASSIGN; nextChar();}
						else {System.out.println("lexical error: '=' expected after ':'; skip to end of program");
						       skipToEndOfFile();}
													} 
			else {System.out.println("invalid lexical unit; skip to end of program"); skipToEndOfFile();System.exit(1);}
		} catch (EndOfFileEncountered e) {
			endOfFile = true;
			token = (terminalString == "")? EOF : checkKeywords(terminalString);
			}
		}
		
	int checkKeywords(String s) {
		if(s.equals("BEGIN")) return(BEGIN);
		else if(s.equals("END")) return(END);
		else return(IDENT);
		}
	
	 void disposeSpace() throws IOException, EndOfFileEncountered{
		//get rid of all spaces like \t, \n, and blank space
		while(Character.isWhitespace(c)) {nextChar();}
		}

	 void nextChar() throws IOException, EndOfFileEncountered{//get next character
		int i;
		if ((i = input.read()) == -1)throw new EndOfFileEncountered();
		c = (char) i;
			
			System.out.print(c);
			
		}
	
	 void skipToEndOfFile() throws IOException, EndOfFileEncountered {
		while (true) {nextChar();}
		}
	
}
