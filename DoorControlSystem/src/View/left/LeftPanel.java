package view.left;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class LeftPanel extends JPanel {

    private GridBagLayout gridLayout;
    private Font font;

    private ImagePanel imagePanel;
    private JLabel lblName;
    private JLabel lblStudentId;
    private JLabel lblGender;
    private JLabel lblNationality;
    private JLabel lblDepartment;
    private JLabel lblCollege;
    private LineBorder border;

    public LeftPanel(){
        super();
        createComponent();
        setComponentUi();
        this.repaint();
    }
    public void setImage(Image image){
        imagePanel.setImageIcon(image);
    }
    private void createComponent(){
        this.imagePanel = new ImagePanel();
        this.gridLayout = new GridBagLayout();
        this.border = new LineBorder(Color.black, 5, true);
        this.lblName = new JLabel("이름 :");
        this.lblStudentId = new JLabel("학번 :");
        this.lblGender = new JLabel("성별 :");
        this.lblNationality = new JLabel("국적 :");
        this.lblDepartment = new JLabel("학과 :");
        this.lblCollege = new JLabel("단과대 :");
        this.font = new Font("Serif", Font.BOLD, 40);

    }
    private void setComponentUi(){
        this.setPreferredSize(new Dimension(333, 768));
        this.setBackground(Color.WHITE);
        this.setLayout(gridLayout);
        this.imagePanel.setBorder(border);
        addComponent();
        setComponentFont();
        setComponentPos();

    }
    private void setComponentFont(){
        this.lblName.setFont(this.font);
        this.lblStudentId.setFont(this.font);
        this.lblGender.setFont(this.font);
        this.lblNationality.setFont(this.font);
        this.lblDepartment.setFont(this.font);
        this.lblCollege.setFont(this.font);
    }
    private void setComponentPos(){
        insertUiPos(imagePanel, 0, 0,1,5);
        insertUiPos(lblName, 0, 5,1,2);
        insertUiPos(lblStudentId, 0, 7,1,2);
        insertUiPos(lblGender, 0, 9,1,2);
        insertUiPos(lblNationality, 0, 11,1,2);
        insertUiPos(lblDepartment, 0, 13,1,2);
        insertUiPos(lblCollege, 0, 15,1,2);
    }
    private void addComponent(){
        this.add(imagePanel);
        this.add(lblName);
        this.add(lblStudentId);
        this.add(lblGender);
        this.add(lblNationality);
        this.add(lblDepartment);
        this.add(lblCollege);
    }
    private void insertUiPos(Component com, int x, int y, int w, int h){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        this.gridLayout.setConstraints(com, gbc);
    }

}
