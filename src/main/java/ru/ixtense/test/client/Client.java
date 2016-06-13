package ru.ixtense.test.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ixtense.test.protocol.Command;
import ru.ixtense.test.protocol.Response;
import ru.ixtense.test.util.NoResponseException;

import java.io.*;
import java.net.Socket;

/**
 * @author Alexander Pyreev
 */
public class Client
{
    private final static Logger LOG = LoggerFactory.getLogger(Client.class);

    private Socket clientSocket;
    private ObjectInputStream streamFromServer;
    private ObjectOutputStream streamToServer;

    private static Long id = 0L;

    public Client(final String host, final Integer port)
    {
        LOG.info("Client start.");
        LOG.info("Connecting to {}...", host);
        try
        {
            clientSocket = new Socket(host, port);
            streamFromServer = new ObjectInputStream(clientSocket.getInputStream());
            streamToServer = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch(IOException e)
        {
            LOG.info("Can't create connection from client. {}", e.getMessage());
        }
    }

    private Object sendCommand(final Command command) throws NoResponseException
    {
        Object obj = null;

        try
        {
            streamToServer.writeObject(command);
        } catch(IOException e)
        {
            LOG.info("Can't sent command to server. {}", e.getMessage());
        }
        LOG.info("Command {} has sent.", command == null ? null : command.toString());

        try
        {
            obj = streamFromServer.readObject();
        } catch(IOException e)
        {
            LOG.info("Can't receive response from server. {}", e.getMessage());
        } catch(ClassNotFoundException e)
        {
            LOG.info("Can't receive response from server. {}", e.getMessage());
        }
        if(obj != null)
        {
            Response response = (Response) obj;
            LOG.info("Response {} received.", response.toString());
        } else
        {
            throw new NoResponseException("Response has no received.");
        }
        return obj;
    }

    public Object remoteCall(final String serviceName, final String methodName, final Object[] params) throws NoResponseException
    {
        Command command = new Command(++id, serviceName, methodName, params);
        return sendCommand(command);
    }

    public void closeResources()
    {
        try
        {
            clientSocket.close();
            streamFromServer.close();
            streamToServer.close();
        } catch(IOException e)
        {
            LOG.info("Something wrong while closing client connection. {}", e.getMessage());
        }
    }
}
