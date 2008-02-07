package
{
	public class Controller
	{
		
		// ------------------------------------------------------------------------
		public function Controller(p:Player)
		{
			player = p;
		}

		// ------------------------------------------------------------------------
		public function switchToPage() :void
		{
			player.showPageView();
		}
		
		// ------------------------------------------------------------------------
		public function showTestIntro() :void
		{
			player.showStartView();
		}
		
		// ------------------------------------------------------------------------
		public function finishTest() :void
		{
			player.showSummaryView();
		}
		
		// ------------------------------------------------------------------------
		// Private members
				/** First page */
		private static const START_STATE:int = 1;
		private static const SHOW_RESULT_STATE:int = 2;
		private static const TEST_STATE:int = 3;
		private static const SHOW_ERRORS_STATE:int = 4;
		
		private var player:Player;
	}
}