package ru.ixtense.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ixtense.test.client.Client;
import ru.ixtense.test.client.ClientExecuter;
import ru.ixtense.test.services.LocalService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Alexander Pyreev
 */
public class ClientStart
{
    private final static Logger LOG = LoggerFactory.getLogger(ClientStart.class);

    public static void main(String[] args)
    {
        if(args.length < 2)
        {
            LOG.info("Define server and TCP port...");
            LocalService.exit();
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        Client client;

        ExecutorService service = Executors.newFixedThreadPool(5);
        for(int i = 0; i < 10; i++)
        {
            client = new Client(host, port);
            service.submit(new Thread(new ClientExecuter(client)));
        }
        try
        {
            Thread.sleep(20000);
        } catch(InterruptedException e)
        {
            LOG.info("Thread is interrupted. {}", e.getMessage());
        }
        service.shutdown();
        LocalService.exit();
    }
}
