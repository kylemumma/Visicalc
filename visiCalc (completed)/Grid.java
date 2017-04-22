package visiCalcFinal;

//Kyle Mumma
//APCS Period 1
//Visicalc Checkpoint 3

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Grid {

	private int numberIndex = 0;
	private int letterIndex = 0;
	private static Cell[][] spreadsheet = new Cell[10][7];
	
	public Grid(){
		//inits all spaces in the 2d array as empty cells
		for(int i = 0; i < spreadsheet.length; i++){
			for(int j = 0; j < spreadsheet[i].length; j++){
				spreadsheet[i][j] = new Cell();
			}
		}
	}
	
	public void sort(String userInput, boolean isAscending){
		int i = 0;
		int firstNum = 0;
		int secondNum;
		ArrayList<Cell> cellList = new ArrayList<Cell>();
		Scanner scanUserInput = new Scanner(userInput);
		
		while(scanUserInput.hasNext()){
			String currentToken = scanUserInput.next();
				if(isCell(currentToken)){
					
					String letters = "ABCDEFG";
					int letterIndex = letters.indexOf(currentToken.substring(0, 1).toUpperCase());
					int numberIndex = Integer.parseInt(currentToken.substring(1)) - 1;
					
					if(i == 0){
						firstNum = numberIndex;
						
					} else if(i == 1){
						secondNum = numberIndex;
						for(int k = firstNum; k <= secondNum; k++){
							cellList.add(spreadsheet[k][letterIndex]);
						}
						Object[] cellArr = cellList.toArray();
						Arrays.sort(cellArr);
						if(isAscending){
							for(int l = 0; l < cellArr.length / 2; l++)
							{
							    Cell temp = (Cell) cellArr[l];
							    cellArr[l] = cellArr[cellArr.length - l - 1];
							    cellArr[cellArr.length - l - 1] = temp;
							}
						}
						
						for(int j = firstNum; j <= secondNum; j++){
							spreadsheet[j][letterIndex] = (Cell) cellArr[j - firstNum];
						}
					}
					i++;
				}
		}	
	}
	
	/*
	public void sortA(String userInput){
		Object[] cellArr;
		cellArr = sortD(userInput);
		
		for(int i = 0; i < cellArr.length / 2; i++)
		{
		    Cell temp = (Cell) cellArr[i];
		    cellArr[i] = cellArr[cellArr.length - i - 1];
		    cellArr[cellArr.length - i - 1] = temp;
		}
		
		System.out.println(Arrays.toString(cellArr));
	}
	*/
	
	public Cell getCell(int letterIndex, int numberIndex){
		return spreadsheet[letterIndex][numberIndex];
	}
	
	public boolean singleCellInput(String userInput){
		if(isCell(userInput) == false){
			return false;
		} else {
			System.out.println(spreadsheet[numberIndex][letterIndex].getInput());
			//System.out.println(spreadsheet[numberIndex][letterIndex] instanceof Cell);
			//System.out.println(spreadsheet[numberIndex][letterIndex] instanceof TextCell);
			//System.out.println(spreadsheet[numberIndex][letterIndex] instanceof DateCell);
			//System.out.println(spreadsheet[numberIndex][letterIndex] instanceof FormulaCell);
			return true;
		}
	}
	
	public String returnCellValue(int sentNumberIndex, int sentLetterIndex){
		return spreadsheet[sentNumberIndex][sentLetterIndex].getValue();
	}
	
	public boolean clearCell(String cell){
		if(isCell(cell)){
			spreadsheet[numberIndex][letterIndex].setValue(null);
			return true;
		}
			return false;
	}
	
	public void clearGrid(){
		for(int i = 0; i < spreadsheet.length; i++){
			for(int j = 0; j < spreadsheet[i].length; j++){
				spreadsheet[i][j].setValue(null);
			}
		}
	}
	
	public boolean setCellValue(String userInput, Grid visiGrid){
		//substrings the accessed Cell ex. A2, B7...
		String accessedCell = userInput.substring(0, userInput.indexOf("=") - 1);
		if(accessedCell.length() > 3){
			return false;
		}
		
		if(!isCell(accessedCell)){
			return false;
		}
		
		//substrings the new value of the cell
		String newCellValue = userInput.substring(userInput.indexOf("=") + 2);
		//set new cell value
		if(newCellValue.contains("\"")){
			spreadsheet[numberIndex][letterIndex] = new TextCell();
			newCellValue = newCellValue.substring(1, newCellValue.length() - 1);
		} else if(newCellValue.contains("(")) {
			spreadsheet[numberIndex][letterIndex] = new FormulaCell(visiGrid);
		} else if(newCellValue.contains("/")){
			spreadsheet[numberIndex][letterIndex] = new DateCell();
		} else {
			if(!isNumber(newCellValue)){
				return false;
			}
			spreadsheet[numberIndex][letterIndex] = new Cell();
		}
		spreadsheet[numberIndex][letterIndex].setValue(newCellValue);
		spreadsheet[numberIndex][letterIndex].setInput(userInput);
		
		return true;
	}
	
	public boolean save(String userInput, File tempSaveFile) throws FileNotFoundException{
		PrintStream save;
		
		//substrings the saveName
		String saveName =  userInput.substring(userInput.toLowerCase().indexOf("e") + 2);
		
		//checks if saveName is empty
		if(saveName.equals("")){
			return false;
		}
		
		save = new PrintStream(new File(saveName));
		Scanner scanTempSave = new Scanner(tempSaveFile);
		
		//prints the saved file to a .txt
		while(scanTempSave.hasNextLine()){
			save.println(scanTempSave.nextLine());
		}
		
		System.out.printf("Successfully saved to: \"%s\"\n\n", saveName);
		return true;
	}
	
	public boolean load(String userInput) throws FileNotFoundException{
		
		//runs process file, cancels if load file doesnt exist
		if(processFile(userInput) == false){
			return false;
		}
		
		return true;
	}
	
	public boolean processFile(String userInput) throws FileNotFoundException{
		String loadName =  userInput.substring(userInput.indexOf("d") + 2);
		File saveFile = new File(loadName);
		//checks if load file exists
		if(!saveFile.exists()){
			return false;
		}
		Scanner scanSave = new Scanner(saveFile);
		
		//process load file commands loop
		while(scanSave.hasNextLine()){
			processCommand(scanSave.nextLine(), saveFile);
		}
		
		return true;
	}
	
	public void processCommand(String command, File saveFile) throws FileNotFoundException{
		VisiCalc visiClass = new VisiCalc();
		Scanner scanSave = new Scanner(saveFile);
		
		//processes file commands
		if(command.equalsIgnoreCase("print")){
			print();
		} else if(command.equalsIgnoreCase("help")){
			visiClass.help();
		} else if(command.equalsIgnoreCase("quit")){
			visiClass.quit();
		} else if(command.contains("=")){
			visiClass.setCellValue(command);
		} else if(command.toLowerCase().contains("save")){
			File loadTempSave = new File("loadTempSave.txt");
			PrintStream output = new PrintStream(loadTempSave);
			
			boolean isBeforeSaveCommand = true;
			while(isBeforeSaveCommand){
				String currentLine = scanSave.nextLine();
				if(currentLine.equals(command)){
					isBeforeSaveCommand = false;
				} else {
					output.println(currentLine);
				}
			}
			
			save(command, loadTempSave);
		} else if(command.toLowerCase().contains("load")){
			load(command);
		} else if(command.toLowerCase().contains("sortd")){
			sort(command, false);
		} else if(command.toLowerCase().contains("sorta")){
			sort(command, true);
		} else if(command.equalsIgnoreCase("clear")){
			clearGrid();
		} else if(command.toLowerCase().contains("clear")){
			String accessedCell = command.substring(command.toLowerCase().indexOf("r") + 2);
			if(clearCell(accessedCell) == false){
				System.out.println(command + "\n");
			}
			
		} else if(command.length() <= 3){
			if(singleCellInput(command) == false){
				System.out.println(command + "\n");
			}
		} else {
			System.out.println(command);
		}
	}
	
	public void print(){
		//prints the top outline (a, b, c, d...)
		System.out.print("     |");
		
		String letters = "ABCDEFG";
		for(int i = 0; i < 7; i++){
			char letter = letters.charAt(i);
			System.out.printf("     %c    |", letter);
		}
		
		System.out.println();
		
		topLine();
		
		//prints each cell
		for(int i = 0; i < spreadsheet.length; i++){
			System.out.printf("%4d |", i + 1);
			for(int j = 0; j < spreadsheet[i].length; j++){
				System.out.print(spreadsheet[i][j]);
			}
			System.out.println();
			topLine();
		}
		
		System.out.println();
	}
	
	private void topLine(){
		String topLine = "----------+";
		
		System.out.print("-----+");
		for(int k = 0; k < 7; k++){
			System.out.print(topLine);
		}
		System.out.println();
	}

	private boolean isNumber(String num){
		String letters = "abcdefghijklmnopqrstuvwxyz";
		for(int i = 0; i < num.length(); i++){
			for(int j = 0; j < 26; j++){
				if(letters.charAt(j) == num.charAt(i)){
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean isCell(String cell){
		//substrings just the letter ex. A, B...
		String cellLetter = cell.substring(0,1);
		//substrings just the number ex. 2, 7...
		String cellNumber = cell.substring(1);
		
		//checks if userInput accessed cell number is an ok number
		String[] acceptableNums = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
		
		boolean isNum = false;
		for(String num : acceptableNums){
			if(cellNumber.equals(num)){
				isNum = true;
				numberIndex = Integer.parseInt(cellNumber) - 1;
			}
		}
		if(isNum == false){
			return false;
		}
		
		//checks if userInput accessed cell letter is ok
		String[] acceptableLetters = {"A", "B", "C", "D", "E", "F", "G"};
		
		boolean isAcceptableLetter = false;
		for(int i = 0; i < acceptableLetters.length; i++){
			if(cellLetter.equalsIgnoreCase(acceptableLetters[i])){
				isAcceptableLetter = true;
				letterIndex = i;
			}
		}
		if(isAcceptableLetter == false){
			return false;
		}
		return true;
	}
	
}
