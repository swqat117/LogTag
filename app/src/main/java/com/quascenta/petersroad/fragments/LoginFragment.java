package com.quascenta.petersroad.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.quascenta.petersroad.activities.ForgotPasswordActivity;
import com.quascenta.petersroad.activities.SocialLoginManager;
import com.quascenta.petersroad.interfaces.LoginPresenter;
import com.quascenta.petersroad.presenters.LoginPresenterImpl;
import com.quascenta.petersroad.interfaces.LoginView;
import com.quascenta.petersroad.activities.MainActivity;
import com.quascenta.petersroad.broadway.R;
import com.quascenta.petersroad.activities.SignUpActivity;
import com.quascenta.petersroad.widget.LogTagEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import timber.log.Timber;

import static com.quascenta.petersroad.broadway.R.id.Login;

/**
 * Created by AKSHAY on 10/27/2016.
 */

public class LoginFragment extends BaseFragment implements LoginView{
    private static final String TAG = MainActivity.class.getSimpleName();
    int i = 0;
    private ProgressDialog progress;
        @Bind(R.id.et_username)LogTagEditText et_username;
        @Bind(R.id.et_pass)LogTagEditText et_password;
        @Bind(R.id.iv_login_person)ImageView iv_user;
        @Bind(R.id.iv_login_passkey)ImageView iv_pass;
        @Bind(Login)Button  _btnValidIndicator;
        SignInButton button;
        LoginButton button1;

        private Observable<CharSequence> _usernameChangeObservable;
        private Observable<CharSequence> _passwordChangeObservable;
        @Bind(R.id.fullscreen_content_controls)LinearLayout linearLayout;
        private Subscription subscription = null;
        LoginPresenter loginPresenter;

    Observable<Boolean> emailObservable;
    Observable<Boolean> passwordObservable;


    public LoginFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_login,container,false);
        ButterKnife.bind(this,view);
        button = (SignInButton) view.findViewById(R.id.gmail_login);
        button1 = (LoginButton) view.findViewById(R.id.facebook_login);
        et_username.changeHintWithAnim("username");
        et_password.changeHintWithAnim("password");


        _usernameChangeObservable = RxTextView.textChanges(et_username).skip(1);
        _passwordChangeObservable = RxTextView.textChanges(et_password).skip(1);
        loginPresenter = new LoginPresenterImpl(this);
        subscription = loginPresenter.add(_usernameChangeObservable,_passwordChangeObservable);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGmailClick();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //setFocusChange();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        subscription.unsubscribe();

    }

    public  void setFocusChange(){

        et_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                et_username.setAlpha(1f);
                iv_user.setAlpha(1f);
                et_password.setAlpha(.6f);
                iv_pass.setAlpha(.6f);
                 }
        });

        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                et_password.setAlpha(1f);
                iv_pass.setAlpha(1f);
                et_username.setAlpha(.6f);
                iv_user.setAlpha(.6f);
                Timber.d("Pass");

            }
        });
    }

    @OnClick(Login)
        public void onclick(){

        switch (i){
            case 0:
                Intent intent = new Intent(getActivity(), SignUpActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent signup_intent = new Intent(getActivity(), ForgotPasswordActivity.class);
                startActivity(signup_intent);
                break;
            case 2:
                Intent Login_intent = new Intent(getActivity(), MainActivity.class);
                startActivity(Login_intent);
                break;
        }


    }

    @Override
    public void updateUI() {

    }

    @Override @SuppressWarnings("deprecation")
    public void setPasswordError(String error, int code) {
        i = code;
        this.et_password.setError(error);
        System.out.println(error);
        linearLayout.setVisibility(View.VISIBLE);
        _btnValidIndicator.setText("Forgot Password");
        _btnValidIndicator.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        _btnValidIndicator.setAnimation(AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_in));

    }



    @Override @SuppressWarnings("deprecation")
    public void setUsernameError(String message, int code) {
        i = code;
        this.et_username.setError(message);
        System.out.println(message);
        linearLayout.setVisibility(View.VISIBLE);
        _btnValidIndicator.setText("SIGN UP");
        _btnValidIndicator.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        _btnValidIndicator.setAnimation(AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_in));

    }


    @Override@SuppressWarnings("deprecation")
    public void onSuccess(String message , int code) {
        i = code;
        linearLayout.setVisibility(View.VISIBLE);
        _btnValidIndicator.setText("LOGIN");
        System.out.println(message);
        _btnValidIndicator.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        _btnValidIndicator.setAnimation(AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_in));


    }

    public void download(View view){
        progress= new ProgressDialog(getActivity());
        progress.setMessage("Loading Page...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(false);

        progress.show();

       new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   Thread.sleep(300l);

               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       }).start();



    }



    public void onClick(){
        SocialLoginManager.getInstance(getContext())
                .facebook()
                .login()
                .subscribe(socialUser -> {
                            Log.d(TAG, "userId: " + socialUser.userId);
                            Log.d(TAG, "photoUrl: " + socialUser.photoUrl);
                            Log.d(TAG, "accessToken: " + socialUser.accessToken);
                            Log.d(TAG, "name: " + socialUser.profile.name);
                            Log.d(TAG, "email: " + socialUser.profile.email);
                            Log.d(TAG, "pageLink: " + socialUser.profile.pageLink);
                        },
                        error -> {
                            Log.d(TAG, "error: " + error.getMessage());
                        });
    }





        public void onGmailClick(){

            SocialLoginManager.getInstance(getContext())
                    .google()
                    .login()
                    .subscribe(socialUser -> {
                                Log.d(TAG, "userId: " + socialUser.userId);
                                Log.d(TAG, "photoUrl: " + socialUser.photoUrl);
                                Log.d(TAG, "accessToken: " + socialUser.accessToken);
                                Log.d(TAG, "email: " + socialUser.profile.email);
                                Log.d(TAG, "name: " + socialUser.profile.name);
                            },
                            error -> {
                                Log.d(TAG, "error: " + error.getMessage());
                            });

    }



    }



