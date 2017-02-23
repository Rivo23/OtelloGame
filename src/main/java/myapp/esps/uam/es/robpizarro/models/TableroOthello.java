package myapp.esps.uam.es.robpizarro.models;

import java.util.ArrayList;

import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Tablero;


public class TableroOthello extends Tablero {
	private int[][] tablero;
	public static int jugador1=10; //Jugador de turno 0
	public static int jugador2=11;
	public static int libre=0;
	private final String bordeIzq="|";
	private final String bordeDer=" |";
	private int size;
	private int libres;
	private int fichasRojas;
	private int fichasNegras;
	/* Información relevante:
	Ficha de tu color: negra+this.getTurno()
	Ficha del contrario: roja-this.getTurno()
	*/
	public TableroOthello(){
		estado= Tablero.EN_CURSO;
	}

	public int getTablero(int i, int j){
		return tablero[i][j];
	}

	public TableroOthello(int s){
		super();
		estado= Tablero.EN_CURSO;
		tablero = new int[s][s];
		size=s;
		libres=s*s-4;
		fichasRojas=2;
		fichasNegras=2;
		for (int i=0; i<size; i++){
			for (int j=0; j<size;j++){
				if((i==(size/2)-1 && j==(size/2)-1) || (i==(size/2) && j==(size/2))){tablero[i][j]=jugador1;}
				else if((i==(size/2)-1 && j==(size/2)) || (i==(size/2) && j==(size/2)-1)){tablero[i][j]=jugador2;}
				else{tablero[i][j]=libre;}
			}
		}
	}

	@Override
	protected void mueve(Movimiento m) throws ExcepcionJuego {
		int i = ((MovimientoOthello) m).getY0();
		int j = ((MovimientoOthello) m).getY1();
		
		if(i==MovimientoOthello.PASAR && j==MovimientoOthello.PASAR){
			this.cambiaTurno();
			this.ultimoMovimiento=m;
			if(movimientosValidos().get(0).equals(new MovimientoOthello(MovimientoOthello.PASAR, MovimientoOthello.PASAR))){
				countFichas();
			}
			return;
		}	
		if(((MovimientoOthello) m).isValidated()!=null){
			realizaMovimiento(m, ((MovimientoOthello) m).isValidated());
		}else{
			ArrayList<Integer[]> fuentes = getFuentes(i, j);
			if(fuentes.size()!=0){
				realizaMovimiento(m, fuentes);
			}else{
				throw new ExcepcionJuego("Movimiento no válido.");
			}
		}
			this.cambiaTurno();
			this.ultimoMovimiento=m;
			this.numJugadas++;
			if(libres==0){
				countFichas();
			}
	}

	@Override
	public boolean esValido(Movimiento m) {
		if(((MovimientoOthello) m).isValidated()!=null){
			return true;
		}else{
			int x0, x1;
			x0 = ((MovimientoOthello) m).getY0();
			x1 = ((MovimientoOthello) m).getY1();
			
			if(tablero[x0][x1]!=libre){return false;}
			else{
				int i0 = ((x0-1>0)? x0-1: 0);
				int j0 = ((x1-1>0)? x1-1: 0);
				int i1 = ((x0+1>=size)? size-1: x0+1);
				int j1 = ((x1+1>=size)? size-1: x1+1);
				for(int i=i0; i<=i1; i++){
					for(int j=j0; j<=j1; j++){
						if(i==x0 && j==x1){continue;}
						else{
						if(tablero[i][j]==jugador2-this.getTurno()){
							if(revisarCamino(i-x0,j-x1,x0,x1)){
								return true;
							}
						}
						}
					}
				}
			}
			return false;
		}
	}
	/*
	 * Partiendo de la casilla (x0, x1) comprueba que por la dirección indicada por c0 y c1 es válida.
	 */
	private boolean revisarCamino(int c0, int c1, int x0, int x1){
		int i = x0+2*c0;
		int j = x1+2*c1;
		if(i<0 || j<0 || i>=size || j>=size){
			return false;
		}else{
			if(tablero[i][j]==libre){
				return false;
			}else if(tablero[i][j]==jugador2-this.getTurno()){
				return revisarCamino(c0, c1, x0+c0, x1+c1);
			}else if(tablero[i][j]==jugador1+this.getTurno()){
				return true;
			}else{
				return false;
			}
		}
	}

