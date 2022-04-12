package com.ou.controllers;

import com.ou.pojos.LimitSale;
import com.ou.services.LimitSaleService;
import com.ou.utils.AlertUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class LimitSaleController implements Initializable {
    private final static LimitSaleService LIMIT_SALE_SERVICE;
    private static final StringConverter<LocalDate> STRING_CONVERTER;
    static {
        LIMIT_SALE_SERVICE = new LimitSaleService();
        STRING_CONVERTER = new StringConverter<>() {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null)
                    return null;
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String s) {
                if (s == null || s.trim().isEmpty())
                    return null;
                return LocalDate.parse(s, dateTimeFormatter);
            }
        };
    }

    @FXML
    ComboBox cbProductId;
    @FXML
    ComboBox cbSaleId;
    @FXML
    DatePicker dpLsalFromDate;
    @FXML
    DatePicker dpLsalToDate;
    @FXML
    TextField txtSaleIsActive;
    @FXML
    Text textAmountProduct;
    @FXML
    DatePicker dpSearchDate;
    @FXML
    Button btnAdd;
    @FXML
    Button btnEdit;
    @FXML
    Button btnDelete;
    @FXML
    Button btnBack;
    @FXML
    TableView tbvLimitSale;
    @FXML
    Text textTotalAmountLimitSale;
    @FXML
    TextArea textAreaListProductId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initInputData();
        this.initLimitSaleTbv();
        this.loadLimitSaleTbvColumns();
        this.loadLimitSaleTbvData();
        this.loadTotalAmountLimitSale();
        this.loadComboboxSaleId();
        this.loadComboboxProductId();
        this.tbvLimitSale.getSelectionModel().getSelectedItems()
                .addListener((ListChangeListener<? super LimitSale>) e -> changeInputData());
        this.cbSaleId.getSelectionModel().selectedItemProperty().
                addListener((observableValue, o, t1) -> LimitSaleController.this.changeData());
        this.btnAdd.setOnMouseClicked(e -> addLimitSale());
        this.btnEdit.setOnMouseClicked(e -> updateLimitSale());
        this.btnDelete.setOnMouseClicked(e -> deleteLimitSale());
        this.btnBack.setOnMouseClicked(e -> back());
        this.dpSearchDate.setOnAction(e -> loadLimitSaleTbvData());
    }

    // KHởi tạo các thuộc tính của vùng input
    private void initInputData() {
        this.txtSaleIsActive.setEditable(false);
        this.textAreaListProductId.setEditable(false);
        this.dpLsalToDate.setConverter(STRING_CONVERTER);
        this.dpLsalFromDate.setConverter(STRING_CONVERTER);
        this.dpSearchDate.setConverter(STRING_CONVERTER);
    }

    // Khởi tạo các thuộc tính của table view
    private void initLimitSaleTbv() {
        this.tbvLimitSale.setPlaceholder(new Label("Không có mã giảm giá nào!"));
        this.tbvLimitSale.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.tbvLimitSale.setEditable(false);
    }

    private void loadTotalAmountLimitSale() {
        try {
            this.textTotalAmountLimitSale.setText(String.valueOf(LIMIT_SALE_SERVICE.getTotalAmountLimitSale()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy dữ liệu cho table view
    private void loadLimitSaleTbvData() {
        try {
            if(this.dpSearchDate.getValue()!=null)
                this.tbvLimitSale.setItems(FXCollections.observableList(LIMIT_SALE_SERVICE.
                    getLimitSales(Date.valueOf(this.dpSearchDate.getValue().toString()))));
            else
                this.tbvLimitSale.setItems(FXCollections.observableList(LIMIT_SALE_SERVICE.
                        getLimitSales(null)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // lấy dữ liệu cho combobox product id
    private void loadComboboxProductId(){
        try {
            cbProductId.getItems().clear();
            cbProductId.getItems().addAll(LIMIT_SALE_SERVICE.getListProductId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // lấy dữ liệu cho combobox sale id
    private void loadComboboxSaleId(){
        try {
            cbSaleId.getItems().addAll(LIMIT_SALE_SERVICE.getListSaleId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tạo các cột và match dữ liệu cho table view
    private void loadLimitSaleTbvColumns() {
        TableColumn<LimitSale, Integer> lsalIdColumn = new TableColumn<>("Mã giả giá có thời hạn");
        TableColumn<LimitSale, Date> lsalFromDateColumn = new TableColumn<>("Từ ngày");
        TableColumn<LimitSale, Date> lsalToDateColumn = new TableColumn<>("Đến ngày");
        TableColumn<LimitSale, Boolean> saleIsActiveColumn = new TableColumn<>("Trạng thái");
        TableColumn<LimitSale, Integer> amountProductColumn = new TableColumn<>("Số sản phẩm áp dụng");
        lsalIdColumn.setCellValueFactory(new PropertyValueFactory<>("saleId"));
        lsalFromDateColumn.setCellValueFactory(new PropertyValueFactory<>("lsalFromDate"));
        lsalToDateColumn.setCellValueFactory(new PropertyValueFactory<>("lsalToDate"));
        saleIsActiveColumn.setCellValueFactory(new PropertyValueFactory<>("saleIsActive"));
        amountProductColumn.setCellValueFactory(new PropertyValueFactory<>("amountProduct"));
        lsalIdColumn.setPrefWidth(200);
        lsalFromDateColumn.setPrefWidth(400);
        lsalToDateColumn.setPrefWidth(400);
        saleIsActiveColumn.setPrefWidth(300);
        amountProductColumn.setPrefWidth(200);
        lsalIdColumn.setSortType(TableColumn.SortType.DESCENDING);
        this.tbvLimitSale.getColumns().addAll(lsalIdColumn, lsalFromDateColumn, lsalToDateColumn, saleIsActiveColumn, amountProductColumn);
    }

    // Thiết lập vùng dữ liệu input khi lựa chọn thay đổi dưới table view
    private void changeInputData() {
        LimitSale selectedLimitSale = (LimitSale) this.tbvLimitSale.getSelectionModel().getSelectedItem();
        if (selectedLimitSale != null) {
            this.cbSaleId.setValue(String.valueOf(selectedLimitSale.getSaleId()));
            this.txtSaleIsActive.setText(selectedLimitSale.getSaleIsActive()? "Đang hoạt động" : "Ngưng hoạt động");
            this.dpLsalFromDate.setValue(LocalDate.parse(selectedLimitSale.getLsalFromDate().toString()));
            this.dpLsalToDate.setValue(LocalDate.parse(selectedLimitSale.getLsalToDate().toString()));
            this.textAmountProduct.setText(String.valueOf(selectedLimitSale.getAmountProduct()));
            this.getIdProductByLsalId();
        }
    }

    // Lấy các id sản phẩm thuộc sale limit
    private void getIdProductByLsalId(){
        this.textAreaListProductId.setText("");
        try {
            List<String> productIds = LIMIT_SALE_SERVICE.getIdProductByLsalId(Integer.parseInt(cbSaleId.getValue().toString()));
            for(int i = 0; i < productIds.size(); i++) {
                this.textAreaListProductId.setText(String.format("%s (%s)", this.textAreaListProductId.getText(), productIds.get(i)));
            };
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // thay đổi dữ liệu vùng input khi lsal_id thay đổi
    private void changeData(){
        if(cbSaleId.getValue().toString() != null && !cbSaleId.getValue().toString().isEmpty() && !Objects.equals(cbSaleId.getValue().toString(), "")) {
            try {
                int lsal_id = parseInt(cbSaleId.getValue().toString());
                if(LIMIT_SALE_SERVICE.isExitsLimitSale(lsal_id)){
                    cbProductId.getItems().clear();
                    cbProductId.getItems().addAll(LIMIT_SALE_SERVICE.
                            getProductIdNotInLimitSaleByLsalId(lsal_id));
                    LimitSale limitSale = LIMIT_SALE_SERVICE.getLimitSaleByLsalId(lsal_id);
                    this.txtSaleIsActive.setText(limitSale.getSaleIsActive()? "Đang hoạt động" : "Ngưng hoạt động");
                    this.dpLsalFromDate.setValue(LocalDate.parse(limitSale.getLsalFromDate().toString()));
                    this.dpLsalToDate.setValue(LocalDate.parse(limitSale.getLsalToDate().toString()));
                    this.textAmountProduct.setText(String.valueOf(limitSale.getAmountProduct()));
                    this.getIdProductByLsalId();
                }else {
                    loadComboboxProductId();
                    textAreaListProductId.clear();
                    dpLsalFromDate.setValue(LocalDate.now());
                    dpLsalToDate.setValue(LocalDate.now());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // xóa dữ liêu vùng input
    private  void clearInputData(){
        this.textAmountProduct.setText("0");
        this.cbProductId.getItems().clear();
        this.txtSaleIsActive.setText("");
        this.textAreaListProductId.clear();
        this.cbSaleId.setValue("");
        this.dpLsalFromDate.setValue(LocalDate.now());
        this.dpLsalToDate.setValue(LocalDate.now());
    }

    // reload data
    private void reloadData(){
        loadLimitSaleTbvData();
        loadTotalAmountLimitSale();
        clearInputData();
    }

    // thêm 1 limitsale
    private void addLimitSale(){
        LimitSale limitSale = new LimitSale();
        try {
            limitSale.setSaleId(parseInt(cbSaleId.getValue().toString()));
            limitSale.setSaleIsActive(Objects.equals(txtSaleIsActive.getText(), "Đang hoạt động"));
            if(!LIMIT_SALE_SERVICE.isExitsLimitSale(limitSale.getSaleId())){
                limitSale.setLsalFromDate(Date.valueOf(dpLsalFromDate.getValue()));
                limitSale.setLsalToDate(Date.valueOf(dpLsalToDate.getValue()));
                if(limitSale.getLsalFromDate().compareTo(limitSale.getLsalToDate()) > 0) {
                    AlertUtils.showAlert("Thêm thất bại! vui lòng chọn ngày hợp lệ!!!", Alert.AlertType.ERROR);
                    return;
                }
            }
        } catch (Exception e) {
            AlertUtils.showAlert("Thêm thất bại!", Alert.AlertType.ERROR);
            return;
        }
        try {
            int proId = 0;
            if(cbProductId.getValue()!= null)
                proId = parseInt(cbProductId.getValue().toString());
            if(LIMIT_SALE_SERVICE.addLimitSale(limitSale, proId)){
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
    // cập nhật ngày của limit sale
    private void updateLimitSale(){
        LimitSale limitSale = new LimitSale();
        try {
            limitSale.setSaleId(parseInt(cbSaleId.getValue().toString()));
            if(!LIMIT_SALE_SERVICE.isExitsLimitSale(limitSale.getSaleId())){
                AlertUtils.showAlert("Sửa thất bại! mã không tồn tại!!!", Alert.AlertType.ERROR);
                return;
            }
            if(txtSaleIsActive.getText().equals("Ngưng hoạt động")){
                AlertUtils.showAlert("Sửa thất bại! mã đã ngưng hoạt động!!!", Alert.AlertType.ERROR);
                return;
            }
            limitSale.setLsalFromDate(Date.valueOf(dpLsalFromDate.getValue()));
            limitSale.setLsalToDate(Date.valueOf(dpLsalToDate.getValue()));
            if(limitSale.getLsalFromDate().compareTo(limitSale.getLsalToDate()) > 0) {
                AlertUtils.showAlert("Sửa thất bại! vui lòng chọn ngày hợp lệ!!!", Alert.AlertType.ERROR);
                return;
            }
        } catch (Exception e) {
            AlertUtils.showAlert("Sửa thất bại!", Alert.AlertType.ERROR);
            return;
        }
        try {
            if(LIMIT_SALE_SERVICE.updateLimitSale(limitSale)){
                AlertUtils.showAlert("Sửa thành công", Alert.AlertType.INFORMATION);
                reloadData();
            }
            else {
                AlertUtils.showAlert("sửa thất bại!", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // xóa limit sale
    private void deleteLimitSale(){
        LimitSale limitSale = new LimitSale();
        try {
            limitSale.setSaleId(parseInt(cbSaleId.getValue().toString()));
            if(!LIMIT_SALE_SERVICE.isExitsLimitSale(limitSale.getSaleId())){
                AlertUtils.showAlert("Xóa thất bại! mã không tồn tại!!!", Alert.AlertType.ERROR);
                return;
            }
        } catch (Exception e) {
            AlertUtils.showAlert("Xóa thất bại!", Alert.AlertType.ERROR);
            return;
        }
        try {
            if(LIMIT_SALE_SERVICE.deleteLimitSale(limitSale)){
                AlertUtils.showAlert("Xóa thành công", Alert.AlertType.INFORMATION);
                reloadData();
            }
            else {
                AlertUtils.showAlert("Xóa thất bại!", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // trở về homepage
    private void back(){

    }
}
