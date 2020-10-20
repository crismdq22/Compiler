package CodigoIntermedio;

import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.Token;

public class TercetoColeccionIndice extends Terceto {
	
	private String tipo; //1 long 2 double
	//4 bytes int 8 bytes double

	public TercetoColeccionIndice(Token izq, Token medio, Token der,	int numeroTerceto) {
		super(izq, medio, der, numeroTerceto);
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
			assembler.append("MOV "+"CX,"+elementos.get(2).getAssembler()+System.lineSeparator());
			assembler.append("CMP "+"CX"+",AX" +System.lineSeparator());
			assembler.append("JGE "+ ConvertidorAssembler.labelFueraRango+System.lineSeparator());
			assembler.append("CMP "+"CX,0"+System.lineSeparator());
			assembler.append("JL "+ConvertidorAssembler.labelFueraRango+System.lineSeparator());
			assembler.append("MOVZX ECX,CX"+System.lineSeparator());
			assembler.append("IMUL "+"ECX,2"+System.lineSeparator());
			assembler.append("ADD "+" ECX,OFFSET "+elementos.get(1).getAssembler()+"+2 "+System.lineSeparator());
			assembler.append("MOV "+ "@aux"+ this.numeroTerceto + ",ECX"+System.lineSeparator());
		}else {
			assembler.append("MOV EAX, OFFSET "+elementos.get(1).getAssembler()+System.lineSeparator());
			assembler.append("MOV @param, EAX"+System.lineSeparator());
			assembler.append("CALL @FUNCTION_LengthD"+System.lineSeparator());
			assembler.append("MOV AX, @retI"+System.lineSeparator());
			assembler.append("MOV "+"CX,"+elementos.get(2).getAssembler()+System.lineSeparator());
			assembler.append("CMP "+"CX"+",AX" +System.lineSeparator());
			assembler.append("JGE "+ ConvertidorAssembler.labelFueraRango+System.lineSeparator());
			assembler.append("CMP "+"CX,0"+System.lineSeparator());
			assembler.append("JL "+ConvertidorAssembler.labelFueraRango+System.lineSeparator());
			assembler.append("MOVZX ECX,CX"+System.lineSeparator());
			assembler.append("IMUL "+"ECX,8"+System.lineSeparator());
			assembler.append("ADD "+" ECX,OFFSET "+elementos.get(1).getAssembler()+"+8 "+System.lineSeparator());
			assembler.append("MOV "+"@aux"+ this.numeroTerceto +",ECX"+System.lineSeparator());
		}
		return assembler.toString();
	}

}
