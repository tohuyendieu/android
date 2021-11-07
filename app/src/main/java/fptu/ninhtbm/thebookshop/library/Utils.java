package fptu.ninhtbm.thebookshop.library;

import java.text.NumberFormat;

public class Utils {
    public static String convertCurrency(double money) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(money);
    }
}
