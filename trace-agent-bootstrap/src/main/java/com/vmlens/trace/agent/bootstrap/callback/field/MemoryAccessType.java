package com.vmlens.trace.agent.bootstrap.callback.field;

public class MemoryAccessType {

	public static final int IS_READ = 1;
	public static final int IS_WRITE = 2;
	public static final int IS_ATOMIC = 4;
	  
	  
	public static final int IS_READ_WRITE = 3;
	
	
	public static int getOperation( boolean isWrite )
	{
	      
	      if(isWrite)
	      {
	    	 return MemoryAccessType.IS_WRITE;
	      }
	      else
	      {
	    	 return MemoryAccessType.IS_READ;
	      }
	}
	
	
	public static boolean containsWrite(int operation)
	{
		return  ( (operation & IS_WRITE) == IS_WRITE ) || ( ( operation & IS_ATOMIC) == IS_ATOMIC );
	}
	
	
	
}
