package ru.ixtense.test.client;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        client.remoteCall("LocalService", "sleep", new Object[]{2000L});
        client.remoteCall("LocalService", "getMessageFromServer", new Object[]{"Hello"});
        client.remoteCall("LocalService", "getMessageFromServer", new Object[]{"Hello", "Buy"});
        client.closeResources();
    }
}
