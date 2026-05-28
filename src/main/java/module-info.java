module com.drxgb.aurorasheetreader {
	requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;

    opens com.drxgb.aurorasheetreader to javafx.fxml;
    opens com.drxgb.aurorasheetreader.controller to javafx.fxml;
    
    exports com.drxgb.aurorasheetreader;
}
