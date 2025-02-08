package client.model.scenemodels;

import client.ClientSide;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static server.ServerSide.loadXML;

public class SignInPageModel{

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private String userID;
    private String userPassword;
    private String userType;
    private Object[] responseFromServer;

    public void auth(String userID, String userPassword, String userType) throws IOException, ClassNotFoundException {
        socket = new Socket(ClientSide.getServerAddress(), ClientSide.getServerPort());
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());

        String id = String.valueOf(userID.hashCode());
        String request = "login";
        String credentials[] = {userID, userPassword, userType};

        sendToServer(id, request, credentials);
        Object[] response = (Object[]) inputStream.readObject();

//        if (!response[].equals("Success")){
//            socket.close();
//            inputStream.close();
//            outputStream.close();
//        }
    }

    public void sendToServer(String id, String request, Object credentials) throws IOException {
        Object[] requests = new Object[]{id, request, credentials};
        outputStream.writeObject(requests);
        outputStream.flush();
    }

    //getters
    public Socket getSocket() {
        return socket;
    }
    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }
    public ObjectInputStream getInputStream() {
        return inputStream;
    }
    public Object[] getResponseFromServer() {
        return responseFromServer;
    }
    public String getUserID() {
        return userID;
    }
    public String getUserPassword() {
        return userPassword;
    }


    //setters
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    public void setInputStream(ObjectInputStream inputStream) {
        this.inputStream = inputStream;
    }
    public void setOutputStream(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }
    public void setResponseFromServer(Object[] responseFromServer) {
        this.responseFromServer = responseFromServer;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}


