package model.text
{
	import model.IModule;
	
	import mx.controls.Text;
	import mx.core.UIComponent;
	
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
		public function getView():UIComponent
		{
			var text_box:Text = new Text();
				
			text_box.text = _text;
			return text_box;			
		}
				
		// ------------------------------------------------------------------------
		// Private members
		private var _text : String; 
	}
}