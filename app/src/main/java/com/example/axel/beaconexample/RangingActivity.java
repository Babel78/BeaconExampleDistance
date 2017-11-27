package com.example.axel.beaconexample;

import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.List;

public class RangingActivity extends AppCompatActivity implements BeaconConsumer,RangeNotifier{
    private BeaconManager mBeaconManager;
    protected static final String TAG = "RangingActivity";
    TextView UUID;
    TextView Distance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);
        UUID= (TextView)findViewById(R.id.uuid);
        Distance= (TextView)findViewById(R.id.distance);
        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mBeaconManager.getBeaconParsers()
                .add(new BeaconParser().setBeaconLayout("s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19"));
        mBeaconManager.bind(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        mBeaconManager.unbind(this);
    }
    @Override
    public void onBeaconServiceConnect() {
        mBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(final Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    for(Beacon beacon:beacons){
                        final Identifier namespacedId = beacon.getId1();
                        //Identifier instancedId= beacon.getId2();
                        final double distance=beacon.getDistance();
                        final int uuid=beacon.getServiceUuid();
                        Log.i(TAG, "Distancia : "+distance+" UUID: "+ uuid);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UUID.setText(String.valueOf(namespacedId.toString()));
                                Distance.setText(String.valueOf(distance));
                            }
                        });
                    }

                }
                else{
                    Log.i(TAG,"GG los beacons :( ");
                }
            }
        });

        try {
            mBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException ignored) {
            System.out.println(ignored.toString());
        }
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {

    }

}
