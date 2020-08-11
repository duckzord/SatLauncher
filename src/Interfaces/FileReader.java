package Interfaces;

import java.io.*;
import Variables.VarChecker;

	public class FileReader{
		
		static double[][] filevars;
		static String filedata;
		static String[] satnames;
		static double numsats;
		
		public FileReader(){
		}
		
		public static double[][] getVariables(File file){
			try{
			int filelength = (int)file.length();
			byte[] reader = new byte[filelength];
			FileInputStream filestream = new FileInputStream(file);
			filestream.read(reader);
			filedata = new String(reader);
			int index = filedata.indexOf("Number of Satellites");
			filedata = filedata.substring(index);
			getNumSats(filedata);
			getGlobVars(filedata);
			if(numsats != 0){
			getCondType(filedata);
				if(filevars[0][1] == 2){
					
				}
			
			satnames = new String[(int)numsats];

			for(int i = 0; i < numsats; i++){
				//getSatNames(filedata,i);
				if(!getSatData(filedata,i)){
					filevars[0][0] = i;
					break;
				}
			}
			}
			
			filestream.close();
			}
			catch (Exception e) {
				System.out.println(file.getName() + " cannot be read: " + e.getMessage());
			}
			
			return filevars;
		}
		
		/* 
		 * =======================================================================
		 * This function looks at the number of satellites the user has specified.
		 * Then the appended 'filedata' string is returned. The new 'filedata' is
		 * a shortened version of the data, so not as much processing is needed.
		 * =======================================================================
		 */
		public static boolean getNumSats(String data){
			int count;
			int count2;
			
			count = data.indexOf(':');
			count2 = data.indexOf('\n',count);
			
			numsats = Double.parseDouble(data.substring(count+1,count2));
			
			filevars = new double[((int)numsats+1)][9];
			
			filevars[0][0] = numsats;
			
			filedata = data.substring(count2);
			return true;
		}
		/*
		 * =====================================================================
		 * This function works out what type of initial conditions are specified
		 * by the user. As the vars array is of type double, I will have to
		 * enter it as a number, and then process this number later on.
		 */
		public static boolean getCondType(String data){
			int count;
			int count2;
			
			count = data.indexOf(':');
			count2 = data.indexOf('\n',count);
			
			String condsubstring = data.substring(count, count2);
			if(condsubstring.contains("Epicycle")){
				filevars[0][1] = 0;
			}
			else if(condsubstring.contains("Initial")){
				filevars[0][1] = 1;
			}
			
			filedata = data.substring(count2);
			
			return true;
		}
		/*
		 * This method will find all the global variables that setup the simulation.
		 * As it is not known what order they are in, the variable label is simply
		 * looked for, and the variable is taken from there.
		 */
		public static void getGlobVars(String data){

			filevars[0][2] = findGlobVar("Launcher Radius","m",data);
			filevars[0][3] = findGlobVar("Launcher Height","m",data);
			filevars[0][4] = findGlobVar("Satellite Width","m",data);
			filevars[0][5] = findGlobVar("Launcher Altitude","km",data);
			filevars[0][6] = findGlobVar("Roll","rad/s",data);
			filevars[0][7] = findGlobVar("Pitch","rad/s",data);
			filevars[0][8] = findGlobVar("Yaw","rad/s",data);
		}
		
		static public void getSatNames(String data, int varset){
			int startfrom = data.indexOf("Satellite Input Conditions");
			int count2;
			
			count2 = data.indexOf(':', startfrom);
			satnames[varset] = data.substring(startfrom, count2);	
			
			System.out.println(satnames[0]);
		}
		/*
		 * =============================================================
		 * This function should get the variables, depending on what 
		 * initial conditions were specified.
		 */
		static public boolean getSatData(String data, int varset){

			int count;
			int startfrom = data.indexOf("Theta");
			int count2;
			//int count3;
			//int count4;
			int varcounter = 0;
			
			for(int n=0;n<6;n++){
							
				count = data.indexOf(':',startfrom);
				count2 = data.indexOf(';', count);
				
				if(!VarChecker.checkVars(data.substring(count+1,count2), "Variable " + (n+1) + " of Satellite " + varset)){
					return false;
				}
				filevars[varset+1][varcounter] = Double.parseDouble(data.substring(count+1, count2));
				startfrom = count2;
				varcounter++;
				if(varcounter == 6){
					varcounter = 0;
				}
				filedata = data.substring(count2);
			}			
			
			return true;
			
		}
		
		public static double findGlobVar(String variable, String limiter, String data){
			int count;
			int count2;
			
			count = data.indexOf(variable);
			count = data.indexOf(":",count);
			count2 = data.indexOf(limiter, count);
			return Double.parseDouble(data.substring(count+1,count2));
		}
	}