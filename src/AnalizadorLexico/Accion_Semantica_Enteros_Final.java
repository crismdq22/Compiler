package AnalizadorLexico;

import java.math.BigDecimal;

public class Accion_Semantica_Enteros_Final implements Accion_Semantica{
	private Error e;
	private boolean volver;
	
	@Override
	public int ejecutar(AnalizadorLexico lexico) {
		int aux;
		if (e!=null) {
			lexico.addError(e);
		}
		if (volver) {
			lexico.getArchivo().retroceder();
		}
		if( new BigDecimal(lexico.getBuffer_temporal()).compareTo(new BigDecimal(2).pow(15)) > 0) {
			Error e = new Error(AnalizadorLexico.warningConstante, "ERROR LEXICO");
			lexico.addWarning(e);
			aux = (int) (Math.pow(2, 15));
		}else 
			aux = Integer.parseInt(lexico.getBuffer_temporal());

		Token t = new Token(AnalizadorLexico.tipoInt, AnalizadorLexico.CTEENTERA, aux);
		lexico.getTablaSimbolos().add(lexico.getBuffer_temporal(), t);
		lexico.setYYlex(lexico.getBuffer_temporal());
		return AnalizadorLexico.CTEENTERA;
	}
	public Accion_Semantica_Enteros_Final(boolean volver, Error e) {
		this.volver = volver;
		this.e = e;
	}
	public Accion_Semantica_Enteros_Final(boolean volver) {
		this.volver = volver;
	}

}
