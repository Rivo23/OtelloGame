package myapp.esps.uam.es.robpizarro.activities;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import myapp.esps.uam.es.robpizarro.R;
import myapp.esps.uam.es.robpizarro.models.Round;
import myapp.esps.uam.es.robpizarro.models.RoundRepository;

/**
 * Created by localuser01 on 22/02/17.
 */

public class AlertDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.game_over);
        alertDialogBuilder.setMessage(R.string.game_over_message);
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SelectFragment selectFragment = new SelectFragment();
                        ((MainActivity) activity).changeFragment(selectFragment);
                        dialog.dismiss();
                        /*
                        Round round = new Round(RoundRepository.SIZE);
                        RoundRepository.get(getActivity()).addRound(round);
                        if (activity instanceof RoundListActivity)
                            ((RoundListActivity) activity).onRoundUpdated(round);
                        else
                            ((MainActivity) activity).finish();
                        dialog.dismiss();
                        */
                    }
                });
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (activity instanceof MainActivity)
                            activity.finish();
                        dialog.dismiss();
                    }
                });
        return alertDialogBuilder.create();
    }
}
