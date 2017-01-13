package io.gumga.presentation.api.dto;

import io.gumga.presentation.api.AbstractNoDeleteGumgaAPI;
import io.gumga.presentation.gateway.GumgaNoDeleteGateway;

public abstract class GumgaNoDeleteDTOAPI<T> extends AbstractNoDeleteGumgaAPI<T> {
	
	public GumgaNoDeleteDTOAPI(GumgaNoDeleteGateway<?, T> gateway) {
		super(gateway);
	}

}
