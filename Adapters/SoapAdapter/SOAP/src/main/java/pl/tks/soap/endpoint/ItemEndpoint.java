package pl.tks.soap.endpoint;

import generated.*;
import jakarta.xml.bind.JAXBElement;
import pl.tks.items.ObjectFactory;
import pl.tks.soap.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.tks.service.services.ItemService;

import java.util.List;

@Endpoint
public class ItemEndpoint {
    private static final String NAMESPACE_URI = "http://tks.pl/items";

    private final ItemService itemService;

    @Autowired
    public ItemEndpoint(ItemService itemService) {
        this.itemService = itemService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getItemsRequest")
    @ResponsePayload
    public JAXBElement<GetItemsResponse> getItems(@RequestPayload JAXBElement<GetItemsRequest> getItemsRequest) {
        List<pl.tks.model.item.Item> items = itemService.getAllItems();
        GetItemsResponse response = new GetItemsResponse();
        response.getItems().addAll(ItemMapper.toSoapItemList(items));
        ObjectFactory factory = new ObjectFactory();
        return factory.createGetItemsResponse(response);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addComicsRequest")
    @ResponsePayload
    public JAXBElement<AddComicsResponse> addComics(@RequestPayload JAXBElement<AddComicsRequest> request) {
        AddComicsRequest requestObj = request.getValue();
        Comics comics = new Comics();
        comics.setBasePrice(requestObj.getBasePrice());
        comics.setItemName(requestObj.getItemName());
        comics.setAvailable(requestObj.isAvailable());
        comics.setItemType(requestObj.getItemType());
        comics.setPageNumber(requestObj.getPageNumber());
        comics.setPublisher(requestObj.getPublisher());
        pl.tks.model.item.Item item = itemService.addItem(ItemMapper.fromSoapItem(comics));
        AddComicsResponse response = new AddComicsResponse();
        response.setItem((Comics) ItemMapper.toSoapItem(item));
        ObjectFactory factory = new ObjectFactory();
        return factory.createAddComicsResponse(response);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addMovieRequest")
    @ResponsePayload
    public JAXBElement<AddMovieResponse> addMovie(@RequestPayload JAXBElement<AddMovieRequest> request) {
        AddMovieRequest requestObj = request.getValue();
        Movie movie = new Movie();
        movie.setBasePrice(requestObj.getBasePrice());
        movie.setItemName(requestObj.getItemName());
        movie.setAvailable(requestObj.isAvailable());
        movie.setItemType(requestObj.getItemType());
        movie.setMinutes(requestObj.getMinutes());
        movie.setCasette(requestObj.isCasette());
        pl.tks.model.item.Item item = itemService.addItem(ItemMapper.fromSoapItem(movie));
        AddMovieResponse response = new AddMovieResponse();
        response.setItem((Movie) ItemMapper.toSoapItem(item));
        ObjectFactory factory = new ObjectFactory();
        return factory.createAddMovieResponse(response);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addMusicRequest")
    @ResponsePayload
    public JAXBElement<AddMusicResponse> addMusic(@RequestPayload JAXBElement<AddMusicRequest> request) {
        AddMusicRequest requestObj = request.getValue();
        Music music = new Music();
        music.setBasePrice(requestObj.getBasePrice());
        music.setItemName(requestObj.getItemName());
        music.setAvailable(requestObj.isAvailable());
        music.setItemType(requestObj.getItemType());
        music.setGenre(requestObj.getGenre());
        music.setVinyl(requestObj.isVinyl());
        pl.tks.model.item.Item item = itemService.addItem(ItemMapper.fromSoapItem(music));
        AddMusicResponse response = new AddMusicResponse();
        response.setItem((Music) ItemMapper.toSoapItem(item));
        ObjectFactory factory = new ObjectFactory();
        return factory.createAddMusicResponse(response);
    }

//    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getItemsByTypeRequest")
//    @ResponsePayload
//    public JAXBElement<GetItemsByTypeResponse> getItemsByType(@RequestPayload JAXBElement<GetItemsByTypeRequest> request) {
//        GetItemsByTypeResponse response = new GetItemsByTypeResponse();
//
//        List<Item> items = ItemMapper.toSoapItemList(itemService.getItemsByItemType(request.getValue().getItemType()));
//        response.getItems().addAll(items);
//
//        ObjectFactory factory = new ObjectFactory();
//        return factory.createGetItemsByTypeResponse(response);
//    }
//
//    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getItemsByNameRequest")
//    @ResponsePayload
//    public JAXBElement<GetItemsByNameResponse> getItemsByName(@RequestPayload JAXBElement<GetItemsByNameRequest> request) {
//        GetItemsByNameResponse response = new GetItemsByNameResponse();
//
//        List<Item> items = ItemMapper.toSoapItemList(itemService.getItemsByItemName(request.getValue().getItemName()));
//
//        response.getItems().addAll(items);
//
//        ObjectFactory factory = new ObjectFactory();
//        return factory.createGetItemsByNameResponse(response);
//    }
}
