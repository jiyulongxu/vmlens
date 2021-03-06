package com.vmlens.trace.agent.bootstrap.callback;




import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import com.vmlens.trace.agent.bootstrap.parallize.ParallizeFacade;
import com.vmlens.trace.agent.bootstrap.parallize.ParallizeSingelton;
import com.vmlens.trace.agent.bootstrap.interleave.operation.Task;
import com.vmlens.trace.agent.bootstrap.parallize.FutureTask2ThreadId;
import com.vmlens.trace.agent.bootstrap.parallize.ParallizeCallback;
import com.vmlens.trace.agent.bootstrap.parallize.logic.RunnableOrThreadWrapper;


public class ExecutorCallback {


	
	public static void threadStartMethodEnter(Object runnable)
	{
		ParallizeFacade.beforeExecutorStart( CallbackState.callbackStatePerThread.get(),runnable);
		
		CallbackStatePerThread callbackStatePerThread = CallbackState.callbackStatePerThread.get();
		 
		callbackStatePerThread.inThreadStart++;
		 
		 callbackStatePerThread.doNotInterleave++;
	}
	

	public static void threadStartMethodExit()
	{
		 CallbackStatePerThread callbackStatePerThread  =
				 CallbackState.callbackStatePerThread.get();
		 
		 callbackStatePerThread.inThreadStart--;
		 callbackStatePerThread.doNotInterleave--;
		 
		 ParallizeCallback.afterThreadStart();
		 
		 
		 
		 
		 
	}
	

	
	public static void forkJoinTaskForkEnter(ForkJoinTask task)
	{
		ParallizeFacade.beforeExecutorStart( CallbackState.callbackStatePerThread.get(),task);
		
		 CallbackState.callbackStatePerThread.get().inThreadStart++;
	}
	
	
	
	public static void forkJoinTaskForkExit()
	{
		 CallbackStatePerThread callbackStatePerThread  =
				 CallbackState.callbackStatePerThread.get();
		 
		 callbackStatePerThread.inThreadStart--;
		 
		 ParallizeCallback.afterThreadStart();
	}
	
	
	
	
	/**
	 * Für ForkJoinPool und Threads, und Threads die runnables aufrufen
	 * 
	 * 
	 * @param task
	 */

	
	
	public static void methodEnterExecTask(Object task)
	{
	
		if( task instanceof Thread)
		{
			CallbackStatePerThread callbackStatePerThread = CallbackState.callbackStatePerThread.get();
			 
			 ParallizeSingelton.beginThreadMethodEnter(callbackStatePerThread,new RunnableOrThreadWrapper(task));
		}
	
	}
	
	
	public static void methodExitExecTask(Object task)
	{
	
		if( task instanceof Thread )
		{
			
			CallbackStatePerThread callbackStatePerThread = CallbackState.callbackStatePerThread.get();
			
			
		ParallizeFacade.beginThreadMethodExit(callbackStatePerThread);
	
		}
	}
	
	
	public static void execAfter()
	{
		// ParallizeCallback.beginTask(  Task.FORK_JOIN_TASK );
	
		
		
		CallbackStatePerThread callbackStatePerThread = CallbackState.callbackStatePerThread.get();
		
		
		
		if(callbackStatePerThread.notStartedCount > 0)
		{
			callbackStatePerThread.notStartedCount--;
			callbackStatePerThread.doNotInterleave = callbackStatePerThread.tempDoNotInterleave;
			return;
		}
		
		
		ParallizeFacade.beginThreadMethodExit(callbackStatePerThread);
		
		callbackStatePerThread.doNotInterleave = callbackStatePerThread.tempDoNotInterleave;
	}
	
	
	public static void execBefore(ForkJoinTask task )
	{
		
		
		
		CallbackStatePerThread callbackStatePerThread = CallbackState.callbackStatePerThread.get();
		 
		callbackStatePerThread.tempDoNotInterleave = callbackStatePerThread.doNotInterleave;
		 
		 callbackStatePerThread.doNotInterleave = 0;
		 
		if(  ! ParallizeSingelton.beginThreadMethodEnter(callbackStatePerThread,new RunnableOrThreadWrapper(task)) )
		{
			callbackStatePerThread.notStartedCount++;
		}
		 
	//	 ParallizeCallback.beginTask(  Task.FORK_JOIN_TASK );
	
		
	
	}
	
	
//	public static Method findMethod(Object obj)
//	{
//		Class current = obj.getClass();
//		
//		while( current != null )
//		{
//			try {
//				return current.getDeclaredMethod("exec");
//			} catch (NoSuchMethodException e) {
//				current = current.getSuperclass();
//			} catch (SecurityException e) {
//				
//				e.printStackTrace();
//				return null;
//			}
//		}
//		
//		
//		System.err.println( "not found for "  + obj.getClass());
//		
//		throw new RuntimeException();
//	}
	
	
	

	 /**
	  * für ThreadPoolExecutor
	  * 
	  * 
	  * @param runnable
	  * @param methodId
	 * @throws Exception 
	  */
	
	 public static Object call(Callable callable,int methodId) throws Exception
	 {
		 
		 
		 CallbackStatePerThread callbackStatePerThread = CallbackState.callbackStatePerThread.get();
		 
		 int temp = callbackStatePerThread.doNotInterleave;
		 callbackStatePerThread.doNotInterleave = 0;
		 
	     ParallizeFacade.callableFromTaskMethodEnter(callbackStatePerThread);
		 try {
			return  callable.call(); 
		 }
		 finally {
				   ParallizeFacade.callableFromTaskMethodExit(callbackStatePerThread);
					callbackStatePerThread.doNotInterleave = temp;
				
		 }
	 }
	
	
	 public static void run(Runnable runnable,int methodId)
	 {
		 
		 
		 CallbackStatePerThread callbackStatePerThread = CallbackState.callbackStatePerThread.get();
		 
		 
		 
		 int temp = callbackStatePerThread.doNotInterleave;
		 
		
		 
		 if(runnable instanceof FutureTask)
		 {
			 callbackStatePerThread.doNotInterleave++;
		 }
		 else
		 {
			 callbackStatePerThread.doNotInterleave = 0;
		 }
		 
		
		 
		 ParallizeSingelton.beginThreadMethodEnter(callbackStatePerThread,new RunnableOrThreadWrapper(runnable));
		 try {
			 runnable.run(); 
		 }
		 finally {
				ParallizeFacade.beginThreadMethodExit(callbackStatePerThread);
				
				 if(runnable instanceof FutureTask)
				 {
					 callbackStatePerThread.doNotInterleave--;
				 }
				 else
				 {
					callbackStatePerThread.doNotInterleave = temp;
				 }
				
				
			
				
		 }
	 }
	 
	 
	 
	 public static Object get(Future future, int methodId) throws InterruptedException, ExecutionException
	 {
		 try {
			 return future.get();
		 }
		 finally
		 {
			 if( future instanceof FutureTask )
			 {
				 
				 Long threadId = FutureTask2ThreadId.get(future);
				 
				 if(  threadId != null )
				 {
					 CallbackStatePerThread callbackStatePerThread = CallbackState.callbackStatePerThread.get();
					 ParallizeFacade.afterFutureGet(callbackStatePerThread , threadId);
				 }
				 
			 }
		 }
		
		 
		 
		 
	 }
	 
	 
	 
	 
	 
}
