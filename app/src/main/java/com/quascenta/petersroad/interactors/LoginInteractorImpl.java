package com.quascenta.petersroad.interactors;


import com.quascenta.petersroad.Utils.Validators.EmailValidator;
import com.quascenta.petersroad.interfaces.LoginInteractor;


/**
 * Created by AKSHAY on 11/1/2016.
 */

public class LoginInteractorImpl implements LoginInteractor {

    User user ;
    EmailValidator emailvalidator = new EmailValidator("invalid Email");


    public LoginInteractorImpl(){
        user = new User("akshayv.1608@gmail.com","123456789");
        System.out.println("pass");
    }
    @Override
    public boolean validEmail(CharSequence username) {
        if(username.toString().equals(user.getUsername()))

        return true;
        else
        return false;
    }

    @Override
    public boolean validPassword(CharSequence password) {
        if(password.toString().equals(user.getPassword()))
        return true;
        else
        return false;
    }

    private class User {
        String username,password;


        public User(String username,String password){
            this.username = username;
            this.password = password;

        }


        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
