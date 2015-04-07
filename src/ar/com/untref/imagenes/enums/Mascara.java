package ar.com.untref.imagenes.enums;

public enum Mascara {

	MEDIA("M�scara de la media"),
	PASA_ALTOS("M�scara pasa altos");
	
	private String descripcion;

	Mascara(String desc){
		
		this.descripcion = desc;
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
