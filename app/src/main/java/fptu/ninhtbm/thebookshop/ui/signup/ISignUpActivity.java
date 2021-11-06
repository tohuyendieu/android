package fptu.ninhtbm.thebookshop.ui.signup;

public interface ISignUpActivity {
    void popSnackbarNotification(int messageResId);

    void backToLogin();

    void setProgressLoading(boolean isLoading);
}
