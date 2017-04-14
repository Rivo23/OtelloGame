package myapp.esps.uam.es.robpizarro.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

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
import myapp.esps.uam.es.robpizarro.models.StyleManager;
import myapp.esps.uam.es.robpizarro.models.TableroOthello;

public class RoundFragment extends Fragment implements PartidaListener, View.OnClickListener {
    public static final String ARG_ROUND_ID = "round_id"; //Identificador de la partida
    private final int REL_WRAP = RelativeLayout.LayoutParams.WRAP_CONTENT;
    private final int REL_MATCH = RelativeLayout.LayoutParams.MATCH_PARENT;
    private int size;
    private Partida game;
    private Round round;
    private Tablero board;
    private Callbacks callbacks;
    private int ids[][];
    private int idText =91;
    private int turnoHumano;
    public static final String DEBUG = "DEBUG";
    public static final String ARG_FIRST_PLAYER_NAME ="first_player_name";
    public static final String ARG_ROUND_TITLE = "round_title";
    public static final String BOARDSTRING ="board_string";
    public static final String SIZEBOARD ="board_size";
    public static final String ROUNDDATE ="round_date";
    private static final String ROUNDTURN = "roundturn";
    private String roundId;
    private String firstPlayerName;
    private String roundTitle;
    private String boardString;
    private String rounddate;

    public RoundFragment() {
    }

    public static RoundFragment newInstance(String roundId, String firstPlayerName,
                                            String roundTitle, int roundSize, String roundDate, String roundBoard, boolean turno) {
        Bundle args = new Bundle();
        args.putString(ARG_ROUND_ID, roundId);
        args.putString(ARG_FIRST_PLAYER_NAME, firstPlayerName);
        args.putString(ARG_ROUND_TITLE, roundTitle);
        args.putInt(SIZEBOARD, roundSize);
        args.putString(ROUNDDATE, roundDate);
        args.putString(BOARDSTRING, roundBoard);
        args.putBoolean(ROUNDTURN, turno);
        RoundFragment roundFragment = new RoundFragment();
        roundFragment.setArguments(args);
        return roundFragment;
    }

    /**
     * Inicializa los atributos del objeto creado.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ROUND_ID)) {
            roundId = getArguments().getString(ARG_ROUND_ID);
        }
        if (getArguments().containsKey(ARG_FIRST_PLAYER_NAME)) {
            firstPlayerName = getArguments().getString(ARG_FIRST_PLAYER_NAME);
        }
        if (getArguments().containsKey(ARG_ROUND_TITLE)) {
            roundTitle = getArguments().getString(ARG_ROUND_TITLE);
        }
        if(getArguments().containsKey(SIZEBOARD)){
            size = getArguments().getInt(SIZEBOARD);
        }
        if(getArguments().containsKey(ROUNDDATE)){
            rounddate = getArguments().getString(ROUNDDATE);
        }
        boolean turno = getArguments().getBoolean(ROUNDTURN);
        if (getArguments().containsKey(BOARDSTRING))
            boardString = getArguments().getString(BOARDSTRING);

        round = new Round(firstPlayerName,rounddate,roundTitle,size, roundId, turno);
        try {
            round.getBoard().stringToTablero(boardString);
        }catch (Exception e){

        }

        ids = new int[size][size];
        int count=1;
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                ids[i][j]=count;
                count++;
            }
        }
    }

    /**
     * Crea un movimiento con las coordenadas del boton que se pulsó y se intenta realizar en la partida
     * @param v vista de la que se recibio el evento click
     */
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

    /**
     * Obtiene la primera coordenada de una vista segun se haya almacenado en el array de ids del tablero
     * @param view vista del boton al que se quiere encontrar
     * @return el valor de la primera coordenada en el tablero, -1 si no se encontró
     */
    private int fromViewToI(View view) {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (view.getId() == ids[i][j])
                    return i;
        return -1;
    }

    /**
     * Obtiene la segunda coordenada de una vista segun se haya almacenado en el array de ids del tablero
     * @param view vista del boton al que se quiere encontrar
     * @return el valor de la segunda coordenada en el tablero
     */
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

    /**
     * Crea la partida y asigna los jugadores y observadores.
     */
    void startRound() {
        if(round!=null){
            ArrayList<Jugador> players = new ArrayList<Jugador>();
            JugadorAleatorio randomPlayer = new JugadorAleatorio("Random player");
            LocalPlayer localPlayer = new LocalPlayer();

            if(round.isTurn()){
                players.add(localPlayer);
                players.add(randomPlayer);
                turnoHumano=0;
            }else{
                players.add(randomPlayer);
                players.add(localPlayer);
                turnoHumano=1;
            }
            board=round.getBoard();
            game=new Partida(board, players);
            game.addObservador(this);
            localPlayer.setPartida(game);
            localPlayer.setVista(ids);

            if (game.getTablero().getEstado() == Tablero.EN_CURSO) {
                game.comenzar();
            }
        }
    }

    /**
     * Dibuja todos los botones del tablero usando el recurso que le corresponda dado por getDrawable.
     */
    private void updateUI() {
        ImageButton button;
        for(int i = 0; i <size; i++) {
            for (int j = 0; j < size; j++) {
                button = (ImageButton) getView().findViewById(ids[i][j]);
                button.setImageResource(getDrawable(((TableroOthello) board).getTablero(i, j)));
            }
        }
    }

