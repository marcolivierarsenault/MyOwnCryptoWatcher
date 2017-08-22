package marco45.org.myowncryptowatcher;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CryptoCurency.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CryptoCurency#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CryptoCurency extends Fragment {
    private static final String QueryURL = "https://api.quadrigacx.com/v2/ticker?book=";

    private String name = "Bitcoin";
    private double QT = 0;
    private String book = "btc_cad";
    private String picture = "btc_cad";

    private OnFragmentInteractionListener mListener;

    public CryptoCurency() {
        // Required empty public constructor
    }

    public static CryptoCurency newInstance(String tmpName, double tmpQT, String tmpbook, String tmppicture) {
        CryptoCurency fragment = new CryptoCurency();
        fragment.QT = tmpQT;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crypto_curency, container, false);
        TextView FullPrice = (TextView)v.findViewById(R.id.priceLbl);
        FullPrice.setText("45");

        TextView value = (TextView)v.findViewById(R.id.valueLBL);
        value.setText("BOOM");

        TextView qtView = (TextView)v.findViewById(R.id.qtLbl);
        FullPrice.setText(Double.toString(QT));

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
}
