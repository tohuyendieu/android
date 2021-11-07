package fptu.ninhtbm.thebookshop.ui.login;

public interface ILoginActivity {

    void popSnackbarNotification(int messageResId);

    void returnLoginRequest();

    void setProgressLoading(boolean isLoading);
}
