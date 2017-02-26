package myapp.esps.uam.es.robpizarro.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.View;

import myapp.esps.uam.es.robpizarro.R;
import myapp.esps.uam.es.robpizarro.models.Round;


/**
 * Created by localuser01 on 20/02/17.
 */

public class MainActivity extends AppCompatActivity  implements RoundFragment.Callbacks {

    private static final String EXTRA_ROUND_ID = "myapp.esps.uam.es.robpizarro.round_id"; //Identificador de la partida.

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            String roundId = getIntent().getStringExtra(EXTRA_ROUND_ID);
            RoundFragment roundFragment = RoundFragment.newInstance(roundId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, roundFragment)
                    .commit();
        }
    }

    public static Intent newIntent(Context packageContext, String sel_o_roindId) {
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra(EXTRA_ROUND_ID, sel_o_roindId);
        return intent;
    }

    @Override
    public void onRoundUpdated(Round round) {

    }

    /**
     *
     * @param frg
     */
    public void changeFragment(Fragment frg){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, frg)
                .commit();
    }
}
