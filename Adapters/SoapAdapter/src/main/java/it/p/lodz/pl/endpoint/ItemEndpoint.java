package it.p.lodz.pl.endpoint;

import io.spring.guides.gs_producing_web_service.*;
import it.p.lodz.pl.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import services.ItemService;

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
    public GetItemsResponse getItems(@RequestPayload GetItemsRequest request) {
        GetItemsResponse  response = new GetItemsResponse();
        List<Item> items = ItemMapper.toSoapItemList(itemService.getAllItems());

        response.getItems().addAll(items);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addItemRequest")
    @ResponsePayload
    public AddItemResponse addItem(@RequestPayload AddItemRequest request) {
        Item item = request.getItem();
        Item addedItem = ItemMapper.toSoapItem(itemService.addItem(ItemMapper.fromSoapItem(item)));
        AddItemResponse response = new AddItemResponse();
        response.setItem(addedItem);
        response.setSuccess(true);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getItemsByTypeRequest")
    @ResponsePayload
    public GetItemsByTypeResponse getItemsByType(@RequestPayload GetItemsByTypeRequest request) {
        GetItemsByTypeResponse response = new GetItemsByTypeResponse();

        List<Item> items = ItemMapper.toSoapItemList(itemService.getItemsByItemType(request.getItemType()));
        response.getItems().addAll(items);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getItemsByNameRequest")
    @ResponsePayload
    public GetItemsByNameResponse getItemsByName(@RequestPayload GetItemsByNameRequest request) {
        GetItemsByNameResponse response = new GetItemsByNameResponse();

        List<Item> items = ItemMapper.toSoapItemList(itemService.getItemsByItemName(request.getItemName()));

        response.getItems().addAll(items);
        return response;
    }
}
