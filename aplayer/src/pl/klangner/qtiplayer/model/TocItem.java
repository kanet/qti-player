package pl.klangner.qtiplayer.model;


public class TocItem {

	// --------------------------------------------------------------------------
	protected TocItem(String id, String href){
		
		this.id = id;
		this.title = id;
		this.href = href;
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
	protected void setTitle(String new_title){
		this.title = new_title;
	}
	
	
	// --------------------------------------------------------------------------
	// Private members
	private String id;
	private String title;
	private String href;
	
}
