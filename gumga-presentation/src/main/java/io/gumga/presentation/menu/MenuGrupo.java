package io.gumga.presentation.menu;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MenuGrupo extends MenuComponent {

	private final List<MenuComponent> itens = new LinkedList<>();
	
	public MenuGrupo(String label) {
		super(label);
	}
	
	public List<MenuComponent> getItens() {
		return Collections.unmodifiableList(itens);
	}
	
	public void addItem(MenuComponent item) {
		itens.add(item);
		Collections.sort(itens);
	}
	
	@Override
	public boolean isGrupo() {
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());

		for (MenuComponent item : itens) {
			sb.append("\n");
			sb.append(item);
		}
		
		return sb.toString();
	}

}
