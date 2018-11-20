package viewer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import mandelbrot.Complex;
import mandelbrot.Mandelbrot;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/**
 * Controls the color of the pixels of the canvas.
 */
public class Controller implements Initializable {

    /**
     * Dimension of the grid used to supersample each pixel.
     * The number of subpixels for each pixel is the square of <code>SUPERSAMPLING</code>
     */
    private static final int SUPERSAMPLING = 3;

    @FXML
    private Canvas canvas; /* The canvas to draw on */

    @FXML
    private StackPane stackPane;
    private List<Pixel> pixels;

    private Camera camera = Camera.camera0; /* The view to display */

    private Mandelbrot mandelbrot = new Mandelbrot(); /* the algorithm */


    /* positions of colors in the histogram */
    private double[] breakpoints = {0., 0.75, 0.85, 0.95, 0.99, 1.0};
    /* colors of the histogram */
    private Color[] colors =
            {Color.gray(0.2),
                    Color.gray(0.7),
                    Color.rgb(55, 118, 145),
                    Color.rgb(63, 74, 132),
                    Color.rgb(145, 121, 82),
                    Color.rgb(250, 250, 200)
            };
    /* algorithm to generate the distribution of colors */
    private Histogram histogram = new Histogram(breakpoints, colors);

    /**
     * Method called when the graphical interface is loaded
     *
     * @param location  location
     * @param resources resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateUI();
    }

    private TextFormatter<Double> formatTextField() {

        Pattern validEditingState = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");

        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState.matcher(text).matches()) {
                return c ;
            } else {
                return null ;
            }
        };

        StringConverter<Double> converter = new StringConverter<>() {

            @Override
            public Double fromString(String s) {
                if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                    return 0.0 ;
                } else {
                    return Double.valueOf(s);
                }
            }


            @Override
            public String toString(Double d) {
                return d.toString();
            }
        };

        return new TextFormatter<>(converter, 0.0, filter);
    }

    private void generateUI() {

        VBox stackPaneVBox = new VBox();
        stackPaneVBox.setPadding(new Insets(5,5,5,5));
        stackPaneVBox.setSpacing(10);

        // Code for Area Selection GUI
        HBox areaSelection = new HBox();
        areaSelection.setPadding(new Insets(5,5,5,5));
        areaSelection.setSpacing(10);

        CheckBox areaSelectionCheckBox = new CheckBox("Area Selection");

        areaSelection.setPadding(new Insets(5,5,5,5));
        areaSelection.setSpacing(10);

        HBox pixelCenter = new HBox();
        Label pixelImageCenter = new Label("Image Center : ");
        Label pixelImageCenterX = new Label("Center X : ");
        TextField pixelImageCenterXValue = new TextField();
        pixelImageCenterXValue.setTextFormatter(formatTextField());

        Label pixelImageCenterY = new Label(" Center Y : ");
        TextField pixelImageCenterYValue = new TextField();
        pixelImageCenterYValue.setTextFormatter(formatTextField());
        pixelCenter.getChildren().addAll(pixelImageCenter,pixelImageCenterX,pixelImageCenterXValue,pixelImageCenterY,pixelImageCenterYValue);

        HBox imageSize = new HBox();
        Label imageWidth = new Label("Image Size : ");
        Label imageWidthX = new Label("Width : ");
        TextField imageWidthValueX = new TextField();
        imageWidthValueX.setTextFormatter(formatTextField());

        Label imageHeigth = new Label("Height : ");
        TextField imageHeightValueY = new TextField();
        imageHeightValueY.setTextFormatter(formatTextField());

        imageSize.getChildren().addAll(imageWidth,imageWidthX,imageWidthValueX,imageHeigth,imageHeightValueY);
        areaSelection.getChildren().addAll(pixelCenter,imageSize);
        areaSelection.setDisable(true);

        areaSelectionCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> areaSelection.setDisable(!areaSelection.isDisable()));

        // Code for Custom Colors GUI
        HBox customColor = new HBox();
        customColor.setPadding(new Insets(5,5,5,5));
        customColor.setSpacing(10);
        CheckBox customColorCheckBox = new CheckBox("Custom Color");
        VBox colorsVBox = new VBox();
        colorsVBox.setPadding(new Insets(5,5,5,5));
        colorsVBox.setSpacing(10);

        HBox hBox0 = new HBox();
        final ColorPicker colorPicker0 = new ColorPicker();
        colorPicker0.setValue(colors[0]);
        colorPicker0.setDisable(true);
        colorPicker0.setOnAction(event -> colors[0] = colorPicker0.getValue());
        hBox0.getChildren().addAll(colorPicker0);

        HBox hBox1 = new HBox();
        final ColorPicker colorPicker1 = new ColorPicker();
        colorPicker1.setValue(colors[1]);
        colorPicker1.setDisable(true);
        colorPicker0.setOnAction(event -> colors[1] = colorPicker1.getValue());
        hBox1.getChildren().addAll(colorPicker1);

        HBox hBox2 = new HBox();
        final ColorPicker colorPicker2 = new ColorPicker();
        colorPicker2.setValue(colors[2]);
        colorPicker2.setDisable(true);
        colorPicker2.setOnAction(event -> colors[2] = colorPicker2.getValue());
        hBox2.getChildren().addAll(colorPicker2);

        HBox hBox3 = new HBox();
        final ColorPicker colorPicker3 = new ColorPicker();
        colorPicker3.setValue(colors[3]);
        colorPicker3.setDisable(true);
        colorPicker3.setOnAction(event -> colors[3] = colorPicker3.getValue());
        hBox3.getChildren().addAll(colorPicker3);

        HBox hBox4 = new HBox();
        final ColorPicker colorPicker4 = new ColorPicker();
        colorPicker4.setValue(colors[4]);
        colorPicker4.setDisable(true);
        colorPicker4.setOnAction(event -> colors[4] = colorPicker4.getValue());
        hBox4.getChildren().addAll(colorPicker4);

        HBox hBox5 = new HBox();
        final ColorPicker colorPicker5 = new ColorPicker();
        colorPicker5.setValue(colors[5]);
        colorPicker5.setDisable(true);
        colorPicker5.setOnAction(event -> colors[5] = colorPicker5.getValue());
        hBox5.getChildren().addAll(colorPicker5);

        customColorCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            colorPicker0.setDisable(!colorPicker0.isDisable());
            colorPicker1.setDisable(!colorPicker1.isDisable());
            colorPicker2.setDisable(!colorPicker2.isDisable());
            colorPicker3.setDisable(!colorPicker3.isDisable());
            colorPicker4.setDisable(!colorPicker4.isDisable());
            colorPicker5.setDisable(!colorPicker5.isDisable());
        });

        HBox generate = new HBox();
        Button generateButton = new Button("Generate !");
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        generateButton.setOnAction(event -> {
            canvas.setVisible(true);
            progressIndicator.setVisible(true);
            if (areaSelectionCheckBox.isSelected()) {
                camera = new Camera(Double.parseDouble(pixelImageCenterXValue.getCharacters().toString()),
                        Double.parseDouble(pixelImageCenterYValue.getCharacters().toString()),
                        Double.parseDouble(imageWidthValueX.getCharacters().toString()),
                        Double.parseDouble(imageWidthValueX.getCharacters().toString()) / Double.parseDouble(imageHeightValueY.getCharacters().toString()));
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            render();

        });
        generate.getChildren().addAll(generateButton,progressIndicator);

        colorsVBox.getChildren().addAll(hBox0,hBox1,hBox2,hBox3,hBox4,hBox5);
        customColor.getChildren().addAll(customColorCheckBox,colorsVBox);
        stackPaneVBox.getChildren().addAll(areaSelectionCheckBox, areaSelection, customColor, generate);
        stackPane.getChildren().addAll(stackPaneVBox);
    }

    /**
     * compute and display the image.
     */
    private void render() {
        Thread generateMandelbrot = new Thread(() -> {
            pixels = getPixels();
            Platform.runLater(() -> renderPixels(pixels));
        });
        generateMandelbrot.start();
    }

