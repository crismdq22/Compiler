package CodigoIntermedio;

import AnalizadorLexico.Token;

public class TercetoLabel extends Terceto {

	public TercetoLabel(Token izq, Token medio, Token der, int numeroTerceto) {
		super(izq, medio, der, numeroTerceto);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getAssembler() {
		return "Label"+(numeroTerceto+1) + ":"+System.lineSeparator();
	}

}
