package module.choice{
	
	import flash.events.Event;
	
	import module.IItem;
	import module.IModule;
	
	import mx.containers.HBox;
	import mx.containers.VBox;
	import mx.controls.Button;
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
			
			// Randomize options
			if(_shuffle){
 				var temp:Array = new Array();
				for each(var i:SimpleChoice in options){
					temp.push(i);
				}

				options = new Array();
				while (temp.length > 0){
					var index:int = int(Math.random()*temp.length);
					options.push(temp[index]);
					temp.splice(index,1);
				}
			}
		}
		
		// ------------------------------------------------------------------------
		public function getView():UIComponent
		{
			var box:VBox = new VBox();
			var text_box:Text;
			var	button:Button;
				
			text_box = new Text();
			text_box.text = prompt;
			text_box.styleName = "prompt";
			box.addChild(text_box);
			
			for each(var sc :SimpleChoice in options){
				var hbox:HBox = new HBox();
				
				text_box = new Text();
				text_box.text = sc.text;
				if(_max_choices != 1){
					button = new CheckBox();
				}else{
					button = new RadioButton();
				}
				
				button.id = sc.identifier;
				button.addEventListener(Event.CHANGE, optionClicked);
				button.selected = (_item.getState(button.id) != null);					
				hbox.addChild(button);
				hbox.addChild(text_box);
				hbox.styleName = "simplechoice";
				box.addChild(hbox);
			}
			
			return box;			
		}
				
		// ------------------------------------------------------------------------
		private function optionClicked(e:Event): void{
			var button:Button = Button(e.target);
			
			_item.setState(button.id, "1");
			if(_max_choices == 1 && last_button){
				_item.setState(last_button.id, null);
			}
			
			last_button = button;
		}
		
		// ------------------------------------------------------------------------
		// Private members
		private var _item : IItem;
		private var prompt : String;
		private var options :Array;
		private var _shuffle:Boolean;
		private var _max_choices: int;
		/** Last selected button */
		private var last_button:Button; 
	}
}