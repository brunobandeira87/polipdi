package br.com.poli.pdi;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;



public class Test {

	public static void main(String[] args) throws IOException  {
		
		String path = "/home/bandeira/Documents/university/20161/pdi/workspace/PDI/images/";
		//Scanner input = new Scanner(System.in);
		path += "lista3/";
		String filename = "figura2";
		String ext = ".jpg";
		
		double[][][] image = PDI.readColorfulImage(path + filename + ext);
		double[][] gray = PDI.toGray(image);
		
		PDI.saveImage(path + filename + "_horizontalSobel" + ext, PDI.horizontalSobel(gray));
		PDI.saveImage(path + filename + "_verticalSobel" + ext, PDI.verticalSobel(gray));
		
		/*
		gaussian();
		int bits = 8;
		int[] layers = {8,5,4};
		
		boolean loopRunning = true;
		
		PDI.saveImage(path + filename + "_lapacean" + ext, PDI.laplacean(gray));
		PDI.saveImage(path + filename + "_horizontalSobel" + ext, PDI.horizontalSobel(gray));
		PDI.saveImage(path + filename + "_verticalSobel" + ext, PDI.verticalSobel(gray));
		int radius = 1;
		//double[][] salt = PDI.saltAndPepperNoise(gray);
		double[][] salt = gray;
		//PDI.gaussianNoise(gray);
		/*
		System.out.println("oi:" + PDI.variancia(gray));
		PDI.saveImage(path + filename + "_median_radius_" + radius +  ext, PDI.medianFilter(salt, radius));
		PDI.saveImage(path + filename + "_geometric_radius_" + radius +  ext, PDI.geometricMeanFilter(salt, radius));
		PDI.saveImage(path + filename + "_harmonic_radius_" + radius +  ext, PDI.harmonicMeanFilter(salt, radius));
		PDI.saveImage(path + filename + "_contraharmonic_radius_" + radius +  ext, PDI.contraharmonicMeanFilter(salt, radius));
		PDI.saveImage(path + filename + "_salt" + radius +  ext, salt);
		PDI.saveImage(path + filename + "_gaussian" +  ext, PDI.gaussianNoise(gray));
		 */
		
		
		/*
		while(loopRunning){
			System.out.println("Choose an option below:");
			System.out.println("1 - Apply Filter");
			System.out.println("2 - Histogram Equalization");
			System.out.print("\nOption Chosen: " );
			int menu = input.nextInt();
			switch(menu){
				case 1:
					System.out.println("\n1 - Operation on a single pixel");
					System.out.println("\n1 - Operation on a neighbourhood pixel");
					System.out.print("\nOption Chosen: " );
					int submenu = input.nextInt();
					switch(submenu){
						case 1:
							PDI.saveImage(path + filename + "_powerlaw" + ext, PDI.powerLaw(gray));
							PDI.saveImage(path + filename + "_log" + ext, PDI.logaritmica(gray));
							PDI.saveImage(path + filename + "_negative" + ext, PDI.negative(gray));
							break;
						case 2: 
								System.out.println("\nChoose a mask radius: ");
								int radius = input.nextInt();
								double[][] blur = PDI.localAveraging(gray, radius);
								PDI.saveImage(path + filename + "_substarct" + ext, PDI.substractImage(gray, blur));
								PDI.saveImage(path + filename + "_sobel" + ext, PDI.sobel(gray));
								PDI.saveImage(path + filename + "_mediana" + ext, PDI.mediana(gray, radius));
								PDI.saveImage(path + filename + "_ponderada" + ext, PDI.localAveraging(gray, radius));
							break;
						default:
							break;
					}
					
					break;
					
				case 2:
					PDI.saveImage(path + filename + "_histogramEqualized" + ext, PDI.histogramEqualization(gray));
					PDI.localHistogram(gray, 3);
					break;
				default:
					loopRunning = false;
					break;
					
			}
			
		}
	*/
		System.out.println("DONE!");
	}
	
	public static void contraHarmonica(){
		double[][] matrix = {{126,52,2},{74,111,203},{42,31,247}};
		double ret = 9;
		
		double denominador = 0.0;
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				denominador += 1 / matrix[i][j];
			}
		}
		
		System.out.println(ret / (denominador));
		
		
	}
	
	public static void variancia(){
		double[][] matrix = {{126,52,2},{74,111,203},{42,31,247}};
		double media = media(matrix);
		double ret = 0.0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				double current = Math.pow((matrix[i][j] - media)/9.0, 2);
				if(current > 255){
					current = 255;
				}
				ret += current;
				
			}
		}
		System.out.println(ret/9.0);
	}
	
	public static double media(double[][]matrix){
		double ret = 0.0;
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				ret += matrix[i][j];
			}
		}
		ret /= matrix.length * matrix[0].length;
		return ret;
		
		
		
		
	}
	
	public static void multiplicar(){
		int x = 3;
		int ret = 0;
		int y = 2;
		
		int iteracoes = 0;
		int temp = 0;
		int inner = 0;
		int current = 0;
		while(iteracoes < y){
			inner = 0;
			while(inner < x){
				if(iteracoes == 0 && inner == 0){
					
					temp += x;
					current = temp;
				}else{
					temp +=current;
				}
				inner++;
			}
			ret = temp;
			iteracoes++;
		
		}
		ret = current;
		
		System.out.println("x^y = " + ret);
	}
	
	public static void gaussian(){
		
		double media = 20;
		double desvio = 30;
		double cum = 0;
		double[] probabilidade = new double[(int)desvio * 2];
		double[] pk = new double[(int)desvio * 2];
		for (int i = 0; i < probabilidade.length; i++) {
			double expo = - 1 * Math.pow(i - media,2) /  (2 * desvio * desvio);
			double dem = Math.sqrt(2 * Math.PI * desvio);
			probabilidade[i] = ((Math.exp(expo) / dem));
			cum += probabilidade[i];
			pk[i] = cum;
			//System.out.println(probabilidade[i]);
		}
		cum = 0;
		double hightest = pk[pk.length - 1];
		System.out.println(hightest);
		System.out.println(hightest);
		for (int i = 0; i < pk.length; i++) {
			pk[i] /= hightest;
			cum += pk[i];
		}
		System.out.println(cum);
		
		/*
		int deloc = -2;
		int[] values = {-9,-8,-7,-6,-5,-4,-3,-2,-1,0,1,2,3,4,5,6,7,8,9};
		double[] prob = new double[values.length];
		int media = 0;
		double cum = 0.0;
		double desv = 30;
		
		for (int i = 0; i < values.length; i++) {
			double expo = - 1 * Math.pow(values[i] - media,2) /  (2 * desv * desv);
			double dem = Math.sqrt(2 * Math.PI * desv);
			prob[i] = ((Math.exp(expo) / dem));
			System.out.println(prob[i]);
		}
		 */
	}
		
		
	

}
