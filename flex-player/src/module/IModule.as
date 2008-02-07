package module{
	import mx.core.UIComponent;
	
	public interface IModule
	{
		function load(node:XML):void;
		function getView():UIComponent;
	}
}