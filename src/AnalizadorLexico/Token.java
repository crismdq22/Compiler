package AnalizadorLexico;
import java.util.HashMap;

public class Token {
	
	private HashMap<String, Object> atributos = new HashMap<String, Object>();
	
	public Token (String string, int nro, Object aux){
		this.AgregarAtributo("Tipo", string);
		this.AgregarAtributo("Nro_Token", nro);
		this.AgregarAtributo("Valor", aux);
	}
	
	public Token(int aux, String valor) {
		this.AgregarAtributo("Nro_Token", aux);
		this.AgregarAtributo("Valor", valor);
		this.AgregarAtributo("Tipo", calcularTipo(aux));
	}
	
	public void AgregarAtributo (String clave,Object o){
		this.atributos.put(clave, o);
	}
	
	public Token(String numeroTercetoString) {
		// Usado para los tercetos
		this.AgregarAtributo("Valor", numeroTercetoString);
	}
	
	public String getValor() {
		Object o =this.getAtributo("Valor");
		String s=String.valueOf(o);
		return s;
	}
	
	public String getAssembler() {
		StringBuilder assembler = new StringBuilder();
		if (!this.existeAtributo("Nro_Token")) {
			if (this.existeAtributo("Direccion")){
				return assembler.append("[@aux"+this.getValor()+"]").toString();
			}
			assembler.append("@aux");
		}
		else if((int)this.getAtributo("Nro_Token") == AnalizadorLexico.ID)
			assembler.append("_");
		else if(this.existeAtributo("AuxDouble")) {
			assembler.append("@"+this.getAtributo("AuxDouble"));
			return assembler.toString();
		}
		assembler.append(this.getValor());
		return assembler.toString();
	}
	
	public Object getAtributo (String clave){
		return this.atributos.get(clave);
	}
	
	public boolean existeAtributo(String clave) {
		return this.atributos.containsKey(clave);
	}
	
	public String getTipo() {
		if (this.existeAtributo("Tipo_Uso")) {
			return String.valueOf(this.getAtributo("Tipo_Uso"));
		}
		return (String)this.getAtributo("Tipo");
	}
 	
	public String toString() {
		String imprimir = " ---Tipo: " + this.getAtributo("Tipo") + " ---" + "Valor: " + this.getAtributo("Valor") + " ---" + "Nro. Token: " + this.getAtributo("Nro_Token");
		return imprimir;
		
	}
	
	private String calcularTipo(int aux) {		
		if ( (aux >=AnalizadorLexico.IF) && (aux<=AnalizadorLexico.ARREGLO) )
			return "Palabra reservada 			";
		else {	
			switch (aux) {
			case AnalizadorLexico.ID:
				return "Identificador				";
			case AnalizadorLexico.CTEDOUBLE:
				return "Constante Double			";
			case AnalizadorLexico.CTEENTERA:
				return "Constante Entera			";
			case AnalizadorLexico.MULTI_LINEA:
				return "Cadena Multilínea			";
			default:
				return "Simbolo				" ;
			}
		}
	}

	public String imprimirTerceto() {
		// TODO Auto-generated method stub
		char c=this.getValor().charAt(0);
		if (Character.isDigit(c)) {
			if (!this.existeAtributo("Nro_Token"))
				return "[" + this.getValor() + "]";
			else 
				return "" + this.getValor() + "";
		}else
			return this.getValor();
	}


}
