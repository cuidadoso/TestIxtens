package ru.ixtense.test.protocol;

import java.io.Serializable;

/**
 * @author Alexander Pyreev
 */
public class Response implements Serializable
{
    private long id;
    private Object result;

    public Response(long id, Object result)
    {
        this.id = id;
        this.result = result;
    }

    public long getId()
    {
        return id;
    }

    public Object getResult()
    {
        return result;
    }

    public void setResult(Object result)
    {
        this.result = result;
    }

    @Override
    public String toString()
    {
        return "Response{" +
                "id=" + id +
                ", result=" + result +
                '}';
    }
}
