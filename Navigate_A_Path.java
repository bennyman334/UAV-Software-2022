import java.io.*;
import java.util.*;
public class Navigate_A_Path {

	static int[][][] grids; //a 1d array of grids, every time drone gets to waypoint, iterate to next
	static int N;
	static int M;
	
	public static void main(String[] args) throws Exception{
		Scanner s = new Scanner (new File ("navigate.in"));
		PrintWriter pw = new PrintWriter ("navigate.out");
		
		N = s.nextInt(); //x-dimension
		M = s.nextInt(); //y-dimension
		int P = s.nextInt(); //number of way points
		int K = s.nextInt(); //number of polygons
		
		int[][] waypoints = new int [2][P];
		//grid = new int [N+1][M+1]; //numbers greater than 0 represent obstacle or already visited place
		grids = new int [N+1][M+1][P-1];
		//takes in waypoints
		for(int i = 0; i < P; i ++) {
			waypoints[0][i] = s.nextInt();
			waypoints[1][i] = s.nextInt();
		}
		
		//filling in the polygons onto the grid
			for(int i = 0; i < K; i ++) {
				int x = s.nextInt();
				int [][] vtx = new int [2][x];
				for(int j = 0; j < x; j ++) {
					vtx[0][j] = s.nextInt();
					vtx[1][j] = s.nextInt();
					for(int k = 0; k < P-1 ; k ++) { //do it for all the grids
					//System.out.println(vtx[0][j] + " " + vtx[1][j]);
					
						if(j!= 0) { //connect the vertices
							int x1 = vtx[0][j-1];
							int y1 = vtx[1][j-1];
							int x2 = vtx[0][j];
							int y2 = vtx[1][j];
							
							fill(x1, y1, x2, y2, i+1, k);
						}
						if(j == x - 1) { //connects final vertex with the first
							int x1 = vtx[0][j];
							int y1 = vtx[1][j];
							int x2 = vtx[0][0];
							int y2 = vtx[1][0];
							
							fill(x1, y1, x2, y2, i+1, k);
						}
					}
				}
			//System.out.println();
			}
			
			//making all the waypoints obstacles
			for(int i = 0; i < P; i ++) {
				for(int j = 0; j < P-1; j ++) {
					int x = waypoints[0][i];
					int y = waypoints[1][i];
					
					grids[x][y][j] = -1;
				}
			}
		
//		for(int k = 0; k < P-1; k ++) {
//			for(int i = M; i >= 0; i --) {
//				for(int j = 0; j <= N; j ++) {
//					System.out.print(grids[j][i][0] + " ");
//				}
//				System.out.println();
//			}
//			System.out.println();
//			System.out.println();
//		}
		
		int x = waypoints[0][0];//starting location x
		int y = waypoints[1][0];//starting location y
		int index = 1;
		int destX = waypoints[0][index];
		int destY = waypoints[1][index];
		//System.out.println(destX + " " + destY);
		
		int j = 0;
		ArrayList<Integer> tbX = new ArrayList <>(); //used to trace back for when reaching a dead end
		ArrayList<Integer> tbY = new ArrayList <>();
		tbX.add(x);
		tbY.add(y);
		System.out.println(x + " " + y);
		pw.println(x + " " + y);
		grids[x][y][index-1] = 0;
		grids[destX][destY][index-1] = 0;
		
		//begin drone traversal
		while (j < 29) {
			int [] move = closest(x, y, destX, destY, index-1);
			grids[x][y][index-1] = -1;//mark path taken to prevent loops
			//System.out.println("dest: " + destX + " " + destY);
			if(move[2] == 0) { //reached dead end, so trace back to latest "unstuck" area
				int[] pos = traceback(tbX, tbY, destX, destY, index-1);
				x = pos[0];
				y = pos[1];
				continue;
			}
			//System.out.println(move[0] + " " + move[1]);
			x += move[0];
			y += move[1];
			
			tbX.add(x);
			tbY.add(y);
			//System.out.println(move[0] + " " + move[1]);
			
			System.out.println(x + " " + y);
			pw.println(x + " " + y);
			//System.out.println(index);
			//System.out.println(destX + " " + destY);
			
			//reached destination
			if(x == destX && y == destY) {
				if(index == P-1) {
					break;
				}
				index ++;
				destX = waypoints[0][index];
				destY = waypoints[1][index];
				
				
				grids[x][y][index-1] = 0; //unset desired waypoints as obstacles
				grids[destX][destY][index-1] = 0;
				//System.out.println
			}
			//break;
			j ++;
		}
		//System.out.println(grids[7][7][1]);
		//System.out.println(canGo(8 , 7, 7, 7, 1));
		pw.close();
		
	}
	
