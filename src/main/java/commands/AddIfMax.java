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

public class AddIfMax extends Add {
    private ProductManager<Product> productCollection;

    public AddIfMax(ProductManager<Product> manager, ResponseHandler response, RequestHandler request, ProductService productService, Request request1, UserService userService) {
        super(manager, response, request, productService, request1, userService);
        this.productCollection = manager;
    }

    @Override
    public void execute(String[] arguments) throws WrongArgumentsAmountException, IncorrectStringValueException {
        throw new WrongArgumentsAmountException();
    }

    @Override
    public String getDescription() {
        return "add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента в этой коллекции";
    }

    @Override
    public String toString() {
        return "add_if_max";
    }

    @Override
    public void execute() throws WrongArgumentsAmountException {
        try {
            this.requestHandler.setExceptingProduct(true);
            Response response = new Response();
            response.setCommand("add");
            this.responseHandler.sendResponse(this.requestHandler.getKey(), response);
            Request request = this.requestHandler.readRequest(this.responseHandler.getKey());
            while (request.getProduct() == null) {
                request = this.requestHandler.readRequest(this.requestHandler.getKey());
            }
            this.requestHandler.setProduct(request.getProduct());
            Product product = this.requestHandler.getProduct();
            if (this.productCollection.getCollection().last().compareTo(product) < 0) {
                this.productCollection.addProdut(product);
                System.out.println("Продукт успешно добавлен в коллекцию");
                IDreceive iDreceive = new IDreceive(this.productCollection);
                iDreceive.updateID();
                IdGenerator.updateCounter(this.productCollection.getCollection());
                return;
            }
            System.out.println("Элемент невозможно добавить в коллекцию, так как он меньше наибольшего элемента ^^");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
