//By: Leann Labelle
import java.io.*;
import java.util.*;

public class Syner {
	private Lexer lex;
	private Hashtable symbolTable;
   
	private boolean calculated;
    private boolean isNum;
    private int curValue;
    private boolean error=false;
	
	public Syner(String inputFile) {
		lex = new Lexer(inputFile);
		symbolTable = new Hashtable();
		symbolTable.put("zero", new Integer(0) );
		symbolTable.put("one", new Integer(1) );
		symbolTable.put("ten", new Integer(10) );
	}

	public void startAnalysis() throws IOException {
		lex.start(); //start lexical analyser to get a token
		parseProgram(); //call parseProgram() to process the analysis
		//after "END" token, there should be the EOF token
		if(lex.token == lex.EOF) {
			if(!error){
				System.out.println("\n"+"analysis complete, no syntax error");}
			else{System.out.println("\n"+"analysis complete, syntax errors found");}
		}
		else {errorMessage("after END - more tokens before EOF");}			
	}

	public void parseProgram() throws IOException {
		if(lex.token == lex.BEGIN){
			while (true){
				lex.getNextToken();
				parseStatement();
				if(lex.token != lex.SEMICOLON){
					break;
				}
			}
			if (lex.token == lex.END) {lex.getNextToken(); }
			else {errorMessage("END token expected!"); }
		}
		else {
			errorMessage("BEGIN token expected!");
		}
	}
	
	public void parseStatement() throws IOException {
		
		calculated=false; //Used to ensure the token is either a variable in the table 
	    isNum=false; //Used to verify value is indeed a number so a calculation can occur
	    curValue=0; //Stores the current value between calculated
		if (lex.token == lex.IDENT) {
			String var = lex.idName;
			lex.getNextToken(); 
			if (lex.token == lex.ASSIGN) {
				lex.getNextToken();
				int v = parseExpression();
				symbolTable.put(var, new Integer(v) );
				System.out.println("\n"+var+" assign "+v);
			} else {
				errorMessage(" assignment symbol expected");
				System.exit(1);
			}
		} else {
			errorMessage(" identifier expected at the begining of a statement");
		}
	}
	
	public int parseExpression() throws IOException{
		parseOperation();
		parseOperationDoublePrime();
        parseExpressionPrime();
		return curValue;
	}
	
	public int parseExpressionPrime() throws IOException {
		boolean add=false;
		int saveCurrNumber = curValue;
		while(lex.token == Lexer.PLUS || lex.token == Lexer.MINUS){
			 if (lex.token == Lexer.PLUS)
             {
                 add = true;
             }else
             {
                 add = false;
             }
             lex.getNextToken();
             parseOperation();
             parseOperationDoublePrime();
             
             if (calculated && isNum)
             {
				if (add)
                 {
                     curValue += saveCurrNumber;
                 }else
                 {
                     curValue = saveCurrNumber - curValue;
                 }
                 saveCurrNumber = curValue;
             }
         }
		return curValue;
		}

	public int parseOperation() throws IOException{
		 parseValue();
         parseOperationPrime();
		return curValue;
     }
	
	public void parseOperationPrime() throws IOException{
         int saveCurrNumber = curValue;
         
         while (lex.token == Lexer.EXPONENT)
         {
             lex.getNextToken();
             parseValue();
             if (calculated && isNum)
             {
                curValue=(int) Math.pow(saveCurrNumber, curValue);
             }
             
             saveCurrNumber = curValue;
         }
	}
	
	public int parseOperationDoublePrime() throws IOException{
		
		 boolean multiply=false;
		 int saveCurrNumber = curValue;
         while (lex.token == Lexer.MULTIPLY || lex.token == Lexer.DIVIDE)
         {
             if (lex.token == Lexer.MULTIPLY)
             {
                 multiply = true;
             }else
             {
                 multiply=false;
             }
             lex.getNextToken();
             parseValue();
             parseOperationPrime();
             if (calculated && isNum)
             {
                 if (multiply)
                 {
                     curValue = curValue*saveCurrNumber;
                 }else
                 {
                     if (curValue == 0)
                     {
                         errorMessage("Cannot divide by zero.");
                     }
                     curValue = saveCurrNumber/curValue;
                 }
                 saveCurrNumber = curValue;
             }
         }
		return saveCurrNumber;
		
	}
	
	public void parseValue() throws IOException{
		
		if(lex.token==Lexer.IDENT){
			if (symbolTable.containsKey(lex.idName))
            {
                curValue = (Integer) symbolTable.get(lex.idName); 
                calculated=true;
                isNum=true;
            }else
            {
            	errorMessage ( lex.idName + " not declared");
                calculated = false;
            }
			lex.getNextToken();
		}
		else if(lex.token==Lexer.NUMBER){
			curValue = lex.idNum;
			calculated=true;
			isNum=true;
			lex.getNextToken();
		}
		else if(lex.token==Lexer.OPEN){
			lex.getNextToken();
			parseExpression();
			if(lex.token != Lexer.CLOSE){
			errorMessage("Not valid, a \")\" is needed");
			}
			lex.getNextToken();
		}else if(lex.token==lex.END || lex.SEMICOLON == lex.token){
			errorMessage (lex.idName + " value needed");
			
		}
		else{
			errorMessage ("Invalid expression");
		}
		
	}
	public void errorMessage(String s) throws IOException {
		System.out.println("\nError: " + s);
                error = true;
	}
}

