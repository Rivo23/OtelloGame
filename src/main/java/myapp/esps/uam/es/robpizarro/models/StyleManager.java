package myapp.esps.uam.es.robpizarro.models;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import myapp.esps.uam.es.robpizarro.R;

/**
 * Created by e268930 on 7/04/17.
 */

public class StyleManager {

    public static int getwindowBackColor(Context context){
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        if(theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true)) return typedValue.resourceId;
        else return R.color.Blue;
    }

    public static int getTextColorPrimary(Context context){
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        if(theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true)) return typedValue.resourceId;
        else return R.color.Blue;
    }

    public static int getPrimaryColor(Context context){
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        if(theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)) return typedValue.resourceId;
        else return R.color.Blue;
    }
}
