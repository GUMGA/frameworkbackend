package io.gumga.presentation.menu;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Menu {
	
	private final List<MenuComponent> menu = new LinkedList<>();
	
	public Menu() {
	}
	
	public Menu(MenuComponent... itens) {
		for (MenuComponent item : itens) 
			menu.add(item);
	}
	
	public List<MenuComponent> getMenu() {
		return Collections.unmodifiableList(menu);
	}
	
	public void addItem(MenuComponent item) {
		menu.add(item);
	}
	
	public void addItem(MenuComponent item, int level) {
		boolean inserted = false;
		List<MenuComponent> flatted = flat();
		
		for (int i = flatted.size() - 1; i >= 0 && !inserted; i--) {
			if (level == 0) break;
			if (!flatted.get(i).isGrupo()) continue;
			
			MenuGrupo grupo = (MenuGrupo) flatted.get(i);
			if (grupo.getLevel() >= item.getLevel()) continue;

			grupo.addItem(item);
			inserted = true;
		}

		if (!inserted) menu.add(item);
	}
	
	public List<MenuComponent> flat() {
		List<MenuComponent> flatted = new LinkedList<>();
		
		for (MenuComponent item : menu) 
			flatted.addAll(item.flat());
		
		return flatted;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (MenuComponent menuItem : menu) {
			sb.append(menuItem);
			sb.append("\n");
		}
		
		return sb.toString();
	}

}
