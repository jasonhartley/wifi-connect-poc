package us.jasonh.wificonnect;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final ScheduledExecutorService worker =
            Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = this;

        String mohawkSsid = getString(R.string.networkSSID);
        String mohawkPass = getString(R.string.networkPass);

        // This assumes connection to a WPA-protected wifi AP
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = "\"" + mohawkSsid + "\"";
        wifiConfig.preSharedKey = "\""+ mohawkPass +"\"";

        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int previousNetworkId = wifiInfo.getNetworkId();

        wifiManager.addNetwork(wifiConfig);

        List<WifiConfiguration> configuredNetworksList = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration configuredNetwork : configuredNetworksList) {
            if (configuredNetwork.SSID != null && configuredNetwork.SSID.equals("\"" + mohawkSsid + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(configuredNetwork.networkId, true);
                wifiManager.reconnect();

                Toast.makeText(getApplicationContext(), "Connected to gadget wifi.",
                        Toast.LENGTH_LONG).show();

                break;
            }
        }

        reconnectToPreviousNetwork(wifiManager, previousNetworkId);

    }

    void reconnectToPreviousNetwork(final WifiManager wifiManager, final int previousNetworkId) {
        Runnable task = new Runnable() {
            public void run() {
                wifiManager.disconnect();
                wifiManager.enableNetwork(previousNetworkId, true);
                wifiManager.reconnect();
            }
        };
        worker.schedule(task, 15, TimeUnit.SECONDS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
