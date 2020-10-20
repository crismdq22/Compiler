package AnalizadorLexico;

public class Accion_Semantica_Error implements Accion_Semantica {
	private Error e;
	@Override
	public int ejecutar(AnalizadorLexico lexico) {
		lexico.addError(e);
		lexico.setBuffer_temporal("");
		return -1;
	}
	public Accion_Semantica_Error(Error e) {
		this.e = e;
	}
	
}
