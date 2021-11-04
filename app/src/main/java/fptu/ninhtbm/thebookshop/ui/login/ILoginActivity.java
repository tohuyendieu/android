package fptu.ninhtbm.thebookshop.ui.login;

public interface ILoginActivity {

    void popSnackbarNotification(int messageResId);

    void directToHome();

    void setProgressLoading(boolean isLoading);
}
