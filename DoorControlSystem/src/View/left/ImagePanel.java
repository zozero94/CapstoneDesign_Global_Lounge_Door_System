package view.left;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {

    private JLabel lblImage;
    private ImageIcon studentImage;


    public ImagePanel(){
        super();
        createComponent();
        setComponentUi();
        addComponent();
    }
    public void createComponent(){
        this.lblImage = new JLabel();

    }
    public void setComponentUi(){
        this.setPreferredSize(new Dimension(400, 400));
        this.setBackground(Color.WHITE);
    }
    public void addComponent(){
        this.add(lblImage);
    }

    public void setImageIcon(Image studentImage){
        this.studentImage = new ImageIcon(studentImage);
        lblImage.setIcon(this.studentImage);

    }

}
