package myapp.esps.uam.es.robpizarro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import myapp.esps.uam.es.robpizarro.R;
import myapp.esps.uam.es.robpizarro.models.Round;

public class RoundListActivity extends AppCompatActivity implements RoundListFragment.Callbacks, RoundFragment.Callbacks {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masterdetail);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new RoundListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
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

    @Override
    public void onRoundSelected(Round round) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = MainActivity.newIntent(this, round.getId());
            startActivity(intent);
        } else {
            RoundFragment roundFragment = RoundFragment.newInstance(round.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, roundFragment)
                    .commit();
        }
    }

    @Override
    public void onRoundUpdated(Round round) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        RoundListFragment roundListFragment = (RoundListFragment)fragmentManager.findFragmentById(R.id.fragment_container);
        roundListFragment.updateUI();
    }


}