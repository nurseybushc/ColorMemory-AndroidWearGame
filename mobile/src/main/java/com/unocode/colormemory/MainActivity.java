package com.unocode.colormemory;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import android.support.annotation.NonNull;
import android.widget.ImageView;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;

    public static final String START_ACTIVITY_PATH = "/colormemory/wear";
    ImageView mBtnOnWearable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnOnWearable = (ImageView) findViewById(R.id.startWearable);
        mBtnOnWearable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("button onClick", "enter");
                getNodes(new OnGotNodesListener() {
                    @Override
                    public void onGotNodes(ArrayList<String> nodes) {
                        if(nodes.size() > 0) {
                            Wearable.MessageApi.sendMessage(
                                    getGoogleApiClient(), nodes.get(0), START_ACTIVITY_PATH, null).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                                @Override
                                public void onResult(MessageApi.SendMessageResult result) {
                                    if (!result.getStatus().isSuccess()) {
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("GoogleApi", "onConnected: " + bundle);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("GoogleApi", "onConnectionSuspended: " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("GoogleApi", "onConnectionFailed: " + connectionResult);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
    }

    protected GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /*public void startWear(View v) {
        getNodes(new OnGotNodesListener() {
            @Override
            public void onGotNodes(ArrayList<String> nodes) {
                if (nodes.size() > 0) {
                    sendMessage(nodes.get(0));
                }
            }
        });
    }*/

    private void getNodes(final OnGotNodesListener cb) {
        Wearable.NodeApi.getConnectedNodes(getGoogleApiClient()).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(@NonNull NodeApi.GetConnectedNodesResult nodes) {
                ArrayList<String> results = new ArrayList<>();

                for (Node node : nodes.getNodes()) {
                    if (!results.contains(node.getId())) results.add(node.getId());
                }

                cb.onGotNodes(results);
            }
        });
    }

    public interface OnGotNodesListener {
        void onGotNodes(ArrayList<String> nodes);
    }


    /*private void sendMessage(String node) {
        Wearable.MessageApi.sendMessage(mGoogleApiClient, node, START_ACTIVITY_PATH, new byte[0]).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
            @Override
            public void onResult(@NonNull MessageApi.SendMessageResult sendMessageResult) {
                if (!sendMessageResult.getStatus().isSuccess()) {
                    Log.e("GoogleApi", "Failed to send message with status code: "
                            + sendMessageResult.getStatus().getStatusCode());
                }
            }
        });
    }*/
}