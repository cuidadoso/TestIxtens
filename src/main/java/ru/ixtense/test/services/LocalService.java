package ru.ixtense.test.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * @author Alexander Pyreev
 */
public class LocalService
{
    private final static Logger LOG = LoggerFactory.getLogger(LocalService.class);

    public static void exit()
    {
        LOG.info("Close connection...");
        System.exit(-1);
    }

    public static boolean isAcepted()
    {
        return true;
    }

    public static String getMessageFromServer(final String msg)
    {
        return msg + " from server";
    }

    public static String getMessageFromServer()
    {
        return "Default message from server";
    }

    public static void sleep(final Long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch(InterruptedException e)
        {
            LOG.info(e.getMessage());
        }
    }

    public static Date getCurrentDate()
    {
        return new Date();
    }

}
