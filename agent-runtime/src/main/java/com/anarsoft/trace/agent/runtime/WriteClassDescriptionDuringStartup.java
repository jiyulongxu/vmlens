package com.anarsoft.trace.agent.runtime;

import com.vmlens.shaded.gnu.trove.list.linked.TLinkedList;

import com.anarsoft.trace.agent.serialization.ClassDescription;

public class WriteClassDescriptionDuringStartup  implements WriteClassDescription  {

	private  TLinkedList<
	TLinkableWrapper<ClassDescription>>   classAnalyzedEventList ;
	
	
	
	public WriteClassDescriptionDuringStartup(
			TLinkedList<TLinkableWrapper<ClassDescription>> classAnalyzedEventList) {
		super();
		this.classAnalyzedEventList = classAnalyzedEventList;
	}



	@Override
	public void write(ClassDescription classDescription) {
		classAnalyzedEventList.add( new TLinkableWrapper<ClassDescription>( classDescription ));
		
	}

}
