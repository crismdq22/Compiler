package AnalizadorLexico;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import AnalizadorSintactico.AnalizadorSintactico;
import AnalizadorSintactico.Parser;
import CodigoIntermedio.AnalizadorCodigoIntermedio;
import CodigoIntermedio.ControladorTercetos;
import CodigoIntermedio.ConvertidorAssembler;

public class Main {
	private static BufferedReader codigo;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 if(0<args.length) {
			 StringBuilder codigo = null;
			 File file = new File(args[0]);
			 InputStream is = new ByteArrayInputStream(file.getAbsolutePath().getBytes());
			 BufferedReader br = new BufferedReader(new InputStreamReader(is));
			 codigo= new StringBuilder( getCodigo( br ) );
			 ControladorArchivo archivo =new ControladorArchivo( codigo );
			 TablaSimbolos ts = new TablaSimbolos();
			 AnalizadorLexico analizadorLexico = new AnalizadorLexico(archivo, ts);
			 AnalizadorSintactico analizadorSintactico = new AnalizadorSintactico( );
			 ControladorTercetos controladorTercetos = new ControladorTercetos();
			 AnalizadorCodigoIntermedio analizadorCI = new AnalizadorCodigoIntermedio();
			 Parser parser;
			 ConvertidorAssembler convertidorAssembler = null;
			try {
				convertidorAssembler = new ConvertidorAssembler(controladorTercetos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    convertidorAssembler.setTablaSimb(ts);	
		    convertidorAssembler.setArchivo(args[0]);
		     parser = new Parser();
		     parser.setLexico(analizadorLexico);
		     parser.setTS(ts);
		     parser.setSintactico(analizadorSintactico);
		     parser.setControladorArchivo(archivo);
		     parser.setCodigoIntermedio(analizadorCI);
		     parser.setControladorTercetos(controladorTercetos);
		     analizadorSintactico.setParser(parser);
		     parser.run();	
		     System.out.println(analizadorSintactico.getErroresSint());
		     System.out.println(analizadorSintactico.getEstructuras());
		     System.out.println(analizadorCI.getErroresCI());
		     if ( analizadorCI.hayErrores() || analizadorLexico.hayErrores() || analizadorSintactico.hayErrores() )
		        	System.out.println( "No se genera codigo intermedio por errores en el codigo" );
		        else{
			        System.out.println( controladorTercetos.imprimirTercetos() );
			        if (convertidorAssembler!=null)
						try {
							convertidorAssembler.generarAssembler();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		        }
			 analizadorLexico.mostrarErrorLex();
			 analizadorLexico.imprimirTablaSimbolos();
			 analizadorLexico.imprimirTokens();
		 }
		 else
			 System.out.println("Se debe pasar como argumento el filepath:");
		 
	}
		 
	private static StringBuilder getCodigo(BufferedReader br) {
		StringBuilder buffer = new StringBuilder();
		try{			
			codigo = new BufferedReader( new FileReader( br.readLine() ) );
			String readLine;
			while ((readLine = codigo.readLine())!= null) {
				buffer.append(readLine+"\n");
			}
			buffer.append("$");
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return buffer;
	}
	
	


}
