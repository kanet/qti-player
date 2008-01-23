package module
{
	public class TextModule implements IModule
	{
		// ------------------------------------------------------------------------
		public function TextModule()
		{
			super();
		}

		// ------------------------------------------------------------------------
		public function load(node:XML) :void
		{
			_text = node.*[0];
		}
				
		// ------------------------------------------------------------------------
		public function get text() :String
		{
			return _text;
		}
				
		// ------------------------------------------------------------------------
		// Private members
		private var _text : String; 
	}
}