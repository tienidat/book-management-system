module org.example.eproject_2 {
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
    requires java.sql;
    requires java.desktop;

    opens org.example.eproject_2.entity to javafx.base;

    exports org.example.eproject_2.screen;
    opens org.example.eproject_2.screen to javafx.fxml;
    exports org.example.eproject_2.entity;
}