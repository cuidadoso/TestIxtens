package ru.ixtense.test.client;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ixtense.test.util.NoResponseException;

/**
 * @author Alexander Pyreev
 */
public class ClientExecuter implements Runnable
{
    private final static Logger LOG = LoggerFactory.getLogger(ClientExecuter.class);

    private Client client;

    public ClientExecuter(final Client client)
    {
        this.client = client;
        LOG.info("Client executor created.");
    }

    public void run()
    {
        try
        {
            client.remoteCall("LocalService", "sleep", new Object[]{2000L});
            client.remoteCall("LocalService", "getMessageFromServer", new Object[]{"Hello"});
            client.remoteCall("LocalService", "isAcepted", new Object[]{});
            client.remoteCall("LocalService", "getCurrentDate", new Object[]{});
            client.remoteCall("LocalService", "getMessageFromServer", new Object[]{"Hello", "Buy"});
        }
        catch(NoResponseException e)
        {
            LOG.info(e.getMessage());
        }

        client.closeResources();
    }
     /*
            client.remoteCall("LocalService", "isAcepted", new Object[]{});
            client.remoteCall("LocalService", "getMessageFromServer", new Object[]{});
            client.remoteCall("LocalService", "getMessageFromServer", new Object[]{"Hello"});
            client.remoteCall("LocalService", "sleep", new Object[]{2000L});
            client.remoteCall("LocalService", "getMessageFromServer", new Object[]{"Hello", "Buy"});
            client.remoteCall("LocalService", "getMessageFromServer2", new Object[]{});
            client.remoteCall("LocalService2", "getMessageFromServer2", new Object[]{});
            client.remoteCall("LocalService", "getCurrentDate", new Object[]{});
        */
}
