package ru.unn.agile.color.viewmodel;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import ru.unn.agile.color.model.ColorSpaces;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.unn.agile.color.model.Converter;

import java.util.ArrayList;
import java.util.List;

import static ru.unn.agile.color.model.CheckParameters.*;


public class ViewModel {

    private final StringProperty firstValue = new SimpleStringProperty();
    private final StringProperty secondValue = new SimpleStringProperty();
    private final StringProperty thirdValue = new SimpleStringProperty();
    private final StringProperty firstValueResult = new SimpleStringProperty();
    private final StringProperty secondValueResult = new SimpleStringProperty();
    private final StringProperty thirdValueResult = new SimpleStringProperty();
    public final BooleanProperty convertingDisabled = new SimpleBooleanProperty();
    private final ObjectProperty<ColorSpaces> fromColorSpace = new SimpleObjectProperty<ColorSpaces>();
    private final ObjectProperty<ColorSpaces> toColorSpace = new SimpleObjectProperty<ColorSpaces>();
    private final ObjectProperty<ObservableList<ColorSpaces>> colorSpaces =
            new SimpleObjectProperty<>(FXCollections.observableArrayList(ColorSpaces.values()));
    private final List<ValueChangeListener> valueChangedListeners = new ArrayList<>();

    public ViewModel() {
        firstValue.set("");
        secondValue.set("");
        thirdValue.set("");
        firstValueResult.set("");
        secondValueResult.set("");
        thirdValueResult.set("");
        fromColorSpace.set(ColorSpaces.LAB);
        toColorSpace.set(ColorSpaces.HSV);
        BooleanBinding couldConvert = new BooleanBinding() {
            {
                super.bind(firstValue, secondValue, thirdValue);
            }
            @Override
            protected boolean computeValue() {
                final Status[] status = new Status[1];
                fromColorSpace.addListener(new ChangeListener<ColorSpaces>() {
                    @Override
                    public void changed(ObservableValue<? extends ColorSpaces> observable,
                                        ColorSpaces oldValue, ColorSpaces newValue) {
                        status[0] = getInputStatus();
                    }
                });
                return getInputStatus() == Status.READY || status[0] == Status.READY;
            }
        };
        convertingDisabled.bind(couldConvert.not());


    }

    private Status getInputStatus() {
        Status inputStatus = Status.READY;
        if (firstValue.get().isEmpty() || secondValue.get().isEmpty()
                || thirdValue.get().isEmpty()) {
            inputStatus = Status.WAITING;
        }
        try {
            if (!firstValue.get().isEmpty() && !secondValue.get().isEmpty()
                    && !thirdValue.get().isEmpty()) {
                double[] params = {Double.parseDouble(firstValue.get()),
                    Double.parseDouble(secondValue.get()),
                    Double.parseDouble(thirdValue.get())};
                checkParameters(fromColorSpace.getValue(), params);
            }
        } catch (IllegalArgumentException ex) {
            inputStatus = Status.BAD_FORMAT;
        }
        return inputStatus;
    }

    public ObjectProperty<ObservableList<ColorSpaces>> colorSpacesProperty() {
        return colorSpaces;
    }

    public final ObservableList<ColorSpaces> getColorSpaces() {
        return colorSpaces.get();
    }

    public ObjectProperty<ColorSpaces> getFromColorSpace() {
        return fromColorSpace;
    }

    public ObjectProperty<ColorSpaces> getToColorSpace() {
        return toColorSpace;
    }

    public Property<String> getFirstValueProperty() {
        return firstValue;
    }

    public Property<String> getSecondValueProperty() {
        return secondValue;
    }

    public Property<String> getThirdValueProperty() {
        return thirdValue;
    }

    public boolean isConvertingDisabled() {
        return convertingDisabled.get();
    }

    public BooleanProperty convertingDisabledProperty() {
        return convertingDisabled;
    }


    public String getFirstValueResult() {
        return firstValueResult.get();
    }

    public StringProperty firstValueResultProperty() {
        return firstValueResult;
    }


    public String getSecondValueResult() {
        return secondValueResult.get();
    }

    public StringProperty secondValueResultProperty() {
        return secondValueResult;
    }

    public String getThirdValueResult() {
        return thirdValueResult.get();
    }

    public StringProperty thirdValueResultProperty() {
        return thirdValueResult;
    }

    public void convert() {
        if (convertingDisabled.get()) {
            return;
        }
        double[] params = {Double.parseDouble(firstValue.get()),
                Double.parseDouble(secondValue.get()),
                Double.parseDouble(thirdValue.get())};
        double[] roots =
                Converter.convert(fromColorSpace.get(), toColorSpace.get(), params);

        firstValueResult.set(String.valueOf(roots[0]));
        secondValueResult.set(String.valueOf(roots[1]));
        thirdValueResult.set(String.valueOf(roots[2]));
        System.out.println(roots[0] + " | " + roots[1] + " | " + roots[2]);
        //resultProperty.set(buildResultString(roots));
        //statusProperty.set(Status.SUCCESS.toString());
    }

    private class ValueChangeListener implements ChangeListener<String> {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            getInputStatus().toString();
        }
    }

    enum Status {
        WAITING("Please enter coefficients of Quadratic Equation"),
        READY("Press 'Solve' for solving Quadratic Equation"),
        BAD_FORMAT("Entered coefficients are incorrect!"),
        SUCCESS("Success");

        private final String name;

        Status(final String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }
}
