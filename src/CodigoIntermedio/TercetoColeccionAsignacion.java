package CodigoIntermedio;

import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.Token;

public class TercetoColeccionAsignacion extends Terceto {
	private String tipo;
	
	public TercetoColeccionAsignacion(Token izq, Token medio, Token der, int numeroTerceto) {
		super(izq, medio, der, numeroTerceto);
		// TODO Auto-generated constructor stub
		this.tipo = medio.getTipo();
	}

	@Override
	public String getAssembler() {
		StringBuilder assembler = new StringBuilder();
		if (tipo == AnalizadorLexico.tipoInt) {
			if (elementos.get(2).existeAtributo("Direccion")) {
				assembler.append("MOV ECX,"+elementos.get(2).getAssembler()+System.lineSeparator());
				assembler.append("MOV AX,[ECX]\n");
			}else
				assembler.append("MOV AX,"+ elementos.get(2).getAssembler() +System.lineSeparator());
			for (int i = 0; i < (int)elementos.get(1).getAtributo("Length"); i++) {
				//i*4 seria direccion de la variable mas 4 bytes, chequear que la indirecciona memoria esta bien
					assembler.append("MOV ["+ elementos.get(1).getAssembler()+"+"+ (i*2+2) + "],AX"+System.lineSeparator());
			}
		}else {
			if (elementos.get(2).existeAtributo("Direccion")) {
				assembler.append("MOV EAX,"+elementos.get(2).getAssembler()+System.lineSeparator());
				assembler.append("FLD QWORD PTR [EAX]\n");
			}else
				assembler.append("FLD "+ elementos.get(2).getAssembler()+System.lineSeparator());
			for (int i = 0; i < (int)elementos.get(1).getAtributo("Length")-1; i++) {
				assembler.append("FST "+ elementos.get(1).getAssembler()+"+"+ (i*8+8)+System.lineSeparator());
			}
			assembler.append("FSTP "+ elementos.get(1).getAssembler()+"+"
			+ ((int)elementos.get(1).getAtributo("Length"))*8+System.lineSeparator());
		}
		return assembler.toString();
	}

}
