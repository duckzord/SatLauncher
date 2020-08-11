package Variables;

public class Equations {
    
    // Variables for initial distance and velocity.
    protected double x0, x1, y0, y1, z0, z1,
    				newx0, newx1, newy0, newy1, newz0, newz1;
    
    protected double septime,A, b, C, delta, e, w;
    
    // Satellite position in terms of launcher coordinate system.
    protected double r, theta, zpos;
    
    // Variables for Epicycle elements.
    protected double newA, newb, newC, newdelta, newe, neww, n = 0.0024243, tanangle;
    
    // Variable for determining what the variables are.
    protected String objtype;
    protected boolean checker = false;
    
    // Variables for moving the satellites.
    private double x, y, z, xvel, yvel, zvel, fixedx0, fixedy0, fixedz0, fixedx1, fixedy1, fixedz1;
    
    // Assign user inputed variables values depending on the condition type chosen.
    public Equations(double cond1, double cond2, double cond3, double cond4, 
            double cond5, double cond6, double cond7, String vartype){
        if(vartype == "Epi"){
            A = cond1;
            b = cond2;
            C = cond3;
            delta = cond4;
            e = cond5;
            w = cond6;
            septime = cond7;
        }
        if(vartype == "Indav"){
        	r = cond7;
        	theta = cond1;
        	zpos = cond2;
        	
        	calcCartCoord(r,theta,zpos);
            
            x1 = cond3;
            y1 = cond4;
            z1 = cond5;
            
            fixedx1 = x1;
            fixedy1 = y1;
            fixedz1 = z1;
            septime = cond6;
        }
        objtype = vartype;
    }
    
    public void calcCartCoord(double r, double theta, double zpos){
    	
    	double thetarad = (theta/180) * (Math.PI);
    	
    	y0 = r * Math.cos(thetarad);
        z0 = -(r * Math.sin(thetarad));
        x0 = zpos;
        
        fixedx0 = x0;
        fixedy0 = y0;
        fixedz0 = z0;
    }
    /*
     * This method was intended to recalculate the initial distances and velocities as
     * the launcher rotates. It is meant to use a rotational matrix, but I am not sure
     * what with..
     */
    public void reCalcInits(double theta1, double phi, double omega){
    	
    	/*
    	 * Need to work out the change in coordinate from the very first conditions,
    	 * NOT the conditions worked out for the previous angle. I.e. always work
    	 * out the new x0 etc, from 0 degrees.
    	 */
    	double theta1rad = (theta1/180) * Math.PI;
    	double phirad = (phi/180) * Math.PI;
    	double omegarad = (omega/180) * Math.PI;
    	
    	// Calculate the new initial position when the launcher rotates.
    	x0 = ((Math.cos(theta1rad)*Math.cos(phirad))*fixedx0) - ((Math.cos(theta1rad)*Math.sin(phirad))*fixedy0) -
    		(Math.sin(theta1rad)*fixedz0);
    	y0 = (Math.sin(theta1rad)*fixedx0) + (Math.cos(phirad)*fixedy0);
    	z0 = ((Math.sin(theta1rad)*Math.cos(phirad))*fixedx0) + ((Math.sin(theta1rad)*Math.sin(phirad))*fixedy0) +
    		(Math.cos(theta1rad)*fixedz0);
    	
    	// Calculate the new initial velocites when the launcher rotates.
    	x1 = ((Math.cos(theta1rad)*Math.cos(phirad))*fixedx1) + ((Math.cos(theta1rad)*Math.sin(phirad))*fixedy1) -
			(Math.sin(theta1rad)*fixedz1);
    	y1 = (Math.sin(theta1rad)*fixedx1) + (Math.cos(phirad)*fixedy1);
    	z1 = ((Math.sin(theta1rad)*Math.cos(phirad))*fixedx1) - ((Math.sin(theta1rad)*Math.sin(phirad))*fixedy1) +
			(Math.cos(theta1rad)*fixedz1);
    	
    }
    // Functions for returning the private variable values.
    public double getx(){
        return x;
    }
    
    public double gety(){
        return y;
    }
    
    public double getz(){
        return z;
    }
    
    public double getx1(){
    	return xvel;
    }
    
    public double gety1(){
    	return yvel;
    }
    
    public double getz1(){
    	return zvel;
    }
    
    public double getSeptime(){
    	return septime;
    }
    
    public double getR(){
    	return r;
    }
    
    public double getTheta(){
    	return theta;
    }
    
    public double getZPos(){
    	return zpos;
    }
    public void setPeriod(double newn){
    	n = newn;
    }
    
