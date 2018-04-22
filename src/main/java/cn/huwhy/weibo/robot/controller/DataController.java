package cn.huwhy.weibo.robot.controller;

import cn.huwhy.interfaces.Paging;
import cn.huwhy.weibo.robot.model.MyFansTerm;
import cn.huwhy.weibo.robot.model.WordType;
import cn.huwhy.weibo.robot.service.FansService;
import cn.huwhy.weibo.robot.util.SpringContentUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

public class DataController extends BaseController implements Initializable {

    @FXML
    private Text txTotal, txRed, txBlack;
    @FXML
    private PieChart chartCount;

    private FansService fansService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fansService = SpringContentUtil.getBean(FansService.class);
        count();
    }

    @Override
    public void refresh() {
        count();
    }

    private void count() {
        MyFansTerm term = new MyFansTerm();
        term.setPage(1);
        term.setSize(1);
        Paging paging = fansService.findMyFans(term);
        long totalNum, redNum, blackNum;
        totalNum = paging.getTotal();
        txTotal.setText("" + totalNum);

        term.setType(WordType.IRON);
        paging = fansService.findMyFans(term);
        redNum = paging.getTotal();
        double redRate = redNum * 100.0 / totalNum;
        txRed.setText(redNum + "");

        term.setType(WordType.BLACK);
        paging = fansService.findMyFans(term);
        blackNum = paging.getTotal();
        double blackRate = blackNum * 100.0 / totalNum;
        txBlack.setText(blackNum + "");
        double otherRate = 100 - redRate - blackRate;

        ObservableList<PieChart.Data> pieChartData =
                observableArrayList(
                        new PieChart.Data("总数", totalNum),
                        new PieChart.Data(String.format("铁粉%.2f%%", redRate), redNum),
                        new PieChart.Data(String.format("黑粉%.2f%%", blackRate), blackNum),
                        new PieChart.Data(String.format("吃瓜群众%.2f%%", otherRate), totalNum - redNum - blackNum)
                );
        chartCount.setData(pieChartData);
    }
}
