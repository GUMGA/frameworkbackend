package io.gumga.application.seed;

import io.gumga.application.GumgaService;
import io.gumga.core.GumgaIdable;
import io.gumga.domain.seed.AppSeed;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.URL;

public abstract class AbstractSeed<T extends GumgaIdable<ID>, ID extends Serializable> implements AppSeed {
	
	@Transactional
	public void loadSeed() throws IOException {
		URL url = Thread.currentThread().getContextClassLoader().getResource(pathFile());
		
		if (url == null)
			throw new FileNotFoundException();
		
		File file = new File(url.getFile());
		
		BufferedReader source = new BufferedReader(new FileReader(file));
		String line;
		int count = 0;

		while ((line = source.readLine()) != null) {
			String[] parts = line.split(";");
			
			service().save(createObject(parts));
			count++;
			
			if (count == 50) {
				service().forceFlush();
				count = 0;
			}
		}

		source.close();
	}
	
	public abstract GumgaService<T, ID> service();
	
	public abstract T createObject(String[] args);

	public abstract String pathFile();
	
}
