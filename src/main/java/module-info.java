module client {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.annotation;
    requires spring.context;
    requires io.netty.all;
    requires java.desktop;
    requires org.slf4j;
    requires libtiled;
    requires sdk;

    opens com.mechempire.client to javafx.fxml;
    exports com.mechempire.client;
}