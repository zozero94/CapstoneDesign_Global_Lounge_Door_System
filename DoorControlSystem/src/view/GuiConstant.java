package view;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class GuiConstant {

    public static final Color BACK_COLOR = new Color(34,41,50);
    public static final Color LINE = new Color(54,64,73 );
    public static final Color TEXT = new Color(48,201,235 );
    public static final Color MAN = new Color(45,169,153 );
    public static final Color WOMAN = new Color(107,171,210 );

    public static final Image getImageUrl(String studentId) {
        URL url;
        Image studentImage = null;
        try {
            url = new URL("https://udream.sejong.ac.kr/upload/per/" + studentId + ".jpg?ver=20190515205000");
            studentImage = ImageIO.read(url);
        } catch (IOException es) {
            try {
                url = new URL("https://user-images.githubusercontent.com/34762799/57696828-c094ff80-768c-11e9-862a-5b52b97ca69d.jpg");// url 이미지가 없는 경우
                studentImage = ImageIO.read(url);
            } catch (IOException c) {

            }
        }
        return studentImage;
    }
}
