package src.p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;

public class Parque implements IParque{


	private int contadorPersonasTotales;
	private Hashtable<String, Integer> contadoresPersonasPuerta;
	private long tinicial;
	private long ttotal;
	private long tmedio;

	private static final int MIN=0;	
	private static final int MAX=40;

	public Parque() {	
		contadorPersonasTotales = 0;
		contadoresPersonasPuerta = new Hashtable<String, Integer>();
		tinicial=System.currentTimeMillis();
		tmedio=0;
		ttotal=0;
	}


	@Override
	public synchronized void entrarAlParque(String puerta) throws InterruptedException{
		comprobarAntesDeEntrar();

		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}

		contadorPersonasTotales++;		
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)+1);

		ttotal += (System.currentTimeMillis()-tinicial)/1000;
		tmedio=ttotal/contadorPersonasTotales;

		imprimirInfo(puerta, "Entrada");

		checkInvariante();

	}

	public synchronized int getValor() {
		return contadorPersonasTotales;
	}

	protected synchronized void setValor(int nuevoValor) {
		contadorPersonasTotales=nuevoValor;
		notifyAll();
	}

	@Override
	public synchronized void salirDelParque(String puerta) throws InterruptedException{
		comprobarAntesDeSalir();

		
		contadorPersonasTotales--;
		contadoresPersonasPuerta.put(puerta,contadoresPersonasPuerta.get(puerta)-1);

		ttotal += (System.currentTimeMillis()-tinicial)/1000;
		tmedio=ttotal/contadorPersonasTotales;

		
		imprimirInfo(puerta, "Salida");

		checkInvariante();

	}

	private void imprimirInfo (String puerta, String movimiento){
		System.out.println(movimiento + " por puerta " + puerta);
		System.out.println("--> Personas en el parque " + contadorPersonasTotales + " tiempo medio de estancia: " + tmedio);

		for(String p: contadoresPersonasPuerta.keySet()){
			System.out.println("----> Por puerta " + p + " " + contadoresPersonasPuerta.get(p));
		}
		System.out.println(" ");
	}

	private int sumarContadoresPuerta() {
		int sumaContadoresPuerta = 0;
		Enumeration<Integer> iterPuertas = contadoresPersonasPuerta.elements();
		while (iterPuertas.hasMoreElements()) {
			sumaContadoresPuerta += iterPuertas.nextElement();
		}
		return sumaContadoresPuerta;
	}

	protected void checkInvariante() {
		assert sumarContadoresPuerta() == contadorPersonasTotales : "INV: La suma de contadores de las puertas debe ser igual al valor del contador del parte";
		assert contadorPersonasTotales >= 0 : "El total de personas es negativo";
		
		// TODO 
		// TODO

	}

	protected void comprobarAntesDeEntrar() throws InterruptedException{
		while (contadorPersonasTotales == MAX ) {
			wait();
		}
	}

	protected void comprobarAntesDeSalir() throws InterruptedException{
		while (contadorPersonasTotales == MIN) {
			wait();
		}
	}





}
