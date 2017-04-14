package myapp.esps.uam.es.robpizarro.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Tablero;
import myapp.esps.uam.es.robpizarro.models.Round;
import myapp.esps.uam.es.robpizarro.models.TableroOthello;

import static myapp.esps.uam.es.robpizarro.database.RoundDataBaseSchema.RoundTable;
import static myapp.esps.uam.es.robpizarro.database.RoundDataBaseSchema.UserTable;

/**
 * Created by e268930 on 31/03/17.
 */

public class RoundCursorWrapper extends CursorWrapper{


    private static final String DEBUG = "DEBUG_WRAP";

    public RoundCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Round getRound() {
        String playername = getString(getColumnIndex(UserTable.Cols.PLAYERNAME));
        String size = getString(getColumnIndex(RoundTable.Cols.SIZE));
        String board = getString(getColumnIndex(RoundTable.Cols.BOARD));


        Round round = new Round(Integer.parseInt(size), false);
        try {
            round.getBoard().stringToTablero(board);;
        } catch (ExcepcionJuego e) {
            Log.d(DEBUG, "Error turning string into tablero");
        }
        if(round.getBoard().getTurno()==1){
            round.setTurn(true);
        }
        round.setPlayerUUID(getString(getColumnIndex(UserTable.Cols.PLAYERUUID)));
        round.setPlayerName(playername);
        round.setId(getString(getColumnIndex(RoundTable.Cols.ROUNDUUID)));
        round.setTitle(getString(getColumnIndex(RoundTable.Cols.TITLE)));
        round.setDate(getString(getColumnIndex(RoundTable.Cols.DATE)));
        return round;
    }

}
