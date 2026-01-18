/*
 * SPDX-FileCopyrightText: 2020 The Proton AOSP Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.protonaosp.deviceconfig;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.DeviceConfig;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "SimpleDeviceConfig";

    @Override
    public void onReceive(Context context, Intent intent) {
        new Thread(() -> {
            Log.i(TAG, "Updating device config at boot");
            updateDefaultConfigs(context);
        }).start();
    }

    private void updateDefaultConfigs(Context context) {
        updateConfig(context, R.array.configs_base);
        updateConfig(context, R.array.configs_device);
    }

    private void updateConfig(Context context, int configArray) {
        // Set current properties
        String[] rawProperties = context.getResources().getStringArray(configArray);
        for (String property : rawProperties) {
            // Format: namespace/key=value
            String[] kv = property.split("=");
            String fullKey = kv[0];
            String[] nsKey = fullKey.split("/");

            String namespace = nsKey[0];
            String key = nsKey[1];
            String value = "";
            if (kv.length > 1) {
                value = kv[1];
            }

            DeviceConfig.setProperty(namespace, key, value, true);
        }
    }
}
