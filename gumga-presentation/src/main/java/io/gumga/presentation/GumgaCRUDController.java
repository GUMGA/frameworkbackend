package io.gumga.presentation;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

public abstract class GumgaCRUDController extends GumgaController {
	
	public abstract String path();
	
	@RequestMapping
	public String index(Model model) {
		return path() + "/base";
	}

	@RequestMapping("/list")
	public String grid(Model model) {
		return path() + "/list";
	}

	@RequestMapping("/form")
	public String form(Model model) {
		return path() + "/form";
	}

}
