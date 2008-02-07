package module
{
	
	public interface IItem
	{
		/** Return URL without file name of this item */
		function get url() :String;
		function getState(key:String) :String;
		function setState(key:String, value:String) :void;
	}
}