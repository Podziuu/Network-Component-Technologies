package pl.tks.soap.endpoint;

import io.spring.guides.gs_producing_web_service.*;
import jakarta.xml.bind.JAXBElement;
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
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    private final ItemService itemService;

    @Autowired
    public ItemEndpoint(ItemService itemService) {
        this.itemService = itemService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getItemsRequest")
    @ResponsePayload
    public JAXBElement<GetItemsResponse> getItems(@RequestPayload JAXBElement<GetItemsRequest> request) {
        GetItemsResponse  response = new GetItemsResponse();
        List<Item> items = ItemMapper.toSoapItemList(itemService.getAllItems());

        response.getItems().addAll(items);

        ObjectFactory factory = new ObjectFactory();
        return factory.createGetItemsResponse(response);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addItemRequest")
    @ResponsePayload
    public JAXBElement<AddItemResponse> addItem(@RequestPayload JAXBElement<AddItemRequest> request) {
        Item item = request.getValue().getItem();
        Item addedItem = ItemMapper.toSoapItem(itemService.addItem(ItemMapper.fromSoapItem(item)));
        AddItemResponse response = new AddItemResponse();
        response.setItem(addedItem);
        response.setSuccess(true);

        ObjectFactory factory = new ObjectFactory();
        return factory.createAddItemResponse(response);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getItemsByTypeRequest")
    @ResponsePayload
    public JAXBElement<GetItemsByTypeResponse> getItemsByType(@RequestPayload JAXBElement<GetItemsByTypeRequest> request) {
        GetItemsByTypeResponse response = new GetItemsByTypeResponse();

        List<Item> items = ItemMapper.toSoapItemList(itemService.getItemsByItemType(request.getValue().getItemType()));
        response.getItems().addAll(items);

        ObjectFactory factory = new ObjectFactory();
        return factory.createGetItemsByTypeResponse(response);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getItemsByNameRequest")
    @ResponsePayload
    public JAXBElement<GetItemsByNameResponse> getItemsByName(@RequestPayload JAXBElement<GetItemsByNameRequest> request) {
        GetItemsByNameResponse response = new GetItemsByNameResponse();

        List<Item> items = ItemMapper.toSoapItemList(itemService.getItemsByItemName(request.getValue().getItemName()));

        response.getItems().addAll(items);

        ObjectFactory factory = new ObjectFactory();
        return factory.createGetItemsByNameResponse(response);
    }
}
