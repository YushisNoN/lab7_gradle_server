package commands;


import commands.exceptions.WrongArgumentsAmountException;
import managers.ProductManager;
import models.IdGenerator;
import models.IncorrectIntegerValueException;
import models.IncorrectStringValueException;
import models.Product;

public class RemoveAnyByPrice extends Command {
    private ProductManager<Product> productManager;

    public RemoveAnyByPrice(ProductManager<Product> manager) {
        super();
        this.productManager = manager;
        this.isNeedArguments = true;
        this.commandArguments = 1;
    }

    @Override
    public void execute(String[] arguments)
            throws WrongArgumentsAmountException, IncorrectStringValueException, IncorrectIntegerValueException {
        if (arguments.length != this.commandArguments) {
            throw new WrongArgumentsAmountException();
        }
        if (arguments[arguments.length - 1].matches("^-?\\d+$") == false) {
            throw new IncorrectIntegerValueException();
        }
        int priceToDelete = Integer.parseInt(arguments[arguments.length - 1]);
        for (Product product : this.productManager.getCollection()) {
            if (product.getPrice() == priceToDelete) {
                this.productManager.getCollection().remove(product);
                IdGenerator.updateCounter(this.productManager.getCollection());
                this.response.setResponse("Элемент успешно удален из коллекции");
                return;
            }
        }
        this.response.setResponse("Элемента с таким значением цены не существует в коллекции :(");
    }

    @Override
    public void execute() throws WrongArgumentsAmountException {
        throw new WrongArgumentsAmountException();
    }

    @Override
    public String getDescription() {
        return "remove_any_by_price price : удалить из коллекции один элемент, значение поля price которого эквивалентно заданному";
    }

    @Override
    public String toString() {
        return "remove_any_by_price";
    }
}
