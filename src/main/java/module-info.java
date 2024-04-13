module org.example.dictionaryappmaster {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires json.simple;
    requires google.cloud.translate;
    requires freetts;

    opens Controllers to javafx.fxml;
    exports Controllers;
    exports App;
    opens App to javafx.fxml;
}