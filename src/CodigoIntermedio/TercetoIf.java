package CodigoIntermedio;

import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.Token;

public class TercetoIf extends Terceto {
	
	private String tipoSalto;
	public static final String etiquetaSaltoIncondicional = "JMP";
	private String tipo;


	
	public TercetoIf(Token izq, Token medio, Token der,	int numeroTerceto) {
		super(izq, medio, der, numeroTerceto);
		// TODO Auto-generated constructor stub
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setTipoSalto(String tipoSalto){
		if(tipoSalto== "<=") {
			if (tipo== AnalizadorLexico.tipoInt)
				this.tipoSalto = "JG";
			else
				this.tipoSalto = "JA";
		}
		else
			if(tipoSalto.equals("=="))
				this.tipoSalto = "JNE";
			else
				if(tipoSalto.equals(">=")) {
					if (tipo== AnalizadorLexico.tipoInt)
						this.tipoSalto = "JL";
					else
						this.tipoSalto = "JB";
				}
				else
					if(tipoSalto.equals(">")) {
						if (tipo== AnalizadorLexico.tipoInt)
							this.tipoSalto = "JLE";
						else
							this.tipoSalto = "JBE";
					}
					else
						if(tipoSalto.equals("<")) {
							if (tipo== AnalizadorLexico.tipoInt)
								this.tipoSalto = "JGE";
							else
								this.tipoSalto = "JAE";
						}
						else
							if(tipoSalto.equals("!="))
								this.tipoSalto = "JE";
	};

	public String getAssembler() {
		StringBuilder assembler = new StringBuilder();
		String operador = elementos.get(0).getValor();
		
		if (operador == ControladorTercetos.BF){
			assembler.append(tipoSalto + " Label" + elementos.get(2).getValor() + System.lineSeparator());
			controladorTercetos.addLabelPendiente( Integer.parseInt(elementos.get(2).getValor() ) );
		}
		else{
			assembler.append(etiquetaSaltoIncondicional + " Label" + elementos.get(1).getValor() + System.lineSeparator());
			assembler.append("Label" + String.valueOf( controladorTercetos.borrarLabelPendiente() ) +":" + System.lineSeparator());
			controladorTercetos.addLabelPendiente( Integer.parseInt( elementos.get(1).getValor() ) );
		}
		return assembler.toString();
	}
}
