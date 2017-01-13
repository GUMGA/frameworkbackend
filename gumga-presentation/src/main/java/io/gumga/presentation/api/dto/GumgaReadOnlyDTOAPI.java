package io.gumga.presentation.api.dto;

import io.gumga.presentation.api.AbstractReadOnlyGumgaAPI;
import io.gumga.presentation.gateway.GumgaReadOnlyGateway;

public abstract class GumgaReadOnlyDTOAPI<T> extends AbstractReadOnlyGumgaAPI<T> {
	
	public GumgaReadOnlyDTOAPI(GumgaReadOnlyGateway<?, T> gateway) {
		super(gateway);
	}

}
