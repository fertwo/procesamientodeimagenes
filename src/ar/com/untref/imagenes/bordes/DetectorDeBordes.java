package ar.com.untref.imagenes.bordes;

import java.awt.image.BufferedImage;
import java.awt.image.Kernel;

import ar.com.untref.imagenes.enums.Canal;
import ar.com.untref.imagenes.filtros.Filtro;
import ar.com.untref.imagenes.filtros.FiltroNuevo;
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.procesamiento.MatricesManager;

public class DetectorDeBordes {

	public static BufferedImage aplicarDetectorDeRoberts(Imagen imagenOriginal){
		
		float[][] mascaraDeRobertsEnX = new float [2][2];
		mascaraDeRobertsEnX[0][0]= 1;
		mascaraDeRobertsEnX[0][1]= 0;
		mascaraDeRobertsEnX[1][0]= 0;
		mascaraDeRobertsEnX[1][1]= -1;
		
		float[][] mascaraDeRobertsEnY = new float [2][2];
		mascaraDeRobertsEnY[0][0]= 0;
		mascaraDeRobertsEnY[0][1]= 1;
		mascaraDeRobertsEnY[1][0]= -1;
		mascaraDeRobertsEnY[1][1]= 0;
		
		BufferedImage im = new BufferedImage(imagenOriginal.getBufferedImage().getHeight(), imagenOriginal.getBufferedImage().getWidth(), BufferedImage.TYPE_3BYTE_BGR);
		Imagen imagenFiltradaEnX = new Imagen(im, imagenOriginal.getFormato(), imagenOriginal.getNombre());
		Imagen imagenFiltradaEnY = new Imagen(im, imagenOriginal.getFormato(), imagenOriginal.getNombre());
		Imagen imagenResultante = new Imagen(im, imagenOriginal.getFormato(), imagenOriginal.getNombre());
		
		int xPixel = 0;
		int yPixel = 0;
		int ancho1 = mascaraDeRobertsEnX.length;
        int alto1 = mascaraDeRobertsEnX[0].length;
        int tam1 = ancho1 * alto1;
        float filtroK1[] = new float[tam1];
        
    	int ancho2 = mascaraDeRobertsEnY.length;
        int alto2 = mascaraDeRobertsEnY[0].length;
        int tam2 = ancho2 * alto2;
        float filtroK2[] = new float[tam2];
        
      //Creamos el filtro - Se pasa de una matriz cuadrada (vector de 2 dimensiones) a un vector lineal
        for(int i=0; i < ancho1; i++){
            for(int j=0; j < alto1; j++){
                filtroK1[i*ancho1 + j] = mascaraDeRobertsEnX[i][j];
            }
        }
        
      //Creamos el filtro - Se pasa de una matriz cuadrada (vector de 2 dimensiones) a un vector lineal
        for(int i=0; i < ancho2; i++){
            for(int j=0; j < alto2; j++){
                filtroK2[i*ancho2 + j] = mascaraDeRobertsEnY[i][j];
            }
        }
        
        Kernel kernelX = new Kernel(ancho1, alto1, filtroK1);
        Filtro filtroX = new Filtro(kernelX);
        
        Kernel kernelY = new Kernel(ancho2, alto2, filtroK2);
        Filtro filtroY = new Filtro(kernelY);
        
        //Aplicamos filtros
        filtroX.filter(imagenOriginal.getBufferedImage(), imagenFiltradaEnX.getBufferedImage());
        filtroY.filter(imagenOriginal.getBufferedImage(), imagenFiltradaEnY.getBufferedImage());
        		
        		
        //Creamos la imagen resultante
        for (int i = 0; i < ancho1; i++) {
            for (int j = 0; j < alto1; j++) {
                xPixel = imagenFiltradaEnX.getBufferedImage().getRGB(i, j);
                yPixel = imagenFiltradaEnY.getBufferedImage().getRGB(i, j);
                imagenResultante.getBufferedImage().setRGB(i, j, (int)Math.hypot(xPixel, yPixel));
            }
        }
        
        return imagenResultante.getBufferedImage();
        
		
	}
	
