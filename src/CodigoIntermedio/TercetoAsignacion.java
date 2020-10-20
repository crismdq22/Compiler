package CodigoIntermedio;

import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.Token;

public class TercetoAsignacion extends Terceto{
	private String tipo;
	
	public TercetoAsignacion(Token izq, Token medio, Token der, int numeroTerceto) {
		super(izq, medio, der, numeroTerceto);
		this.tipo = medio.getTipo();
	}
	
	public String getAssembler() {
		StringBuilder assembler = new StringBuilder();
		if (tipo == AnalizadorLexico.tipoInt) {
			if (elementos.get(2).existeAtributo("Direccion")) {
				assembler.append("MOV EAX,"+elementos.get(2).getAssembler()+System.lineSeparator());
				assembler.append("MOV CX,[EAX]\n");
			}else {
				assembler.append("MOV "+ "CX,"+ elementos.get(2).getAssembler()+System.lineSeparator());
			}
			if (elementos.get(1).existeAtributo("Direccion")) {
				assembler.append("MOV EAX,"+elementos.get(1).getAssembler()+System.lineSeparator());
				assembler.append("MOV [EAX],CX\n");
			}else{
				assembler.append("MOV "+ elementos.get(1).getAssembler() + ",CX"+System.lineSeparator());
			}
		}else {
			if (elementos.get(2).existeAtributo("Direccion")) {
				assembler.append("MOV EAX,"+elementos.get(2).getAssembler()+System.lineSeparator());
				assembler.append("FLD QWORD PTR [EAX]\n");
			}else
				assembler.append("FLD "+ elementos.get(2).getAssembler()+System.lineSeparator());
			if (elementos.get(1).existeAtributo("Direccion")) {
				assembler.append("MOV EAX,"+elementos.get(1).getAssembler()+System.lineSeparator());
				assembler.append("FSTP QWORD PTR [EAX]\n");
			}else
				assembler.append("FSTP "+ elementos.get(1).getAssembler()+System.lineSeparator());
		}
		return assembler.toString();
	}
}
