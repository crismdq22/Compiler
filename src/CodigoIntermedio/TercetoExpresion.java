package CodigoIntermedio;

import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.Token;

public class TercetoExpresion extends Terceto {

	private String tipo;

	public TercetoExpresion(Token izq, Token medio,	Token der, int numeroTerceto) {
		super(izq, medio, der, numeroTerceto);
		this.tipo = medio.getTipo();
	}
	
	private String convertirOperador(String op){
		if (op == "+") return "ADD";
		else return "SUB";
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
				assembler.append(convertirOperador(elementos.get(0).getValor())+" AX,[ECX]"+System.lineSeparator());
			}else
				assembler.append(convertirOperador(elementos.get(0).getValor()) + " AX,"+ elementos.get(2).getAssembler()+System.lineSeparator());
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
			if (elementos.get(0).getValor() =="+") {
				assembler.append("FADD"+System.lineSeparator());
			}else {
				assembler.append("FSUB"+System.lineSeparator());
			}
			assembler.append("FSTP "+"@aux"+this.numeroTerceto+System.lineSeparator());
		}
		return assembler.toString();
	}
}
