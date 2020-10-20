%token ID
%token MULTI_LINEA
%token CTEDOUBLE
%token S_ASIGNACION
%token S_MAYOR_IGUAL
%token S_MENOR_IGUAL
%token CTEENTERA
%token S_PORCENTAJE
%token COMENTARIO
%token S_DISTINTO
%token S_IGUAL

%token IF
%token ELSE 
%token ENDIF
%token UNTIL
%token DO
%token PRINT
%token BEGIN
%token END 
%token INT
%token DOUBLE
%token first
%token last
%token length
%start programa
%%

%{
package AnalizadorSintactico;
import AnalizadorLexico.*;
import AnalizadorLexico.Error;
import AnalizadorSintactico.*;
import java.util.ArrayList;
import CodigoIntermedio.*;
%}


programa : declaraciones BEGIN bloques_de_sentencias END ';' {analizadorS.addEstructura (new Error ( analizadorS.estructuraPRINCIPAL,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()  )); }
		 | declaraciones {analizadorS.addEstructura (new Error ( analizadorS.estructuraPRINCIPAL,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()  )); }
		 | declaraciones BEGIN error ';' {analizadorS.addError (new Error ( analizadorS.errorSentencias,"ERROR SINTACTICO", controladorArchivo.getLinea() )); } 
		 | BEGIN bloques_de_sentencias END ';' {analizadorS.addEstructura (new Error ( analizadorS.estructuraPRINCIPAL,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea() ));}
		 | declaraciones BEGIN bloques_de_sentencias END { analizadorS.addError (new Error ( analizadorS.errorPuntoComa,"ERROR SINTACTICO", controladorArchivo.getLinea()-1)); }
		 | BEGIN bloques_de_sentencias END {analizadorS.addError (new Error ( analizadorS.errorPuntoComa,"ERROR SINTACTICO", controladorArchivo.getLinea()-1)); }
		 | BEGIN bloques_de_sentencias  {analizadorS.addError (new Error ( analizadorS.errorEND,"ERROR SINTACTICO", controladorArchivo.getLinea()-1)); }
		 | declaraciones BEGIN bloques_de_sentencias  {analizadorS.addError (new Error ( analizadorS.errorEND,"ERROR SINTACTICO", controladorArchivo.getLinea()-1)); }
		 | bloques_de_sentencias {analizadorS.addError (new Error ( analizadorS.errorSentencias,"ERROR SINTACTICO", controladorArchivo.getLinea())); }
		;

declaraciones : declaraciones sentencia_declarativa
				| sentencia_declarativa
				;		
		
sentencia_declarativa : tipo variables ';' { String tipo = ((Token) $1.obj).getValor();
										analizadorCI.declaracionParametros(tipo, (ArrayList<Token>)$2.obj,tablaSimbolo,controladorArchivo);
						analizadorS.addEstructura (new Error ( analizadorS.estructuraDECLARACION,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()  ));}
					| error ';'{ analizadorS.addError (new Error ( analizadorS.errorDeclaracionVar,"ERROR SINTACTICO", controladorArchivo.getLinea() ));}	
					| tipo variables { analizadorS.addError (new Error ( analizadorS.errorPuntoComa,"ERROR SINTACTICO",    controladorArchivo.getLinea()-1));}
					| tipo error ';' { analizadorS.addError (new Error ( analizadorS.errorDeclaracionVar,"ERROR SINTACTICO", controladorArchivo.getLinea() ));}
					| error sentencia_declarativa {analizadorS.addError (new Error ( analizadorS.errorDeclaracionVar,"ERROR SINTACTICO", controladorArchivo.getLinea()-1)); }
				;

