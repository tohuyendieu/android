package fptu.ninhtbm.thebookshop.ui.signup.presenter;

public interface ISignUpPresenter {
    void createAccountCustomer(String firstname, String lastname, String username, String email, String password, String repassword, String phone, String address);
}
