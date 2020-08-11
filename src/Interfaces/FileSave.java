package Interfaces;

import java.io.*;
import java.text.DecimalFormat;

public class FileSave{
	private static String newline = System.getProperty("line.separator");
	private static String separator = "=====================================================================================" + newline;
	protected static String savestring;
	protected DecimalFormat df = new DecimalFormat();

	
	public FileSave(File savefile, double[][] satvars, double[] simvars, int numsats){
	    df.setMaximumFractionDigits(6);
	    savestring =  separator + separator;
		savestring += "This is a file that has saved the simulator conditions. Currently" +
				     " only the satellite"  +newline+" conditions are saved." + newline + newline;
		savestring += separator + separator + newline;
		savestring += "------------Setup the global variables used in the program-------------------" + newline + newline;
		savestring += "Number of Satellites: " + numsats + newline + newline + newline;
		savestring += "Launcher Height: " + df.format(simvars[3]) + "m" + newline;
		savestring += "Launcher Radius: " + df.format(simvars[2]) + "m" + newline;
		savestring += "Launcher Altitude: " + df.format(simvars[5]) + "km" + newline;
		savestring += "Launcher Rotation Rates: " + newline +
					"        Roll: " + df.format(simvars[6]) + " rad/s" + newline +
					"        Pitch: " + df.format(simvars[7]) + " rad/s" + newline +
					"        Yaw: " + df.format(simvars[8]) + " rad/s" + newline + newline;
		savestring += "Satellite Width: " + df.format(simvars[4]) + "m" + newline + newline + newline;
		savestring += separator + newline;			
		if(SatSetup.eqtype == "Indav"){
			savestring += "-What are the conditions below specified in. Depending on what the conditions are-" + newline +
			              "------used depends on the format used to specify the actual variables.------" + newline + newline;
			savestring += "Initial Condition Type: Initial Distance and Velocity" + newline + newline;
			savestring += separator + separator + newline;
			for(int i = 0; i<numsats; i++){
				savestring += "Satellite " + (i) + ":" + newline + newline;
				
				savestring += "Satellite position in terms of launcher coordinates - " +
				"Theta (degrees): " + df.format(satvars[i][0]) + ";  "
				+ "z (metres): " + df.format(satvars[i][1]) + ";" + newline + newline;
				savestring += "Velocities (metres/second) - x1: " + df.format(satvars[i][2]) + ";  "
				+ "y1: " + df.format(satvars[i][3]) + ";  " + "z1: " + df.format(satvars[i][4]) + ";" + newline + newline;
				savestring += "Separation Time (seconds): " + df.format(satvars[i][5]) + ";" + newline + newline + newline;
			}
		}
		
		
		try {
			FileOutputStream filestream = new FileOutputStream(savefile);
	
			int stringlength = savestring.length();
			byte[] savearray = new byte[stringlength];
			savearray = savestring.getBytes();
			filestream.write(savearray);
			
			filestream.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}