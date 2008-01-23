package
{
	import model.Assessment;
	
	import mx.containers.Box;

	public class View extends Box
	{
    [Bindable]
    public var assessment:Assessment;
    [Bindable]
    public var controller:Controller;

		public function View(){
			percentWidth = 100; 
			direction = "vertical"; 
		}
		
		
	}
}