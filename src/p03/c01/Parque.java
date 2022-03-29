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
	private static final int MAX=50;

	public Parque() {	

		contadorPersonasTotales = 0;
		contadoresPersonasPuerta = new Hashtable<String, Integer>();
		tinicial=System.currentTimeMillis();
		tmedio=0;
		ttotal=0;
	}


	@Override
	public synchronized void entrarAlParque(String puerta) {
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
		notifyAll();
	}


	@Override
	public synchronized void salirDelParque(String puerta) {
		comprobarAntesDeSalir();

		contadorPersonasTotales--;
		contadoresPersonasPuerta.put(puerta,contadoresPersonasPuerta.get(puerta)-1);

		ttotal += (System.currentTimeMillis()-tinicial)/1000;
		tmedio=ttotal/contadorPersonasTotales;


		imprimirInfo(puerta, "Salida");

		checkInvariante();
		notifyAll();

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
		assert contadorPersonasTotales >= MIN : "INV: El número de personas totales tiene que ser superior a 0";
		assert  contadorPersonasTotales <= MAX : "INV: El número de personas totales tiene que ser inferior a 50";

	}

	protected void comprobarAntesDeEntrar() {
		while (contadorPersonasTotales == MAX ) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void comprobarAntesDeSalir() {
		while (contadorPersonasTotales == MIN) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}



}
