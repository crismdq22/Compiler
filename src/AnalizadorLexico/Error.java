package AnalizadorLexico;

public class Error {
	public String msjError;
	private int nroLinea;
	private String tipo;

	public String showError() {
		return msjError+ " " + nroLinea;
	}
	
	public Error(String msj, String tipo, int nroL) {
		this.msjError = msj;
		this.nroLinea = nroL;
		this.tipo = tipo;
	}
	
	public Error(String msj, String tipo) {
		this.msjError = msj;
		this.tipo = tipo;
	}
	
	public void setNrolinea (int nro) {
		this.nroLinea=nro;
	}
	
	public String getDescripcion() {
		return this.tipo;
	}
	
	public String toString() {
		return (this.msjError + ' ' + nroLinea + '\n');
		
	}
}
