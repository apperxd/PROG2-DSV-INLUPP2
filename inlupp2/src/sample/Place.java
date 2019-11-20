package sample;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

abstract class Place extends Polygon {
    private boolean selected = false;
    private String category;
    private  Position position;
    private String namn;


    public String toString(){
        return namn + " "+position;
    }


    Place(String category, Position position, String namn){
        super(position.getX(), position.getY(), position.getX()-15, position.getY()-30, position.getX()+15, position.getY()-30);
        this.category = category;
        this.position = position;
        this.namn = namn;


    }

    public boolean getSelected(){
        return selected;
    }

    public String getCategory(){
        return category;
    }

    public String getNamn(){
        return namn;
    }

    public Position getPos(){ return position;}




    public boolean selected(){
        selected = !selected;
        if (selected) {
            setStroke(Color.YELLOWGREEN);
            setStrokeWidth(3);
            return true;
        } else {
            setStroke(null);
            return false;

        }

    }


    public void infoBox(){
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setHeaderText(toString());
        info.showAndWait();

    }

}


class NamedPlace extends Place{
    public NamedPlace (String category, Position position, String name){
        super(category,position,name);
    }

}
class DescribedPlace extends Place {
    private String description;

    public DescribedPlace(String category, Position position, String name, String description) {
        super(category, position, name);
        this.description = description;
    }
    public String getDescription(){
        return description;
}

    public void infoBox(){
        Alert info = new Alert(Alert.AlertType.INFORMATION, "" + description);
        info.setHeaderText(super.toString());
        info.showAndWait();

    }

    public String toString(){
        return super.toString() + " Desc" + description;
    }
}





