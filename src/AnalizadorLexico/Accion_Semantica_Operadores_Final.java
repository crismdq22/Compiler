package AnalizadorLexico;

public class Accion_Semantica_Operadores_Final implements Accion_Semantica {
	private int codigo;
	private boolean volver;
	private Error e;
	@Override
	public int ejecutar(AnalizadorLexico lexico) {
		if (volver) {
			lexico.getArchivo().retroceder();
		}
		else{
			lexico.setBuffer_temporal(lexico.getBuffer_temporal() + lexico.getArchivo().getActual());
		}
		if (e!=null) {
			lexico.addError(e);
		}
		return this.codigo;
	}
	public Accion_Semantica_Operadores_Final(int n, boolean volver, Error e) {
		this.codigo=n;
		this.volver = volver;
		this.e = e;
	}

	public Accion_Semantica_Operadores_Final(int n, boolean volver) {
		this.codigo=n;
		this.volver = volver;
	}
}
