package
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import model.Assessment;
	import model.Item;
	
	import mx.containers.Box;
	import mx.controls.Button;
	import mx.controls.Text;
	
	public class PageView extends Box
	{
		// ------------------------------------------------------------------------
		public function PageView(a:Assessment, c:Controller){
			super();
			assessment = a;
			controller = c;
			
			item = assessment.items[controller.index];
			createLayout();
		}
		
		// ------------------------------------------------------------------------
		private function createLayout() : void{
			var header:Box = new Box();
			var body:Box = new Box();
			var footer:Box = new Box();
			var atitle:Text = new Text();
			var ititle:Text = new Text();
			var prev_button:Button = new Button();
			var next_button:Button = new Button();
			var finish_button:Button = new Button();
			var counter:Text = new Text();

			opaqueBackground= 0xbbffbb;
			percentWidth=100;

			// Create header			
			header.direction="horizontal";
			header.percentWidth=100;
			header.opaqueBackground= 0xbbbbff;
			atitle.text = assessment.title;
			header.addChild(atitle);
			
			// Create body
			ititle.text = item.title;
			body.direction="horizontal";
			body.percentWidth=100;
			body.addChild(ititle);
			
			// Create footer			
			footer.direction="horizontal";
			footer.percentWidth=100;
			footer.opaqueBackground= 0xffbbbb;
			prev_button.label = "Previous";
			prev_button.addEventListener(MouseEvent.CLICK, prev_page);
			if( controller.index == 0)
				prev_button.enabled = false;
			footer.addChild(prev_button);
			counter.text = controller.index + "/" + assessment.items.length;
			footer.addChild(counter);
			finish_button.label = "Finish";
			finish_button.addEventListener(MouseEvent.CLICK, result_page);
			footer.addChild(finish_button);
			next_button.label = "Next";
			next_button.addEventListener(MouseEvent.CLICK, next_page);
			if( controller.index+1 == assessment.items.length)
				next_button.enabled = false;
			footer.addChild(next_button);
			
			// Add panels
			addChild(header);
			addChild(body);
			addChild(footer);
				
		}

		// ------------------------------------------------------------------------
		private function prev_page(e:Event): void{
			controller.switchToPage(controller.index-1);
		}
		
		// ------------------------------------------------------------------------
		private function next_page(e:Event): void{
			controller.switchToPage(controller.index+1);
		}
		
		// ------------------------------------------------------------------------
		private function result_page(e:Event): void{
			controller.switchToPage(Controller.RESULT_PAGE);
		}
		
		// ------------------------------------------------------------------------
		// Private memberbs
    private var assessment:Assessment;
    private var controller:Controller;
		private var item:Item;
	}
}