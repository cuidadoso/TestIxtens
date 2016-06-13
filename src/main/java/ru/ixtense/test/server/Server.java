package ru.ixtense.test.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ixtense.test.services.LocalService;

import java.io.*;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.util.*;

/**
 * @author Alexander Pyreev
 */
public class Server implements Runnable
{
    private final static Logger LOG = LoggerFactory.getLogger(Server.class);
    private final static String SERVICES_PATH = "ru.ixtense.test.services.";

    private static Set<Class> services = new HashSet<Class>();
    private static Map<String, List<String>> servicesCache = new HashMap<String, List<String>>();

    private ServerSocket serverSocket;
    private boolean isStopped = false;
    public Server(final int port)
    {
        try
        {
            serverSocket = new ServerSocket(port);
        } catch(IOException e)
        {
            LOG.info("Can't open port. {}", e.getMessage());
        }
        createServiceCache();
    }

    public void run()
    {
        while(!isStopped())
        {
            LOG.info("Waiting for a commands from client...");
            try
            {
                new Thread(new ServerExecutor(serverSocket.accept())).start();
            } catch(IOException e)
            {
                LOG.info("Can't accept connection from client or connection is closed. {}", e.getMessage());
            }
        }
        try
        {
            serverSocket.close();
        } catch(IOException e)
        {
            LOG.info("Can't close server port. {}", e.getMessage());
        }
        LOG.info("Server stopped.");
        LocalService.exit();
    }

    private synchronized boolean isStopped()
    {
        return isStopped;
    }

    public synchronized void stop()
    {
        isStopped = true;
        try
        {
            serverSocket.close();
        } catch(IOException e)
        {
            LOG.info("Something wrong while closing server port. {}", e.getMessage());
        }
    }

    private void createServiceCache()
    {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("server");
        Enumeration bundleKeys = resourceBundle.getKeys();

        while(bundleKeys.hasMoreElements())
        {
            String key = (String) bundleKeys.nextElement();
            String value = resourceBundle.getString(key);

            Class clazz = null;
            try
            {
                clazz = Class.forName(SERVICES_PATH + value);
            } catch(ClassNotFoundException e)
            {
                LOG.info("Can't find service {}. {}", value, e.getMessage());
                LocalService.exit();
            }
            List<String> methodNames = new ArrayList<String>();
                services.add(clazz);
                for(Method method : clazz.getDeclaredMethods())
                {
                    methodNames.add(method.getName());
                }
                servicesCache.put(clazz.getName(), methodNames);
        }
    }

    static Set<Class> getServices()
    {
        return services;
    }

    static Map<String, List<String>> getServicesCache()
    {
        return servicesCache;
    }
}
