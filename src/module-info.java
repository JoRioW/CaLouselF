
module CaLouselF {
	requires javafx.graphics;
	requires java.sql;
	requires javafx.controls;
	requires javafx.base;
	
	
	opens model to javafx.base;
	
	opens main;
}