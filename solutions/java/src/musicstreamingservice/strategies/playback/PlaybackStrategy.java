package musicstreamingservice.strategies.playback;

import musicstreamingservice.enums.SubscriptionTier;
import musicstreamingservice.entities.Player;
import musicstreamingservice.entities.Song;

public interface PlaybackStrategy {
    void play(Song song, Player player);
}
