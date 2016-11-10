package a450team3.tacoma.uw.edu.sylph;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import a450team3.tacoma.uw.edu.sylph.favorites.FavoriteActivity;


public class YoutubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    //API_KEY for the connecting with youtube API
    public static final String API_KEY = "AIzaSyBnS5M7WoEylYNk-2yqbku-8seBTMB6E6M";
    //List of Video_ID
    public List<String> mVideo_List;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // attaching layout xml
        setContentView(R.layout.activity_youtube_player);

        //Getting Extra if there is any
        Intent intent = getIntent();


        //Populating the video_List with video_id
        mVideo_List = new ArrayList<String>();
        mVideo_List.add("PT2_F-1esPk"); //The Chainsmokers - Closer
        mVideo_List.add("UprcpdwuwCg"); //twenty one pilots - Heathens
        mVideo_List.add("Pw-0pbY9JeU"); //twenty one pilots - Ride
        mVideo_List.add("RgKAFK5djSk"); //Wiz Khalifa - See You Again
        mVideo_List.add("09R8_2nJtjg"); //Maroon 5 - Sugar
        if ((intent.getStringExtra(FavoriteActivity.YOUTUBE_CODE)) != null) {
            //Add the one from favorites if it exists
            mVideo_List.add(intent.getStringExtra(FavoriteActivity.YOUTUBE_CODE));
        }


        //Help display the video id (future this will be Title and Artist)
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, mVideo_List);
//        // attaching layout xml
        ListView listView = (ListView) findViewById(R.id.video_list);
        listView.setAdapter(adapter);

        // Initializing YouTube player view
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
        youTubePlayerView.initialize(API_KEY, this);
        // Google API Client
        GoogleApiClient client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    /**
     * If initializations is Fail the display the Toast error message
     *
     * @param theProvider: (override) i am not using
     * @param theResult:   (override) i am not using
     */
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider theProvider, YouTubeInitializationResult theResult) {
        Toast.makeText(this, "Failed to initialize.", Toast.LENGTH_LONG).show();
    }

    /**
     * the initializations was success,
     * Play the content
     *
     * @param theProvider:      The provider which was used to initialize the YouTubePlayer.
     * @param theYoutubePlayer: A YouTubePlayer which can be used to control video playback in the provider.
     * @param wasRestored:      Whether the player was restored from a previously saved state,
     *                          as part of the YouTubePlayerView or YouTubePlayerFragment restoring its state
     */
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider theProvider, YouTubePlayer theYoutubePlayer, boolean wasRestored) {
        if (null == theYoutubePlayer) return;
        // Start buffering
        if (!wasRestored) {
            theYoutubePlayer.cueVideos(mVideo_List);
            getTitleOfSong("https://www.youtube.com/watch?v=RgKAFK5djSk"); //testing purpose
        }

        // Add listeners to YouTubePlayer instance
        // All the Method is Override from parents
        theYoutubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onAdStarted() {
            }

            @Override
            public void onError(YouTubePlayer.ErrorReason arg0) {
            }

            @Override
            public void onLoaded(String arg0) {
            }

            @Override
            public void onLoading() {
            }

            @Override
            public void onVideoEnded() {
            }

            @Override
            public void onVideoStarted() {
            }

        });
        // Add listeners to YouTubePlayer instance
        // All the Method is Override from parents
        theYoutubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
            //video is in buffering
            @Override
            public void onBuffering(boolean arg0) {
                Context context = getApplicationContext();
                CharSequence text = "Loading....";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            //video is in paused
            @Override
            public void onPaused() {
                Context context = getApplicationContext();
                CharSequence text = "Paused...";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            //video is in Playing display info
            @Override
            public void onPlaying() {
                Context context = getApplicationContext();
                CharSequence text = "Playing [Title]";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            @Override
            public void onSeekTo(int arg0) {
            }

            @Override
            public void onStopped() {

            }
        });
    }

    /**
     * (Not yet) Get the youtubeURL and converted into JASON object and s
     * Store JASON object (title, URL, Image URL)
     *
     * @param theYoutubeURL : Video URL
     * @return: String URL of Jason (not yet using)
     */
    public String getTitleOfSong(String theYoutubeURL) {
        try {
            if (theYoutubeURL != null) {
                URL embededURL = new URL("http://www.youtube.com/oembed?url=" +
                        theYoutubeURL + "&format=json");
                Log.i("***************", embededURL.toString());
                return "What up";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}