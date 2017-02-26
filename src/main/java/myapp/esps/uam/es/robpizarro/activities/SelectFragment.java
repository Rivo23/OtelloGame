package myapp.esps.uam.es.robpizarro.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

/**
 * Created by localuser01 on 24/02/17.
 */

public class SelectFragment extends Fragment implements View.OnClickListener {
    //private Callbacks callbacks;
    private final int REL_WRAP = RelativeLayout.LayoutParams.WRAP_CONTENT;
    private final int REL_MATCH = RelativeLayout.LayoutParams.MATCH_PARENT;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case 1:
                startNewSelectedRound(4);
                break;
            case 2:
                startNewSelectedRound(6);
                break;
            case 3:
                startNewSelectedRound(8);
                break;
            default:
                break;
        }
    }

    private void startNewSelectedRound(int size){
        Round round = new Round(size);
        RoundRepository.get(getActivity()).addRound(round);
        if (getActivity() instanceof MainActivity) {
            RoundFragment roundFragment = RoundFragment.newInstance(round.getId());
            ((MainActivity)getActivity()).changeFragment(roundFragment);
        }else{
            Context context = getContext();
            Intent intent = MainActivity.newIntent(context, round.getId());
            context.startActivity(intent);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //callbacks = (SelectFragment.Callbacks) context;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        //callbacks = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = makeBoard();
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private View makeBoard() {
        RelativeLayout.LayoutParams paramsMatch = new RelativeLayout.LayoutParams(REL_MATCH, REL_MATCH);
        RelativeLayout root = new RelativeLayout(getActivity());
        root.setGravity(Gravity.CENTER);
        root.setBackgroundResource(R.color.Blue);
        root.setLayoutParams(paramsMatch);
        createTitleTextView(root, 0);

        createButtonBelow(root, 1, R.drawable.tab_4_48dp, 0);
        createButtonBelow(root, 2, R.drawable.tab_6_48dp, 1);
        createButtonBelow(root, 3, R.drawable.tab_8_48dp, 2);

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createTitleTextView(RelativeLayout parent, int id){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(REL_WRAP,
                REL_WRAP);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.title_select);
        textView.setId(id);
        textView.setPadding(5,5,5,5);
        textView.setBackgroundResource(R.color.Blue);

        textView.setLayoutParams(params);
        parent.addView(textView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createButtonBelow (RelativeLayout parent, int id, int background, int below){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(REL_MATCH,
                REL_WRAP);
        params.addRule(RelativeLayout.BELOW, below);
        ImageButton button = new ImageButton(getActivity());
        button.setId(id);
        button.setImageResource(background);
        button.setBackgroundResource(R.color.White);
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
        button.setBackgroundResource(R.color.White);
        button.setLayoutParams(params);
        button.setOnClickListener(this);
        parent.addView(button);
    }
}
