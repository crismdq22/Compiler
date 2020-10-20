package AnalizadorLexico;
public class ControladorArchivo {
	
	static final char saltoLinea = '\n';
	static final char finArchivo = '$';
	//numero de linea actual
	private int linea;
	private char actual;

	//Posicion dentro del buffer o archivo
	private int pos;
	
	//Buffer en el que se va leyendo el codigo
	private StringBuilder buffer;
	
	public ControladorArchivo(StringBuilder archivo){
		this.buffer = archivo;
		pos = 0;
		actual = buffer.charAt(pos);
		linea= 1;
	}
	
	public ControladorArchivo() {
		linea = 1;
		pos = 0;
	}
	
	public void setBuffer(StringBuilder buffer) {
		this.buffer = buffer;
	}
	
	public int getLinea() {
		return linea;
	}
	
	public boolean finArchivo(){
		return (actual == finArchivo);		
	}
	
	public void avanzar(){
		if ( actual == saltoLinea )
			linea++;
		if (pos<buffer.length()){
			pos++;
			actual = buffer.charAt(pos);
		}
	}
	
	public void retroceder(){
		if (pos<buffer.length()){
			pos--;
			actual = buffer.charAt(pos);
		}
		if ( actual == saltoLinea )
			linea--;
	}
	
	public char getActual(){
		return actual;
	}
	
	
	public void add(String str){
		buffer.insert(pos,str);
		actual = buffer.charAt(pos);
	}
	
}
