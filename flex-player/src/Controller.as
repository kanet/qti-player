package
{
	public class Controller
	{
		/** First page */
		public static const ABOUT_PAGE:int = -1;
		/** Last page */
		public static const SUMMARY_PAGE:int = -2;
		
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
			
			if(index == ABOUT_PAGE){
				player.showAboutView();
			}
			else if(index == SUMMARY_PAGE){
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