    /* Calculate the x, y and z coordinates of a satellite at a time 't'. The equations
     * are different depending on what type the initial conditions are.
     */
    public void Translations(double t){
        
        if(objtype == "Epi"){ // Initial conditions is epicycle elements.
            x = A*Math.cos((n*t)+ delta)+ b;
            y = - 2*A*Math.sin((n*t)+delta)- ((3/2)*n*b*t) + (w);
            z = C*Math.cos((n*t) + e);
            
        } else if(objtype == "Indav"){ // Initial conditions is in position and velocity.
            x = 4*x0 + (2*y1)/n + (x1/n)*Math.sin(n*t) - (3*x0 + ((2*y1)/n))*Math.cos(n*t);
            y = y0 - (2*x1)/n - ((6*n*x0) + 3*y1)*t + ((2*x1)/n)*Math.cos(n*t) + (6*x0 + (4*y1)/n)*Math.sin(n*t);
            z = z0*Math.cos(n*t) + (z1/n)*Math.sin(n*t);
            xvel = x1*Math.cos(n*t) + ((3*n*x0) + 2*y1)*Math.sin(n*t);
            yvel = -2*x1*Math.sin(n*t) + ((6*n*x0)+4*y1)*Math.cos(n*t) - 6*n*x0 - 3*y1;
            zvel = -z0*n*Math.sin(n*t) + z1*Math.cos(n*t);
        }
        
        /* 
         * Launcher is a special condition, and needs all possible initial conditions to be
         * set to zero. This is because this will put the position of the launcher to (0,0,0).
         * These conditions are also required when a satellite is changed to the origin, and
         * some calculations need to be done to find the new "initial conditions".
         */
        else if(objtype == "Launcher"){
            x0 = 0;
            y0 = 0;
            z0 = 0;
            x1 = 0;
            y1 = 0;
            z1 = 0;
            A = 0;
            b = 0;
            C = 0;
            delta = 0;
            e = 0;
            w = 0;
            septime = 0;
            
        }
    }
    
    /*
     * This "Translations" function calculates the x, y and z coordinates for different
     * values of the initial velocities. This is only valid for the "Indav" conditions.
     */
    public void Translations(double t, double altx1, double alty1, double altz1){
        
	double newx1 = altx1;
	double newy1 = alty1;
	double newz1 = altz1;

            x = 4*x0 + (2*newy1)/n + (newx1/n)*Math.sin(n*t) - (3*x0 + ((2*newy1)/n))*Math.cos(n*t);
            y = y0 - (2*newx1)/n - ((6*n*x0) + 3*newy1)*t + ((2*newx1)/n)*Math.cos(n*t) + (6*x0 + (4*newy1)/n)*Math.sin(n*t);
            z = z0*Math.cos(n*t) + (newz1/n)*Math.sin(n*t);
            xvel = newx1*Math.cos(n*t) + ((3*n*x0) + 2*newy1)*Math.sin(n*t);
            yvel = -2*newx1*Math.sin(n*t) + ((6*n*x0)+4*newy1)*Math.cos(n*t) - 6*n*x0 - 3*newy1;
            zvel = -z0*n*Math.sin(n*t) + newz1*Math.cos(n*t);
    }
    
