package it.p.lodz.pl.mapper;

import io.spring.guides.gs_producing_web_service.*;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {

    // Główna metoda mapująca ItemEnt na odpowiedni typ SOAP
    public static Item toSoapItem(model.item.Item item) {
        return switch(item.getItemType().toLowerCase()) {
            case "music" -> toMusicSoap((model.item.Music) item);
            case "movie" -> toMovieSoap((model.item.Movie) item);
            case "comic" -> toComicsSoap((model.item.Comics) item);
            default -> throw new IllegalStateException("Unexpected value: " + item.getItemType());
        };
    }

    // Maper dla Music
    private static Music toMusicSoap(model.item.Music music) {
        Music soapMusic = new Music();
        soapMusic.setId(music.getId());
        soapMusic.setItemName(music.getItemName());
        soapMusic.setBasePrice(music.getBasePrice());
        soapMusic.setAvailable(music.isAvailable());
        soapMusic.setGenre(MusicGenre.fromValue(music.getGenre().name()));
        soapMusic.setVinyl(music.isVinyl());
        return soapMusic;
    }

    // Maper dla Movie
    private static Movie toMovieSoap(model.item.Movie movie) {
        Movie soapMovie = new Movie();
        soapMovie.setId(movie.getId());
        soapMovie.setItemName(movie.getItemName());
        soapMovie.setBasePrice(movie.getBasePrice());
        soapMovie.setAvailable(movie.isAvailable());
        soapMovie.setMinutes(movie.getMinutes());
        soapMovie.setCasette(movie.isCasette());
        return soapMovie;
    }

    // Maper dla Comics
    private static Comics toComicsSoap(model.item.Comics comics) {
        Comics soapComics = new Comics();
        soapComics.setId(comics.getId());
        soapComics.setItemName(comics.getItemName());
        soapComics.setBasePrice(comics.getBasePrice());
        soapComics.setAvailable(comics.isAvailable());
        soapComics.setPageNumber(comics.getPageNumber());
        soapComics.setPublisher(comics.getPublisher());
        return soapComics;
    }

    public static List<Item> toSoapItemList(List<model.item.Item> items) {
        return items.stream().map(ItemMapper::toSoapItem).collect(Collectors.toList());
    }

    public static model.item.Item fromSoapItem(Item item) {
        if (item instanceof Music music) {
            return fromMusicSoap(music);
        } else if (item instanceof Movie movie) {
            return fromMovieSoap(movie);
        } else if (item instanceof Comics comics) {
            return fromComicsSoap(comics);
        } else {
            throw new IllegalArgumentException("Unknown SOAP Item type: " + item.getClass());
        }
    }

    // Z SOAP -> model.item.Music
    private static model.item.Music fromMusicSoap(Music soapMusic) {
        model.item.Music music = new model.item.Music();
        music.setId(soapMusic.getId());
        music.setItemName(soapMusic.getItemName());
        music.setBasePrice(soapMusic.getBasePrice());
        music.setAvailable(soapMusic.isAvailable());
        music.setGenre(model.item.MusicGenre.valueOf(soapMusic.getGenre().value()));
        music.setVinyl(soapMusic.isVinyl());
        return music;
    }

    // Z SOAP -> model.item.Movie
    private static model.item.Movie fromMovieSoap(Movie soapMovie) {
        model.item.Movie movie = new model.item.Movie();
        movie.setId(soapMovie.getId());
        movie.setItemName(soapMovie.getItemName());
        movie.setBasePrice(soapMovie.getBasePrice());
        movie.setAvailable(soapMovie.isAvailable());
        movie.setMinutes(soapMovie.getMinutes());
        movie.setCasette(soapMovie.isCasette());
        return movie;
    }

    // Z SOAP -> model.item.Comics
    private static model.item.Comics fromComicsSoap(Comics soapComics) {
        model.item.Comics comics = new model.item.Comics();
        comics.setId(soapComics.getId());
        comics.setItemName(soapComics.getItemName());
        comics.setBasePrice(soapComics.getBasePrice());
        comics.setAvailable(soapComics.isAvailable());
        comics.setPageNumber(soapComics.getPageNumber());
        comics.setPublisher(soapComics.getPublisher());
        return comics;
    }

    public static List<model.item.Item> fromSoapItemList(List<Item> items) {
        return items.stream().map(ItemMapper::fromSoapItem).collect(Collectors.toList());
    }
}
