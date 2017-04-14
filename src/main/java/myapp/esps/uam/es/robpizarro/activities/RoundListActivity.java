package myapp.esps.uam.es.robpizarro.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import myapp.esps.uam.es.robpizarro.R;
import myapp.esps.uam.es.robpizarro.models.MusicManager;
import myapp.esps.uam.es.robpizarro.models.Round;
import myapp.esps.uam.es.robpizarro.models.RoundRepository;
import myapp.esps.uam.es.robpizarro.models.RoundRepositoryFactory;
import myapp.esps.uam.es.robpizarro.models.TableroOthello;

public class RoundListActivity extends BaseActivity implements RoundListFragment.Callbacks, RoundFragment.Callbacks,
        SelectFragment.Callbacks, HelpInGameFragment.Callbacks {
    private View.OnClickListener listener4,listener6,listener8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masterdetail);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (!(fragment instanceof RoundListFragment)) {
            fragment = new RoundListFragment();
            fm.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
        continueMusic=true;
        listener4=new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                final View view = v;
                boolean turnoo=PreferenceActivity.getTurno(v.getContext());
                Round round = new Round(4, turnoo);
                RoundRepository repository = RoundRepositoryFactory.createRepository(v.getContext());
                RoundRepository.BooleanCallback callbacks = new RoundRepository.BooleanCallback(){
                    @Override
                    public void onResponse(boolean ok){
                        if(!ok){
                            Snackbar.make(view, R.string.error_new_game,
                                    Snackbar.LENGTH_LONG).show();
                        }
                    }
                };
                String playeruuid = PreferenceActivity.getUUIDPlayer(v.getContext());
                round.setPlayerUUID(playeruuid);
                repository.addRound(round, callbacks);
                repository.close();
                onNewRoundCreated(round);
                return;
            }
        };
        com.getbase.floatingactionbutton.FloatingActionButton actionButton = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.new_4_round);
        actionButton.setOnClickListener(listener4);
        listener6=new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                final View view = v;
                boolean turnoo=PreferenceActivity.getTurno(v.getContext());
                Round round = new Round(6, turnoo);
                RoundRepository repository = RoundRepositoryFactory.createRepository(v.getContext());
                RoundRepository.BooleanCallback callbacks = new RoundRepository.BooleanCallback(){
                    @Override
                    public void onResponse(boolean ok){
                        if(!ok){
                            Snackbar.make(view, R.string.error_new_game,
                                    Snackbar.LENGTH_LONG).show();
                        }
                    }
                };
                String playeruuid = PreferenceActivity.getUUIDPlayer(v.getContext());
                round.setPlayerUUID(playeruuid);
                repository.addRound(round, callbacks);
                repository.close();
                onNewRoundCreated(round);
                return;
            }
        };
        com.getbase.floatingactionbutton.FloatingActionButton actionButton1 = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.new_6_round);
        actionButton1.setOnClickListener(listener6);
        listener8=new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                final View view = v;
                boolean turnoo=PreferenceActivity.getTurno(v.getContext());
                Round round = new Round(8, turnoo);
                RoundRepository repository = RoundRepositoryFactory.createRepository(v.getContext());
                RoundRepository.BooleanCallback callbacks = new RoundRepository.BooleanCallback(){
                    @Override
                    public void onResponse(boolean ok){
                        if(!ok){
                            Snackbar.make(view, R.string.error_new_game,
                                    Snackbar.LENGTH_LONG).show();
                        }
                    }
                };
                String playeruuid = PreferenceActivity.getUUIDPlayer(v.getContext());
                round.setPlayerUUID(playeruuid);
                repository.addRound(round, callbacks);
                repository.close();
                onNewRoundCreated(round);
                return;
            }
        };
        com.getbase.floatingactionbutton.FloatingActionButton actionButton2 = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.new_8_round);
        actionButton2.setOnClickListener(listener8);
    }



    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent){
        if(keyEvent.getKeyCode()==KeyEvent.KEYCODE_BACK){
            continueMusic=true;
        }
        return continueMusic;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!continueMusic) {
            MusicManager.pause();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        continueMusic = false;
        if (PreferenceActivity.getMusicON(this)) {
            MusicManager.start(this, MusicManager.MUSIC_MENU);
        }
    }

    /**
     * Cambia con un efecto Fade, el fragmento que se muestra en fragment_container.
     * @param frg Fragment que será mostrado en fragment_container.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void changeFragmentContainer(Fragment frg){
        TransitionManager.beginDelayedTransition(((ViewGroup)findViewById(R.id.fragment_container)), new Fade());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, frg)
                .commit();
    }


    /*METODOS DE CALLBACKS*/

    /**
     * Intenta mostar el tablero dentro de esta actividad en detail_fragment_container, si no es posible lanza una
     * MainActivity que albergará el tablero.
     * @param round Partida que se quiere mostrar
     */
    @Override
    public void onRoundSelected(Round round) {
        String playeruuid = PreferenceActivity.getUUIDPlayer(this);
        String tableroOthello = round.getBoard().tableroToString();
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = MainActivity.newIntent(this, round.getId(), playeruuid, round.getTitle(), round.getSize(),
                    round.getDate(), tableroOthello, round.isTurn());
            startActivity(intent);
        } else {
            RoundFragment roundFragment = RoundFragment.newInstance(round.getId(), playeruuid, round.getTitle(),
                    round.getSize(), round.getDate(), tableroOthello, round.isTurn());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, roundFragment)
                    .commit();
        }
    }

    /**
     * Intenta mostrar el fragmento de seleccion de la partida en detail_fragment_container, si no es posible lo
     * mostrara en fragment_container
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onMenuSelection() {
        HelpInGameFragment frg = new HelpInGameFragment();
        if (findViewById(R.id.detail_fragment_container) == null) {
            changeFragmentContainer(frg);
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, frg)
                    .commit();
        }
    }

    /**
     * Actualiza la vista de roundlistfragment, segun los cambios en la partida
     * @param round Partida que se quiere mostrar
     */
    @Override
    public void onRoundUpdated(Round round) {
        RoundRepository repository = RoundRepositoryFactory.createRepository(this);
        RoundRepository.BooleanCallback booleanCallback = new RoundRepository.BooleanCallback(){
            @Override
            public void onResponse(boolean ok) {
                if(!ok){
                    Toast.makeText(getApplicationContext(), R.string.error_updating,
                            Toast.LENGTH_LONG).show();
                }
            }
        };
        repository.updateRound(round, booleanCallback);
        repository.close();

        FragmentManager fragmentManager = getSupportFragmentManager();
        RoundListFragment roundListFragment = (RoundListFragment)fragmentManager.findFragmentById(R.id.fragment_container);
        roundListFragment.updateUI();
    }

    /**
     * Cuando se ha creado una nueva partida, intenta mostrar el tablero en detail_fragment_container.
     * Si no es posible lanzará una nueva MainActivity
     * @param round Partida que se quiere mostrar
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNewRoundCreated(Round round) {
        String playeruuid = PreferenceActivity.getUUIDPlayer(this);
        String tableroOthello = round.getBoard().tableroToString();
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = MainActivity.newIntent(this, round.getId(), playeruuid, round.getTitle(), round.getSize(),
                    round.getDate(), tableroOthello, round.isTurn());
            startActivity(intent);
            changeFragmentContainer(new RoundListFragment());
        } else {
            RoundFragment roundFragment = RoundFragment.newInstance(round.getId(), playeruuid, round.getTitle(),
                    round.getSize(), round.getDate(), tableroOthello, round.isTurn());
            changeFragmentContainer(new RoundListFragment());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, roundFragment)
                    .commit();
        }
    }


    @Override
    public void onPreferencesSelected() {
        Intent intent = new Intent(this, PreferenceActivity.class);
        startActivity(intent);
    }

    /**
     * Actualiza la vista de la actividad si se cancela la accion de crear nueva partida
     * pulsando 'Cancelar'
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCancelButton() {
        if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof RoundListFragment)) {
            changeFragmentContainer(new RoundListFragment());
        }
    }

    @Override
    public void onLogout(){
        PreferenceActivity.deletePlayerNamePreference(this);
        PreferenceActivity.deletePlayerUUIDPreference(this);
        PreferenceActivity.deletePasswordPreference(this);
        Intent intent=new Intent(RoundListActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onStats() {
        Intent intent=new Intent(this, ScoresActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReturn() {
        if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof RoundListFragment)) {
            changeFragmentContainer(new RoundListFragment());
        }
    }
}