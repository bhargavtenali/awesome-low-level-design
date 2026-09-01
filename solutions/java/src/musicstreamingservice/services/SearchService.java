package musicstreamingservice.services;

import musicstreamingservice.entities.Album;
import musicstreamingservice.entities.Artist;
import musicstreamingservice.entities.Song;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SearchService {

    public List<Song> searchSongsByTitle(List<Song> songs, String title) {
        String query = title.toLowerCase();

        return songs.stream()
                .filter(song ->
                        song.getTitle().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }

    public List<Artist> searchArtistsByName(List<Artist> artists, String name) {
        String query = name.toLowerCase();

        return artists.stream()
                .filter(artist ->
                        artist.getName().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }

    public List<Album> searchAlbumsByTitle(List<Album> albums, String title) {
        String query = title.toLowerCase();

        return albums.stream()
                .filter(album ->
                        album.getTitle().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }
}