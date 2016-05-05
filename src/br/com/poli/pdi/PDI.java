package br.com.poli.pdi;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;


public class PDI {

	
	public static double[][][] readColorfulImage(String filePath){
		File f = new File(filePath);
		BufferedImage imagem = null;
		System.out.println(filePath);
		System.out.print("Reading Image...\t ");
		
		try {
			imagem = ImageIO.read(f);
			System.out.println(" [ OK ]");
		} catch (Exception e) {

			throw new RuntimeException("Erro ao tentar ler o arquivo. CAUSE: " + e.getCause());
		}
		
		double[][][] ret = new double[3][imagem.getHeight()][imagem.getWidth()];
		for (int i = 0; i < imagem.getWidth(); i++) {
			for (int j = 0; j < imagem.getHeight(); j++) {
				double[] currentPixel = new double[3];
				imagem.getRaster().getPixel(i, j, currentPixel);
				ret[0][j][i] = currentPixel[0];
				ret[1][j][i] = currentPixel[1];
				ret[2][j][i] = currentPixel[2];
			}
		}
		
		return ret;
	}
	
	/**
	 * Le imagem e retorna na forma de matriz de double
	 * @param filePath
	 * @return
	 * @throws RuntimeException
	 */
	
	public static double[][] readImage(String filePath) throws RuntimeException{
		File f = new File(filePath);
		BufferedImage image;
		
		System.out.print("Reading Image...\t");
		
		// carregar a imagem através do seu path
		try {
			image = ImageIO.read(f);
			
			System.out.println(" [ OK ]");
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("Treta das boas: " + e.getCause() );
		}
		// cria uma matriz de double em que os valores dos pixels da imagem original
		// serão atribuidos aos pixels da nova imagem, que está "EM BRANCO"
		
		int i = 0, j = 0;
		double[] currentPixel = new double[1];
		double ret[][] = new double [ image.getHeight() ][ image.getWidth() ];
		int width = image.getWidth();
		int height = image.getHeight();
		
		try{
			// loop atraves das fotos, percorrendo pixel linha por coluna. da esquerad para direita, de cima para baixo. 
			// lembrar que o plano cartesiano da imagem é invertido. X é ordenada e Y é abcissa, no plano cartesiano 3
			
			for (i = 0; i < width; i++) {
				for (j = 0; j < height; j++) {
					// cria um array de uma posicao para ser o Pixel atual da imagem 
					// que estamos percorrendo no loop
					
					// entender o que é Raster e essa função 
					// diz respeito a pegar o valor do pixel atual na imagem original
					// e guardar nesse array temporario 
					image.getRaster().getPixel(i, j, currentPixel);
					
					
					// preenchimento dessa forma, pois a imagem é altura x largura	
					ret[j][i] = currentPixel[0];
					
					
				}
			}
			
		}catch(Exception e ){
			System.out.println("olha a merda: " + i + " " + j);
			
		}
		
		
		return ret;
	}
	
	/**
	 * 
	 * @param image
	 * @return
	 */

