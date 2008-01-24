package model.choice
{
	import model.IModule;
	
	import mx.containers.HBox;
	import mx.containers.VBox;
	import mx.controls.RadioButton;
	import mx.controls.Text;
	import mx.core.UIComponent;
	
	public class ChoiceModule implements IModule
	{
		// ------------------------------------------------------------------------
		public function ChoiceModule()
		{
			super();
		}

		// ------------------------------------------------------------------------
		public function load(node:XML) :void
		{
			var qti2p1NS:Namespace = new Namespace("http://www.imsglobal.org/xsd/imsqti_v2p1");
			
			prompt = node.qti2p1NS::prompt[0];
			options = new Array();
			for each (var scn:XML in node.qti2p1NS::simpleChoice){
				var sc: SimpleChoice = new SimpleChoice();
				
				sc.identifier = scn.@identifier;
				sc.text = scn.*[0];
				options.push(sc);
			}
		}
		
		// ------------------------------------------------------------------------
		public function getView():UIComponent
		{
			var box:VBox = new VBox();
			var text_box:Text;
				
			text_box = new Text();
			text_box.text = prompt;
			text_box.styleName = "prompt";
			box.addChild(text_box);
			
			for each(var sc :SimpleChoice in options){
				var hbox:HBox = new HBox();
				var radio:RadioButton = new RadioButton();
				
				text_box = new Text();
				text_box.text = sc.text;
				hbox.addChild(radio);
				hbox.addChild(text_box);
				hbox.styleName = "simplechoice";
				box.addChild(hbox);
			}
			
			return box;			
		}
				
		// ------------------------------------------------------------------------
		// Private members
		private var prompt : String;
		private var options :Array; 
	}
}