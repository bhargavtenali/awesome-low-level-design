package musicstreamingservice;

import musicstreamingservice.entities.Album;
import musicstreamingservice.entities.Artist;
import musicstreamingservice.entities.Playlist;
import musicstreamingservice.entities.Song;
import musicstreamingservice.entities.User;
import musicstreamingservice.enums.SubscriptionTier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MusicStreamingDemo {

    public static void main(String[] args) throws Exception {
        MusicStreamingSystem system = MusicStreamingSystem.getInstance();

        Artist daftPunk = system.addArtist("Daft Punk");
        Album discovery = system.addAlbum("Discovery");
        Song timeSong = system.addSong("One More Time", daftPunk.getId(), 320);
        Song strongerSong = system.addSong("Harder Better Faster Stronger", daftPunk.getId(),225);
        Song loveSong = system.addSong("Digital Love", daftPunk.getId(), 300);
        discovery.addTrack(timeSong);
        discovery.addTrack(strongerSong);
        discovery.addTrack(loveSong);

        User alice = system.registerUser("Alice", SubscriptionTier.FREE);
        User bob = system.registerUser("Bob", SubscriptionTier.PREMIUM);
        alice.followArtist(daftPunk);
        bob.followArtist(daftPunk);
        daftPunk.releaseAlbum(discovery);

        System.out.println("\n=== Alice Playback ===");
        Playlist alicePlaylist = alice.createPlaylist("Favorites");
        discovery.getTracks().forEach(alicePlaylist::addTrack);
        system.load(alice.getId(), alicePlaylist);
        system.play(alice.getId());
        system.seek(alice.getId(), 30);
        system.next(alice.getId());
        system.pause(alice.getId());

        //FIXME: Not Needed
        System.out.println("\n=== Concurrent Playback ===");
        system.load(alice.getId(), discovery);
        system.load(bob.getId(), discovery);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            Future<?> aliceTask = executor.submit(() -> {
                system.play(alice.getId());
                system.next(alice.getId());
            });
            Future<?> bobTask = executor.submit(() -> {
                system.play(bob.getId());
                system.next(bob.getId());
            });
            aliceTask.get();
            bobTask.get();
        } finally {
            executor.shutdown();
        }
    }
}