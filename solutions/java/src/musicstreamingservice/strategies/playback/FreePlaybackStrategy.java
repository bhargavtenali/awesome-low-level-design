package musicstreamingservice.strategies.playback;

import musicstreamingservice.entities.Player;
import musicstreamingservice.entities.Song;

public class FreePlaybackStrategy implements PlaybackStrategy {

    private static final int SONGS_BEFORE_AD = 3;

    private int songsPlayed;

    public FreePlaybackStrategy() {
        this(0);
    }

    public FreePlaybackStrategy(int songsPlayed) {
        this.songsPlayed = songsPlayed;
    }

    @Override
    public synchronized void play(Song song, Player player) {
        if (songsPlayed > 0 && songsPlayed % SONGS_BEFORE_AD == 0) {
            System.out.println("Playing advertisement...");
        }

        player.setCurrentSong(song);
        songsPlayed++;
    }
}