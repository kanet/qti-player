package
{
	public class Controller
	{
		/** First page */
		public static const START_PAGE:int = -1;
		/** Last page */
		public static const RESULT_PAGE:int = -2;
		
		// ------------------------------------------------------------------------
		public function Controller(p:Player)
		{
			player = p;
		}

		// ------------------------------------------------------------------------
		public function get index() :int
		{
			return _index;
		}
		
		
		// ------------------------------------------------------------------------
		public function switchToPage(page: int) :void
		{
			_index = page;
			
			if(index == START_PAGE){
				player.showStartView();
			}
			else if(index == RESULT_PAGE){
				player.showSummaryView();
			}
			else{
				player.showPageView();
			}
		}
		
		// ------------------------------------------------------------------------
		// Private members
		private var _index: int;
		private var player:Player;
	}
}