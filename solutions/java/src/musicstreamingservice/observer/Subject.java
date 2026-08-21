package musicstreamingservice.observer;

import musicstreamingservice.entities.Album;
import musicstreamingservice.entities.Artist;

import java.util.ArrayList;
import java.util.List;

public interface Subject {
    void addObserver(ArtistObserver observer);
    void removeObserver(ArtistObserver observer);
    void notifyObservers(Artist artist, Album album);
}
