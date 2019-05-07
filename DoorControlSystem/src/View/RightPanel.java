package view;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.time.Year;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class RightPanel extends JPanel {

    private TimeTableXYDataset xyTable;
    private JFreeChart jfreechart;
    private ChartPanel chartPanel;
    public JButton btn;
    public RightPanel(){
        super();
        setUi();
        createComponent();
        setXYTableData();
        chartPanel = new ChartPanel(jfreechart);
        add(chartPanel);

        btn = new JButton("문을여시오");
        add(btn);

    }

    private void createComponent(){
        xyTable = new TimeTableXYDataset();
    }
    private void setUi(){
        this.setPreferredSize(new Dimension(933, 768));
        this.setBackground(Color.white);
    }


    public void setXYTableData(){
        Day day = new Day(1,3,2005);
        Day day1 = new Day(2,3,2005);
        Day day2 = new Day(3,3,2005);
        Day day3 = new Day(4,3,2005);
        Day day4 = new Day(5,3,2005);
        Day day5 = new Day(6,3,2005);
        Day day6 = new Day(7,3,2005);
        Day day7 = new Day(8,3,2005);
        //this.xyTable.add(new Year(2017), 3, "여성");
        this.xyTable.add(day, 3, "여성");
        this.xyTable.add(day1, 3, "여성");
        this.xyTable.add(day2, 3, "여성");
        this.xyTable.add(day3, 3, "여성");
        this.xyTable.add(day4, 3, "여성");
        this.xyTable.add(day5, 3, "여성");
        this.xyTable.add(day6, 3, "여성");
        this.xyTable.add(day7, 3, "여성");
        this.xyTable.add(day, 3, "남성");
        this.xyTable.add(day1, 3, "남성");
        this.xyTable.add(day2, 3, "남성");
        this.xyTable.add(day3, 3, "남성");
        this.xyTable.add(day4, 3, "남성");
        this.xyTable.add(day5, 3, "남성");
        this.xyTable.add(day6, 3, "남성");
        this.xyTable.add(day7, 3, "남성");
        DateAxis dateaxis = new DateAxis("Date");
        dateaxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
        dateaxis.setLowerMargin(0.01D);
        dateaxis.setUpperMargin(0.01D);
        NumberAxis numberaxis = new NumberAxis("Count");
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        numberaxis.setUpperMargin(0.10000000000000001D);
        StackedXYBarRenderer stackedxybarrenderer = new StackedXYBarRenderer(0.14999999999999999D);
        stackedxybarrenderer.setDrawBarOutline(false);
        stackedxybarrenderer.setBaseItemLabelsVisible(true);
        stackedxybarrenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
        stackedxybarrenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER));
        stackedxybarrenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{0} : {1} = {2}", new SimpleDateFormat("yyyy"), new DecimalFormat("0")));
        XYPlot xyplot = new XYPlot(xyTable, dateaxis, numberaxis, stackedxybarrenderer);
        jfreechart = new JFreeChart("Holes-In-One / Double Eagles", xyplot);
        jfreechart.removeLegend();
        jfreechart.addSubtitle(new TextTitle("PGA Tour, 1983 to 2003"));
        TextTitle texttitle = new TextTitle("http://www.golfdigest.com/majors/masters/index.ssf?/majors/masters/gw20040402albatross.html", new Font("Dialog", 0, 8));
        jfreechart.addSubtitle(texttitle);
        jfreechart.setTextAntiAlias(RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
        LegendTitle legendtitle = new LegendTitle(xyplot);
        legendtitle.setBackgroundPaint(Color.white);
        legendtitle.setFrame(new BlockBorder());
        legendtitle.setPosition(RectangleEdge.BOTTOM);
        jfreechart.addSubtitle(legendtitle);
    }
}
