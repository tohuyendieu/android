package fptu.ninhtbm.thebookshop.ui.signup;

public interface ISignUpActivity {
    void popSnackbarNotification(int messageResId);

    void directToLogin();

    void setProgressLoading(boolean isLoading);
}
