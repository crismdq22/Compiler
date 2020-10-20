package CodigoIntermedio;

import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.Token;

public class TercetoColeccionLength extends Terceto {

	private String tipo;
	
	public TercetoColeccionLength(Token izq, Token medio, Token der, int numeroTerceto) {
		super(izq, medio, der, numeroTerceto);
		// TODO Auto-generated constructor stub
		this.tipo = medio.getTipo();
	}

	@Override
	public String getAssembler() {
		StringBuilder assembler = new StringBuilder();
		if (tipo == AnalizadorLexico.tipoInt) {
			assembler.append("MOV EAX, OFFSET "+elementos.get(1).getAssembler()+System.lineSeparator());
			assembler.append("MOV @param, EAX"+System.lineSeparator());
			assembler.append("CALL @FUNCTION_LengthI"+System.lineSeparator());
			assembler.append("MOV AX, @retI"+System.lineSeparator());
			assembler.append("MOV "+ "@aux"+ this.numeroTerceto + ",AX"+System.lineSeparator());
		}else
		{
			assembler.append("MOV EAX, OFFSET "+elementos.get(1).getAssembler()+System.lineSeparator());
			assembler.append("MOV @param, EAX"+System.lineSeparator());
			assembler.append("CALL @FUNCTION_LengthD"+System.lineSeparator());
			assembler.append("MOV AX, @retI"+System.lineSeparator());
			assembler.append("MOV "+ "@aux"+ this.numeroTerceto + ",AX"+System.lineSeparator());
		}
		return assembler.toString();
	}

}
