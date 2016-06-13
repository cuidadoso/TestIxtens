package ru.ixtense.test.util;

/**
 * @author Alexander Pyreev
 */
public class NoResponseException extends RuntimeException
{
    public NoResponseException(String message)
    {
        super(message);
    }
}
