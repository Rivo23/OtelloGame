package myapp.esps.uam.es.robpizarro.activities;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.uam.eps.multij.Tablero;
import myapp.esps.uam.es.robpizarro.R;
import myapp.esps.uam.es.robpizarro.models.Round;
import myapp.esps.uam.es.robpizarro.models.RoundRepository;
import myapp.esps.uam.es.robpizarro.models.RoundRepositoryFactory;
import myapp.esps.uam.es.robpizarro.models.TableroOthello;


/**
 * Created by e268930 on 21/02/17.
 */

public class RoundListFragment extends Fragment {

    //private static final int SIZE = 3;

    private RecyclerView roundRecyclerView;
    private RoundAdapter roundAdapter;
    private Callbacks callbacks;

    /**
     * Esta interfaz con un	 método	debe ser sobrescrito por toda
     * actividad que aloje RoundListFragment.
     */
    public interface Callbacks {
        void onRoundSelected(Round round);
        void onMenuSelection();
        void onPreferencesSelected();
        void onLogout();
        void onStats();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (Callbacks) context;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    /**
     * Este	método	infla	y	crea	la	vista	del	fragmento	y	la	devuelve	para	que	la	actividad
     la	 incruste	 en	 su	 contenedor
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_round_list, container, false);

        roundRecyclerView = (RecyclerView) view.findViewById(R.id.round_recycler_view);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        roundRecyclerView.setLayoutManager(linearLayoutManager);
        roundRecyclerView.setItemAnimator(new DefaultItemAnimator());

        setHasOptionsMenu(true);

        updateUI();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }


    public void updateUI() {
        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
        RoundRepository.RoundsCallback roundsCallback = new RoundRepository.RoundsCallback() {
            @Override
            public void onResponse(List<Round> rounds) {
                ArrayList<Round> rounds1 = new ArrayList<>(rounds);
                if (roundAdapter == null) {
                    roundAdapter = new RoundAdapter(rounds1);
                    roundRecyclerView.setAdapter(roundAdapter);
                } else {
                    roundAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onError(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }
        };
        String playeruuid = PreferenceActivity.getUUIDPlayer(getActivity());
        repository.getRounds(playeruuid, null, null, roundsCallback);
        repository.close();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_help:
                callbacks.onMenuSelection();
                return true;
            case R.id.menu_item_setting:
                callbacks.onPreferencesSelected();
                return true;
            case R.id.menu_item_logout:
                callbacks.onLogout();
                return true;
            case R.id.menu_stats:
                callbacks.onStats();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class RoundAdapter extends RecyclerView.Adapter<myapp.esps.uam.es.robpizarro.activities.RoundListFragment.RoundAdapter.RoundHolder> {
        private ArrayList<Round> rounds;


        public class RoundHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private final int REL_WRAP = RelativeLayout.LayoutParams.WRAP_CONTENT;
            private final int REL_MATCH = TableLayout.LayoutParams.MATCH_PARENT;
            private TextView idTextView;
            private FrameLayout boardTextView;
            private TextView dateTextView;
            private TextView stateBoadView;
            private RelativeLayout relativeLayout;
            private Round round;

            public RoundHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                relativeLayout = (RelativeLayout) itemView.findViewById(R.id.rel_lay_round_list);
                idTextView = (TextView) itemView.findViewById(R.id.list_item_id);
                dateTextView = (TextView) itemView.findViewById(R.id.list_item_date);
                stateBoadView = (TextView) itemView.findViewById(R.id.list_item_state);
            }
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void bindRound(Round round) {
                this.round = round;
                idTextView.setText(round.getTitle());
                boardTextView=(FrameLayout) makeFrame(round);
                dateTextView.setText(String.valueOf(round.getDate()).substring(0, 19));
                addFrameLayout(relativeLayout, boardTextView);
                relativeLayout.setBackgroundResource(getListFragmentColor(round.getBoard().getEstado()));
                switch (round.getBoard().getEstado()) {
                    case Tablero.FINALIZADA:
                        stateBoadView.setText(R.string.estado_fin);
                        break;
                    case Tablero.EN_CURSO:
                        stateBoadView.setText(R.string.estado_en_curso);
                        break;
                    case Tablero.TABLAS:
                        stateBoadView.setText(R.string.estado_tablas);
                        break;
                    default:
                        break;
                }
            }
            @RequiresApi(api = Build.VERSION_CODES.M)
            private View makeFrame(Round round){
                FrameLayout frameLayout = new FrameLayout(getContext());
                TableLayout.LayoutParams paramsMatch = new TableLayout.LayoutParams(REL_WRAP, REL_WRAP);
                TableLayout root = new TableLayout(getContext());

                root.setLayoutParams(paramsMatch);

                int lastId=1;
                TableroOthello tab = ((TableroOthello) round.getBoard());
                int size = round.getSize();
                TableRow row;
                TableRow.LayoutParams paramsMatch0  = new TableRow.LayoutParams(REL_WRAP, REL_WRAP);
                for(int i=0;i<size; i++){
                    row = new TableRow(getContext());
                    row.setLayoutParams(paramsMatch0);
                    for(int j=0; j<size; j++){
                        createButtonRightOf(row, lastId, getDrawable(tab.getTablero(i,j)));
                        lastId++;
                    }
                    root.addView(row);
                }
                frameLayout.addView(root);
                return frameLayout;
            }
            private int getDrawable(int pos){
                if(pos==TableroOthello.jugador1){
                    return R.drawable.green_dot_8dp;
                }else if(pos==TableroOthello.jugador2){
                    return R.drawable.red_dot_8dp;
                }else{
                    return R.drawable.free_dot_8dp;
                }
            }


            @RequiresApi(api = Build.VERSION_CODES.M)
            private void createButtonRightOf (TableRow parent, int id, int background){
                ImageButton button = new ImageButton(parent.getContext());
                button.setId(id);
                button.setImageResource(background);
                button.setBackgroundResource(getWindowBackgroundColor());
                parent.addView(button);
            }

            private void addFrameLayout(RelativeLayout parent, FrameLayout frameLayout){
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(REL_WRAP,
                        REL_WRAP);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.ALIGN_END);
                frameLayout.setLayoutParams(params);
                parent.addView(frameLayout);
            }

            private int getWindowBackgroundColor(){
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = getContext().getTheme();
                if(theme.resolveAttribute(R.attr.mainBackground, typedValue, true)) return typedValue.resourceId;
                else return R.color.Blue;
            }

            private int getListFragmentColor(int estado){
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = getContext().getTheme();
                switch (estado){
                    case Tablero.FINALIZADA:
                        if(theme.resolveAttribute(R.attr.list_fragment_finalizada_color, typedValue, true)) return typedValue.resourceId;
                        break;
                    case Tablero.EN_CURSO:
                        if(theme.resolveAttribute(R.attr.list_fragment_en_curso_color, typedValue, true)) return typedValue.resourceId;
                        break;
                    default:
                        if(theme.resolveAttribute(R.attr.list_fragment_finalizada_color, typedValue, true)) return typedValue.resourceId;
                        break;
                }
                return R.color.White;
            }

            @Override
            public void onClick(View view) {
                RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
                final Round round1 = this.round;
                RoundRepository.RoundsCallback roundsCallback = new RoundRepository.RoundsCallback()
                {
                    @Override
                    public void onResponse(List<Round> rounds) {
                        callbacks.onRoundSelected(round1);
                    }
                    @Override
                    public void onError(String error) {
                        Snackbar.make(getView(), R.string.error_reading_rounds,
                                Snackbar.LENGTH_LONG).show();
                    }
                };
                String playeruuid = PreferenceActivity.getUUIDPlayer(getActivity());
                repository.getRounds(playeruuid, null, null, roundsCallback);
                repository.close();
            }
        }

        public RoundAdapter(ArrayList<Round> rounds){
            this.rounds = rounds;
        }

        @Override
        public myapp.esps.uam.es.robpizarro.activities.RoundListFragment.RoundAdapter.RoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_round, parent, false);
            return new myapp.esps.uam.es.robpizarro.activities.RoundListFragment.RoundAdapter.RoundHolder(view);
        }
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onBindViewHolder(myapp.esps.uam.es.robpizarro.activities.RoundListFragment.RoundAdapter.RoundHolder holder, int position) {
            Round round = rounds.get(position);

            holder.bindRound(round);
        }

        @Override
        public int getItemCount() {
            return rounds.size();
        }
    }
}
