package com.lima.souza.app.calculadeira;

import android.content.Context;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.TextView;

public class EditTextUtils {

    public static float getTextSizeInSP(TextView textView) {
        Context context = textView.getContext();
        return textView.getTextSize() / context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static void setTextSizeInSP(TextView textView, float textSizeSP) {
        Context context = textView.getContext();
        float textSizePixels = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                textSizeSP,
                context.getResources().getDisplayMetrics()
        );
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePixels);
    }
}