    /**
     * En el evento cambio, actualiza la vista, calcula si el jugador humano debe pasar, y lo ejecuta si es así.
     * En el evento fin, actualiza la vista, lanza un dialogo, mostrando el resultado.
     * En el evento error, continua la partida.
     * @param evento evento lanzado por la partida
     */
    @Override
    public void onCambioEnPartida(Evento evento) {
        switch (evento.getTipo()) {
            case Evento.EVENTO_CAMBIO:
                updateUI();
                callbacks.onRoundUpdated(round);
                if(game.getTablero().getTurno()==turnoHumano) {
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
                removeListener();
                AlertDialogFragment alert = AlertDialogFragment.newInstance(evento.getDescripcion(), AlertDialogFragment.FINALROUND, getResources().getString(R.string.game_over_message));
                alert.show(getActivity().getFragmentManager(), "ALERT DIALOG");
                game.removeObservador(this);
                break;
            case Evento.EVENTO_ERROR:
                game.continuar();
                break;
        }
    }

    private void removeListener(){
        ImageButton button;
        for(int i = 0; i <size; i++) {
            for (int j = 0; j < size; j++) {
                button = (ImageButton) getView().findViewById(ids[i][j]);
                button.setClickable(false);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = makeBoard();
        TextView roundTitleTextView = (TextView) rootView.findViewById(idText);
        roundTitleTextView.setText(round.getTitle());
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private View makeBoard(){
        RelativeLayout.LayoutParams paramsMatch = new RelativeLayout.LayoutParams(REL_MATCH, REL_MATCH);
        RelativeLayout root = new RelativeLayout(getActivity());
        root.setGravity(Gravity.CENTER);

        root.setBackgroundResource(StyleManager.getwindowBackColor(getContext()));
        root.setLayoutParams(paramsMatch);

        createTitleTextView(root, idText);
        int lastB=idText;
        TableroOthello tab = ((TableroOthello) round.getBoard());

        for(int i=0;i<size; i++){
            createButtonsBelow(root, i+2, size*i+size/2+1, getDrawable(tab.getTablero(i,size/2-1)), getDrawable(tab.getTablero(i,size/2)),lastB);
            lastB=(i+2)*idText;
            createButtonRightOf(root, i*size+size/2+2, getDrawable(tab.getTablero(i,size/2+1)), lastB);
            for(int j=size/2+2; j<size; j++){
                createButtonRightOf(root, i*size+j+1, getDrawable(tab.getTablero(i,j)), i*size+j);
            }
            createButtonLeftOf(root, i*size+size/2-1, getDrawable(tab.getTablero(i,size/2-2)), lastB);
            for(int k=size/2-3; k>=0;k--){
                createButtonLeftOf(root, i*size+k+1, getDrawable(tab.getTablero(i,k)), i*size+k+2);
            }
        }
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.addView(root, new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT));
        scrollView.setFillViewport(true);
        return scrollView;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createTitleTextView(RelativeLayout parent, int id){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(REL_WRAP,
                REL_WRAP);

        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.setMargins(12,12,12,12);
        TextView textView = new TextView(getActivity());
        textView.setId(id);
        textView.setPadding(12,12,12,12);
        textView.setBackgroundResource(StyleManager.getwindowBackColor(getContext()));
        textView.setTextSize(32);
        textView.setLayoutParams(params);
        parent.addView(textView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createButtonsBelow (RelativeLayout parent, int idrelative, int id, int backlf, int backrg, int below){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(REL_WRAP, REL_WRAP);
        RelativeLayout root = new RelativeLayout(getActivity());
        params.addRule(RelativeLayout.BELOW, below);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        root.setBackgroundResource(StyleManager.getwindowBackColor(getContext()));
        root.setLayoutParams(params);
        root.setId(idrelative*idText);

        RelativeLayout.LayoutParams paramslayout = new RelativeLayout.LayoutParams(REL_WRAP, REL_WRAP);
        paramslayout.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        ImageButton button = new ImageButton(getActivity());
        button.setId(id-1);
        button.setImageResource(backlf);
        button.setAdjustViewBounds(true);
        button.setBackgroundResource(StyleManager.getwindowBackColor(getContext()));
        button.setLayoutParams(paramslayout);
        button.setOnClickListener(this);
        root.addView(button);

        RelativeLayout.LayoutParams paramslayout1 = new RelativeLayout.LayoutParams(REL_WRAP, REL_WRAP);
        paramslayout1.addRule(RelativeLayout.RIGHT_OF, id-1);
        paramslayout1.addRule(RelativeLayout.ALIGN_BOTTOM);
        ImageButton button2 = new ImageButton(getActivity());
        button2.setId(id);
        button2.setImageResource(backrg);
        button2.setAdjustViewBounds(true);
        button2.setBackgroundResource(StyleManager.getwindowBackColor(getContext()));
        button2.setLayoutParams(paramslayout1);
        button2.setOnClickListener(this);
        root.addView(button2);

        parent.addView(root);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createButtonRightOf (RelativeLayout parent, int id, int background, int right){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(REL_WRAP, REL_WRAP);
        params.addRule(RelativeLayout.RIGHT_OF, right);
        params.addRule(RelativeLayout.ALIGN_BOTTOM, right);
        ImageButton button = new ImageButton(getActivity());
        button.setId(id);

        button.setImageResource(background);
        button.setAdjustViewBounds(true);
        button.setBackgroundResource(StyleManager.getwindowBackColor(getContext()));
        button.setLayoutParams(params);
        button.setOnClickListener(this);
        parent.addView(button);

    }

    private void createButtonLeftOf (RelativeLayout parent, int id, int background, int left){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(REL_WRAP, REL_WRAP);
        params.addRule(RelativeLayout.LEFT_OF, left);
        params.addRule(RelativeLayout.ALIGN_BOTTOM, left);
        ImageButton button = new ImageButton(getActivity());
        button.setId(id);

        button.setImageResource(background);
        button.setAdjustViewBounds(true);
        button.setBackgroundResource(StyleManager.getwindowBackColor(getContext()));
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

}
