import java.io.*;
import java.util.*;
public class Find_A_Point {

	public static void main(String[] args) throws Exception{
		Scanner s = new Scanner (new File ("find.in"));
		PrintWriter pw = new PrintWriter ("find.out");
		
		int N = s.nextInt(); //number of x-axis markers
		int M = s.nextInt(); //number of y-axis markers
		
		double Dx = s.nextDouble(); //x increments
		double Dy = s.nextDouble(); //y increments
		
		int K = s.nextInt();
		
		double[] x = new double [K];
		double[] y = new double [K];
		for(int i = 0; i < K; i ++) {
			x[i] = s.nextDouble();
			y[i] = s.nextDouble();
		}
		
		int[] xRes = new int [K];
		int[] yRes = new int [K];
		
		for(int i = 0; i < K; i ++) {
			if(x[i] >= (N-1)*Dx) { //out of bounds for x
				xRes[i] = N - 1;
			} else {
				double dist = x[i]%Dx;
				if(dist <= Dx/2) { //lower bound or equal to half of increment
					xRes[i] = (int)(x[i]/Dx); //result will be rounded down
				} else {
					xRes[i] = (int)(x[i]/Dx) + 1; //otherwise, result rounded up
				}
				
			}
			
			if(y[i] >= (M-1)*Dy) { //out of bounds for y
				yRes[i] = M - 1;
			} else {
				double dist = y[i]%Dy;
				if(dist <= Dy/2) {
					yRes[i] = (int)(y[i]/Dy);
				} else {
					yRes[i] = (int)(y[i]/Dy) + 1;
				}
			}
		}
		for(int i = 0; i < K; i ++) {
			System.out.println(xRes[i] + " " + yRes[i]);
			pw.println(xRes[i] + " " + yRes[i]);
		}
		
		pw.close();
	}

}
