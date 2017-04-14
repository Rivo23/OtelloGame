package myapp.esps.uam.es.robpizarro.activities;

import android.view.View;

import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.Tablero;

/**
 * Created by e268930 on 21/02/17.
 */

public class LocalPlayer implements Jugador, View.OnClickListener{
    private int SIZE;
    Partida game;
    private int ids[][];

    public LocalPlayer(){

    }

    public void setPartida(Partida part){
        game=part;
    }
    public void setVista(int[][] array) {ids=array; }

    @Override
    public String getNombre() {
        return "Jugador Humano";
    }

    @Override
    public boolean puedeJugar(Tablero tablero) {
        return true;
    }

    @Override
    public void onCambioEnPartida(Evento evento) {
    }

    @Override
    public void onClick(View view) {
    }
}
