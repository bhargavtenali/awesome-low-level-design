package musicstreamingservice.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Album implements Playable {
    private final String id;
    private final String title;
    private final List<Song> tracks = new ArrayList<>();

    public Album(String title) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
    }

    public synchronized void addTrack(Song song) {
        tracks.add(song);
    }

    @Override
    public synchronized List<Song> getTracks() {
        return List.copyOf(tracks);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}