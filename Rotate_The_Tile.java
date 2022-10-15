import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
public class Rotate_The_Tile {

	public static void main(String[] args) throws Exception{
		Scanner s = new Scanner (new File ("rotate.in"));
		PrintWriter pw = new PrintWriter ("rotate.out");
		
		double H = s.nextDouble();
		double V = s.nextDouble();
		double L = s.nextDouble();
		
		double X = s.nextDouble();
		double Y = s.nextDouble();
		
		
		//System.out.println(H/2);
		double A = (L/2)/Math.tan(Math.toRadians(V/2));
		double W = 2*A*Math.tan(Math.toRadians(H/2));
		
		double R = Math.toDegrees(Math.atan(-1*Y/A));
		double P = Math.toDegrees(Math.atan(X/A));
		
		DecimalFormat df = new DecimalFormat(".#");
		pw.println(df.format(A) + " " + df.format(W) + " " + df.format(R) + " " + df.format(P));
		pw.close();
	}

}
