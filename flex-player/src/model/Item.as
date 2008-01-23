package model{
	import flash.net.URLLoader;
	import flash.net.URLRequest;
	
	import module.IModule;
	import module.TextModule;
	


	[Bindable]
	public class Item
	{
		
		// ------------------------------------------------------------------------	
		public function Item(identifier :String)
		{
			_id = identifier;
			_title = identifier;
			_is_loaded = false
		}
		
		// ------------------------------------------------------------------------
		public function get description() :String
		{
			return _description;
		}
		
		// ------------------------------------------------------------------------
		public function set description(value : String) :void
		{
			_description = value;
		}
		
		// ------------------------------------------------------------------------
		public function get href() :String
		{
			return _href;
		}
		
		// ------------------------------------------------------------------------
		public function set href(value : String) :void
		{
			_href = value;
		}
		
		// ------------------------------------------------------------------------
		public function get modules() :Array
		{
			return _modules;
		}
		
		// ------------------------------------------------------------------------
		public function get title() :String
		{
			return _title;
		}
		
		// ------------------------------------------------------------------------
		public function set title(value : String) :void
		{
			_title = value;
		}
		
		// ------------------------------------------------------------------------
		public function load(server: String, f :Function) :void
		{
			on_loaded = f;
			if(_is_loaded){
				on_loaded();
				return;
			}
			
			var url_request:URLRequest = new URLRequest(server + "/" + href);
			url_loader = new URLLoader( );
			// Register to be notified when the XML finishes loading
			url_loader.addEventListener(Event.COMPLETE, xml_loaded);
			// Load the XML
			url_loader.load(url_request);

		}
		
		// ------------------------------------------------------------------------
		public function toString() :String
		{
			return _title;
		}
		
		// ------------------------------------------------------------------------
		public function load_data() :void
		{
			
		}
		
		// ------------------------------------------------------------------------
		/** Method invoked automatically when the XML finishes loading */
		private function xml_loaded(e:Event):void {
			var qti2p1NS:Namespace = new Namespace("http://www.imsglobal.org/xsd/imsqti_v2p1");
    	var assessmentItem:XML = new XML(url_loader.data);
    	
     	// Load modules
     	_modules = new Array();
     	for each (var node:XML in assessmentItem.qti2p1NS::itemBody[0].*){
     		
     		var m:IModule = null;
     		
     		if(node.localName().toString() == "p"){
	     		m = new TextModule();
	     		m.load(node);
	     	}

     		if(m){
	     		_modules.push(m);
	     	}
     	}
     	
     	_is_loaded = true;
     	on_loaded();
		}
   
		// ------------------------------------------------------------------------
		// Private members
		private var _description :String;			
		private var _href :String;			
		private var _id :String;			
		private var _title :String;
		private var _is_loaded :Boolean;
		private var _modules: Array;
		/** The object used to load the XML */
		private var url_loader:URLLoader;
		private var on_loaded:Function;			
	}
}