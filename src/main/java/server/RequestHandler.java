package server;

import models.Product;
import transfer.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

public class RequestHandler {
    private int buffer_size;
    private SocketAddress clientAdress;
    private boolean expectindProduct;
    private SelectionKey key;

    private Product product;
    public RequestHandler(Server server) {
        this.buffer_size = server.getBufferSize();
    }
    public Request readRequest(SelectionKey key) throws IOException, ClassNotFoundException {
        DatagramChannel datagramChannel = (DatagramChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(65536);
        this.key = key;
        this.clientAdress = datagramChannel.receive(buffer);
        if(clientAdress != null) {
            buffer.flip();
            byte[] dataBytes = new byte[buffer.remaining()];
            buffer.get(dataBytes);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(dataBytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object receivedObject = objectInputStream.readObject();
            return (Request) receivedObject;
        }
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return null;
    }

    public void setExceptingProduct(boolean flag) {
        this.expectindProduct = flag;
    }

    public boolean isExceptingProduct() {
        return this.expectindProduct;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    public Product getProduct() {
        return this.product;
    }

    public SelectionKey getKey() {
        return this.key;
    }

    public int getBufferSize() {
        return this.buffer_size;
    }
    public SocketAddress getClientAdress() {
        return this.clientAdress;
    }
}
