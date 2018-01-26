package io.gumga.application.seed;

import io.gumga.application.GumgaService;
import io.gumga.core.GumgaIdable;
import io.gumga.domain.seed.AppSeed;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.URL;

/**
 * Classe abstrata para criação de seeds
 * @param <T> Classe a partir da qual serão gerados os seeds
 * @param <ID> Identificador da classe
 */
public abstract class AbstractSeed<T extends GumgaIdable<ID>, ID extends Serializable> implements AppSeed {

	/**
	 * Método responsável por popular uma tabela do banco de dados de acordo com a Classe T
	 * @throws IOException
	 */
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

    /**
     * Método abstrato que retorna o service utilizado para gerenciamento dos seeds
     * @return Serviço utilizado
     */
	public abstract GumgaService<T, ID> service();

    /**
     * Cria objeto que será salvo
     * @param args argumentos
     * @return Objeto
     */
	public abstract T createObject(String[] args);

    /**
     * @return Caminho do arquivo
     */
	public abstract String pathFile();
	
}
