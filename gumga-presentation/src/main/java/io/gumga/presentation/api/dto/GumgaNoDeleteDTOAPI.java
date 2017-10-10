package io.gumga.presentation.api.dto;

import io.gumga.presentation.api.AbstractNoDeleteGumgaAPI;
import io.gumga.presentation.gateway.GumgaNoDeleteGateway;

import java.io.Serializable;

public abstract class GumgaNoDeleteDTOAPI<T, ID extends Serializable> extends AbstractNoDeleteGumgaAPI<T, ID> {
	
	public GumgaNoDeleteDTOAPI(GumgaNoDeleteGateway<?, T, ID> gateway) {
		super(gateway);
	}

}
