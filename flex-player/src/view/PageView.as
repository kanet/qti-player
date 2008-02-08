package view{
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import model.Assessment;
	import model.Item;
	
	import module.IModule;
	
	import mx.containers.Box;
	import mx.containers.Canvas;
	import mx.containers.HBox;
	import mx.containers.VBox;
	import mx.controls.Button;
	import mx.controls.Text;
	
	public class PageView extends Canvas
	{
		// ------------------------------------------------------------------------
		public function PageView(a:Assessment, c:Controller){
			super();
			assessment = a;
			controller = c;
			createLayout();
			styleName = "pageview";
			loadPage(0);
		}
		
		// ------------------------------------------------------------------------
		private function createLayout() : void{
			var content:Box = new VBox();
			var header:Box = new HBox();
			var footer:Box = new HBox();
			var atitle:Text = new Text();
			var counter:Text = new Text();

			// Create header			
			atitle.text = assessment.title;
			header.addChild(atitle);
			prev_button.label = "<";
			prev_button.addEventListener(MouseEvent.CLICK, prevPage);
			header.addChild(prev_button);
			counter.text = (currentPage+1) + "/" + assessment.items.length;
			header.addChild(counter);
			next_button.label = ">";
			next_button.addEventListener(MouseEvent.CLICK, nextPage);
			header.addChild(next_button);
			header.styleName = "pageheader";
			
			// Create footer			
			footer.addChild(submit_button);
			footer.styleName = "pagefooter";
			
			// Add panels
			_body.styleName = "pagebody";
			content.addChild(header);
			content.addChild(_body);
			content.addChild(footer);
			addChild(content);
				
		}

		// ------------------------------------------------------------------------
		private function createBody() : void{
			
			// Create body
			_body.removeAllChildren();
			for each(var m:IModule in item.modules){
				_body.addChild(m.getView());
			}
			
			prev_button.enabled = (currentPage > 0);
			if( currentPage+1 < assessment.items.length){
				next_button.enabled = true;
				submit_button.label = "Next question";
				submit_button.removeEventListener(MouseEvent.CLICK, resultPage);
				submit_button.addEventListener(MouseEvent.CLICK, nextPage);
			}
			else{
				next_button.enabled = false;
				submit_button.label = "Finish test";
			submit_button.removeEventListener(MouseEvent.CLICK, nextPage);
				submit_button.addEventListener(MouseEvent.CLICK, resultPage);
			}
			
		}

		// ------------------------------------------------------------------------
		private function loadPage(index:int) : void{
			
			currentPage = index;
			item = assessment.items[currentPage];
			item.load(assessment.url, createBody);
		}
		
		// ------------------------------------------------------------------------
		private function prevPage(e:Event): void{
			loadPage(currentPage-1);
		}
		
		// ------------------------------------------------------------------------
		private function nextPage(e:Event): void{
			loadPage(currentPage+1);
		}
		
		// ------------------------------------------------------------------------
		private function resultPage(e:Event): void{
			controller.finishTest();
		}
		
		// ------------------------------------------------------------------------
		// Private memberbs
    private var assessment:Assessment;
    private var controller:Controller;
		private var item:Item;
		private var currentPage:int;
		private var _body:Box = new VBox();
		private	var prev_button:Button = new Button();
		private var next_button:Button = new Button();
		private var submit_button:Button = new Button();
		
	}
}