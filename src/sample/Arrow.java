package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;


import java.awt.*;

public class Arrow extends Group {

    private final QuadCurve line;

    public Arrow() {
        this(new QuadCurve(), new Line(), new Line());
    }

    private static final double arrowLength = 20;
    private static final double arrowWidth = 10;

    private Arrow(QuadCurve line, Line arrow1, Line arrow2) {
        super(line, arrow1, arrow2);
        this.line = line;


        InvalidationListener updater = o -> {
            Angle angle = new Angle();
            Point point1 = new Point((int) getEndX(), (int) getEndY());
            Point point2 = new Point((int) getStartX(), (int) getStartY());
            double degree = angle.getAngle(point1, point2);

            line.setFill(null);
            line.setStroke(Color.BLACK);

            line.setStrokeWidth(3);
            line.controlXProperty().bind((line.startXProperty().add(line.endXProperty()).divide(2)).add(Math.sin(degree)*50));
            line.controlYProperty().bind((line.startYProperty().add(line.endYProperty()).divide(2)).add(Math.cos(degree)*50));

            double ex = getEndX();
            double ey = getEndY();
            double sx = getStartX();
            double sy = getStartY();

            arrow1.setEndX(ex);
            arrow1.setEndY(ey);
            arrow2.setEndX(ex);
            arrow2.setEndY(ey);

            if (ex == sx && ey == sy) {
                // arrow parts of length 0
                arrow1.setStartX(ex);
                arrow1.setStartY(ey);
                arrow2.setStartX(ex);
                arrow2.setStartY(ey);
            } else {
                double factor = arrowLength / Math.hypot(sx-ex, sy-ey);
                double factorO = arrowWidth / Math.hypot(sx-ex, sy-ey);

                // part in direction of main line
                double dx = (sx - ex) * factor;
                double dy = (sy - ey) * factor;

                // part ortogonal to main line
                double ox = (sx - ex) * factorO;
                double oy = (sy - ey) * factorO;



                arrow1.setStartX(ex + dx - oy);
                arrow1.setStartY(ey + dy + ox);
                arrow2.setStartX(ex + dx + oy);
                arrow2.setStartY(ey + dy - ox);


            }
        };
        arrow1.setStrokeWidth(3);line.setStrokeWidth(3);
        arrow2.setStrokeWidth(3);// add updater to properties
        arrow1.setStroke(Color.RED);
        arrow2.setStroke(Color.RED);

        startXProperty().addListener(updater);
        startYProperty().addListener(updater);
        endXProperty().addListener(updater);
        endYProperty().addListener(updater);
        updater.invalidated(null);
    }

    // start/end properties

    public void setColor() { line.setStroke(Color.WHITE);}

    public final void setStartX(double value) {
        line.setStartX(value);
    }

    public final double getStartX() {
        return line.getStartX();
    }

    public final DoubleProperty startXProperty() {
        return line.startXProperty();
    }

    public final void setStartY(double value) {
        line.setStartY(value);
    }

    public final double getStartY() {
        return line.getStartY();
    }

    public final DoubleProperty startYProperty() {
        return line.startYProperty();
    }

    public final void setEndX(double value) {
        line.setEndX(value);
    }

    public final double getEndX() {
        return line.getEndX();
    }

    public final DoubleProperty endXProperty() {
        return line.endXProperty();
    }

    public final void setEndY(double value) {
        line.setEndY(value);
    }

    public final double getEndY() {
        return line.getEndY();
    }

    public final DoubleProperty endYProperty() {
        return line.endYProperty();
    }

}
