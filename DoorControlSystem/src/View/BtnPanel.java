package view;

import control.action_handel.ButtonListener;
import control.action_handel.MouseListener;
import view.right.GuiConstant;

import javax.swing.*;
import java.awt.*;

public class BtnPanel extends JPanel {

    private static BtnPanel btnPanel = null;
    private JLabel lblImage;
    private ImageIcon mainImage;
    private JButton btn[];
    private ImageIcon icon[];
    private Font font = new Font("HY엽서M", Font.BOLD, 30);
    private JFileChooser fileChooser;


    public static final String BUTTON_NAME[] = {"로그             ", "차트             " ,"데이터저장     ", "문열기           "};
    public static final String BUTTON_IMAGE_PATH[] = {"../resource/log.png", "../resource/chart.png" ,"../resource/excel.png", "../resource/open.png"};

    public synchronized static BtnPanel getInstance(){
        if(btnPanel == null) btnPanel = new BtnPanel();
        return btnPanel;
    }
    private BtnPanel(){
        super();
        setComponentUi();
    }

    private void setComponentUi(){

        createComponent();
        this.setPreferredSize(new Dimension(333, 768));
        this.setBackground(GuiConstant.RIGHTPANEL);
        this.setLayout(null);
        for(int i = 0 ; i < BUTTON_NAME.length; i++) {
            this.btn[i].setBorderPainted(false);
            this.btn[i].setBackground(i == 0 ? GuiConstant.RIGHTPANEL_2 : GuiConstant.RIGHTPANEL);
            this.btn[i].setForeground(Color.white);
            this.btn[i].setFont(font);
            this.btn[i].setFocusPainted(false);
            this.btn[i].setFocusTraversalPolicy(null);
            this.btn[i].setHorizontalTextPosition(SwingConstants.LEFT);
        }
        this.lblImage.setIcon(mainImage);

        setComponentPos();
        addComponent();
    }
    private void createComponent(){
        this.lblImage = new JLabel();
        this.btn = new JButton[4];
        this.icon = new ImageIcon[4];
        for(int i = 0 ; i < BUTTON_NAME.length; i++){
            this.icon[i] = new ImageIcon(getClass().getResource(BUTTON_IMAGE_PATH[i]));
            this.btn[i] = new JButton(BUTTON_NAME[i],icon[i]);
        }
        this.mainImage = new ImageIcon(getClass().getResource("../resource/log.png"));
        this.fileChooser = new JFileChooser();
    }
    private void setComponentPos(){
        lblImage.setBounds(0,0,333,153);
        for(int i = 0; i < 4; i++) btn[i].setBounds(0, (203 + i * 80), 333,80);
    }
    private void addComponent(){
        for(int i = 0 ; i < 4; i++) this.add(btn[i]);
        this.add(lblImage);
    }

    public JLabel getLblImage() {
        return lblImage;
    }
    public void setLblImage(JLabel lblImage) {
        this.lblImage = lblImage;
    }
    public JButton[] getBtn() {
        return btn;
    }
    public void setBtn(JButton[] btn) {
        this.btn = btn;
    }

    public void setBtnMouseListener(MouseListener mouseListener){
        for(int i = 0 ; i < btn.length; i++)
            btn[i].addMouseListener(mouseListener);
    }
    public void setBtnActionListener(ButtonListener btnListener){
        for(int i = 0 ; i < btn.length; i++)
            btn[i].addActionListener(btnListener);
    }
    public void turnButtonColor(JButton button) {
        if (btn[0] == button) {
            btn[0].setBackground(GuiConstant.RIGHTPANEL_2);
            btn[1].setBackground(GuiConstant.RIGHTPANEL);
        }
        else if(btn[1] == button) {
            btn[0].setBackground(GuiConstant.RIGHTPANEL);
            btn[1].setBackground(GuiConstant.RIGHTPANEL_2);
        }
    }
    public void saveExcel(){

           int ret = fileChooser.showOpenDialog(null);

    }

}
