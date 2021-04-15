package it.tweetmail.client;

import it.tweetmail.client.model.Model;
import it.tweetmail.client.model.ModelFactory;
import it.tweetmail.client.model.modules.observables.Observables;
import it.tweetmail.client.model.modules.observables.ObservablesManager;
import it.tweetmail.client.scene.AppScene;
import it.tweetmail.client.scene.SceneSwitcher;
import it.tweetmail.client.utils.AlertUtil;
import it.tweetmail.lib.enums.Command;
import it.tweetmail.lib.enums.MailFolder;
import it.tweetmail.lib.enums.ServerResponse;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static Model model;

    public static void main(String[] args) {
        model = ModelFactory.newInstance();
        Main.initObservables();
        AppFx.mainFx(args);
    }

    private static void initObservables() {
        ObservablesManager<Observables> obm = model.observablesManager();
        // List<Email>
        obm.newList(Observables.EMAIL_LIST);
        // String
        obm.newObject(Observables.STATUS_LABEL);
        // MailFolder Enum
        obm.newObject(Observables.CURRENT_FOLDER, MailFolder.INBOX);
        // Boolean
        obm.newObject(Observables.SERVER_STATUS);
    }

    public static Model appModel() {
        return model;
    }

    /**
     * JavaFx Application class
     */
    public final static class AppFx extends Application {
        private SceneSwitcher sceneSwitcher;
        private ScheduledExecutorService checkEmail;

        public static void mainFx(String[] args) {
            launch(args);
        }

        @Override
        public void start(Stage stage) throws IOException {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
            stage.setTitle("UniTo Prog3 - Mail Client [" + model.configManager().read("user.email") + "]");

            model.clientMail().bindServerStatus(model.observablesManager().getObject(Observables.SERVER_STATUS));
            startCheckThread();

            initSceneSwitcher(stage);
            sceneSwitcher.switchTo(AppScene.MAIN);

            stage.show();
        }

        private void initSceneSwitcher(Stage stage) throws IOException {
            sceneSwitcher = new SceneSwitcher(stage);

            sceneSwitcher.addScene(AppScene.MAIN);
            sceneSwitcher.addScene(AppScene.READ);
            sceneSwitcher.addScene(AppScene.COMPOSE);
        }

        private void startCheckThread() {
            AtomicInteger lastNotifyCount = new AtomicInteger();
            checkEmail = Executors.newSingleThreadScheduledExecutor();
            checkEmail.scheduleAtFixedRate(() ->
                    model.clientMail().sendCmd(Command.CHECK_EMAILS, (response, args) -> {
                        if (response == ServerResponse.OK) {
                            int notifyCount = Integer.parseInt(args.get(0));
                            if (notifyCount > lastNotifyCount.getAndSet(notifyCount)) {
                                AlertUtil.showInfo("There are some new emails!");
                            }
                        }
                    }), 10, 15, TimeUnit.SECONDS);
        }

        @Override
        public void stop() {
            checkEmail.shutdownNow();
            model.clientMail().close();
            model.configManager().save();
            System.exit(0);
        }
    }
}
