package com.quascenta.petersroad.presenters;

import com.quascenta.petersroad.Utils.Validators.AlphaNumericValidator;
import com.quascenta.petersroad.Utils.Validators.EmailValidator;
import com.quascenta.petersroad.interactors.LoginInteractorImpl;
import com.quascenta.petersroad.interfaces.LoginInteractor;
import com.quascenta.petersroad.interfaces.LoginView;
import com.quascenta.petersroad.interfaces.LoginPresenter;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import timber.log.Timber;

import static android.text.TextUtils.isEmpty;
import static android.util.Patterns.EMAIL_ADDRESS;


/**
 * Created by AKSHAY on 11/1/2016.
 */

public class LoginPresenterImpl implements LoginPresenter {
    private static final int USERNAME_ERR = 0;
    private static final int PASSWORD_ERR = 1;
    private static final int LOGIN_SUCCESS = 2;
    LoginView loginView;

    EmailValidator emailValidator = new EmailValidator("Invalid email");
    AlphaNumericValidator alphaNumericValidator = new AlphaNumericValidator("invalid password");


    // not to pass context instead use Dagger 2 ..... for now its okay
   public LoginPresenterImpl(LoginView loginView){
       this.loginView = loginView;

   }

    @Override
    public Subscription add(Observable<CharSequence> username, Observable<CharSequence> password) {
        LoginInteractor loginInteractor = new LoginInteractorImpl();

        //Need to add login interactor with proper validation from the database
        //For now just reading the values and passing true

        return Observable.combineLatest(username,password,(user,pass) -> {

            boolean emailValid = !isEmpty(user) &&
                    EMAIL_ADDRESS.matcher(user).matches() && loginInteractor.validEmail(user) ;
            boolean passValid = !isEmpty(pass) && pass.length() > 7 && loginInteractor.validPassword(pass);

            if (!emailValid) {
                loginView.setUsernameError("Invalid Email!",USERNAME_ERR);

            }

            if (!passValid) {
                loginView.setPasswordError("Invalid Password!",PASSWORD_ERR);

            }
            return emailValid && passValid;
        }).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {
                Timber.d("completed");
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "there was an error");
                e.printStackTrace();
            }

            @Override
            @SuppressWarnings("deprecation")
            public void onNext(Boolean formValid) {
                if (formValid) {
                  loginView.onSuccess("Pass",LOGIN_SUCCESS);

                } else {

                    System.out.println("Error ");
                }
            }
        });

            }
}
