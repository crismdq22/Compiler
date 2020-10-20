package CodigoIntermedio;

import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.Token;

public class TercetoComparacion extends Terceto {
	
	private String tipo;

	public TercetoComparacion(Token izq, Token medio, Token der, int numeroTerceto) {
		super(izq, medio, der, numeroTerceto);
		// TODO Auto-generated constructor stub
		this.tipo = medio.getTipo();
	}

	public String getAssembler() {
		StringBuilder assembler = new StringBuilder();
			if  (tipo == AnalizadorLexico.tipoInt) {
				if (elementos.get(1).existeAtributo("Direccion")) {
					assembler.append("MOV ECX,"+elementos.get(1).getAssembler()+System.lineSeparator());
					assembler.append("MOV AX,[ECX]"+System.lineSeparator());
				}else
					assembler.append("MOV "+ "AX,"+ elementos.get(1).getAssembler()+System.lineSeparator());
				if (elementos.get(2).existeAtributo("Direccion")) {
					assembler.append("MOV ECX,"+elementos.get(2).getAssembler()+System.lineSeparator());
					assembler.append("CMP AX,[ECX]"+System.lineSeparator());
				}else
					assembler.append("CMP "+"AX,"+ elementos.get(2).getAssembler()+System.lineSeparator());
			}else {
				if (elementos.get(2).existeAtributo("Direccion")) {
					assembler.append("MOV EAX,"+elementos.get(2).getAssembler()+System.lineSeparator());
					assembler.append("FLD QWORD PTR [EAX]"+System.lineSeparator());
				}else
					assembler.append("FLD "+ elementos.get(2).getAssembler()+System.lineSeparator());
				if (elementos.get(1).existeAtributo("Direccion")) {
					assembler.append("MOV EAX,"+elementos.get(1).getAssembler()+System.lineSeparator());
					assembler.append("FLD QWORD PTR [EAX]"+System.lineSeparator());
				}else
					assembler.append("FLD "+ elementos.get(1).getAssembler()+System.lineSeparator());
				assembler.append("FCOMPP "+System.lineSeparator());
				assembler.append("FSTSW estado "+System.lineSeparator());
				assembler.append("MOV AX,estado "+System.lineSeparator());
				assembler.append("SAHF "+System.lineSeparator());
			}
		return assembler.toString();
	}

}
