package commands;


import commands.exceptions.WrongArgumentsAmountException;
import db.service.ProductService;
import db.service.UserService;
import managers.ProductManager;
import models.IncorrectStringValueException;
import models.Product;
import models.User;
import server.RequestHandler;
import server.ResponseHandler;
import transfer.Request;

public class Add extends Command {

    private ProductManager<Product> productCollection;

    protected ResponseHandler responseHandler;
    protected RequestHandler requestHandler;
    protected ProductService productService;
    protected UserService userService;
    protected Request request;
    public Add(ProductManager<Product> manager, ResponseHandler response, RequestHandler request, ProductService productService, Request request1, UserService userService) {
        super();
        this.productCollection = manager;
        this.requestHandler = request;
        this.responseHandler = response;
        this.productService = productService;
        this.request = request1;
        this.userService = userService;
    }

    @Override
    public void execute(String[] arguments) throws WrongArgumentsAmountException, IncorrectStringValueException {
        throw new WrongArgumentsAmountException();
    }

    @Override
    public String getDescription() {
        return "add {element} : добавить новый элемент в коллекцию";
    }

    @Override
    public String toString() {
        return "add";
    }

    @Override
    public void execute() throws WrongArgumentsAmountException {
        this.response.setResponse("Продукт добавлен");
    }

}
