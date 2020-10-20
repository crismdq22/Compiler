package CodigoIntermedio;

import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.Token;

public class TercetoPrint extends Terceto {

	private int nroPrint;
	private int tipoPrint;
	
	public TercetoPrint(Token izq, Token medio, Token der, int numeroTerceto) {
		super(izq, medio, der, numeroTerceto);
		this.tipoPrint = 0;
		// TODO Auto-generated constructor stub
	}
	
	public void setTipoPrint(int i) {
		this.tipoPrint = i;
	}

	public String getAssembler() {
		// TODO Auto-generated method stub
		StringBuilder assembler = new StringBuilder();
		if (tipoPrint==0) {
			assembler.append("invoke MessageBox, NULL, addr print"+ nroPrint +", addr print"+nroPrint+", MB_OK "+System.lineSeparator());
			return assembler.toString();
		}else {
			if (elementos.get(1).getTipo() == AnalizadorLexico.tipoInt) {
				if (elementos.get(1).existeAtributo("Direccion")) {
					assembler.append("MOV ECX,"+elementos.get(1).getAssembler()+System.lineSeparator());
					assembler.append("MOV AX,[ECX]"+System.lineSeparator());
					assembler.append("MOVSX EAX, AX" +System.lineSeparator());
					assembler.append("MOV @printI, EAX"+System.lineSeparator());
					assembler.append("invoke printf, cfm$(\"%d\\n\"), @printI"+System.lineSeparator());
				}else {
					assembler.append("MOV AX,"+ elementos.get(1).getAssembler()+System.lineSeparator());
					assembler.append("MOVSX EAX, AX" +System.lineSeparator());
					assembler.append("MOV @printI, EAX"+System.lineSeparator());
					assembler.append("invoke printf, cfm$(\"%d\\n\"), @printI" +System.lineSeparator());
				}
			}else {
				if (elementos.get(1).existeAtributo("Direccion")) {
					assembler.append("MOV EAX,"+elementos.get(1).getAssembler()+System.lineSeparator());
					assembler.append("FLD QWORD PTR [EAX]"+System.lineSeparator());
					assembler.append("FSTP @printD"+System.lineSeparator());
					assembler.append("invoke printf, cfm$(\"%f\\n\"), @printD" +System.lineSeparator());
				}else
					assembler.append("invoke printf, cfm$(\"%f\\n\"), " + elementos.get(1).getAssembler()+System.lineSeparator());
			}
			return assembler.toString();
		}
	}

	public void setNroPrint(int i) {
		this.nroPrint = i;
	}
	
	public String getNroPrint() {
		return "print"+this.nroPrint;
	}
}
