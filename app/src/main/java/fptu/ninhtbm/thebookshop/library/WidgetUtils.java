package fptu.ninhtbm.thebookshop.library;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

public class WidgetUtils {
    public static void showSnackbar(View view, int resId) {
        Snackbar.make(view, resId, Snackbar.LENGTH_LONG).show();
    }

    public static Dialog getCustomDialogSetUp(Context context, int ResId) {
        Dialog dialog = new Dialog(context);
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(ResId);
        dialog.getWindow().setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        return dialog;
    }
}
