package musicstreamingservice;

import musicstreamingservice.entities.Album;
import musicstreamingservice.entities.Artist;
import musicstreamingservice.entities.Playable;
import musicstreamingservice.entities.Player;
import musicstreamingservice.entities.Song;
import musicstreamingservice.entities.User;
import musicstreamingservice.enums.SubscriptionTier;
import musicstreamingservice.services.SearchService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MusicStreamingSystem {

    private static final MusicStreamingSystem INSTANCE = new MusicStreamingSystem();

    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, Song> songs = new ConcurrentHashMap<>();
    private final Map<String, Artist> artists = new ConcurrentHashMap<>();
    private final Map<String, Album> albums = new ConcurrentHashMap<>();
    private final Map<String, Player> players = new ConcurrentHashMap<>();

    private final SearchService searchService = new SearchService();

    private MusicStreamingSystem() {
    }

    public static MusicStreamingSystem getInstance() {
        return INSTANCE;
    }

    public User registerUser(String name, SubscriptionTier tier) {
        User user = new User(name, tier);
        User existing = users.putIfAbsent(user.getId(), user);
        if (existing != null) {
            throw new IllegalArgumentException("User already exists: " + user.getId());
        }
        players.putIfAbsent(user.getId(), new Player());
        return user;
    }

    public Artist addArtist( String name) {
        Artist artist = new Artist(name);
        Artist existing = artists.putIfAbsent(artist.getId(), artist);
        if (existing != null) {
            throw new IllegalArgumentException("Artist already exists: " + artist.getId());
        }
        return artist;
    }

    public Album addAlbum(String name) {
        Album album = new Album(name);
        Album existing = albums.putIfAbsent(album.getId(), album);
        if (existing != null) {
            throw new IllegalArgumentException("Album already exists: " + album.getId());
        }
        return album;
    }

    public Song addSong(String title, String artistId, int durationInSeconds) {
        Artist artist = artists.get(artistId);
        if (artist == null) {
            throw new IllegalArgumentException("Artist not found: " + artistId);
        }
        Song song = new Song(title, artist, durationInSeconds);
        Song existing = songs.putIfAbsent(song.getId(), song);
        if (existing != null) {
            throw new IllegalArgumentException("Song already exists: " + song.getId());
        }
        return song;
    }

    public User getUser(String userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        return user;
    }

    public Player getPlayer(String userId) {
        Player player = players.get(userId);
        if (player == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        return player;
    }

    public void load(String userId, Playable playable) {
        User user = getUser(userId);
        getPlayer(userId).load(playable, user);
    }

    public void play(String userId) {
        getPlayer(userId).clickPlay();
    }

    public void pause(String userId) {
        getPlayer(userId).clickPause();
    }

    public void next(String userId) {
        getPlayer(userId).clickNext();
    }

    public void seek(String userId, int positionInSeconds) {
        getPlayer(userId).seek(positionInSeconds);
    }

    public void stop(String userId) {
        getPlayer(userId).clickStop();
    }

    public List<Song> searchSongsByTitle(String title) {
        return searchService.searchSongsByTitle(new ArrayList<>(songs.values()), title);
    }

    public List<Artist> searchArtistsByName(String name) {
        return searchService.searchArtistsByName(new ArrayList<>(artists.values()), name);
    }

    public List<Album> searchAlbumsByTitle(String title) {
        return searchService.searchAlbumsByTitle(new ArrayList<>(albums.values()), title);
    }

    public List<Song> recommendSongs(String userId) {
        User user = getUser(userId);
        Set<Artist> followedArtists = user.getFollowedArtists();

        return songs.values()
                .stream()
                .filter(song -> followedArtists.contains(song.getArtist()))
                .limit(10)
                .collect(Collectors.toList());
    }
}