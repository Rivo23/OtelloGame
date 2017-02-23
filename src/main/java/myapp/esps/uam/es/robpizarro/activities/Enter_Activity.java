package myapp.esps.uam.es.robpizarro.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import myapp.esps.uam.es.robpizarro.R;

/**
 * Created by e268930 on 23/02/17.
 */

public class Enter_Activity extends AppCompatActivity implements View.OnClickListener{
    private Button b_new;
    private Button b_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_layout);
        b_new=(Button)findViewById(R.id.ib00);
        b_help=(Button) findViewById(R.id.ib01);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ib00:
                break;
            case R.id.ib01:
                break;
            default:
                break;
        }
    }
}
