module JavaJXProject {
	requires javafx.controls;
	requires java.desktop;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires org.junit.jupiter.api;
	requires junit;
	requires java.base;
	requires jdk.jpackage;
	requires org.junit.platform.suite.api;
	requires org.junit.platform.runner;
	
	opens application_word_frequency to javafx.graphics, javafx.fxml;
}
