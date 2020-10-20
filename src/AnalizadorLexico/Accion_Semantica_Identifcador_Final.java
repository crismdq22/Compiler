package AnalizadorLexico;

public class Accion_Semantica_Identifcador_Final implements Accion_Semantica {
	private boolean volver;
	private Error e;
	@Override
	public int ejecutar(AnalizadorLexico lexico) {
		String aux = lexico.getBuffer_temporal();
		if (volver) {
			lexico.getArchivo().retroceder();
		}	
		if (e!=null) {
			lexico.addError(e);
		}
		switch (lexico.getBuffer_temporal()) {
		case "if":
			return AnalizadorLexico.IF;
		case "end_if":
			return AnalizadorLexico.ENDIF;
		case "else":
			return AnalizadorLexico.ELSE;
		case "print":
			return AnalizadorLexico.PRINT;
		case "int":
			return AnalizadorLexico.INT;
		case "begin":
			return AnalizadorLexico.BEGIN;
		case "end":
			return AnalizadorLexico.END;
		case "double":
			return AnalizadorLexico.DOUBLE;
		case "do":
			return AnalizadorLexico.DO;
		case "until":
			return AnalizadorLexico.UNTIL;
		case "first":
			return AnalizadorLexico.first;
		case "last":
			return AnalizadorLexico.last;
		case "length":
			return AnalizadorLexico.length;
		default:
			if (lexico.getBuffer_temporal().length() > 25) {
				aux = lexico.getBuffer_temporal().substring(0, 25);
				lexico.addWarning(new Error(AnalizadorLexico.warningIdentificador, "ERROR LEXICO"));
			}
			Token t = new Token("Identificador", AnalizadorLexico.ID, aux);
			lexico.getTablaSimbolos().add(lexico.getBuffer_temporal(), t);
			lexico.setYYlex(lexico.getBuffer_temporal());
			return AnalizadorLexico.ID;
			
		}
	}
	public Accion_Semantica_Identifcador_Final(boolean volver, Error e) {
		this.volver = volver;
		this.e = e;
	}
	public Accion_Semantica_Identifcador_Final(boolean volver) {
		this.volver = volver;
	}
}
