package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_art.img_to_char.CharRenderer;
import image.Image;

import java.awt.*;

public class Driver {
    public static void main(String[] args) {
        //boolean[][] cs = CharRenderer.getImg('c', 16, "Ariel");
        //CharRenderer.printBoolArr(cs);
        Image img = Image.fromFile("board.jpeg");
        BrightnessImgCharMatcher obj = new BrightnessImgCharMatcher(img,"Ariel");
        CharRenderer.printCharArr(obj.chooseChars(2,new Character[]{'o','m'}));
    }
}
