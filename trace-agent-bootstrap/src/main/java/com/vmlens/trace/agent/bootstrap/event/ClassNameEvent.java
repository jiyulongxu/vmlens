package com.vmlens.trace.agent.bootstrap.event;

import java.io.DataOutputStream;

public class ClassNameEvent implements StaticEvent  {
	
	
	private final String name;
	private final int id;

	
	
	
	

	public ClassNameEvent(String name, int id) {
		super();
		this.name = name;
		this.id = id;
	}






	@Override
	public void serialize(StreamRepository streamRepository) throws Exception {
	
		DataOutputStream stream = streamRepository.className.getStream();
		
		stream.writeInt(id);
		stream.writeUTF(name);
	
		
		stream.flush();
		
	}
	
}
