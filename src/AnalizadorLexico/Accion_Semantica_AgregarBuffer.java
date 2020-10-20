package AnalizadorLexico;

public class Accion_Semantica_AgregarBuffer implements Accion_Semantica {
	private Error e;
	@Override
	public int ejecutar(AnalizadorLexico lexico) {
		if (e!=null) {
			lexico.addError(e);
		}
		lexico.setBuffer_temporal( new StringBuilder(lexico.getBuffer_temporal().concat(String.valueOf(lexico.getArchivo().getActual()))).toString());
		return -1;
	}
	public Accion_Semantica_AgregarBuffer(Error e) {
		this.e = e;
	}
	public Accion_Semantica_AgregarBuffer() {
	}
}
