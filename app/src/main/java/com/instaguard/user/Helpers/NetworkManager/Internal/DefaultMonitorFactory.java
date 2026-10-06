package com.instaguard.user.Helpers.NetworkManager.Internal;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.instaguard.user.Helpers.NetworkManager.Monitor;
import com.instaguard.user.Helpers.NetworkManager.MonitorFactory;

public class DefaultMonitorFactory implements MonitorFactory {
    public static final String ACCESS_NETWORK_PERMISSION = Manifest.permission.ACCESS_NETWORK_STATE;

    @NonNull
    @Override
    public Monitor create(
            @NonNull Context context,
            int connectionType,
            @NonNull Monitor.ConnectivityListener listener) {

        int permissionResult = ContextCompat.checkSelfPermission(context, ACCESS_NETWORK_PERMISSION);
        boolean hasPermission = permissionResult == PackageManager.PERMISSION_GRANTED;

        return hasPermission ? new DefaultMonitor(context, listener, connectionType)
                : new NoopMonitor();
    }
}
