package pl.tks.soap.mapper;

import generated.*;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {

    // Główna metoda mapująca ItemEnt na odpowiedni typ SOAP
    public static Item toSoapItem(pl.tks.modelrent.item.Item item) {
        return switch(item.getItemType().toLowerCase()) {
            case "music" -> toMusicSoap((pl.tks.modelrent.item.Music) item);
            case "movie" -> toMovieSoap((pl.tks.modelrent.item.Movie) item);
            case "comics" -> toComicsSoap((pl.tks.modelrent.item.Comics) item);
            default -> throw new IllegalStateException("Unexpected value: " + item.getItemType());
        };
    }

    // Maper dla Music
    private static Music toMusicSoap(pl.tks.modelrent.item.Music music) {
        Music soapMusic = new Music();
        soapMusic.setId(music.getId());
        soapMusic.setItemName(music.getItemName());
        soapMusic.setBasePrice(music.getBasePrice());
        soapMusic.setAvailable(music.isAvailable());
        soapMusic.setGenre(music.getGenre().name());
        soapMusic.setVinyl(music.isVinyl());
        return soapMusic;
    }

    // Maper dla Movie
    private static Movie toMovieSoap(pl.tks.modelrent.item.Movie movie) {
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
    private static Comics toComicsSoap(pl.tks.modelrent.item.Comics comics) {
        Comics soapComics = new Comics();
        soapComics.setId(comics.getId());
        soapComics.setItemName(comics.getItemName());
        soapComics.setBasePrice(comics.getBasePrice());
        soapComics.setAvailable(comics.isAvailable());
        soapComics.setPageNumber(comics.getPageNumber());
        soapComics.setPublisher(comics.getPublisher());
        return soapComics;
    }

    public static List<Item> toSoapItemList(List<pl.tks.modelrent.item.Item> items) {
        return items.stream().map(ItemMapper::toSoapItem).collect(Collectors.toList());
    }

    public static pl.tks.modelrent.item.Item fromSoapItem(Item item) {
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
    private static pl.tks.modelrent.item.Music fromMusicSoap(Music soapMusic) {
        pl.tks.modelrent.item.Music music = new pl.tks.modelrent.item.Music();
        music.setId(soapMusic.getId());
        music.setItemName(soapMusic.getItemName());
        music.setBasePrice(soapMusic.getBasePrice());
        music.setAvailable(soapMusic.isAvailable());
        music.setGenre(pl.tks.modelrent.item.MusicGenre.valueOf(soapMusic.getGenre()));
        music.setVinyl(soapMusic.isVinyl());
        music.setItemType(soapMusic.getItemType());
        return music;
    }

    // Z SOAP -> model.item.Movie
    private static pl.tks.modelrent.item.Movie fromMovieSoap(Movie soapMovie) {
        pl.tks.modelrent.item.Movie movie = new pl.tks.modelrent.item.Movie();
        movie.setId(soapMovie.getId());
        movie.setItemName(soapMovie.getItemName());
        movie.setBasePrice(soapMovie.getBasePrice());
        movie.setAvailable(soapMovie.isAvailable());
        movie.setMinutes(soapMovie.getMinutes());
        movie.setCasette(soapMovie.isCasette());
        movie.setItemType(soapMovie.getItemType());
        return movie;
    }

    // Z SOAP -> model.item.Comics
    private static pl.tks.modelrent.item.Comics fromComicsSoap(Comics soapComics) {
        pl.tks.modelrent.item.Comics comics = new pl.tks.modelrent.item.Comics();
        comics.setId(soapComics.getId());
        comics.setItemName(soapComics.getItemName());
        comics.setBasePrice(soapComics.getBasePrice());
        comics.setAvailable(soapComics.isAvailable());
        comics.setPageNumber(soapComics.getPageNumber());
        comics.setPublisher(soapComics.getPublisher());
        comics.setItemType(soapComics.getItemType());
        return comics;
    }

    public static List<pl.tks.modelrent.item.Item> fromSoapItemList(List<Item> items) {
        return items.stream().map(ItemMapper::fromSoapItem).collect(Collectors.toList());
    }
}
