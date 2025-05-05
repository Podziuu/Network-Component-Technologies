package pl.tks.reposrent.mappers;

import org.bson.types.ObjectId;
import pl.tks.modelrent.item.Comics;
import pl.tks.modelrent.item.Item;
import pl.tks.modelrent.item.Movie;
import pl.tks.modelrent.item.Music;
import pl.tks.reposrent.entities.item.ComicsEnt;
import pl.tks.reposrent.entities.item.ItemEnt;
import pl.tks.reposrent.entities.item.MovieEnt;
import pl.tks.reposrent.entities.item.MusicEnt;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static Music toMusic(MusicEnt musicEnt) {
        return new Music(musicEnt.getId().toHexString(),
                musicEnt.getBasePrice(),
                musicEnt.getItemName(),
                musicEnt.isAvailable(),
                musicEnt.getGenre(),
                musicEnt.isVinyl());
    }

    public static MusicEnt toMusicEnt(Music music) {
        return new MusicEnt(new ObjectId(),
                music.getBasePrice(),
                music.getItemName(),
                music.isAvailable(),
                music.getGenre(),
                music.isVinyl());
    }

    public static Movie toMovie(MovieEnt movieEnt) {
        return new Movie(movieEnt.getId().toHexString(),
                movieEnt.getBasePrice(),
                movieEnt.getItemName(),
                movieEnt.isAvailable(),
                movieEnt.getMinutes(),
                movieEnt.isCasette());
    }

    public static MovieEnt toMovieEnt(Movie movie) {
        return new MovieEnt(new ObjectId(),
                movie.getBasePrice(),
                movie.getItemName(),
                movie.isAvailable(),
                movie.getMinutes(),
                movie.isCasette());
    }

    public static Comics toComics(ComicsEnt comicsEnt) {
        return new Comics(comicsEnt.getId().toHexString(),
                comicsEnt.getBasePrice(),
                comicsEnt.getItemName(),
                comicsEnt.isAvailable(),
                comicsEnt.getPageNumber(),
                comicsEnt.getPublisher());
    }

    public static ComicsEnt toComicsEnt(Comics comics) {
        return new ComicsEnt(new ObjectId(),
                comics.getBasePrice(),
                comics.getItemName(),
                comics.isAvailable(),
                comics.getPageNumber(),
                comics.getPublisher());
    }

    public static Item toItem(ItemEnt itemEnt) {
        return switch (itemEnt.getItemType().toLowerCase()) {
            case "comics" -> toComics((ComicsEnt) itemEnt);
            case "music" -> toMusic((MusicEnt) itemEnt);
            case "movie" -> toMovie((MovieEnt) itemEnt);
            default -> throw new RuntimeException(itemEnt.getItemType());
        };
    }

    public static ItemEnt toItemEnt(Item item) {
        return switch (item.getItemType().toLowerCase()) {
            case "comics" -> toComicsEnt((Comics) item);
            case "music" -> toMusicEnt((Music) item);
            case "movie" -> toMovieEnt((Movie) item);
            default -> throw new RuntimeException(item.getItemType());
        };
    }

    public static List<Item> toItemList(List<ItemEnt> items) {
        return items.stream().map(ItemMapper::toItem).collect(Collectors.toList());
    }
}
