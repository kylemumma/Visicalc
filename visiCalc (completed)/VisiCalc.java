package visiCalcFinal;

//Kyle Mumma
//APCS Period 1
//Visicalc Checkpoint 3

import java.io.*;
import java.util.*;

public class VisiCalc {

	static Grid cellGrid = new Grid();
	public static boolean isRunning;
	
	public static void main(String[] args) throws FileNotFoundException{
		Scanner scan = new Scanner(System.in);
		File tempSaveFile = new File("tempSave.txt");
		PrintStream tempSave = new PrintStream(tempSaveFile);
		
		//input loop
		isRunning = true;
		while(isRunning){
			System.out.println("What would you like to do?");
			String userInput = scan.nextLine();
			
			tempSave.println(userInput);
			
			if(userInput.contains("=")){
				setCellValue(userInput);
			} else {
				gridCommands(userInput, tempSaveFile);
			}
		}
	}
	
	public static void gridCommands(String userInput, File tempSaveFile) throws FileNotFoundException{
		
		if(userInput.equalsIgnoreCase("quit")){ //user enters quit
			quit();
		} else if(userInput.equalsIgnoreCase("help")){ //user enters help
			help();
		} else if(userInput.equalsIgnoreCase("print")){ //user enters print
			cellGrid.print();
		} else if(userInput.toLowerCase().contains("save")){ //user enters save{}
			if(cellGrid.save(userInput, tempSaveFile) == false){
				System.out.println(userInput + "\n");
			}
		} else if(userInput.toLowerCase().contains("load")){ //user enters load{}
			if(cellGrid.load(userInput) == false){
				System.out.println(userInput + "\n");
			} else {
				System.out.println();
			}
		} else if(userInput.toLowerCase().contains("sortd")){
			cellGrid.sort(userInput, false);
		} else if(userInput.toLowerCase().contains("sorta")){
			cellGrid.sort(userInput, true);
		} else if(userInput.equalsIgnoreCase("clear")){
			cellGrid.clearGrid();
		} else if(userInput.toLowerCase().contains("clear")){
			String accessedCell = userInput.substring(userInput.toLowerCase().indexOf("r") + 2);
			if(cellGrid.clearCell(accessedCell) == false){
				System.out.println(userInput + "\n");
			}
			
		} else if(userInput.length() <= 3){
			if(cellGrid.singleCellInput(userInput) == false){
				System.out.println(userInput + "\n");
			}
		} else {
			System.out.println(userInput + "\n");
		}
	}
	
	//prints the help
	public static void help(){
		
		System.out.println("\nWelcome to Visual Calculator!");
		System.out.println("There are multiple commands you can enter:\n");
		System.out.println("Print - Prints out the spreadsheeet");
		System.out.println("Help - Prints out the help menu");
		System.out.println("Quit - Exits Visicalc");
		System.out.println("Save{SAVENAME} - Saves current visicalc");
		System.out.println("Load{FILENAME} - Loads a previously saved file\n");
		
	}
	
	//quit method
	public static void quit(){
		isRunning = false;
	}
	
	public static void setCellValue(String userInput){
		if(cellGrid.setCellValue(userInput, cellGrid) == false){
			System.out.println(userInput);
		}
	}
	
}
