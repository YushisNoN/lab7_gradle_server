package commands;

import commands.exceptions.WrongArgumentsAmountException;
import db.service.ProductService;
import db.service.UserService;
import managers.ProductManager;
import models.*;
import transfer.Request;
import server.RequestHandler;
import transfer.Response;
import server.ResponseHandler;
import utils.IDreceive;

import java.io.IOException;
import java.util.TreeSet;

public class UpdateID extends Add {
    private ProductManager<Product> productManager;

    private Request req;

    @Override
    public void setRequest(Request request)
    {
        this.req = request;
    }


    public UpdateID(ProductManager<Product> manager, ResponseHandler response, RequestHandler request, ProductService productService, Request request1, UserService userService) {
        super(manager, response, request, productService, request1, userService);
        this.productManager = manager;
        this.isNeedArguments = true;
        this.commandArguments = 1;
    }

    @Override
    public void execute(String[] arguments)
            throws WrongArgumentsAmountException, IncorrectStringValueException{
        if (arguments.length != this.commandArguments) {
            throw new WrongArgumentsAmountException();
        }
        if (arguments[arguments.length - 1].matches("^-?\\d+$") == false) {
            try {
                throw new IncorrectIntegerValueException();
            } catch (IncorrectIntegerValueException e) {
                throw new RuntimeException(e);
            }
        }
        ProductManager<Product> manager = new ProductManager<>();
        long id = Long.parseLong(arguments[arguments.length - 1]);
        long userId = this.userService.getId(this.req.getUser().getUsername());
        manager.setCollection(new TreeSet<Product>(this.productService.getCollectionById(userId)));
        for (Product product : manager.getCollection()) {
            if (product.getId() == id) {
                try {
                    this.requestHandler.setExceptingProduct(true);
                    Response response = new Response();
                    response.setCommand("add");
                    this.responseHandler.sendResponse(this.requestHandler.getKey(), response);
                    Request request1 = this.requestHandler.readRequest(this.responseHandler.getKey());
                    while(request1 == null) {
                        request1 = this.requestHandler.readRequest(this.responseHandler.getKey());
                    }
                    Product productResponse = request1.getProduct();
                    if(productResponse != null) {
                        Product productUpdate = this.productService.getProductById(id);

                        productUpdate.setCoordinates(productResponse.getCoordinates());
                        productUpdate.setName(productResponse.getName());
                        productUpdate.setOwner((productResponse.getOwner()));
                        productUpdate.setPrice(productResponse.getPrice());
                        productUpdate.setUnitOfMeasure(productResponse.getUnitOfMeasure());
                        Product product1 = this.productService.createProduct(productUpdate);
                        response.setResponse("Объект успешно обновлен :) ");
                        response.setCommand(null);
                        this.responseHandler.sendResponse(this.requestHandler.getKey(), response);
                        this.requestHandler.setExceptingProduct(false);
                        return;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        this.response.setResponse("Элемент с таким id невозможно изменить, так как он принадлежит другому пользователю");
    }

    @Override
    public void execute() throws WrongArgumentsAmountException {
        throw new WrongArgumentsAmountException();
    }

    @Override
    public String getDescription() {
        return "update_id {element} : обновить значение элемента коллекции, id которого равен заданному";
    }



    @Override
    public String toString() {
        return "update_id";
    }
}
