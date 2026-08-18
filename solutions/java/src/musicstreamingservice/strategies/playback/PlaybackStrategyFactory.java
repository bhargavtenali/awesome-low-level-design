package musicstreamingservice.strategies.playback;

import musicstreamingservice.enums.SubscriptionTier;

public class PlaybackStrategyFactory {

    public static PlaybackStrategy getStrategy(SubscriptionTier tier, int songsPlayed) {
        if (tier == SubscriptionTier.PREMIUM) {
            return new PremiumPlaybackStrategy();
        }
        return new FreePlaybackStrategy(songsPlayed);
    }
}
