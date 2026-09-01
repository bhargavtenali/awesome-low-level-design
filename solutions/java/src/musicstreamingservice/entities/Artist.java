package musicstreamingservice.entities;

import musicstreamingservice.observer.ArtistObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Artist {
    private final String id;
    private final String name;
    private final List<Album> discography = new ArrayList<>();
    private final Set<ArtistObserver> followers = ConcurrentHashMap.newKeySet();

    public Artist(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public void addFollower(ArtistObserver observer) {
        followers.add(observer);
    }

    public void removeFollower(ArtistObserver observer) {
        followers.remove(observer);
    }

    public void releaseAlbum(Album album) {
        synchronized (this) {
            discography.add(album);
        }
        System.out.printf("Artist %s released album %s.%n", name, album.getTitle());
        notifyFollowers(album);
    }

    private void notifyFollowers(Album album) {
        for (ArtistObserver follower : followers) {
            follower.update(this, album);
        }
    }

    public synchronized List<Album> getDiscography() {
        return List.copyOf(discography);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}