package ATM_app.Controller;

import ATM_app.ATM;
import ATM_app.Card;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController {

    @FXML
    public TextField cardNumberInput;

    @FXML
    public TextField pinInput;

    @FXML
    public Button submit;

    @FXML
    public Label message;

    @FXML
    public Button maintenance;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private ATM atm;

    @FXML
    public void submitButtonOnAction(ActionEvent event) throws IOException {
        String cardNumber = cardNumberInput.getText();
        String pinNumber = pinInput.getText();

        //Checking if card is valid
        Card card = atm.checkCardNum(cardNumber);
        if (card != null) {
            if (card.checkDetails(cardNumber, pinNumber)) {
                if (!card.isStolenOrLost()) {
                    if (card.inDate()) {
                        if (!card.isBlocked()) {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TransactionType.fxml"));
                            root = loader.load();

                            TransactionTypeController transactionTypeController = loader.getController();
                            transactionTypeController.setATM(this.atm);
                            transactionTypeController.setCard(card);

                            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stage.setTitle("Transaction Selection");
                            scene = new Scene(root);
                            stage.setScene(scene);

                            stage.show();
                        }
                        else {
                            message.setText("This card has been blocked. ");
                        }
                    }
                    else {
                        message.setText("Card has expired or start date has not arrived. ");
                    }
                }
                else {
                    message.setText("Card has been reported lost or stolen, sorry. ");
                    this.atm.removeCard(card.getCardNumber());
                }
            }
            else {
                int attemptsRemaining = card.getNumAttempts();
                if (attemptsRemaining < 1) {
                    card.block();
                    message.setText("This card has been blocked. ");
                } else {
                    message.setText("Incorrect PIN. You have " + attemptsRemaining + " attempts remaining. ");
                }
            }
        }
        else {
            //no card of card number: cardNumber"
            message.setText("No card exists with card number: " + cardNumber);
        }
    }

    @FXML
    public void maintenanceButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Maintenance.fxml"));
        root = loader.load();

        MaintenanceController maintenanceController = loader.getController();
        maintenanceController.setATM(this.atm);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("ATM Maintenance");
        scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }

    public void setATM(ATM atm) {
        this.atm = atm;
    }

}
