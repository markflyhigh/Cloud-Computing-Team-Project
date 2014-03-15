package edu.cmu.cs.webapp.todolist6.formbean;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class ItemForm extends FormBean {
	private String action;
	private String item;
	
	public String getAction() { return action; }
	public String getItem()   { return item;   }
	
	public void setAction(String s) { action = s;        }
	public void setItem(String s)   { item   = s.trim(); }

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (action == null || action.length() == 0) {
			errors.add("Action is required");
		}

		if (item == null || item.length() == 0) {
			errors.add("Item is required");
		}

        if (item.matches(".*[<>\"].*")) errors.add("Item may not contain angle brackets or quotes");
        
        if (!action.equals("Add to Top") && !action.equals("Add to Bottom")) errors.add("Invalid action");


		return errors;
	}
}
