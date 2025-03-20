package mappers;

import dto.ComicsEntDTO;
import dto.ItemEntDTO;
import dto.MovieEntDTO;
import dto.MusicEntDTO;
import entities.item.ComicsEnt;
import entities.item.ItemEnt;
import entities.item.MovieEnt;
import entities.item.MusicEnt;
import exception.InvalidItemTypeException;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static MusicEnt toMusic(MusicEntDTO dto) {
        return new MusicEnt(dto.getId() != null ? new ObjectId(dto.getId()) : null,
                dto.getBasePrice(),
                dto.getItemName(),
                dto.isAvailable(),
                dto.getGenre(),
                dto.isVinyl());
    }

    public static MovieEnt toMovie(MovieEntDTO dto) {
        return new MovieEnt(dto.getId() != null ? new ObjectId(dto.getId()) : null,
                dto.getBasePrice(),
                dto.getItemName(),
                dto.isAvailable(),
                dto.getMinutes(),
                dto.isCasette());
    }

    public static ComicsEnt toComics(ComicsEntDTO dto) {
        return new ComicsEnt(dto.getId() != null ? new ObjectId(dto.getId()) : null,
                dto.getBasePrice(),
                dto.getItemName(),
                dto.isAvailable(),
                dto.getPagesNumber(),
                dto.getPublisher());
    }

    public static MusicEntDTO toMusicDTO(MusicEnt music) {
        return new MusicEntDTO(
                music.getId().toString(),
                music.getBasePrice(),
                music.getItemName(),
                music.isAvailable(),
                music.getGenre(),
                music.isVinyl()
        );
    }

    public static MovieEntDTO toMovieDTO(MovieEnt movie) {
        return new MovieEntDTO(
                movie.getId().toString(),
                movie.getBasePrice(),
                movie.getItemName(),
                movie.isAvailable(),
                movie.getMinutes(),
                movie.isCasette()
        );
    }

    public static ComicsEntDTO toComicsDTO(ComicsEnt comics) {
        return new ComicsEntDTO(
                comics.getId().toString(),
                comics.getBasePrice(),
                comics.getItemName(),
                comics.isAvailable(),
                comics.getPageNumber(),
                comics.getPublisher()
        );
    }

    public static ItemEntDTO toDTO(ItemEnt item) {
        if (item instanceof MusicEnt music) {
            return new MusicEntDTO(
                    item.getId().toString(),
                    item.getBasePrice(),
                    item.getItemName(),
                    item.isAvailable(),
                    music.getGenre(),
                    music.isVinyl()
            );
        } else if (item instanceof MovieEnt movie) {
            return new MovieEntDTO(
                    item.getId().toString(),
                    item.getBasePrice(),
                    item.getItemName(),
                    item.isAvailable(),
                    movie.getMinutes(),
                    movie.isCasette()
            );
        } else {
            ComicsEnt comics = (ComicsEnt) item;
            return new ComicsEntDTO(
                    item.getId().toString(),
                    item.getBasePrice(),
                    item.getItemName(),
                    item.isAvailable(),
                    comics.getPageNumber(),
                    comics.getPublisher()
            );
        }
    }

    public static ItemEnt toItem(ItemEntDTO itemDTO) {
        return switch (itemDTO.getItemType().toLowerCase()) {
            case "music" -> toMusic((MusicEntDTO) itemDTO);
            case "movie" -> toMovie((MovieEntDTO) itemDTO);
            case "comics" -> toComics((ComicsEntDTO) itemDTO);
            default -> throw new InvalidItemTypeException("Nieznany type: " + itemDTO.getItemType());
        };
    }

    public static List<ItemEntDTO> toDTOList(List<ItemEnt> items) {
        return items.stream().map(ItemMapper::toDTO).collect(Collectors.toList());
    }
}