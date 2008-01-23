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
		public function load(location :String) :void
		{
			_url = location;
			
			var url_request:URLRequest = new URLRequest(location + "/imsmanifest.xml");
			url_loader = new URLLoader( );
			// Register to be notified when the XML finishes loading
			url_loader.addEventListener(Event.COMPLETE, xml_loaded);
			// Load the XML
			url_loader.load(url_request);
		}
		
		// ------------------------------------------------------------------------
		public function loaded(f :Function) :void{
			on_load = f;
		}
		
		
		// ------------------------------------------------------------------------
		/** Method invoked automatically when the XML finishes loading */
		private function xml_loaded(e:Event):void {
			var node:XML;
			var cp1p1NS:Namespace = new Namespace("http://www.imsglobal.org/xsd/imscp_v1p1");
			var imsmdNS:Namespace = new Namespace("http://www.imsglobal.org/xsd/imsmd_v1p2");
    	var manifest:XML = new XML(url_loader.data);
    	// Load test metadata
    	node = manifest.cp1p1NS::metadata.imsmdNS::lom.imsmdNS::general.imsmdNS::title[0];
     	title = node.imsmdNS::langstring[0].toString();
    	node = manifest.cp1p1NS::metadata.imsmdNS::lom.imsmdNS::general.imsmdNS::description[0];
     	description = node.imsmdNS::langstring[0].toString();
    	node = manifest.cp1p1NS::metadata.imsmdNS::lom.imsmdNS::rights.imsmdNS::description[0];
     	copyright = node.imsmdNS::langstring[0].toString();
     	// Load items
     	_items = new Array();
     	var item: Item;
     	for each (var resource:XML in manifest.cp1p1NS::resources.cp1p1NS::resource){
     		item = new Item(resource.@identifier);
     		item.href = resource.@href
    		node = resource.cp1p1NS::metadata.imsmdNS::lom.imsmdNS::general.imsmdNS::title[0];
     		item.title = node.imsmdNS::langstring[0].toString();
     		_items.push(item);
     	}
     	
     	on_load();
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
		private var on_load:Function;
	}
}