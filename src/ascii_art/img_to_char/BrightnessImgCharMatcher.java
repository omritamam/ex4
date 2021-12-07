package ascii_art.img_to_char;

import image.Image;

import java.awt.*;
import java.util.Arrays;

public class BrightnessImgCharMatcher {
    private final Image image;
    private final String fontName;
    private double[] charsBrightness;
    private Character[] charSet;

    /**
     * constructor
     * @param image - an imange to covert to asciiArt
     * @param fontName - font of the ascii chars
     */
    public BrightnessImgCharMatcher(Image image, String fontName){
        this.image = image;
        this.fontName = fontName;
    }

    /**
     *
     * @param numCharsInRow - number of chars in a single row
     * @param charSet - set of Character[], all possible chars to be returned
     * @return an char[][] array of the image in the field, described by the chars in chaeSet, in resolution of
     * numCharsInRow
     */
    public char[][] chooseChars(int numCharsInRow, Character[] charSet){
        charsBrightness = buildCharsBrightnessArray(charSet);
        return convertImageToAscii(numCharsInRow);
    }

    /**
     *
     * @param charSet - array of chars
     * @return array with brightnesses values for each corresponding char in charSet
     */
    private double[] buildCharsBrightnessArray(Character[] charSet) {
        this.charSet = charSet;
        double [] brightness = new double[charSet.length];
        for(int i = 0; i< charSet.length; i++){
            brightness[i] =  getCharBrightness(charSet[i]);
        }
    return linearTran(brightness);
    }

    /**
     *
     * @param c - a Charcter
     * @return - the brightness in a scale of 0-1
     */
    private double getCharBrightness(Character c) {
        boolean[][] booleansArray = CharRenderer.getImg(c, 16, fontName);
        float counter = 0;
        for(boolean[] arr : booleansArray){
            for (boolean ch : arr){
                if(ch){
                    counter++;
                }
            }
        }
        return counter/256;
    }

    /***
     * stretching the array
     * @param arr - arr
     * @return - new array with transported value
     */
    public double[] linearTran(double[] arr){
        double [] brightness = new double[arr.length];
        double min = Arrays.stream(arr).min().getAsDouble();
        double max = Arrays.stream(arr).max().getAsDouble();
        double denominator = max - min;

        for(int i = 0; i< brightness.length; i++){
            brightness[i] = (arr[i]-min) / denominator;
        }
        return brightness;
    }

    private double getImageBrightness(Image image){
        double pixelsCounter = 0;
        double pixelBrightness=0;
        for(Color color : image.pixels()){
            pixelBrightness += getPixelBrightness(color);
            pixelsCounter+=1;
        }
        return pixelBrightness/pixelsCounter;
    }

    /**
     *
     * @param color - a color
     * @return - value of 0-1 that presents the brightness
     */
    private double getPixelBrightness(Color color) {
        return (color.getRed() * 0.2126 + color.getGreen() * 0.7152 +
                color.getBlue()* 0.0722)/255;
    }

    /**
     *
     * @param numCharsInRow -resolution for the output ascii image
     * @return - an ascii image
     */
    private char[][] convertImageToAscii(int numCharsInRow){
        int pixels = image.getWidth() / numCharsInRow;
        char[][] asciiArt = new char[image.getHeight()/pixels][numCharsInRow];
        double[] result = new double[numCharsInRow * image.getHeight()/pixels];
        int i = 0;
        // gets all subImages corresponding chars
        for(Image subImage : image.squareSubImagesOfSize(pixels)){
            if(i ==result.length){
                break;
            }
            result[i] = getImageBrightness(subImage);
            i++;
        }
        //place the chars in a suitable data structure of 2-D array
        i=0;
        for(int k =0; k< image.getHeight()/pixels; k++){
            for(int j =0; j< numCharsInRow; j++) {
                asciiArt[k][j] = matchChar(result[i]);
                i++;
            }
        }
        return asciiArt;
    }

    /**
     *
     * @param brightness- a double
     * @return a char from charSet with closest brightness value
     */
    private char matchChar(double brightness) {
        int minIndex = 0;
        double minDistance = Double.MAX_VALUE;
        for(int i = 0; i<charSet.length; i++){
            if (Math.abs(charsBrightness[i]-brightness)<= minDistance){
                minIndex = i;
                minDistance = Math.abs(charsBrightness[i]-brightness);
            }
        }
        return charSet[minIndex];
    }

}
