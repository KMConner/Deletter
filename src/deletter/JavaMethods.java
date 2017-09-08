package deletter;

import twitter4j.TwitterStream;

public class JavaMethods {
    /**
     * Add Stream listener to the specified TwitterStream object.
     * <p>
     * If this operation is in kotlin, IllegalAccessError will thrown.
     * It seems to be a bug in kotlin compiler.
     *
     * @param stream Twitter Stream to add listener to.
     */
    public static void addListener(TwitterStream stream) {
        stream.addListener(new MyUserStreamListener());
    }
}
