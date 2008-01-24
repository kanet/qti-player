package model.choice
{
	import model.IItem;
	import model.IModule;
	
	import mx.containers.HBox;
	import mx.containers.VBox;
	import mx.controls.CheckBox;
	import mx.controls.RadioButton;
	import mx.controls.Text;
	import mx.core.UIComponent;
	
	public class ChoiceModule implements IModule
	{
		// ------------------------------------------------------------------------
		public function ChoiceModule(i:IItem)
		{
			_item = i;
		}

		// ------------------------------------------------------------------------
		public function load(node:XML) :void
		{
			var qti2p1NS:Namespace = new Namespace("http://www.imsglobal.org/xsd/imsqti_v2p1");
			
			prompt = node.qti2p1NS::prompt[0];
			_max_choices = node.@maxChoices;
			_shuffle = (node.@shuffle == "true");
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
				
				text_box = new Text();
				text_box.text = sc.text;
				if(_max_choices != 1){
					var check:CheckBox = new CheckBox();
					hbox.addChild(check);
				}else{
					var radio:RadioButton = new RadioButton();
					hbox.addChild(radio);
				}
				hbox.addChild(text_box);
				hbox.styleName = "simplechoice";
				box.addChild(hbox);
			}
			
			return box;			
		}
				
		// ------------------------------------------------------------------------
		// Private members
		private var _item : IItem;
		private var prompt : String;
		private var options :Array;
		private var _shuffle:Boolean;
		private var _max_choices: int; 
	}
}