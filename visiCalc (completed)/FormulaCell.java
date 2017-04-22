package visiCalcFinal;

import java.util.Scanner;

public class FormulaCell extends Cell{
	
	Grid visiGrid;
	
	public FormulaCell(Grid grid){
		visiGrid = grid;
	}
	
	public String getValue(){
		Scanner scanValueString = new Scanner(value);
		double sum = 0;
		String operator = "+";
		boolean isAverage = false;
		boolean isSum = false;
		int i = 0;
		int firstNum = 0;
		int secondNum;
		
		while(scanValueString.hasNext()){
			String currentToken = scanValueString.next();
			if(isSum){
				
				if(isCell(currentToken)){
					
					String letters = "ABCDEFG";
					int letterIndex = letters.indexOf(currentToken.substring(0, 1).toUpperCase());
					int numberIndex = Integer.parseInt(currentToken.substring(1)) - 1;
					
					if(i == 0){
						firstNum = numberIndex;
						
					} else if(i == 2){
						secondNum = numberIndex;
						for(int k = firstNum; k <= secondNum; k++){
							sum += Double.parseDouble(visiGrid.returnCellValue(k, letterIndex));
						}
					}
				}
				
				i++;
			} else if(isAverage){
				if(isCell(currentToken)){
					
					String letters = "ABCDEFG";
					int letterIndex = letters.indexOf(currentToken.substring(0, 1).toUpperCase());
					int numberIndex = Integer.parseInt(currentToken.substring(1)) - 1;
					
					if(i == 0){
						firstNum = numberIndex;
						
					} else if(i == 2){
						secondNum = numberIndex;
						for(int k = firstNum; k <= secondNum; k++){
							sum += Double.parseDouble(visiGrid.returnCellValue(k, letterIndex));
						}
						sum /= (secondNum - firstNum + 1);
					}
				}
				
				i++;
				
			} else if(currentToken.toLowerCase().equals("sum")){
				isSum = true;
			} else if(currentToken.toLowerCase().equals("avg")){
				isAverage = true;
			} else if(isNumber(currentToken) || isCell(currentToken)){
				if(isCell(currentToken)){
					String letters = "ABCDEFG";
					int letterIndex = letters.indexOf(currentToken.substring(0, 1).toUpperCase());
					int numberIndex = Integer.parseInt(currentToken.substring(1)) - 1;
					
					//currentToken = visiGrid.getCell(letterIndex, numberIndex).getValue();
					
					//System.out.println(visiGrid.getCell(letterIndex, numberIndex) instanceof TextCell);
					//System.out.println(visiGrid.getCell(letterIndex, numberIndex) instanceof Cell);
					
					//if(visiGrid.getCell(letterIndex, numberIndex) instanceof TextCell ||
							//visiGrid.getCell(letterIndex, numberIndex) instanceof DateCell ||
							//visiGrid.getCell(letterIndex, numberIndex).getValue() == null){
						//return "NaN";
					//}
				}
					
				sum = calculate(sum, currentToken, operator);
				
			} else if(isOperator(currentToken)){
				operator = currentToken;
			}
			
		}
		
		return Double.toString(sum);
	}
	
	private boolean isOperator(String input){
		if(input.equals("+") ||
			input.equals("-") ||
			input.equals("*") ||
			input.equals("/")){
			return true;
		}
		return false;
	}
	
	private boolean isNumber(String num){
		String letters = "abcdefghijklmnopqrstuvwxyz`~!@#$%^&*()_-+={}|\\:;\"'<>?,/";
		//for(int i = 0; i < num.length(); i++){
			for(int j = 0; j < letters.length(); j++){
				if(Character.toString(letters.charAt(j)).equals(num)){//.charAt(i)){
					return false;
				}
			}
		//}
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
			}
		}
		if(isAcceptableLetter == false){
			return false;
		}
		return true;
	}

	
	private double calculate(double sum, String currentToken, String operator){
		
		if(isCell(currentToken)){
			String letters = "ABCDEFG";
			int letterIndex = letters.indexOf(currentToken.substring(0, 1).toUpperCase());
			int numberIndex = Integer.parseInt(currentToken.substring(1)) - 1;
			
			currentToken = visiGrid.returnCellValue(numberIndex, letterIndex);
		}
		
		double currentTokenDouble = Double.parseDouble(currentToken);
		
		if(operator.equals("+")){
			sum += currentTokenDouble;
		} else if(operator.equals("-")){
			sum -= currentTokenDouble;
		} else if(operator.equals("*")){	
			sum *= currentTokenDouble;
		} else if(operator.equals("/")){
			sum /= currentTokenDouble;
		}
		
		return sum;
	}
	
}
