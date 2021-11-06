package fptu.ninhtbm.thebookshop.library;

public class Validates {
    public static boolean validateEmail(String value){
        String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
        if(value != null && !value.trim().equals("") && value.matches(EMAIL_REGEX)){
            return true;
        }
        return false;
    }

    public static boolean validNumeric(String value){
        String NAME_REGEX = "[0-9]+";
        if(value != null && !value.trim().equals("") && value.matches(NAME_REGEX)){
            return true;
        }
        return false;
    }

    public static boolean validatePhone(String value){
        if(value != null && !value.trim().equals("")
                && value.replaceAll("\\s+", "").length() >= 10
                && value.replaceAll("\\s+", "").length() <= 15){
            return true;
        }
        return false;
    }
}