variables : variables ',' lista_variables	{ArrayList<Token> t1 =(ArrayList<Token>)$1.obj;
						ArrayList<Token> t2 =(ArrayList<Token>)$3.obj;
						t1.addAll(t2);
						$$ = new ParserVal(t1);}
		| variables ',' coleccion	{ArrayList<Token> t1 =(ArrayList<Token>)$1.obj;
						ArrayList<Token> t2 =(ArrayList<Token>)$3.obj;
						t1.addAll(t2);
						$$ = new ParserVal(t1);}
		| coleccion	{$$ = new ParserVal((ArrayList<Token>)$1.obj);}
		| lista_variables {$$ = new ParserVal((ArrayList<Token>)$1.obj);}
		;
	
lista_variables: ID {	ArrayList<Token> lista = new ArrayList<>();
					Token t = (Token)$1.obj;
					if (!t.existeAtributo("Uso")){
						t.AgregarAtributo("Uso",analizadorL.variable);
					}
                			lista.add(t);
                			$$ = new ParserVal(lista); }
				;
				
				
coleccion : ID '[' CTEENTERA']' {	ArrayList<Token> lista = new ArrayList<>();
					Token t = (Token)$1.obj;
					if (!t.existeAtributo("Uso")){
						t.AgregarAtributo("Uso",analizadorL.coleccion);
						t.AgregarAtributo("Length",((Token)$3.obj).getAtributo("Valor"));
					}
                			lista.add(t);
                			$$ = new ParserVal(lista); }
		  | error '[' CTEENTERA']' { analizadorS.addError (new Error ( analizadorS.errorDeclaracionColeccion,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }
		  | ID '[' error ']'  { analizadorS.addError (new Error ( analizadorS.errorSubIndice,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }
		  ;
		  
bloques_de_sentencias : bloques_de_sentencias sentencia
					  | sentencia
			;
			
sentencia : sentencia_control
		 | sentencia_seleccion
		 | print
		 | asignacion
		 | error sentencia {analizadorS.addError (new Error ( analizadorS.errorSentencias,"ERROR SINTACTICO", controladorArchivo.getLinea()-1)); }
		 ;

lado_izquierdo : ID	{	//chequeo semantico variable no declarada
					Token t1 = (Token)$1.obj;						
		    			if  ( !t1.existeAtributo("Uso") ) 
							analizadorCI.addError (new Error ( analizadorCI.errorNoExisteVariable,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));

						$$ = new ParserVal( t1 );}
		| ID '[' subindice ']'	{	Token t1 = (Token)$1.obj;						
		    		if  ( !t1.existeAtributo("Uso") ) {
					analizadorCI.addError (new Error ( analizadorCI.errorNoExisteVariable,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));
					$$ = new ParserVal(t1);
				}else{
				String tipo = (String)((Token)$1.obj).getAtributo("Tipo_Uso");
				TercetoColeccionIndice terceto = new TercetoColeccionIndice ( new Token(controladorTercetos.CGI), (Token)$1.obj ,  (Token)$3.obj , controladorTercetos.getProxNumero() );
				controladorTercetos.addTerceto (terceto);
				Token nuevo = new Token( controladorTercetos.numeroTercetoString() ) ;
				Token aux = new Token( controladorTercetos.numeroTercetoString() ) ;
				nuevo.AgregarAtributo("Tipo_Uso",tipo);
				nuevo.AgregarAtributo("Uso",analizadorL.variable);
				nuevo.AgregarAtributo("Direccion",true);
				aux.AgregarAtributo("Direccion",true);
				aux.AgregarAtributo("Tipo_Uso",tipo);
				aux.AgregarAtributo("Uso",analizadorL.vAuxiliar);
				tablaSimbolo.add("@aux"+aux.getValor(),aux);
				$$ = new ParserVal( nuevo );}
				}
		;
				
				
asignacion : lado_izquierdo S_ASIGNACION expresion ';' { analizadorS.addEstructura (new Error ( analizadorS.estructuraASIG,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()  )); 
							String valor =":=";	
														Token t1 = (Token) $1.obj;
														Token t2 = (Token) $3.obj;
														if ( (t1 != null) && (t2 != null) ){
															if(!tipoCompatible(t1,t2))
																analizadorCI.addError (new Error ( analizadorCI.errorTiposDiferentes,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));
														}
													if (t1.existeAtributo("Uso")){
														if (((int)t1.getAtributo("Uso")) == analizadorL.variable){
															TercetoAsignacion terceto;
															terceto = new TercetoAsignacion (  new Token(analizadorL.S_ASIGNACION, ":=" ) , (Token)$1.obj ,   (Token)$3.obj , controladorTercetos.getProxNumero() );
														
															controladorTercetos.addTerceto (terceto);	
														} else{
															TercetoColeccionAsignacion terceto = new TercetoColeccionAsignacion( new Token(analizadorL.S_ASIGNACION, ":=" ) , (Token)$1.obj ,  (Token)$3.obj , controladorTercetos.getProxNumero() );
														
															controladorTercetos.addTerceto (terceto);
														}
													}							
														$$ = new ParserVal((Token)$1.obj);
							}
			| lado_izquierdo S_ASIGNACION expresion { analizadorS.addError (new Error ( analizadorS.errorPuntoComa,"ERROR SINTACTICO", controladorArchivo.getLinea()-1));}
			| error S_ASIGNACION expresion ';'{ analizadorS.addError (new Error ( analizadorS.errorAsignacion,"ERROR SINTACTICO", controladorArchivo.getLinea() )); }
			| lado_izquierdo S_ASIGNACION error ';'{ analizadorS.addError (new Error ( analizadorS.errorAsignacion,"ERROR SINTACTICO", controladorArchivo.getLinea() )); }
			| lado_izquierdo '=' expresion ';'{ analizadorS.addError (new Error ( analizadorS.errorAsignacionIgual,"ERROR SINTACTICO", controladorArchivo.getLinea() )); }
			| lado_izquierdo '=' expresion { analizadorS.addError (new Error ( analizadorS.errorAsignacionIgual,"ERROR SINTACTICO", controladorArchivo.getLinea()-1 )); }
			;

subindice : ID	{	Token t1 = (Token)$1.obj;						
		    	if  ( !t1.existeAtributo("Uso") ) {
				analizadorCI.addError (new Error ( analizadorCI.errorNoExisteVariable,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));
			} else if ((int)t1.getAtributo("Uso") == analizadorL.coleccion){
				analizadorCI.addError (new Error ( analizadorCI.errorColeccionPorVariable,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));}
			$$ = new ParserVal((Token)$1.obj);}
		  | CTEENTERA	{$$ = new ParserVal((Token)$1.obj);}
		  ;

print : PRINT '(' MULTI_LINEA ')' ';' {	TercetoPrint terceto = new TercetoPrint (  new Token(analizadorL.PRINT,"Print") , (Token)$3.obj , null, controladorTercetos.getProxNumero() );
					terceto.setNroPrint(controladorTercetos.getNroPrint());
					controladorTercetos.addTerceto (terceto);
					controladorTercetos.addPrint(terceto);
					analizadorS.addEstructura (new Error ( analizadorS.estructuraPrint,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()  )); }
		  | PRINT '(' lado_izquierdo  ')' ';' {	TercetoPrint terceto = new TercetoPrint ( new Token(analizadorL.PRINT,"Print") , (Token)$3.obj , null, controladorTercetos.getProxNumero() );
					if (((Token)$3.obj).existeAtributo("Uso")){
						if ((int)((Token)$3.obj).getAtributo("Uso") == analizadorL.coleccion){	analizadorCI.addError (new Error ( analizadorCI.errorFaltaIndiceColeccion,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));}
					}
					terceto.setTipoPrint(1);
					controladorTercetos.addTerceto (terceto);
					analizadorS.addEstructura (new Error ( analizadorS.estructuraPrint,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()  )); }
		  | PRINT '(' error ')' ';' { analizadorS.addError (new Error ( analizadorS.errorPrint1,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }	 
		  | PRINT '(' MULTI_LINEA ')'  { analizadorS.addError (new Error ( analizadorS.errorPuntoComa,"ERROR SINTACTICO", controladorArchivo.getLinea()-1)); }
		  | PRINT '(' lado_izquierdo ')'  { analizadorS.addError (new Error ( analizadorS.errorPuntoComa,"ERROR SINTACTICO", controladorArchivo.getLinea()-1)); }
		  | error '(' MULTI_LINEA ')' ';' { analizadorS.addError (new Error ( analizadorS.errorPrint2,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }
	  ;

condicion_sin_parentesis : expresion operador expresion {TercetoComparacion terceto = new TercetoComparacion (  (Token)$2.obj  , (Token)$1.obj , (Token)$3.obj , controladorTercetos.getProxNumero() );
															controladorTercetos.addTerceto (terceto);
															
															if(!tipoCompatible((Token)$1.obj,(Token)$3.obj))
																analizadorCI.addError (new Error ( analizadorCI.errorTiposDiferentes,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));
															Token nuevo = new Token( controladorTercetos.numeroTercetoString() );
															Token aux = new Token( controladorTercetos.numeroTercetoString() );
															nuevo.AgregarAtributo("Tipo",((Token)$1.obj).getTipo());
															aux.AgregarAtributo("Tipo",((Token)$1.obj).getTipo());
															nuevo.AgregarAtributo("Valor",((Token) $2.obj).getValor());
															aux.AgregarAtributo("Uso",analizadorL.vAuxiliar);
															tablaSimbolo.add("@aux"+aux.getValor(),aux);
															$$ = new ParserVal(nuevo);
							analizadorS.addEstructura( new Error ( analizadorS.estructuraCONDICION,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()  )); }
                          |  error operador expresion { analizadorS.addError (new Error ( analizadorS.errorCondicionI,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }
                          |  expresion operador error  { analizadorS.addError (new Error ( analizadorS.errorCondicionD,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }
                          ;


condicion : '(' condicion_sin_parentesis ')' { $$ = $2;}
          | condicion_sin_parentesis ')' { analizadorS.addError (new Error ( analizadorS.errorParentesisA,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }
          | '(' condicion_sin_parentesis error { analizadorS.addError (new Error ( analizadorS.errorParentesisC,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }
          ;

		
cuerpo_if : BEGIN bloques_de_sentencias END ';'
		  | sentencia
		| bloques_de_sentencias sentencia {analizadorS.addError (new Error ( analizadorS.errorBloqueEnBeginEnd ,"ERROR SINTACTICO", controladorArchivo.getLinea() ));}
		| bloques_de_sentencias BEGIN bloques_de_sentencias END ';' {analizadorS.addError (new Error ( analizadorS.errorBloqueEnBeginEnd ,"ERROR SINTACTICO", controladorArchivo.getLinea() ));}
		| BEGIN bloques_de_sentencias END ';' bloques_de_sentencias {analizadorS.addError (new Error ( analizadorS.errorElse ,"ERROR SINTACTICO", controladorArchivo.getLinea() ));}
          | bloques_de_sentencias END ';' { analizadorS.addError (new Error ( analizadorS.errorBEGIN,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }
          | BEGIN bloques_de_sentencias { analizadorS.addError (new Error ( analizadorS.errorEND,"ERROR SINTACTICO", controladorArchivo.getLinea()  ));}
		  ;
		            
cuerpo_else : BEGIN bloques_de_sentencias END ';'
            | sentencia
		| bloques_de_sentencias sentencia {analizadorS.addError (new Error ( analizadorS.errorBloqueEnBeginEnd ,"ERROR SINTACTICO", controladorArchivo.getLinea() ));}
		| bloques_de_sentencias BEGIN bloques_de_sentencias END ';' {analizadorS.addError (new Error ( analizadorS.errorBloqueEnBeginEnd ,"ERROR SINTACTICO", controladorArchivo.getLinea() ));}
		| BEGIN bloques_de_sentencias END ';' bloques_de_sentencias {analizadorS.addError (new Error ( analizadorS.errorBloqueEnBeginEnd ,"ERROR SINTACTICO", controladorArchivo.getLinea() ));}
			| bloques_de_sentencias END ';'{ analizadorS.addError (new Error ( analizadorS.errorBEGIN,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }
          	| BEGIN bloques_de_sentencias { analizadorS.addError (new Error ( analizadorS.errorEND,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }
            ;     
    
sentecia_if_condicion : IF  condicion 	{	TercetoIf terceto = new TercetoIf (  (new Token( controladorTercetos.BF)  ), new Token( controladorTercetos.numeroTercetoString()  ), null, controladorTercetos.getProxNumero() );
											terceto.setTipo(((Token)$2.obj).getTipo());
											terceto.setTipoSalto(((Token)$2.obj).getValor());
											controladorTercetos.addTerceto (terceto);
											controladorTercetos.apilar(); 
										}
					  ;

sentencia_seleccion  : sentecia_if_condicion  cuerpo_if ELSE {	
													TercetoIf terceto = new TercetoIf (  new Token( controladorTercetos.BI)  , null, null, controladorTercetos.getProxNumero() );
													controladorTercetos.addTerceto (terceto);
													controladorTercetos.desapilar();
													controladorTercetos.apilar();
										}
						cuerpo_else ENDIF ';' { 	controladorTercetos.desapilar();
													analizadorS.addEstructura (new Error ( analizadorS.estructuraIF,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()  )); }
                 	 | error  condicion  cuerpo_if ELSE cuerpo_else ENDIF ';' { analizadorS.addError (new Error ( analizadorS.errorPalabraIF,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }                     
                                        	 
                     | sentecia_if_condicion  cuerpo_if ENDIF ';' { controladorTercetos.desapilar();
                     												analizadorS.addEstructura (new Error ( analizadorS.estructuraIF,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()  )); }
                     | sentecia_if_condicion cuerpo_if ENDIF error { analizadorS.addError (new Error ( analizadorS.errorPuntoComa,"ERROR SINTACTICO", controladorArchivo.getLinea()-1 )); }
                     | error  condicion  cuerpo_if ENDIF ';' { analizadorS.addError (new Error ( analizadorS.errorPalabraIF,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()  )); }
                     ;


inicio_do: DO { TercetoLabel tercetoLabel = new TercetoLabel(null,null,null,controladorTercetos.getProxNumero());
				controladorTercetos.addTerceto(tercetoLabel);
		controladorTercetos.apilarDo();
		analizadorS.addEstructura (new Error ( analizadorS.inicioDO,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()-1  )); }
				
sentencia_control : inicio_do sentencia UNTIL condicion ';' { 	TercetoIf terceto = new TercetoIf (  (new Token( controladorTercetos.BF)  ), new Token( controladorTercetos.numeroTercetoString()  ), null, controladorTercetos.getProxNumero() );
											terceto.setTipo(((Token)$4.obj).getTipo());
											terceto.setTipoSalto(((Token)$4.obj).getValor());
											controladorTercetos.addTerceto (terceto);
											controladorTercetos.desapilarDo(terceto);
													analizadorS.addEstructura (new Error (analizadorS.estructuraDO, "ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()));	}
				  | inicio_do sentencia UNTIL condicion  { 	controladorTercetos.desapilarError();
										analizadorS.addError (new Error ( analizadorS.errorPuntoComa,"ERROR SINTACTICO", controladorArchivo.getLinea()-1 )); }
				  | inicio_do BEGIN bloques_de_sentencias END ';' UNTIL condicion ';' { TercetoIf terceto = new TercetoIf ( (new Token( controladorTercetos.BF)  ), new Token( controladorTercetos.numeroTercetoString()  ), null, controladorTercetos.getProxNumero() );
													terceto.setTipo(((Token)$7.obj).getTipo());
													terceto.setTipoSalto(((Token)$7.obj).getValor());
													controladorTercetos.addTerceto (terceto);
													controladorTercetos.desapilarDo(terceto);
														analizadorS.addEstructura (new Error (analizadorS.estructuraDO, "ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()));}
				  | inicio_do BEGIN bloques_de_sentencias END ';' UNTIL condicion { controladorTercetos.desapilarError();	
													analizadorS.addError (new Error ( analizadorS.errorPuntoComa,"ERROR SINTACTICO", controladorArchivo.getLinea()-1 )); }
				  | inicio_do bloques_de_sentencias END ';' condicion ';'  { 	controladorTercetos.desapilarError();
												analizadorS.addError (new Error (analizadorS.errorDOUNTIL, "ERROR SINTACTICO", controladorArchivo.getLinea()));}
				  | inicio_do error END ';' condicion ';' { 	controladorTercetos.desapilarError();
										analizadorS.addError (new Error (analizadorS.errorDO, "ERROR SINTACTICO", controladorArchivo.getLinea()));}
				  | inicio_do bloques_de_sentencias sentencia UNTIL condicion ';' {	controladorTercetos.desapilarError();
													analizadorS.addError (new Error ( analizadorS.errorBloqueEnBeginEnd ,"ERROR SINTACTICO", controladorArchivo.getLinea() ));}
				  | inicio_do bloques_de_sentencias BEGIN bloques_de_sentencias END ';' UNTIL condicion ';' {	controladorTercetos.desapilarError();
																	analizadorS.addError (new Error ( analizadorS.errorBloqueEnBeginEnd ,"ERROR SINTACTICO", controladorArchivo.getLinea() ));}
				  | inicio_do BEGIN bloques_de_sentencias END ';' bloques_de_sentencias UNTIL condicion ';' {	controladorTercetos.desapilarError();
																analizadorS.addError (new Error ( analizadorS.errorBloqueEnBeginEnd ,"ERROR SINTACTICO", controladorArchivo.getLinea() ));} 
				  | inicio_do BEGIN bloques_de_sentencias END ';' UNTIL error ';' { 	controladorTercetos.desapilarError();
														analizadorS.addError (new Error (analizadorS.errorDOCondicion, "ERROR SINTACTICO", controladorArchivo.getLinea()));}
				  | inicio_do sentencia UNTIL error ';' { 	controladorTercetos.desapilarError();
										analizadorS.addError (new Error (analizadorS.errorDOCondicion, "ERROR SINTACTICO", controladorArchivo.getLinea()));}
				  ;
				  
expresion : termino	{$$=$1;}
			| expresion '+' termino{	Token t1 = (Token) $1.obj;
							Token t2 = (Token) $3.obj;
										if ( (t1 != null) && (t2 != null) ){
											if(!tipoCompatible(t1,t2))
												analizadorCI.addError (new Error ( analizadorCI.errorTiposDiferentes,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));
										}			
										String valor ="+";
										TercetoExpresion terceto = new TercetoExpresion (  new Token((int) valor.charAt(0),"+"  ), (Token)$1.obj ,  (Token)$3.obj , controladorTercetos.getProxNumero() );
										controladorTercetos.addTerceto (terceto);
										Token nuevo = new Token( controladorTercetos.numeroTercetoString() ) ;
										nuevo.AgregarAtributo("Tipo",((Token)$1.obj).getTipo());
										nuevo.AgregarAtributo("Uso",analizadorL.vAuxiliar);
										tablaSimbolo.add("@aux"+nuevo.getValor(),nuevo);
										$$ = new ParserVal(nuevo);	}
			| expresion '-' termino {	Token t1 = (Token) $1.obj;
							Token t2 = (Token) $3.obj;
										if ( (t1 != null) && (t2 != null) ){
											if(!tipoCompatible(t1,t2))
												analizadorCI.addError (new Error ( analizadorCI.errorTiposDiferentes,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));
										}			
										String valor ="-";
										TercetoExpresion terceto = new TercetoExpresion (  new Token((int) valor.charAt(0),"-"  ), (Token)$1.obj ,  (Token)$3.obj , controladorTercetos.getProxNumero() );
										controladorTercetos.addTerceto (terceto);
										Token nuevo = new Token( controladorTercetos.numeroTercetoString() ) ;
										nuevo.AgregarAtributo("Tipo",((Token)$1.obj).getTipo());
										nuevo.AgregarAtributo("Uso",analizadorL.vAuxiliar);
										tablaSimbolo.add("@aux"+nuevo.getValor(),nuevo);
										$$ = new ParserVal(nuevo);}
			;
		

termino : factor {$$=$1;}
		| termino '*' factor 	{	Token t1 = (Token) $1.obj;
						Token t2 = (Token) $3.obj;
										if ( (t1 != null) && (t2 != null) ){
											if(!tipoCompatible(t1,t2))
												analizadorCI.addError (new Error ( analizadorCI.errorTiposDiferentes,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));
										}
										String valor ="*";
										TercetoExpresionMult terceto = new TercetoExpresionMult (  new Token((int) valor.charAt(0), "*"  ),(Token)$1.obj ,  (Token)$3.obj , controladorTercetos.getProxNumero() );
										controladorTercetos.addTerceto (terceto);
										Token nuevo = new Token( controladorTercetos.numeroTercetoString() );
										nuevo.AgregarAtributo("Tipo",((Token)$1.obj).getTipo());
										nuevo.AgregarAtributo("Uso",analizadorL.vAuxiliar);
										tablaSimbolo.add("@aux"+nuevo.getValor(),nuevo);
										$$ = new ParserVal(nuevo);
								}
		| termino '/' factor	{Token t1 = (Token) $1.obj;
					Token t2 = (Token) $3.obj;
						if ( (t1 != null) && (t2 != null) ){
							if(!tipoCompatible(t1,t2))
								analizadorCI.addError (new Error ( analizadorCI.errorTiposDiferentes,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));
						}
						String valor ="/";
						TercetoExpresionDiv terceto = new TercetoExpresionDiv (  new Token((int) valor.charAt(0), "/" ) , (Token)$1.obj ,  (Token)$3.obj , controladorTercetos.getProxNumero() );
						controladorTercetos.addTerceto (terceto);
						Token nuevo = new Token( controladorTercetos.numeroTercetoString() );
						nuevo.AgregarAtributo("Tipo",((Token)$1.obj).getTipo());
						nuevo.AgregarAtributo("Uso",analizadorL.vAuxiliar);
						tablaSimbolo.add("@aux"+nuevo.getValor(),nuevo);
						$$ = new ParserVal(nuevo);
					}
		;

factor : CTEENTERA {$$=$1;}
		| CTEDOUBLE	{	if(!((Token)$1.obj).existeAtributo("AuxDouble"))
						((Token)$1.obj).AgregarAtributo("AuxDouble",analizadorL.getVAuxDouble());
					$$=$1;}
		| lado_izquierdo { if (((Token)$1.obj).existeAtributo("Uso")){
					if ((int)((Token)$1.obj).getAtributo("Uso") == analizadorL.coleccion){	analizadorCI.addError (new Error ( analizadorCI.errorFaltaIndiceColeccion,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));}
					}
					$$=$1;}	
		| metodos	{$$=$1;}
		| '-' CTEENTERA {Object valor =((Token)$2.obj).getAtributo("Valor");
					Token t = new Token (analizadorL.tipoInt,CTEENTERA,"-"+valor);
					analizadorL.getTablaSimbolos().add("-"+valor,t);
					$$=new ParserVal(t);}
		| '-' CTEDOUBLE {Object valor =((Token)$2.obj).getAtributo("Valor");
				if (!analizadorL.getTablaSimbolos().existe("-"+valor)){
					Token t = new Token (analizadorL.tipoDouble,CTEDOUBLE,"-"+valor);
					if(!t.existeAtributo("AuxDouble"))
						t.AgregarAtributo("AuxDouble",analizadorL.getVAuxDouble());
					analizadorL.getTablaSimbolos().add("-"+valor,t);
					$$=new ParserVal(t);}
				else
					$$=new ParserVal(analizadorL.getTablaSimbolos().get("-"+valor));}
		;

metodos : ID '.' nombre_metodo {Token t1= (Token)$1.obj;
				if  ( !t1.existeAtributo("Uso") ) {
					analizadorCI.addError (new Error ( analizadorCI.errorNoExisteVariable,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));
				}else if ((int)t1.getAtributo("Uso") == analizadorL.variable){
				analizadorCI.addError (new Error ( analizadorCI.errorVariablePorColeccion,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));
				}else { t1 = analizadorCI.getTokenMetodo((Token)$1.obj,(Token)$3.obj,analizadorL,controladorTercetos,tablaSimbolo);}
				$$ = new ParserVal( t1 );
				}
		| ID '.' ID { analizadorS.addError (new Error (analizadorS.errorMetodo, "ERROR SINTACTICO", controladorArchivo.getLinea()));}
		;
		
nombre_metodo : first '(' ')' {$$= new ParserVal(new Token(analizadorL.first,"first"));}
			  | last '(' ')'	{$$= new ParserVal(new Token(analizadorL.last,"last"));}
			  | length '(' ')'	{$$= new ParserVal(new Token(analizadorL.length,"length"));}
			  ;

tipo : INT {$$ = new ParserVal(  new Token( analizadorL.tipoInt ) );}
     | DOUBLE {$$ = new ParserVal(  new Token( analizadorL.tipoDouble ) );}
     ;



operador : '<' { String valor = "<";
							  $$ = new ParserVal(  new Token((int) valor.charAt(0),"<" ) ); }
		 | '>' { String valor = ">";
		 					  $$ = new ParserVal(  new Token((int) valor.charAt(0), ">" ) );}
		 | S_MAYOR_IGUAL 	{ $$ = new ParserVal(  new Token(analizadorL.S_MAYOR_IGUAL,">=" ) ); }
		 | S_MENOR_IGUAL 	{ $$ = new ParserVal(  new Token(analizadorL.S_MENOR_IGUAL,"<=" ) ); }
		 | S_IGUAL		{ $$ = new ParserVal(  new Token(analizadorL.S_IGUAL, "==" ));}
 		 | S_DISTINTO		{ $$ = new ParserVal(  new Token(analizadorL.S_DISTINTO, "!="));	}
		 ;


%%

AnalizadorLexico analizadorL;
AnalizadorSintactico analizadorS;
TablaSimbolos tablaSimbolo;
ControladorArchivo controladorArchivo;
ControladorTercetos controladorTercetos;
AnalizadorCodigoIntermedio analizadorCI;

public void setLexico(AnalizadorLexico al) {
       analizadorL = al;
}

public void setSintactico (AnalizadorSintactico as){
	analizadorS = as;
}

public void setTS (TablaSimbolos ts){
	tablaSimbolo = ts;
}

public void setControladorArchivo ( ControladorArchivo ca){
	controladorArchivo = ca;
}

public void setCodigoIntermedio(AnalizadorCodigoIntermedio aci){
	analizadorCI = aci;
}

public void setControladorTercetos ( ControladorTercetos ct){
	controladorTercetos = ct;
}

int yylex()
{
	int val = analizadorL.yylex();
	yylval = new ParserVal((Token)tablaSimbolo.get(analizadorL.getYYlex()));
    return val;
}

public boolean tipoCompatible(Token t1, Token t2){
	String tipo1 = t1.getTipo();
	String tipo2 = t2.getTipo();
	return (tipo1 == tipo2);
}

void yyerror(String s) {
	if(s.contains("under"))
		System.out.println("par:"+s);
}


