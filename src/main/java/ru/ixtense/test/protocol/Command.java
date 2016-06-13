package ru.ixtense.test.protocol;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Alexander Pyreev
 */
public class Command implements Serializable
{
    private long id;
    private String serviceName;
    private String methodName;
    private Object[] parameters;

    public Command(long id, String serviceName, String methodName)
    {
        this.id = id;
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.parameters = new Object[]{};
    }

    public Command(long id, String serviceName, String methodName, Object[] parameters)
    {
        this.id = id;
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.parameters = parameters;
    }

    public long getId()
    {
        return id;
    }

    public String getServiceName()
    {
        return serviceName;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public Object[] getParameters()
    {
        return parameters;
    }

    @Override
    public String toString()
    {
        return "Command{" +
                "id=" + id +
                ", serviceName='" + serviceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}
