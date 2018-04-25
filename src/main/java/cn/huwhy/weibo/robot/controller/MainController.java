package cn.huwhy.weibo.robot.controller;

import cn.huwhy.common.util.RandomUtil;
import cn.huwhy.common.util.StringUtil;
import cn.huwhy.weibo.robot.AppContext;
import cn.huwhy.weibo.robot.model.Member;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends BaseController implements Initializable {

    private String code;

    @FXML
    private TextField txWbName, txWbPwd, txCode;
    @FXML
    private Label lbCode;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab tabWb, tabWords, tabTask, tabAutoTask, tabFans, tabData, tabMy;

    public void refreshCode() {
        code = RandomUtil.getRandomNum(4);
        lbCode.setText("验证码 " + code + " 刷新");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        code = RandomUtil.getRandomNum(4);
        refreshCode();
        tabPane.getTabs().removeAll(tabWords, tabTask, tabAutoTask, tabFans, tabData, tabMy);
    }

    public void init() {
        Member member = AppContext.getMember();
        if (member != null) {
            txWbName.setText(member.getWbName());
            if (StringUtil.isNotEmpty(member.getWbName())) {
                txWbName.setEditable(false);
            }
            txWbPwd.setText(member.getWbPassword());
        }
    }

    @FXML
    public void btnLoginClick() {
        if (txCode.getText().equals(this.code)) {
            tabPane.getTabs().remove(tabWb);
            tabPane.getTabs().addAll(tabWords, tabTask, tabAutoTask, tabFans, tabData, tabMy);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "");
            alert.setTitle("提示");
            alert.setHeaderText("验证码错误");
            alert.showAndWait();
        }
    }

    @FXML
    public void tabSelected(Event e) {
        if (e.getTarget() == tabWb) {
        } else if (e.getTarget() == tabWords) {
            Parent parent = AppContext.loadFxml("word/list.fxml");
            tabWords.setContent(parent);
        } else if (e.getTarget() == tabFans) {
            Parent parent = AppContext.loadFxml("fans/list.fxml");
            tabFans.setContent(parent);
        } else if (e.getTarget() == tabTask) {
            Parent parent = AppContext.loadFxml("task/index.fxml");
            tabTask.setContent(parent);
        } else if (e.getTarget()== tabAutoTask) {
            Parent parent = AppContext.loadFxml("task/index2.fxml");
            tabAutoTask.setContent(parent);
        } else if (e.getTarget()== tabData) {
            AppContext.setAutoTask(true);
            Parent parent = AppContext.loadFxml("data/index.fxml");
            tabData.setContent(parent);
        } else if (e.getTarget() == tabMy) {
            Parent parent = AppContext.loadFxml("my/setting.fxml");
            tabMy.setContent(parent);
        }
    }

}
