package net.trials;

/**
 * Created by pranavsethi on 05/04/15.
 */
public interface Config {
    static final String SCHOOL_RSS = "http://dpsrkp.net/dpsrkpAndroidApp/webview-root/v3/rss/?feed=";
    static final String BASE_SERVER = "http://impranav.ddns.net:8080/";
    static final String IMAGE_PULL_BASE = "http://dpsrkp.net/images/stu/";
    // CONSTANTS
    static final String YOUR_SERVER_URL =  BASE_SERVER + "experiments/gcm/register.php";
    // YOUR_SERVER_URL : Server url where you have placed your server files
    // Google project id
    static final String GOOGLE_SENDER_ID = "949658796779";  // Place here your Google project id

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM Android Example";

    static final String DISPLAY_MESSAGE_ACTION =
            "net.trials.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";


}
