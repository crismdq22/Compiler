package AnalizadorLexico;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class AnalizadorLexico {
	
	public static final int F = -1;  //F Es la constante que indica un estado final !  
	public static final int E = -2;  //E Es la constante que indica un estado de error !
	public static final int WI = -3; //WI Es la constante que indica un Warning de identificador
	public static final int WC = -4; //WC Es la constante que indica un Warning de constante
	public static final int INPUTLETRA = 65;
	public static final int INPUTDIGITO = 48;
	public static final int INPUTDIVISION = 47;
	public static final int INPUTPRODUCTO = 42;
	public static final int INPUTSUMA = 43;
	public static final int INPUTRESTA = 45;
	public static final int INPUTIGUAL = 61;
	public static final int INPUTMENOR = 60;
	public static final int INPUTMAYOR = 62;
	public static final int INPUTCOMENTARIO = 35;
	public static final int INPUTPARENTESISI = 40;
	public static final int INPUTPARENTESISF = 41;
	public static final int INPUTLLAVEF = 125;
	public static final int INPUTLLAVEI = 123;
	public static final int INPUTCOMA = 44;
	public static final int INPUTPUNTOYCOMA = 59;
	public static final int INPUTGUIONBAJO = 95;
	public static final int INPUTD = 68;
	public static final int INPUTd = 100;
	public static final int INPUTPUNTO = 46;
	public static final int INPUTCORCHETEI = 91;
	public static final int INPUTCORCHETEF = 93;
	public static final int INPUTTAB = 9;
	public static final int INPUTESPACIO = 32;
	public static final int INPUTDOSPUNTOS = 58;
	public static final int INPUTSL = 10;
	public static final int INPUTEOF = 3;
		

	static final String constanteE = "CE";
	static final String identificador = "I";
	static final String constanteD = "CD";
	
	public static final int ID = 257  ;
	public static final int MULTI_LINEA = 258 ;
	public static final int CTEDOUBLE = 259;
	public static final int S_ASIGNACION = 260;
	public static final int S_MAYOR_IGUAL = 261;
	public static final int S_MENOR_IGUAL = 262;	
	public static final int CTEENTERA = 263;
	public static final int S_PORCENTAJE = 264 ;
	public static final int COMENTARIO = 265;
	public static final int S_DISTINTO = 266;
	public static final int S_IGUAL = 267;
	
	
	//PALABRA RESERVADA
	static final int IF = 268;
	static final int ELSE = 269;
	static final int ENDIF = 270;
	static final int UNTIL = 271;
	static final int DO = 272;
	public static final int PRINT = 273;
	static final int BEGIN = 274;
	static final int END = 275;
	static final int INT = 276;
	static final int DOUBLE = 277;
	public static final int first=278;
	public static final int last=279;
	public static final int length=280;
	public static final int ARREGLO=281;
	
	
	public static final int variable = 282;
	public static final int coleccion = 283;
	public static final String tipoDouble = "Double";
	public static final String tipoInt = "Int";

	public static final int nroTerceto=284;
	public static final int vAuxiliar = 287;
	
	static final String warningIdentificador = "Warning: Identificador truncado por superar 25 carácteres, en línea";
	static final String warningConstante = "Warning: Constante fuera de rango en linea";
	static final String warningCaracter = "Error: Caracter invalido en linea";
	static final String errorCaracter = "Error: Caracter no reconocido en linea";
	static final String errorDouble = "Error: Declaracion incorrecta de tipo Double en linea";

	
	//controlador del archivo
	private ControladorArchivo archivo;
	private TablaSimbolos tablaSimbolos;
	static List<String> str;
	protected  int [][] matrizEstado;		
	protected  Accion_Semantica [][] accionSemantica;		
	private  int estado;
	private  String buffer_temporal;
	private  char entrada;	
	private HashMap<Integer,Integer> mapEntrada;
	private List<Error> errores = new ArrayList<Error>();
	private List<Error> warnings = new ArrayList<Error>();
	
	private ArrayList<Token> tokens = new ArrayList<Token>();

	private int vAuxDouble;
	private String ylex;
	

	public AnalizadorLexico(ControladorArchivo archivo, TablaSimbolos ts){
		matrizEstado = new int [15][26];
		accionSemantica = new Accion_Semantica[15][26];
		estado = 0;
		cargarMatrizEstado();
		mapEntrada = new HashMap<Integer,Integer>();
		cargarMapEntrada();
		cargarMatrizAccionSemantica();
		this.archivo = archivo;
		this.tablaSimbolos = ts;
		vAuxDouble = 0;
	}

	private void cargarMapEntrada() {
		mapEntrada.put(INPUTLETRA, 0);
		mapEntrada.put(INPUTDIGITO, 1);
		mapEntrada.put(INPUTDIVISION, 2);
		mapEntrada.put(INPUTPRODUCTO, 3);
		mapEntrada.put(INPUTSUMA, 4);
		mapEntrada.put(INPUTRESTA, 5);
		mapEntrada.put(INPUTIGUAL, 6);
		mapEntrada.put(INPUTMENOR, 7);
		mapEntrada.put(INPUTMAYOR, 8);
		mapEntrada.put(INPUTCOMENTARIO, 9);
		mapEntrada.put(INPUTPARENTESISI, 10);
		mapEntrada.put(INPUTPARENTESISF, 11);
		mapEntrada.put(INPUTLLAVEF, 12);
		mapEntrada.put(INPUTCOMA, 13);
		mapEntrada.put(INPUTPUNTOYCOMA, 14);
		mapEntrada.put(INPUTGUIONBAJO, 15);
		mapEntrada.put(INPUTLLAVEI, 16);
		mapEntrada.put(INPUTD, 17);	//D
		mapEntrada.put(INPUTd, 17); //d
		mapEntrada.put(INPUTPUNTO, 18);
		mapEntrada.put(INPUTCORCHETEI, 19);
		mapEntrada.put(INPUTCORCHETEF, 20);
		mapEntrada.put(INPUTDOSPUNTOS, 21);
		mapEntrada.put(INPUTESPACIO, 23);
		mapEntrada.put(INPUTTAB, 23);
		mapEntrada.put(INPUTSL, 24);
		mapEntrada.put(INPUTEOF, 25); //End of file preguntar si esta bien
	}
	
	public void cargarMatrizAccionSemantica() {			
		accionSemantica[0][0]= new Accion_Semantica_AgregarBuffer();
		accionSemantica[0][1]= new Accion_Semantica_AgregarBuffer();
		accionSemantica[0][2]= new Accion_Semantica_Operadores_Final((int)'/', false);
		accionSemantica[0][3]= new Accion_Semantica_Operadores_Final((int)'*', false);
		accionSemantica[0][4]= new Accion_Semantica_Operadores_Final((int)'+', false);
		accionSemantica[0][5]= new Accion_Semantica_Operadores_Final((int)'-', false);
		accionSemantica[0][6]= new Accion_Semantica_AgregarBuffer();
		accionSemantica[0][7]= new Accion_Semantica_AgregarBuffer();
		accionSemantica[0][8]= new Accion_Semantica_AgregarBuffer();
		accionSemantica[0][9]= null;
		accionSemantica[0][10]= new Accion_Semantica_Operadores_Final((int)'(', false);
		accionSemantica[0][11]= new Accion_Semantica_Operadores_Final((int)')', false);
		accionSemantica[0][12]= new Accion_Semantica_Operadores_Final((int)'}', false);
		accionSemantica[0][13]= new Accion_Semantica_Operadores_Final((int)',', false);
		accionSemantica[0][14]= new Accion_Semantica_Operadores_Final((int)';', false);
		accionSemantica[0][15]= new Accion_Semantica_Operadores_Final((int)'_', false);
		accionSemantica[0][16]= null;
		accionSemantica[0][17]= new Accion_Semantica_AgregarBuffer(); 
		accionSemantica[0][18]= new Accion_Semantica_AgregarBuffer();
		accionSemantica[0][19]= new Accion_Semantica_Operadores_Final((int)'[', false);
		accionSemantica[0][20]= new Accion_Semantica_Operadores_Final((int)']', false);
		accionSemantica[0][21]= new Accion_Semantica_AgregarBuffer();
		accionSemantica[0][22]= new Accion_Semantica_Error(new Error(errorCaracter, "ERROR LEXICO"));
		accionSemantica[0][23]= null;
		accionSemantica[0][24]= null;
		accionSemantica[0][25]= null;

		accionSemantica[1][0]= new Accion_Semantica_AgregarBuffer();
		accionSemantica[1][1]= new Accion_Semantica_AgregarBuffer();
		accionSemantica[1][2]= new Accion_Semantica_Identifcador_Final(true);
		accionSemantica[1][3]= new Accion_Semantica_Identifcador_Final(true);
		accionSemantica[1][4]= new Accion_Semantica_Identifcador_Final(true);
		accionSemantica[1][5]= new Accion_Semantica_Identifcador_Final(true);
		accionSemantica[1][6]= new Accion_Semantica_Identifcador_Final(true);
		accionSemantica[1][7]= new Accion_Semantica_Identifcador_Final(true);
		accionSemantica[1][8]= new Accion_Semantica_Identifcador_Final(true);
		accionSemantica[1][9]= new Accion_Semantica_Identifcador_Final(true);
		accionSemantica[1][10]= new Accion_Semantica_Identifcador_Final(true);
		accionSemantica[1][11]= new Accion_Semantica_Identifcador_Final(true);
		accionSemantica[1][12]= new Accion_Semantica_Identifcador_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[1][13]= new Accion_Semantica_Identifcador_Final(true);
		accionSemantica[1][14]= new Accion_Semantica_Identifcador_Final(true); 
		accionSemantica[1][15]= new Accion_Semantica_AgregarBuffer();
		accionSemantica[1][16]= new Accion_Semantica_Identifcador_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[1][17]= new Accion_Semantica_AgregarBuffer(); 
		accionSemantica[1][18]= new Accion_Semantica_Identifcador_Final(true);
		accionSemantica[1][19]= new Accion_Semantica_Identifcador_Final(true); 
		accionSemantica[1][20]= new Accion_Semantica_Identifcador_Final(true); 
		accionSemantica[1][21]= new Accion_Semantica_Identifcador_Final(true);
		accionSemantica[1][22]= new Accion_Semantica_Identifcador_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[1][23]= new Accion_Semantica_Identifcador_Final(false);
		accionSemantica[1][24]= new Accion_Semantica_Identifcador_Final(false);
		accionSemantica[1][25]= new Accion_Semantica_Identifcador_Final(false);

		accionSemantica[2][0]= new Accion_Semantica_Enteros_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[2][1]= new Accion_Semantica_AgregarBuffer();
		accionSemantica[2][2]= new Accion_Semantica_Enteros_Final(true);
		accionSemantica[2][3]=new Accion_Semantica_Enteros_Final(true);
		accionSemantica[2][4]=new Accion_Semantica_Enteros_Final(true);
		accionSemantica[2][5]=new Accion_Semantica_Enteros_Final(true);
		accionSemantica[2][6]=new Accion_Semantica_Enteros_Final(true);
		accionSemantica[2][7]=new Accion_Semantica_Enteros_Final(true);
		accionSemantica[2][8]=new Accion_Semantica_Enteros_Final(true);
		accionSemantica[2][9]=new Accion_Semantica_Enteros_Final(true);
		accionSemantica[2][10]=new Accion_Semantica_Enteros_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[2][11]=new Accion_Semantica_Enteros_Final(true);
		accionSemantica[2][12]=new Accion_Semantica_Enteros_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[2][13]=new Accion_Semantica_Enteros_Final(true);
		accionSemantica[2][14]=new Accion_Semantica_Enteros_Final(true); 
		accionSemantica[2][15]=new Accion_Semantica_Enteros_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[2][16]=new Accion_Semantica_Enteros_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[2][17]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO")); 
		accionSemantica[2][18]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[2][19]=new Accion_Semantica_Enteros_Final(true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[2][20]=new Accion_Semantica_Enteros_Final(true); 
		accionSemantica[2][21]=new Accion_Semantica_Enteros_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[2][22]=new Accion_Semantica_Enteros_Final(true, new Error(warningCaracter, "ERROR LEXICO" ));
		accionSemantica[2][23]=new Accion_Semantica_Enteros_Final(false);
		accionSemantica[2][24]=new Accion_Semantica_Enteros_Final(false);
		accionSemantica[2][25]=new Accion_Semantica_Enteros_Final(false);

		accionSemantica[3][0]= new Accion_Semantica_Operadores_Final((int)'.', true);
		accionSemantica[3][1]= new Accion_Semantica_AgregarBuffer();
		accionSemantica[3][2]=new Accion_Semantica_Operadores_Final((int)'.', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[3][3]=new Accion_Semantica_Operadores_Final((int)'.', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[3][4]=new Accion_Semantica_Operadores_Final((int)'.', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[3][5]=new Accion_Semantica_Operadores_Final((int)'.', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[3][6]=new Accion_Semantica_Operadores_Final((int)'.', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[3][7]=new Accion_Semantica_Operadores_Final((int)'.', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[3][8]=new Accion_Semantica_Operadores_Final((int)'.', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[3][9]=new Accion_Semantica_Operadores_Final((int)'.', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[3][10]=new Accion_Semantica_Operadores_Final((int)'.', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[3][11]=new Accion_Semantica_Operadores_Final((int)'.', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[3][12]=new Accion_Semantica_Operadores_Final((int)'.', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[3][13]=new Accion_Semantica_Operadores_Final((int)'.', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[3][14]=new Accion_Semantica_Operadores_Final((int)'.', true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[3][15]=new Accion_Semantica_Operadores_Final((int)'.', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[3][16]=new Accion_Semantica_Operadores_Final((int)'.', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[3][17]=new Accion_Semantica_Operadores_Final((int)'.', true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[3][18]=new Accion_Semantica_Operadores_Final((int)'.', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[3][19]=new Accion_Semantica_Operadores_Final((int)'.', true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[3][20]=new Accion_Semantica_Operadores_Final((int)'.', true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[3][21]=new Accion_Semantica_Operadores_Final((int)'.', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[3][22]=new Accion_Semantica_Operadores_Final((int)'.', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[3][23]=new Accion_Semantica_Operadores_Final((int)'.', false);
		accionSemantica[3][24]=new Accion_Semantica_Operadores_Final((int)'.', false);
		accionSemantica[3][25]=new Accion_Semantica_Operadores_Final((int)'.', false);


		accionSemantica[4][0]=null;
		accionSemantica[4][1]=null;
		accionSemantica[4][2]=null;
		accionSemantica[4][3]=null;
		accionSemantica[4][4]=null;
		accionSemantica[4][5]=null;
		accionSemantica[4][6]=null;
		accionSemantica[4][7]=null;
		accionSemantica[4][8]=null;
		accionSemantica[4][9]=null;
		accionSemantica[4][10]=null;
		accionSemantica[4][11]=null;
		accionSemantica[4][12]=null;
		accionSemantica[4][13]=null;
		accionSemantica[4][14]=null; 
		accionSemantica[4][15]=null;
		accionSemantica[4][16]=null;
		accionSemantica[4][17]=null; 
		accionSemantica[4][18]=null;
		accionSemantica[4][19]=null; 
		accionSemantica[4][20]=null; 
		accionSemantica[4][21]=null;
		accionSemantica[4][22]=null;
		accionSemantica[4][23]=null;
		accionSemantica[4][24]=null;
		accionSemantica[4][25]=null;

		accionSemantica[5][0]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[5][1]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[5][2]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[5][3]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[5][4]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[5][5]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[5][6]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[5][7]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[5][8]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[5][9]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[5][10]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[5][11]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[5][12]=new Accion_Semantica_CadenaFinal(false);
		accionSemantica[5][13]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[5][14]=new Accion_Semantica_AgregarBuffer(); 
		accionSemantica[5][15]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[5][16]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[5][17]=new Accion_Semantica_AgregarBuffer(); 
		accionSemantica[5][18]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[5][19]=new Accion_Semantica_AgregarBuffer(); 
		accionSemantica[5][20]=new Accion_Semantica_AgregarBuffer(); 
		accionSemantica[5][21]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[5][22]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[5][23]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[5][24]=null;
		accionSemantica[5][25]=new Accion_Semantica_CadenaFinal(false);


		accionSemantica[6][0]=new Accion_Semantica_Operadores_Final((int)'<', true);
		accionSemantica[6][1]=new Accion_Semantica_Operadores_Final((int)'<', true);
		accionSemantica[6][2]=new Accion_Semantica_Operadores_Final((int)'<', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[6][3]=new Accion_Semantica_Operadores_Final((int)'<', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[6][4]=new Accion_Semantica_Operadores_Final((int)'<', true);
		accionSemantica[6][5]=new Accion_Semantica_Operadores_Final((int)'<', true);
		accionSemantica[6][6]=new Accion_Semantica_Operadores_Final(S_MENOR_IGUAL, false);
		accionSemantica[6][7]=new Accion_Semantica_Operadores_Final((int)'<', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[6][8]=new Accion_Semantica_Operadores_Final(S_DISTINTO, false);
		accionSemantica[6][9]=new Accion_Semantica_Operadores_Final((int)'<', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[6][10]=new Accion_Semantica_Operadores_Final((int)'<', true);
		accionSemantica[6][11]=new Accion_Semantica_Operadores_Final((int)'<', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[6][12]=new Accion_Semantica_Operadores_Final((int)'<', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[6][13]=new Accion_Semantica_Operadores_Final((int)'<', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[6][14]=new Accion_Semantica_Operadores_Final((int)'<', true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[6][15]=new Accion_Semantica_Operadores_Final((int)'<', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[6][16]=new Accion_Semantica_Operadores_Final((int)'<', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[6][17]=new Accion_Semantica_Operadores_Final((int)'<', true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[6][18]=new Accion_Semantica_Operadores_Final((int)'<', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[6][19]=new Accion_Semantica_Operadores_Final((int)'<', true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[6][20]=new Accion_Semantica_Operadores_Final((int)'<', true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[6][21]=new Accion_Semantica_Operadores_Final((int)'<', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[6][22]=new Accion_Semantica_Operadores_Final((int)'<', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[6][23]=new Accion_Semantica_Operadores_Final((int)'<', false);
		accionSemantica[6][24]=new Accion_Semantica_Operadores_Final((int)'<', false);
		accionSemantica[6][25]=new Accion_Semantica_Operadores_Final((int)'<', false);

		accionSemantica[7][0]=new Accion_Semantica_Operadores_Final((int) '>', true);
		accionSemantica[7][1]=new Accion_Semantica_Operadores_Final((int) '>', true);
		accionSemantica[7][2]=new Accion_Semantica_Operadores_Final((int) '>', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[7][3]=new Accion_Semantica_Operadores_Final((int) '>', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[7][4]=new Accion_Semantica_Operadores_Final((int) '>', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[7][5]=new Accion_Semantica_Operadores_Final((int) '>', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[7][6]=new Accion_Semantica_Operadores_Final(S_MAYOR_IGUAL, false);
		accionSemantica[7][7]=new Accion_Semantica_Operadores_Final((int) '>', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[7][8]=new Accion_Semantica_Operadores_Final((int) '>', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[7][9]=new Accion_Semantica_Operadores_Final((int) '>', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[7][10]=new Accion_Semantica_Operadores_Final((int) '>', true);
		accionSemantica[7][11]=new Accion_Semantica_Operadores_Final((int) '>', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[7][12]=new Accion_Semantica_Operadores_Final((int) '>', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[7][13]=new Accion_Semantica_Operadores_Final((int) '>', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[7][14]=new Accion_Semantica_Operadores_Final((int) '>', true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[7][15]=new Accion_Semantica_Operadores_Final((int) '>', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[7][16]=new Accion_Semantica_Operadores_Final((int) '>', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[7][17]=new Accion_Semantica_Operadores_Final((int) '>', true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[7][18]=new Accion_Semantica_Operadores_Final((int) '>', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[7][19]=new Accion_Semantica_Operadores_Final((int) '>', true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[7][20]=new Accion_Semantica_Operadores_Final((int) '>', true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[7][21]=new Accion_Semantica_Operadores_Final((int) '>', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[7][22]=new Accion_Semantica_Operadores_Final((int) '>', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[7][23]=new Accion_Semantica_Operadores_Final((int) '>', false);
		accionSemantica[7][24]=new Accion_Semantica_Operadores_Final((int) '>', false);
		accionSemantica[7][25]=new Accion_Semantica_Operadores_Final((int) '>', false);


		accionSemantica[8][0]=new Accion_Semantica_Operadores_Final((int) '=', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[8][1]=new Accion_Semantica_Operadores_Final((int) '=', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[8][2]=new Accion_Semantica_Operadores_Final((int) '=', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[8][3]=new Accion_Semantica_Operadores_Final((int) '=', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[8][4]=new Accion_Semantica_Operadores_Final((int) '=', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[8][5]=new Accion_Semantica_Operadores_Final((int) '=', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[8][6]=new Accion_Semantica_Operadores_Final(S_IGUAL, false);
		accionSemantica[8][7]=new Accion_Semantica_Operadores_Final((int) '=', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[8][8]=new Accion_Semantica_Operadores_Final((int) '=', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[8][9]=new Accion_Semantica_Operadores_Final((int) '=', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[8][10]=new Accion_Semantica_Operadores_Final((int) '=', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[8][11]=new Accion_Semantica_Operadores_Final((int) '=', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[8][12]=new Accion_Semantica_Operadores_Final((int) '=', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[8][13]=new Accion_Semantica_Operadores_Final((int) '=', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[8][14]=new Accion_Semantica_Operadores_Final((int) '=', true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[8][15]=new Accion_Semantica_Operadores_Final((int) '=', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[8][16]=new Accion_Semantica_Operadores_Final((int) '=', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[8][17]=new Accion_Semantica_Operadores_Final((int) '=', true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[8][18]=new Accion_Semantica_Operadores_Final((int) '=', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[8][19]=new Accion_Semantica_Operadores_Final((int) '=', true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[8][20]=new Accion_Semantica_Operadores_Final((int) '=', true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[8][21]=new Accion_Semantica_Operadores_Final((int) '=', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[8][22]=new Accion_Semantica_Operadores_Final((int) '=', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[8][23]=new Accion_Semantica_Operadores_Final((int) '=', false);
		accionSemantica[8][24]=new Accion_Semantica_Operadores_Final((int) '=', false);
		accionSemantica[8][25]=new Accion_Semantica_Operadores_Final((int) '=', false);


		accionSemantica[9][0]=new Accion_Semantica_Double_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[9][1]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[9][2]=new Accion_Semantica_Double_Final(true);
		accionSemantica[9][3]=new Accion_Semantica_Double_Final(true);
		accionSemantica[9][4]=new Accion_Semantica_Double_Final(true);
		accionSemantica[9][5]=new Accion_Semantica_Double_Final(true);
		accionSemantica[9][6]=new Accion_Semantica_Double_Final(true);
		accionSemantica[9][7]=new Accion_Semantica_Double_Final(true);
		accionSemantica[9][8]=new Accion_Semantica_Double_Final(true);
		accionSemantica[9][9]=new Accion_Semantica_Double_Final(true);
		accionSemantica[9][10]=new Accion_Semantica_Double_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[9][11]=new Accion_Semantica_Double_Final(true);
		accionSemantica[9][12]=new Accion_Semantica_Double_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[9][13]=new Accion_Semantica_Double_Final(true);
		accionSemantica[9][14]=new Accion_Semantica_Double_Final(true); 
		accionSemantica[9][15]=new Accion_Semantica_Double_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[9][16]=new Accion_Semantica_Double_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[9][17]=new Accion_Semantica_AgregarBuffer(); 
		accionSemantica[9][18]=new Accion_Semantica_Double_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[9][19]=new Accion_Semantica_Double_Final(true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[9][20]=new Accion_Semantica_Double_Final(true); 
		accionSemantica[9][21]=new Accion_Semantica_Double_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[9][22]=new Accion_Semantica_Double_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[9][23]=new Accion_Semantica_Double_Final(false);
		accionSemantica[9][24]=new Accion_Semantica_Double_Final(false);
		accionSemantica[9][25]=new Accion_Semantica_Double_Final(false);


		accionSemantica[10][0]=new Accion_Semantica_Operadores_Final((int)':', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[10][1]=new Accion_Semantica_Operadores_Final((int)':', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[10][2]=new Accion_Semantica_Operadores_Final((int)':', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[10][3]=new Accion_Semantica_Operadores_Final((int)':', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[10][4]=new Accion_Semantica_Operadores_Final((int)':', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[10][5]=new Accion_Semantica_Operadores_Final((int)':', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[10][6]=new Accion_Semantica_Operadores_Final(S_ASIGNACION, false);
		accionSemantica[10][7]=new Accion_Semantica_Operadores_Final((int)':', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[10][8]=new Accion_Semantica_Operadores_Final((int)':', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[10][9]=new Accion_Semantica_Operadores_Final((int)':', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[10][10]=new Accion_Semantica_Operadores_Final((int)':', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[10][11]=new Accion_Semantica_Operadores_Final((int)':', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[10][12]=new Accion_Semantica_Operadores_Final((int)':', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[10][13]=new Accion_Semantica_Operadores_Final((int)':', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[10][14]=new Accion_Semantica_Operadores_Final((int)':', true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[10][15]=new Accion_Semantica_Operadores_Final((int)':', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[10][16]=new Accion_Semantica_Operadores_Final((int)':', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[10][17]=new Accion_Semantica_Operadores_Final((int)':', true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[10][18]=new Accion_Semantica_Operadores_Final((int)':', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[10][19]=new Accion_Semantica_Operadores_Final((int)':', true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[10][20]=new Accion_Semantica_Operadores_Final((int)':', true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[10][21]=new Accion_Semantica_Operadores_Final((int)':', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[10][22]=new Accion_Semantica_Operadores_Final((int)':', true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[10][23]=new Accion_Semantica_Operadores_Final((int)':', false);
		accionSemantica[10][24]=new Accion_Semantica_Operadores_Final((int)':', false);
		accionSemantica[10][25]=new Accion_Semantica_Operadores_Final((int)':', false);


		accionSemantica[11][0]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[11][1]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[11][2]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[11][3]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[11][4]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[11][5]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[11][6]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[11][7]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[11][8]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[11][9]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[11][10]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[11][11]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[11][12]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[11][13]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[11][14]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO")); 
		accionSemantica[11][15]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[11][16]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[11][17]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO")); 
		accionSemantica[11][18]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[11][19]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO")); 
		accionSemantica[11][20]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO")); 
		accionSemantica[11][21]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[11][22]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[11][23]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[11][24]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[11][25]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));


		accionSemantica[12][0]=new Accion_Semantica_Double_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[12][1]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[12][2]=new Accion_Semantica_Double_Final(true);
		accionSemantica[12][3]=new Accion_Semantica_Double_Final(true);
		accionSemantica[12][4]=new Accion_Semantica_Double_Final(true);
		accionSemantica[12][5]=new Accion_Semantica_Double_Final(true);
		accionSemantica[12][6]=new Accion_Semantica_Double_Final(true);
		accionSemantica[12][7]=new Accion_Semantica_Double_Final(true);
		accionSemantica[12][8]=new Accion_Semantica_Double_Final(true);
		accionSemantica[12][9]=new Accion_Semantica_Double_Final(true);
		accionSemantica[12][10]=new Accion_Semantica_Double_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[12][11]=new Accion_Semantica_Double_Final(true);
		accionSemantica[12][12]=new Accion_Semantica_Double_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[12][13]=new Accion_Semantica_Double_Final(true);
		accionSemantica[12][14]=new Accion_Semantica_Double_Final(true); 
		accionSemantica[12][15]=new Accion_Semantica_Double_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[12][16]=new Accion_Semantica_Double_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[12][17]=new Accion_Semantica_Double_Final(true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[12][18]=new Accion_Semantica_Double_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[12][19]=new Accion_Semantica_Double_Final(true, new Error(warningCaracter, "ERROR LEXICO")); 
		accionSemantica[12][20]=new Accion_Semantica_Double_Final(true); 
		accionSemantica[12][21]=new Accion_Semantica_Double_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[12][22]=new Accion_Semantica_Double_Final(true, new Error(warningCaracter, "ERROR LEXICO"));
		accionSemantica[12][23]=new Accion_Semantica_Double_Final(false);
		accionSemantica[12][24]=new Accion_Semantica_Double_Final(false);
		accionSemantica[12][25]=new Accion_Semantica_Double_Final(false);

		//CREO QUE ACA NO SERIA ERROR, SINO QUE TENDRIA QUE SER ACCION_SEMANTICA_DOUBLE_FINAL
		accionSemantica[13][0]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[13][1]=new Accion_Semantica_AgregarBuffer();
		accionSemantica[13][2]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[13][3]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[13][4]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[13][5]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[13][6]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[13][7]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[13][8]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[13][9]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[13][10]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[13][11]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[13][12]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[13][13]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[13][14]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO")); 
		accionSemantica[13][15]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[13][16]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[13][17]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO")); 
		accionSemantica[13][18]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[13][19]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO")); 
		accionSemantica[13][20]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO")); 
		accionSemantica[13][21]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[13][22]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[13][23]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[13][24]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		accionSemantica[13][25]=new Accion_Semantica_Error(new Error(errorDouble, "ERROR LEXICO"));
		
		accionSemantica[14][0]=null;
		accionSemantica[14][1]=null;
		accionSemantica[14][2]=null;
		accionSemantica[14][3]=null;
		accionSemantica[14][4]=null;
		accionSemantica[14][5]=null;
		accionSemantica[14][6]=null;
		accionSemantica[14][7]=null;
		accionSemantica[14][8]=null;
		accionSemantica[14][9]=null;
		accionSemantica[14][10]=null;
		accionSemantica[14][11]=null;
		accionSemantica[14][12]=null;
		accionSemantica[14][13]=null;
		accionSemantica[14][14]=null; 
		accionSemantica[14][15]=null;
		accionSemantica[14][16]=null;
		accionSemantica[14][17]=null; 
		accionSemantica[14][18]=null;
		accionSemantica[14][19]=null; 
		accionSemantica[14][20]=null; 
		accionSemantica[14][21]=null;
		accionSemantica[14][22]=null;
		accionSemantica[14][23]=null;
		accionSemantica[14][24]=null;
		accionSemantica[14][25]=null;

}

public void cargarMatrizEstado() {
		
		matrizEstado[0][0]=1;
		matrizEstado[0][1]=2;
		matrizEstado[0][2]=F;
		matrizEstado[0][3]=F;
		matrizEstado[0][4]=F;
		matrizEstado[0][5]=F;
		matrizEstado[0][6]=8;
		matrizEstado[0][7]=6;
		matrizEstado[0][8]=7;
		matrizEstado[0][9]=4;
		matrizEstado[0][10]=F;
		matrizEstado[0][11]=F;
		matrizEstado[0][12]=F;
		matrizEstado[0][13]=F;
		matrizEstado[0][14]=F; 
		matrizEstado[0][15]=F;
		matrizEstado[0][16]=5;
		matrizEstado[0][17]=1; 
		matrizEstado[0][18]=3;
		matrizEstado[0][19]=F; 
		matrizEstado[0][20]=F; 
		matrizEstado[0][21]=10;
		matrizEstado[0][22]=14;
		matrizEstado[0][23]=0;
		matrizEstado[0][24]=0;
		matrizEstado[0][25]=F;
		
		matrizEstado[1][0]=1;
		matrizEstado[1][1]=1;
		matrizEstado[1][2]=F;
		matrizEstado[1][3]=F;
		matrizEstado[1][4]=F;
		matrizEstado[1][5]=F;
		matrizEstado[1][6]=F;
		matrizEstado[1][7]=F;
		matrizEstado[1][8]=F;
		matrizEstado[1][9]=F;
		matrizEstado[1][10]=F;
		matrizEstado[1][11]=F;
		matrizEstado[1][12]=F;
		matrizEstado[1][13]=F;
		matrizEstado[1][14]=F; 
		matrizEstado[1][15]=1;
		matrizEstado[1][16]=F;
		matrizEstado[1][17]=1; 
		matrizEstado[1][18]=F;
		matrizEstado[1][19]=F; 
		matrizEstado[1][20]=F; 
		matrizEstado[1][21]=F;
		matrizEstado[1][22]=F;
		matrizEstado[1][23]=F;
		matrizEstado[1][24]=F;
		matrizEstado[1][25]=F;
		
		matrizEstado[2][0]=F;
		matrizEstado[2][1]=2;
		matrizEstado[2][2]=F;
		matrizEstado[2][3]=F;
		matrizEstado[2][4]=F;
		matrizEstado[2][5]=F;
		matrizEstado[2][6]=F;
		matrizEstado[2][7]=F;
		matrizEstado[2][8]=F;
		matrizEstado[2][9]=F;
		matrizEstado[2][10]=F;
		matrizEstado[2][11]=F;
		matrizEstado[2][12]=F;
		matrizEstado[2][13]=F;
		matrizEstado[2][14]=F; 
		matrizEstado[2][15]=F;
		matrizEstado[2][16]=F;
		matrizEstado[2][17]=14; 
		matrizEstado[2][18]=9;
		matrizEstado[2][19]=F; 
		matrizEstado[2][20]=F; 
		matrizEstado[2][21]=F;
		matrizEstado[2][22]=F;
		matrizEstado[2][23]=F;
		matrizEstado[2][24]=F;
		matrizEstado[2][25]=F;
		
		matrizEstado[3][0]=F;
		matrizEstado[3][1]=9;
		matrizEstado[3][2]=F;
		matrizEstado[3][3]=F;
		matrizEstado[3][4]=F;
		matrizEstado[3][5]=F;
		matrizEstado[3][6]=F;
		matrizEstado[3][7]=F;
		matrizEstado[3][8]=F;
		matrizEstado[3][9]=F;
		matrizEstado[3][10]=F;
		matrizEstado[3][11]=F;
		matrizEstado[3][12]=F;
		matrizEstado[3][13]=F;
		matrizEstado[3][14]=F; 
		matrizEstado[3][15]=F;
		matrizEstado[3][16]=F;
		matrizEstado[3][17]=11;//puede venir 2.D, por ende va a el estado 11 
		matrizEstado[3][18]=F;
		matrizEstado[3][19]=F; 
		matrizEstado[3][20]=F; 
		matrizEstado[3][21]=F;
		matrizEstado[3][22]=F;
		matrizEstado[3][23]=F;
		matrizEstado[3][24]=F;
		matrizEstado[3][25]=F;
		
		matrizEstado[4][0]=4;
		matrizEstado[4][1]=4;
		matrizEstado[4][2]=4;
		matrizEstado[4][3]=4;
		matrizEstado[4][4]=4;
		matrizEstado[4][5]=4;
		matrizEstado[4][6]=4;
		matrizEstado[4][7]=4;
		matrizEstado[4][8]=4;
		matrizEstado[4][9]=4;
		matrizEstado[4][10]=4;
		matrizEstado[4][11]=4;
		matrizEstado[4][12]=4;
		matrizEstado[4][13]=4;
		matrizEstado[4][14]=4; 
		matrizEstado[4][15]=4;
		matrizEstado[4][16]=4;
		matrizEstado[4][17]=4; 
		matrizEstado[4][18]=4;
		matrizEstado[4][19]=4; 
		matrizEstado[4][20]=4; 
		matrizEstado[4][21]=4;
		matrizEstado[4][22]=4;
		matrizEstado[4][23]=4;
		matrizEstado[4][24]=0;
		matrizEstado[4][25]=F;
		
		matrizEstado[5][0]=5;
		matrizEstado[5][1]=5;
		matrizEstado[5][2]=5;
		matrizEstado[5][3]=5;
		matrizEstado[5][4]=5;
		matrizEstado[5][5]=5;
		matrizEstado[5][6]=5;
		matrizEstado[5][7]=5;
		matrizEstado[5][8]=5;
		matrizEstado[5][9]=5;
		matrizEstado[5][10]=5;
		matrizEstado[5][11]=5;
		matrizEstado[5][12]=F;
		matrizEstado[5][13]=5;
		matrizEstado[5][14]=5; 
		matrizEstado[5][15]=5;
		matrizEstado[5][16]=5;
		matrizEstado[5][17]=5; 
		matrizEstado[5][18]=5;
		matrizEstado[5][19]=5; 
		matrizEstado[5][20]=5; 
		matrizEstado[5][21]=5;
		matrizEstado[5][22]=5;
		matrizEstado[5][23]=5;
		matrizEstado[5][24]=5;
		matrizEstado[5][25]=F;
		
		matrizEstado[6][0]=F;
		matrizEstado[6][1]=F;
		matrizEstado[6][2]=F;
		matrizEstado[6][3]=F;
		matrizEstado[6][4]=F;
		matrizEstado[6][5]=F;
		matrizEstado[6][6]=F;
		matrizEstado[6][7]=F;
		matrizEstado[6][8]=F;
		matrizEstado[6][9]=F;
		matrizEstado[6][10]=F;
		matrizEstado[6][11]=F;
		matrizEstado[6][12]=F;
		matrizEstado[6][13]=F;
		matrizEstado[6][14]=F; 
		matrizEstado[6][15]=F;
		matrizEstado[6][16]=F;
		matrizEstado[6][17]=F; 
		matrizEstado[6][18]=F;
		matrizEstado[6][19]=F; 
		matrizEstado[6][20]=F; 
		matrizEstado[6][21]=F;
		matrizEstado[6][22]=F;
		matrizEstado[6][23]=F;
		matrizEstado[6][24]=F;
		matrizEstado[6][25]=F;
		
		matrizEstado[7][0]=F;
		matrizEstado[7][1]=F;
		matrizEstado[7][2]=F;
		matrizEstado[7][3]=F;
		matrizEstado[7][4]=F;
		matrizEstado[7][5]=F;
		matrizEstado[7][6]=F;
		matrizEstado[7][7]=F;
		matrizEstado[7][8]=F;
		matrizEstado[7][9]=F;
		matrizEstado[7][10]=F;
		matrizEstado[7][11]=F;
		matrizEstado[7][12]=F;
		matrizEstado[7][13]=F;
		matrizEstado[7][14]=F; 
		matrizEstado[7][15]=F;
		matrizEstado[7][16]=F;
		matrizEstado[7][17]=F; 
		matrizEstado[7][18]=F;
		matrizEstado[7][19]=F; 
		matrizEstado[7][20]=F; 
		matrizEstado[7][21]=F;
		matrizEstado[7][22]=F;
		matrizEstado[7][23]=F;
		matrizEstado[7][24]=F;
		matrizEstado[7][25]=F;
		
		matrizEstado[8][0]=F;
		matrizEstado[8][1]=F;
		matrizEstado[8][2]=F;
		matrizEstado[8][3]=F;
		matrizEstado[8][4]=F;
		matrizEstado[8][5]=F;
		matrizEstado[8][6]=F;
		matrizEstado[8][7]=F;
		matrizEstado[8][8]=F;
		matrizEstado[8][9]=F;
		matrizEstado[8][10]=F;
		matrizEstado[8][11]=F;
		matrizEstado[8][12]=F;
		matrizEstado[8][13]=F;
		matrizEstado[8][14]=F; 
		matrizEstado[8][15]=F;
		matrizEstado[8][16]=F;
		matrizEstado[8][17]=F; 
		matrizEstado[8][18]=F;
		matrizEstado[8][19]=F; 
		matrizEstado[8][20]=F; 
		matrizEstado[8][21]=F;
		matrizEstado[8][22]=F;
		matrizEstado[8][23]=F;
		matrizEstado[8][24]=F;
		matrizEstado[8][25]=F;
		
		matrizEstado[9][0]=F;
		matrizEstado[9][1]=9;
		matrizEstado[9][2]=F;
		matrizEstado[9][3]=F;
		matrizEstado[9][4]=F;
		matrizEstado[9][5]=F;
		matrizEstado[9][6]=F;
		matrizEstado[9][7]=F;
		matrizEstado[9][8]=F;
		matrizEstado[9][9]=F;
		matrizEstado[9][9]=F;
		matrizEstado[9][11]=F;
		matrizEstado[9][12]=F;
		matrizEstado[9][13]=F;
		matrizEstado[9][14]=F; 
		matrizEstado[9][15]=F;
		matrizEstado[9][16]=F;
		matrizEstado[9][17]=11; 
		matrizEstado[9][18]=F;
		matrizEstado[9][19]=F; 
		matrizEstado[9][20]=F; 
		matrizEstado[9][21]=F;
		matrizEstado[9][22]=F;
		matrizEstado[9][23]=F;
		matrizEstado[9][24]=F;
		matrizEstado[9][25]=F;
		
		matrizEstado[10][0]=F;
		matrizEstado[10][1]=F;
		matrizEstado[10][2]=F;
		matrizEstado[10][3]=F;
		matrizEstado[10][4]=F;
		matrizEstado[10][5]=F;
		matrizEstado[10][6]=F;
		matrizEstado[10][7]=F;
		matrizEstado[10][8]=F;
		matrizEstado[10][9]=F;
		matrizEstado[10][10]=F;
		matrizEstado[10][11]=F;
		matrizEstado[10][12]=F;
		matrizEstado[10][13]=F;
		matrizEstado[10][14]=F; 
		matrizEstado[10][15]=F;
		matrizEstado[10][16]=F;
		matrizEstado[10][17]=F; 
		matrizEstado[10][18]=F;
		matrizEstado[10][19]=F; 
		matrizEstado[10][20]=F; 
		matrizEstado[10][21]=F;
		matrizEstado[10][22]=F;
		matrizEstado[10][23]=F;
		matrizEstado[10][24]=F;
		matrizEstado[10][25]=F;
		
		matrizEstado[11][0]=14;
		matrizEstado[11][1]=12;
		matrizEstado[11][2]=14;
		matrizEstado[11][3]=14;
		matrizEstado[11][4]=13;
		matrizEstado[11][5]=13;
		matrizEstado[11][6]=14;
		matrizEstado[11][7]=14;
		matrizEstado[11][8]=14;
		matrizEstado[11][9]=14;
		matrizEstado[11][10]=14;
		matrizEstado[11][11]=14;
		matrizEstado[11][12]=14;
		matrizEstado[11][13]=14;
		matrizEstado[11][14]=14; 
		matrizEstado[11][15]=14;
		matrizEstado[11][16]=14;
		matrizEstado[11][17]=14; 
		matrizEstado[11][18]=14;
		matrizEstado[11][19]=14; 
		matrizEstado[11][20]=14; 
		matrizEstado[11][21]=14;
		matrizEstado[11][22]=14;
		matrizEstado[11][23]=0;
		matrizEstado[11][24]=0;
		matrizEstado[11][25]=0;
		
		matrizEstado[12][0]=F;
		matrizEstado[12][1]=12;
		matrizEstado[12][2]=F;
		matrizEstado[12][3]=F;
		matrizEstado[12][4]=F;
		matrizEstado[12][5]=F;
		matrizEstado[12][6]=F;
		matrizEstado[12][7]=F;
		matrizEstado[12][8]=F;
		matrizEstado[12][9]=F;
		matrizEstado[12][10]=F;
		matrizEstado[12][11]=F;
		matrizEstado[12][12]=F;
		matrizEstado[12][13]=F;
		matrizEstado[12][14]=F; 
		matrizEstado[12][15]=F;
		matrizEstado[12][16]=F;
		matrizEstado[12][17]=F; 
		matrizEstado[12][18]=F;
		matrizEstado[12][19]=F; 
		matrizEstado[12][20]=F; 
		matrizEstado[12][21]=F;
		matrizEstado[12][22]=F;
		matrizEstado[12][23]=F;
		matrizEstado[12][24]=F;
		matrizEstado[12][25]=F;
		
		matrizEstado[13][0]=14;
		matrizEstado[13][1]=12;
		matrizEstado[13][2]=14;
		matrizEstado[13][3]=14;
		matrizEstado[13][4]=14;
		matrizEstado[13][5]=14;
		matrizEstado[13][6]=14;
		matrizEstado[13][7]=14;
		matrizEstado[13][8]=14;
		matrizEstado[13][9]=14;
		matrizEstado[13][10]=14;
		matrizEstado[13][11]=14;
		matrizEstado[13][12]=14;
		matrizEstado[13][13]=14;
		matrizEstado[13][14]=14;
		matrizEstado[13][15]=14;
		matrizEstado[13][16]=14;
		matrizEstado[13][17]=14;
		matrizEstado[13][18]=14;
		matrizEstado[13][19]=14;
		matrizEstado[13][20]=14;
		matrizEstado[13][21]=14;
		matrizEstado[13][22]=14;
		matrizEstado[13][23]=0;
		matrizEstado[13][24]=0;
		matrizEstado[13][25]=0;
		
		matrizEstado[14][0]=14;
		matrizEstado[14][1]=14;
		matrizEstado[14][2]=14;
		matrizEstado[14][3]=14;
		matrizEstado[14][4]=14;
		matrizEstado[14][5]=14;
		matrizEstado[14][6]=14;
		matrizEstado[14][7]=14;
		matrizEstado[14][8]=14;
		matrizEstado[14][9]=14;
		matrizEstado[14][10]=14;
		matrizEstado[14][11]=14;
		matrizEstado[14][12]=14;
		matrizEstado[14][13]=0;
		matrizEstado[14][14]=0;
		matrizEstado[14][15]=14;
		matrizEstado[14][16]=14;
		matrizEstado[14][17]=14;
		matrizEstado[14][18]=14;
		matrizEstado[14][19]=14;
		matrizEstado[14][20]=14;
		matrizEstado[14][21]=14;
		matrizEstado[14][22]=14;
		matrizEstado[14][23]=0;
		matrizEstado[14][24]=0;
		matrizEstado[14][25]=0;

	}

public String getBuffer_temporal(){
		return this.buffer_temporal;
}
	
public void setBuffer_temporal(String s) {
		this.buffer_temporal = s;
}
	
public ControladorArchivo getArchivo(){
		return this.archivo;
}

	public int getNroLinea() {
		return this.getArchivo().getLinea();
	}

private int ObtenerColumna(char entrada) {
		int ascii = (int) entrada;
		if (ascii<=57 && ascii >= 48) {
			ascii = INPUTDIGITO;
		}else if ((ascii<=90 && ascii >= 65 && ascii!=68) || (ascii<=122 && ascii >= 97 && ascii!=100)) {
			ascii = INPUTLETRA;
		}
		if (!mapEntrada.containsKey(ascii)) {
			return 22;
		} else {return mapEntrada.get(ascii);}
	}
	

	public int yylex() {
		
		estado = 0;
		buffer_temporal = "";
		int aux=0;
		
		//Para que no almacene en el buffer los espacios vacios
		while ( (archivo.getActual() == ' ' ) || (archivo.getActual() == '	'  ) || (archivo.getActual() == '\n' ))
				archivo.avanzar();
		while (estado != F){ // QUE NO SEA UN ESTADO FINAL.
			if (!archivo.finArchivo()){ // QUE NO SE HAYA TERMINADO EL ARCHIVO
				this.entrada = archivo.getActual();
				int columna= ObtenerColumna(entrada);
				if(accionSemantica[estado][columna]!=null)
					aux=accionSemantica[estado][columna].ejecutar(this); 		
				estado=matrizEstado[estado][columna];
				this.archivo.avanzar();
			}
			else{
				return 0; // llegamos a un fin de archivo, consultar !!!
			}	
		}
		
		if (estado == F){
			estado = 0;
			Token t = new Token(aux, getBuffer_temporal());
			this.tokens.add(t);
		}
		return aux;
		
	}
	
	public void setYYlex(String ylex) {
		this.ylex = ylex;
	}
	
	public String getYYlex() {
		return ylex;
	}
	
	public char getEntrada (){
		return this.entrada;
	}

	public TablaSimbolos getTablaSimbolos() {
		return this.tablaSimbolos;
	}
	
	/*public void imprimirTablaSimbolos() {
		System.out.println( "////////////////////// TABLA DE SIMBOLOS //////////////////" + '\n');
		Enumeration<String> e = this.tablaSimbolos.obtenerLexemas();
		String s;
		Token t;
		while( e.hasMoreElements() ){
		  s = e.nextElement();
		  t = tablaSimbolos.get(s);
		  System.out.println( "Lexema : " + s + " " + t.toString() );
		}
	
	}*/
	public void imprimirTablaSimbolos() {
		this.tablaSimbolos.imprimir();
	}

	public void addError(Error e) {
		e.setNrolinea(getNroLinea());
		if (e != null)
			this.errores.add(e);		
	}
	
	public void addWarning(Error e) {
		e.setNrolinea(getNroLinea());
		if(e != null)
			this.warnings.add(e);
	}

	
	public ArrayList<Integer> devolverTokens() {
		ArrayList<Integer> aux= new ArrayList<Integer>();
		while (!archivo.finArchivo()) {
				aux.add(yylex());
		}
		return aux;
	}
	
	
	
/*	public void imprimirTokens(ArrayList<Integer> a) {
		System.out.println( "////////////////////// TOKENS //////////////////" + '\n');
		for (int i=0; i < listInts.size(); i++)			
			System.out.println("Token: " + listInts.get(i) + '\n');
		
	}*/
	
	public void imprimirTokens() {
		System.out.println("---------------------------------------------------------------------------------------");
		System.out.println( "			TOKENS" + '\n');
		for (int i=0; i < tokens.size(); i++)			
			System.out.println(tokens.get(i));
			System.out.println("");
	}
	
	public int getVAuxDouble() {
		vAuxDouble = vAuxDouble + 1;
		return this.vAuxDouble;	
	}
	
	public void mostrarErrorLex() {
		System.out.println( "////////////////////// ERRORES LEXICOS //////////////////" + '\n');
		//System.out.println(this.errores);
		for (int i=0; i<errores.size(); i++)
			System.out.println(errores.get(i).toString());
		for (int i=0; i<warnings.size(); i++)
			System.out.println(warnings.get(i).toString());
	}
	
	public boolean hayErrores() {
		return (errores.size()>0);
	}
	
}
