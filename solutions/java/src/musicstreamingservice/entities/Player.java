package musicstreamingservice.entities;

import musicstreamingservice.enums.PlayerStatus;
import musicstreamingservice.state.PlayerState;
import musicstreamingservice.state.StoppedState;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private PlayerState state;
    private PlayerStatus status;
    private List<Song> queue = new ArrayList<>();
    private int currentIndex = -1;
    private Song currentSong;
    private User currentUser;
    private int currentPositionInSeconds;

    public Player() {
        this.state = new StoppedState();
        this.status = PlayerStatus.STOPPED;
    }

    public synchronized void load(Playable playable, User user) {
        this.currentUser = user;
        this.queue = new ArrayList<>(playable.getTracks());
        this.currentIndex = queue.isEmpty() ? -1 : 0;
        this.currentSong = null;
        this.currentPositionInSeconds = 0;
        changeState(new StoppedState());
        setStatus(PlayerStatus.STOPPED);
        System.out.println("Loaded " + queue.size() + " track(s).");
    }

    public synchronized void playCurrentSongInQueue() {
        if (currentIndex < 0 || currentIndex >= queue.size()) {
            System.out.println("No song available to play.");
            return;
        }
        Song songToPlay = queue.get(currentIndex);
        currentUser.getPlaybackStrategy().play(songToPlay, this);
    }

    public synchronized void clickPlay() {
        state.play(this);
    }

    public synchronized void clickPause() {
        state.pause(this);
    }

    public synchronized void clickNext() {
        state.next(this);
    }

    public synchronized void clickStop() {
        state.stop(this);
    }

    public synchronized boolean hasQueue() {
        return !queue.isEmpty();
    }

    public synchronized boolean hasNextTrack() {
        return currentIndex + 1 < queue.size();
    }

    public synchronized void moveToNextTrack() {
        if (hasNextTrack()) {
            currentIndex++;
            currentPositionInSeconds = 0;
        }
    }

    public synchronized void seek(int positionInSeconds) {
        if (currentSong == null) {
            throw new IllegalStateException("No song is currently playing.");
        }
        if (positionInSeconds < 0 || positionInSeconds > currentSong.getDurationInSeconds()) {
            throw new IllegalArgumentException("Invalid seek position.");
        }
        currentPositionInSeconds = positionInSeconds;
        System.out.printf(
                "Seeked %s to %d seconds.%n",
                currentSong.getTitle(),
                positionInSeconds);
    }

    public synchronized void changeState(PlayerState state) {
        this.state = state;
    }

    public synchronized void setStatus(PlayerStatus status) {
        this.status = status;
        System.out.println("Player status: " + status);
    }

    public synchronized void setCurrentSong(Song song) {
        this.currentSong = song;
        this.currentPositionInSeconds = 0;
        if (currentUser != null) {
            currentUser.recordPlayedSong(song);
        }
        System.out.println("Now playing: " + song);
    }

    public synchronized PlayerStatus getStatus() {
        return status;
    }

    public synchronized PlayerState getState() {
        return state;
    }

    public synchronized Song getCurrentSong() {
        return currentSong;
    }

    public synchronized int getCurrentPositionInSeconds() {
        return currentPositionInSeconds;
    }
}