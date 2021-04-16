package it.tweetmail.client.controllers;

import it.tweetmail.client.controllers.components.ClientStatusLabel;
import it.tweetmail.client.controllers.components.EmailTableView;
import it.tweetmail.client.controllers.components.ServerStatusDot;
import it.tweetmail.client.model.modules.client.ClientMail;
import it.tweetmail.client.model.modules.observables.Observables;
import it.tweetmail.client.scene.AppScene;
import it.tweetmail.client.utils.AlertUtil;
import it.tweetmail.lib.enums.Command;
import it.tweetmail.lib.enums.MailFolder;
import it.tweetmail.lib.enums.ServerResponse;
import it.tweetmail.lib.objects.Email;
import it.tweetmail.lib.utils.MailUtil;
import it.tweetmail.lib.utils.Serializer;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public final class MainController extends BaseController {
    @FXML
    private Button inboxFolderBtn;

    @FXML
    private Button outboxFolderBtn;

    @FXML
    private Button trashFolderBtn;

    @FXML
    private TableView<Email> emailTableView;

    @FXML
    private Label clientStatusLabel;

    @FXML
    private Circle serverStatusCircle;

    @FXML
    private Text folderTitle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Check for config.properties "user.email" value
        if (!MailUtil.validate(getCurrentUser())) {
            AlertUtil.showError("The configuration file contains an invalid email, correct the error and restart the application.");
            System.exit(1);
        }

        // Set CurrentFolder listener
        SimpleObjectProperty<MailFolder> currentFolder = observablesManager().getObject(Observables.CURRENT_FOLDER);
        currentFolder.addListener((observable, oldValue, newValue) -> {
            inboxFolderBtn.setDisable(newValue == MailFolder.INBOX);
            outboxFolderBtn.setDisable(newValue == MailFolder.OUTBOX);
            trashFolderBtn.setDisable(newValue == MailFolder.TRASH);
            loadEmailList(newValue);

            String mFolderTitle = newValue.toString();
            folderTitle.setText(mFolderTitle.substring(0,1).toUpperCase() + mFolderTitle.substring(1).toLowerCase());
        });

        // Init emails tableview
        EmailTableView.bind(emailTableView);
        emailTableView.setRowFactory(param -> new TableRow<>() {
            @Override
            protected void updateItem(Email item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) return;

                // Row style (Email read or not)
                setStyle(item.hasBeenRead() ? "" : "-fx-font-weight: bold");

                // Row tooltip
                String tooltipDate = EmailTableView.simpleDateFormat.format(Date.from(Instant.ofEpochSecond(item.getDateSent())));
                Tooltip rowToolTip = new Tooltip("Subject: " + item.getSubject()
                        + "\nSender: " + item.getSender()
                        + "\nRecipients: " + String.join(", ", item.getRecipients())
                        + "\nDate: " + tooltipDate);
                rowToolTip.setStyle("-fx-font-weight: normal");
                setTooltip(rowToolTip);

                // Row double click
                setOnMouseClicked(event -> {
                    if (event.getButton() != MouseButton.PRIMARY) return;
                    if (event.getClickCount() < 2) return;
                    Email email = emailTableView.getSelectionModel().getSelectedItem();
                    loadEmail(email.getUUID(), (response, args) -> {
                        if (response != ServerResponse.OK) {
                            setStatusLabel(response.toString());
                            return;
                        }
                        ReadController composeController = (ReadController) sceneSwitcher().getController(AppScene.READ);
                        composeController.loadEmailView(Serializer.readFromB64(args.get(0)));

                        sceneSwitcher().switchTo(AppScene.READ);
                        setStatusLabel(BaseController.waitingForInput);

                        setStyle("");
                        getItem().setToRead(false);
                    });
                });
            }
        });

        // Init server status dot
        ServerStatusDot.bind(serverStatusCircle);

        // Init status label
        ClientStatusLabel.bind(clientStatusLabel);

        // Load emails from server
        loadEmailList(getCurrentFolder());
    }

    @FXML
    public void changeFolder(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        observablesManager().setObjectValue(Observables.CURRENT_FOLDER, MailFolder.fromString(button.getText()));
    }

    @FXML
    public void refreshAction(ActionEvent actionEvent) {
        loadEmailList(getCurrentFolder());
    }

    @FXML
    public void composeAction(ActionEvent actionEvent) {
        sceneSwitcher().switchTo(AppScene.COMPOSE);
        setStatusLabel(BaseController.waitingForInput);
    }

    private void loadEmailToCompose(String emailUUID, int mode) {
        loadEmail(emailUUID, (response, args) -> {
            if (response != ServerResponse.OK) {
                setStatusLabel(response.toString());
                return;
            }

            ComposeController composeController = (ComposeController) sceneSwitcher().getController(AppScene.COMPOSE);
            composeController.loadEmailView(Serializer.readFromB64(args.get(0)), mode);
            sceneSwitcher().switchTo(AppScene.COMPOSE);
        });
    }

    private void loadEmail(String emailUUID, ClientMail.ResponseHandler responseHandler) {
        clientMail().sendCmd(Command.READ_EMAIL, responseHandler, getCurrentFolder().name(), emailUUID);
    }

    private void loadEmailList(MailFolder folder) {
        setStatusLabel("Loading...");
        observablesManager().clearList(Observables.EMAIL_LIST);

        clientMail().sendCmd(Command.LIST_EMAILS, (response, args) -> {
            if (response != ServerResponse.OK) {
                setStatusLabel(response.toString());
                AlertUtil.showError(response.toString());
                return;
            }

            ArrayList<Email> retrieved = Serializer.readFromB64(args.get(0));
            observablesManager().fillList(Observables.EMAIL_LIST, retrieved);
            setStatusLabel("Emails list loaded!");
        }, folder.name());
    }
}
