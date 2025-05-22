package commands;

import commands.exceptions.WrongArgumentsAmountException;
import db.service.ProductService;
import db.service.UserProductService;
import managers.ProductManager;
import models.IncorrectStringValueException;
import models.Product;
import models.User;
import models.UserProducts;

import java.util.List;

public class Clear extends Command {

    private UserProductService userProducts;
    public Clear(UserProductService userProducts) {
        super();
        this.userProducts = userProducts;
    }

    @Override
    public String toString() {
        return "clear";
    }

    @Override
    public void execute() throws WrongArgumentsAmountException {
        //if (this.productManager.getCollection().isEmpty()) {
        //    this.response.setResponse("коллекция пустая :)");
       //     return;
       // }
        //TODO сделать удаление лишь тех элементов, которыми владеет пользователь
        long id = this.userService.getId(this.request.getUser().getUsername());

        List<Product> productList = this.productService.getCollectionById(id);
        for(var x: productList) {
            System.out.println(x.getId());
        }
        long deletedUserProduct = this.userProducts.deleteAllByUserId(id) > 0 ? this.userProducts.deleteAllByUserId(id): 0;
        System.out.println(deletedUserProduct);
        if(deletedUserProduct >= 0) {
            this.productService.deleteAllProducts(productList);
            this.response.setResponse("Коллекция успешна очищена >:)");
        }
        else {
            this.response.setResponse("Коллекция пустая :)");
        }
    }

    @Override
    public void execute(String[] arguments) throws WrongArgumentsAmountException, IncorrectStringValueException {
        throw new WrongArgumentsAmountException();
    }

    @Override
    public String getDescription() {
        return "clear : очистить коллекцию";
    }
}
