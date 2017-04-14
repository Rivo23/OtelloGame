package myapp.esps.uam.es.robpizarro.activities;

import android.os.Bundle;
import android.widget.TextView;

import myapp.esps.uam.es.robpizarro.R;
import myapp.esps.uam.es.robpizarro.models.RoundRepository;
import myapp.esps.uam.es.robpizarro.models.RoundRepositoryFactory;

/**
 * Created by localuser01 on 12/04/17.
 */

public class ScoresActivity extends BaseActivity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_layout);
        RoundRepository repository = RoundRepositoryFactory.createRepository(this);
        String playeruuid = PreferenceActivity.getUUIDPlayer(this);
        String[] values=repository.getStats(playeruuid);
        TextView textView0 = (TextView) findViewById(R.id.textView1);
        textView0.setText(values[0]);
        TextView textView1 = (TextView) findViewById(R.id.textView2);
        textView1.setText(values[1]);
        TextView textView2 = (TextView) findViewById(R.id.textView3);
        textView2.setText(values[2]);
        repository.close();
    }
}
