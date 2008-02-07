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
			var header:Box = new HBox();
			var footer:Box = new HBox();
			var atitle:Text = new Text();
			var finish_button:Button = new Button();
			var counter:Text = new Text();

			// Create header			
			atitle.text = assessment.title;
			header.addChild(atitle);
			header.height = 50;
			header.styleName = "pageheader";
			
			// Create footer			
			prev_button.label = "Previous";
			prev_button.addEventListener(MouseEvent.CLICK, prevPage);
			footer.addChild(prev_button);
			counter.text = (currentPage+1) + "/" + assessment.items.length;
			footer.addChild(counter);
			finish_button.label = "Finish";
			finish_button.addEventListener(MouseEvent.CLICK, resultPage);
			footer.addChild(finish_button);
			next_button.label = "Next";
			next_button.addEventListener(MouseEvent.CLICK, nextPage);
			footer.addChild(next_button);
			footer.height = 50;
			footer.styleName = "pagefooter";
			
			// Add panels
			_body.styleName = "pagebody";
			addChild(header);
			addChild(_body);
			addChild(footer);
				
		}

		// ------------------------------------------------------------------------
		private function createBody() : void{
			
			// Create body
			_body.removeAllChildren();
			for each(var m:IModule in item.modules){
				_body.addChild(m.getView());
			}
			
			prev_button.enabled = (currentPage > 0);
			next_button.enabled = ( currentPage+1 < assessment.items.length);
			
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
		
	}
}