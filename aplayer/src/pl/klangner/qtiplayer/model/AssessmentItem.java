package pl.klangner.qtiplayer.model;

import java.util.ArrayList;
import java.util.List;


public class AssessmentItem {

	// --------------------------------------------------------------------------
	protected AssessmentItem(String id, String href){
		
		this.id = id;
		this.title = id;
		this.href = href;
	}
	
	// --------------------------------------------------------------------------
	public String getDescription(){
		return description;
	}
	
	// --------------------------------------------------------------------------
	public String getHref(){
		return href;
	}
	
	// --------------------------------------------------------------------------
	public String getId(){
		return id;
	}
	
	// --------------------------------------------------------------------------
	public List<Module> getModules(){
		return modules;
	}
	
	// --------------------------------------------------------------------------
	public String getTitle(){
		return title;
	}
	
	// --------------------------------------------------------------------------
	public String toString(){
		return title;
	}
	
	// --------------------------------------------------------------------------
	protected void setDescritpion(String desc) {
		this.description = desc;
	}

	// --------------------------------------------------------------------------
	protected void setTitle(String new_title){
		this.title = new_title;
	}
	
	
	// --------------------------------------------------------------------------
	// Private members
	private String 				description;
	private String 				href;
	private String 				id;
	private boolean				is_loaded = false;
	private String 				title;
	private List<Module>	modules = new ArrayList<Module>();
	
}