	public static BufferedImage aplicarDetectorDePrewitt(Imagen imagenOriginal){
		
		int[][] mascaraDePrewittEnX = calcularMascaraDePrewittEnX();
		int[][] mascaraDePrewittEnY = calcularMascaraDePrewittEnY();
		
        Imagen imagenFiltradaEnX = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		Imagen imagenFiltradaEnY = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		
		Imagen imagenResultante = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		
        FiltroNuevo filtroEnX = new FiltroNuevo(mascaraDePrewittEnX);
        
        //Aplicamos filtros en X y en Y
        int[][] matrizRojoEnX = filtroEnX.filtrar(imagenFiltradaEnX, mascaraDePrewittEnX, Canal.ROJO);
        int[][] matrizVerdeEnX = filtroEnX.filtrar(imagenFiltradaEnX, mascaraDePrewittEnX, Canal.VERDE);
        int[][] matrizAzulEnX = filtroEnX.filtrar(imagenFiltradaEnX, mascaraDePrewittEnX, Canal.AZUL);
        		
        int[][] matrizRojoEnY = filtroEnX.filtrar(imagenFiltradaEnY, mascaraDePrewittEnY, Canal.ROJO);
        int[][] matrizVerdeEnY = filtroEnX.filtrar(imagenFiltradaEnY, mascaraDePrewittEnY, Canal.VERDE);
        int[][] matrizAzulEnY = filtroEnX.filtrar(imagenFiltradaEnY, mascaraDePrewittEnY, Canal.AZUL);
        		
        //Sintetizamos usando la raiz de los cuadrados
        int[][] matrizRojosSintetizadosYTransformados = MatricesManager.aplicarTransformacionLineal(sintetizar(matrizRojoEnX, matrizRojoEnY));
        int[][] matrizVerdesSintetizadosYTransformados = MatricesManager.aplicarTransformacionLineal(sintetizar(matrizVerdeEnX, matrizVerdeEnY));
        int[][] matrizAzulesSintetizadosYTransformados = MatricesManager.aplicarTransformacionLineal(sintetizar(matrizAzulEnX, matrizAzulEnY));

        imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizRojosSintetizadosYTransformados, matrizVerdesSintetizadosYTransformados, matrizAzulesSintetizadosYTransformados));
        
        return imagenResultante.getBufferedImage();
	}

	private static int[][] calcularMascaraDePrewittEnY() {
		
		int[][] mascaraDePrewittEnY = new int [3][3];
		mascaraDePrewittEnY[0][0]= -1;
		mascaraDePrewittEnY[0][1]= 0;
		mascaraDePrewittEnY[0][2]= 1;
		mascaraDePrewittEnY[1][0]= -1;
		mascaraDePrewittEnY[1][1]= 0;
		mascaraDePrewittEnY[1][2]= 1;
		mascaraDePrewittEnY[2][0]= -1;
		mascaraDePrewittEnY[2][1]= 0;
		mascaraDePrewittEnY[2][2]= 1;
		return mascaraDePrewittEnY;
	}

	private static int[][] calcularMascaraDePrewittEnX() {
		
		int[][] mascaraDePrewittEnX = new int [3][3];
		mascaraDePrewittEnX[0][0]= -1;
		mascaraDePrewittEnX[0][1]= -1;
		mascaraDePrewittEnX[0][2]= -1;
		mascaraDePrewittEnX[1][0]= 0;
		mascaraDePrewittEnX[1][1]= 0;
		mascaraDePrewittEnX[1][2]= 0;
		mascaraDePrewittEnX[2][0]= 1;
		mascaraDePrewittEnX[2][1]= 1;
		mascaraDePrewittEnX[2][2]= 1;
		
		return mascaraDePrewittEnX;
	}
	
	private static int[][] sintetizar(int[][] matrizEnX, int[][] matrizEnY) {

		int[][] matrizFinal  = new int[matrizEnX.length][matrizEnX[0].length]; 
		
		for (int i=0; i<matrizEnX.length ;i++){
			for (int j=0; j<matrizEnX[0].length ;j++){
				
				matrizFinal[i][j] = (int) Math.hypot(matrizEnX[i][j], matrizEnY[i][j]);
			}
		}
		
		return matrizFinal;
	}

	public static BufferedImage aplicarDetectorDeSobel(Imagen imagenOriginal){
		
		float[][] mascaraDeSobelEnX = new float [3][3];
		mascaraDeSobelEnX[0][0]= -1;
		mascaraDeSobelEnX[0][1]= -2;
		mascaraDeSobelEnX[0][2]= -1;
		mascaraDeSobelEnX[1][0]= 0;
		mascaraDeSobelEnX[1][1]= 0;
		mascaraDeSobelEnX[1][2]= 0;
		mascaraDeSobelEnX[2][0]= 1;
		mascaraDeSobelEnX[2][1]= 2;
		mascaraDeSobelEnX[2][2]= 1;
		
		float[][] mascaraDeSobelEnY = new float [3][3];
		mascaraDeSobelEnY[0][0]= -1;
		mascaraDeSobelEnY[0][1]= 0;
		mascaraDeSobelEnY[0][2]= 1;
		mascaraDeSobelEnY[1][0]= -2;
		mascaraDeSobelEnY[1][1]= 0;
		mascaraDeSobelEnY[1][2]= 2;
		mascaraDeSobelEnY[2][0]= -1;
		mascaraDeSobelEnY[2][1]= 0;
		mascaraDeSobelEnY[2][2]= 1;
		
        BufferedImage im = new BufferedImage(imagenOriginal.getBufferedImage().getWidth(), imagenOriginal.getBufferedImage().getHeight(), imagenOriginal.getBufferedImage().getType());
		Imagen imagenFiltradaEnX = new Imagen(im, imagenOriginal.getFormato(), imagenOriginal.getNombre());
		Imagen imagenFiltradaEnY = new Imagen(im, imagenOriginal.getFormato(), imagenOriginal.getNombre());
		Imagen imagenResultante = new Imagen(im, imagenOriginal.getFormato(), imagenOriginal.getNombre());
		
        int xPixel = 0;
		int yPixel = 0;

		int ancho1 = mascaraDeSobelEnX.length;
        int alto1 = mascaraDeSobelEnX[0].length;
        int tam1 = ancho1 * alto1;
        float filtroK1[] = new float[tam1];
        
        int ancho2 = mascaraDeSobelEnY.length;
        int alto2 = mascaraDeSobelEnY[0].length;
        int tam2 = ancho2 * alto2;
        float filtroK2[] = new float[tam2];
        
        
      //Creamos el filtro - Se pasa de una matriz cuadrada (vector de 2 dimensiones) a un vector lineal
        for(int i=0; i < ancho1; i++){
            for(int j=0; j < alto1; j++){
                filtroK1[i*ancho1 + j] = mascaraDeSobelEnX[i][j];
            }
        }
        
      //Creamos el filtro - Se pasa de una matriz cuadrada (vector de 2 dimensiones) a un vector lineal
        for(int i=0; i < ancho2; i++){
            for(int j=0; j < alto2; j++){
                filtroK2[i*ancho2 + j] = mascaraDeSobelEnY[i][j];
            }
        }
        
        Kernel kernelX = new Kernel(ancho1, alto1, filtroK1);
        Filtro filtroX = new Filtro(kernelX);
        
        Kernel kernelY = new Kernel(ancho2, alto2, filtroK2);
        Filtro filtroY = new Filtro(kernelY);
        
        //Aplicamos filtros
        filtroX.filter(imagenOriginal.getBufferedImage(), imagenFiltradaEnX.getBufferedImage());
        filtroY.filter(imagenOriginal.getBufferedImage(), imagenFiltradaEnY.getBufferedImage());
        		
        		
        //Creamos la imagen resultante
        for (int i = 0; i < ancho1; i++) {
            for (int j = 0; j < alto1; j++) {
                xPixel = imagenFiltradaEnX.getBufferedImage().getRGB(i, j);
                yPixel = imagenFiltradaEnY.getBufferedImage().getRGB(i, j);
                imagenResultante.getBufferedImage().setRGB(i, j, (int)Math.hypot(xPixel, yPixel));
            }
        }
        
        return imagenResultante.getBufferedImage();
		
	}
	
	public static BufferedImage aplicarDetectorLaplaciano(Imagen imagenOriginal){
		
		float[][] mascaraDeLaplaciano = new float [3][3];
		mascaraDeLaplaciano[0][0]= 0;
		mascaraDeLaplaciano[0][1]= 1;
		mascaraDeLaplaciano[0][2]= 0;
		mascaraDeLaplaciano[1][0]= 1;
		mascaraDeLaplaciano[1][1]= -4;
		mascaraDeLaplaciano[1][2]= 1;
		mascaraDeLaplaciano[2][0]= 0;
		mascaraDeLaplaciano[2][1]= 1;
		mascaraDeLaplaciano[2][2]= 0;
		
		int ancho = mascaraDeLaplaciano.length;
        int alto = mascaraDeLaplaciano[0].length;
        int tam = ancho * alto;
        float filtroK[] = new float[tam];
        
        BufferedImage im = new BufferedImage(imagenOriginal.getBufferedImage().getWidth(), imagenOriginal.getBufferedImage().getHeight(), imagenOriginal.getBufferedImage().getType());
		Imagen imagenFiltrada = new Imagen(im, imagenOriginal.getFormato(), imagenOriginal.getNombre());
		Imagen imagenResultante = new Imagen(im, imagenOriginal.getFormato(), imagenOriginal.getNombre());
        
        
      //Creamos el filtro - Se pasa de una matriz cuadrada (vector de 2 dimensiones) a un vector lineal
        for(int i=0; i < ancho; i++){
            for(int j=0; j < alto; j++){
                filtroK[i*ancho + j] = mascaraDeLaplaciano[i][j];
            }
        }
        
        Kernel kernel = new Kernel(ancho, alto, filtroK);
        Filtro filtro = new Filtro(kernel);
        
        //Aplicamos filtros
        filtro.filter(imagenOriginal.getBufferedImage(), imagenFiltrada.getBufferedImage());
        		
        		
        //Creamos la imagen resultante
        for (int i = 0; i < ancho; i++) {
            for (int j = 0; j < alto; j++) {
                int pixel = imagenFiltrada.getBufferedImage().getRGB(i, j);
                imagenResultante.getBufferedImage().setRGB(i, j, pixel);
            }
        }
        
        return imagenResultante.getBufferedImage();
	}
	
}
