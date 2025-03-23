package mappers;

import entities.item.ComicsEnt;
import entities.item.ItemEnt;
import entities.item.MovieEnt;
import entities.item.MusicEnt;
import exception.InvalidItemTypeException;
import model.item.*;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static Music toMusic(MusicEnt musicEnt) {
        return new Music(musicEnt.getId().toString(),
                musicEnt.getBasePrice(),
                musicEnt.getItemName(),
                musicEnt.isAvailable(),
                musicEnt.getGenre(),
                musicEnt.isVinyl());
    }

    public static MusicEnt toMusicEnt(Music music) {
        return new MusicEnt(new ObjectId(music.getId()),
                music.getBasePrice(),
                music.getItemName(),
                music.isAvailable(),
                music.getGenre(),
                music.isVinyl());
    }

    public static Movie toMovie(MovieEnt movieEnt) {
        return new Movie(movieEnt.getId().toString(),
                movieEnt.getBasePrice(),
                movieEnt.getItemName(),
                movieEnt.isAvailable(),
                movieEnt.getMinutes(),
                movieEnt.isCasette());
    }

    public static MovieEnt toMovieEnt(Movie movie) {
        return new MovieEnt(new ObjectId(movie.getId()),
                movie.getBasePrice(),
                movie.getItemName(),
                movie.isAvailable(),
                movie.getMinutes(),
                movie.isCasette());
    }

    public static Comics toComics(ComicsEnt comicsEnt) {
        return new Comics(comicsEnt.getId().toString(),
                comicsEnt.getBasePrice(),
                comicsEnt.getItemName(),
                comicsEnt.isAvailable(),
                comicsEnt.getPageNumber(),
                comicsEnt.getPublisher());
    }

    public static ComicsEnt toComicsEnt(Comics comics) {
        return new ComicsEnt(new ObjectId(comics.getId()),
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
            default -> throw new InvalidItemTypeException(itemEnt.getItemType());
        };
    }

    public static ItemEnt toItemEnt(Item item) {
        return switch (item.getItemType().toLowerCase()) {
            case "comics" -> toComicsEnt((Comics) item);
            case "music" -> toMusicEnt((Music) item);
            case "movie" -> toMovieEnt((Movie) item);
            default -> throw new InvalidItemTypeException(item.getItemType());
        };
    }

    public static List<Item> toItemList(List<ItemEnt> items) {
        return items.stream().map(ItemMapper::toItem).collect(Collectors.toList());
    }
}
