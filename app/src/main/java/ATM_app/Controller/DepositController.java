package ATM_app.Controller;

import ATM_app.ATM;
import ATM_app.Card;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.util.HashMap;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;

public class DepositController {

    @FXML
    public TextField noteAmountOneHundred;

    @FXML
    public TextField noteAmountFifty;

    @FXML
    public TextField noteAmountTwenty;

    @FXML
    public TextField noteAmountTen;

    @FXML
    public TextField noteAmountFive;

    @FXML
    public Button deposit;

    @FXML
    public Button cancel;

    @FXML
    public Label receiptLabel;

    private ATM atm;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Card card;

    public void depositButtonOnAction(ActionEvent event) throws IOException {
        Integer hundredAmount = Integer.parseInt(noteAmountOneHundred.getText());
        Integer fiftyAmount = Integer.parseInt(noteAmountFifty.getText());
        Integer twentyAmount = Integer.parseInt(noteAmountTwenty.getText());
        Integer tenAmount = Integer.parseInt(noteAmountTen.getText());
        Integer fiveAmount = Integer.parseInt(noteAmountFive.getText());

        HashMap<Double, Integer> noteQuantities = new HashMap<>();
        noteQuantities.put(100.00, hundredAmount);
        noteQuantities.put(50.00, fiftyAmount);
        noteQuantities.put(20.00, twentyAmount);
        noteQuantities.put(10.00, tenAmount);
        noteQuantities.put(5.00, fiveAmount);

        String receipt = this.atm.deposit(this.card, noteQuantities);
        System.out.println(receipt);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Welcome.fxml"));
        root = loader.load();

        WelcomeController welcomeController = loader.getController();
        welcomeController.setATM(this.atm);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("XYZ ATM Banking");
        scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }

    public void cancelButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TransactionType.fxml"));
        root = loader.load();

        TransactionTypeController transactionTypeController = loader.getController();
        transactionTypeController.setATM(this.atm);
        transactionTypeController.setCard(this.card);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Transaction Selection");
        scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }

    public void setATM(ATM atm) {
        this.atm = atm;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
