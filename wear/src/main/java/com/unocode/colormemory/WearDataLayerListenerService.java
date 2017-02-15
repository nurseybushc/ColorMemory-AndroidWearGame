package com.unocode.colormemory;

import android.content.Intent;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class WearDataLayerListenerService extends WearableListenerService {
    public static final String START_ACTIVITY_PATH = "/colormemory/wear";
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        if(messageEvent.getPath().equals(START_ACTIVITY_PATH)){
            Intent intent = new Intent(this , MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}