package com.ou.controllers;

import com.ou.pojos.LimitSale;
import com.ou.services.LimitSaleService;
import com.ou.utils.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
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
            private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
                try{
                    return LocalDate.parse(s, dateTimeFormatter);
                }catch (DateTimeException dateTimeException){
                    return null;
                }

            }
        };
    }

    @FXML
    ComboBox cbProductLimitSaleId;
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
    @FXML
    ComboBox cbProductNotInLimitSaleId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ((Stage)App.window).setTitle("Qu???n l?? gi???m gi?? c?? th???i h???n - OU Market");
        this.initInputData();
        this.initLimitSaleTbv();
        this.loadLimitSaleTbvColumns();
        this.loadLimitSaleTbvData();
        this.loadTotalAmountLimitSale();
        this.loadComboboxSaleId();
        this.tbvLimitSale.getSelectionModel().getSelectedItems()
                .addListener((ListChangeListener<? super LimitSale>) e -> changeInputData());
        this.cbSaleId.getSelectionModel().selectedItemProperty().
                addListener((observableValue, o, t1) -> LimitSaleController.this.changeData());
        this.btnAdd.setOnMouseClicked(e -> addLimitSale());
        this.btnEdit.setOnMouseClicked(e -> updateLimitSale());
        this.btnDelete.setOnMouseClicked(e -> deleteLimitSale());
        this.btnBack.setOnAction(e -> back());
        this.dpSearchDate.setOnAction(e -> loadLimitSaleTbvData());
    }

    // KH???i t???o c??c thu???c t??nh c???a v??ng input
    private void initInputData() {
        this.txtSaleIsActive.setEditable(false);
        this.textAreaListProductId.setEditable(false);
        this.dpLsalToDate.setConverter(STRING_CONVERTER);
        this.dpLsalFromDate.setConverter(STRING_CONVERTER);
        this.dpSearchDate.setConverter(STRING_CONVERTER);
    }

    // Kh???i t???o c??c thu???c t??nh c???a table view
    private void initLimitSaleTbv() {
        this.tbvLimitSale.setPlaceholder(new Label("Kh??ng c?? m?? gi???m gi?? n??o!"));
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

    // L???y d??? li???u cho table view
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

    // l???y d??? li???u cho combobox sale id
    private void loadComboboxSaleId(){
        try {
            cbSaleId.getItems().addAll(LIMIT_SALE_SERVICE.getListSaleId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // T???o c??c c???t v?? match d??? li???u cho table view
    private void loadLimitSaleTbvColumns() {
        TableColumn<LimitSale, Integer> lsalIdColumn = new TableColumn<>("M?? gi??? gi?? c?? th???i h???n");
        TableColumn<LimitSale, Date> lsalFromDateColumn = new TableColumn<>("T??? ng??y");
        TableColumn<LimitSale, Date> lsalToDateColumn = new TableColumn<>("?????n ng??y");
        TableColumn<LimitSale, Boolean> saleIsActiveColumn = new TableColumn<>("Tr???ng th??i");
        TableColumn<LimitSale, Integer> amountProductColumn = new TableColumn<>("S??? s???n ph???m ??p d???ng");
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

    // Thi???t l???p v??ng d??? li???u input khi l???a ch???n thay ?????i d?????i table view
    private void changeInputData() {
        LimitSale selectedLimitSale = (LimitSale) this.tbvLimitSale.getSelectionModel().getSelectedItem();
        if (selectedLimitSale != null) {
            this.cbSaleId.setValue(String.valueOf(selectedLimitSale.getSaleId()));
            this.txtSaleIsActive.setText(selectedLimitSale.getSaleIsActive()? "??ang ho???t ?????ng" : "Ng??ng ho???t ?????ng");
            this.dpLsalFromDate.setValue(LocalDate.parse(selectedLimitSale.getLsalFromDate().toString()));
            this.dpLsalToDate.setValue(LocalDate.parse(selectedLimitSale.getLsalToDate().toString()));
            this.textAmountProduct.setText(String.valueOf(selectedLimitSale.getAmountProduct()));
            this.getIdProductByLsalId();
        }
    }

    // L???y c??c id s???n ph???m thu???c sale limit
    private void getIdProductByLsalId(){
        this.textAreaListProductId.setText("");
        try {
            List<String> productIds = LIMIT_SALE_SERVICE.getIdProductByLsalId(Integer.parseInt(cbSaleId.getValue().toString()));
            for (String productId : productIds) {
                this.textAreaListProductId.setText(String.format("%s (%s)", this.textAreaListProductId.getText(), productId));
            }
            ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // thay ?????i d??? li???u v??ng input khi lsal_id thay ?????i
    private void changeData(){
        if(cbSaleId.getValue().toString() != null && !cbSaleId.getValue().toString().isEmpty() && !Objects.equals(cbSaleId.getValue().toString(), "")) {
            try {
                int lsal_id = parseInt(cbSaleId.getValue().toString());
                if(LIMIT_SALE_SERVICE.isExitsLimitSale(lsal_id)){
                    cbProductNotInLimitSaleId.getItems().clear();
                    cbProductLimitSaleId.getItems().clear();
                    cbProductNotInLimitSaleId.getItems().addAll(LIMIT_SALE_SERVICE.
                            getProductIdNotInLimitSaleByLsalId(lsal_id));
                    cbProductLimitSaleId.getItems().addAll(LIMIT_SALE_SERVICE.
                            getIdProductByLsalId(lsal_id));
                    LimitSale limitSale = LIMIT_SALE_SERVICE.getLimitSaleByLsalId(lsal_id);
                    this.txtSaleIsActive.setText(limitSale.getSaleIsActive()? "??ang ho???t ?????ng" : "Ng??ng ho???t ?????ng");
                    this.dpLsalFromDate.setValue(LocalDate.parse(limitSale.getLsalFromDate().toString()));
                    this.dpLsalToDate.setValue(LocalDate.parse(limitSale.getLsalToDate().toString()));
                    this.textAmountProduct.setText(String.valueOf(limitSale.getAmountProduct()));
                    this.getIdProductByLsalId();
                }else {
                    cbProductLimitSaleId.getItems().clear();
                    cbProductNotInLimitSaleId.getItems().clear();
                    cbProductNotInLimitSaleId.getItems().addAll(LIMIT_SALE_SERVICE.
                            getProductIdNotInLimitSaleByLsalId(lsal_id));
                    textAreaListProductId.clear();
                    textAreaListProductId.setText("Ch??a c?? s???n ph???m n??o!!!");
                    txtSaleIsActive.setText("");
                    dpLsalFromDate.setValue(LocalDate.now());
                    dpLsalToDate.setValue(LocalDate.now());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // x??a d??? li??u v??ng input
    private  void clearInputData(){
        this.textAmountProduct.setText("0");
        this.cbProductLimitSaleId.getItems().clear();
        this.cbProductNotInLimitSaleId.getItems().clear();
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

    // th??m 1 limitsale
    private void addLimitSale(){
        LimitSale limitSale = new LimitSale();
        try {
            limitSale.setSaleId(parseInt(cbSaleId.getValue().toString()));
            limitSale.setSaleIsActive(!Objects.equals(txtSaleIsActive.getText(), "Ng??ng ho???t ?????ng"));
            if(!limitSale.getSaleIsActive() && cbProductNotInLimitSaleId.getValue()!=null){
                AlertUtils.showAlert("Th??m th???t b???i! M?? ???? ng??ng s??? d???ng!!!", Alert.AlertType.ERROR);
                return;
            }
            if(!LIMIT_SALE_SERVICE.isExitsLimitSale(limitSale.getSaleId())){
                limitSale.setLsalFromDate(Date.valueOf(dpLsalFromDate.getValue()));
                limitSale.setLsalToDate(Date.valueOf(dpLsalToDate.getValue()));
                if(limitSale.getLsalFromDate().compareTo(limitSale.getLsalToDate()) > 0) {
                    AlertUtils.showAlert("Th??m th???t b???i! vui l??ng ch???n ng??y h???p l???!!!", Alert.AlertType.ERROR);
                    return;
                }
            }
        } catch (Exception e) {
            AlertUtils.showAlert("Th??m th???t b???i!", Alert.AlertType.ERROR);
            return;
        }
        try {
            int proId = 0;
            if(cbProductNotInLimitSaleId.getValue()!= null)
                proId = parseInt(cbProductNotInLimitSaleId.getValue().toString());
            if(LIMIT_SALE_SERVICE.addLimitSale(limitSale, proId)){
                AlertUtils.showAlert("Th??m th??nh c??ng", Alert.AlertType.INFORMATION);
                reloadData();
            }
            else {
                AlertUtils.showAlert("Th??m th???t b???i!", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // c???p nh???t ng??y c???a limit sale
    private void updateLimitSale(){
        LimitSale limitSale = new LimitSale();
        try {
            limitSale.setSaleId(parseInt(cbSaleId.getValue().toString()));
            if(!LIMIT_SALE_SERVICE.isExitsLimitSale(limitSale.getSaleId())){
                AlertUtils.showAlert("S???a th???t b???i! m?? kh??ng t???n t???i!!!", Alert.AlertType.ERROR);
                return;
            }
            if(txtSaleIsActive.getText().equals("Ng??ng ho???t ?????ng")){
                AlertUtils.showAlert("S???a th???t b???i! m?? ???? ng??ng ho???t ?????ng!!!", Alert.AlertType.ERROR);
                return;
            }
            limitSale.setLsalFromDate(Date.valueOf(dpLsalFromDate.getValue()));
            limitSale.setLsalToDate(Date.valueOf(dpLsalToDate.getValue()));
            if(limitSale.getLsalFromDate().compareTo(limitSale.getLsalToDate()) > 0) {
                AlertUtils.showAlert("S???a th???t b???i! vui l??ng ch???n ng??y h???p l???!!!", Alert.AlertType.ERROR);
                return;
            }
        } catch (Exception e) {
            AlertUtils.showAlert("S???a th???t b???i!", Alert.AlertType.ERROR);
            return;
        }
        try {
            if(LIMIT_SALE_SERVICE.updateLimitSale(limitSale)){
                AlertUtils.showAlert("S???a th??nh c??ng", Alert.AlertType.INFORMATION);
                reloadData();
            }
            else {
                AlertUtils.showAlert("s???a th???t b???i!", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // x??a limit sale
    private void deleteLimitSale(){
        LimitSale limitSale = new LimitSale();
        int proId = 0;
        if(cbProductLimitSaleId.getValue() != null && !cbProductLimitSaleId.getValue().toString().isEmpty())
            proId = parseInt(cbProductLimitSaleId.getValue().toString());
        if(Objects.equals(txtSaleIsActive.getText(), "Ng??ng ho???t ?????ng")){
            AlertUtils.showAlert("X??a th???t b???i! m?? ???? ng??ng ho???t ?????ng!!!", Alert.AlertType.ERROR);
            return;
        }
        try {
            limitSale.setSaleId(parseInt(cbSaleId.getValue().toString()));
            if(!LIMIT_SALE_SERVICE.isExitsLimitSale(limitSale.getSaleId())){
                AlertUtils.showAlert("X??a th???t b???i! m?? kh??ng t???n t???i!!!", Alert.AlertType.ERROR);
                return;
            }
        } catch (Exception e) {
            AlertUtils.showAlert("X??a th???t b???i!", Alert.AlertType.ERROR);
            return;
        }
        try {
            if(LIMIT_SALE_SERVICE.deleteLimitSale(limitSale, proId)){
                AlertUtils.showAlert("X??a th??nh c??ng", Alert.AlertType.INFORMATION);
                reloadData();
            }
            else {
                AlertUtils.showAlert("X??a th???t b???i!", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // tr??? v??? homepage
    private void back(){
        try {
            App.setRoot("homepage-admin");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
