package commands;

import commands.exceptions.WrongArgumentsAmountException;
import db.service.ProductService;
import db.service.UserService;
import managers.ProductManager;
import models.IdGenerator;
import models.IncorrectStringValueException;
import models.Product;

import models.User;
import transfer.Request;
import server.RequestHandler;
import transfer.Response;
import server.ResponseHandler;
import utils.IDreceive;

import java.io.IOException;

public class RemoveGreater extends Add {
    private ProductManager<Product> productCollection;

    public RemoveGreater(ProductManager<Product> manager, ResponseHandler response, RequestHandler request, ProductService productService, Request request1, UserService userService) {
        super(manager, response, request, productService, request1, userService);
        this.productCollection = manager;
    }

    @Override
    public void execute(String[] arguments) throws WrongArgumentsAmountException, IncorrectStringValueException {
        throw new WrongArgumentsAmountException();
    }

    @Override
    public String getDescription() {
        return "remove_greater {element} : удалить из коллекции все элементы, превыщающие заданный.";
    }

    @Override
    public String toString() {
        return "remove_greater";
    }

    @Override
    public void execute() {
        try {
            this.requestHandler.setExceptingProduct(true);
            Response response = new Response();
            response.setCommand("add");
            this.responseHandler.sendResponse(this.requestHandler.getKey(), response);
            Request request = this.requestHandler.readRequest(this.responseHandler.getKey());
            while(request.getProduct()== null) {
                request = this.requestHandler.readRequest(this.requestHandler.getKey());
            }
            this.requestHandler.setProduct(request.getProduct());
            Product product = this.requestHandler.getProduct();
            int sizeOld = this.productCollection.getCollection().size();
            this.productCollection.getCollection()
                    .removeAll(this.productCollection.getCollection().tailSet(product, false));
            int sizeNew = this.productCollection.getCollection().size();
            IDreceive iDreceive = new IDreceive(this.productCollection);
            iDreceive.updateID();
            IdGenerator.updateCounter(this.productCollection.getCollection());
            this.response.setResponse("Из коллекции успешно удалено " + (sizeOld - sizeNew) + " Элементов");
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
