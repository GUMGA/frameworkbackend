package io.gumga.presentation;

import io.gumga.core.GumgaIdable;
import io.gumga.core.utils.ReflectionUtils;

import java.util.LinkedList;
import java.util.List;

public abstract class GumgaTranslator<BusinessObject extends GumgaIdable<?>, ServiceObject> {
	
	public abstract BusinessObject to(ServiceObject serviceObject);
	
	public abstract ServiceObject from(BusinessObject businessObject);
	
	public List<BusinessObject> to(List<ServiceObject> serviceObjects) {
		List<BusinessObject> result = new LinkedList<>();
		
		for (ServiceObject obj : serviceObjects)
			result.add(to(obj));
		
		return result;
	}
	
	public List<ServiceObject> from(List<BusinessObject> serviceObjects) {
		List<ServiceObject> result = new LinkedList<>();
		
		for (BusinessObject obj : serviceObjects)
			result.add(from(obj));
		
		return result;
	}
	
	public Class<?> domainClass() {
		return ReflectionUtils.inferGenericType(getClass());
	}
	
	public Class<?> dtoClass() {
		return ReflectionUtils.inferGenericType(getClass(), 1);
	}
	
}
