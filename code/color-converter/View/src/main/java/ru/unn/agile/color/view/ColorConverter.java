package ru.unn.agile.color.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import ru.unn.agile.color.model.ColorSpaces;
import ru.unn.agile.color.viewmodel.ViewModel;


public class ColorConverter {
   @FXML
   private TextField firstValue;
   @FXML
   private TextField secondValue;
   @FXML
   private TextField thirdValue;
   @FXML
   private ComboBox<ColorSpaces> toSomeColor;
   @FXML
   private ComboBox<ColorSpaces> fromSomeColor;
   @FXML
   Button convertButton;
   @FXML
   private ViewModel viewModel;

   public ColorConverter() {
   }

   @FXML
   void initialize() {
      firstValue.textProperty().bindBidirectional(viewModel.getFirstValueProperty());
      secondValue.textProperty().bindBidirectional(viewModel.getSecondValueProperty());
      thirdValue.textProperty().bindBidirectional(viewModel.getThirdValueProperty());
      fromSomeColor.valueProperty().bindBidirectional(viewModel.getFromColorSpace());
      toSomeColor.valueProperty().bindBidirectional(viewModel.getToColorSpace());
      convertButton.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {
            viewModel.convert();
         }
      });
   }
}
