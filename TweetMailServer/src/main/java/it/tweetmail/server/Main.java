package it.tweetmail.server;

import java.net.URL;
import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import it.tweetmail.server.modules.Model;
import it.tweetmail.server.modules.ModelFactory;
import it.tweetmail.server.server.Server;
import it.tweetmail.server.server.ServerFactory;

public class Main {

    private static Model model;
    private static Server server;

    public static void main(String[] args) {
        Main.initApp();
        AppFx.mainFx(args);
    }

    private static void initApp() {
        model = ModelFactory.newInstance();
        server = ServerFactory.newInstance();

        model.observablesManager().newList("console_log");
    }

    public static Model appModel() {
        return model;
    }

    public static void printLn(String s) {
        model.observablesManager().addListEntry("console_log", s);
    }

    public final static class AppFx extends Application {
        public static void mainFx(String[] args) {
            launch(args);
        }

        @Override
        public void start(Stage stage) throws Exception {
            server.start();

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("console_scene.fxml")));
            Scene master = new Scene(root);

            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png"))));
            stage.setTitle("UniTo Prog3 - Mail Server");
            stage.setResizable(false);
            stage.setScene(master);
            stage.show();
        }

        @Override
        public void stop() {
            ((Thread) server).interrupt();
            model.configManager().save();
            System.exit(0);
        }
    }
}
