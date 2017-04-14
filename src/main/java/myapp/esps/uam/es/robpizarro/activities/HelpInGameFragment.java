package myapp.esps.uam.es.robpizarro.activities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import myapp.esps.uam.es.robpizarro.R;

/**
 * Created by localuser01 on 11/04/17.
 */

public class HelpInGameFragment extends Fragment {
    private View.OnClickListener listener;
    public Callbacks callbacks;

    public interface Callbacks {
        void onReturn();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (HelpInGameFragment.Callbacks) context;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_master2, container, false);
        com.getbase.floatingactionbutton.FloatingActionButton floating = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.back);
        if (floating!=null){
            listener=new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callbacks.onReturn();
                }
            };
            floating.setOnClickListener(listener);
        }
        return view;
    }


}
