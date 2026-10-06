package com.instaguard.user.Helpers.SocialNetworks.facebookSignIn;

public interface FacebookResponse {
    void onFbSignInFail();

    void onFbSignInSuccess();

    void onFbProfileReceived(FacebookUser facebookUser);

    void onFBSignOut();
}
