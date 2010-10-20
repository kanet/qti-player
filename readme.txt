1. Building project
Ant build uses apache ivy to retrieve required libraries. Default libraries are located in libs folder.

2. Used open source projects:
- MathJax - mathjax.org
	MathJax causes exception when used in GWTTestCase. Add -Dgwt.args="-runStyle HtmlUnit:IE6 to VM Arguments to resolve this.
	
- Flash-Ajax Video Component - http://www.adobe.com/go/favideo/
	video player

- jscssp - http://glazman.org/JSCSSP/
	javascript css parser
	
- http://code.google.com/p/google-gin
	dependency injection, requires GWT 1.5 or higher and Guice 2.0
	
- http://code.google.com/p/gwt-graphics
	cross-browser vector graphics library, uses SVG and VML for creating graphics
	
- http://code.google.com/p/gwt-voices
	provides cross-platform browser sound capabilities
	
3. QTI format
Link do Implementation Guide:
http://www.imsglobal.org/question/qtiv2p1pd2/imsqti_implv2p1pd2.html