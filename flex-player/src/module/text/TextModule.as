package module.text
{
	import module.IItem;
	import module.IModule;
	import module.util.Picture;
	
	import mx.controls.Image;
	import mx.controls.Text;
	import mx.core.UIComponent;
	
	public class TextModule implements IModule
	{
		// ------------------------------------------------------------------------
		public function TextModule(i:IItem)
		{
			super();
			_item = i;
		}

		// ------------------------------------------------------------------------
		public function load(root:XML) :void
		{ 
			var qti2p1NS:Namespace = new Namespace("http://www.imsglobal.org/xsd/imsqti_v2p1");
			var node:XML = root.*[0];
			if(node.localName() == "img"){
				_text = null;
				_picture = new Picture();
				_picture.src = _item.url + "/" + node.@src;
				_picture.alt = node.@alt;
			}
			else{
				_text = node;
				_picture = null;
			}
		}
		
		// ------------------------------------------------------------------------
		public function getView():UIComponent
		{
			if(_text){
				var text_box:Text = new Text();
				text_box.text = _text;
				return text_box;			
			}
			else if(_picture){
				var img:Image = new Image();
				img.source = _picture.src;
				return img;			
			}
			
			return new Text();
		}
				
		// ------------------------------------------------------------------------
		// Private members
		private var _item : IItem;
		private var _text : String; 
		private var _picture : Picture;
		
	}
}