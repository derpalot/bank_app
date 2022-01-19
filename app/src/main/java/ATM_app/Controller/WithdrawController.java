package ATM_app.Controller;

import ATM_app.ATM;
import ATM_app.Card;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
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

public class WithdrawController {

    @FXML
    public TextField withdrawalAmount;

    @FXML
    public Button withdraw;

    @FXML
    public Button cancel;

    @FXML
    public Label receiptLabel;

    private ATM atm;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Card card;

    public void withdrawButtonOnAction(ActionEvent event) throws IOException {
        //Implement logic
        String receipt = this.atm.withdraw(Double.parseDouble(withdrawalAmount.getText()), this.card);
        System.out.println(receipt);
        receiptLabel.setText(receipt);

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