	public static int[] traceback(ArrayList <Integer> tbx, ArrayList <Integer> tby, int x2, int y2, int index) {
		int [] arr = new int [2];
		for(int i = tbx.size() - 1; i >= 0; i --) {
			int x1 = tbx.get(i);
			int y1 = tby.get(i);
			System.out.println(x1 + " " + y1);
			
			int[] move = closest(x1, y1, x2, y2, index);
			if(move[2] != 0) {
				arr[0] = x1;
				arr[1] = y1;
				break;
			} else {
				tbx.remove(i);
				tby.remove(i);
			}
		}
		return arr;
	}
	
	//find the move that will bring the drone closest to destination
	public static int[] closest (int x1, int y1, int x2, int y2, int index) {
		int[] move = new int [3];
		int[] x =  new int [] {1, -1, 0};
		int[] y =  new int [] {0, 1, -1};
		
		double dist = Integer.MAX_VALUE; //minimum distance between
		int directions = 0; //available directions
		
		for(int i : x) {
			for(int j: y) {
				//System.out.println(i + " " + j);
				if((i != 0 || j != 0) && canGo(x1, y1, x1+i, y1+j, index) == true) {
					double d = Math.sqrt(Math.pow(x2-(x1+i), 2) + Math.pow(y2-(y1+j), 2));
					directions ++;
					//System.out.println(d);
					if(d < dist) { //minimizing the distance
						dist = d;
						move[0] = i;
						move[1] = j;
					}
				}
			}
		}
		move[2] = directions;
		return move;
	}
	
	
	//determines if destination can be visited
	public static boolean canGo (int x1, int y1, int x2, int y2, int index) {
		
		if(x2 > 0 && x2 <= N && y2 > 0 && y2 <= M) {
			if(Math.abs(x1 - x2) == 1 && Math.abs(y1 - y2) == 1) {//ensure that diagonal won't go into polygon
				if(grids[x1][y2][index]!= 0 && grids[x2][y1][index] != 0 && grids[x1][y2][index] == grids[x2][y1][index]) { //also make sure that it's same polygon
					return false;
				}
			}
			if(grids[x2][y2][index] == 0) { //if space is not occupied
				return true;
			}
		}
		return false;
	}
	
	//fills in the edges of the polygon
	public static void fill(int x1, int y1, int x2, int y2, int filler, int index) {
		int xstep = 0;
		int ystep = 0;
		if(x1 < x2) {
			xstep = 1; //x-increment
			ystep = (y2 - y1)/(Math.abs(x2 - x1)); //y-increment
		} else if (x1 > x2) {
			xstep = -1;
			ystep = (y2 - y1)/(Math.abs(x2 - x1)); //y-increment
		} else {
			ystep = (y2 - y1)/(Math.abs(y2-y1)); //y-increment
		}
		//System.out.println("(" + x1 + ", " + y1 + ") |" + xstep + " " + ystep);
		while(x1 != x2 || y1 != y2) {
			grids[x1][y1][index] = filler;
			x1 += xstep;
			y1 += ystep;
		}
		grids[x2][y2][index] = filler;
		
	}

}
