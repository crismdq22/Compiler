package AnalizadorSintactico;

import java.util.ArrayList;

import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.Error;
import AnalizadorLexico.TablaSimbolos;

public class AnalizadorSintactico {
	
	//descripcion estructura sintactica
	static final String estructuraPrint = "Sentencia para imprimir por pantalla ";
	static final String estructuraIF = "Final sentencia de seleccion IF ";
	static final String estructuraDO = "Final sentencia de iteracion DO ";
	static final String estructuraASIG = "Sentencia de ASIGNACIÓN ";
	static final String estructuraCONDICION = "Sentencia de CONDICION ";
	static final String estructuraPRINCIPAL = "Estructura del programa principal ";
	static final String estructuraDECLARACION = "Sentencia de declaracion de variables.";
	static final String inicioIF = "Inicio estructura IF";
	static final String inicioDO = "Inicio estructura DO";
	
	
	//descripciones errores sintacticos
	static final String errorPrint1 = "Existe un error en la sentencia print ";
	static final String errorPrint2 = "Se espera la palabra reservada 'PRINT' al comienzo de la sentencia ";
	static final String errorParentesisA = "Parentesis no equilibrados. Faltó abrir parentesis. ";
	static final String errorParentesisC = "Parentesis no equilibrados. Faltó cerrar parentesis. ";
	static final String errorCondicionI = "No se reconoce el lado izquierdo de la condicion ";
	static final String errorCondicionD = "No se reconoce el lado derecho de la condicion ";
	static final String errorPuntoComa = "Se espera un ';' al final de la sentencia ";
	static final String errorDeclaracionVar = "No se declarararon correctamente las variables. ";
	static final String errorSentencias = "No se declararon correctamente las sentencias dentro del programa. ";
	static final String errorAsignacion = "Error en la asignacion.";
	static final String errorAsignacionIgual = "Error en la asignacion, se esperaba := pero se tuvo =";
	static final String errorTipo = "Error al declarar el tipo.";
	static final String errorSimboloAsignacion = "La asignacion es con :=";
	static final String errorDeclaracionColeccion = "Error en la declaración de la colección.";
	static final String errorSubIndice = "Se espera una constante entera o variable dentro de los corchetes ";
	static final String errorTipo_operacion = "Los tipos de las variables son incompatibles.";
	static final String errorPalabraIF = "Se esperaba la palabra if en minuscula ";
	static final String errorPalabraElse = "Falta la Palabra reservada ELSE";
	static final String errorENDIF = "Se esperaba la palabra reservada ENDIF al final";
	static final String errorBEGINIF = "Se esperaba la palabra reservada BEGIN al comienzo";
	static final String errorDOCondicion = "Se esperaba una condicion en la sentencia DO";
	static final String errorDOUNTIL = "Se esperaba la palabra reservada UNTIL.";
	static final String errorDO = "Error en la sentencia DO.";
	static final String errorEND = "Se esperaba la palabra reservada END al final";
	static final String errorBEGIN = "Se esperaba la palabra reservada BEGIN al comienzo";
	static final String errorPrograma = "No se encontraron estructuras validas";
	static final String errorMetodo = "No existe tal metodo para colecciones";
	static final String errorComaDeclaracionVar = "Las variables deben ser separadas por coma";
	static final String errorBloqueEnBeginEnd = "Todas las sentencias deben estar dentro de begin end";
	static final String errorElse ="Se esperaba un Else";

	ArrayList<Error> erroresSint;
	ArrayList<Error > estructuras;
	AnalizadorLexico analizadorL;
	TablaSimbolos tablaS;
	Parser parser;
	
	public AnalizadorSintactico() {
		erroresSint = new ArrayList<Error>();
		estructuras = new ArrayList<Error>();
	}
	
	public void addError( Error error ){
		erroresSint.add(error);
	}
	
	public void setParser(Parser parser) {
		this.parser = parser;
	}
	
	public void addEstructura(Error estructura){
		estructuras.add(estructura); //usamos el error tambien para almacenar la estructura para mostrarla 
	}
	
	public String getErroresSint(){
		String aux = "////////////////////// ERRORES SINTACTICOS ////////////////////// \n";
		for (Error e:erroresSint){
			aux = aux + e.showError() + "\n";
		}
		if (erroresSint.size() == 0)
			return "No se encontraron errores sintacticos \n";
		else
			return aux;
	}
	
	public String getEstructuras(){
		String aux = "//////////////////////  ESTRUCTURAS ENCONTRADAS ////////////////////// \n";
		for (Error e:estructuras){
			aux = aux + e.showError() + "\n";
		}
		if (estructuras.size() == 0)
			return "No se encontraron estructuras sintacticas";
		else
			return aux;
	}
	
	public boolean hayErrores() {
		return (erroresSint.size()>0 || parser.yynerrs>0);
	}
}
