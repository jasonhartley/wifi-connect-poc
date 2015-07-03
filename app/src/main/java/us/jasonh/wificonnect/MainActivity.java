package us.jasonh.wificonnect;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

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
        wifiManager.addNetwork(wifiConfig);

        List<WifiConfiguration> configuredNetworksList = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration configuredNetwork : configuredNetworksList) {
            if (configuredNetwork.SSID != null && configuredNetwork.SSID.equals("\"" + mohawkSsid + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(configuredNetwork.networkId, true);
                wifiManager.reconnect();

                break;
            }
        }
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
