package io.gumga.presentation;


import io.gumga.application.GumgaService;
import io.gumga.core.GumgaIdable;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
public abstract class GumgaAPI<T extends GumgaIdable<ID>, ID extends Serializable> extends AbstractGumgaAPI<T> {
	
	public GumgaAPI(GumgaService<T, ID> service) {
		super(service);
	}
	
}