    // Simple function to set a satellite to the origin - i.e. set all initial conditions to zero.
    public void SetAsOrigin(){
        objtype = "Launcher";
    }
   
   
   /*
    * These equations are meant to calculate were another satellite (or the launcher) would be if
    * the satellite (satellite in the function definition) was at the origin. Again, as before the
    * way this is calculated depends on what type the initial conditions were set as. At the moment
    * this only works when time is 0. Maybe it is something to do with the way the translations is
    * called??....
    */
   public void NewPosWRT(Equations satellite, double time){
	   
	   //System.out.println(satellite.A + "," + satellite.b + "," + satellite.C + "," + satellite.delta + "," + satellite.e + "," + satellite.w);
	   System.out.println("New Origin " + satellite.x + "," + satellite.y + "," + satellite.z);
	   System.out.println("Sat to be changed " + x + "," + y + "," + z);
	   
	   if(satellite.objtype == "Indav"){
		   	   /*if(septime > satellite.septime){
		   		   satellite.Translations(0);
		   		   Translations(0);
		   	   	}
		   	   else if((satellite.septime > septime) && (time > septime)){
		   		   satellite.Translations(0);
		   		   Translations(satellite.septime - septime);
		   	   }
		   	   else {
		   		   satellite.Translations(0);
		   		   Translations(0);
		   	   }*/
			   //System.out.println("This time: " + septime + ", Changed time: " + satellite.septime);
		   		System.out.println(x0);
		   		System.out.println(satellite.x0);
		   		x0 = x0 - satellite.x0;
		   		y0 = y0 - satellite.y0;
		   		z0 = z0 - satellite.z0;
		   		x1 = x1 - satellite.x1;
		   		y1 = y1 - satellite.y1;
		   		z1 = z1 - satellite.z1;
		   	   
		   		if(objtype == "Launcher"){
		   			septime = satellite.septime;
		   	   }
		   
		   objtype = satellite.objtype;
   }
	   else if(satellite.objtype == "Epi"){
		   newA = Math.sqrt(Math.pow(A, 2) + Math.pow(satellite.A, 2) + A*satellite.A*Math.cos(delta + satellite.delta));
		   newb = b - satellite.b;
		   newC = Math.sqrt(Math.pow(C, 2) + Math.pow(satellite.C, 2) +  C*satellite.C*Math.cos(e + satellite.e));
		   newdelta = Math.acos((A*Math.cos(delta) - satellite.A*Math.cos(satellite.delta))/newA);
		   newe = Math.acos((C*Math.cos(e) - satellite.C*Math.cos(satellite.e))/newC);
		   neww = w - satellite.w;
	   
		   /*
		    * I create a newA, newb, etc because the original values are still used in later equations. 
		    * For example, if I set A to the new value when A was calculated, the new A would be used
		    * in the delta equation, which would give the incorrect answer.
		    */ 
		   A = newA;
		   b = newb;
		   C = newC;
		   delta = newdelta;
		   e = newe;
		   w = neww;
		   if(objtype == "Launcher"){
		   n = 0 - satellite.n;
		   septime = satellite.septime;
		   }
	   
		   objtype = satellite.objtype;
	   }
	   if(time < septime){
	   Translations(0); // Not sure if this is needed, only here to determine what x, y and z is at the moment.
	   }
	   else{
		   Translations(time - septime);
	   }
       System.out.println("================================================");
       //System.out.println(A + "," + b + "," + C + "," + delta + "," + e + "," + w);
       System.out.println("Changed to " + x + "," + y + "," + z);
       System.out.println("================================================");
       System.out.println("================================================");
   }
    
    // Convert initial velocity and distance conditions to the Epicycle elements.
   	public double[] getInitialConditions(String type){
   		double[] conditions = new double[7];
   		if(type == "Indav"){
   			conditions[0] = theta;
   			conditions[1] = zpos;
   			conditions[2] = fixedx1;
   			conditions[3] = fixedy1;
   			conditions[4] = fixedz1;
   			conditions[5] = septime;
   		}
   		
   		return conditions;
   	}
   	
    public double[] ConvertToEpi(){
    	
    	double[] epielements = new double[6];
        
        A = Math.sqrt((Math.pow((x1/n),2))+Math.pow(((3*x0)+(2*(y1/n))),2));
        b = 4*x0 + (2*(y1/n));
        C = Math.sqrt((Math.pow(z0,2))+(Math.pow((z1/n),2)));
        delta = Math.atan(x1/(-3*n*x0 - 2*y1));
        tanangle = z1/(z0*n);
        /*
         * Because tan is used the angle that the inverse tan takes has to be less than
         * 90 degrees (PI/2) otherwise the answer will be incorrect. I have yet to verify this,
         * but if true, then this will also apply to delta above.
         */ 
    	e = Math.atan(tanangle);
        /*if(tanangle > (Math.PI/2)){
        	e = e - Math.PI;
        }
        else if(tanangle < -(Math.PI/2)){
        	e = e + Math.PI;
        }*/
        w = y0 - (2*(x1/n));
        epielements[0] = A;
        epielements[1] = b;
        epielements[2] = C;
        epielements[3] = delta;
        epielements[4] = e;
        epielements[5] = w;
        
        return epielements;
    }
    
    // Convert Epicycle elements to the initial velocity and distance conditions.
    public double[] ConvertToIndav(){
        
    	double[] indavelements = new double[6];
    	
        x0 = -A*Math.cos(-delta) + b;
        x1 = -n*A*Math.sin(-delta);
        y0 = w - 2*A*Math.sin(-delta);
        y1 = -(3*n*b)/2 + 2*n*A*Math.cos(-delta);
        z0 = Math.sqrt((Math.pow(C,2))/(1+Math.pow(Math.tan(e),2)));
        z1 = Math.sqrt((Math.pow(C,2))/(1+Math.pow(Math.tan(e),2)))*n*Math.tan(e);
        
        indavelements[0] = x0;
        indavelements[1] = x1;
        indavelements[2] = y0;
        indavelements[3] = y1;
        indavelements[4] = z0;
        indavelements[5] = z1;
        
        return indavelements;
    }  
}
