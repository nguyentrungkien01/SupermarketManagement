package com.ou.controllers;

import com.ou.pojos.MemberType;
import com.ou.pojos.Sale;
import com.ou.services.MemberTypeService;
import com.ou.utils.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class MemberTypeController implements Initializable {

    private final static MemberTypeService MEMBER_TYPE_SERVICE;
    static {
        MEMBER_TYPE_SERVICE = new MemberTypeService();
    }
    @FXML
    TextField txtMemberTypeId;
    @FXML
    TextField txtMemberTypeName;
    @FXML
    TextField txtMemberTypeIsActive;
    @FXML
    TextField txtTotalMoney;
    @FXML
    TextField txtTotalAmountMember;
    @FXML
    Text textTotalAmountMemberType;
    @FXML
    TextField txtSearchMemberTypeName;
    @FXML
    TextField txtSalePercent;
    @FXML
    TableView tbvMemberType;
    @FXML
    Button btnAdd;
    @FXML
    Button btnEdit;
    @FXML
    Button btnDelete;
    @FXML
    Button btnBack;
    @FXML
    ComboBox cbSaleId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initInputData();
        this.loadMemberTypeTbvColumns();
        this.loadTableMemberTypeData(txtSearchMemberTypeName.getText());
        this.loadComboBoxSaleId();
        this.cbSaleId.getSelectionModel().selectedItemProperty().
                addListener((observableValue, o, t1) -> loadSalePercent());
        this.tbvMemberType.getSelectionModel().getSelectedItems()
                .addListener((ListChangeListener<? super MemberType>) e -> changeInputData());
        this.txtSearchMemberTypeName.textProperty().addListener(e -> loadTableMemberTypeData(this.txtSearchMemberTypeName.getText()));
        this.loadTotalAmountMemberType();
        this.btnAdd.setOnMouseClicked(e -> addMemberType());
        this.btnDelete.setOnMouseClicked(e -> deleteMemberType());
        this.btnEdit.setOnMouseClicked(e -> updateMemberType());
        this.btnBack.setOnMouseClicked(e -> back());
    }

    // lấy tổng số loại thành viên
    private void loadTotalAmountMemberType(){
        try {
            textTotalAmountMemberType.setText(String.valueOf(MEMBER_TYPE_SERVICE.getTotalAmountMemberType()));;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // load mã giảm giá cho combobox sale_id
    private void loadComboBoxSaleId(){
        try {
            cbSaleId.getItems().addAll(MEMBER_TYPE_SERVICE.getSaleIds());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // thay đổi giá trị sale percent khi sale id thay đổi
    public void loadSalePercent(){
        try {
            if(cbSaleId.getValue() != null && !cbSaleId.getValue().toString().isEmpty())
                this.txtSalePercent.setText(
                        MEMBER_TYPE_SERVICE.getSalePercentBySaleId(Integer.parseInt(cbSaleId.getValue().toString())) + "%");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // khởi tạo dữ liệu vùng input
    private void initInputData(){
        this.txtMemberTypeId.setEditable(false);
        this.txtMemberTypeIsActive.setEditable(false);
        this.txtTotalAmountMember.setEditable(false);
        this.txtSalePercent.setEditable(false);
    }

    // khởi tạo các cột của table view
    private void loadMemberTypeTbvColumns(){
        TableColumn<MemberType, Integer> memtIdColumn = new TableColumn("Mã loại thành viên");
        TableColumn<MemberType, String> memtIsActiveColumn = new TableColumn("Trạng thái");
        TableColumn<MemberType, String> memtNameColumn = new TableColumn("Tên loại thành viên");
        TableColumn<MemberType, BigDecimal> memtTotalMoneyColumn = new TableColumn("Tổng tiền tích lũy tối thiểu");
        TableColumn<MemberType, Integer> memtSaleColumn = new TableColumn("Giảm giá");
        memtIdColumn.setCellValueFactory(new PropertyValueFactory<>("memtId"));
        memtNameColumn.setCellValueFactory(new PropertyValueFactory<>("memtName"));
        memtIsActiveColumn.setCellValueFactory(new PropertyValueFactory<>("memtIsActive"));
        memtTotalMoneyColumn.setCellValueFactory(new PropertyValueFactory<>("memtTotalMoney"));
        memtSaleColumn.setCellValueFactory(new PropertyValueFactory<>("sale"));
        memtIdColumn.setPrefWidth(200);
        memtNameColumn.setPrefWidth(400);
        memtTotalMoneyColumn.setPrefWidth(300);
        memtIsActiveColumn.setPrefWidth(400);
        memtSaleColumn.setPrefWidth(200);
        this.tbvMemberType.getColumns().addAll(memtIdColumn, memtNameColumn, memtTotalMoneyColumn, memtIsActiveColumn, memtSaleColumn);
    }

    // lấy dữ liệu vào table view
    private void loadTableMemberTypeData(String kw){
        try {
            this.tbvMemberType.setItems(FXCollections.observableList(MEMBER_TYPE_SERVICE.getMemberTypes(kw)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Thiết lập vùng dữ liệu input khi lựa chọn thay đổi dưới table view
    private void changeInputData() {
        MemberType memberType = (MemberType) this.tbvMemberType.getSelectionModel().getSelectedItem();
        if (memberType != null) {
            this.cbSaleId.setValue(memberType.getSale().getSaleId());
            this.txtMemberTypeId.setText(String.valueOf(memberType.getMemtId()));
            this.txtMemberTypeName.setText(memberType.getMemtName());
            if(memberType.getMemtIsActive())
                this.txtMemberTypeIsActive.setText("Đang hoạt động");
            else
                this.txtMemberTypeIsActive.setText("Ngưng hoạt động");
            this.txtTotalAmountMember.setText(String.valueOf(memberType.getAmountMember()));
            this.txtSalePercent.setText(String.valueOf(memberType.getSale().getSalePercent()));
            this.txtTotalMoney.setText(memberType.getMemtTotalMoney().toString());
        }
    }

    private void clearInputData(){
        txtMemberTypeId.setText("");
        txtMemberTypeIsActive.setText("");
        txtTotalMoney.setText("");
        txtSalePercent.setText("");
        txtMemberTypeName.setText("");
        cbSaleId.setValue("");
        txtTotalAmountMember.setText("");
    }

    // lọad lại dữ liệu cho scene
    private void reloadData(){
        clearInputData();
        loadTotalAmountMemberType();
        loadTableMemberTypeData("");
    }

    private void addMemberType(){
        MemberType memberType = new MemberType();
        memberType.setMemtName(txtMemberTypeName.getText());
        memberType.setMemtTotalMoney(new BigDecimal(-1));
        memberType.setMemtIsActive(!Objects.equals(txtMemberTypeIsActive.getText(), "Ngưng hoạt động"));
        try {
            if(cbSaleId.getValue() == null || txtTotalMoney.getText().isEmpty() || txtTotalMoney.getText() == null
                    || !txtTotalMoney.getText().matches("\\d+")){
                AlertUtils.showAlert("Thêm thất bại!!", Alert.AlertType.ERROR);
                return;
            }
            memberType.setMemtTotalMoney(new BigDecimal(txtTotalMoney.getText()));
            Sale sale = new Sale();
            sale.setSaleId(Integer.parseInt(cbSaleId.getValue().toString()));
            memberType.setSale(sale);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        try {
            if (MEMBER_TYPE_SERVICE.addMemberType(memberType)) {
                AlertUtils.showAlert("Thêm thành công", Alert.AlertType.INFORMATION);
                reloadData();
            }
            else {
                AlertUtils.showAlert("Thêm thất bại!", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateMemberType(){
        MemberType memberType = new MemberType();
        memberType.setMemtId(0);
        memberType.setMemtName(txtMemberTypeName.getText());
        memberType.setMemtTotalMoney(new BigDecimal(-1));
        memberType.setMemtIsActive(!Objects.equals(txtMemberTypeIsActive.getText(), "Ngưng hoạt động"));
        try {
            if(txtMemberTypeId.getText().isEmpty() || txtMemberTypeId.getText() == null ||
                    cbSaleId.getValue() == null || txtTotalMoney.getText().isEmpty() || txtTotalMoney.getText() == null
                    || !txtTotalMoney.getText().matches("\\d+") || cbSaleId.getValue() == null){
                AlertUtils.showAlert("Sửa thất bại!!", Alert.AlertType.ERROR);
                return;
            }
            memberType.setMemtId(Integer.parseInt(txtMemberTypeId.getText()));
            memberType.setMemtTotalMoney(new BigDecimal(txtTotalMoney.getText()));
            Sale sale = new Sale();
            sale.setSaleId(Integer.parseInt(cbSaleId.getValue().toString()));
            memberType.setSale(sale);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        try {
            if (MEMBER_TYPE_SERVICE.updateMemberType(memberType)) {
                AlertUtils.showAlert("Sửa thành công", Alert.AlertType.INFORMATION);
                reloadData();
            }
            else
                AlertUtils.showAlert("Sửa thất bại!", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteMemberType(){
        MemberType memberType = new MemberType();
        memberType.setMemtId(0);
        try {
            if(txtMemberTypeId.getText().isEmpty() || txtMemberTypeId.getText() == null){
                AlertUtils.showAlert("Xóa thất bại!!", Alert.AlertType.ERROR);
                return;
            }
            memberType.setMemtId(Integer.parseInt(txtMemberTypeId.getText()));
            memberType.setMemtIsActive(Objects.equals(txtMemberTypeIsActive.getText(), "Đang hoạt động"));
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        try {
            if (MEMBER_TYPE_SERVICE.deleteMemberType(memberType)) {
                AlertUtils.showAlert("Xóa thành công", Alert.AlertType.INFORMATION);
                reloadData();
            }
            else
                AlertUtils.showAlert("Xóa thất bại!", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void back(){

    }
}
