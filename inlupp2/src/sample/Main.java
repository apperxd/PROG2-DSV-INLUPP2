package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jdk.jfr.Category;
import org.w3c.dom.events.Event;


import java.io.*;
import java.util.*;
import java.util.concurrent.Flow;


public class Main extends Application {
    private Pane balken = new Pane();
    private MenuBar menuBar = new MenuBar();
    private ObservableList<String> category = FXCollections.observableArrayList("Bus", "Underground", "Train");
    private ListView<String> categories = new ListView<>(category);


    private Button neww;
    private Stage primaryStage;
    private Pane bilden;
    private ImageView imageView;
    private ClickHandler clickHandler = new ClickHandler();
    private RadioButton radio1;
    private RadioButton radio2;
    private Place place;
    private String valdcategori;
    private ToggleGroup tgl;
    private Button hideBtn;
    private TextField textArea;
    private Button searchBtn;
    private BorderPane root;
    private String filpath;
    private Boolean newData = false;

    private Map<Position, Place> places = new HashMap<>();
    private HashMap<String, Collection<Place>> placesNameSearch = new HashMap<>();
    //eventuell en hasmap med 4 arraylistprivate Map<String, >

    private ArrayList<Place> selectedPlaces = new ArrayList<>();
    private ArrayList<Place> busList = new ArrayList<>();
    private ArrayList<Place> undergroundList = new ArrayList<>();
    private ArrayList<Place> trainList = new ArrayList<>();
    private ArrayList<Place> unkownList = new ArrayList<>();


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        root = new BorderPane();


        //VBOX TOP
        VBox vbox = new VBox();
        vbox.getChildren().add(menuBar);
        Menu archiveMenu = new Menu("File");
        menuBar.getMenus().add(archiveMenu);
        MenuItem openItem = new MenuItem("Load Map");
        archiveMenu.getItems().add(openItem);
        openItem.setOnAction(new OpenHandler());

        MenuItem loadPlaces = new MenuItem("Load Places");
        archiveMenu.getItems().add(loadPlaces);
        loadPlaces.setOnAction(new LoadHandler());

        MenuItem saveItem = new MenuItem("Save");
        archiveMenu.getItems().add(saveItem);
        saveItem.setOnAction(new SaverHandler());

        MenuItem exitItem = new MenuItem("Exit");
        archiveMenu.getItems().add(exitItem);
        exitItem.setOnAction(new ExitHandler());
        primaryStage.setOnCloseRequest(new ExitWindowHandler());

        //Center top bar

        ////////////////////////////////////////////////////////////7
        FlowPane pane = new FlowPane();
        pane.setAlignment(Pos.CENTER);

        neww = new Button("New");
        neww.setOnAction(new NewHandler());
        pane.getChildren().add(neww);

        VBox vboxx = new VBox();
        pane.getChildren().add(vboxx);

        radio1 = new RadioButton("Named");
        vboxx.getChildren().add(radio1);

        radio2 = new RadioButton("Described");
        vboxx.getChildren().add(radio2);

        tgl = new ToggleGroup();
        radio1.setToggleGroup(tgl);
        radio2.setToggleGroup(tgl);
        radio1.setSelected(true);

//SÖK********************************************
        textArea = new TextField("Search");
        pane.getChildren().add(textArea);

        searchBtn = new Button("Search");
        pane.getChildren().add(searchBtn);
        searchBtn.setOnAction(new SearchHandler());

//SÖK********************************************
        hideBtn = new Button("Hide");
        hideBtn.setOnAction(new HideHandler());
        pane.getChildren().add(hideBtn);


        Button removeBtn = new Button("Remove");
        pane.getChildren().add(removeBtn);
        removeBtn.setOnAction(new RemoveHandler());

        Button coordinateBtn = new Button("Coordinates");
        pane.getChildren().add(coordinateBtn);
        coordinateBtn.setOnAction(new CordinateHandler());
        pane.setPadding(new Insets(10));
        pane.setHgap(8);
        pane.setVgap(4);
        vbox.getChildren().add(pane);

        root.setTop(vbox);


        //LIstVIew
        VBox cate = new VBox();
        categories.setPrefSize(170, 80);
        Button hideBtn = new Button("Hide Category");

        hideBtn.setOnAction(new HideCategory());
        cate.getChildren().addAll(new Label("Categories"), categories, hideBtn);
        cate.setAlignment(Pos.CENTER);
        //cate.setPadding(new Insets(0, 80, 0, 0));
        cate.setSpacing(3);
        root.setRight(cate);
        categories.getSelectionModel().selectedItemProperty().addListener(new ListHandler());


