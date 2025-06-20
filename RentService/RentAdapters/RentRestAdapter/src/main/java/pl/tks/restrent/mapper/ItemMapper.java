package pl.tks.restrent.mapper;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import pl.tks.modelrent.item.Comics;
import pl.tks.modelrent.item.Item;
import pl.tks.modelrent.item.Movie;
import pl.tks.modelrent.item.Music;
import pl.tks.restrent.dto.ComicsDTO;
import pl.tks.restrent.dto.ItemDTO;
import pl.tks.restrent.dto.MovieDTO;
import pl.tks.restrent.dto.MusicDTO;
import pl.tks.servicerent.exception.InvalidItemTypeException;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class ItemMapper {
    public static Music toMusic(MusicDTO dto) {
        return new Music(dto.getId() != null ? String.valueOf(new ObjectId(dto.getId())) : null,
                dto.getBasePrice(),
                dto.getItemName(),
                dto.isAvailable(),
                dto.getGenre(),
                dto.isVinyl());
    }

    public static Movie toMovie(MovieDTO dto) {
        return new Movie(dto.getId() != null ? String.valueOf(new ObjectId(dto.getId())) : null,
                dto.getBasePrice(),
                dto.getItemName(),
                dto.isAvailable(),
                dto.getMinutes(),
                dto.isCasette());
    }

    public static Comics toComics(ComicsDTO dto) {
        return new Comics(dto.getId() != null ? String.valueOf(new ObjectId(dto.getId())) : null,
                dto.getBasePrice(),
                dto.getItemName(),
                dto.isAvailable(),
                dto.getPagesNumber(),
                dto.getPublisher());
    }

    public static MusicDTO toMusicDTO(Music music) {
        return new MusicDTO(
                music.getId().toString(),
                music.getBasePrice(),
                music.getItemName(),
                music.isAvailable(),
                music.getGenre(),
                music.isVinyl()
        );
    }

    public static MovieDTO toMovieDTO(Movie movie) {
        return new MovieDTO(
                movie.getId().toString(),
                movie.getBasePrice(),
                movie.getItemName(),
                movie.isAvailable(),
                movie.getMinutes(),
                movie.isCasette()
        );
    }

    public static ComicsDTO toComicsDTO(Comics comics) {
        return new ComicsDTO(
                comics.getId().toString(),
                comics.getBasePrice(),
                comics.getItemName(),
                comics.isAvailable(),
                comics.getPageNumber(),
                comics.getPublisher()
        );
    }

    public static ItemDTO toDTO(Item item) {
        if (item instanceof Music music) {
            return new MusicDTO(
                    item.getId().toString(),
                    item.getBasePrice(),
                    item.getItemName(),
                    item.isAvailable(),
                    music.getGenre(),
                    music.isVinyl()
            );
        } else if (item instanceof Movie movie) {
            return new MovieDTO(
                    item.getId().toString(),
                    item.getBasePrice(),
                    item.getItemName(),
                    item.isAvailable(),
                    movie.getMinutes(),
                    movie.isCasette()
            );
        } else {
            Comics comics = (Comics) item;
            return new ComicsDTO(
                    item.getId().toString(),
                    item.getBasePrice(),
                    item.getItemName(),
                    item.isAvailable(),
                    comics.getPageNumber(),
                    comics.getPublisher()
            );
        }
    }

    public static Item toItem(ItemDTO itemDTO) {
        return switch (itemDTO.getItemType().toLowerCase()) {
            case "music" -> toMusic((MusicDTO) itemDTO);
            case "movie" -> toMovie((MovieDTO) itemDTO);
            case "comics" -> toComics((ComicsDTO) itemDTO);
            default -> throw new InvalidItemTypeException("Nieznany type: " + itemDTO.getItemType());
        };
    }

    public static List<ItemDTO> toDTOList(List<Item> items) {
        return items.stream().map(ItemMapper::toDTO).collect(Collectors.toList());
    }
}
