package io.gumga.application.clock;

import io.gumga.domain.config.Dev;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Date;

@Dev 
@Primary
@Component 
public class RelogioTeste extends RelogioImpl {

	private Date date;
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public Date now() {
		return date == null ? super.now() : date;
	}

}
