package com.quascenta.petersroad.activities;

import android.provider.ContactsContract;

import java.io.Serializable;

/**
 * Created by AKSHAY on 12/23/2016.
 */

public class SocialUser implements Serializable {

    public String userId;
    public String accessToken;
    public String photoUrl;
    public Profile profile;




    public SocialUser(){}

    public SocialUser(SocialUser user){
        this.userId = user.userId;
        this.accessToken = user.accessToken;
        this.photoUrl = user.photoUrl;

        if(user.profile !=  null){
            this.profile = new Profile(user.profile);
        }
    }


    @Override
    public boolean equals(Object obj) {
        if(this == obj)return true;
        if( obj == null || getClass() != obj.getClass()) return false;

        SocialUser that = (SocialUser) obj;
        return userId != null ? userId.equals(that.userId) : that.userId == null;

    }


    @Override
    public int hashCode(){
        return userId != null ? userId.hashCode() : 0;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SocialUser{");
        sb.append("userId='").append(userId).append('\'');
        sb.append(", accessToken='").append(accessToken).append('\'');
        sb.append(", photoUrl='").append(photoUrl).append('\'');
        sb.append(", profile='").append(profile);
        sb.append('}');
        return sb.toString();
    }

  public static class Profile implements Serializable{

      public String name;
      public String email;
      public String pageLink;


      public Profile(){}


      public Profile(Profile other){
          this.name = other.name;
          this.email = other.email;
          this.pageLink = other.pageLink;
      }


      @Override
      public String toString() {
          final StringBuilder stringBuilder = new StringBuilder("Profile{");
          stringBuilder.append("name=").append(name).append('\'');
          stringBuilder.append(",email='").append(email).append('\'');
          stringBuilder.append(", pageLink='").append(pageLink).append('\'');
          stringBuilder.append('}');
          return stringBuilder.toString();

      }
  }




    }

