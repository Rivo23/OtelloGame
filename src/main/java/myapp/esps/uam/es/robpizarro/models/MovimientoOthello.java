package myapp.esps.uam.es.robpizarro.models;

import java.util.ArrayList;

import es.uam.eps.multij.Movimiento;

public class MovimientoOthello extends Movimiento {
	private int y0;
	private int y1;
	public static int PASAR=99;
	private final ArrayList<Integer[]> isValidated;

	public MovimientoOthello(int y2, int y3) {
		// TODO Auto-generated constructor stub
		super();
		y0=y2;
		y1=y3;
		isValidated=null;
	}
	
	public MovimientoOthello(int y2, int y3, ArrayList<Integer[]> validated){
		super();
		y0=y2;
		y1=y3;
		isValidated=validated;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return y0+" "+y1;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof MovimientoOthello))
			return false;
		else{
			return this.toString().equals(o.toString());
		}
	}
	

	public int getY0(){
		return y0;
	}
	
	public int getY1(){
		return y1;
	}

	public ArrayList<Integer[]> isValidated(){
		return isValidated;
	}
}
