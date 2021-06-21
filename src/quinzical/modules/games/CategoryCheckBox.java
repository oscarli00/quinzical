package quinzical.modules.games;

import javafx.scene.control.CheckBox;
import quinzical.data.Category;

public class CategoryCheckBox extends CheckBox{
	private Category _category;
	
	public CategoryCheckBox(Category c) {
		super(c.toString());
		_category=c;
	}
	
	public Category getCategory() {
		return _category;
	}
}