    /**
     * display each pixel
     *
     * @param pixels the list of all the pixels to display
     */
    private void renderPixels(List<Pixel> pixels) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        for (Pixel pix : pixels) {
            pix.render(context);
        }
    }

    /**
     * Attributes to each subpixel a color
     *
     * @param subPixels the list of all subpixels to display
     */
    private void setSubPixelsColors(List<SubPixel> subPixels) {
        int nonBlackPixelsCount = countNonBlackSubPixels(subPixels);
        if (nonBlackPixelsCount == 0) return;
        Color[] colors = histogram.generate(nonBlackPixelsCount);
        subPixels.sort(SubPixel::compare);
        int pixCount = 0;
        for (SubPixel pix : subPixels) {
            pix.setColor(colors[pixCount]);
            pixCount++;
            if (pixCount >= colors.length) // remaining subpixels stay black (converge).
                break;
        }
    }


    /**
     * Count how many subpixel diverge.
     *
     * @param subPixels the subpixels to display
     * @return the number of diverging subpixels
     */
    private int countNonBlackSubPixels(List<SubPixel> subPixels) {
        return (int)
                subPixels.stream()
                        .filter(pix -> pix.value != Double.POSITIVE_INFINITY)
                        .count();
    }

    /**
     * Generates the list of all the pixels in the canvas
     *
     * @return the list of pixels
     */
    private List<Pixel> getPixels() {
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();
        List<SubPixel> subPixels =
                new ArrayList<>(width * height * SUPERSAMPLING * SUPERSAMPLING);
        List<Pixel> pixels =
                new ArrayList<>(width * height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Pixel pix = preparePixel(x, y);
                subPixels.addAll(pix.getSubPixels());
                pixels.add(pix);
            }
        }
        setSubPixelsColors(subPixels);
        return pixels;
    }

    /**
     * Create the pixel with given coordinates
     *
     * @param x horizontal coordinate of the pixel
     * @param y vertical coordinate of the pixel
     * @return the computed pixel with given coordinates
     */
    private Pixel preparePixel(int x, int y) {
        double width = SUPERSAMPLING * canvas.getWidth();
        double height = SUPERSAMPLING * canvas.getHeight();
        List<SubPixel> sampledSubPixels = new ArrayList<>();
        for (int i = 0; i < SUPERSAMPLING; i++) {
            for (int j = 0; j < SUPERSAMPLING; j++) {
                Complex z =
                        camera.toComplex(
                                ((double) (SUPERSAMPLING * x) + i) / width,
                                1 - ((double) (SUPERSAMPLING * y) + j) / height // invert y-axis
                        );
                double divergence = mandelbrot.divergence(z);
                sampledSubPixels.add(new SubPixel(divergence));
            }
        }
        return new Pixel(x, y, sampledSubPixels);
    }
}
