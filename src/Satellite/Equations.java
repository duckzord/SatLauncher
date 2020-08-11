package Satellite;

public class Equations {
    
    // Variables for intial distance and velocity.
    protected double x0, x1, y0, y1, z0, z1;
    
    // Variables for Epicycle elements.
    protected double A, b, C, delta, e, w, n;
    
    // Variable for determining what the variables are.
    protected String objtype;
    
    // Variables for moving the satellites.
    static protected double x, y, z, xvel, yvel, zvel;
    // Default constructor as private so you it is not possible to use.
    private Equations(){
    }
    
    // Assign variables values depending on the condition type chosen.
    public Equations(double cond1, double cond2, double cond3, double cond4, 
            double cond5, double cond6, double cond7, String vartype){
        if(vartype == "Epi"){
            A = cond1;
            b = cond2;
            C = cond3;
            delta = cond4;
            e = cond5;
            w = cond6;
            n = cond7;
        }
        if(vartype == "Inidv"){
            x0 = cond1;
            x1 = cond2;
            y0 = cond3;
            y1 = cond4;
            z0 = cond5;
            z1 = cond6;
            n = cond7;
        }
        if(vartype == "Launcher"){
            A = 0;
            x0 = 0;
        }
        
        objtype = vartype;
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
    
    public double getxvel(){
        return xvel;
    }
    
    // Functions for setting variable values.
    public void setx(double newx){
        x = newx;
    }
    
    public void sety(double newy){
        y = newy;
    }
    
    public void setz(double newz){
        z = newz;
    }
    
    // Work out the x coordinate depending on what conditions were used.
    public void Translations(double t){
        
        if(objtype == "Epi"){
            x = A*Math.cos((n*t)+ delta)+ b;
            y = (-w)-(3/2)*n*(b*t) - 2*A*Math.sin((n*t)+delta);
            z = C*Math.cos((n*t) + e);
            xvel = -A*n*Math.sin(delta);
            yvel = -((3/2)*n*b)-2*n*A*Math.cos(delta);
            zvel = -Math.sin(e);
            
        } else if(objtype == "Inidv"){
            x = 4*x0 + (2*y1)/n + (x1/n)*Math.sin(n*t) - (3*x0 + 2*y1)*Math.cos(n*t);
            y = y0 - (2*x1)/n - ((6*n*x0) + 3*y1)*t + ((2*x1)/n)*Math.cos(n*t) 
                    + (6*x0 + (4*y1)/n)*Math.sin(n*t);
            z = z0*Math.cos(n*t) + (z1/n)*Math.cos(n*t);
        }
        else if(objtype == "Launcher"){
            x = 0;
            y = 0;
            z = 0;
            xvel = 0;
            yvel = 0;
            zvel = 0;
            
        }
    }
    
    public void SetAsOrigin(){
        A = 0;
        x0 = 0;
        objtype = "Launcher";
    }
    
   public void SetAsSat(Equations satellite){

       x = x - satellite.x;
       y = y - satellite.y;
       z = z - satellite.z;
       xvel = xvel - satellite.xvel;
       yvel = yvel - satellite.yvel;
       zvel = zvel - satellite.zvel;
       n = satellite.n;
       
       x0 = x;
       x1 = xvel;
       y0 = y;
       y1 = yvel;
       z0 = z;
       z1 = zvel;
       this.ConvertToEpi();
       objtype = "Epi";
   }
    
    // Convert initial velocity and distance to the Epicycle elements.
    public void ConvertToEpi(){
        
        A = Math.sqrt((Math.pow((x1/n),2))+Math.pow(((3*x0)+(2*(y1/n))),2));
        b = 4*x0 + (2*(y1/n));
        C = Math.sqrt(((Math.pow(z0,2))+(Math.pow(z1/n,2))));
        delta = -(Math.atan(x1/(-3*n*x0 - 2*y1)));
        e = Math.atan(z1/(z0*n));
        w = y0 - (2*(x1/n));
    }
    
    // Convert Epicycle elements to the initial velocity and distance elements.
    public void ConvertToInidv(){
        
        x0 = -A*Math.cos(-delta) + b;
        x1 = -n*A*Math.sin(-delta);
        y0 = w - 2*A*Math.sin(-delta);
        y1 = -(3*n*b)/2 + 2*n*A*Math.cos(-delta);
        z0 = Math.sqrt((Math.pow(C,2))/(1+Math.pow(Math.tan(e),2)));
        z1 = Math.sqrt((Math.pow(C,2))/(1+Math.pow(Math.tan(e),2)))*n*Math.tan(e);
        
    }
    
    
}
