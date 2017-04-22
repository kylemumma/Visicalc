package visiCalcFinal;

//Kyle Mumma
//APCS Period 1
//Visicalc Checkpoint 2

public class Cell implements Comparable {

	String value;
	String originalInput;
	
	public void setInput(String input){
		originalInput = input;
	}
	
	public String getInput(){
		return originalInput;
	}
	
	public void setValue(String v){
		value = v;
	}
	
	public String getValue(){
		return value;
	}
	
	public String toString(){
		if(value != null){
			return String.format("%10.10s|", getValue());
		} else {
			return "          |";
		}		
	}
	
	public int compareTo(Object o){
		Cell newO = new Cell();
		newO = (Cell) o;
		
		int oValue = Integer.parseInt(newO.getValue());
		int thisValue = Integer.parseInt(getValue());
		
		if(oValue < thisValue){
			return -1;
		} else if(oValue > thisValue){
			return 1;
		}
		
		return 0;
	}
	
}
