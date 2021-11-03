package fptu.ninhtbm.thebookshop.library;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class WidgetUtils {
    public static void showSnackbar(View view, int resId) {
        Snackbar snackbar = Snackbar.make(view, resId, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