        //setprefSIZe

        //bilden/////////////////////////////

        bilden = new Pane();
        imageView = new ImageView();
        root.setCenter(bilden);
        ////////////////////////////////////////

        primaryStage.setTitle("Karta");
        primaryStage.setScene(new Scene(root));
        primaryStage.sizeToScene();
        primaryStage.show();




    }


    class ListHandler implements ChangeListener<String> {
        @Override
        public void changed(ObservableValue obs, String old, String nev) {
            if (nev != null) {
                switch (nev) {
                    case "Bus":
                        for (Place p : busList) {
                            p.setVisible(true);
                        }
                        break;
                    case "Underground":
                        for (Place p : undergroundList) {
                            p.setVisible(true);
                        }
                        break;
                    case "Train":
                        for (Place p : trainList) {
                            p.setVisible(true);
                        }
                        break;

                }
            }
            else return;
        }
    }


    class OpenHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            if (isNewData()) return;
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select picture to open");
            File file = fileChooser.showOpenDialog(primaryStage);


            if (file != null) {
                clearAllData();

                filpath = file.getAbsolutePath();
                Image image = new Image("file:" + filpath);
                imageView.setImage(image);
                bilden.getChildren().add(imageView);
                primaryStage.sizeToScene();
            }


        }


    }

    private boolean isNewData() {
        if (newData == true) {
            Alert varning = new Alert(Alert.AlertType.CONFIRMATION);
            varning.setHeaderText("Det finns osparad data, vill du ändå fortsätta?");
            Optional<ButtonType> svar = varning.showAndWait();
            if (svar.isPresent() && svar.get() == ButtonType.OK) {
                clearAllData();

            } else {
                return true;
            }
        }
        return false;
    }

    class CordinateHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            AlertCord alert = new AlertCord();
            Optional<ButtonType> result = alert.showAndWait();
            try {
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Position pos = new Position(alert.getXField(), alert.getYField());
                    if (places.containsKey(pos)) {
                        for (Place p : selectedPlaces) {
                            p.selected();

                        }
                        selectedPlaces.clear();

                        Place p = places.get(pos);
                        p.selected();
                        p.setVisible(true);
                        selectedPlaces.add(p);
                    } else {
                        errorBox();
                    }
                }
            } catch (NumberFormatException a) {
                errorBox();
            }

        }


    }


    private void selectedRemoveOrDelete(Place p) {
        if (p.selected() == true) {
            selectedPlaces.add(p);
        } else if (p.selected() == false) {
            selectedPlaces.remove(p);
        }
    }

    public void addPlace(Place p) {
        selectedPlaces.add(p);
    }

    class SearchHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            String placename = textArea.getText();
            for (Place p : selectedPlaces) {
                p.selected();
            }
            selectedPlaces.clear();
            ///avmarkera allt i arraylisten de markerade platserna



            if (placesNameSearch.get(placename) == null) {
                errorBox();

            } else
                for (Place pl : placesNameSearch.get(placename)) {

                       pl.setVisible(true);


                    pl.selected();
                    selectedPlaces.add(pl);
                }

        }


    }

    class NewHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            bilden.addEventHandler(MouseEvent.MOUSE_CLICKED, clickHandler);
            bilden.setCursor(Cursor.CROSSHAIR);

        }
    }

    class ExitHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            if(isNewData()) return;
            clearAllData();
            primaryStage.close();

        }
    }
    class ExitWindowHandler implements EventHandler<WindowEvent> {
        @Override
        public void handle(WindowEvent event) {
            if(isNewData()) {
                event.consume();
                return;
            }
            primaryStage.close();
            clearAllData();

        }
    }

    class RemoveHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            for (Place p : selectedPlaces) {
                if (p.equals(places.get(p.getPos()))) {
                    placesNameSearch.remove(p.getNamn());


                    places.remove(p.getPos());
                    p.setVisible(false);
                    if(p.getCategory() != null){
                        removeFromCategoryList(p);
                    }


                }
            }


            selectedPlaces.clear();
            newData = true;
        }
    }

    private void removeFromCategoryList(Place p) {
        switch (p.getCategory()) {
            case "Bus":
                busList.remove(p);
                break;
            case "Underground":
                undergroundList.remove(p);
                break;
            case "Train":
                trainList.remove(p);
                break;


        }

    }

    class HideCategory implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String cat = categories.getSelectionModel().getSelectedItem();
            if (cat == "Bus") {
                for (Place p : busList) {
                    places.get(p.getPos()).setVisible(false);
                }

            } else if (cat == "Underground") {
                for (Place p : undergroundList) {
                    places.get(p.getPos()).setVisible(false);
                }
            } else if (cat == "Train") {
                for (Place p : trainList) {
                    places.get(p.getPos()).setVisible(false);

                }

            }
        }
    }


    class HideHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {

            for (Place p : selectedPlaces) {
                p.selected();
                p.setVisible(false);

                //selectedPlaces.remove(p);
            }
            selectedPlaces.clear();
        }
    }


    private void fillerForLoad(String s, Place p) {
        if (s != null) {
            switch (s) {
                case "Bus":
                    p.setFill(Color.RED);
                    busList.add(p);
                    break;
                case "Train":
                    p.setFill(Color.GREEN);
                    trainList.add(p);
                    break;
                case "Underground":
                    p.setFill(Color.BLUE);
                    undergroundList.add(p);
                    break;


                default:
                    p.setFill(Color.BLACK);
            }
        } else
            p.setFill(Color.BLACK);
    }
    private void filler(String s, Place p) {
        if (categories.getSelectionModel().getSelectedItem() != null) {
            switch (categories.getSelectionModel().getSelectedItem()) {
                case "Bus":
                    p.setFill(Color.RED);
                    busList.add(p);
                    break;

                case "Underground":
                    p.setFill(Color.BLUE);
                    undergroundList.add(p);
                    break;

                case "Train":
                    p.setFill(Color.GREEN);
                    trainList.add(p);
                    break;
                default:
                    p.setFill(Color.BLACK);
            }
        } else
            p.setFill(Color.BLACK);
    }


    class ClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            valdcategori = categories.getSelectionModel().getSelectedItem();
            double x = event.getX();
            double y = event.getY();
            Position position = new Position(x, y);
            Place place;
            //if-sats kolla vilken radio button som e vald å skapa named eller descriped
            if (radio1.isSelected()) {
                MyAlert namnet = new MyAlert();
                Optional<ButtonType> svar = namnet.showAndWait();
                if(namnet.getName().trim().isEmpty() == true){
                    errorBox();
                    bilden.removeEventHandler(MouseEvent.MOUSE_CLICKED, clickHandler);
                    bilden.setCursor(Cursor.DEFAULT);
                    return;
                }
                if (svar.isPresent() && svar.get() == ButtonType.OK) {
                    place = new NamedPlace(valdcategori, position, namnet.getName());
                    if(places.containsKey(place.getPos()) == true){
                        errorBox();
                        return;
                    }

                    if (placesNameSearch.containsKey(namnet.getName())) {
                        placesNameSearch.get(namnet.getName()).add(place);



                    }
                    else
                    placesNameSearch.put(namnet.getName(), new HashSet<>());
                    placesNameSearch.get(namnet.getName()).add(place);



                } else return;
                filler(namnet.getName(), place);
                bilden.getChildren().add(place);
                places.put(position, place);
            } else {
                AlertDesc namnet = new AlertDesc();
                Optional<ButtonType> svar = namnet.showAndWait();
                if(namnet.getName().trim().isEmpty() == true || namnet.getDesc().trim().isEmpty()){
                    errorBox();
                    bilden.removeEventHandler(MouseEvent.MOUSE_CLICKED, clickHandler);
                    bilden.setCursor(Cursor.DEFAULT);
                    return;
                }

                if (svar.isPresent() && svar.get() == ButtonType.OK) {
                    place = new DescribedPlace(valdcategori, position, namnet.getName(), namnet.getDesc());
                    filler(namnet.getName(), place);
                    bilden.getChildren().add(place);
                    places.put(position, place);

                    //Set<Place> setIckeSammaNamn = new HashSet<>();
                    if (placesNameSearch.containsKey(namnet.getName())) {
                        placesNameSearch.get(namnet.getName()).add(place);



                    }
                    else
                        placesNameSearch.put(namnet.getName(), new HashSet<>());
                    placesNameSearch.get(namnet.getName()).add(place);



                } else return;
            }

            bilden.removeEventHandler(MouseEvent.MOUSE_CLICKED, clickHandler);
            bilden.setCursor(Cursor.DEFAULT);
            newData = true;
            place.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    place.selected();
                    if (place.getSelected()) {
                        selectedPlaces.add(place);
                    } else
                        selectedPlaces.remove(place);
                } else if (e.getButton() == MouseButton.SECONDARY) {
                    place.infoBox();

                }
            });

        }
    }

    private void errorBox() {
        Alert errorBox = new Alert(Alert.AlertType.ERROR);
        errorBox.showAndWait();
    }

    class SaverHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event){
            newData = false;
            try{

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Where to write file");


                FileWriter utfil = new FileWriter("jarvafaltet.places");
                PrintWriter out = new PrintWriter(utfil);
                for(Place p : places.values()) {
                    if (p instanceof NamedPlace){
                        out.print("Named,");
                   }
                    else {
                        out.print("Described,");
                    }
                    if(p.getCategory() == null){
                        out.print("None,");
                    }
                    if (busList.contains(p)){
                        out.print("Bus,");
                    }
                    else if(undergroundList.contains(p)){
                        out.print("Underground,");
                    }
                    else if(trainList.contains(p)){
                        out.print("Train,");
                    }
                    out.print(p.getPos().getX()+","+p.getPos().getY() + "," + p.getNamn());
                    if(p instanceof DescribedPlace){
                        out.print("," + ((DescribedPlace) p).getDescription());
                    }
                    out.println("");

                }

                out.close();
            }catch(IOException e){
                new Alert(Alert.AlertType.ERROR, "Fel!").showAndWait();
            }
        }
    }

    class LoadHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event){

            if (isNewData()) return;


            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select picture to open");
                File file = fileChooser.showOpenDialog(primaryStage);

                if (file != null) {
                    String filpath = file.getAbsolutePath();
                    bilden.getChildren().clear();
                    bilden.getChildren().add(imageView);
                    clearAllData();

                    FileReader in = new FileReader(filpath);
                    BufferedReader br = new BufferedReader(in);
                    String line;
                    for(Place p : places.values()){
                        bilden.getChildren().remove(p);
                    }
                    while ((line = br.readLine()) != null) {

                        String[] tokens = line.split(",");
                        String typAvClass = tokens[0];
                        String category = tokens[1];
                        double x = Double.parseDouble(tokens[2]);
                        double y = Double.parseDouble(tokens[3]);
                        // ändrade till liten double ist Double


                        if (typAvClass.equals("Named")) {

                            if (tokens.length > 4) {
                                String namn = tokens[4];

                                Position nypos = new Position(x, y);
                                Place ny = new NamedPlace(category, nypos, namn);

                                if (placesNameSearch.containsKey(namn)) {
                                    placesNameSearch.get(namn).add(ny);


                                } else {
                                    placesNameSearch.put(namn, new HashSet<>());
                                    placesNameSearch.get(namn).add(ny);
                                }
                                ny.setVisible(true);
                                fillerForLoad(category, ny);
                                bilden.getChildren().add(ny);
                                places.put(nypos, ny);
                                setOnActionNamed(ny);


                            }


                        } else if (tokens.length == 5) {
                            String namn = tokens[4];

                            loadClickDataPut(category, x, y, namn, null);


                        } else {
                            if (tokens.length == 6) {
                                String namn = tokens[4];
                                String desc = tokens[5];

                                loadClickDataPut(category, x, y, namn, desc);


                            }
                        }
                    }


                    br.close();
                    in.close();
                }}catch(FileNotFoundException e){
                    new Alert(Alert.AlertType.ERROR, "Fel!").showAndWait();
                }catch(IOException e){
                    new Alert(Alert.AlertType.ERROR, "Fel!").showAndWait();
                }
}
    }

    private void loadClickDataPut(String category, double x, double y, String namn, String desc) {
        Position nypos = new Position(x, y);
        Place ny = new DescribedPlace(category, nypos, namn, desc);


        setOnAction(ny);
        if (placesNameSearch.containsKey(ny.getNamn())) {
            placesNameSearch.get(ny.getNamn()).add(ny);


        } else {
            placesNameSearch.put(ny.getNamn(), new HashSet<>());
            placesNameSearch.get(ny.getNamn()).add(ny);
        }
        fillerForLoad(category, ny);
        bilden.getChildren().add(ny);
        ny.setVisible(true);
        places.put(nypos, ny);

    }

    private void setOnActionNamed(Place ny) {
        ny.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                ny.selected();
                if (ny.getSelected()) {
                    selectedPlaces.add(ny);
                } else
                    selectedPlaces.remove(ny);
            } else if (e.getButton() == MouseButton.SECONDARY) {
                ny.infoBox();

            }
        });
    }
    private void clearAllData(){
        selectedPlaces.clear();
        placesNameSearch.clear();
        places.clear();
        busList.clear();
        trainList.clear();
        undergroundList.clear();
    }

    private void setOnAction(Place ny) {
        ny.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                ny.selected();
                if (ny.getSelected()) {
                    selectedPlaces.add(ny);
                } else
                    selectedPlaces.remove(ny);
            } else if (e.getButton() == MouseButton.SECONDARY) {
                ny.infoBox();

            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}


