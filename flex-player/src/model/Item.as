package model{


	[Bindable]
	public class Item
	{
		
		// ------------------------------------------------------------------------	
		public function Item(identifier :String)
		{
			_id = identifier;
			_title = identifier;
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
		public function toString() :String
		{
			return _title;
		}
		
		// ------------------------------------------------------------------------
		// Private members
		private var _description :String;			
		private var _href :String;			
		private var _id :String;			
		private var _title :String;			
	}
}