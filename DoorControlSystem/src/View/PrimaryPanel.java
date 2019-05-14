package view;

import view.left.LeftPanel;
import view.right.RightPanel;

import javax.swing.*;
import java.awt.*;

public class PrimaryPanel extends JPanel {

    private LeftPanel leftPanel;
    private RightPanel rightPanel;


    private BtnPanel btnPanel;
    private RightPrimaryPanel rightPrimaryPanel;

    public PrimaryPanel(){
        super();
        createComponent();
        setUi();

    }

    private void createComponent(){
        //leftPanel = new LeftPanel();
        //rightPanel = new RightPanel();
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
    public void setBtnPanel(BtnPanel btnPanel) {
        this.btnPanel = btnPanel;
    }
    public RightPrimaryPanel getRightPrimaryPanel() {
        return rightPrimaryPanel;
    }
    public void setRightPrimaryPanel(RightPrimaryPanel rightPrimaryPanel) {
        this.rightPrimaryPanel = rightPrimaryPanel;
    }
}
