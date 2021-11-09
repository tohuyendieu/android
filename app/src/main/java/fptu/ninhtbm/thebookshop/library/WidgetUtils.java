package fptu.ninhtbm.thebookshop.library;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import fptu.ninhtbm.thebookshop.R;

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


    public static Dialog showDialogSuccessCheckedGreen(Context context, String message, View.OnClickListener action) {
        Dialog dialog = new Dialog(context);
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_review_checked);
        TextView textMessage = dialog.findViewById(R.id.txt_message);
        textMessage.setText(message);
        dialog.getWindow().setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        dialog.setOnShowListener(dialogInterface -> {
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(2000); // Delay 2s and close dialog
                    action.onClick(null);
                    dialog.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        });
        return dialog;
    }
}
