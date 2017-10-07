package io.gumga.presentation.dto;

import io.gumga.presentation.api.AbstractReadOnlyGumgaAPI;
import io.gumga.presentation.gateway.GumgaReadOnlyGateway;

import java.io.Serializable;

public abstract class GumgaReadOnlyDTOAPI<T, ID extends Serializable> extends AbstractReadOnlyGumgaAPI<T, ID> {
	
	public GumgaReadOnlyDTOAPI(GumgaReadOnlyGateway<?, T, ID> gateway) {
		super(gateway);
	}

}
