package view.right;

import control.action_handel.LogChartBtnListener;
import model.DataAccessObject;
import model.dto.Data;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import view.DateCalculator;
import view.GuiConstant;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LogChartPanel extends JPanel {

    private static LogChartPanel logChartPanel = null;
    private DefaultCategoryDataset dataset;
    private StackedBarRenderer renderer;
    private CategoryPlot categoryPlot;
    private JFreeChart jfreechart;
    private ChartPanel chartPanel;
    private JPanel line;
    private TextTitle subTitle;
    private JButton btn[];
    private JComboBox<String> chartType;
    private DataAccessObject dao = new DataAccessObject();
    private Data data = new Data();
    private ArrayList<Data> logs;
    private static final String BUTTON[] = {"<","O",">"};
    private static final String CHART[] = {"월별 조회","국가별 조회","시간대별 조회"};
    private static final String TIME[] = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};
    private static final String DAY[] = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
    private static final Font FONT_ITEM = new Font("고딕", 0, 11);
    private static final Font FONT = new Font("고딕", 0, 12);
    private static final Font FONT_CHART = new Font("고딕", 0, 17);
    private static final Font FONT_CHART_TITLE = new Font("고딕", 0, 30);
    private static final int NEXT_TIME[][] = {{0,-1,0},{0,0,0},{0,1,0}};

    private String date = DateCalculator.currentTimeDay();
    public static synchronized LogChartPanel getInstance(){
        if(logChartPanel == null) logChartPanel = new LogChartPanel();
        return logChartPanel;
    }
    private LogChartPanel(){
        super();
        chartPanel = null;
        createComponent();
        setComponentUi();
        setComponentPos();
        addComponent();
        showDayLogChart(chartType.getSelectedItem().toString());
    }

    private void createComponent(){
        line = new JPanel();
        chartType = new JComboBox<String>(CHART);
        btn = new JButton[BUTTON.length];
        for(int i = 0; i < BUTTON.length;i++) btn[i] = new JButton(BUTTON[i]);
        subTitle = new TextTitle();
    }
    private void setComponentUi(){
        this.setPreferredSize(new Dimension(1033, 700));
        this.setBackground(Color.WHITE);
        this.setLayout(null);
        this.line.setBackground(GuiConstant.LINE);
        this.line.setLayout(null);
        this.chartType.setBackground(Color.WHITE);
        for(int i = 0; i < BUTTON.length; i++) this.btn[i].setBackground(Color.WHITE);

    }
    private void addComponent(){
        this.add(line);
        this.add(btn[0]);
        for(int i = 0; i < BUTTON.length;i++) this.add(btn[i]);
        this.add(chartType);
    }

    private void setComponentPos() {
        this.line.setBounds(0, 60, 1033, 100);
        for(int i = 0; i < BUTTON.length; i++) this.btn[i].setBounds(300 + (i * 200), 713, 50, 40);
        this.chartType.setBounds(30, 203, 210,30);
    }

    public void setBtnActionListener(LogChartBtnListener logChartBtnListener){
        for(int i = 0; i < BUTTON.length; i++) btn[i].addActionListener(logChartBtnListener);
        this.chartType.addActionListener(logChartBtnListener);
    }

    private void setDatasetDaily(){
        dataset = new DefaultCategoryDataset();
        for (int i = 0 ; i < DAY.length ; i++) {
            dataset.addValue(0,"남자",DAY[i]);//데이터 삽입
            dataset.addValue(0,"여자",DAY[i]);//데이터 삽입
        }
        for (Data log: logs) {
            dataset.addValue(log.getCount(),log.getGender(),log.getTime().substring(3,5));//데이터 삽입
        }
    }

    private void setDatasetTime(){
        dataset = new DefaultCategoryDataset();
        for (int i = 0 ; i < TIME.length ; i++) {
            dataset.addValue(0,"남자",TIME[i]);//데이터 삽입
            dataset.addValue(0,"여자",TIME[i]);//데이터 삽입
        }
        for (Data log: logs) {
            dataset.addValue(log.getCount(),log.getGender(),log.getTime());//데이터 삽입
        }
    }
    private void setDatasetNationality(){
        dataset = new DefaultCategoryDataset();
        for (Data log: logs) {
            dataset.addValue(log.getCount(),log.getGender(),log.getNationality());//데이터 삽입
        }
    }
    private void setChartUi(String mode){
        jfreechart.getTitle().setFont(FONT_CHART_TITLE);
        jfreechart.getLegend().setItemFont(FONT);
        categoryPlot = (CategoryPlot) jfreechart.getPlot();
        categoryPlot.getDomainAxis().setLabelFont(FONT_CHART);
        categoryPlot.getDomainAxis().setTickLabelFont(FONT_ITEM);
        categoryPlot.getRangeAxis().setLabelFont(FONT_CHART);
        categoryPlot.setBackgroundPaint(Color.WHITE);// 표 메인 배경색 변경
        categoryPlot.setRangeGridlinePaint(GuiConstant.BACK_COLOR);// 표 메인 배경색 변경
        if(!mode.equals(CHART[2])) {
            renderer = (StackedBarRenderer) categoryPlot.getRenderer();
            renderer.setSeriesPaint(0, GuiConstant.WOMAN);
            renderer.setSeriesPaint(1, GuiConstant.MAN);
            renderer.setDrawBarOutline(true);
            renderer.setBaseItemLabelsVisible(true);
            renderer.setItemMargin(0.5);
            renderer.setBaseItemLabelFont(FONT_ITEM);
            renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        }
    }
    public void showDayLogChart(String mode){
        data.setTime(date);
        logs = dao.getArrayListData(data,chartType.getSelectedIndex() + 3);
        turnOffButton();
        if(mode.equals(CHART[0])) {
            setDatasetDaily();
            subTitle.setText(date.substring(0,7));
            jfreechart = ChartFactory.createStackedBarChart("Global Lounge 출입 횟수", "날짜", "출입횟수", dataset, PlotOrientation.VERTICAL, true, true, true);
            turnOnButton();
        }else if(mode.equals(CHART[1])){
            setDatasetNationality();
            subTitle.setText("국가별");
            jfreechart = ChartFactory.createStackedBarChart("Global Lounge 출입 횟수", "국가명", "출입횟수", dataset, PlotOrientation.VERTICAL, true, true, true);
        }else if(mode.equals(CHART[2])){
            setDatasetTime();
            subTitle.setText("시간별");
            jfreechart = ChartFactory.createLineChart("Global Lounge 출입 횟수","시간","출입횟수",dataset,PlotOrientation.VERTICAL,true,true,true);
        }
        setChartUi(mode);
        jfreechart.addSubtitle(subTitle);
        if(chartPanel != null) remove(chartPanel);
        chartPanel = new ChartPanel(jfreechart);
        chartPanel.setBounds(10, 250,1000,453);
        chartPanel.setDomainZoomable(false);
        add(chartPanel);
        repaint();
    }
    public void changDate(JButton button){
        for(int i = 0; i < BUTTON.length; i++){
            if(button.getText().equals(BUTTON[i]))
            {
                if(button.getText().equals(BUTTON[1])) this.date = DateCalculator.currentTimeDay();
                else this.date = DateCalculator.nextDate(date, NEXT_TIME[i][0],NEXT_TIME[i][1],NEXT_TIME[i][2]);

                showDayLogChart(chartType.getSelectedItem().toString());
                break;
            }
        }
    }
    private void turnOffButton(){
        for(int i = 0; i < btn.length; i++) {
            this.btn[i].setVisible(false);
            this.btn[i].setSelected(false);
        }
    }
    private void turnOnButton(){
        for(int i = 0; i < btn.length; i++) {
            this.btn[i].setVisible(true);
            this.btn[i].setSelected(true);
        }
    }
    public JComboBox<String> getChartType() {
        return chartType;
    }
}
