package com.instaguard.user.Helpers.NetworkManager;

public interface Monitor extends LifecycleListener {

    interface ConnectivityListener {
        void onConnectivityChanged(int connectionType, boolean isConnected, boolean isFast);
    }
}
