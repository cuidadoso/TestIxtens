package ru.ixtense.test.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ixtense.test.protocol.Command;
import ru.ixtense.test.protocol.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.*;

import static ru.ixtense.test.server.Server.getServices;
import static ru.ixtense.test.server.Server.getServicesCache;

/**
 * @author Alexander Pyreev
 */
class ServerExecutor implements Runnable
{
    private final static Logger LOG = LoggerFactory.getLogger(Server.class);
    private final static String SERVICES_PATH = "ru.ixtense.test.services.";
    private Socket clientSocket;

    ServerExecutor(final Socket clientSocket)
    {
        this.clientSocket = clientSocket;
        LOG.info("Server executor created.");
    }

    public void run()
    {
        ObjectOutputStream streamToClient = null;
        ObjectInputStream streamFromClient = null;
        try
        {
            streamToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            streamFromClient = new ObjectInputStream(clientSocket.getInputStream());
        } catch(IOException e)
        {
            LOG.info("Cant create input/output streams for client. {}", e.getMessage());
        }

        Object obj;
        Response response;

        try
        {
            assert streamFromClient != null;
            while((obj = streamFromClient.readObject()) != null)
            {
                response = null;
                if(obj instanceof Command)
                {
                    Command command = (Command) obj;
                    LOG.info("Receive command {}", command.toString());
                    String serviceName = SERVICES_PATH + command.getServiceName();
                    if(getServicesCache() != null &&
                            !getServicesCache().isEmpty() &&
                            getServicesCache().containsKey(serviceName))
                    {
                        String methodName = command.getMethodName();
                        Object[] parameters = command.getParameters();
                        List<String> serviceMethods = getServicesCache().get(serviceName);
                        if(serviceMethods != null &&
                                !serviceMethods.isEmpty() &&
                                serviceMethods.contains(methodName))
                        {
                            for(Class clazz : getServices())
                            {
                                if(clazz.getName().equals(serviceName))
                                {
                                    for(Method method : clazz.getMethods())
                                    {
                                        if(method.getName().equals(methodName) && checkParameters(method.getParameterTypes(), parameters))
                                        {
                                            Object result;
                                            try
                                            {
                                                result = method.invoke(clazz, parameters);
                                                if(result == null)
                                                {
                                                    result = "Void result.";
                                                }
                                                response = new Response(command.getId(), result);
                                            } catch(IllegalAccessException e)
                                            {
                                                LOG.info("Can't invoke method {}. {}", method.getName(), e.getMessage());
                                            } catch(InvocationTargetException e)
                                            {
                                                LOG.info("Can't invoke method {}. {}", method.getName(), e.getMessage());
                                            }
                                            assert response != null;
                                            LOG.info("Sent response {}", response.toString());
                                        }
                                    }
                                }
                            }
                        } else
                        {
                            LOG.info("Service {} has no method {}.", command.getServiceName(), methodName);
                        }
                    } else
                    {
                        LOG.info("Service {} is not found.", command.getServiceName());
                    }
                } else
                {
                    LOG.info("Command is undefined.");
                }

                try
                {
                    streamToClient.writeObject(response);
                } catch(IOException e)
                {
                    LOG.info("Can't sent response to client. {}", e.getMessage());
                }

                if(response == null)
                {
                    LOG.info("Response has no sent.");
                }
            }
        } catch(IOException e)
        {
            LOG.info("Can't receive command from client. {}", e.getMessage());
        } catch(ClassNotFoundException e)
        {
            LOG.info("Can't receive command from client. {}", e.getCause().toString());
        }
        finally
        {
            try
            {
                assert streamToClient != null;
                streamToClient.close();
                assert streamFromClient != null;
                streamFromClient.close();
                clientSocket.close();
            } catch(IOException e)
            {
                LOG.info("Can't close input/output streams.");
            }
        }
    }

    private boolean checkParameters(final Class[] paramTypes, final Object[] params)
    {
        if(paramTypes.length != params.length)
            return false;
        for(int i = 0; i < params.length; i++)
        {
            if(!paramTypes[i].getName().equals(params[i].getClass().getName()))
            {
                return false;
            }
        }
        return true;
    }
}
