package sample;

import javafx.geometry.Pos;

public class Position {
    double x;
    double y;

    Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "  [" + x + "," + y + "]";
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


    public int hashCode() {
        return (int) ((x * y) * (x * 100 - y / 3));
    }

    public boolean equals(Object o) {
        if (o instanceof Position) {
            Position p = (Position) o;
            return x == p.getX() && y == p.getY();
        }
        else return false;
    }
}

