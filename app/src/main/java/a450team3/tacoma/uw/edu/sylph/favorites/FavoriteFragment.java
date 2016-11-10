package a450team3.tacoma.uw.edu.sylph.favorites;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import a450team3.tacoma.uw.edu.sylph.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FavoriteFragment extends Fragment {

    /** Number of Columns for the list. */
    private int mColumnCount = 1;

    /** URL for the php file, used for retrieving favorites. */
    private static final String FAVORITES_URL =
            "http://cssgate.insttech.washington.edu/~_450team3/getFavorites.php?cmd=favorites";

    /** List of favorites for creating fragment. */
    private List<Favorite> mFavoritesList;

    /** Recycler View for cycling fragments */
    private RecyclerView mRecyclerView;

    /** Listener for List interaction */
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FavoriteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            DownloadFavoriteTask dFT = new DownloadFavoriteTask();
            dFT.execute(new String[] {FAVORITES_URL});
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Favorite favorite);
    }

    /**
     * Runs in the background to download the Favorites data from the database.
     */
    public class DownloadFavoriteTask extends AsyncTask<String, Void, String> {

        /**
         * Downoalds the data as a background thread.
         * @param urls urls to download String data from.
         * @return Returns the data or an error.
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;

            for (String string: urls) {
                try {
                    URL urlObj = new URL(string);
                    urlConnection = (HttpURLConnection) urlObj.openConnection();
                    InputStream contentFromUrl = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(contentFromUrl));

                    String s = "";
                    while ((s = reader.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    Log.e("FavoriteFragment", response);
                    response = "Unable to download favorites. Reasons: " + e.getMessage();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
            return response;
        }

        @Override
        public void onPostExecute(String result) {
            //We got something wrong here
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
                return;
            }
            mFavoritesList = new ArrayList<>();
            result = Favorite.parseFavoriteJSON(result, mFavoritesList);
            if (result != null) {
                Log.e("Favorite Fragment", "Something has gone wrong with JSON.");
            }
            if (!mFavoritesList.isEmpty()) {
                mRecyclerView.setAdapter(new MyFavoriteRecyclerViewAdapter(
                        mFavoritesList, mListener));
            }
        }
    }
}
