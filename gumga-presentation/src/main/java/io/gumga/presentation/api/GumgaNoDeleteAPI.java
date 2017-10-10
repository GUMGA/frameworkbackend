package io.gumga.presentation.api;

import io.gumga.domain.service.GumgaWritableServiceable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
public abstract class GumgaNoDeleteAPI<T, ID extends Serializable> extends AbstractNoDeleteGumgaAPI<T, ID> {

	public GumgaNoDeleteAPI() {
		super(null);
	}
	
	@Autowired
	public void setService(GumgaWritableServiceable<T, ID> service) {
		setService(service);
	}

}
