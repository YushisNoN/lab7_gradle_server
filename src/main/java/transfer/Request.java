package transfer;

import models.Product;
import models.User;

import java.io.Serializable;

public class Request  implements Serializable {
    private String command = null;
    private Product product = null;
    private User user = null;

    public void setCommand(String command) {
        this.command = command;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommand() {
        return this.command;
    }

    public User getUser() {
        return this.user;
    }

    public Product getProduct() {
        return this.product;
    }
}

