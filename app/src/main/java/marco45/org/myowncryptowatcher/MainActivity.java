package marco45.org.myowncryptowatcher;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements CryptoCurency.OnFragmentInteractionListener, AltCoinCurency.OnFragmentInteractionListener{

    static ArrayList<CryptoCurency> allCrypto = new ArrayList<CryptoCurency>();
    static ArrayList<AltCoinCurency> altCoin = new ArrayList<AltCoinCurency>();

    static SharedPreferences settings;
    static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar)
        ;
        settings = this.getPreferences(MODE_PRIVATE);



        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        allCrypto.add(CryptoCurency.newInstance("Bitcoin",settings.getString("Bitcoin", "0"),"btc_cad","https://files.coinmarketcap.com/static/img/coins/32x32/bitcoin.png",this));
        allCrypto.add(CryptoCurency.newInstance("Ethereum",settings.getString("Ethereum", "0"),"eth_cad","https://i.imgur.com/wRNT3aL.png",this));
        allCrypto.add(CryptoCurency.newInstance("Bitcoin Cash",settings.getString("Bitcoin Cash","0"),"bch_cad","https://files.coinmarketcap.com/static/img/coins/32x32/bitcoin-cash.png",this));
        allCrypto.add(CryptoCurency.newInstance("Bitcoin Gold",settings.getString("Bitcoin Gold","0"),"btg_cad","https://files.coinmarketcap.com/static/img/coins/32x32/bitcoin-gold.png",this));

        altCoin.add(AltCoinCurency.newInstance("STORJ",settings.getString("STORJ", "0"),"BTC-STORJ","https://i.imgur.com/xiEC31X.png",this));

        for (CryptoCurency current : allCrypto){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.cryptoLayout, current).commit();
        }

        for (AltCoinCurency current : altCoin){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.cryptoLayout, current).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void updateGlobalValue(){
        double masterValue = 0;
        for (CryptoCurency current : allCrypto){
            masterValue += current.getValue();
        }


        for (AltCoinCurency current : altCoin){
            masterValue += current.getValue();
        }
        TextView totalValue = (TextView)findViewById(R.id.totalValueLbl);
        totalValue.setText(new DecimalFormat("#####.##").format(masterValue)+'$');
    }
}
