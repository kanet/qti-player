package model{
	import flash.net.URLLoader;
	import flash.net.URLRequest;
	import flash.utils.Dictionary;
	
	import module.IItem;
	import module.IModule;
	import module.choice.ChoiceModule;
	import module.text.TextModule;
	
	import mx.controls.Alert;
	


	[Bindable]
	public class Item implements IItem
	{
		
		// ------------------------------------------------------------------------	
		public function Item(identifier :String)
		{
			_id = identifier;
			_title = identifier;
			_is_loaded = false
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
		public function get maxScore() :int
		{
			return _correct_values.length;
		}
		
		// ------------------------------------------------------------------------
		public function get score() :int
		{
			var score:int = 0;


      for each(var id:String in _correct_values){
				if(_states[id] != null)
					score ++;
      }
      
      return score;
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
		public function get url() :String
		{
			return _href.substr(0, _href.lastIndexOf("/"));
		}
		
		// ------------------------------------------------------------------------
		public function getState(key:String) :String
		{
			return _states[key];
		}
		
		// ------------------------------------------------------------------------
		public function setState(key:String, value:String) :void
		{
			if(value != null)
				_states[key] = value;
			else
				delete _states[key];
			
		}
		
		// ------------------------------------------------------------------------
		public function reset() :void
		{
      for (var id:String in _states){
				delete _states[id];
      }
		}
		
		// ------------------------------------------------------------------------
		/**
		 * Ponieważ ladowanie danych odbywa się asynchronicznie to funkcja ta jako
		 * drugi parametr potrzebuje wskaznik do funkcji, ktora ma byc wywolana po zaladowaniu dokumentu
		 */
		public function load(server: String, f :Function) :void
		{
			on_loaded = f;
			if(_is_loaded){
				on_loaded();
				return;
			}
			
			_href = server + "/" + _href;
			var url_request:URLRequest = new URLRequest(_href);
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
    	
    	// Load response
    	_correct_values = new Array();
    	for each (var value:XML in assessmentItem.qti2p1NS::responseDeclaration.
    		qti2p1NS::correctResponse.qti2p1NS::value)
    	{
    		_correct_values.push(value.toString());
    	}
    	
     	// Load modules
     	_modules = new Array();
     	for each (var node:XML in assessmentItem.qti2p1NS::itemBody[0].*){
     		
     		var m:IModule = null;
     		
     		if(node.localName().toString() == "p")
	     		m = new TextModule(this);
	     	else if(node.localName().toString() == "choiceInteraction")
	     		m = new ChoiceModule(this);

     		if(m){
	     		m.load(node);
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
		private var _correct_values: Array;
		/** Interaction state */
		private var _states:Dictionary = new Dictionary();
		/** Interaction response */
		private var _response:Dictionary = new Dictionary();
		/** The object used to load the XML */
		private var url_loader:URLLoader;
		private var on_loaded:Function;			
	}
}