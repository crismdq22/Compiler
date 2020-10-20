package CodigoIntermedio;

import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.Token;

public class TercetoExpresionMult extends TercetoExpresion {

	private String tipo;
	
	public TercetoExpresionMult(Token izq, Token medio,	Token der, int numeroTerceto) {
		super(izq, medio, der, numeroTerceto);
		this.tipo = medio.getTipo();
		// TODO Auto-generated constructor stub
	}
	
	public String getAssembler() {
		StringBuilder assembler = new StringBuilder();
		if (tipo == AnalizadorLexico.tipoInt) {
			if (elementos.get(1).existeAtributo("Direccion")) {
				assembler.append("MOV ECX,"+elementos.get(1).getAssembler()+System.lineSeparator());
				assembler.append("MOV AX,[ECX]"+System.lineSeparator());
			}else
				assembler.append("MOV "+ "AX,"+ elementos.get(1).getAssembler()+System.lineSeparator());
			if (elementos.get(2).existeAtributo("Direccion")) {
				assembler.append("MOV ECX,"+elementos.get(2).getAssembler()+System.lineSeparator());
				assembler.append("IMUL AX,[ECX]"+System.lineSeparator());
			}else
				assembler.append("IMUL "+"AX," + elementos.get(2).getAssembler()+System.lineSeparator());
			assembler.append("MOV "+ "@aux"+ this.numeroTerceto + ",AX"+System.lineSeparator());
			assembler.append("JC "+ ConvertidorAssembler.labelOverflow+ System.lineSeparator());
		}else {
			if (elementos.get(1).existeAtributo("Direccion")) {
				assembler.append("MOV EAX,"+elementos.get(1).getAssembler()+System.lineSeparator());
				assembler.append("FLD QWORD PTR [EAX]"+System.lineSeparator());
			}else
				assembler.append("FLD "+elementos.get(1).getAssembler()+System.lineSeparator());
			if (elementos.get(2).existeAtributo("Direccion")) {
				assembler.append("MOV EAX,"+elementos.get(2).getAssembler()+System.lineSeparator());
				assembler.append("FLD QWORD PTR [EAX]"+System.lineSeparator());
			}else
				assembler.append("FLD "+elementos.get(2).getAssembler()+System.lineSeparator());
			assembler.append("FMUL "+System.lineSeparator());
			assembler.append("FST @aux"+this.numeroTerceto+System.lineSeparator());
			assembler.append("CALL @FUNCTION_MulCheck"+ System.lineSeparator());
		}
		return assembler.toString();
	}	

}
