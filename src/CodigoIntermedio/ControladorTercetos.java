package CodigoIntermedio;

import java.util.ArrayList;

import AnalizadorLexico.TablaSimbolos;
import AnalizadorLexico.Token;

public class ControladorTercetos {
	
	public static final String BF = "BF";
	public static final String BI = "BI";
	public static final String CGI = "CGI";
	public static final String CAS = "CAS";
	public static final String CFI = "CFI";
	public static final String CLA = "CLA";
	public static final String CLE = "CLE";
	
	
	private ArrayList<Terceto> tercetos;
	private ArrayList<Integer> pila;
	private ArrayList<Integer> labelPendientes; // por el tema del if
	private ArrayList<TercetoPrint> prints;
	private int cantPrint;
	private int num_terceto_actual = 0;
	private Terceto tercetoAux;
			
	public ControladorTercetos() {
		tercetos = new ArrayList<Terceto>();
		pila = new ArrayList<Integer>();
		labelPendientes = new ArrayList<Integer>();
		prints = new ArrayList<TercetoPrint>();
		cantPrint = 0;
	}
	
	public int getNroPrint() {
		cantPrint = cantPrint + 1;
		return cantPrint;
	}
	
	public String imprimirTercetos() {
		String cadena="Tercetos: "+System.lineSeparator();
		for (Terceto t: tercetos ) {
			if (!(t instanceof TercetoLabel))
			cadena= cadena + t.toString() + System.lineSeparator();
		}
		return cadena;
	}
	
	public void addTerceto(int index, Terceto t){
		tercetos.add(index, t);
	}
	
	public int borrarLabelPendiente() {
		int l = labelPendientes.get( labelPendientes.size()-1 );
		labelPendientes.remove( labelPendientes.size()-1 );
		return l;
	}
	
	public void addTerceto(Terceto t){
		tercetos.add(t);
	}

	
	public void removeTerceto(){
		tercetos.remove(tercetos.size());
		num_terceto_actual--;
	}
	
	public Terceto getTercetoAux(){
		return this.tercetoAux;
	}
	
	public int getProxNumero(){
		return tercetos.size()+1;
	}
	
	public int getCantTercetos(){
		return tercetos.size();
	}

	public void addLabelPendiente(int labelPendiente) {
		this.labelPendientes.add( labelPendiente );
	}
	
	public String numeroTercetoString(){
		return String.valueOf(tercetos.size());
	}
	
	public void apilar(){
		pila.add(new Integer(tercetos.size()-1) );
	}
	
	public void apilarDo() {
		pila.add(new Integer(tercetos.size()+1) );
	}
	
	public void desapilar(){
		int tercetoMod = pila.get(pila.size()-1);
		pila.remove(pila.size()-1);
		Terceto nuevo = tercetos.get(tercetoMod);
		Token add = new Token( String.valueOf(tercetos.size()+1) ) ;
		if (nuevo.getToken(1) == null)
			nuevo.setElemento(1, add);
		else
			nuevo.setElemento(2, add);
		tercetos.set(tercetoMod, nuevo);
	}
	
	public void desapilarDo(Terceto terceto) {
		int i = pila.get(pila.size()-1);
		pila.remove(pila.size()-1);
		Token add = new Token( String.valueOf(i) ) ;
		terceto.setElemento(2, add);
	}
	
	public void desapilarError() {
		pila.remove(pila.size()-1);
	}

	public ArrayList<Terceto> getTercetos() {
		return tercetos;
	}
	
	public Terceto getTerceto (int index) {
		return tercetos.get(index-1);
	}
			
	public String generarAssembler() {
		String assembler = "";
		
		num_terceto_actual = 1; //numero de terceto para colocar el label
		for ( Terceto t: tercetos ){			
			t.setControladorTercetos(this);
			assembler = assembler + t.getAssembler();			
			num_terceto_actual++;
			if ( (!labelPendientes.isEmpty()) && ( num_terceto_actual == labelPendientes.get(labelPendientes.size()-1) ) ){
				assembler = assembler + "Label" + String.valueOf(labelPendientes.get(labelPendientes.size()-1))+ ":" + System.lineSeparator();
				borrarLabelPendiente();
			}
		}
		return assembler;
	}
	
	public int getNumTercetoActual(){
		return num_terceto_actual;
	}
	
	public void addPrint(TercetoPrint t){
		this.prints.add(t);
	}

	public void setTablaSimbolos(TablaSimbolos tablaSimbolos) {
	}
	
	public String getPrintsAssembler(){
		StringBuilder assembler = new StringBuilder();
		for (TercetoPrint t:prints) {
				assembler.append( t.getNroPrint()+" db \""+t.getToken(1).getValor()+"\", 0"+System.lineSeparator());
		}
		return assembler.toString();
	}
}
