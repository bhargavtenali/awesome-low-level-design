package musicstreamingservice.entities;

import musicstreamingservice.enums.SubscriptionTier;
import musicstreamingservice.observer.ArtistObserver;
import musicstreamingservice.strategies.playback.FreePlaybackStrategy;
import musicstreamingservice.strategies.playback.PlaybackStrategy;
import musicstreamingservice.strategies.playback.PremiumPlaybackStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class User implements ArtistObserver {
    private final String id;
    private final String name;
    private final PlaybackStrategy playbackStrategy;
    private final Set<Artist> followedArtists = ConcurrentHashMap.newKeySet();
    private final List<Playlist> playlists = new ArrayList<>();
    private final List<Song> listeningHistory = new ArrayList<>();

    public User(String name, SubscriptionTier tier) {
        this(name, tier, 0);
    }

    public User(String name, SubscriptionTier tier, int songsPlayed) {
        this.id = UUID.randomUUID().toString();
        this.name = name;

        if (tier == SubscriptionTier.PREMIUM) {
            this.playbackStrategy = new PremiumPlaybackStrategy();
        } else {
            this.playbackStrategy = new FreePlaybackStrategy(songsPlayed);
        }
    }

    public void followArtist(Artist artist) {
        if (followedArtists.add(artist)) {
            artist.addFollower(this);
        }
    }

    public void unfollowArtist(Artist artist) {
        if (followedArtists.remove(artist)) {
            artist.removeFollower(this);
        }
    }

    public synchronized Playlist createPlaylist(String name) {
        Playlist playlist = new Playlist(name);
        playlists.add(playlist);
        return playlist;
    }

    public synchronized List<Playlist> getPlaylists() {
        return List.copyOf(playlists);
    }

    public synchronized void recordPlayedSong(Song song) {
        listeningHistory.add(song);
    }

    public synchronized List<Song> getListeningHistory() {
        return List.copyOf(listeningHistory);
    }

    public Set<Artist> getFollowedArtists() {
        return Set.copyOf(followedArtists);
    }

    @Override
    public void update(Artist artist, Album album) {
        System.out.printf(
                "--- Notification for %s ---%nArtist %s released album %s.%n",
                name,
                artist.getName(),
                album.getTitle());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PlaybackStrategy getPlaybackStrategy() {
        return playbackStrategy;
    }
}