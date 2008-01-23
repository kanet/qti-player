package
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import model.Assessment;
	import model.Item;
	
	import mx.containers.Box;
	import mx.controls.Button;
	import mx.controls.Text;
	
	public class ResultView extends Box
	{
		// ------------------------------------------------------------------------
		public function ResultView(a:Assessment, c:Controller){
			super();
			assessment = a;
			controller = c;
			
			createLayout();
		}
		
		// ------------------------------------------------------------------------
		private function createLayout() : void{
			var title:Text = new Text();
			var desc:Text = new Text();
			var start_button:Button = new Button();
			var copyright:Text = new Text();

			opaqueBackground= 0xbbffbb;
			percentWidth=100;

			// Create header			
			title.text = assessment.title;
			desc.text = assessment.description;
			start_button.label = "Retake";
			start_button.addEventListener(MouseEvent.CLICK, start_test);
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