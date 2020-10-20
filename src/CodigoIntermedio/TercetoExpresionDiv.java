package CodigoIntermedio;

import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.Token;

public class TercetoExpresionDiv extends TercetoExpresion{
	private String tipo;
	
	public TercetoExpresionDiv(Token izq, Token medio,	Token der, int numeroTerceto) {
		super(izq, medio, der, numeroTerceto);
		elementos.get(0).AgregarAtributo("Tipo", elementos.get(1).getAtributo("Tipo"));
		this.tipo = medio.getTipo();
	}
	
	public String getAssembler() {
		StringBuilder assembler = new StringBuilder();
		if (tipo == AnalizadorLexico.tipoInt) {
			if (elementos.get(2).existeAtributo("Direccion")) {
				assembler.append("MOV EAX,"+elementos.get(2).getAssembler()+System.lineSeparator());
				assembler.append("MOV CX,[EAX]"+System.lineSeparator());
			}else
				assembler.append("MOV "+ "CX,"+ elementos.get(2).getAssembler()+System.lineSeparator());
			assembler.append("CMP "+ "CX,"+ "0"+System.lineSeparator());
			assembler.append("JE "+ ConvertidorAssembler.labelDivCero+System.lineSeparator());// meter label error div por zero aca.
			if (elementos.get(1).existeAtributo("Direccion")) {
				assembler.append("MOV EDX,"+elementos.get(1).getAssembler()+System.lineSeparator());
				assembler.append("MOV AX,[EDX]"+System.lineSeparator());
			}else
				assembler.append("MOV "+ "AX,"+ elementos.get(1).getAssembler()+System.lineSeparator());
			assembler.append("IDIV "+"CX"+System.lineSeparator());
			assembler.append("MOV "+ "@aux"+ this.numeroTerceto + ",AX"+System.lineSeparator());
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
			assembler.append("FTST "+System.lineSeparator());
			assembler.append("FSTSW estado "+System.lineSeparator());
			assembler.append("MOV AX,estado "+System.lineSeparator());
			assembler.append("SAHF "+System.lineSeparator());
			assembler.append("JE "+ConvertidorAssembler.labelDivCero+System.lineSeparator());
			assembler.append("FDIV "+System.lineSeparator());
			assembler.append("FSTP "+"@aux"+this.numeroTerceto+System.lineSeparator());
		}
		return assembler.toString();
	}
}
