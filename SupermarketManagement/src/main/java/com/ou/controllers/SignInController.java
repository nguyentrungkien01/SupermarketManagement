package com.ou.controllers;

import com.ou.pojos.Staff;
import com.ou.services.SignInService;
import com.ou.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SignInController implements Initializable {
    private SignInService signInService;

    @FXML
    private TextField txtUsername;
    
    @FXML 
    private PasswordField txtPassword;
    
    @FXML
    private Button btnSignIn;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signInService = new SignInService();
        this.btnSignIn.setOnMouseClicked((t) -> {
            checkAccount();
        });
    }

    // Kiểm tra mật khẩu
    @FXML
    private void  checkAccount(){
        try {
            if (checkTextInput() == true){
                Staff staff =signInService.getAccountMD5(this.txtUsername.getText().trim(), this.txtPassword.getText().trim());
//                System.out.println(staff.getStaPassword() +"---"+ staff.getStaUsername());
                if(staff == null)
                    AlertUtils.showAlert("Tên tài khoản hoặc mật khẩu không đúng!", Alert.AlertType.ERROR);
                else{
                    // chuyen windown
                    if(staff.getStaIsAdmin() == true) {// update 11/04/20220
                        HomepageAdminController admin = new HomepageAdminController();
                        admin.setId(staff.getPersId());
                        try {
                            App.setRoot("homepage-admin");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else // Nhân viên
                        AlertUtils.showAlert("Chuyển qua cửa sổ nhân viên", Alert.AlertType.INFORMATION);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Kiểm tra dữ liệu nhập
    private boolean checkTextInput() throws SQLException, NoSuchAlgorithmException{
        if("".equals(this.txtUsername.getText().trim()) || this.txtUsername.getText().trim().length()< 6){
            AlertUtils.showAlert("Tên tài khoản phải có ít nhất 6 kí tự !!", Alert.AlertType.ERROR);
            return false;
        }
        else 
            if("".equals(this.txtPassword.getText().trim()) || this.txtPassword.getText().trim().length()< 6){
                AlertUtils.showAlert("Mật khẩu phải có ít nhất 6 kí tự !!", Alert.AlertType.ERROR);
                return false;
            }
            else{
                return true;
            }
    }

    // Sự kiện bàn phím
    @FXML
    public void setKeyEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            checkAccount();
        }
    }
}
