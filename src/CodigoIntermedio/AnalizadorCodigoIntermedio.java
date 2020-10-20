package CodigoIntermedio;

import java.util.ArrayList;

import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.ControladorArchivo;
import AnalizadorLexico.Error;
import AnalizadorLexico.TablaSimbolos;
import AnalizadorLexico.Token;

public class AnalizadorCodigoIntermedio {
	
	public static final String errorNoExisteVariable = "Esta variable no fue declarada ";
	public static final String errorVariableRedeclarada= "Ya se declaró una variable con este nombre ";
	public static final String errorTiposDiferentes = "Se pretende hacer operaciones con tipos diferentes ";
	public static final String errorFaltaIndiceColeccion = "Es necesario [indice] para la coleccion ";
	public static final String errorColeccionPorVariable = "Se pretende usar una coleccion como variable ";
	public static final String errorVariablePorColeccion = "Se pretende usar una variable como coleccion ";
	
	private ArrayList<Error> erroresCodigoIntermedio;

	public AnalizadorCodigoIntermedio() {
		erroresCodigoIntermedio = new ArrayList<Error>();
	}
	public void addError(Error error){
		erroresCodigoIntermedio.add(error);
	}
	
	public void declaracionParametros(String tipo, ArrayList<Token> listToken, TablaSimbolos tablaS, ControladorArchivo controladorArchivo) {
		for(Token t : listToken ){ 
			/*Chequear que la variable ya no este declarada*/			
			if (tablaS.get((String)t.getAtributo("Valor")).existeAtributo("Tipo_Uso")){
					addError (new Error ( errorVariableRedeclarada,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));
			}
				else {
					//la variable no fue declarada
				t.AgregarAtributo("Tipo_Uso",tipo);
				tablaS.add((String)t.getAtributo("Valor"), t);
					} 														
			}
	}
	
	public Token getTokenMetodo(Token coleccion, Token tokenMetodo,AnalizadorLexico analizadorL,ControladorTercetos controladorT, TablaSimbolos ts) {
		int metodo = (int)tokenMetodo.getAtributo("Nro_Token");
		Token nuevo,aux;
		if (metodo == AnalizadorLexico.first){
			TercetoColeccionFirst terceto = new TercetoColeccionFirst(new Token(ControladorTercetos.CFI), coleccion, null, controladorT.getProxNumero() );
			controladorT.addTerceto (terceto);
			nuevo = new Token( controladorT.numeroTercetoString() ) ;
			nuevo.AgregarAtributo("Tipo_Uso",coleccion.getAtributo("Tipo_Uso"));
			nuevo.AgregarAtributo("Uso",AnalizadorLexico.variable);
			
		} else if (metodo == AnalizadorLexico.last) {
			TercetoColeccionLast terceto = new TercetoColeccionLast(new Token(ControladorTercetos.CLA), coleccion, null, controladorT.getProxNumero() );
			controladorT.addTerceto (terceto);
			nuevo = new Token( controladorT.numeroTercetoString() ) ;
			nuevo.AgregarAtributo("Tipo_Uso",coleccion.getAtributo("Tipo_Uso"));
			nuevo.AgregarAtributo("Uso",AnalizadorLexico.variable);
		}else {
			TercetoColeccionLength terceto = new TercetoColeccionLength( new Token(ControladorTercetos.CLE),coleccion, null, controladorT.getProxNumero() );
			controladorT.addTerceto (terceto);
			nuevo = new Token( controladorT.numeroTercetoString() ) ;
			nuevo.AgregarAtributo("Uso",AnalizadorLexico.variable);
			nuevo.AgregarAtributo("Tipo_Uso",AnalizadorLexico.tipoInt);
		}
		aux = new Token( controladorT.numeroTercetoString() ) ;
		aux.AgregarAtributo("Tipo_Uso",nuevo.getAtributo("Tipo_Uso"));
		aux.AgregarAtributo("Uso",AnalizadorLexico.vAuxiliar);
		ts.add("@aux"+aux.getValor(), aux);
		return nuevo;
	}
	
	public String getErroresCI(){
		String aux = "Errores Codigo Intermedio: "+System.lineSeparator();
		for (Error e:erroresCodigoIntermedio){
			aux = aux + e.showError()+ System.lineSeparator();
		}
		if (erroresCodigoIntermedio.size() == 0)
			return "No hay errores de generacion de codigo intermedio.";
		else
			return aux;
	}
	
	public boolean hayErrores() {
		return (!erroresCodigoIntermedio.isEmpty());	
	}
}
