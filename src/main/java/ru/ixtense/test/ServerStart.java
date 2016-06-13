package ru.ixtense.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ixtense.test.server.Server;
import ru.ixtense.test.services.LocalService;

import java.io.IOException;

/**
 * @author Alexander Pyreev
 */
public class ServerStart
{
    private final static Logger LOG = LoggerFactory.getLogger(ServerStart.class);

    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            LOG.info("Define TCP port...");
            LocalService.exit();
        }
        int port = Integer.parseInt(args[0]);
        LOG.info("Server started.");

        try
        {
            Server server = new Server(port);
            new Thread(server).start();
            Thread.sleep(10000);
            server.stop();
        } catch(InterruptedException e)
        {
            LOG.info("Something wrong while starting server.");
        }
    }
}
