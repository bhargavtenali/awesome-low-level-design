package musicstreamingservice.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Playlist implements Playable {
    private final String id;
    private final String name;
    private final List<Song> tracks = new ArrayList<>();

    public Playlist(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public synchronized void addTrack(Song song) {
        tracks.add(song);
    }

    public synchronized void removeTrack(Song song) {
        tracks.remove(song);
    }

    @Override
    public synchronized List<Song> getTracks() {
        return List.copyOf(tracks);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}