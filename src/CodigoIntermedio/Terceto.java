 package CodigoIntermedio;

import java.util.ArrayList;

import AnalizadorLexico.Token;

public abstract class Terceto {
	
	protected ArrayList<Token> elementos;
	protected int numeroTerceto;
	protected ControladorTercetos controladorTercetos;
	int posicion;

	public Terceto(Token izq, Token medio, Token der, int numeroTerceto) {
		elementos = new ArrayList<Token>();
		elementos.add(izq);
		elementos.add(medio);
		elementos.add(der);
		this.numeroTerceto = numeroTerceto;
		
	}
	
	public void setPosicionTerceto(int pos){
		posicion = pos;
	}
	
	public int getPosicionTerceto(){
		return posicion;
	}

	@Override
	public String toString(){
		String terceto = numeroTerceto + "  (";
		for (int i = 0 ; i< elementos.size(); i++){
			if (elementos.get(i) != null)
				terceto = terceto + elementos.get(i).imprimirTerceto();
			else
				terceto = terceto + " - ";
			if (i != elementos.size()-1)
				terceto = terceto + ", ";
			else
				terceto = terceto + ")";
		}
		return terceto;
	}
	
	public void setElemento(int index, Token t){
		elementos.set(index, t);
	}
	
	public Token getToken(int index){
		if (elementos.get(index)==null)
			return null;
		else
			return elementos.get(index);
	}
	
	public void setControladorTercetos(ControladorTercetos controladorTercetos) {
		this.controladorTercetos = controladorTercetos;
	}

	public abstract String getAssembler();
}
