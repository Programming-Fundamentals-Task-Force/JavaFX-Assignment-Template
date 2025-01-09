package org.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import static org.testfx.assertions.api.Assertions.*;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.geometry.Point2D;
import javafx.geometry.BoundingBox;

import java.util.ArrayList;
import java.util.stream.Stream;

import com.lab.*;

@ExtendWith(ApplicationExtension.class)
class AppTest extends App {
    private Stage primaryStage;

    @Start
    public void start(Stage stage) {
        super.start(stage);
        primaryStage = stage;
    }

    @Test public void
    app_window_has_correct_size(FxRobot robot) {
        Scene scene = primaryStage.getScene();
        assertEquals(500, scene.getWidth());
        assertEquals(200, scene.getHeight());
    }

    @Test public void
    app_has_two_textfields(FxRobot robot) {
        assertThat(robot.lookup(".text-field").queryAll()).hasSize(2);
    }

    @Test public void
    app_has_add_button(FxRobot robot) {
        assertThat(robot.lookup(".button").queryButton()).hasText("Add");
    }

    @Test public void
    app_has_correct_text_labels(FxRobot robot) {
        var labels = robot.lookup(".label").queryAllAs(Label.class);
        assertThat(labels).extracting(Label::getText).anySatisfy(text -> assertThat(text).contains("+"));
        assertThat(labels).extracting(Label::getText).anySatisfy(text -> assertThat(text).contains("="));
        assertThat(labels).extracting(Label::getText).anySatisfy(text -> assertThat(text).contains("0"));
    }

    @Test public void
    all_controls_are_roughly_centered(FxRobot robot) {
        Scene scene = primaryStage.getScene();
        BoundingBox validRegion = new BoundingBox(scene.getWidth() * 0.1, scene.getHeight() * 0.1,
                                                  scene.getWidth() * 0.8, scene.getHeight() * 0.8);
        ArrayList<Control> controls = new ArrayList<>();

        controls.addAll(robot.lookup(".label").queryAll());
        controls.addAll(robot.lookup(".text-field").queryAll());
        controls.add(robot.lookup(".button").queryButton());

        assertThat(controls).allMatch(control -> {
            Point2D controlCenter = control.localToScene(control.getWidth() / 2, control.getHeight() / 2);
            return validRegion.contains(controlCenter);
        });
    }

    @Test public void
    add_two_numbers_from_textfields_when_clicked(FxRobot robot) {
        TextField[] textFields = robot.lookup(".text-field").queryAllAs(TextField.class).toArray(new TextField[0]);
        Stream<Label> labels = robot.lookup(".label").queryAllAs(Label.class).stream();
        Label resultLabel = labels
            .filter(label -> label.getText().contains("0"))
            .findAny()
            .get();

        // Test case 1
        textFields[0].setText("123");
        textFields[1].setText("456");
        robot.clickOn("Add");
        assertThat(resultLabel.getText()).contains("579");

        // Test case 2
        textFields[0].setText("0");
        textFields[1].setText("0");
        robot.clickOn("Add");
        assertThat(resultLabel.getText()).contains("0");
    }
}
