package ATM_app.Controller;

import ATM_app.ATM;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class MaintenanceController {

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
    public TextField dollarAmountTwo;

    @FXML
    public TextField dollarAmountOne;

    @FXML
    public TextField centAmountFifty;

    @FXML
    public TextField centAmountTwenty;

    @FXML
    public TextField centAmountTen;

    @FXML
    public TextField centAmountFive;

    @FXML
    public TextField maintenancePinInput;

    @FXML
    public Button add;

    @FXML
    public Button cancel;

    private Parent root;
    private Stage stage;
    private Scene scene;
    private ATM atm;


    public void addButtonOnAction(ActionEvent event) throws IOException {
        String maintenancePin = maintenancePinInput.getText();

        Integer hundredNoteAmount = Integer.parseInt(noteAmountOneHundred.getText());
        Integer fiftyNoteAmount = Integer.parseInt(noteAmountFifty.getText());
        Integer twentyNoteAmount = Integer.parseInt(noteAmountTwenty.getText());
        Integer tenNoteAmount = Integer.parseInt(noteAmountTen.getText());
        Integer fiveNoteAmount = Integer.parseInt(noteAmountFive.getText());
        Integer twoDollarAmount = Integer.parseInt(dollarAmountTwo.getText());
        Integer oneDollarAmount = Integer.parseInt(dollarAmountOne.getText());
        Integer fiftyCentAmount = Integer.parseInt(centAmountFifty.getText());
        Integer twentyCentAmount = Integer.parseInt(centAmountTwenty.getText());
        Integer tenCentAmount = Integer.parseInt(centAmountTen.getText());
        Integer fiveCentAmount = Integer.parseInt(centAmountFive.getText());

        HashMap<Double, Integer> money = new HashMap<>();
        money.put(100.00, hundredNoteAmount);
        money.put(50.00, fiftyNoteAmount);
        money.put(20.00, twentyNoteAmount);
        money.put(10.00, tenNoteAmount);
        money.put(5.00, fiveNoteAmount);
        money.put(2.00, twoDollarAmount);
        money.put(1.00, oneDollarAmount);
        money.put(0.50, fiftyCentAmount);
        money.put(0.20, twentyCentAmount);
        money.put(0.10, tenCentAmount);
        money.put(0.05, fiveCentAmount);

        String result = this.atm.maintenance(money, maintenancePin);
        System.out.println(result);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Welcome.fxml"));
        root = loader.load();

        WelcomeController welcomeController = loader.getController();
        welcomeController.setATM(this.atm);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("ATM XYZ Banking");
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
        stage.setTitle("ATM XYZ Banking");
        scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }

    public void setATM(ATM atm) {
        this.atm = atm;
    }
}
