package model{

import flash.events.*;
import flash.net.*;

	[Bindable]
	public class Assessment
	{
		
		// ------------------------------------------------------------------------	
		public function Assessment(location: String)
		{
			load(location);
		}
		
		// ------------------------------------------------------------------------
		public function get copyright() :String
		{
			return _copyright;
		}
		
		// ------------------------------------------------------------------------
		private function set copyright(value : String) :void
		{
			_copyright = value;
		}
		
		// ------------------------------------------------------------------------
		public function get description() :String
		{
			return _description;
		}
		
		// ------------------------------------------------------------------------
		private function set description(value : String) :void
		{
			_description = value;
		}
		
		// ------------------------------------------------------------------------
		public function get items() :Array
		{
			return _items;
		}
		
		// ------------------------------------------------------------------------
		private function set items(i : Array) :void
		{
			_items = i;
		}
		
		// ------------------------------------------------------------------------
		public function get title() :String
		{
			return _title;
		}
		
		// ------------------------------------------------------------------------
		private function set title(value : String) :void
		{
			_title = value;
		}
		
		// ------------------------------------------------------------------------
		public function get url() :String
		{
			return _url;
		}
		
		// ------------------------------------------------------------------------
		public function load(test_url :String) :void
		{
			_url = test_url.substr(0, test_url.lastIndexOf("/"));
			
			var url_request:URLRequest = new URLRequest(test_url);
			url_loader = new URLLoader( );
			// Register to be notified when the XML finishes loading
			url_loader.addEventListener(Event.COMPLETE, xml_loaded);
			// Load the XML
			url_loader.load(url_request);
		}
		
		// ------------------------------------------------------------------------
		public function reset() :void{
			
			for each(var i:Item in _items){
				i.reset();
			}
		}
		
		
		// ------------------------------------------------------------------------
		public function loaded(f :Function) :void{
			on_loaded = f;
		}
		
		
		// ------------------------------------------------------------------------
		/** Method invoked automatically when the XML finishes loading */
		private function xml_loaded(e:Event):void {
			var node:XML;
			var qti2p1NS:Namespace = new Namespace("http://www.imsglobal.org/xsd/imsqti_v2p1");

    	var assessmentTest:XML = new XML(url_loader.data);
    	// Load test metadata
     	title = assessmentTest.@title;
     	// Load items
     	_items = new Array();
     	var item: Item;
     	for each (var air:XML in assessmentTest.qti2p1NS::testPart.qti2p1NS::assessmentSection.qti2p1NS::assessmentItemRef){
     		item = new Item(air.@identifier);
     		item.href = air.@href
//     		item.title = node.imsmdNS::langstring[0].toString();
     		_items.push(item);
     	}
     	
     	on_loaded();
		}
   
	  // ------------------------------------------------------------------------
		// Private members
		private var _copyright :String;			
		private var _description :String;			
		private var _title :String;			
		private var _url :String;
		private var _items:Array;
		// The object used to load the XML
		private var url_loader:URLLoader;
		private var on_loaded:Function;
	}
}