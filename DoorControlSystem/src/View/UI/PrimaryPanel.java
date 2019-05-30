package view.UI;

import view.left.BtnPanel;
import view.right.RightPrimaryPanel;

import javax.swing.*;
import java.awt.*;

public class PrimaryPanel extends JPanel {

    private BtnPanel btnPanel;
    private RightPrimaryPanel rightPrimaryPanel;

    public PrimaryPanel(){
        super();
        createComponent();
        setUi();
    }
    private void createComponent(){
        btnPanel = BtnPanel.getInstance();
        rightPrimaryPanel = RightPrimaryPanel.getInstance();
    }
    private void setUi(){
        this.setPreferredSize(new Dimension(1366, 768));
        this.setBackground(Color.black);
        this.setLayout(new BorderLayout(0,0));
        add(btnPanel, BorderLayout.WEST);
        add(rightPrimaryPanel, BorderLayout.CENTER);
    }
    public BtnPanel getBtnPanel() {
        return btnPanel;
    }
    public RightPrimaryPanel getRightPrimaryPanel() {
        return rightPrimaryPanel;
    }
}
