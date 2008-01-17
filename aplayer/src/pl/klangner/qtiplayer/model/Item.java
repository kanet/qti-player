package pl.klangner.qtiplayer.model;


public class Item {

	// --------------------------------------------------------------------------
	protected Item(String id, String href){
		
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
	private String description;
	private String href;
	private String id;
	private String title;
	
}
