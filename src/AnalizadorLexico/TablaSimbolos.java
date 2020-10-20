package AnalizadorLexico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;


public class TablaSimbolos {

	private HashMap<String, Token> tSimb;
	ArrayList<Token> prints = new ArrayList<Token>();
	public TablaSimbolos(){
		tSimb = new HashMap<>();
	}
	
	public void addSimbolo( Token t){
		tSimb.put(String.valueOf(t.getAtributo("Nombre")), t);
	}

	
	public Token get(String k) {
		if (this.tSimb.containsKey(k))
			return tSimb.get(k);
		return null;
	}
	
	public boolean existe(String nombre){
		return tSimb.containsKey(nombre);
	}

	public void add(String a, Token t) {
		if (!this.existe(a))
			this.tSimb.put(a, t);
	}

	public Enumeration<String> obtenerLexemas() {
		return Collections.enumeration(tSimb.keySet());
	}

	public Token getAtributo(Object s) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void imprimir() {
		System.out.println("---------------------------------------------------------------------------------------");
		System.out.println("			TABLA DE SIMBOLOS" + '\n');
		Token valor;
		for ( String key : tSimb.keySet() ) {
		  valor = tSimb.get( key );
		  if (valor.existeAtributo("Uso")){
			  if ((int)valor.getAtributo("Uso")==AnalizadorLexico.variable) {
		  		System.out.println("Tipo: " + valor.getTipo() + "	" 
		  				+"Valor: " + valor.getAtributo("Valor")+"	"+
		  				"Nro_Token: "+valor.getAtributo("Nro_Token")+"	Uso:"+valor.getAtributo("Uso")+
		  				"	Tipo_Uso:"+valor.getAtributo("Tipo_Uso"));
		  	}else if((int)valor.getAtributo("Uso")==AnalizadorLexico.coleccion){
		  		System.out.println("Tipo: " + valor.getTipo() + "	" 
		  				+ "Valor: " + valor.getAtributo("Valor")+"	"+
		  				"Nro_Token: "+valor.getAtributo("Nro_Token")+"	Uso:"+valor.getAtributo("Uso")+
					  "	Tipo_Uso:"+valor.getAtributo("Tipo_Uso")+"	Length:"+valor.getAtributo("Length"));
		  	}else if ((int)valor.getAtributo("Uso")==AnalizadorLexico.vAuxiliar){
				  System.out.println("Tipo: " + valor.getTipo() + "	" 
						  + "Valor: " + valor.getAtributo("Valor")+"	"+"Uso: "+valor.getAtributo("Uso"));
					}
		  	} else {
		  		System.out.println("Tipo: " + valor.getTipo() + "	" 
						  + "Valor: " + valor.getAtributo("Valor")+"	"+"Nro_Token: "+valor.getAtributo("Nro_Token"));
		  	}
		}
	}
	
		

	public String getAssembler() {
		ArrayList<Token> tokens = getTokens();
		StringBuilder assembler = new StringBuilder();
		for (Token t: tokens){
			if (t.existeAtributo("Uso")) {
				if  ((int)t.getAtributo("Uso") == AnalizadorLexico.variable) {			

					if(t.getTipo() == AnalizadorLexico.tipoInt )
						assembler.append("_"+t.getValor()+ " DW ?"+System.lineSeparator());
					else
						assembler.append("_"+t.getValor()+ " QWORD ?"+System.lineSeparator());
				} else if ((int)t.getAtributo("Uso") == AnalizadorLexico.coleccion) {
					if(t.getTipo() == AnalizadorLexico.tipoInt ) {
						assembler.append("_"+t.getValor()+ " DW "+t.getAtributo("Length")+", "+t.getAtributo("Length")+ " DUP(?)"+System.lineSeparator());
					}else {
						assembler.append("_"+t.getValor()+ " QWORD "+t.getAtributo("Length")+".0, "+t.getAtributo("Length")+" DUP(?)"+System.lineSeparator());
					}
				} else {
					if (t.existeAtributo("Direccion")) {
						assembler.append("@aux"+t.getValor()+ " DWORD ?"+System.lineSeparator());
					}else {
						if(t.getTipo() == AnalizadorLexico.tipoInt )
							assembler.append("@aux"+t.getValor()+ " DW ?"+System.lineSeparator());
						else
							assembler.append("@aux"+t.getValor()+ " QWORD ?"+System.lineSeparator());
					}
				}
			}else if((int)t.getAtributo("Nro_Token")==AnalizadorLexico.CTEDOUBLE) {
				assembler.append("@"+t.getAtributo("AuxDouble")+" QWORD "+t.getValor()+System.lineSeparator());
			}
		}
		return assembler.toString();
	}
	
	public ArrayList<Token> getPrints() {
		ArrayList<Token> tokens =getTokens();
		for (Token t: tokens){
			if ( ( (int)t.getAtributo("Nro_Token") == AnalizadorLexico.MULTI_LINEA) && (!estaPrint(t) ) )
				prints.add(t);
		}
		return prints;
	}
	
	private boolean estaPrint(Token token){
		for (Token t:prints)
			if ( t.getValor().equals( token.getValor() ) )
				return true;
		return false;
	}
	
	public ArrayList<Token> getTokens(){
		return new ArrayList<>(tSimb.values());
	}
}