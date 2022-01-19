package ATM_app.Controller;

import ATM_app.ATM;
import ATM_app.Card;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;

public class TransactionTypeController {

    @FXML
    public Button withdraw;

    @FXML
    public Button deposit;

    @FXML
    public Button checkBalance;

    @FXML
    public Button cancel;

    private ATM atm;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Card card;

    public void withdrawButtonOnAction(ActionEvent event) throws IOException {
        //Implement transition
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Withdraw.fxml"));
        root = loader.load();

        WithdrawController withdrawController = loader.getController();
        withdrawController.setATM(this.atm);
        withdrawController.setCard(this.card);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Withdraw");
        scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }

    public void depositButtonOnAction(ActionEvent event) throws IOException {
        //Implement transition
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Deposit.fxml"));
        root = loader.load();

        DepositController depositController = loader.getController();
        depositController.setATM(this.atm);
        depositController.setCard(this.card);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Deposit");
        scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }

    public void checkBalanceButtonOnAction(ActionEvent event) throws IOException {
        //Implement transition
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CheckBalance.fxml"));
        root = loader.load();

        CheckBalanceController checkBalanceController = loader.getController();
        checkBalanceController.setATM(this.atm);
        checkBalanceController.setCard(this.card);
        checkBalanceController.setBalanceAmount();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Your Balance");
        scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }

    public void cancelButtonOnAction(ActionEvent event) throws IOException {
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

    public void setATM(ATM atm) {
        this.atm = atm;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
