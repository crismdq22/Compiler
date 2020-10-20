package CodigoIntermedio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import AnalizadorLexico.TablaSimbolos;

public class ConvertidorAssembler {

	public static final String labelDivCero = "LabelDivCero";
	public static final String labelOverflow = "LabelOverflow";
	public static final String labelFueraRango = "LabelIndiceFueraDeRango";
	private String archivo;
	
	static ControladorTercetos controladorTercetos;
	static TablaSimbolos tablaSimb;
	static File arch;
	public ConvertidorAssembler( ControladorTercetos controladorTercetos ) throws IOException {
		ConvertidorAssembler.controladorTercetos = controladorTercetos;
		tablaSimb = null;
	}
	
	public void setTablaSimb(TablaSimbolos tablaSimb) {
		ConvertidorAssembler.tablaSimb = tablaSimb;
	}
	
	public void setArchivo(String archivo) {
		this.archivo=archivo;
	}
	
	public void generarAssembler () throws IOException{
		arch = new File(archivo.substring(0,archivo.length()-4)+".asm");
		writeFile1();

		String comc = "cmd /c .\\masm32\\bin\\ml /c /Zd /coff"+archivo.substring(0,archivo.length()-4)+ ".asm";
		Process ptasm32 = Runtime.getRuntime().exec(comc);
		ptasm32.getInputStream();

		String coml = "cmd /c \\masm32\\bin\\Link /SUBSYSTEM:CONSOLE"+archivo.substring(0,archivo.length()-4)+".obj ";
		Process ptlink32 = Runtime.getRuntime().exec(coml);
		ptlink32.getInputStream();
	}
	
	public static void writeFile1() throws IOException {
		FileOutputStream fos = new FileOutputStream(arch);
	 
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		
		bw.write(".386" + System.lineSeparator() 
				+ ".model flat, stdcall" + System.lineSeparator()
				+ "option casemap :none" + System.lineSeparator()
				+ "include \\masm32\\include\\windows.inc" + System.lineSeparator()
				+ "include \\masm32\\include\\kernel32.inc" + System.lineSeparator()
				+ "include \\masm32\\include\\user32.inc" + System.lineSeparator()
				+ "includelib \\masm32\\lib\\kernel32.lib" + System.lineSeparator()
				+ "includelib \\masm32\\lib\\user32.lib" + System.lineSeparator()
				+ "include \\masm32\\include\\masm32rt.inc"+ System.lineSeparator() 
				+ "dll_dllcrt0 PROTO C"+ System.lineSeparator() 
				+ "printf PROTO C :VARARG"+ System.lineSeparator()
				+".data" + System.lineSeparator());	
		String data = tablaSimb.getAssembler() ;
		data = data + "@param DWORD ?"+System.lineSeparator();
		data = data + "@retI DW ?"+System.lineSeparator();
		data = data + "@retD QWORD ?"+System.lineSeparator();
		data = data + "@printI DWORD ?"+System.lineSeparator();
		data = data + "@printD QWORD ?"+System.lineSeparator();
		data = data + "@maxD QWORD 1.7976931348623157D308"+System.lineSeparator();
		data = data + "@minD QWORD 2.2250738585072014D-308"+System.lineSeparator();
		data = data + controladorTercetos.getPrintsAssembler();
		data = data + "DividirCero db \"Error al dividir por cero!\", 0" + System.lineSeparator();
		data = data + "errorOverflow db \"Se produce overflow en una multiplicacion\", 0" + System.lineSeparator();
		data = data + "FueraRango db \"Se intento acceder a una posicion de coleccion fuera de rango!\", 0" + System.lineSeparator();
		data = data + "estado DW ? "+System.lineSeparator();
		data = data + System.lineSeparator() + ".code"+ System.lineSeparator();
		
		bw.write( data );
		
		//Inicia el codigo
		String code = "start:" + System.lineSeparator() + (String) controladorTercetos.generarAssembler(); 

		code = code + "invoke ExitProcess, 0" + System.lineSeparator();

		bw.write( code );
		String errores = getErroresRunTime();
		bw.write(errores);
		bw.write( "end start" );

		bw.close();
	}

