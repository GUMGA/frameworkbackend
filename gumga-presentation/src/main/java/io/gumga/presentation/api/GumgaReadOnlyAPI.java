package io.gumga.presentation.api;

import io.gumga.domain.service.GumgaReadableServiceable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
public abstract class GumgaReadOnlyAPI<T, ID extends Serializable> extends AbstractReadOnlyGumgaAPI<T, ID> {

	public GumgaReadOnlyAPI() {
		super(null);
	}
	
	@Autowired
	public void setService(GumgaReadableServiceable<T, ID> service) {
		this.service = service;
	}

}
