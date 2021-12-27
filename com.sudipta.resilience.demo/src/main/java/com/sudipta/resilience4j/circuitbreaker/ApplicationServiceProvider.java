package com.sudipta.resilience4j.circuitbreaker;

public class ApplicationServiceProvider
{
    int sleepTime = 3000;

    public String getResource(/* int sleepTime */)
    {
	String returnMessage = "";

	try
	{

	    System.out.println("In Get resource before sleep");
	    if (sleepTime > 2000)
	    {
		throw new RuntimeException("Too Long to process");
	    }
	    Thread.currentThread().sleep(sleepTime);

	    System.out.println("In Get resource After sleep");
	    returnMessage = "Resource Response";
	    return returnMessage;
	}
	catch (InterruptedException e)
	{
	    // TODO Auto-generated catch block
	    throw new RuntimeException(e.getMessage());

	}

    }

    public String getFallbackResource(Throwable throwable)
    {
	String returnMessage = "Fallback return sting";

	return returnMessage;

    }

}
