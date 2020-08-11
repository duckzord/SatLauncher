package Variables;

import javax.swing.*;


public class VarChecker {
	
	//private static JTextField field;
	private static String name, contents;
	
	/*
	 * This is the constructor, and sets variables that will be used later in the code. Then it
	 * makes a check that all the following functions return true (therefore the variable is
	 * valid), and returns true, which allows the satellite to be entered. If one function returns
	 * false then false is returned to the interface, and a satellite cannot be entered until the
	 * error is corrected.
	 */ 
	public static boolean checkVars(JTextField checkfield, String fieldname){
		//field = checkfield;
		name = fieldname;
		contents = checkfield.getText().trim();

		if(checkInput() && checkIsNum() && checkValidNum()){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static boolean checkVars(String variable, String fieldname){
		//field = checkfield;
		name = fieldname;
		contents = variable.trim();

		if(checkInput() && checkIsNum() && checkValidNum()){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static boolean checkInput(){
		if(contents.isEmpty()){
			System.out.println("Field " + name + " has no variable set, please enter a variable");
			return false;
		}
		else{
			return true;
		}
	}
	
	/*
	 * This function checks whether the variable entered is actually number. It makes the check
	 * by finding the unicode of the character (which is return as a decimal number conversion
	 * of the hexadecimal code) and then checking it is in the correct range (or it is a decimal
	 * point). Also a check at the beginning looks at whether the number starts with a minus sign, 
	 * if it does then the check is started from the second character, otherwise the check is
	 * started from the first character.
	 */
	public static boolean checkIsNum(){
		int start;
		int pointcounter = 0;
		
		if(contents.charAt(0) == '-'){ // Hexadecimal code for minus sign (-)
			start = 1;
		}
		else{
			start = 0;
		}
		for (int i = start ; i < contents.length(); i++){
			if(contents.charAt(i) == '.'){
				pointcounter++;
			}
			else if(contents.codePointAt(i) < 48 || contents.codePointAt(i) > 57 || pointcounter > 1){
				System.out.println("Variable " + name + " is not a number, please enter again");
				return false;
			}
			
		}
		return true;
	}
	
	/*
	 * This function checks whether the number entered for a condition is within a valid range,
	 * or for angles, if they are a valid angle. I am considering making automatic changes here
	 * for angles, but I'm not sure if this will work!
	 */
	public static boolean checkValidNum(){
		if(name == "A" || name == "C"){
			if(Double.parseDouble(contents) < 0 || Double.parseDouble(contents) > 100){
				System.out.println("Field " + name + " is not within the valid range of 0 - 100.");
				return false;
			}
			else{
				return true;
			}
		}
		if(name == "Number of Satellites"){
			if(Double.parseDouble(contents) < 1){
				System.out.println("You can't enter zero or less satellites, or a fraction of a satellite. Please enter a sensible number.");
				return false;
			}
			else{
				return true;
			}
		}
		if(name == "Time"){
			if(Double.parseDouble(contents) < 0){
				System.out.println("Please enter a positive time");
				return false;
				}
			else{
				return true;
			}
			}
		
		else{
			return true;
		}
		
	}
	
}