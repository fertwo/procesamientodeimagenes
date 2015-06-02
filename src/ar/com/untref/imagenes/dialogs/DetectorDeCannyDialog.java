package ar.com.untref.imagenes.dialogs;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ar.com.untref.imagenes.enums.NivelMensaje;
import ar.com.untref.imagenes.helpers.DialogsHelper;
import ar.com.untref.imagenes.ventanas.VentanaPrincipal;
import ar.com.untref.imagenes.ventanas.VentanaRuido;

@SuppressWarnings("serial")
public class DetectorDeCannyDialog extends JDialog {

	private VentanaPrincipal ventana;
	private JButton botonConfirmar;
	private JLabel labelUmbral1;
	private JLabel labelUmbral2;
	private JTextField umbral1Elegido;
	private JTextField umbral2Elegido;
	private JLabel labelSigma1;
	private JLabel labelSigma2;
	private JTextField sigma1Elegido;
	private JTextField sigma2Elegido;
	private VentanaRuido ventanaRuido;
	private JPanel jpanel;

	public DetectorDeCannyDialog(VentanaPrincipal ventana, JPanel jpanel) {
		super(ventana);
		this.ventana = ventana;
		this.jpanel = jpanel;
		initUI();
	}

	public DetectorDeCannyDialog(VentanaRuido ventanaRuido, JPanel jpanel) {
		super(ventanaRuido);
		this.ventanaRuido = ventanaRuido;
		this.jpanel = jpanel;
		initUI();
	}

	private void initUI() {

		labelUmbral1 = new JLabel("Umbral 1");
		labelUmbral2 = new JLabel("Umbral 2");
		umbral1Elegido = new JTextField();
		umbral2Elegido = new JTextField();

		labelSigma1 = new JLabel("Sigma 1");
		labelSigma2 = new JLabel("Sigma 2");
		sigma1Elegido = new JTextField();
		sigma2Elegido = new JTextField();
		
		
		createLayout();

		setModalityType(ModalityType.APPLICATION_MODAL);

		setTitle("Umbralizaci�n con Hist�resis");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
	}

	private void createLayout() {

		botonConfirmar = new JButton("Listo");

		botonConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!umbral1Elegido.getText().toString().isEmpty() && !umbral2Elegido.getText().toString().isEmpty() && !sigma1Elegido.getText().toString().isEmpty() && !sigma2Elegido.getText().toString().isEmpty()) {
					
					try{
						int umbral1 = Integer.valueOf(umbral1Elegido.getText().toString());
						int umbral2 = Integer.valueOf(umbral2Elegido.getText().toString());
						int sigma1 = Integer.valueOf(sigma1Elegido.getText().toString());
						int sigma2 = Integer.valueOf(sigma2Elegido.getText().toString());
						
						if (umbral1 < umbral2){
							
							if (ventana != null){
								
								ventana.aplicarDetectorCanny(umbral1, umbral2, sigma1, sigma2);
							}
							
							if (ventanaRuido != null){
								
								ventanaRuido.aplicarDetectorCanny(umbral1, umbral2, sigma1, sigma2);
							}
							
							DetectorDeCannyDialog.this.dispose();
						} else {
							
							DialogsHelper.mostarMensaje(jpanel, "El umbral 1 debe ser menor al umbral 2", NivelMensaje.ERROR);
						}
					} catch (Exception ex){
						
						DialogsHelper.mostarMensaje(jpanel, "Por favor ingresa umbrales v�lidos", NivelMensaje.ERROR);
						ex.printStackTrace();
					}
				} else {
					
					DialogsHelper.mostarMensaje(jpanel, "Por favor ingresa umbrales v�lidos", NivelMensaje.ERROR);
				}
			}
		});
		
		Container pane = getContentPane();
		GroupLayout gl = new GroupLayout(pane);
		pane.setLayout(gl);

		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);

		gl.setHorizontalGroup(gl.createParallelGroup(Alignment.CENTER)
				.addComponent(labelSigma1).addComponent(labelSigma2)
				.addComponent(sigma1Elegido).addComponent(sigma2Elegido)
				.addComponent(labelUmbral1).addComponent(labelUmbral2)
				.addComponent(umbral1Elegido).addComponent(umbral2Elegido)
				.addComponent(botonConfirmar).addGap(200));

		gl.setVerticalGroup(gl.createSequentialGroup().addGap(30)
				.addComponent(labelSigma1).addGap(20).addComponent(sigma1Elegido).addGap(20)
				.addComponent(labelSigma2).addGap(20).addComponent(sigma2Elegido).addGap(20)
				.addComponent(labelUmbral1).addGap(20).addComponent(umbral1Elegido).addGap(20)
				.addComponent(labelUmbral2).addGap(20).addComponent(umbral2Elegido)
				.addGap(20).addComponent(botonConfirmar).addGap(30));

		pack();
	}
}