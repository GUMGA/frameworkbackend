package io.gumga.presentation.menu;

import java.util.LinkedList;
import java.util.List;

public abstract class MenuComponent implements Comparable<MenuComponent> {

	private final String label;
	private int level;
	private String id;
	private String clazz;

	public MenuComponent(String label) {
		this.label = label;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public String getLabel() {
		return label;
	}

	public boolean isGrupo() {
		return false;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getClazz() {
		return clazz;
	}

        @Override
	public int compareTo(MenuComponent otherMenuItem) {
		return label.compareTo(otherMenuItem.getLabel());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < level; i++)
			sb.append("\t");

		sb.append(label);

		return sb.toString();
	}

	public List<MenuComponent> flat() {
		List<MenuComponent> flatted = new LinkedList<>();
		flatted.add(this);

		if (isGrupo())
			for (MenuComponent menuItem : ((MenuGrupo) this).getItens())
				flatted.addAll(menuItem.flat());

		return flatted;
	}

}
