package musicstreamingservice.entities;

import musicstreamingservice.observer.ArtistObserver;
import musicstreamingservice.observer.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Artist implements Subject {
    private final String id;
    private final String name;
    private final List<Album> discography = new ArrayList<>();
    private final Set<ArtistObserver> observers = ConcurrentHashMap.newKeySet();

    @Override
    public void addObserver(ArtistObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ArtistObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Artist artist, Album album) {
        for (ArtistObserver observer : observers) {
            observer.update(artist, album);
        }
    }

    public Artist(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public void releaseAlbum(Album album) {
        discography.add(album);
        System.out.printf("[System] Artist %s has released a new album: %s%n", name, album.getTitle());
        notifyObservers(this, album);
    }

    public String getId() { return id; }
    public String getName() { return name; }
}
