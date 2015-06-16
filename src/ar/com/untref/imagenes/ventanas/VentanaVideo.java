package ar.com.untref.imagenes.ventanas;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import ar.com.untref.imagenes.enums.FormatoDeImagen;
import ar.com.untref.imagenes.helpers.DialogsHelper;
import ar.com.untref.imagenes.listeners.MarcarFotogramaListener;
import ar.com.untref.imagenes.modelo.Fotogramas;
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.procesamiento.ProcesadorDeImagenes;
import ar.com.untref.imagenes.procesamiento.ProcesadorDeVideo;
import ar.com.untref.imagenes.segmentacion.Segmentador;

@SuppressWarnings("serial")
public class VentanaVideo extends JFrame{
	
	private JPanel panelBotones;
	private JLabel labelPrincipal;
	private JButton botonSegmentar;
	
	public VentanaVideo() {
		this.setTitle("Procesamiento de Video");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		getContentPane().setLayout(null);
		
		labelPrincipal = new JLabel("");
		labelPrincipal.setHorizontalAlignment(SwingConstants.CENTER);
		labelPrincipal.setBounds(0, 0, 774, 474);
		getContentPane().add(labelPrincipal);
		
		panelBotones = new JPanel();
		panelBotones.setBounds(0, 485, 774, 43);
		getContentPane().add(panelBotones);
		
		JPanel panel1 = new JPanel();
		panelBotones.add(panel1);
		
		botonSegmentar = new JButton("Segmentar");
		botonSegmentar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ProcesadorDeVideo procesador = ProcesadorDeVideo.obtenerInstancia();
				
				BufferedImage imagenNueva = ProcesadorDeImagenes.obtenerInstancia().clonarBufferedImage(procesador.getImagenActual().getBufferedImage()); 
				Imagen image = new Imagen(imagenNueva, FormatoDeImagen.JPEG, "segmentada");
				
				BufferedImage bufferSegmentado = Segmentador.segmentarImagen(image, 
						new Point(procesador.getX1(), procesador.getY1()), 
						new Point(procesador.getX2(), procesador.getY2()), 100, 80);
				
//				Imagen imagenSegmentada = new Imagen(bufferSegmentado, procesador.getImagenActual().getFormato(), procesador.getImagenActual().getNombre()+"_segmentada");
				VentanaVideo.this.refrescarImagen(bufferSegmentado);
			}
		});
		botonSegmentar.setHorizontalAlignment(SwingConstants.LEFT);
		botonSegmentar.setEnabled(false);
		panel1.add(botonSegmentar);
		
		JButton botonSeleccionar = new JButton("Seleccionar");
		botonSeleccionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DialogsHelper.mostarMensaje(getContentPane(), "Cliquea en la esquina superior izquierda y la inferior derecha que formar�n el cuadrado para marcar una regi�n en la imagen");
				labelPrincipal.addMouseListener(new MarcarFotogramaListener(VentanaVideo.this));
				botonSegmentar.setEnabled(true);
			}
		});
		panel1.add(botonSeleccionar);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNuevoVideo = new JMenu("Nuevo Video");
		menuBar.add(mnNuevoVideo);
		
		JMenuItem mntmHoja = new JMenuItem("Abuela");
		mntmHoja.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ProcesadorDeVideo.obtenerInstancia().cargarVideo(Fotogramas.ABUELA);
				refrescarImagen(ProcesadorDeVideo.obtenerInstancia().getImagenActual().getBufferedImage());
			}
		});
		mnNuevoVideo.add(mntmHoja);
		
	}
	
	public void refrescarImagen(BufferedImage imagen) {

		labelPrincipal.setIcon(new ImageIcon(imagen));
	}

	public JLabel getPanelDeImagen() {

		return labelPrincipal;
	}

	public void habilitarBotonSegmentar() {

		botonSegmentar.setEnabled(true);
	}
}