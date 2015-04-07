package ar.com.untref.imagenes.listeners;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import ar.com.untref.imagenes.procesamiento.ProcesadorDeImagenes;
import ar.com.untref.imagenes.ventanas.VentanaPrincipal;

public class MarcarImagenListener implements MouseListener{
	
	private VentanaPrincipal ventanaPrincipal;
	private Integer x1;
	private Integer y1;
	private Integer x2;
	private Integer y2;
	private static JLabel labelImagenMarcada;

	public MarcarImagenListener(VentanaPrincipal ventana) {

		this.ventanaPrincipal = ventana;
	}
	
	
	@Override
	public void mouseClicked(MouseEvent click) {

		if (x1==null){
			
			x1 = click.getX();
			y1= click.getY();
		} else {
			
			x2 = click.getX();
			y2= click.getY();

			labelImagenMarcada = ProcesadorDeImagenes.obtenerInstancia().marcarImagenActual(x1, y1, x2, y2, ventanaPrincipal);
			((Component) click.getSource()).removeMouseListener(this);
			ventanaPrincipal.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	public static JLabel getLabelImagenMarcada(){
		return labelImagenMarcada;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
		ventanaPrincipal.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
		ventanaPrincipal.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}