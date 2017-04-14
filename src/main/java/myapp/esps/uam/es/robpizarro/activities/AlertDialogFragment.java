package myapp.esps.uam.es.robpizarro.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import myapp.esps.uam.es.robpizarro.R;

/**
 * Created by localuser01 on 22/02/17.
 */

public class AlertDialogFragment extends DialogFragment {
    public static String keyTitle = "STR";
    public static String keyMsg = "MSG";
    public static String keyType = "INT";
    public static final int STARTROUND = 1;
    public static final int FINALROUND = 99;

    public static AlertDialogFragment newInstance(String title, int type, String message) {
        AlertDialogFragment f = new AlertDialogFragment();

        Bundle args = new Bundle();
        args.putString(keyTitle, title);
        args.putString(keyMsg, message);
        args.putInt(keyType, type);
        f.setArguments(args);

        return f;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(getArguments().getString(keyTitle));
        alertDialogBuilder.setMessage(getArguments().getString(keyMsg));
        switch (getArguments().getInt(keyType)){
            case STARTROUND:
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                break;
            case FINALROUND:
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            public void onClick(DialogInterface dialog, int which) {
                        SelectFragment selectFragment = new SelectFragment();

                        if (activity instanceof MainActivity){
                            ((MainActivity) activity).changeFragment(selectFragment);
                        }else{
                            ((RoundListActivity) activity).changeFragmentContainer(selectFragment);
                        }
                        dialog.dismiss();
                            }
                        });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (activity instanceof MainActivity)
                            activity.finish();
                        else{
                            ((RoundListActivity) getActivity()).changeFragmentContainer(new RoundListFragment());
                        }
                            }
                        });
                break;
            default:
                break;
        }
        return alertDialogBuilder.create();
    }
}
