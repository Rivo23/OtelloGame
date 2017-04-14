package myapp.esps.uam.es.robpizarro.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.Toast;

import myapp.esps.uam.es.robpizarro.R;
import myapp.esps.uam.es.robpizarro.models.MusicManager;
import myapp.esps.uam.es.robpizarro.models.Round;
import myapp.esps.uam.es.robpizarro.models.RoundRepository;
import myapp.esps.uam.es.robpizarro.models.RoundRepositoryFactory;


/**
 * Created by localuser01 on 20/02/17.
 */

public class MainActivity extends BaseActivity  implements RoundFragment.Callbacks, SelectFragment.Callbacks {

    private static final String EXTRA_ROUND_ID = "myapp.esps.uam.es.robpizarro.round_id"; //Identificador de la partida.
    private static final String EXTRA_ROUND_TITLE = "myapp.esps.uam.es.robpizarro.round_title";
    private static final String EXTRA_ROUND_SIZE = "myapp.esps.uam.es.robpizarro.round_size";
    private static final String EXTRA_ROUND_DATE = "myapp.esps.uam.es.robpizarro.round_date";
    private static final String EXTRA_ROUND_BOARD = "myapp.esps.uam.es.robpizarro.round_board";
    private static final String EXTRA_ROUND_TURN = "myapp.esps.uam.es.robpizarro.round_turn";
    private static final String EXTRA_ROUND_PNAME = "myapp.esps.uam.es.robpizarro.round_p_name";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_layout);

        continueMusic=true;
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            String roundId = getIntent().getStringExtra(EXTRA_ROUND_ID);
            String playeruuid = getIntent().getStringExtra(EXTRA_ROUND_PNAME);
            String title = getIntent().getStringExtra(EXTRA_ROUND_TITLE);
            int size = getIntent().getIntExtra(EXTRA_ROUND_SIZE, 8);
            String date = getIntent().getStringExtra(EXTRA_ROUND_DATE);
            String stringTablero = getIntent().getStringExtra(EXTRA_ROUND_BOARD);
            boolean isturn = getIntent().getBooleanExtra(EXTRA_ROUND_TURN, false);
            RoundFragment roundFragment = RoundFragment.newInstance(roundId, playeruuid, title,
                    size, date, stringTablero, isturn);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, roundFragment)
                    .commit();
        }
    }

    public static Intent newIntent(Context packageContext, String sel_o_roindId, String firstPlayerName,
                                   String roundTitle, int roundSize, String roundDate, String roundBoard, boolean turno) {
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra(EXTRA_ROUND_ID, sel_o_roindId);
        intent.putExtra(EXTRA_ROUND_SIZE, roundSize);
        intent.putExtra(EXTRA_ROUND_PNAME, firstPlayerName);
        intent.putExtra(EXTRA_ROUND_TITLE, roundTitle);
        intent.putExtra(EXTRA_ROUND_DATE, roundDate);
        intent.putExtra(EXTRA_ROUND_BOARD, roundBoard);
        intent.putExtra(EXTRA_ROUND_TURN, turno);
        return intent;
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




    @Override
    public void onRoundUpdated(Round round) {
        /*RoundRepository repository = RoundRepositoryFactory.createRepository(this);
        RoundRepository.BooleanCallback booleanCallback = new RoundRepository.BooleanCallback(){
            @Override
            public void onResponse(boolean ok) {
                if(!ok){
                    Toast.makeText(getApplicationContext(), R.string.error_reading_rounds, Toast.LENGTH_LONG).show();
                }
            }
        };
        repository.updateRound(round, booleanCallback);
        repository.close();*/
    }
    /**
     *
     * @param frg
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void changeFragment(Fragment frg){
        TransitionManager.beginDelayedTransition(((ViewGroup)findViewById(R.id.fragment_container)), new Fade());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, frg)
                .commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNewRoundCreated(Round round) {
        String playeruuid = PreferenceActivity.getPlayerName(this);
        String tableroOthello = round.getBoard().tableroToString();
        RoundFragment roundFragment = RoundFragment.newInstance(round.getId(), playeruuid, round.getTitle(), round.getSize(),
                round.getDate(), tableroOthello, round.isTurn());
        changeFragment(roundFragment);
    }

    @Override
    public void onCancelButton() {
        this.finish();
    }
}
