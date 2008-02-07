package view{
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import model.Assessment;
	import model.Item;
	
	import mx.containers.Box;
	import mx.containers.Grid;
	import mx.containers.GridItem;
	import mx.containers.GridRow;
	import mx.controls.Button;
	import mx.controls.Text;
	
	public class ResultView extends Box
	{
		// ------------------------------------------------------------------------
		public function ResultView(a:Assessment, c:Controller){
			super();
			assessment = a;
			controller = c;
			
			styleName = "resultview";
			createLayout();
		}
		
		// ------------------------------------------------------------------------
		private function createLayout() : void{
			var title:Text = new Text();
			var result_box:Text = new Text();
			var reset_button:Button = new Button();

			// Create header			
			title.text = assessment.title;
			reset_button.label = "Reset";
			reset_button.addEventListener(MouseEvent.CLICK, startTest);
			
			var score:int = 0;
			var max:int = 0;
			for each(var item:Item in assessment.items){
				score += item.score;
				max += item.maxScore;
			}
			
			result_box.text = "You scored: " + (score/max*100) + "% (" + score + "/" + max +" points)";
			
			addChild(title);
			addChild(result_box);
			addChild(createResultGrid());
			addChild(reset_button);
			
		}

		// ------------------------------------------------------------------------
		private function createResultGrid() : Grid{
			var grid:Grid = new Grid();
			var row:GridRow;

			// Create header
			row = new GridRow();
			row.addChild(createCell("Page name"));
			row.addChild(createCell("Score"));
			row.addChild(createCell("Max score"));
			row.styleName = "resultgridheader";
			grid.addChild(row);
			
			// Add pages
      for each(var item:Item in assessment.items){
				row = new GridRow();
				row.addChild(createCell(item.title));
				row.addChild(createCell(item.score.toString()));
				row.addChild(createCell(item.maxScore.toString()));
				grid.addChild(row);
      }
      
      grid.styleName = "resultgrid";
			return grid;
		}

		// ------------------------------------------------------------------------
		private function createCell(label:String) : GridItem{
			var cell:GridItem = new GridItem();
			var text:Text = new Text();
			
			text.text = label;
			cell.addChild(text);
			return cell;
		}
		
		// ------------------------------------------------------------------------
		private function startTest(e:Event): void{
			controller.switchToPage(0);
		}
		
		// ------------------------------------------------------------------------
		// Private memberbs
    private var assessment:Assessment;
    private var controller:Controller;
	}
}