package Interfaces;

final class tparams{
	public double mjd, day;
	public int month, year;
}

class nordata{
	double bstar;
	double i, o, e, w, m, n;
}

class eparams{
	double a, e, i, o, w, xi, eta;
	double beta;
	double n, a0, ap;
}

public class findTLE{

	double D2R = (Math.PI/180.0);
	double SEC2DAY = (1.0/86400.0);
	double R2D = (180.0/Math.PI);
	
	double MU = 398600.5;
	double RE = 6378.137;
	
	double GM = 1.0;
	double AE = 1.0;
	double LO = RE;
	double V0 = Math.sqrt(MU/AE);
	double TO = Math.sqrt((LO*LO*LO)/MU);
	
	tparams tdata = new tparams();
	nordata nor = new nordata();
	eparams e = new eparams();
	
	public eparams EpifromTLE(String data){
		
		norad(data);
		conv();
		
		return e;
	}
	
	public void norad(String data){

		String str;
		int num;
		double doub;
		//char x;
		
		str = data.substring(18, 20);
		num = (int)Double.parseDouble(str);
		
		if(num>70){
			num += 1900;
		}
		else{
			num+= 2000;
		}
		tdata.year = num;
		
		str = data.substring(20, 33);
		doub = Double.parseDouble(str);
		tdata.day = doub;
		
		/*
		 * Come back to this bit about bstar!
		 */
		/*
		str = data.substring(54, 62);
		x = str.indexOf()*/
		
		str = data.substring(9,17);
		nor.i = D2R * Double.parseDouble(str);
		
		str = data.substring(17, 26);
		nor.o = D2R * Double.parseDouble(str);
		
		str = "0.";
		str.concat(data.substring(26,34));
		nor.e = Double.parseDouble(str);
		
		str = data.substring(34,43);
		nor.w = D2R * Double.parseDouble(str);
		
		str = data.substring(43,52);
		nor.m = D2R * Double.parseDouble(str);
		
		str = data.substring(52,64);
		nor.n = Double.parseDouble(str);
	}
	
	public void conv(){
		
		
		int [] dm = {31,28,31,30,31,30,31,31,30,31,30,31};
		double d,d0;
		int k , y, aa=0;
		
		e.i = nor.i;
		e.o = nor.o;
		e.e = nor.e;
		e.w = nor.w;
		e.n = nor.n;
		d = Math.cos(nor.w);
		e.xi = nor.e * d;
		d = Math.sin(nor.w);
		e.eta = nor.e * d;
		
		e.n = (2*Math.PI) * e.n * SEC2DAY;
		e.n *= TO;
		e.a = GM / (e.n*e.n);
		e.a = Math.pow(e.a, 0.333333333);
		
		d = e.a - 1.0 - 78.0 / LO;
		d = 42.0 / (LO * d);
		d = Math.pow(d,4);
		e.beta = nor.bstar * d * e.a;
		
		e.ap = e.w - 2.0 * e.e * Math.sin(e.w);
		
		d = nor.m + nor.w;
		d = nor.m + 2.0 * nor.e * Math.sin(nor.m) + (5.0/4.0 * nor.e * nor.e * Math.sin(2.0*nor.m));
		
		d += nor.w;
		
		d = Math.IEEEremainder(d, 2.0*Math.PI);
		
		e.a0 = d;
		e.a0 = d - 2.0 * e.e * ( Math.sin(e.a0 - e.ap) + Math.sin(e.ap));
		
		d = tdata.day;
		k = 0;
		d -= 1.0;
		while (d> dm[k]){
			d -= dm[k];
			k++;
		}
		d += 1.0;
		k++;
		tdata.day = d;
		tdata.month = k;
		y = tdata.year;
		
		if(k<3){
			y--;
			k+=12;
		}
		
		aa = (int)(y/100);
		d += 2 - aa + (int)(aa / 4);
		d += (int)(365.25 * y);
		d += (int)(30.6001 * (k + 1));
		tdata.mjd = d - 679006.0;
		
	}
	
}


