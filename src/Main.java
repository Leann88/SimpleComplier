//By Leann Labelle
import java.io.*;
import java.util.*;

public class Main {
	private static Syner syn;
	private static String inputFile;
	public static void main (String args[]) throws IOException, EndOfFileEncountered{
		if(args.length != 1) {
			System.out.println("default input is from file exampleInput9.txt");
			inputFile = "TestingFiles/exampleInput9.txt";} else {inputFile = args[0];}
		syn = new Syner(inputFile);
		syn.startAnalysis();
	}
}