package a450team3.tacoma.uw.edu.sylph.favorites;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * A favorite object will handle the data stored on a database,
 * for a list of favorites specified by the user's account.
 * Created by andyb on 11/9/2016.
 */

public class Favorite {

    /** String to be used in JSON to retrieve account. */
    public static final String ACCOUNT = "Account";
    /** String to be used in JSON to retrieve title. */
    public static final String TITLE = "Title";
    /** String to be used in JSON to retrieve description. */
    public static final String DESCRIPTION = "Description";
    /** String to be used in JSON to retrieve the url. */
    public static final String URL = "URL";

    /** Account this favorite belongs to */
    private String account;

    /** Title of the media */
    private String title;

    /** Description of the media. */
    private String description;

    /** Url of the media. Should be in form of a youtube.com url */
    private String url;

    /**
     * Returns the Account.
     * @return The user's account.
     */
    public String getAccount() {
        return account;
    }

    /**
     * Returns the media's title.
     * @return The media's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the media's description.
     * @return The media's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the url.
     * @return The url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * A favorite will hold the informaton about a user's favorites. Will be
     * held in a list fragment.
     * @param theAccount The user's account.
     * @param theTitle The media title.
     * @param theDescription The media description.
     * @param theURL The media's url.
     */
    public Favorite (String theAccount, String theTitle,
                     String theDescription, String theURL) {
        account = theAccount;
        title = theTitle;
        description = theDescription;
        url = theURL;
    }

    /**
     * Parses the JSON recieved into a Favorite Object
     * @param favoiteJSON The string holding JSON data
     * @param favoriteList List to be added to on Favorite Fragment
     * @return Null if Successful.
     */
    public static String parseFavoriteJSON(String favoiteJSON, List<Favorite> favoriteList) {
        String reason = null;

        if( favoiteJSON != null) {
            try {
                JSONArray array = new JSONArray(favoiteJSON);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject jObj = array.getJSONObject(i);
                    Favorite fav = new Favorite(jObj.getString(ACCOUNT), jObj.getString(TITLE),
                            jObj.getString(DESCRIPTION), jObj.getString(URL));
                    favoriteList.add(fav);

                }
            } catch (Exception e) {
                reason = "Unable to add to list " + e.getMessage();
            }
        }
        return reason;
    }

}
