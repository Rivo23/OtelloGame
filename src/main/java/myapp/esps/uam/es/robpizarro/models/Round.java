package myapp.esps.uam.es.robpizarro.models;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import es.uam.eps.multij.Tablero;

/**
 * Created by e268930 on 21/02/17.
 */

public class Round {
    private int size;
    private String id;
    private String title;
    private String date;
    private Tablero board;
    private boolean turn;
    private String playerUUID;
    private String playerName;

    public Round(int size, boolean turno) {
        this.size = size;
        id = UUID.randomUUID().toString();
        title = "ROUND " + id.toString().substring(19, 23).toUpperCase();
        date = new Date().toString();
        board = new TableroOthello(size);
        playerUUID = "";
        playerName = "";
        turn = turno;
    }

    public Round(String playerUUID, String rounddate, String roundtitle,
                             int size, String roundid, boolean turno){
        this.size = size;
        this.turn = turno;
        board = new TableroOthello();
        setPlayerUUID(playerUUID);
        setId(roundid);
        setDate(rounddate);
        setTitle(roundtitle);
    }

    public int getSize() { return size;}
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public Tablero getBoard() {
        return board;
    }
    public void setBoard(Tablero board) {
        this.board = board;
    }
    public boolean isTurn() {
        return turn;
    }
    public void setTurn(boolean ok) {turn=ok;}
    public String getPlayerUUID() {
        return playerUUID;
    }
    public void setPlayerUUID(String name) {playerUUID = name;}
    public void setPlayerName(String name) {playerName = name;}
    public String getPlayerName() { return playerName; }
}
