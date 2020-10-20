package AnalizadorLexico;

public class Accion_Semantica_CadenaFinal implements Accion_Semantica{
	private boolean volver;
	private Error e;
	@Override
	public int ejecutar(AnalizadorLexico lexico) {
		// TODO Auto-generated method stub
		if (e!=null) {
			lexico.addError(e);
		}
		Token t = new Token("Cadena Multilínea", AnalizadorLexico.MULTI_LINEA, lexico.getBuffer_temporal());
		lexico.getTablaSimbolos().add(lexico.getBuffer_temporal(), t);
		lexico.setYYlex(lexico.getBuffer_temporal());
		if (volver) {
			lexico.getArchivo().retroceder();
		}
		return AnalizadorLexico.MULTI_LINEA;
	}
	
	public Accion_Semantica_CadenaFinal(boolean vol, Error e) {
		this.volver = vol;
		this.e = e;
	}
	
	public Accion_Semantica_CadenaFinal(boolean vol) {
		this.volver = vol;
	}
}
