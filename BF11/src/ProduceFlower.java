import java.io.*;
import java.util.*;

public class ProduceFlower{
	public static void main(String args[]){
		try{
			final int TIME = 200;
			final int WIDTH = 740;
			final int HEIGHT = 560;
			final int VOLUMN = 15;

			PrintWriter out = new PrintWriter(
								  new FileWriter("flower.txt"));		  
			Random ra = new Random();
			for(int i=0;i<5;i++){
				out.println(ra.nextInt(TIME)+" "+ra.nextInt(WIDTH)+" "+ra.nextInt(HEIGHT)+" "+ra.nextInt(VOLUMN)+" "+ra.nextInt(3));
			}
			for(int i=5;i<20;i++){
				out.println(ra.nextInt(2800)+" "+ra.nextInt(WIDTH)+" "+ra.nextInt(HEIGHT)+" "+ra.nextInt(VOLUMN)+" "+ra.nextInt(3));
			}
			out.close();
		}catch(FileNotFoundException e){
			System.out.println("Error:Cannot open file for writing.");
		}catch(IOException e){
			System.out.println("Error:Cannot write to file.");
		}
	}
}