package com.matterhornlegal.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;

import com.matterhornlegal.R;


/**
 * Created by karan.kalsi on 4/7/2017.
 */
public class CircularProgressDialog {
    private Dialog dialog;
    private View circularProgress;
    public CircularProgressDialog(Context context)
    {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.circular_progress_dialog_new);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        circularProgress = dialog.findViewById(R.id.circular_progress_view);
    }
public void show()
{
    try{
        if(dialog==null )return;
        dialog.show();
    }
    catch (Exception e)
    {
    }

}
    public void dismiss()
    {
        try{
        if(dialog==null )return;
        dialog.dismiss();
    }
    catch (Exception e)
    {

    }
    }

    public boolean isShowing()
    {
        return dialog!=null && dialog.isShowing();
    }
}
