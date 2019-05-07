package view;

import javax.swing.*;
import java.awt.*;

public class PrimaryPanel extends JPanel {

    private LeftPanel leftPanel;
    private RightPanel rightPanel;

    public PrimaryPanel(){
        super();
        createComponent();
        setUi();

    }

    private void createComponent(){
        leftPanel = new LeftPanel();
        rightPanel = new RightPanel();
    }
    private void setUi(){
        this.setPreferredSize(new Dimension(1366, 768));
        this.setBackground(Color.black);
        this.setLayout(new BorderLayout(5,5));
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    public LeftPanel getLeftPanel() {
        return leftPanel;
    }
    public RightPanel getRightPanel() {
        return rightPanel;
    }
}
