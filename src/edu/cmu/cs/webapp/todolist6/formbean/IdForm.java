package edu.cmu.cs.webapp.todolist6.formbean;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class IdForm extends FormBean {
	private String id;

	public String getId() { return id;    }
	
	public int getIdAsInt() {
		// Be sure to first call getValidationErrors() to ensure
		// that NullPointer exception or NumberFormatException will not be thrown!
		return Integer.parseInt(id);
	}
	
	public void setId(String id) { this.id = id; }

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();
		
		if (id == null || id.length() == 0) {
			errors.add("Id is required");
			return errors;
		}

		try {
			Integer.parseInt(id);
		} catch (NumberFormatException e) {
			errors.add("Id is not an integer");
		}
		
		return errors;
	}
}
