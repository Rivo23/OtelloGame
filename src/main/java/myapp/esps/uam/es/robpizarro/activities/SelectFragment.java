package myapp.esps.uam.es.robpizarro.activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import myapp.esps.uam.es.robpizarro.R;
import myapp.esps.uam.es.robpizarro.models.Round;

/**
 * Created by localuser01 on 24/02/17.
 */

public class SelectFragment extends Fragment implements View.OnClickListener {
    private Callbacks callbacks;
    private final int REL_WRAP = RelativeLayout.LayoutParams.WRAP_CONTENT;
    private final int REL_MATCH = RelativeLayout.LayoutParams.MATCH_PARENT;

    @Override
    public void onClick(View v) {

    }

    public interface Callbacks {
        void onRoundSelectSize(Round round);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (SelectFragment.Callbacks) context;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = makeBoard();
        TextView roundTitleTextView = (TextView) rootView.findViewById(0);
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

        createButtonBelow(root, 1, getDrawable(), 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createTitleTextView(RelativeLayout parent, int id){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(REL_WRAP,
                REL_WRAP);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        TextView textView = new TextView(getActivity());
        textView.setId(id);
        textView.setPadding(5,5,5,5);
        textView.setBackgroundResource(R.color.Blue);

        textView.setLayoutParams(params);
        parent.addView(textView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createButtonBelow (RelativeLayout parent, int id, int background, int below){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(REL_WRAP,
                REL_WRAP);
        params.addRule(RelativeLayout.BELOW, below);
        ImageButton button = new ImageButton(getActivity());
        button.setId(id);
        button.setImageResource(background);
        button.setBackgroundResource(R.color.Blue);
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
        button.setBackgroundResource(R.color.Blue);
        button.setLayoutParams(params);
        button.setOnClickListener(this);
        parent.addView(button);
    }
}
