package myapp.esps.uam.es.robpizarro.activities;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import es.uam.eps.multij.AccionMover;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.JugadorAleatorio;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.PartidaListener;
import es.uam.eps.multij.Tablero;
import myapp.esps.uam.es.robpizarro.R;
import myapp.esps.uam.es.robpizarro.models.MovimientoOthello;
import myapp.esps.uam.es.robpizarro.models.Round;
import myapp.esps.uam.es.robpizarro.models.RoundRepository;
import myapp.esps.uam.es.robpizarro.models.TableroOthello;

public class RoundFragment extends Fragment implements PartidaListener, View.OnClickListener {
    public static final String ARG_ROUND_ID = "myapp.esps.uam.es.robpizarro.round_id"; //Identificador de la partida
    private final int REL_WRAP = RelativeLayout.LayoutParams.WRAP_CONTENT;
    private final int REL_MATCH = RelativeLayout.LayoutParams.MATCH_PARENT;
    private int size;
    private Partida game;
    private Round round;
    private Tablero board;
    private Callbacks callbacks;
    private int ids[][];

    public RoundFragment() {
    }

    public static RoundFragment newInstance(String roundId) {
        Bundle args = new Bundle();
        args.putString(ARG_ROUND_ID, roundId);
        RoundFragment roundFragment = new RoundFragment();
        roundFragment.setArguments(args);
        return roundFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ROUND_ID)) {
            String roundId = getArguments().getString(ARG_ROUND_ID);
            round = RoundRepository.get(getActivity()).getRound(roundId);
            size = round.getSize();
            ids = new int[size][size];
            int count=1;

            for(int i=0;i<size;i++){
                for(int j=0;j<size;j++){
                    ids[i][j]=count;
                    count++;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        try{
            Movimiento move = new MovimientoOthello(fromViewToI(v),fromViewToJ(v));
            AccionMover action = new AccionMover(game.getJugador(1), move);
            game.realizaAccion(action);
        }catch (ExcepcionJuego e){
            Snackbar.make(v, R.string.invalid_movement, Snackbar.LENGTH_SHORT).show();
        }catch (Exception m){

        }
    }

    private int fromViewToI(View view) {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (view.getId() == ids[i][j])
                    return i;
        return -1;
    }
    private int fromViewToJ(View view) {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (view.getId() == ids[i][j])
                    return j;
        return -1;
    }

    /**
     * Mismo concepto que RoundListFragment, toda actividad que aloje un RoundFragment
     * tendrá que implementar este método de la interfaz.
     */
    public interface Callbacks {
        void onRoundUpdated(Round round);
    }

    @Override
    public void onStart() {
        super.onStart();
        startRound();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (RoundFragment.Callbacks) context;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    void startRound() {
        if(round!=null){
            ArrayList<Jugador> players = new ArrayList<Jugador>();
            JugadorAleatorio randomPlayer = new JugadorAleatorio("Random player");
            LocalPlayer localPlayer = new LocalPlayer();
            players.add(randomPlayer);
            players.add(localPlayer);
            board=round.getBoard();
            game=new Partida(board, players);
            game.addObservador(this);
            localPlayer.setPartida(game);
            localPlayer.setVista(ids);

            if (game.getTablero().getEstado() == Tablero.EN_CURSO)
                game.comenzar();
        }
    }

    private void updateUI() {
        ImageButton button;
        for(int i = 0; i <size; i++)
            for(int j =0; j <size; j++) {
                button = (ImageButton) getView().findViewById(ids[i][j]);
                delay(1);
                button.setImageResource(getDrawable(((TableroOthello)board).getTablero(i, j)));
            }
    }

    @Override
    public void onCambioEnPartida(Evento evento) {
        switch (evento.getTipo()) {
            case Evento.EVENTO_CAMBIO:
                updateUI();
                callbacks.onRoundUpdated(round);
                if(game.getTablero().getTurno()==1) {
                    if (game.getTablero().movimientosValidos().get(0).equals(new MovimientoOthello(MovimientoOthello.PASAR, MovimientoOthello.PASAR))) {
                        AccionMover action = new AccionMover(game.getJugador(1), new MovimientoOthello(MovimientoOthello.PASAR, MovimientoOthello.PASAR));
                        try{
                            evento.getPartida().realizaAccion(action);
                            Snackbar.make(getView(), R.string.pass_movement, Snackbar.LENGTH_SHORT).show();
                        }catch(Exception e) {
                            Evento ev = new Evento(Evento.EVENTO_ERROR, "Ha habido un error", game, evento.getCausa());
                            onCambioEnPartida(ev);
                        }
                    }
                }
                break;
            case Evento.EVENTO_FIN:
                updateUI();
                callbacks.onRoundUpdated(round);
                new AlertDialogFragment().show(getActivity().getFragmentManager(), "ALERT DIALOG");
                break;
            case Evento.EVENTO_ERROR:
                game.continuar();
                break;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = makeBoard();
        TextView roundTitleTextView = (TextView) rootView.findViewById(0);
        roundTitleTextView.setText(round.getTitle());
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private View makeBoard(){
        RelativeLayout.LayoutParams paramsMatch = new RelativeLayout.LayoutParams(REL_MATCH, REL_MATCH);
        RelativeLayout root = new RelativeLayout(getActivity());
        root.setGravity(Gravity.CENTER);
        root.setBackgroundResource(R.color.Blue);
        root.setLayoutParams(paramsMatch);
        createTitleTextView(root, 0);
        int lastB=0, lastId=1, lastR;
        TableroOthello tab = ((TableroOthello) round.getBoard());

        for(int i=size-1;i>=0; i--){
            createButtonBelow(root, lastId, getDrawable(tab.getTablero(0,i)),lastB);
            lastB=lastId; lastR=lastId;
            lastId++;
            for(int j=1; j<size; j++){
                createButtonRightOf(root, lastId, getDrawable(tab.getTablero(j,i)), lastR);
                lastR=lastId++;
            }
        }
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createTitleTextView(RelativeLayout parent, int id){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(REL_WRAP,
                REL_WRAP);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        TextView textView = new TextView(getActivity());
        textView.setId(id);
        textView.setPadding(5,5,5,5);
        textView.setBackgroundResource(R.color.Blue);

        textView.setLayoutParams(params);
        parent.addView(textView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createButtonBelow (RelativeLayout parent, int id, int background, int below){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(REL_WRAP,
                REL_WRAP);
        params.addRule(RelativeLayout.BELOW, below);
        ImageButton button = new ImageButton(getActivity());
        button.setId(id);
        button.setImageResource(background);
        button.setBackgroundResource(R.color.Blue);
        button.setLayoutParams(params);
        button.setOnClickListener(this);
        parent.addView(button);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createButtonRightOf (RelativeLayout parent, int id, int background, int right){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(REL_WRAP,
                REL_WRAP);
        params.addRule(RelativeLayout.RIGHT_OF, right);
        params.addRule(RelativeLayout.ALIGN_BOTTOM, right);
        ImageButton button = new ImageButton(getActivity());
        button.setId(id);
        button.setImageResource(background);
        button.setBackgroundResource(R.color.Blue);
        button.setLayoutParams(params);
        button.setOnClickListener(this);
        parent.addView(button);
    }

    private int getDrawable(int pos){
        if(pos==TableroOthello.jugador1){
            return R.drawable.green_dot_48dp;
        }else if(pos==TableroOthello.jugador2){
            return R.drawable.red_dot_48dp;
        }else{
            return R.drawable.free_dot_48dp;
        }
    }

    private void delay(int seconds){

    }
}
