package pl.tks.repos.mappers;

import pl.tks.model.item.Comics;
import pl.tks.model.item.Item;
import pl.tks.model.item.Movie;
import pl.tks.model.item.Music;
import pl.tks.repos.entities.item.ComicsEnt;
import pl.tks.repos.entities.item.ItemEnt;
import pl.tks.repos.entities.item.MovieEnt;
import pl.tks.repos.entities.item.MusicEnt;
import org.bson.types.ObjectId;

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
        ObjectId objectId = ObjectId.isValid(music.getId()) ? new ObjectId(music.getId()) : new ObjectId();
        return new MusicEnt(objectId,
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
        ObjectId objectId = ObjectId.isValid(movie.getId()) ? new ObjectId(movie.getId()) : new ObjectId();
        return new MovieEnt(objectId,
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
        ObjectId objectId = ObjectId.isValid(comics.getId()) ? new ObjectId(comics.getId()) : new ObjectId();
        return new ComicsEnt(objectId,
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
