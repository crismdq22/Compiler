package CodigoIntermedio;

import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.Token;

public class TercetoColeccionLast extends Terceto {

	private String tipo;
	
	public TercetoColeccionLast(Token izq, Token medio, Token der, int numeroTerceto) {
		super(izq, medio, der, numeroTerceto);
		this.tipo = medio.getTipo();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getAssembler() {
		StringBuilder assembler = new StringBuilder();
		if (tipo == AnalizadorLexico.tipoInt) {
			assembler.append("MOV EAX, OFFSET "+ elementos.get(1).getAssembler()+System.lineSeparator());
			assembler.append("MOV @param, EAX"+System.lineSeparator());
			assembler.append("CALL @FUNCTION_LastI"+System.lineSeparator());
			assembler.append("MOV AX, @retI"+System.lineSeparator());
			assembler.append("MOV "+ "@aux"+ this.numeroTerceto + ",AX"+System.lineSeparator());
		}else {
			assembler.append("MOV EAX, OFFSET "+elementos.get(1).getAssembler()+System.lineSeparator());
			assembler.append("MOV @param, EAX"+System.lineSeparator());
			assembler.append("CALL @FUNCTION_LastD"+System.lineSeparator());
			assembler.append("FLD @retD"+System.lineSeparator());
			assembler.append("FSTP "+ "@aux"+ this.numeroTerceto +System.lineSeparator());
		}
		return assembler.toString();
	}

}
