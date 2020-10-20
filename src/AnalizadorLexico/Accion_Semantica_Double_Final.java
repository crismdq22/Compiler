package AnalizadorLexico;

import java.math.BigDecimal;

public class Accion_Semantica_Double_Final implements Accion_Semantica {
	static final double MINIMO_POSITIVO = 2.2250738585072014 * Math.pow( 10, -308 );
	static final double MINIMO_NEGATIVO = -2.2250738585072014 * Math.pow(10 , -308 );
	static final double MAXIMO_POSITIVO = 1.7976931348623157 * Math.pow(10, 308 );
	static final double MAXIMO_NEGATIVO = -1.7976931348623157 * Math.pow(10, 308 );
	private boolean volver;
	private Error e;
	
	private int getIndice(String s) {
		return Math.max(s.indexOf("D"), s.indexOf("d"));
	}
	
	public Accion_Semantica_Double_Final(boolean vol, Error e) {
		this.volver = vol;
		this.e = e;
	}
	
	public Accion_Semantica_Double_Final(boolean vol) {
		this.volver = vol;
	}
	//Contemplamos que todo double que venga con exponente D esta normalizado
	@Override
	public int ejecutar(AnalizadorLexico lexico) {
		double mantisa;
		int exp;
		BigDecimal aux;
		if (e!=null) {
			lexico.addError(e);
		}
		if (volver) {
			lexico.getArchivo().retroceder();
		}	
		int i = getIndice(lexico.getBuffer_temporal());
		if (i>0) {
			mantisa = Double.parseDouble(lexico.getBuffer_temporal().substring(0,i));
			if (lexico.getBuffer_temporal().charAt(i+1) == '+') {
				exp = Integer.parseInt((lexico.getBuffer_temporal().substring(i+2,lexico.getBuffer_temporal().length())));	
			}else if (lexico.getBuffer_temporal().charAt(i+1) == '-'){
				exp = - Integer.parseInt((lexico.getBuffer_temporal().substring(i+2,lexico.getBuffer_temporal().length())));
			}else {
				exp = Integer.parseInt((lexico.getBuffer_temporal().substring(i+1,lexico.getBuffer_temporal().length())));
			}
			aux = new BigDecimal(mantisa);
			aux = aux.scaleByPowerOfTen(exp);
		} else 
			aux = new BigDecimal(lexico.getBuffer_temporal());
		if (aux.compareTo(new BigDecimal(MAXIMO_POSITIVO)) > 0) {
			Error e = new Error(AnalizadorLexico.warningConstante, "ERROR LEXICO");
			lexico.addWarning(e);
			aux = BigDecimal.valueOf(MAXIMO_POSITIVO);
		}
		else if ((aux.compareTo(new BigDecimal(MINIMO_POSITIVO)) < 0) && (aux.compareTo(BigDecimal.ZERO) != 0)) {
			Error e = new Error(AnalizadorLexico.warningConstante, "ERROR LEXICO");
			lexico.addWarning(e);
			aux = BigDecimal.valueOf(MINIMO_POSITIVO);
		}
		
		Token t = new Token(AnalizadorLexico.tipoDouble, AnalizadorLexico.CTEDOUBLE, aux.doubleValue());
		lexico.getTablaSimbolos().add(lexico.getBuffer_temporal(), t);
		lexico.setYYlex(lexico.getBuffer_temporal());
		return AnalizadorLexico.CTEDOUBLE;
		
		}
}
