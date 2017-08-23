package marco45.org.myowncryptowatcher;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AltCoinCurency.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AltCoinCurency#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AltCoinCurency extends Fragment {
    private static final String bitcoinURL = "https://api.quadrigacx.com/v2/ticker?book=btc_cad";
    private static final String QueryURL = "https://bittrex.com/api/v1.1/public/getticker?market=";

    static SharedPreferences settings;
    static SharedPreferences.Editor editor;

    private String name = "Bitcoin";
    private double QT = 0;
    private String book = "btc_cad";
    private String picture = "btc_cad";
    private double price = 0;
    private double value = 0;
    private MainActivity activity;
    private double bitcoinValue = 0;

    private OnFragmentInteractionListener mListener;

    public AltCoinCurency() {
        // Required empty public constructor
    }

    public static AltCoinCurency newInstance(String tmpName, String tmpQT, String tmpbook, String tmppicture, MainActivity global) {
        AltCoinCurency fragment = new AltCoinCurency();
        fragment.QT = Double.parseDouble(tmpQT);
        fragment.name = tmpName;
        fragment.book = tmpbook;
        fragment.picture = tmppicture;
        fragment.activity = global;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crypto_curency, container, false);

        //Get Price
        try{
            // Create URL
            String resultMessage = getResponseText(bitcoinURL);
            JSONObject response = new JSONObject(resultMessage);
            bitcoinValue = response.getDouble("last");

            resultMessage = getResponseText(QueryURL+book);
            response = new JSONObject(resultMessage);
            double tmpValue = response.getJSONObject("result").getDouble("Last");

            price = bitcoinValue*tmpValue;

        } catch (Exception e){
            Log.e("ERROR",e.toString());
        }

        value = price * QT;
        activity.updateGlobalValue();



        //Set all info in the view
        TextView FullPrice = (TextView)v.findViewById(R.id.priceLbl);
        FullPrice.setText(new DecimalFormat("#####.##").format(price)+'$');

        final TextView valueField = (TextView)v.findViewById(R.id.valueLBL);
        valueField.setText(new DecimalFormat("#####.##").format(value)+'$');

        final TextView qtView = (TextView)v.findViewById(R.id.qtLbl);
        qtView.setText(new DecimalFormat("#####.##").format(QT));

        TextView namelbl = (TextView)v.findViewById(R.id.nameLbl);
        namelbl.setText(name);


        ImageView imageView = (ImageView)v.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Set Quantity");
                final EditText input = new EditText(activity);

                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        QT = Double.parseDouble(input.getText().toString());
                        settings = activity.getPreferences(Context.MODE_PRIVATE);
                        editor = settings.edit();
                        editor.putString(name, Double.toString(QT));
                        editor.commit();

                        value = price * QT;
                        activity.updateGlobalValue();
                        valueField.setText(new DecimalFormat("#####.##").format(value)+'$');
                        qtView.setText(new DecimalFormat("#####.##").format(QT));


                    }
                });

                builder.show();

            }
        });

        try{
            URL url = new URL(picture);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            imageView.setImageBitmap(bmp);

        } catch (IOException e){
            //TODO: add error handling
        }

        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Return the total value of the coins
     * @return
     */
    public double getValue(){
        return value;
    }

    private String getResponseText(String stringUrl) throws IOException
    {
        StringBuilder response  = new StringBuilder();

        URL url = new URL(stringUrl);
        HttpURLConnection httpconn = (HttpURLConnection)url.openConnection();
        if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK)
        {
            BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()),8192);
            String strLine = null;
            while ((strLine = input.readLine()) != null)
            {
                response.append(strLine);
            }
            input.close();
        }
        return response.toString();
    }
}
