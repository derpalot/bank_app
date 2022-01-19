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
import javafx.stage.Stage;

import java.io.IOException;

public class CheckBalanceController {

    @FXML
    public Label checkBalanceSubHeading;

    @FXML
    public Label balanceAmount;

    @FXML
    public Button cancel;

    private ATM atm;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Card card;

    public void setBalanceAmount() {
        balanceAmount.setText("$ " + this.card.getCurrentAmount());
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
