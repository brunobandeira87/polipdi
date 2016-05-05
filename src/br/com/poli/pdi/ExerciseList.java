package br.com.poli.pdi;


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;


import javax.imageio.ImageIO;

public class ExerciseList {

	public static void main(String[] args) throws IOException {
		
		
		String path = ""; //COLOCAR O CAMINHO DA IMAGEM AQUI
		
		String filename = "lista3";
		String ext = ".jpg";
		
		double[][][] image = readImage(path + filename + ext);
		double[][] gray = PDI.toGray(image);
		//melhor radius = 2;
		int radius = 4;
		saveImage(path + filename + "_median_radius_" + radius +  ext, mediana(gray, radius));
		
		System.out.println("DONE!");
		

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

			throw new RuntimeException("Fidey" + e.getCause());
		}
		
	}
	
	public static double[][] mediana(double[][] image, int radius){
		
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
	
	public static double[][] toGray(double[][][] imagem){
		// construtor que tem que passar largura, altura e tipo de imagem (GRAY)
		double[][] ret = new double[imagem[0].length][imagem[0][0].length];
		double[] currentPixels = new double[3];
		BufferedImage newCopy = new BufferedImage(imagem[0][0].length, imagem[0].length, BufferedImage.TYPE_BYTE_GRAY);

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
	
	//TODO 
	public static double[][][] readImage(String filePath){
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

}
