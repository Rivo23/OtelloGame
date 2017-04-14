package myapp.esps.uam.es.robpizarro.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import myapp.esps.uam.es.robpizarro.R;
import myapp.esps.uam.es.robpizarro.models.Round;
import myapp.esps.uam.es.robpizarro.models.RoundRepository;
import myapp.esps.uam.es.robpizarro.models.RoundRepositoryFactory;
import myapp.esps.uam.es.robpizarro.models.StyleManager;

/**
 * Created by localuser01 on 24/02/17.
 */

public class SelectFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private final int REL_WRAP = RelativeLayout.LayoutParams.WRAP_CONTENT;
    private final int REL_MATCH = RelativeLayout.LayoutParams.MATCH_PARENT;
    private Callbacks callback;
    private final int id_4button=3;
    private boolean turnoHumano=false;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case id_4button:
                startNewSelectedRound(4);
                break;
            case id_4button+1:
                startNewSelectedRound(6);
                break;
            case id_4button+2:
                startNewSelectedRound(8);
                break;
            case id_4button+3:
                callback.onCancelButton();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        turnoHumano=compoundButton.isChecked();
    }

    public interface Callbacks {
        void onNewRoundCreated(Round round);
        void onCancelButton();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startNewSelectedRound(int size){
        Round round = new Round(size, turnoHumano);
        RoundRepository repository = RoundRepositoryFactory.createRepository(getContext());
        RoundRepository.BooleanCallback callbacks = new RoundRepository.BooleanCallback(){
            @Override
            public void onResponse(boolean ok){
                if(!ok){
                    Snackbar.make(getView(), R.string.error_new_game,
                            Snackbar.LENGTH_LONG).show();
                }
            }
        };
        String playeruuid = PreferenceActivity.getUUIDPlayer(getActivity());
        round.setPlayerUUID(playeruuid);
        repository.addRound(round, callbacks);
        repository.close();
        callback.onNewRoundCreated(round);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (SelectFragment.Callbacks) context;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
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
        root.setBackgroundResource(StyleManager.getwindowBackColor(getContext()));
        root.setLayoutParams(paramsMatch);


        createTitleTextView(root, 99*id_4button);
        createCheckBox(root, id_4button-1, 99*id_4button);
        createButtonBelow(root, id_4button, R.drawable.tab_4_48dp, id_4button-1);
        createButtonBelow(root, id_4button+1, R.drawable.tab_6_48dp, id_4button);
        createButtonBelow(root, id_4button+2, R.drawable.tab_8_48dp, id_4button+1);
        createButtonwithShapeBelow(root, id_4button+3, id_4button+2);

        ScrollView scrollView = new ScrollView(getContext());
        scrollView.addView(root, new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT));
        scrollView.setFillViewport(true);
        return scrollView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createCheckBox(RelativeLayout parent, int id, int below){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(REL_WRAP,
                REL_WRAP);
        params.addRule(RelativeLayout.BELOW, below);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        CheckBox checkBox = new CheckBox(getActivity());
        checkBox.setId(id);
        checkBox.setText(getString(R.string.checkbox));
        checkBox.setPadding(5,5,5,5);
        checkBox.setTextSize(24);
        checkBox.setOnCheckedChangeListener(this);
        checkBox.setLayoutParams(params);

        parent.addView(checkBox);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createTitleTextView(RelativeLayout parent, int id){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(REL_WRAP,
                REL_WRAP);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.title_select);
        textView.setBackgroundResource(StyleManager.getwindowBackColor(getContext()));
        textView.setTextColor(StyleManager.getTextColorPrimary(getContext()));
        textView.setTextSize(32);
        textView.setId(id);
        textView.setPadding(5,5,5,5);

        textView.setLayoutParams(params);
        parent.addView(textView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createButtonBelow (RelativeLayout parent, int id, int background, int below){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(REL_MATCH,
                REL_WRAP);
        params.addRule(RelativeLayout.BELOW, below);
        params.setMargins(getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin), getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin),
                getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin), getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin));
        ImageButton button = new ImageButton(getActivity());
        button.setId(id);
        button.setImageResource(background);
        button.setBackgroundResource(StyleManager.getwindowBackColor(getContext()));
        button.setLayoutParams(params);
        button.setOnClickListener(this);

        ShapeDrawable shapedrawable = new ShapeDrawable();
        shapedrawable.setShape(new RectShape());
        shapedrawable.getPaint().setColor(StyleManager.getPrimaryColor(getContext()));
        shapedrawable.getPaint().setStrokeWidth(10f);
        shapedrawable.getPaint().setStyle(Paint.Style.STROKE);
        button.setBackground(shapedrawable);

        parent.addView(button);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void createButtonwithShapeBelow(RelativeLayout parent, int id, int below){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(REL_WRAP,
                REL_WRAP);
        params.addRule(RelativeLayout.BELOW, below);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);

        Button button = new Button(getActivity());
        button.setId(id);
        button.setText(getResources().getString(R.string.cancelar).toUpperCase());
        button.setBackgroundResource(R.drawable.buttonshape);
        button.setLayoutParams(params);
        button.setOnClickListener(this);
        parent.addView(button);
    }

}
