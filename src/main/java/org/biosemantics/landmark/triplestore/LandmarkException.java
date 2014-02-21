package org.biosemantics.landmark.triplestore;

public class LandmarkException extends Exception {
	public LandmarkException()
	{
		super();
	}
	
	public LandmarkException(Exception e)
	{
		super(e);
	}
	
	public LandmarkException(String msg, Exception e)
	{
		super(msg, e);
	}
}