	private static String getErroresRunTime() {
		StringBuilder errores =new StringBuilder(labelDivCero + ":" + System.lineSeparator());
		errores.append( "invoke MessageBox, NULL, addr DividirCero, addr DividirCero, MB_OK" + System.lineSeparator());
		errores.append("invoke ExitProcess, 0" + System.lineSeparator());
		errores.append(labelOverflow + ":" + System.lineSeparator());
		errores.append("invoke MessageBox, NULL, addr errorOverflow, addr errorOverflow, MB_OK" + System.lineSeparator());
		errores.append("invoke ExitProcess, 0" + System.lineSeparator());
		errores.append(labelFueraRango + ":" + System.lineSeparator());
		errores.append("invoke MessageBox, NULL, addr FueraRango, addr FueraRango, MB_OK" + System.lineSeparator());
		errores.append( "invoke ExitProcess, 0" + System.lineSeparator());
		errores.append("@FUNCTION_FirstI:"+System.lineSeparator()
				+"MOV EAX, @param"+System.lineSeparator()
				+"MOV CX, [EAX+2]"+System.lineSeparator()
				+"MOV @retI, CX"+System.lineSeparator()
				+"RET"+System.lineSeparator());
		errores.append("@FUNCTION_FirstD:"+System.lineSeparator()
				+"MOV EAX, @param"+System.lineSeparator()
				+"FLD QWORD PTR [EAX+8]"+System.lineSeparator()
				+"FSTP @retD"+System.lineSeparator()
				+"RET"+System.lineSeparator());
		errores.append( "@FUNCTION_LastI:"+System.lineSeparator()
				+"CALL @FUNCTION_LengthI"+System.lineSeparator()
				+"MOV AX, @retI"+System.lineSeparator()
				+"MOVZX EAX, AX"+System.lineSeparator()
				+"IMUL EAX, 2"+System.lineSeparator()
				+"ADD EAX, @param"+System.lineSeparator()
				+"MOV CX, [EAX]"+System.lineSeparator()
				+"MOV @retI, CX"+System.lineSeparator()
				+"RET"+System.lineSeparator());
		errores.append("@FUNCTION_LastD:"+System.lineSeparator()
				+"CALL @FUNCTION_LengthD"+System.lineSeparator()
				+"MOV AX, @retI"+System.lineSeparator()
				+"MOVZX EAX, AX"+System.lineSeparator()
				+"IMUL EAX, 8"+System.lineSeparator()
				+"ADD EAX, @param"+System.lineSeparator()
				+"FLD QWORD PTR [EAX]"+System.lineSeparator()
				+"FSTP @retD"+System.lineSeparator()
				+"RET"+System.lineSeparator());
		errores.append("@FUNCTION_LengthI:"+System.lineSeparator()+ "MOV EAX, @param"+System.lineSeparator()
				+"MOV CX, [EAX]"+System.lineSeparator()
				+"MOV @retI, CX"+System.lineSeparator()
				+"RET"+System.lineSeparator());
		errores.append("@FUNCTION_LengthD:"+System.lineSeparator()+ "MOV EAX, @param"+System.lineSeparator()
				+ "FLD QWORD PTR [EAX]"+System.lineSeparator()
				+"FISTP @retI"+System.lineSeparator()
					+"RET"+System.lineSeparator());
		errores.append("@FUNCTION_MulCheck:"+System.lineSeparator());
		errores.append("FTST "+System.lineSeparator());
		errores.append("FSTSW estado "+System.lineSeparator());
		errores.append("MOV AX,estado "+System.lineSeparator());
		errores.append("SAHF "+System.lineSeparator());
		errores.append("JE LabelZero"+System.lineSeparator());
		errores.append("FABS"+System.lineSeparator());
		errores.append("FCOM @maxD"+System.lineSeparator());
		errores.append("FSTSW estado "+System.lineSeparator());
		errores.append("MOV AX,estado "+System.lineSeparator());
		errores.append("SAHF "+System.lineSeparator());
		errores.append("JA "+ ConvertidorAssembler.labelOverflow+ System.lineSeparator());
		errores.append("FCOM @minD"+System.lineSeparator());
		errores.append("FSTSW estado "+System.lineSeparator());
		errores.append("MOV AX,estado "+System.lineSeparator());
		errores.append("SAHF "+System.lineSeparator());
		errores.append("JB "+ ConvertidorAssembler.labelOverflow+ System.lineSeparator());
		errores.append("LabelZero:"+System.lineSeparator());
		errores.append("FCOMPP"+System.lineSeparator());
		errores.append("RET"+System.lineSeparator());
		return errores.toString();
	}

}
