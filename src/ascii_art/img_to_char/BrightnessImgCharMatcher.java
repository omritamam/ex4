package ascii_art.img_to_char;

import image.Image;

import java.awt.*;
import java.util.Arrays;

public class BrightnessImgCharMatcher {
    private Image image;
    private int pixels;
    private String fontName;
    private double[] charsBrightness;
    private Character[] charSet;

    public BrightnessImgCharMatcher(Image image, String fontName){
        this.image =image;
        this.fontName = fontName;
    }


    public char[][] chooseChars(int numCharsInRow, Character[] charSet){
        charsBrightness = getBrightness(charSet);
        return convertImageToAscii(image, numCharsInRow);
    }

    private double[] getBrightness(Character[] charSet) {
        this.charSet = charSet;
        int [] brightness = new int[charSet.length];
        for(int i = 0; i< charSet.length; i++){
            boolean[][] booleansArray = CharRenderer.getImg(charSet[i], 16, fontName);
            int counter = 0;
            for(boolean[] arr : booleansArray){
                for (boolean ch : arr){
                    if(!ch){
                        counter++;
                    }
                }
            }
            brightness[i] =  counter;
        }
    return lingearTran(brightness);
    }

    public double[] lingearTran(int[] arr){
        double [] brightness = new double[arr.length];
        int min = Arrays.stream(arr).min().getAsInt();
        int max = Arrays.stream(arr).max().getAsInt();
        int denominator = max - min;

        for(int i = 0; i< brightness.length; i++){
            brightness[i] = ((double)(arr[i]-min))/(double) denominator;
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
        int counter = 0;
        double rank=0;
        for(Color color : image.pixels()){
            double greyPixel = getPixelBrightness(color);
            rank += greyPixel;
            counter++;
        }
        return counter/rank;
    }

    private double getPixelBrightness(Color color) {
        return (color.getRed() * 0.2126 + color.getGreen() * 0.7152 +
                color.getBlue() * 0.0722)/255;
    }

    private char[][] convertImageToAscii(Image img, int numCharsInRow){
        pixels = img.getWidth() / numCharsInRow;

        char[][] asciiArt = new char[img.getHeight()/pixels][img.getWidth()/pixels];
        int[] result = new int[img.getHeight()/pixels *img.getWidth()/pixels];
        double[] result2 = new double[img.getHeight()/pixels *img.getWidth()/pixels];
        int i = 0;
        for(Image subImage : image.squareSubImagesOfSize(image.getWidth()/numCharsInRow)){
            result[i]= (int) (255 * (getImageBrightness(subImage)));
            i++;
        }
        result2 = lingearTran(result);
        i=0;
        for(int k =0; k< img.getHeight()/pixels; k++){
            for(int j =0; j< img.getWidth()/pixels; j++) {
                asciiArt[k][j] = matchChar(result2[i]);
                i++;
            }
        }
        return asciiArt;
    }

    private char matchChar(double imageBrightness) {
        int minIndex = 0;
        double minDistance = 1;
        for(int i = 0; i<charSet.length; i++){
            if (Math.abs(charsBrightness[i]-imageBrightness)< minDistance){
                minIndex = i;
                minDistance = Math.abs(charsBrightness[i]-imageBrightness);
            }
        }
        return charSet[minIndex];
    }

}
