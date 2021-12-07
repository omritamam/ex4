package ascii_art.img_to_char;

import image.Image;

import java.awt.*;
import java.util.Arrays;

public class BrightnessImgCharMatcher {
    private Image image;
    private int pixels;
    private final String fontName;
    private double[] charsBrightness;
    private Character[] charSet;


    public BrightnessImgCharMatcher(Image image, String fontName){
        this.image = image;
        this.fontName = fontName;
    }


    public char[][] chooseChars(int numCharsInRow, Character[] charSet){
        charsBrightness = bulidCharsBrightnessArray(charSet);
        return convertImageToAscii(image, numCharsInRow);
    }


    private double[] bulidCharsBrightnessArray(Character[] charSet) {
        this.charSet = charSet;
        double [] brightness = new double[charSet.length];
        for(int i = 0; i< charSet.length; i++){
            brightness[i] =  getCharBrightness(charSet[i]);
        }
    return lingearTran(brightness);
    }

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

    public double[] lingearTran(double[] arr){
        double [] brightness = new double[arr.length];
        double min = Arrays.stream(arr).min().getAsDouble();
        double max = Arrays.stream(arr).max().getAsDouble();
        double denominator = max - min;

        for(int i = 0; i< brightness.length; i++){
            brightness[i] = (arr[i]-min)  //*255
                                                / denominator;
        }
        return brightness;
    }
    public double[] lingearTran2(int[][] arr){
        double [] brightness = new double[arr.length*arr[0].length];
        int min =1;
        int max = 0;
        for(int[] subarr : arr){
             min = Math.min(min,Arrays.stream(subarr).min().getAsInt());
             max = Math.max(max,Arrays.stream(subarr).max().getAsInt());
        }
        int denominator = max - min;
        int i =0;
        for(int[] subarr : arr){
            for(int bun : subarr) {
                brightness[i] = ((double) (bun - min)) / (double) denominator;
                i++;
            }
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

    private double getPixelBrightness(Color color) {
        return (color.getRed() * 0.2126 + color.getGreen() * 0.7152 +
                color.getBlue()* 0.0722)/255;
    }

    private char[][] convertImageToAscii(Image img, int numCharsInRow){
        pixels = img.getWidth() / numCharsInRow;

        char[][] asciiArt = new char[img.getHeight()/pixels][numCharsInRow];
        double[] result = new double[numCharsInRow * img.getHeight()/pixels];
        int i = 0;
        int i2 = 0;

        for(Image subImage : image.squareSubImagesOfSize(pixels)) {
            i2++;
        }

        System.out.println("pixels are "+pixels);
        System.out.println("img.getHeight() are "+img.getHeight());
        System.out.println("img.getWidth() are "+img.getWidth());
        System.out.println("numCharsInRow are "+numCharsInRow);
        System.out.println("images num is "+i2);
        System.out.println();
        for(Image subImage : image.squareSubImagesOfSize(pixels)){
            if(i ==result.length){
                break;
            }
            result[i]=  (getImageBrightness(subImage));
            i++;
        }
       // result2 = lingearTran(result);
        i=0;
        for(int k =0; k< img.getHeight()/pixels; k++){
            for(int j =0; j< numCharsInRow; j++) {
                asciiArt[k][j] = matchChar(result[i]);
                i++;
            }
        }
        return asciiArt;
    }

    private char matchChar(double imageBrightness) {
        int minIndex = 0;
        double minDistance = Double.MAX_VALUE;
        for(int i = 0; i<charSet.length; i++){
            if (Math.abs(charsBrightness[i]-imageBrightness)<= minDistance){
                minIndex = i;
                minDistance = Math.abs(charsBrightness[i]-imageBrightness);
            }
        }
        return charSet[minIndex];
    }

}