	@Override
	public ArrayList<Movimiento> movimientosValidos() {
		ArrayList<Movimiento> moves = new ArrayList<Movimiento>();
		
		for(int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				if(tablero[i][j]==libre){
					ArrayList<Integer[]> array = getFuentes(i,j);
					if(!array.isEmpty()){
						Movimiento move=new MovimientoOthello(i,j, array);
						moves.add(move);
					}
				}
			}
		}
		if(moves.size()==0){
			moves.add(new MovimientoOthello(MovimientoOthello.PASAR, MovimientoOthello.PASAR));
		}
		return moves;
	}

	@Override
	public String tableroToString(){
		String estado = (this.size+" ");
		for(int i=0; i<size; i++){
			for(int j=0;j<size; j++){
				estado+=(tablero[i][j]+" ");
			}
		}
		estado+=(fichasRojas+"R");
		estado+=(fichasNegras+"N");
		estado+=(this.getTurno()+"T");
		estado+=(this.libres+" ");
		return estado;
	}

	@Override
	public void stringToTablero(String cadena) throws ExcepcionJuego {
		try{
			String[] piqueo = cadena.split(" RNT");
			size = Integer.parseInt(piqueo[0]);
			int count=1;
			tablero = new int[size][size];
			for(int i=0; i<size; i++){
				for(int j=0; j<size; j++){
					tablero[i][j]=Integer.parseInt(piqueo[count]);
					count++;
				}
			}
			fichasRojas = Integer.parseInt(piqueo[count]);
			count++;
			fichasNegras = Integer.parseInt(piqueo[count]);
			count++;
			this.turno = Integer.parseInt(piqueo[count]);
			count++;
			libres = Integer.parseInt(piqueo[count]);
			
		}catch(Exception e){
			
		}
	}

	@Override
	public String toString(){
		String abscisas=new String(" "+bordeIzq);
		for(int i=0; i<size; i++){
			abscisas+=(i+bordeDer);
		}
		String tab="";
		for(int j=size-1; j>=0; j--){
			tab+=(j+bordeIzq);
			for(int l=0; l<size; l++){
				if(tablero[l][j]==jugador2){
					tab+=("R"+bordeDer);
				}else if(tablero[l][j]==jugador1){
					tab+=("N"+bordeDer);
				}else {
					tab+=("-"+bordeDer);
				}
			}
			tab+="\n";
		}
		return tab+abscisas;
	}
	
	/*
	 * Devuelve un arraylist de las casillas que validan un movimiento determinado por las posiciones x0 y x1.
	 */
	
	private ArrayList<Integer[]> getFuentes(int x0, int x1){
		ArrayList<Integer[]> fuentes = new ArrayList<Integer[]>();
		int i0 = ((x0-1>0)? x0-1: 0);
		int j0 = ((x1-1>0)? x1-1: 0);
		int i1 = ((x0+1>=size)? size-1: x0+1);
		int j1 = ((x1+1>=size)? size-1: x1+1);
		for(int i=i0; i<=i1; i++){
			for(int j=j0; j<=j1; j++){
				if(i==x0 && j==x1){continue;}
				else{
					if(tablero[i][j]==jugador2-this.getTurno()){
						try{
							fuentes.addAll(obtenerFuentes(i-x0,j-x1,x0,x1));
						}catch(NullPointerException e){
							continue;
						}
					}
				}
			}
		}
		return fuentes;
	}
	
	/*
	 * Función auxiliar a la anterior encargada de encontrar las casillas que validan un movimiento.
	 * No se debe llamar desde una función que no sea getFuentes.
	 */
	private ArrayList<Integer[]> obtenerFuentes(int c0, int c1, int x0, int x1){
		ArrayList<Integer[]> fuente = new ArrayList<Integer[]>();
		int i = x0+2*c0;
		int j = x1+2*c1;
		if(i<0 || j<0 || i>=size || j>=size){
			return null;
		}else{
		if(tablero[i][j]==libre){
			return null;
		}else if(tablero[i][j]==jugador2-this.getTurno()){
			try{
				fuente.addAll(obtenerFuentes(c0, c1, x0+c0, x1+c1));
			}catch(NullPointerException e){
				fuente=null;
			}
			return fuente;
		}else if(tablero[i][j]==jugador1+this.getTurno()){
			Integer[] array ={i,j};
			fuente.add(array);
			return fuente;
		}else{
			return null;
		}
		}
	}
	
	/*
	 * Funcion encargada de realizar un movimiento.
	 * Recibe un movimiento y un arraylist de fuentes. (m.isValidated() o getFuentes())
	 */
	private void realizaMovimiento(Movimiento m, ArrayList<Integer[]> fuentes){
		int i = ((MovimientoOthello) m).getY0();
		int j = ((MovimientoOthello) m).getY1();
		
		tablero[i][j]=jugador1+this.getTurno();
		if(this.getTurno()==0){
			this.fichasNegras++;
		}else{
			this.fichasRojas++;
		}
		libres--;
		
		for(Integer[] array: fuentes){
			int i0= ((array[0]-i==0)?0:(array[0]-i)/Math.abs(array[0]-i)); 
			int j0= ((array[1]-j==0)?0:(array[1]-j)/Math.abs(array[1]-j)); 
			int l=0, k=0;
						
			while(true){
				tablero[i+l][j+k]=jugador1+this.getTurno();
				l+=i0;
				k+=j0;
				if(i+l==array[0] && j+k==array[1]){
					break;
				}
			}
		}
	}
	
	/*
	 * Modifica el turno de la partida para dejarlo en el jugador que tiene
	 * mas fichas en el tablero. En caso contrario coloca el estado en tablas. 
	 */
	private void countFichas(){
		estado= Tablero.FINALIZADA;
		int red=0;
		int black=0;
		for(int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				if(tablero[i][j]==jugador1){black++;}
				else if(tablero[i][j]==jugador2){red++;}
			}
		}
		if(red>black){
			this.turno=1;
		}else if(red==black){
			estado=Tablero.TABLAS;
		}else{
			this.turno=0;
		}
	}
}
