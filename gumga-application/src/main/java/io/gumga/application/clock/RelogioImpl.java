package io.gumga.application.clock;

import io.gumga.core.Relogio;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RelogioImpl implements Relogio {

	@Override
	public Date now() {
		return new Date();
	}
	
}
