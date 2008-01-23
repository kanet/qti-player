package
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import model.Assessment;
	
	import mx.containers.VBox;
	import mx.controls.Button;
	import mx.controls.Text;
	
	public class StartView extends VBox
	{
		// ------------------------------------------------------------------------
		public function StartView(a:Assessment, c:Controller){
			super();
			assessment = a;
			controller = c;
			styleName = "startview";
			
			createLayout();
		}
		
		// ------------------------------------------------------------------------
		private function createLayout() : void{
			var title:Text = new Text();
			var desc:Text = new Text();
			var start_button:Button = new Button();
			var copyright:Text = new Text();

			// Create header			
			title.text = assessment.title;
			desc.text = assessment.description;
			start_button.label = "Start";
			start_button.addEventListener(MouseEvent.CLICK, start_test);
			start_button.setStyle("fontSize",24);
			copyright.text = assessment.copyright;
			
			addChild(title);
			addChild(desc);
			addChild(start_button);
			addChild(copyright);
			
		}

		// ------------------------------------------------------------------------
		private function start_test(e:Event): void{
			controller.switchToPage(0);
		}
		
		// ------------------------------------------------------------------------
		// Private memberbs
    private var assessment:Assessment;
    private var controller:Controller;
	}
}