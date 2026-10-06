package com.instaguard.user.Helpers.NetworkManager.Internal;


import com.instaguard.user.Helpers.NetworkManager.Monitor;

public class NoopMonitor implements Monitor {
    @Override
    public void onStart() {
        //no-op
    }

    @Override
    public void onStop() {
        //no-op
    }
}
