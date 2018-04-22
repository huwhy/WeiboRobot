package cn.huwhy.weibo.robot.controller;

import cn.huwhy.weibo.robot.model.Member;
import cn.huwhy.weibo.robot.service.MemberService;
import cn.huwhy.weibo.robot.util.SpringContentUtil;
import cn.huwhy.weibo.robot.view.AppContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends BaseController implements Initializable{

    @FXML
    private Text actionTarget;
    @FXML
    private TextField txUsername;
    @FXML
    private PasswordField txPassword;

    private MemberService memberService;

    @FXML
    public void handleSubmitButtonAction(ActionEvent e) {
        actionTarget.setText("Sign in button pressed");
        if (txUsername.getText().equals("") || txPassword.getText().equals("")) {
            actionTarget.setText("用户密码错误");
        } else {
            Member member = memberService.getByName(txUsername.getText());
            if (member != null && member.getPassword().equals(txPassword.getText())) {
                member.setConfig(memberService.getConfig(member.getId()));
                AppContext.showMain(member);
            } else {
                actionTarget.setText("用户密码错误");
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.memberService = SpringContentUtil.getBean(MemberService.class);
    }
}
