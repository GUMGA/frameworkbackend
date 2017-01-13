package io.gumga.application;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService extends GumgaService<Company, Long> {

	@Autowired
	public CompanyService(CompanyRepository repository) {
		super(repository);
	}
	
	@Override
	public void afterSave(Company entity) {
		System.out.println("after save");
	}

	@Override
	public void afterUpdate(Company entity) {
		System.out.println("after update");
	}
	
}
