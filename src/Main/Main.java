package Main;

import Variables.Equations;
import Interfaces.MainWindow;
import Interfaces.CalcProbs;

import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import com.jogamp.opengl.util.gl2.GLUT;

import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.logging.*;

public class Main
        implements GLEventListener,
        KeyListener {
	
	// This creates the window which the simulation is displayed in.
	MainWindow window = new MainWindow("Satellite Viewer", this);
	
    // All the variables for the extended collision propability
    protected double newx, newy, newz, newx1, newy1, newz1, pathinc, xlimit1, ylimit1, zlimit1,
    sat1x, sat1y, sat1z, sat2x, sat2y, sat2z, acc = 4, newsep, endtime;
    
    // Collision counters used in the extended collision probability calculations are here.
    
    // Launcher geometry in metres
    public static float launch_rad = 0.6f, launch_height = 3, launch_alt = 620,
    launch_anglex = 0, launch_anglez = 0, launch_angley = 0, 
    rotation_rate_x = 0, rotation_rate_y = 0, rotation_rate_z = 0;
    
    public static double [] simvars = new double[9];
    
    protected static double period =2*Math.PI/((2*Math.PI)*Math.sqrt((Math.pow((launch_alt+6378),3))/398600));
    
    // Satellite geometry in metres
    protected static float sat_width = 0.2f;
    protected static double r = launch_rad;
    
    protected int fps = 0;

    // These variables are used for the normal collision detection during simulation.
    protected double xcheck, xcheck1, ycheck, ycheck1, zcheck, zcheck1, diffx, diffy, diffz;
    
    // Some additional integers used in the program
    protected static int equationcheck = 0, varslength;
    
    // Some boolean variables that are only needed in this file
    protected boolean display;
    
    // Public variables for use in other packages.
    public static boolean varset, names, checker, plotline, drawn, warnlogset = false,
    midlogset = false, startenabled = false, motion = false, probcheck = false, problines = false,
    axislines = false;
	public static double t, timeinc = 1, collrange = (double)sat_width, warnrange = 0.6, midrange = 1.0;
	// These variables are used with the camera.
    public static double v1 = 1,  cam_mag = 15,  v3 = 1, lookx = 0, looky = 0, lookz = 0;
    
    // Material properties
    protected static float[] sat_col_amb = new float[4];
    
	// Variables to use the GLUT and GLU libraries.
    private GLUT glut = new GLUT();
    private GLUgl2 glu = new GLUgl2();
    
    
   
    // The array of Equations are set up here, ready to initialised later. Also the launcher is set up here too.
    public static Equations[] satellite; // = new Equations[Vars.length];
    Equations launcher = new Equations(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, "Launcher");
    // The logs are set up here ready for use.
    //ublic static Logs clog = new Logs("collision.log");
    //public static Logs wlog = new Logs("warning.log");
    

    public static void main(String[] args) {
    	new Main(); 
    }

    public void init(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();

        gl.glClearDepth(1.0d);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL2.GL_FLAT);
        
        
        gl.glEnable(GL2.GL_LIGHT0);
        
        
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    public void display(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        
        
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        drawSats(GL2.GL_RENDER, gl);
        
        // Create some lines that represent the x,y and z axes, turned on and off
        // in the simulation setup.
        if(axislines){
        gl.glBegin(GL.GL_LINES);
        gl.glColor3f(0.8f,0.3f,0.1f);
        gl.glVertex3d(0.0, 0.0, 0.0);
        gl.glVertex3d(0.0, 0.0, 10.0);
        gl.glColor3f(0.5f,0.8f,0.5f);
        gl.glVertex3d(0.0, 0.0, 0.0);
        gl.glVertex3d(10.0, 0.0, 0.0);
        gl.glColor3f(0.1f,0.3f,0.8f);
        gl.glVertex3d(0.0, 0.0, 0.0);
        gl.glVertex3d(0.0, 10.0, 0.0);
        gl.glEnd();
        }
        // If two satellites have collided then this error message is displayed.
        if(display){
            gl.glColor3f(1.0f,0.0f,0.0f);
            gl.glRasterPos2i(-8 , 0);
            glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24,"SATELLITES COLLIDED!");
        }
        // Only if there are satellites drawn should these functions be called.
        if(drawn){
        	starttime();
        	checkSatPositions();
        if(!startenabled){
        	window.startsim.setEnabled(true);
        	startenabled = true;
        }
        }
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {

        GL2 gl = drawable.getGL().getGL2();

        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(40.0, (float) w / (float) h, 0.1f, 100.0);
        //glu.gluOrtho2D(0.0, 3.0, 0.5, 0.5);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean b, boolean b0) {
    }
    
    // This function is called the draw the satellites for the first time.
    public static void createSats(double[][] Vars, int numvar,String eqtype){
    	
    	varslength = numvar;
    	satellite = new Equations[varslength];
    	
        for (int i = 0; i < varslength; i++) {
        	
        	if(eqtype == "Indav"){
        	Vars[i][6] = launch_rad + (sat_width/2);
        	}
        	
            satellite[i] = new Equations(Vars[i][0], Vars[i][1], Vars[i][2],
                    Vars[i][3], Vars[i][4], Vars[i][5], Vars[i][6], eqtype);
            satellite[i].setPeriod(period);
        }
        drawn = true;
    }
    // This method sets the global variables from conditions loaded from a file.
    public static void createVariables(double[] variables){
    	launch_rad = (float)variables[2];
    	launch_height = (float)variables[3];
    	sat_width = (float)variables[4];
    	
    	
    	period = 2*Math.PI/((2*Math.PI)*Math.sqrt((Math.pow((variables[5]+6378),3))/398600));
    	rotation_rate_z = (float)variables[6];
    	rotation_rate_y = (float)variables[7];
    	rotation_rate_x = (float)variables[8];
    	
    	simvars = variables;
    	//r = launch_rad + (sat_width/2);
    	
    	
    	
    }
    /*
     * Here the satellites are actually drawn. For every frame the translations function is called for
     * a time t. These x, y and z coordinates are then used to draw the position of the satellites.
     * The launcher is drawn separately from the other satellites because it is not a satellite[] object.
     */
    public void drawSats(int mode, GL2 gl) {
            
    glu.gluLookAt(cam_mag * Math.cos(v1 / Math.PI * 2), cam_mag, cam_mag * Math.sin(v3 / Math.PI * 2),// camera location
               		looky, lookz, lookx, // what the camera is looking at
               		0, 1, 0);  // up vextor
    
    	// If satellites have been created then the satellites can be drawn.
    	if(drawn){
    		
    		/*
    		 * Here the satellite paths are drawn onto the simulator so that the user can
    		 * see the motion a satellite will follow without actually running the simulator.
    		 * The paths can be turned on and off via the simulator setup, and the length of
    		 * the path (pathtime) can also be set, however the longer the path, the longer
    		 * the processing time and there is slow down in the simulator.
    		 */
    		if(plotline){
    		for(int sat = 0; sat < varslength; sat++){
    			changecolour(gl,sat);
    			gl.glBegin(GL.GL_LINE_STRIP);
    			for(int j = 0; j < CalcProbs.pathtime; j++){
    				if(j > satellite[sat].getSeptime()){
    					satellite[sat].Translations(j - satellite[sat].getSeptime());
    				}
    				else{
    					satellite[sat].Translations(0);
    				}
    	        	gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, sat_col_amb,0);
    				gl.glVertex3d(satellite[sat].gety(), satellite[sat].getx(), satellite[sat].getz());
    			}
    			gl.glEnd();
    		}
    		}
    	/*
    	 * For every satellite the code below is executed.
    	 */
    	for (int i = 0; i < varslength; i++) {
        	
        	/*
        	 * Draw the motion paths of the possible different paths a satellite could follow.
        	 * The user can turn on and off the paths via the simulation setup interface, 
        	 * however there is severe slow down when the paths are displayed.
        	 */
        	if(problines){
        	pathinc = (CalcProbs.checkrange*2)/(acc-1);
        	satellite[i].Translations(0);
    		newx = satellite[i].getx1();
    		newx = newx - CalcProbs.checkrange;
    		newsep = Main.satellite[i].getSeptime() - CalcProbs.seprange;
	    	endtime = Main.satellite[i].getSeptime() + CalcProbs.seprange;
    		/*
    		 * The next four "for" loops calculate every possible path the satellite could follow
    		 * given a range and some estimated initial conditions.
    		 */
    		for(int nx = 0; nx <acc; nx++){
    			satellite[i].Translations(0);
    			newy = satellite[i].gety1();
    			newy = newy - CalcProbs.checkrange;
    			for(int ny = 0; ny < acc; ny++){
    				satellite[i].Translations(0);
    				newz = satellite[i].getz1();
        			newz = newz - CalcProbs.checkrange;
    				for(int nz = 0; nz < acc; nz++){
    					/*
    					 * For every possible path for every satellite, for the same amount of
    					 * time as the single paths above (pathtime), the lines are drawn on the 
    					 * simulator.
    					 */
    					changecolour(gl,i);
	    	        	gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, sat_col_amb,0);
    					for(int t = (int)newsep; t<endtime; t++){
    						if(t>0){
    					gl.glBegin(GL.GL_LINE_STRIP);
    	    			for(int coltime = 0; coltime < CalcProbs.pathtime; coltime++){
    	    			if(coltime>t){
    	    				satellite[i].Translations(coltime - t,newx,newy,newz);
    	    			}
    	    			else{
    	    				satellite[i].Translations(0);
    	    			}
    	    				gl.glVertex3d(satellite[i].gety(), satellite[i].getx(), satellite[i].getz());
    	    			}
    	    			gl.glEnd();
    					}
    					}
    				
    					newz += pathinc;	
    				}
    				newy += pathinc;
    			}
    			newx += pathinc;
    		}
        	}
        	
    		float[] position = {15.0f,15.0f,15.0f,1.0f};
            float[] colour = {0.0f,0.0f,0.0f,1.0f};
            gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position,0);
            gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, colour,0);
            
        	/*
        	 * Here the satellites are drawn, if they have not launched, i.e. the actual
        	 * time is less than the satellite's separation time, the satellite's
        	 * coordinates are calculated as though at time zero.
        	 */
        	if(t > satellite[i].getSeptime()){
            satellite[i].Translations(t-satellite[i].getSeptime());
        	}
        	else{
        		satellite[i].Translations(0);
        	}
        	
            gl.glPushMatrix();
            changecolour(gl,i);
        	//satellite[i].reCalcInits(launch_anglez,"z");

        	if(t > satellite[i].getSeptime()){
                satellite[i].Translations(t-satellite[i].getSeptime());
                gl.glTranslated(satellite[i].gety(),satellite[i].getx(), satellite[i].getz());
                
        	}
        	else{
            	satellite[i].reCalcInits(launch_angley-90,launch_anglez,launch_anglex);
        		satellite[i].Translations(0);
        		gl.glTranslated(satellite[i].gety(),satellite[i].getx(),satellite[i].getz());
        	}
        	/*else{
        		gl.glRotated(satellite[i].getTheta(), 0.0, 1.0, 0.0);
        		gl.glTranslated(satellite[i].getR(), satellite[i].getZPos(), 0.0);
        	}*/
        	gl.glRotated(launch_anglex,0.0,0.0,1.0);
        	gl.glRotated(launch_angley,1.0,0.0,0.0);
        	gl.glRotated(launch_anglez,0.0,1.0,0.0);
        	gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, sat_col_amb,0);
            glut.glutSolidCube(sat_width);
            
            if((!motion && t != 0) || names){
            gl.glRasterPos3d(-1, -1, -1);
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_10,"Satellite " + i);
            }
            
            gl.glPopMatrix(); 
    		}
        }
  
    	/*
    	 * Here the launcher is drawn into the simulator. The launcher is drawn
    	 * even if the satellites are not entered yet. A launch time calculation
    	 * is included here in case the origin is reset to another satellite, an
    	 * the launcher needs to appear to "launch" from the satellite.
    	 */
        if(t > launcher.getSeptime()){
        	launcher.Translations(t - launcher.getSeptime());
        }
        else{
        	launcher.Translations(0);
        }
        
        gl.glPushMatrix();
        float[] launch_col_amb = {0.5f, 0.5f, 0.5f, 1.0f};
        gl.glRotated(launch_angley,1.0,0.0,0.0);
    	gl.glRotated(launch_anglez,0.0,1.0,0.0);
        gl.glTranslated(launcher.gety(),launcher.getx(),launcher.getz());
        	gl.glPushMatrix();
        	//gl.glRotated(launch_anglez,0.0,1.0,0.0);
        	gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, launch_col_amb,0);
        	glut.glutSolidCylinder(launch_rad,launch_height,20,20);
        	gl.glTranslated(0.0,0.0,0.0);
        	gl.glRotated(180,1.0,0.0,0.0);
        	glut.glutSolidCone(launch_rad,(launch_height/1.5),20,20);
        	gl.glPopMatrix();
        	gl.glRotated(launch_anglex,0.0,0.0,1.0);
        	gl.glRotated(launch_angley,1.0,0.0,0.0);
        	gl.glRotated(launch_anglez,0.0,1.0,0.0);
        gl.glPopMatrix();
        
}
    
    /*
     * This function is used to check each satellite's positions against every other satellite.
     * This is done by finding the satellite in question's x, y and z coordinates, and then
     * subtracting them from the next satellite's x, y and z coordinates. If the satellite is
     * less than the collision range away from another, then it is said they have collided, the 
     * animation is then stopped and the camera is set to look at the collided satellites. A
     * number of conditions are logged in the log files. The calculations are done in "real time".
     */
    public void checkSatPositions(){
        if(motion){
        for(int i=0;i<varslength;i++){
        	if(satellite[i].getSeptime() < t){
            for(int j=0;j<varslength;j++){
                if(j>i && satellite[j].getSeptime() < t){
                        if(checkRange(collrange,j,i)){
                        	// Display an error message in the main window.
                            window.satCollided(true, i, j);
                            // The camera is set to look at one of the satellites.
                            lookx = satellite[j].getx();
                            looky = satellite[j].gety();
                            lookz = satellite[j].getz();
                        }
                		else if(warnlogset){
                			if(checkRange(warnrange,j,i)){
                				/*wlog.addLog(Level.WARNING,"At time: "+t+" satellites "+i+"" +
                    					" and "+j+ " are within "+warnrange+" metre(s) of each"
                    					+ " other");*/
                    		}
                		else if(midlogset){
                				if(checkRange(midrange,j,i)){
                    			/*wlog.addLog(Level.FINE, "Satellite " + i + " is "+midrange+" metres " +
                    					"away from Satellite " + j);*/
                    		}
                		}
                }
            }  
        }
        }
        }
        }
    }
    
    // Start or stop the time.
    public void starttime() {
        if (motion) {
            t += timeinc;
            display = false;
            window.time.setText("" + t);
            launch_anglex += rotation_rate_x;
            launch_angley += rotation_rate_y;
            launch_anglez += rotation_rate_z;
        } else if (motion) {
            t += 0;
        }
    }
    
    public static double[] collectSimVars(){
    	DecimalFormat df = new DecimalFormat();
    	df.setMaximumFractionDigits(6);
    	
    	simvars[2] = launch_rad;
    	simvars[3] = launch_height;
    	simvars[4] = sat_width;
    	simvars[5] = launch_alt;
    	simvars[6] = rotation_rate_z;
    	simvars[7] = rotation_rate_y;
    	simvars[8] = rotation_rate_x;
    	
    	return simvars;
    }
    /*
     * These if statements performs the check for collisions. Only the
     * x direction is looked at first, to determine whether to check
     * the y direction, then the z direction. This should hopefully
     * reduce some unnecessary checks. The result of all conditions
     * being met (i.e. satellites have collided) is to stop the
     * simulation, print a message to the screen and then focus the
     * camera on the colliding satellites.
     */
    public boolean checkRange(double range, int satnumber, int refsat){
    	
       	satellite[satnumber].Translations(t - satellite[satnumber].getSeptime());
    	satellite[refsat].Translations(t - satellite[refsat].getSeptime());
    	
    	if(Math.abs(satellite[satnumber].getx() - satellite[refsat].getx()) < range){
    		if(Math.abs(satellite[satnumber].gety() - satellite[refsat].gety()) < range){
    			if(Math.abs(satellite[satnumber].getz() - satellite[refsat].getz()) < range){
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    /*
     * Choose a colour for each satellite depending on which number it is. Basically
     * this function works out the binary format of the satellite number and uses
     * it in the gl colour function.
     */
    public void changecolour(GL gl,int satnum){
    	satnum = satnum + 1;
    	while(satnum > 7){
    		satnum = satnum - 7;
    	}
    	float xcol = Math.abs(satnum/4);
    	if(xcol != 0){
    		satnum = satnum - 4;
    	}
    	float ycol = Math.abs(satnum/2);
    	if(ycol != 0){
    		satnum = satnum - 2;
    	}
    	float zcol = Math.abs(satnum);
    	
    	sat_col_amb[0] = xcol;
    	sat_col_amb[1] = ycol;
    	sat_col_amb[2] = zcol;
    	sat_col_amb[3] = 1.0f;
    }

    // Not sure if I need any mouse events yet, because everything is done through the interface.
    public void mouseClicked(MouseEvent mouse) {
    }

    public void mousePressed(MouseEvent mouse) {
        if (mouse.getButton() == MouseEvent.BUTTON3) {
            if (!motion) {
                motion = true;
                lookx = looky = lookz = 0;
                v1 = v3 = 1;
                cam_mag = 15;
            } else {
                motion = false;
            }
        }
    }


    public void keyTyped(KeyEvent key) {
    }
    /* 
     * Below are the actions performed for when certain keys are pressed.
     */
    public void keyPressed(KeyEvent key) {
    	
        if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
            v1 += 0.1;
            v3 = v1;
        }
        
        if (key.getKeyCode() == KeyEvent.VK_LEFT) {
            v1 -= 0.1;
            v3 = v1;
        }
        
        if ((key.getKeyCode() > 47) && (key.getKeyCode() < 58 ) && !motion){
        	int satpicked = key.getKeyCode() - 48;
        	if(satpicked < varslength){
            launcher.NewPosWRT(satellite[satpicked],t);
            for(int i = 0; i < varslength; i++){
            	if(i != satpicked){
            	satellite[i].NewPosWRT(satellite[satpicked], t);
            	}
            }
            satellite[satpicked].SetAsOrigin();
        	}
        }
        
        if (key.getKeyCode() == KeyEvent.VK_COMMA && !motion){
            t -= timeinc;
        }
        
        if (key.getKeyCode() == KeyEvent.VK_PERIOD && !motion){
            t += timeinc;
        }
        if (key.getKeyCode() == KeyEvent.VK_UP){
            cam_mag -= 0.1;
        }
        
        if (key.getKeyCode() == KeyEvent.VK_DOWN){
            cam_mag += 0.1;
        }
        // ======================================
        if (key.getKeyCode() == KeyEvent.VK_W){
            launch_angley -= 1;
            /*for(int s = 0; s<varslength; s++){
            	satellite[s].reCalcInits(launch_angley-90,"y");
            }*/
            
        }
        if (key.getKeyCode() == KeyEvent.VK_S){
            launch_angley += 1;
            /*for(int s = 0; s<varslength; s++){
            	satellite[s].reCalcInits(launch_angley-90,"y");
            }*/
        }

        if (key.getKeyCode() == KeyEvent.VK_D){
            launch_anglez += 1;
            /*for(int s = 0; s<varslength; s++){
            	satellite[s].reCalcInits(launch_anglez,"z");
            }*/
        }
        if (key.getKeyCode() == KeyEvent.VK_A){
            launch_anglez -= 1;
            /*for(int s = 0; s<varslength; s++){
            	satellite[s].reCalcInits(launch_anglez,"z");
            }*/
        }
        // ======================================

        if (key.getKeyCode() == KeyEvent.VK_R){
        	for(int i = 0; i < varslength; i++){
        		satellite[i].NewPosWRT(launcher, t);
        	}
        	launcher.SetAsOrigin();
        }
    }

    public void keyReleased(KeyEvent key) {
    }
}

