import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.ColorPicker;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class Painter extends Application {

    private static Pane pane=new Pane();
    private double width =350;
    private static double height = 300;
    private double psw=width;
    private double psh=height;
    private double x;

    private double widther=20;
    @Override
    public void start(Stage primaryStage) {

        ToggleButton TB = new ToggleButton("Line");
        TB.setSelected(true);
        TB.setLayoutX(10);
        TB.setLayoutY(10);
        ToggleButton TB1 = new ToggleButton("Draw");
        TB1.setLayoutX(50);
        TB1.setLayoutY(10);
        ToggleButton TB2 = new ToggleButton("Circle");
        TB2.setLayoutX(95);
        TB2.setLayoutY(10);
        ToggleButton TB3 = new ToggleButton("Eraser");
        TB3.setLayoutX(145);
        TB3.setLayoutY(10);

        TB.setOnAction(e->{
            TB1.setSelected(false);
            TB2.setSelected(false);
            TB3.setSelected(false);
        });
        TB1.setOnAction(e-> {
            TB.setSelected(false);
            TB2.setSelected(false);
            TB3.setSelected(false);
        });
        TB2.setOnAction(e-> {
            TB.setSelected(false);
            TB1.setSelected(false);
            TB3.setSelected(false);
        });
        TB3.setOnAction(e-> {
            TB.setSelected(false);
            TB1.setSelected(false);
            TB2.setSelected(false);
            widther=20;
        });
        ColorPicker cp=new ColorPicker();
        cp.setValue(Color.BLACK);
        cp.setLayoutX(psw-140);
        cp.setLayoutY(10);

        Button btn=new Button(" Save ");
        btn.setLayoutY(psh-40);
        btn.setLayoutX(75);

        Button clear=new Button(" Clear ");
        clear.setLayoutY(psh-40);
        clear.setLayoutX(25);

        Rectangle rect1 = new Rectangle();
        rect1.setLayoutX(30);
        rect1.setLayoutY(50);
        rect1.setWidth(psw-60);
        rect1.setHeight(psh-110);
        rect1.setFill(Color.WHITE);
        rect1.setStroke(Color.BLACK);
        pane.getChildren().add(rect1);

        Canvas rect = new Canvas(); 
        rect.setLayoutX(30);
        rect.setLayoutY(50);
        rect.setWidth(psw-60);
        rect.setHeight(psh-110);
        pane.getChildren().add(rect);
        GraphicsContext gc = rect.getGraphicsContext2D();

        pane.getChildren().addAll(btn,cp,TB,TB1,TB2,TB3,clear);
        Line line=new Line();
        Line line1= new Line();

        Circle circle1=new Circle();
        Rectangle eraser = new Rectangle();
        eraser.setStroke(Color.BLACK);
        eraser.setFill(Color.WHITE);

        circle1.setFill(Color.TRANSPARENT);
        circle1.setStroke(Paint.valueOf("#000000"));

        pane.getChildren().addAll(line1,circle1,eraser);
        rect.setOnMousePressed(e->{
            if(TB.isSelected()) {

                gc.setStroke(cp.getValue());
                line.setStartX(e.getX());
                line.setStartY(e.getY());

                line1.setStroke(cp.getValue());
                line1.setStartX(e.getX()+30);
                line1.setStartY(e.getY()+50);
                line1.setEndX(e.getX()+30);
                line1.setEndY(e.getY()+50);
            }
           else if(TB1.isSelected()) {
                gc.setStroke(cp.getValue());
                gc.setFont(Font.font(70));
                gc.beginPath();
                gc.lineTo(e.getX(),e.getY());
            }
           else if(TB2.isSelected()) {
                x=e.getX();

                circle1.setStroke(cp.getValue());
                circle1.setLayoutX(e.getX()+30);
                circle1.setLayoutY(e.getY()+50);
                circle1.setRadius(0.0);
            }
           else if(TB3.isSelected()) {
               gc.clearRect(e.getX()-widther/2.0,e.getY()-widther/2.0,widther,widther);
           }
        });
        rect.setOnMouseDragged(e->
        {
            if(TB.isSelected()) {
                if((e.getX()>=0&&e.getX()<=rect.getWidth())&&(e.getY()>=0&&e.getY()<=rect.getHeight())) { 
                    line1.setEndX(e.getX() + 30);
                    line1.setEndY(e.getY() + 50);
                }
            }
            else if(TB1.isSelected()) { 
                gc.lineTo(e.getX(),e.getY());
                gc.stroke();
            }
            else if(TB2.isSelected()) {
                circle1.setRadius(Math.abs(x-e.getX()));
            }
            else if(TB3.isSelected()) {
                gc.clearRect(e.getX()-widther/2.0,e.getY()-widther/2.0,widther,widther);
            }
        });
        rect.setOnMouseReleased(e->{
            if(TB.isSelected()) { 
                line.setEndX(e.getX());
                line.setEndY(e.getY());
                gc.setStroke(cp.getValue());
                gc.strokeLine(line.getStartX(),line.getStartY(),line.getEndX(),line.getEndY());
                line1.setStartX(-10);
                line1.setEndX(-10);
            }
            else if(TB1.isSelected()) {
                gc.lineTo(e.getX(),e.getY());
                gc.stroke();
                gc.closePath();
            }
            else if(TB2.isSelected()) {
                gc.setStroke(cp.getValue());

                gc.strokeOval((circle1.getLayoutX()-30)-circle1.getRadius(),+(circle1.getLayoutY()-50)-circle1.getRadius(), circle1.getRadius()*2,circle1.getRadius()*2);
                circle1.setLayoutX(-10);
                circle1.setLayoutY(-10);
                circle1.setRadius(0.0);
            }
            else if(TB3.isSelected()) {
                eraser.setWidth(widther);
                eraser.setHeight(widther);
                eraser.setVisible(false);
                gc.clearRect(e.getX()-widther/2.0,e.getY()-widther/2.0,widther,widther);
            }
        });

        btn.setOnMouseEntered(e->{
            btn.setStyle("-fx-button-color: Yellow");
            btn.setStyle("-fx-border-color: Purple");
        });
        btn.setOnMouseExited(e->{
            btn.setStyle("-fx-button-color: Transparent");
            btn.setStyle("-fx-border-color: Transparent");
        });

        btn.setOnAction(e->{
            FileChooser savefile=new FileChooser();
            savefile.setTitle("Save File");

            savefile.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PNG","*.png"),
                    new FileChooser.ExtensionFilter("Jpeg","*.jpg")

            );
            File file = savefile.showSaveDialog(primaryStage);

            try{

                Image snapshot=rect.snapshot(null,null);
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot,null),"png",file);
            }
            catch (Exception e1)
            {
                System.out.println(""+e1);
            }
        });

        clear.setOnAction(e->{
            gc.setFill(Color.WHITE);
            gc.fillRect(1,1,rect1.getWidth()-2,rect1.getHeight()-2);
        });
        Scene scene = new Scene(pane, psw, psh);
        AtomicBoolean che= new AtomicBoolean(false);
        scene.setOnMousePressed(e->{
            if((e.getX()<rect.getWidth()+30&&e.getX()>30)&&(e.getY()<rect.getHeight()+50&&e.getY()>50)) {
                if (TB3.isSelected()) {
                    che.set(true);
                    eraser.setVisible(true);
                    eraser.setWidth(widther);
                    eraser.setHeight(widther);
                    eraser.setLayoutX(e.getX() - widther / 2);
                    eraser.setLayoutY(e.getY() - widther / 2);
                }
            }
        });
        scene.setOnMouseDragged(e->{

            if(che.get())
            {
                if (TB3.isSelected()) {
                    eraser.setVisible(true);
                    eraser.setWidth(widther);
                    eraser.setHeight(widther);
                    eraser.setLayoutX(e.getX() - widther / 2);
                    eraser.setLayoutY(e.getY() - widther / 2);
                }

            }
        });
        scene.setOnMouseReleased(e->{
            eraser.setVisible(false);
            che.set(false);
        });
        primaryStage.setTitle("Drawing By Umair");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinWidth(350);
        primaryStage.setMinHeight(310);
        AnimationTimer time= new AnimationTimer() {
            @Override
            public void handle(long l) {
                psh = primaryStage.getHeight()-38;
                psw= primaryStage.getWidth()-16;
                rect.setWidth(psw-60);
                rect.setHeight(psh-110);
                rect1.setWidth(psw-60);
                rect1.setHeight(psh-110);
                btn.setLayoutY(psh-40);
                cp.setLayoutX(psw-140);
                clear.setLayoutY(psh-40);

            }
        };

        time.start();
        scene.setOnKeyPressed(e -> {
            if (new KeyCodeCombination(KeyCode.ADD, KeyCombination.CONTROL_DOWN).match(e)) {
                if(TB3.isSelected())
                {
                    if(widther<80) {
                        widther += 5;
                    }
                        eraser.setVisible(true);
                        eraser.setWidth(widther);
                        eraser.setHeight(widther);
                        eraser.setLayoutX(psw / 2.0 - widther / 2.0);
                        eraser.setLayoutY(psh / 2.0 - widther / 2.0);
                }
            }
            if (new KeyCodeCombination(KeyCode.SUBTRACT, KeyCombination.CONTROL_DOWN).match(e)) {
                if(TB3.isSelected())
                {
                    if(widther>5) {
                        widther -= 5;
                    }
                        eraser.setVisible(true);
                        eraser.setWidth(widther);
                        eraser.setHeight(widther);
                        eraser.setLayoutX(psw/2.0 - widther/2.0);
                        eraser.setLayoutY(psh/2.0 - widther/2.0);
                }
            }
        });
        scene.setOnKeyReleased(e-> eraser.setVisible(false));
    }
}