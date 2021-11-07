package fptu.ninhtbm.thebookshop.library;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.Timestamp;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convertTimestampToDateFormat(Timestamp timeStamp, String expectFormat) {
        LocalDate date = timeStamp.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DateTimeFormatter expectDateFormatter = DateTimeFormatter.ofPattern(expectFormat);
        return date.format(expectDateFormatter);
    }
}