	public static double[][] readImage(BufferedImage image){
		double[][] ret = new double[image.getHeight()][image.getWidth()];
		
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				double[] currentPixel = new double[1];
				image.getRaster().getPixel(i, j, currentPixel);				
				// não entendo porque está trocado				
				ret[j][i] = currentPixel[0];
			}
		}
		
		return ret;
	}
	
	/**
	 * Imagem funciona como uma matriz de 3 dimensao. 
	 * image[x][][] => camada RGB
	 * image[][x][] => altura da imagem
	 * image[][][x] => largura da imagem
	 * @param imagem
	 * @return
	 */
	public static double[][] toGray(double[][][] imagem){
		// construtor que tem que passar largura, altura e tipo de imagem (GRAY)
		double[][] ret = new double[imagem[0].length][imagem[0][0].length];
		double[] currentPixels = new double[3];
		BufferedImage newCopy = new BufferedImage(imagem[0][0].length, imagem[0].length, BufferedImage.TYPE_BYTE_GRAY);
		//System.out.println("SIZE: " + imagem[0][0].length + " x " + imagem[0].length);
		//System.out.println("IMAGE: " + newCopy.getWidth() + " x " + newCopy.getHeight());
		int i = 0, j = 0;
		// looop varredo imagem onde i = linha e j = coluna
		try{
			for ( i = 0; i < imagem[0][0].length; i++) {
				for ( j = 0; j < imagem[0].length; j++) {
					// cria um array temporario com os valores dos pixels em cada canal RGB
					
					currentPixels[0] =  imagem[0][j][i];
					currentPixels[1] =  imagem[1][j][i]; 
					currentPixels[2] =  imagem[2][j][i];
					// atribui a nova imagem o valor medio do pixel em termos de tons de cinza
					newCopy.getRaster().setPixel(i, j, currentPixels);
				}
			}
			ret = readImage(newCopy);
		} catch(Exception e){
			System.out.println("Numero da variavel : " + i + " " + j + "\n");
			
		}
		return ret;
	}

	public static void saveImage(String filePath, double[][] image) throws IOException{
		BufferedImage newImg = new BufferedImage(image[0].length, image.length, BufferedImage.TYPE_BYTE_GRAY);

		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[i].length; j++) {
				
				//gray image -> gray
				double[] currentPixel = new double[] {image[i][j]};
				newImg.getRaster().setPixel(j, i, currentPixel);
				
				  
				  
				/*
				colored -> gray
				double[] currentPixel = new double[] {image[i][j]};
				newImg.getRaster().setPixel(i, j, currentPixel);
				 * */
			}
		}
		
		try {
			ImageIO.write(newImg, "jpg", new File(filePath));
		} catch (IOException e) {
			// TODO: handle exception
			throw new RuntimeException("Fidey" + e.getCause());
		}
		
	}
	
	
	
	public static double[][] mediaCamada(double[][] image, int amount){
		double[][][] totalImages = new double[amount][image.length][image[0].length];
		double[][] ret = new double[image.length][image[0].length];
		
		for(int k = 0 ; k < amount ; k++){
			totalImages[k] = saltAndPepperNoise(image); 
		}
		
		for(int i = 0; i < totalImages[0].length; i++){
			for (int j = 0; j < totalImages[0][0].length; j++) {
				ret[i][j] = 0;
				for(int k = 0; k < totalImages.length; k++){
					ret[i][j] += totalImages[k][i][j];
				}
				ret[i][j] /= totalImages.length;
			}
		}
		
		return ret;
	}
	
	/*
	 * Noise Generator
	 */
	
	
	/**
	 * Salt and Pepper
	 * @param image
	 * @return
	 */
	
	public static double[][] saltAndPepperNoise(double[][] image){
		double[][] ret = new double[image.length][image[0].length];
		
		for(int i = 0 ; i < image.length; i++){
			for(int j = 0; j < image[0].length; j++){
				if(Math.random() < 0.1){
					ret[i][j] = 0;
				}
				else if(Math.random() < 0.15){
					ret[i][j] = 255;
				}
				else{
					ret[i][j] = image[i][j];
				}
			}
		}
		
		return ret;
	}
	
	
	public static double[][] saltNoise(double[][] image){
		double[][] ret = new double[image.length][image[0].length];
		
		for(int i = 0 ; i < image.length; i++){
			for(int j = 0; j < image[0].length; j++){
				if(Math.random() < 0.15){
					ret[i][j] = 255;
				}
				
				else{
					ret[i][j] = image[i][j];
				}
			}
		}
		
		return ret;
	}
	
	
	
	/**
	 * 
	 * A mean filter smooths local variations in an image and noise is reduced 
	 * as a result of blurring
	 * @param image
	 * @param radius
	 * @return
	 */
	
	public static double[][] arithmeticMeanFilter(double[][] image, int radius){
		double[][] ret = new double[image.length][image[0].length];
		int raio = radius;
		double[][] mask = new double[2 * raio + 1][2 * raio + 1];
		double kernel = 1 / Math.pow((2 * raio + 1),2);		
		
		for(int i = raio ; i < image.length - (2 * raio + 1); i++){
			for (int j = raio ; j < image[0].length - (2 * raio + 1); j++){
				double middle = 0.0;
				for(int k = 0; k < mask.length; k++){
					for(int l = 0 ; l < mask[0].length; l++){
						middle += image[i+k][j+l] * kernel;
					}
				}

				ret[i][j] = middle; 
			}
		}
		
		return ret;
	}
	
	/**
	 * 
	 * Geometric Mean achieves smoothing comparable to arithmetic mean filter,
	 * but it tends to lose less image detail in the process.
	 * 
	 * 
	 * Melhor aplicavel quando tem mais pixels com maior intesidade
	 * RUIDO tipo Salt 
	 * @param image
	 * @param radius
	 * @return
	 */
	
	public static double[][] geometricMeanFilter(double[][] image, int radius){
		double[][] ret = new double[image.length][image[0].length];
		int raio = radius;
		double[][] mask = new double[2 * raio + 1][2 * raio + 1];
		
		double expoent =  (double)(1 / Math.pow(2 * raio + 1, 2));
		
		for(int i = raio ; i < image.length - (2 * raio + 1); i++){
			for (int j = raio ; j < image[0].length - (2 * raio + 1); j++){
				double product = 1;
				for(int k = 0; k < mask.length; k++){
					for(int l = 0 ; l < mask[0].length; l++){
						product *= image[i+k][j+l];
						
					}
				}
				

				ret[i][j] = Math.pow(product, expoent); 
				
			}
		}
		
		return ret;
	}
	//FIXME
	public static double[][] harmonicMeanFilter(double[][] image, int radius){
		double[][] ret = new double[image.length][image[0].length];
		int raio = radius;
		double[][] mask = new double[2 * raio + 1][2 * raio + 1];
		double kernel = 1 / Math.pow((2 * raio + 1),2);		
		double pixels =  mask.length * mask[0].length;
		System.out.println(pixels);
		
		for(int i = raio ; i < image.length - (2 * raio + 1); i++){
			for (int j = raio ; j < image[0].length - (2 * raio + 1); j++){
				double denominador = 0;
				for(int k = 0; k < mask.length; k++){
					for(int l = 0 ; l < mask[0].length; l++){
						
						if(image[i+k][j+l] == 0){
							denominador += 1;
						}
						else {
							denominador += 1 / image[i+k][j+l];
						}
						
						/*
						 * if(image[i+k][j+l] > 0){
							denominador += Math.round(1.0 / image[i+k][j+l]);
							//System.out.println(image[i+k][j+l]);
						}
						else if(image[i+k][j+l] == 0){
							denominador = 1.0;
						}
						else if(image[i+k][j+l] > 255){
							denominador = 255;
						}
						 */
						//System.out.println(denominador);
						
					}
				}
				
			

				ret[i][j] = Math.round((double)pixels / denominador);
				
				//System.out.println(ret[i][j]);
				//System.out.println(temp);
				
			}
		}
		
		return ret;
		
	}
	
	public static double[][] contraharmonicMeanFilter(double[][] image, int radius){
		double[][] ret = new double[image.length][image[0].length];
		int raio = radius;
		double[][] mask = new double[2 * raio + 1][2 * raio + 1];
		double kernel = 1 / Math.pow((2 * raio + 1),2);		
		double Q = 1.2;
		for(int i = raio ; i < image.length - (2 * raio + 1); i++){
			for (int j = raio ; j < image[0].length - (2 * raio + 1); j++){
				double numerador = 0.0;
				double denominador = 0.0;
				for(int k = 0; k < mask.length; k++){
					for(int l = 0 ; l < mask[0].length; l++){
						numerador += Math.pow(image[i+k][j+l], Q+1);
						denominador += Math.pow(image[i+k][j+l], Q);
					}
				}

				ret[i][j] = Math.round(numerador / denominador);
				//System.out.println(ret[i][j]);
			}
		}
		
		return ret;

	}
	
	
	// ORDERED FILTERS
	public static double[][] medianFilter(double[][] image, int radius){
		
		double[][] ret = new double[image.length][image[0].length];
		double[][] mask = new double[2 * radius + 1][2 * radius + 1];
		
		for(int i = radius ; i < image.length - (2 * radius + 1); i++){
			for(int j = radius ; j < image[0].length - (2 * radius + 1); j++){
				double[] array = new double[mask.length * mask.length];
				int count = 0;
				for(int k = 0 ; k < mask.length; k++){
					for(int l = 0 ; l < mask[0].length; l++){
						
						array[count] = image[k+i][l+j]; 
						count++;
					}
					//System.out.println(count);
				}
				Arrays.sort(array);
				ret[i][j] = array[array.length/2];
			}
		}
		
		return ret;
	}
	
	
	
	private static double getHighestPixel(double[][] image){
		double ret = Double.MIN_VALUE;		
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				if(ret < image[i][j]){
					ret = image[i][j];
				}
			}			
		}
		
		return ret;
	}
	
	private static double getLowestPixel(double[][] image){
		double ret = Double.MAX_VALUE;
		
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				if(image[i][j] < ret){
					ret = image[i][j];
				}
			}
		}
		
		return ret;
	}
	
	public static double basicGrayScaleTransformation(double r, double maxPixelValue, int typeOfFilter){
		double ret = 0;
		double c = 7;
		double k = 7;
		double gama = 0.24;
		switch(typeOfFilter){		
			case 1:
				ret = maxPixelValue - r;
				break;
			case 2:
				ret = c * Math.log(1 + r);
				break;
			case 3: 
				ret = k * Math.pow(r, gama);
				break;
		
		}
		
		return ret;
	}
	
	public static double[][] negative(double[][] image){
		double[][] ret = new double[image.length][image[0].length];
		double grayScaleMaximum = getHighestPixel(image);
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				ret[i][j] =  basicGrayScaleTransformation(image[i][j], grayScaleMaximum, 1);				
			}
		}		
		
		return ret;
	}
	
	public static double[][] logaritmica(double[][] image){
		double[][] ret = new double[image.length][image[0].length];
		double grayScaleMaximum = getHighestPixel(image);
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				// O valor de C é setado no método basicGrayScaleTransformation
				ret[i][j] =  basicGrayScaleTransformation(image[i][j], grayScaleMaximum, 2);				
			}
		}		
		
		return ret;
	}
	
	public static double[][] powerLaw(double[][] image){
		double[][] ret = new double[image.length][image[0].length];
		double grayScaleMaximum = getHighestPixel(image);
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				// O valor de C é setado no método basicGrayScaleTransformation
				ret[i][j] =  basicGrayScaleTransformation(image[i][j], grayScaleMaximum, 3);				
			}
		}		
		
		return ret;
	}
	
	 
	private static int[] getHistogram(double[][] image){
		int[] ret = new int[(int)(getHighestPixel(image) + 1)];
		
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				ret[(int)image[i][j]]++; 			
			}
		}		
		
		return ret;
	}
	
	/**
	 * Equalização do Histograma.
	 * @param image
	 * @return double[][] imagem
	 */
	
	public static double[][] histogramEqualization(double[][] image){
		System.out.println(variancia(image));
		int[] histogram = getHistogram(image);
//		double[] probabilidade = new double[histogram.length];

		int size = image.length * image[0].length;
		double[] probabilidade = probabilidade(histogram, size);
		
		
		double[][] ret = new double[image.length][image[0].length];
		
		// calculando a probabilidade para cada valor de pixel aparecer na imagem
		
		
		// calculando a probabilidade acumulativa
		int[] cumulativeProbabilidade = cumulativeDistribution(probabilidade, size);	
		
		// mapear os valores de níveis de cinza para os novos valores
		//printHistogram(cumulativeProbabilidade);
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				int pixelValue = (int) image[i][j];
				ret[i][j] = cumulativeProbabilidade[pixelValue];
			}
		}
		//System.out.println(getLowestPixel(image));
		//System.out.println(getHighestPixel(image));
		return ret;
	}
	
	/**
	 * Calcular a probabilidade de cada intensidade de cinza da imagem aparece na imagem
	 * @param histogram
	 * @param pixels
	 * @return
	 */
	private static double[] probabilidade(int[] histogram, int pixels){
		double[] ret = new double[histogram.length];		
		for (int i = 0; i < histogram.length; i++) {
			ret[i] = (double) histogram[i] / pixels;
		}
		
		return ret;
	}
	
	private static int average(double[][] image){
		double ret = 0.0;
		double pixels = image.length * image[0].length;
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				ret += image[i][j];
			}
		}
		ret /= pixels;
		
		return (int)Math.round(ret);
	}
	
	
	public static double avarageProb(double[] probabilidade){
		double ret = 0.0;
		
		for (int i = 0; i < probabilidade.length; i++) {
			ret += i * probabilidade[i];
		}
		
		return ret;
	}
	
	
	
	
	public static double standartDesvio(double[][] image){
		double ret = Math.sqrt(variancia(image));
		return ret;
	}
	
	public static double variancia(double[][] image){
		double ret = 0.0;
		int[] histogram = getHistogram(image);
//		double[] probabilidade = new double[histogram.length];

		int size = image.length * image[0].length;
		double pixels = image.length * image[0].length;
		double[] probabilidade = probabilidade(histogram, size);
		double avg = Math.round(avarageProb(probabilidade));
		System.out.println(avg);
		
		/*
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				double current = Math.round(Math.pow((image[i][j] - avg), 2));
				if(current > 255){
					current = 255;
				}
				ret += current * probabilidade[(int)image[i][j]];
				
			}
		}
			*/
		
		
		//ret /= pixels;
		for (int i = 0; i < probabilidade.length; i++) {
			 ret += Math.pow((i-avg),2) * probabilidade[i];
		}
		System.out.println(Math.sqrt(ret));
		return Math.round(ret);
		
	
	}
	
	
	public static double[][] gaussianNoise(double[][] image){
		double[][] ret = new double[image.length][image[0].length];
		double variance = variancia(image);
		int pixels = image.length * image[0].length;
		double stand = standartDesvio(image);
		stand = 30;
		variance = 900; 
		double avg = average(image);
		avg = 30;
		int[] histogram = getHistogram(image);
		double[] prob = new double[256];
		
		for (int i = 0; i < prob.length; i++) {
			double expo = - 1 * Math.pow(i - avg,2) /  (2 * variance);
			double dem = Math.sqrt(2 * Math.PI * stand);
			prob[i] = ((Math.exp(expo) / dem));
			
			//System.out.println(prob[i]);
		}
		int[] cumulativeProbabilidade = cumulativeDistribution(prob, pixels);	
		
		// mapear os valores de níveis de cinza para os novos valores
		//printHistogram(cumulativeProbabilidade);
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				int pixelValue = (int) image[i][j];
				ret[i][j] = cumulativeProbabilidade[pixelValue];
				if(ret[i][j] > 0)
				System.out.println(ret[i][j]);
			}
		}
		
		/*
		for (int i = 0; i < ret.length; i++) {
			for (int j = 0; j < ret[0].length; j++) {
				double expo = - 1 * Math.pow(image[i][j] - avg,2) /  (2 * variance);
				double dem = Math.sqrt(2 * Math.PI * stand);
				ret[i][j] = Math.round(image[i][j] + (Math.exp(expo) / dem));
				System.out.println((Math.exp(expo) / dem));
						
						
						
				//ret[i][j] = image[i][j] + 
			}
		}
		*/
		return ret;
	}
	
	/*
	 * transformar as probabilidades, somando a atual com a anterior. depois multiplicar
	 * pela quantidade de tons de cinza e arrendondar esse valor.
	 */
	
	private static int[] cumulativeDistribution(double[] probabilidade, int pixels){
		int[] ret = new int[probabilidade.length];
		double cumulative = 0;
		for (int i = 0; i < probabilidade.length; i++) {
			cumulative += probabilidade[i] / pixels;
			ret[i] = (int) Math.round(cumulative * (probabilidade.length - 1 ));
		}
		
		
		return ret;
	}
	
	
	//funcao de binarização de uma imagem, através de um limiar (MAX/2)
	
	public static double[][] thresholding(double[][] image, int bits){
		double[][] ret = new double[image.length][image[0].length];
		
		int middle = (int)Math.pow(2, bits) / 2;
		int max = (int)Math.pow(2, bits) - 1;
		int min = 0;
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				ret[i][j] = (image[i][j] >= middle) ?   max :  min; 
			}
		}
		
		return ret;
	}
	
	public static double[][] transformationHightlightsByKeepingOthers(double[][] image, int a, int b){
		double[][] ret = new double[image.length][image[0].length];		
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				ret[i][j] = (image[i][j] >= a && image[i][j] <= b) ? 225 : image[i][j];
			}
		}		
		return ret;
	}
	
	/**
	 * Transforma a imagem, dado um intervalo [A,B]. Reduz a intensidade dos pixels [0,A) e (B,L-1)
	 * @param image
	 * @param a
	 * @param b
	 * @return
	 */
	public static double[][] transformationHightlightsByReducingOthers(double[][] image, int a, int b){
		double[][] ret = new double[image.length][image[0].length];		
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				ret[i][j] = (image[i][j] >= a && image[i][j] <= b) ? 225 : 64;
			}
		}		
		return ret;
	}
	
	//TODO
	public static double[][] decomposingImage(double[][] image, int numberOfLayers, int[] layers){
		double[][] ret = new double[image.length][image[0].length];
		double[][][] temp = new double[numberOfLayers][image.length][image[0].length]; 
		for (int i = 0; i < temp.length; i++) {
			temp[i] = nthBit(image, layers[i]);		
		}
		
		for (int i = 0; i < temp[0].length; i++) {
			for (int j = 0; j < temp[0][0].length; j++) {
				for (int k = 0; k < temp.length; k++) {
					ret[i][j] += temp[k][i][j];
					
				}
			}
		}
					
		return ret;
	}
	
	
	private static double[][] nthBit(double[][] image, int numberOfLayer){
		double[][] ret = new double[image.length][image[0].length];
		
		int avg = (int) Math.pow(2,numberOfLayer -1);
		System.out.println(avg);
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				
				ret[i][j] = (image[i][j] <= avg) ? 0 : 1;
				ret[i][j] *= avg;
				
			}
		}
		
		return ret;
	}
	
	public static double globalAverage(double[][] imagem){
		double ret = 0.0;
		
		for (int i = 0; i < imagem.length; i++) {
			for (int j = 0; j < imagem[0].length; j++) {
				ret += imagem[i][j];
			}
		}
		ret /= imagem.length * imagem[0].length;
		
		return Math.round(ret);
	}
	
	public static double variance(double[][] imagem){
		double ret = 0.0;
		
		int[] histogram = getHistogram(imagem);
		
		double[] probabilidade = probabilidade(histogram, imagem.length * imagem[0].length);
		double pixels = imagem.length * imagem[0].length;
		double globalAverage = average(imagem);
		System.out.println(globalAverage);
		
		for (int i = 0; i < imagem.length; i++) {
			for (int j = 0; j < imagem[0].length; j++) {
				ret += Math.pow(imagem[i][j] - globalAverage, 2);
			}
		}
		ret /= pixels;
		/*
		for (int i = 0; i < probabilidade.length; i++) {
			ret += Math.pow(histogram[i] - globalAverage, 2) * probabilidade[i];
		}
		*/
		//ret /= imagem[0].length * imagem.length;
		return Math.sqrt(ret);
	}
	
	public static void localHistogram(double[][] imagem, int radius){
		double[][] mask = new double[2 * radius + 1][2 * radius + 1];
		double[][] ret = new double[imagem.length][imagem[0].length];
		double localAverage = 0.0;
		double localVariance = 0.0;
		double globalAverage = average(imagem);
		double globalVariance = variance(imagem);
		double temp = 0.0;
		double[] grays = new double[255];
		
		/**
		 * OBS: 
		 * 
		 * O VALOR DE K0 < 1.0
		 * 
		 * O VALOR DE K1 < K2
		 * 
		 * O VALOR DE K2 SERÁ > 1 SE QUISER MELHORAR AREAS CLARAS
		 * OU SERA <= 1 SE QUISERMOS MELHORAR AREAS ESCURAS
		 * 
		 * K1 < K2 LOWER LIMIT
		 * 
		 * 
		 * g(x,y) = E * f(x,y) , if msxy <= k0mg && k1dg <= dsxy <= k2dg
		 * else
		 * g(x,y) = f(x,y)
		 * 
		 * 
		 * msxy => media local na vizinhança do pixel xy
		 * mg => media global da imagem
		 * dsxy => desvio padrao na vizinhança
		 * dg => desvio padrao na imagem
		 * 
		 * 
		 * 1. Calcular media global
		 * 2. Calcular desvio padrao global
		 * 
		 * 
		 * 
		 */
		
		
		for (int i = 0; i < imagem.length; i++) {
			for (int j = 0; j < imagem[0].length; j++) {
				if( (i < radius || i >= imagem.length - radius) &&
						(j < radius || j >= imagem[0].length - radius) ){
					ret[i][j] = imagem[i][j];
				}
				else{
					// Calculando a ocorrencia dos pixels na janela 
					for (int k = 0; k < mask.length; k++) {
						for (int l = 0; l < mask[0].length; l++) {
							grays[(int)imagem[i+k][j+l]]++;
						}
					}
					System.out.println("hue");
					// dividir as intensidades de pixels pela quantidade de pixels na janela
					for (int z = 0; z < grays.length; z++) {
						grays[z] /= mask.length * mask[0].length; 
					}
					
					// calcular a media local dos pixels
					
					double sum = 0.0;
					
					for (int k = 0; k < grays.length; k++) {
						sum += k * grays[k];
					}
					System.out.println("SUM: " + sum);
					/*
					double lastOne = 0;
					for (int x = 0; x < grays.length; x++) {
						
						lastOne += grays[x];
						grays[x] = (int)(Math.round(lastOne * grays.length - 1));
						
					}
					
					for (int k = 0; k < mask.length; k++) {
						for (int l = 0; l < grays.length; l++) {
							
						}
					}
					*/
				}
			}
		}
		
	}
	
	private static void getLocalProb(int i, int j, double[][]imagem, int bits){
		bits = 255;
		
		
		
	}
	
	public static void DFT(double[][] image){
		double[][] ret = new double[image.length][image[0].length];
		double real = 0;
		double imaginary = 0;
		double temp = 0;
		double currentPixel = 0;
		for (int x = 0; x < image.length; x++) {
			for (int y = 0; y < image[0].length; y++) {
				temp = 0;
				real = 0;
				imaginary = 0;
				currentPixel = image[x][y];
				for (int u = 0; u < image[0].length; u++) {
					for (int v = 0; v < image.length; v++) {
						/*
						real += Math.cos(2 * Math.PI * (u * x  + v * y / (image.length))) / ;
						imaginary += -1 * Math.cos(2 * Math.PI * (u * x  + v * y / (image.length))) /;
						*/
					}
				}
				ret[x][y] = real + imaginary;
			}
		}
	}
	
	public static double[][] substractImage(double[][] original, double[][] blur){
		double[][] ret = new double[original.length][original[0].length];
		double[][] mask = new double[original.length][original[0].length];
		double a = 2;
		double hightestPixel = getHighestPixel(original);
		for (int i = 0; i < ret.length; i++) {
			for (int j = 0; j < ret[0].length; j++) {
				if(original[i][j] - blur[i][j] >= 0){
					mask[i][j] = original[i][j] - blur[i][j];
				}
				else{
					mask[i][j] = 0;
				}
				
				if((original[i][j] + a * mask[i][j]) <= 255){
					ret[i][j] = (original[i][j] + a * mask[i][j]);
				}
				else{ 
					ret[i][j] = hightestPixel;
				}
			}
		}
		
		return ret;
	}
	 
	

	/**
	 * Filtro laplaciano - 2 Derivada
	 * @param image
	 * @return
	 */
	
	public static double[][] laplacean(double[][] image){
		double[][] ret = new double[image.length][image[0].length];	
		for (int i = 0; i < ret.length; i++) {
			for (int j = 0; j < ret[0].length; j++) {
				if((i+1 < ret.length-10 && j+1 < ret[0].length-10 && i - 1 >= 0 && j - 1 >= 0)){
					//System.out.println(i + "\t" + j);
					double temp = image[i+1][j] + image[i-1][j] + image[i][j+1] + image[i][j-1] - 4 * image[i][j];
					
					if(temp >= 0 ) {
						ret[i][j] = temp; 
					}
					else{ 
						ret[i][j] = 0;
					}							
					
				}
				else{
					ret[i][j] = 0;
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * Filtro de Sobel Horizontal
	 * @param image
	 * @return
	 */
	
	public static double[][] verticalSobel(double[][] image){
		double[][] ret = new double[image.length][image[0].length];
		double[][] mask = {{1,0,-1},{2,0,-2}, {1,0,-1}};
		
		
		
		for (int i = mask.length; i < image.length - mask.length; i++) {
			for (int j = mask[0].length; j < image[0].length - mask[0].length; j++) {
				double newValue = 0;
				
				for (int k = 0; k < mask.length; k++) {
					for (int l = 0; l < mask[0].length; l++) {
						newValue += (mask[k][l] * image[i+k][j+l]) / 9;
					}
					
				}
				if(newValue < 0){
					newValue = 0;
				}
				ret[i][j] = newValue;
						
			}
		}
		
		return ret;
	}
	
	public static double[][] horizontalSobel(double[][] image){
		double[][] ret = new double[image.length][image[0].length];
		double[][] mask = {{-1,-2,-1},{0,0,0}, {1,2,1}};
		
		
		
		for (int i = mask.length; i < image.length - mask.length; i++) {
			for (int j = mask[0].length; j < image[0].length - mask[0].length; j++) {
				double newValue = 0;
				
				for (int k = 0; k < mask.length; k++) {
					for (int l = 0; l < mask[0].length; l++) {
						newValue += (mask[k][l] * image[i+k][j+l]) / 9;
					}
					
				}
				if(newValue < 0){
					newValue = 0;
				}
				ret[i][j] = newValue;
						
			}
		}
		
		return ret;
	}
	
	public static void desviopadrao(double[][] matrix){
		//double[][] ret = null;
		double ret = 0.0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				
			}
		}
		
		
		
	}
	
	//TODO 
	public static double[][] gaussianNoise(double[][] image, int radius){
		double[][] ret = new double[image.length][image[0].length];
		
		
		// calcular a probabilidade 
		// definir a variancia
		// definir 
		int[] histogram = getHistogram(image);
		
		
		
		
		
		return ret;
	}
	
	private static void printHistogram(int[] histogram){
		System.out.println("Pixel\tQuantidade");
		for (int i = 0; i < histogram.length; i++) {
			System.out.println(i + "\t" + histogram[i]);
		}
		
	}
	
	private static void printImage(double[][] image){
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				System.out.print(image[i][j] + "\t");
			}
			System.out.println("");
		}
	}
	
}
