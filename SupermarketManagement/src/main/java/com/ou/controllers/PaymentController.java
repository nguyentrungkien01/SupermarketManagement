package com.ou.controllers;

import com.ou.pojos.*;
import com.ou.services.MemberService;
import com.ou.services.PaymentService;
import com.ou.services.ProductService;
import com.ou.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class PaymentController implements Initializable {
    private final static PaymentService PAYMENT_SERVICE;
    private final static ProductService PRODUCT_SERVICE;
    private final static MemberService MEMBER_SERVICE;
    private final static DateTimeFormatter DTF;


    static {
        PAYMENT_SERVICE = new PaymentService();
        PRODUCT_SERVICE = new ProductService();
        MEMBER_SERVICE = new MemberService();
        DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    @FXML
    private HBox hbxBillProducts;

    @FXML
    private Button btnProSearch;

    @FXML
    private TextField txtProId;

    @FXML
    private TextField txtProName;

    @FXML
    private TextField txtProductId;

    @FXML
    private TextField txtProCat;

    @FXML
    private TextField txtProMan;

    @FXML
    private TextField txtProAmo;

    @FXML
    private HBox hbxProUnits;

    @FXML
    private TextField txtStaName;

    @FXML
    private TextField txtMemberId;

    @FXML
    private TextField txtMemName;

    @FXML
    private TextField txtMemType;

    @FXML
    private TextField txtSalePercent;

    @FXML
    private Button btnMemChoice;

    @FXML
    private AnchorPane acpMemInfo;

    @FXML
    private TextField txtMemId;

    @FXML
    private Button btnMemConfirm;

    @FXML
    private TextField txtBillTempTotalMoney;

    @FXML
    private TextField txtBillTotalSaleMoney;

    @FXML
    private TextField txtBillActualTotalMoney;

    @FXML
    private TextField txtBillCustomerMoney;

    @FXML
    private Button btnCalExcessCash;

    @FXML
    private TextField txtExcessCash;

    @FXML
    private TextField txtBillCreatedDate;

    @FXML
    private TextField txtBillBranchAddress;

    @FXML
    private Button btnBillCreation;

    @FXML
    private Button btnBillPayment;

    @FXML
    private Button btnBillProductAddition;

    @FXML
    private Button btnBillProductCancel;

    @FXML
    private Button btnSignOut;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ((Stage)App.window).setTitle("Thanh to??n - OU Market");
        initInputData();
        this.btnProSearch.setOnMouseClicked(evt -> searchProductData());
        this.btnMemChoice.setOnMouseClicked(evt -> this.acpMemInfo.setVisible(!this.acpMemInfo.isVisible()));
        this.btnMemConfirm.setOnMouseClicked(evt -> confirmMember());
        this.btnCalExcessCash.setOnMouseClicked(evt -> calExcessCash());
        this.btnBillCreation.setOnMouseClicked(evt -> createNewBill());
        this.btnBillPayment.setOnMouseClicked(evt -> pay());
        this.btnBillProductAddition.setOnMouseClicked(evt -> addProduct());
        this.btnBillProductCancel.setOnMouseClicked(evt -> cancelProduct());
        this.btnSignOut.setOnMouseClicked(evt -> signOut());
    }

    // Kh???i t???o thu???c t??nh c??c ?? input
    private void initInputData() {
        this.txtProName.setDisable(true);
        this.txtProductId.setVisible(false);
        this.txtProCat.setDisable(true);
        this.txtProMan.setDisable(true);
        this.txtStaName.setDisable(true);
        this.txtMemName.setDisable(true);
        this.txtMemberId.setVisible(false);
        this.txtMemType.setDisable(true);
        this.acpMemInfo.setVisible(false);
        this.txtSalePercent.setDisable(true);
        this.txtBillTempTotalMoney.setDisable(true);
        this.txtBillTotalSaleMoney.setDisable(true);
        this.txtBillActualTotalMoney.setDisable(true);
        this.txtExcessCash.setDisable(true);
        this.txtBillCreatedDate.setDisable(true);
        this.txtBillBranchAddress.setDisable(true);
        this.txtBillTempTotalMoney.setText("0");
        this.txtBillTotalSaleMoney.setText("0");
        this.txtBillActualTotalMoney.setText("0");
        this.txtBillCustomerMoney.setText("0");
        this.txtExcessCash.setText("");
        this.txtStaName.setText(String.format("%s %s", App.currentStaff.getPersLastName(),
                App.currentStaff.getPersFirstName()));
        this.txtBillCreatedDate.setText(DTF.format(LocalDateTime.now()));
        this.txtBillBranchAddress.setText(App.currentStaff.getBranch().getBraName());
        this.btnBillPayment.setDisable(true);
    }

    // N???p d??? li???u s???n ph???m
    private void loadProductInfo(Product product, List<ProductUnit> productUnits) {
        this.txtProductId.setText(product.getProId().toString());
        this.txtProName.setText(product.getProName());
        this.txtProCat.setText(product.getCategory().getCatName());
        this.txtProMan.setText(product.getManufacturer().getManName());
        this.txtProAmo.setText("1");
        VBox vbxUniAction = (VBox) this.hbxProUnits.getChildren().get(0);
        VBox vbxUniName = (VBox) this.hbxProUnits.getChildren().get(1);
        VBox vbxProPrice = (VBox) this.hbxProUnits.getChildren().get(2);
        VBox vbxProUniId = (VBox) this.hbxProUnits.getChildren().get(3);
        this.hbxProUnits.getChildren().forEach(col -> {
            VBox vbxCol = (VBox) col;
            int amount = vbxCol.getChildren().size();
            if (amount > 1)
                vbxCol.getChildren().subList(1, amount).clear();
        });
        productUnits.forEach(pu -> {
            AnchorPane acpUniAction = new AnchorPane();
            acpUniAction.setPrefHeight(57.0);
            acpUniAction.setPrefWidth(68.0);
            RadioButton rdbChoice = new RadioButton();
            rdbChoice.setLayoutX(30);
            rdbChoice.setLayoutY(10);
            Separator sptUniAction = new Separator();
            sptUniAction.setPrefHeight(6.0);
            sptUniAction.setPrefWidth(68.0);
            sptUniAction.setLayoutY(30.0);
            acpUniAction.getChildren().add(rdbChoice);
            acpUniAction.getChildren().add(sptUniAction);

            AnchorPane acpUniName = new AnchorPane();
            acpUniName.setPrefHeight(57);
            acpUniName.setPrefWidth(212);
            Text textUniName = new Text();
            Separator sptUniName = new Separator();
            textUniName.setLayoutY(20.0);
            sptUniName.setPrefHeight(6.0);
            sptUniName.setPrefWidth(212.0);
            sptUniName.setLayoutY(30.0);
            textUniName.setText(String.valueOf(pu.getUnit().getUniName()));
            textUniName.setLayoutX((acpUniName.getPrefWidth() -
                    textUniName.getLayoutBounds().getWidth()) / 2);
            acpUniName.getChildren().add(textUniName);
            acpUniName.getChildren().add(sptUniName);

            AnchorPane acpProPrice = new AnchorPane();
            acpProPrice.setPrefHeight(57);
            acpProPrice.setPrefWidth(188);
            Text textProPrice = new Text();
            Separator sptProPrice = new Separator();
            textProPrice.setLayoutY(20.0);
            sptProPrice.setPrefHeight(6.0);
            sptProPrice.setPrefWidth(188.0);
            sptProPrice.setLayoutY(30.0);
            textProPrice.setText(pu.getProPrice() + " VN??");
            textProPrice.setLayoutX((acpProPrice.getPrefWidth() -
                    textProPrice.getLayoutBounds().getWidth()) / 2);
            acpProPrice.getChildren().add(textProPrice);
            acpProPrice.getChildren().add(sptProPrice);

            AnchorPane acpProUnitId = new AnchorPane();
            acpProUnitId.setPrefHeight(57);
            acpProUnitId.setPrefWidth(100);
            Text textProUnitId = new Text();
            Separator sptProUnitId= new Separator();
            textProUnitId.setLayoutY(20.0);
            sptProUnitId.setPrefHeight(6.0);
            sptProUnitId.setPrefWidth(100.0);
            sptProUnitId.setLayoutY(30.0);
            textProUnitId.setText(String.valueOf(pu.getPruId()));
            textProUnitId.setLayoutX((acpProUnitId.getPrefWidth() -
                    textProUnitId.getLayoutBounds().getWidth()) / 2);
            acpProUnitId.getChildren().add(textProUnitId);
            acpProUnitId.getChildren().add(sptProUnitId);

            vbxUniAction.getChildren().add(acpUniAction);
            vbxUniName.getChildren().add(acpUniName);
            vbxProPrice.getChildren().add(acpProPrice);
            vbxProUniId.getChildren().add(acpProUnitId);

            rdbChoice.setOnMouseClicked(evt -> {
                if (vbxUniAction.getChildren().size() > 1)
                    vbxUniAction.getChildren().subList(1, vbxUniAction.getChildren().size())
                            .forEach(acp -> {
                                AnchorPane anchorPane = (AnchorPane) acp;
                                ((RadioButton) anchorPane.getChildren().get(0)).setSelected(false);
                            });
                rdbChoice.setSelected(true);
            });

        });
        if (vbxUniAction.getChildren().size() > 1) {
            AnchorPane anchorPane = (AnchorPane) vbxUniAction.getChildren().get(1);
            ((RadioButton) anchorPane.getChildren().get(0)).setSelected(true);
        }
    }

    // T??m ki???m th??ng tin s???n ph???m d???a v??o id
    private void searchProductData() {
        try {
            int proId = Integer.parseInt(this.txtProId.getText());
            Product product = PRODUCT_SERVICE.getProductById(proId);
            List<ProductUnit> productUnits = PRODUCT_SERVICE.getProductUnits(proId);
            if (product == null) {
                AlertUtils.showAlert("Kh??ng t??m th???y s???n ph???m!", Alert.AlertType.ERROR);
                return;
            }
            loadProductInfo(product, productUnits);
        } catch (NumberFormatException numberFormatException) {
            AlertUtils.showAlert("Kh??ng t??m th???y s???n ph???m!", Alert.AlertType.ERROR);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }

    // X??c nh???n th??nh vi??n
    private void confirmMember() {
        try {
            int memId = Integer.parseInt(this.txtMemId.getText());
            Member member = MEMBER_SERVICE.getMemberById(memId);
            if (member == null) {
                AlertUtils.showAlert("Kh??ng t??m th???y th??nh vi??n!", Alert.AlertType.ERROR);
                return;
            }
            SalePercent salePercent = MEMBER_SERVICE.getSalePercentOfMember(member.getPersId());
            this.txtMemName.setText(String.format("%s %s", member.getPersLastName(), member.getPersFirstName()));
            this.txtMemberId.setText(String.valueOf(member.getPersId()));
            this.txtMemType.setText(member.getMemberType().getMemtName());
            this.txtSalePercent.setText(String.format("-%s", salePercent.toString()));
            this.acpMemInfo.setVisible(false);
        } catch (NumberFormatException numberFormatException) {
            AlertUtils.showAlert("Kh??ng t??m th???y th??nh vi??n!", Alert.AlertType.ERROR);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    // T??nh ti???n th???a
    private void calExcessCash() {
        try {
            double bActualTotalMoney = Double.parseDouble(this.txtBillActualTotalMoney.getText());
            double bCustomerMoney = Double.parseDouble(this.txtBillCustomerMoney.getText());
            if (bActualTotalMoney == 0 || bCustomerMoney == 0 || bCustomerMoney < bActualTotalMoney ||
                    this.txtBillCustomerMoney.getText().contains("d") ||
                    this.txtBillCustomerMoney.getText().contains("f")) {
                AlertUtils.showAlert("S??? ti???n c???n t??nh to??n kh??ng ph?? h???p!", Alert.AlertType.ERROR);
                return;
            }
            this.txtExcessCash.setText(String.valueOf(bCustomerMoney - bActualTotalMoney));
            this.txtExcessCash.setDisable(true);
            this.txtBillCustomerMoney.setDisable(true);
            this.btnBillPayment.setDisable(false);
            this.btnBillProductAddition.setDisable(true);
            this.btnBillProductCancel.setDisable(true);
            this.btnCalExcessCash.setDisable(true);
        } catch (NumberFormatException numberFormatException) {
            AlertUtils.showAlert("S??? ti???n c???n t??nh to??n kh??ng ph?? h???p!", Alert.AlertType.ERROR);
        }

    }

    // T???o h??a ????n m???i
    private void createNewBill() {
        this.txtProId.setText("");
        this.txtProductId.setText("");
        this.txtProName.setText("");
        this.txtProCat.setText("");
        this.txtProMan.setText("");
        this.txtProAmo.setText("");
        this.txtProductId.setText("");
        this.txtMemName.setText("");
        this.txtMemberId.setText("");
        this.txtMemType.setText("");
        this.txtMemId.setText("");
        this.txtSalePercent.setText("");
        this.txtBillTempTotalMoney.setText("0");
        this.txtBillTotalSaleMoney.setText("0");
        this.txtBillActualTotalMoney.setText("0");
        this.txtBillCustomerMoney.setText("0");
        this.txtBillCreatedDate.setText(DTF.format(LocalDateTime.now()));
        this.txtExcessCash.setText("");
        this.txtExcessCash.setDisable(false);
        this.acpMemInfo.setVisible(false);
        this.hbxBillProducts.getChildren().forEach(col -> {
            VBox vbxCol = (VBox) col;
            int rowAmount = vbxCol.getChildren().size();
            if (rowAmount > 1)
                vbxCol.getChildren().subList(1, rowAmount).clear();
        });
        this.hbxProUnits.getChildren().forEach(col -> {
            VBox vbxCol = (VBox) col;
            int rowAmount = vbxCol.getChildren().size();
            if (rowAmount > 1)
                vbxCol.getChildren().subList(1, rowAmount).clear();
        });
        this.txtBillCustomerMoney.setDisable(false);
        this.btnBillPayment.setDisable(true);
        this.btnBillProductAddition.setDisable(false);
        this.btnBillProductCancel.setDisable(false);
        this.btnCalExcessCash.setDisable(false);
    }

    // L???y danh s??ch s???n ph???m ???? th??m v??o h??a ????n
    private List<ProductBill> getProductsOfBill() {
        List<ProductBill> productBills = new ArrayList<>();
        VBox vbxProId = (VBox) this.hbxBillProducts.getChildren().get(1);
        VBox vbxProAmo = (VBox) this.hbxBillProducts.getChildren().get(3);
        VBox vbxProUnit = (VBox) this.hbxBillProducts.getChildren().get(4);
        int amount = vbxProId.getChildren().size();
        if (amount > 1)

            vbxProId.getChildren().subList(1, amount).forEach(row -> {
                AnchorPane acpProIdRow = (AnchorPane) row;
                int idx = vbxProId.getChildren().indexOf(acpProIdRow);

                AnchorPane acpProAmo = (AnchorPane) vbxProAmo.getChildren().get(idx);
                Text txtProAmo = (Text) acpProAmo.getChildren().get(0);

                AnchorPane acpProUnit = (AnchorPane) vbxProUnit.getChildren().get(idx);
                Text txtProUnit = (Text) acpProUnit.getChildren().get(0);

                ProductBill productBill = new ProductBill();
                ProductUnit productUnit = new ProductUnit();
                int pruId = Integer.parseInt(txtProUnit.getText().substring(0, txtProUnit.getText().indexOf("|")).trim());
                productUnit.setPruId(pruId);

                productBill.setProductUnit(productUnit);
                productBill.setProAmount(Integer.parseInt(txtProAmo.getText().trim()));
                productBills.add(productBill);

            });

        return productBills;
    }

    // Thanh to??n
    private void pay() {
        if (this.txtBillCustomerMoney.getText() == null ||
                this.txtBillCustomerMoney.getText().isEmpty()) {
            AlertUtils.showAlert("Ch??a nh???p ti???n kh??ch h??ng thanh to??n",
                    Alert.AlertType.ERROR);
            return;
        }
        try {

            List<ProductBill> productBills = getProductsOfBill();
            if (productBills.size() == 0) {
                AlertUtils.showAlert("????n h??ng ch??a c?? s???n ph???m n??o! Vui l??ng ki???m tra l???i",
                        Alert.AlertType.ERROR);
                return;
            }
            Bill bill = new Bill();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse(this.txtBillCreatedDate.getText().trim());
            bill.setBillCreatedDate(new Timestamp(date.getTime()));
            bill.setBillCustomerMoney(BigDecimal.valueOf(
                    Double.parseDouble(this.txtBillCustomerMoney.getText().trim())));
            bill.setBillTotalMoney(BigDecimal.valueOf(
                    Double.parseDouble(this.txtBillActualTotalMoney.getText().trim())));
            bill.setBillTotalSaleMoney(BigDecimal.valueOf(
                    Double.parseDouble(this.txtBillTotalSaleMoney.getText().trim())));
            bill.setProductBills(productBills);
            bill.setStaff(App.currentStaff);
            Member member = null;
            if (this.txtMemberId.getText() != null &&
                    !this.txtMemberId.getText().isEmpty()) {
                member = new Member();
                member.setPersId(Integer.parseInt(this.txtMemberId.getText().trim()));
                SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
                String memDateOfBirth = sdformat.format(MEMBER_SERVICE.getMemberById(
                        member.getPersId()).getPersDateOfBirth());
                String billCreatedDate = sdformat.format(sdformat.parse(this.txtBillCreatedDate.getText().trim()));

                //gi???m gi?? mua > 1tr v?? ngay ng??y sinh c???a kh??c
                if(bill.getBillTotalMoney().subtract(new BigDecimal(1000)).intValue()>0 &&
                    memDateOfBirth.equals(billCreatedDate)){
                    int salePercent = 7;
                    BigDecimal saleMoney = bill.getBillTotalMoney().divide(new BigDecimal(1000000))
                            .multiply(new BigDecimal(salePercent));
                    bill.setBillTotalSaleMoney(bill.getBillTotalSaleMoney().add(saleMoney));
                    bill.setBillTotalMoney(bill.getBillTotalMoney().subtract(saleMoney));
                }
            }
            bill.setMember(member);
            // Gi???m gi?? th??nh vi??n
            if (this.txtSalePercent.getText()!=null && this.txtSalePercent.getText().length()>0){
                int salePercent = Integer.parseInt(this.txtSalePercent.getText().substring(1,
                        this.txtSalePercent.getText().length()-1));
                BigDecimal saleMoney = bill.getBillTotalMoney().divide(new BigDecimal(100))
                        .multiply(new BigDecimal(salePercent));
                bill.setBillTotalSaleMoney(bill.getBillTotalSaleMoney().add(saleMoney));
                bill.setBillTotalMoney(bill.getBillTotalMoney().subtract(saleMoney));
            }

            AlertUtils.showAlert("Thanh to??n th??nh c??ng", Alert.AlertType.INFORMATION);
            PAYMENT_SERVICE.addBill(bill);

            createNewBill();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            AlertUtils.showAlert(
                    "???? x???y ra l???i trong qu?? tr??nh thanh to??n. Vui l??ng th??? l???i ",
                    Alert.AlertType.ERROR);
        }

    }

    // Ki???m tra th??ng tin s???n ph???m c?? h???p l??? hay kh??ng
    private boolean isValidProductInfo() {
        try {
            if (this.txtProName.getText() == null || this.txtProName.getText().length() == 0 ||
                    !this.txtProAmo.getText().matches("\\d+"))
                return false;
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    // L???y th??ng tin ????n v??? s???n ph???m d???a v??o l???a ch???n b???ng g??a
    private String getProUnit() {
        AtomicReference<String> proUnit = new AtomicReference<>("");
        VBox vbxChoice = (VBox) this.hbxProUnits.getChildren().get(0);
        VBox vbxProUnit = (VBox) this.hbxProUnits.getChildren().get(1);
        VBox vbxProUniId =  (VBox) this.hbxProUnits.getChildren().get(3);
        AtomicReference<Integer> idx = new AtomicReference<>(0);
        vbxChoice.getChildren().subList(1, vbxChoice.getChildren().size()).forEach(row -> {
            idx.getAndSet(idx.get() + 1);
            AnchorPane acpChoiceRow = (AnchorPane) row;
            RadioButton rdbChoice = (RadioButton) acpChoiceRow.getChildren().get(0);
            if (rdbChoice.isSelected()) {
                AnchorPane acpUniRow = (AnchorPane) vbxProUnit.getChildren().get(idx.get());
                Text txtProUnit = (Text) acpUniRow.getChildren().get(0);

                AnchorPane acpProUniIdRow = (AnchorPane) vbxProUniId.getChildren().get(idx.get());
                Text txtProUniId = (Text) acpProUniIdRow.getChildren().get(0);

                proUnit.set(String.format("%s | %s", txtProUniId.getText().trim(), txtProUnit.getText().trim()));
            }
        });
        return proUnit.get();
    }

    // L???y th??ng tin gi?? c???a s???n ph???m d???a v??o l???a ch???n b???ng gi??
    private String getProPrice() {
        AtomicReference<String> proPrice = new AtomicReference<>("0");
        VBox vbxChoice = (VBox) this.hbxProUnits.getChildren().get(0);
        VBox vbxPrice = (VBox) this.hbxProUnits.getChildren().get(2);
        AtomicReference<Integer> idx = new AtomicReference<>(0);
        vbxChoice.getChildren().subList(1, vbxChoice.getChildren().size()).forEach(row -> {
            idx.getAndSet(idx.get() + 1);
            AnchorPane acpChoiceRow = (AnchorPane) row;
            RadioButton rdbChoice = (RadioButton) acpChoiceRow.getChildren().get(0);
            if (rdbChoice.isSelected()) {
                AnchorPane acpPriceRow = (AnchorPane) vbxPrice.getChildren().get(idx.get());
                Text txtProPrice = (Text) acpPriceRow.getChildren().get(0);
                proPrice.set(txtProPrice.getText());
            }
        });
        return proPrice.get();
    }

    // L???y th??ng tin gi???m gi?? s???n ph???m
    private String getProSalePrice(int proId, String price) throws SQLException {
        if (price.contains("VN??"))
            price = price.substring(0, price.indexOf("VN??")).trim();
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        ProductLimitSale productLimitSale =
                PRODUCT_SERVICE.getProductLimitSaleOfProduct(proId, date);
        if (productLimitSale == null)
            return String.format("%s VN??", price);

        SalePercent salePercent = productLimitSale.getLimitSale()
                .getSale().getSalePercent();
        String salePrice = String.valueOf(Double.parseDouble(price) -
                (Double.parseDouble(price) * salePercent.getSperPercent()) / 100);

        return String.format("%s VN??", salePrice);
    }

    // C???ng d???n ti???n t???m t??nh
    private void increaseBillTempTotalMoney() {
        VBox vbxProAmo = (VBox) this.hbxBillProducts.getChildren().get(3);
        VBox vbxTotalMoney = (VBox) this.hbxBillProducts.getChildren().get(5);
        int rowAmount = vbxTotalMoney.getChildren().size();
        if (rowAmount > 1) {
            AnchorPane acpProPriceRow = (AnchorPane) vbxTotalMoney.getChildren().get(rowAmount - 1);
            Text proPrice = (Text) acpProPriceRow.getChildren().get(0);
            AnchorPane acpProAmo = (AnchorPane) vbxProAmo.getChildren().get(rowAmount - 1);
            Text proAmo = (Text) acpProAmo.getChildren().get(0);
            double proP = Double.parseDouble(proPrice.getText().substring(0,
                    proPrice.getText().indexOf("VN??")).trim());
            double btTotalMoney = Double.parseDouble(this.txtBillTempTotalMoney.getText().trim()) +
                    proP * Integer.parseInt(proAmo.getText().trim());
            this.txtBillTempTotalMoney.setText(String.valueOf(btTotalMoney));
        }
    }

    // C???ng d???n ti???n gi???m gi??
    private void increaseBillTotalSaleMoney() {
        VBox vbxProAmo = (VBox) this.hbxBillProducts.getChildren().get(3);
        VBox vBoxTotalMoney = (VBox) this.hbxBillProducts.getChildren().get(5);
        VBox vbxTotalSaleMoney = (VBox) this.hbxBillProducts.getChildren().get(6);
        int rowAmount = vbxTotalSaleMoney.getChildren().size();
        if (rowAmount > 1) {
            AnchorPane acpTotalMoneyRow = (AnchorPane) vBoxTotalMoney.getChildren().get(rowAmount - 1);
            Text proPrice = (Text) acpTotalMoneyRow.getChildren().get(0);
            double proP = Double.parseDouble(proPrice.getText().substring(0,
                    proPrice.getText().indexOf("VN??")).trim());
            AnchorPane acpTotalSaleMoneyRow = (AnchorPane) vbxTotalSaleMoney.getChildren().get(rowAmount - 1);
            Text proSalePrice = (Text) acpTotalSaleMoneyRow.getChildren().get(0);
            double proSP = Double.parseDouble(proSalePrice.getText().substring(0,
                    proSalePrice.getText().indexOf("VN??")).trim());
            AnchorPane acpProAmo = (AnchorPane) vbxProAmo.getChildren().get(rowAmount - 1);
            Text proAmo = (Text) acpProAmo.getChildren().get(0);
            double btTotalSaleMoney = Double.parseDouble(this.txtBillTotalSaleMoney.getText().trim()) +
                    (proP - proSP) * Integer.parseInt(proAmo.getText().trim());
            this.txtBillTotalSaleMoney.setText(String.valueOf(btTotalSaleMoney));
        }
    }


    // T??nh t???ng ti???n th???c s??? c???a h??a ????n
    private void calBillActualTotalMoney() {
        double bActualTotalMoney = Double.parseDouble(this.txtBillTempTotalMoney.getText()) -
                Double.parseDouble(this.txtBillTotalSaleMoney.getText());
        this.txtBillActualTotalMoney.setText(String.valueOf(bActualTotalMoney));
    }

    // Th??m s???n ph???m v??o h??a ????n
    private void addProductToBill() throws SQLException {
        if(Double.parseDouble(this.txtProAmo.getText().trim()) > 1000)
        {
            AlertUtils.showAlert("S??? l?????ng v?????t qu?? 1000. Vui l??ng nh???p l???i", Alert.AlertType.ERROR);
            return;
        }

        VBox vbxProAction = (VBox) this.hbxBillProducts.getChildren().get(0);
        VBox vbxProId = (VBox) this.hbxBillProducts.getChildren().get(1);
        VBox vbxProName = (VBox) this.hbxBillProducts.getChildren().get(2);
        VBox vbxProAmount = (VBox) this.hbxBillProducts.getChildren().get(3);
        VBox vbxProUnit = (VBox) this.hbxBillProducts.getChildren().get(4);
        VBox vbxProPrice = (VBox) this.hbxBillProducts.getChildren().get(5);
        VBox vbxProSalePrice = (VBox) this.hbxBillProducts.getChildren().get(6);
        VBox vbxProTotalPrice = (VBox) this.hbxBillProducts.getChildren().get(7);

        AnchorPane acpProAction = new AnchorPane();
        acpProAction.setPrefHeight(65);
        acpProAction.setPrefWidth(70);
        CheckBox ckbProAction = new CheckBox();
        ckbProAction.setLayoutX(30);
        ckbProAction.setLayoutY(20);
        Separator sptProAction = new Separator();
        sptProAction.setPrefHeight(6.0);
        sptProAction.setPrefWidth(70.0);
        sptProAction.setLayoutY(50.0);
        acpProAction.getChildren().add(ckbProAction);
        acpProAction.getChildren().add(sptProAction);

        AnchorPane acpProId = new AnchorPane();
        acpProId.setPrefHeight(65);
        acpProId.setPrefWidth(115);
        Text textProId = new Text();
        Separator sptProId = new Separator();
        textProId.setLayoutY(34.0);
        sptProId.setPrefHeight(6.0);
        sptProId.setPrefWidth(115.0);
        sptProId.setLayoutY(50.0);
        textProId.setText(this.txtProductId.getText());
        textProId.setLayoutX((acpProId.getPrefWidth() -
                textProId.getLayoutBounds().getWidth()) / 2);
        acpProId.getChildren().add(textProId);
        acpProId.getChildren().add(sptProId);

        AnchorPane acpProName = new AnchorPane();
        acpProName.setPrefHeight(65);
        acpProName.setPrefWidth(232);
        Text textProName = new Text();
        Separator sptProName = new Separator();
        textProName.setLayoutY(34.0);
        sptProName.setPrefHeight(6.0);
        sptProName.setPrefWidth(232.0);
        sptProName.setLayoutY(50.0);
        textProName.setText(this.txtProName.getText());
        textProName.setLayoutX((acpProName.getPrefWidth() -
                textProName.getLayoutBounds().getWidth()) / 2);
        acpProName.getChildren().add(textProName);
        acpProName.getChildren().add(sptProName);


        AnchorPane acpProAmount = new AnchorPane();
        acpProAmount.setPrefHeight(65);
        acpProAmount.setPrefWidth(138);
        Text textProAmount = new Text();
        Separator sptProAmount = new Separator();
        textProAmount.setLayoutY(34.0);
        sptProAmount.setPrefHeight(6.0);
        sptProAmount.setPrefWidth(138.0);
        sptProAmount.setLayoutY(50.0);
        textProAmount.setText(this.txtProAmo.getText());
        textProAmount.setLayoutX((acpProAmount.getPrefWidth() -
                textProAmount.getLayoutBounds().getWidth()) / 2);
        acpProAmount.getChildren().add(textProAmount);
        acpProAmount.getChildren().add(sptProAmount);

        AnchorPane acpProUnit = new AnchorPane();
        acpProUnit.setPrefHeight(65);
        acpProUnit.setPrefWidth(175);
        Text textProUnit = new Text();
        Separator sptProUnit = new Separator();
        textProUnit.setLayoutY(34.0);
        sptProUnit.setPrefHeight(6.0);
        sptProUnit.setPrefWidth(175.0);
        sptProUnit.setLayoutY(50.0);
        textProUnit.setText(getProUnit());
        textProUnit.setLayoutX((acpProUnit.getPrefWidth() -
                textProUnit.getLayoutBounds().getWidth()) / 2);
        acpProUnit.getChildren().add(textProUnit);
        acpProUnit.getChildren().add(sptProUnit);

        AnchorPane acpProPrice = new AnchorPane();
        acpProPrice.setPrefHeight(65);
        acpProPrice.setPrefWidth(192);
        Text textProPrice = new Text();
        Separator sptProPrice = new Separator();
        textProPrice.setLayoutY(34.0);
        sptProPrice.setPrefHeight(6.0);
        sptProPrice.setPrefWidth(192.0);
        sptProPrice.setLayoutY(50.0);
        textProPrice.setText(getProPrice());
        textProPrice.setLayoutX((acpProPrice.getPrefWidth() -
                textProPrice.getLayoutBounds().getWidth()) / 2);
        acpProPrice.getChildren().add(textProPrice);
        acpProPrice.getChildren().add(sptProPrice);

        AnchorPane acpProSalePrice = new AnchorPane();
        acpProSalePrice.setPrefHeight(65);
        acpProSalePrice.setPrefWidth(171);
        Text textProSalePrice = new Text();
        Separator sptProSalePrice = new Separator();
        textProSalePrice.setLayoutY(34.0);
        sptProSalePrice.setPrefHeight(6.0);
        sptProSalePrice.setPrefWidth(171.0);
        sptProSalePrice.setLayoutY(50.0);
        int proId = Integer.parseInt(this.txtProductId.getText());
        textProSalePrice.setText(getProSalePrice(proId, textProPrice.getText()));
        textProSalePrice.setLayoutX((acpProPrice.getPrefWidth() -
                textProSalePrice.getLayoutBounds().getWidth()) / 2);
        acpProSalePrice.getChildren().add(textProSalePrice);
        acpProSalePrice.getChildren().add(sptProSalePrice);

        AnchorPane acpProTotalPrice = new AnchorPane();
        acpProTotalPrice.setPrefHeight(65);
        acpProTotalPrice.setPrefWidth(175);
        Text textProTotalPrice = new Text();
        Separator sptProTotalPrice = new Separator();
        textProTotalPrice.setLayoutY(34.0);
        sptProTotalPrice.setPrefHeight(6.0);
        sptProTotalPrice.setPrefWidth(175.0);
        sptProTotalPrice.setLayoutY(50.0);
        String sp = textProSalePrice.getText()
                .substring(0, textProSalePrice.getText().indexOf("VN??")).trim();
        double totalPrice = Double.parseDouble(sp) *
                Integer.parseInt(textProAmount.getText().trim());
        textProTotalPrice.setText(String.format("%.1f VN??", totalPrice));
        textProTotalPrice.setLayoutX((acpProPrice.getPrefWidth() -
                textProTotalPrice.getLayoutBounds().getWidth()) / 2);
        acpProTotalPrice.getChildren().add(textProTotalPrice);
        acpProTotalPrice.getChildren().add(sptProTotalPrice);

        vbxProAction.getChildren().add(acpProAction);
        vbxProId.getChildren().add(acpProId);
        vbxProName.getChildren().add(acpProName);
        vbxProAmount.getChildren().add(acpProAmount);
        vbxProUnit.getChildren().add(acpProUnit);
        vbxProPrice.getChildren().add(acpProPrice);
        vbxProSalePrice.getChildren().add(acpProSalePrice);
        vbxProTotalPrice.getChildren().add(acpProTotalPrice);
    }

    // reset d??? li???u t??m ki???m s???n ph???m
    private void clearProductInput() {
        this.txtProId.setText("");
        this.txtProName.setText("");
        this.txtProductId.setText("");
        this.txtProCat.setText("");
        this.txtProMan.setText("");
        this.txtProAmo.setText("1");
        this.hbxProUnits.getChildren().forEach(col -> {
            VBox vbxCol = (VBox) col;
            int rowAmount = vbxCol.getChildren().size();
            if (rowAmount > 1)
                vbxCol.getChildren().subList(1, rowAmount).clear();
        });
    }

    // Th??m s???n ph???m v??o h??a ????n
    private void addProduct() {
        try {
            if (isValidProductInfo()) {
                addProductToBill();
                clearProductInput();
                increaseBillTempTotalMoney();
                increaseBillTotalSaleMoney();
                calBillActualTotalMoney();
            } else {
                AlertUtils.showAlert("Th??ng tin s???n ph???m kh??ng h???p l???!", Alert.AlertType.ERROR);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    // H???y s???n ph???m
    private void cancelProduct() {
        VBox vbxChoiceAction = (VBox) this.hbxBillProducts.getChildren().get(0);
        VBox vbxProId = (VBox) this.hbxBillProducts.getChildren().get(1);
        VBox vbxProName = (VBox) this.hbxBillProducts.getChildren().get(2);
        VBox vbxProAmount = (VBox) this.hbxBillProducts.getChildren().get(3);
        VBox vbxProUnit = (VBox) this.hbxBillProducts.getChildren().get(4);
        VBox vbxTotalMoney = (VBox) this.hbxBillProducts.getChildren().get(5);
        VBox vbxTotalSaleMoney = (VBox) this.hbxBillProducts.getChildren().get(6);
        VBox vbxTtMoney = (VBox) this.hbxBillProducts.getChildren().get(7);
        List<Integer> indices = new ArrayList<>();
        int rowAmount = vbxChoiceAction.getChildren().size();
        if (rowAmount > 1)
            vbxChoiceAction.getChildren().subList(1, rowAmount).forEach(row -> {
                AnchorPane acpChoiceRow = (AnchorPane) row;
                CheckBox checkBox = (CheckBox) acpChoiceRow.getChildren().get(0);
                if (checkBox.isSelected())
                    indices.add(vbxChoiceAction.getChildren().indexOf(acpChoiceRow));
            });
        if (indices.size() >= 1) {
            indices.sort((e1, e2) -> -e1.compareTo(e2));
            AtomicReference<Double> sumTotalMoney = new AtomicReference<>((double) 0);
            AtomicReference<Double> sumTotalSaleMoney = new AtomicReference<>((double) 0);
            indices.forEach(idx -> {
                // amount
                AnchorPane acpRowAmo = (AnchorPane) vbxProAmount.getChildren().get(idx);
                Text txtProAmo = (Text) acpRowAmo.getChildren().get(0);
                int amount = Integer.parseInt(txtProAmo.getText().trim());

                // total money
                AnchorPane acpRowTotalMoney = (AnchorPane) vbxTotalMoney.getChildren().get(idx);
                Text txtTotalMoney = (Text) acpRowTotalMoney.getChildren().get(0);
                double proP = Double.parseDouble(txtTotalMoney.getText().substring(0,
                        txtTotalMoney.getText().indexOf("VN??")).trim());
                sumTotalMoney.updateAndGet(v -> v + proP * amount);

                // total sale money
                AnchorPane acpRowTotalSaleMoney = (AnchorPane) vbxTotalSaleMoney.getChildren().get(idx);
                Text txtTotalSaleMoney = (Text) acpRowTotalSaleMoney.getChildren().get(0);
                double proSP = Double.parseDouble(txtTotalSaleMoney.getText().substring(0,
                        txtTotalSaleMoney.getText().indexOf("VN??")).trim());
                sumTotalSaleMoney.updateAndGet(v -> v + proSP * amount);

                vbxChoiceAction.getChildren().remove((int) idx);
                vbxProId.getChildren().remove((int) idx);
                vbxProName.getChildren().remove((int) idx);
                vbxProAmount.getChildren().remove((int) idx);
                vbxProUnit.getChildren().remove((int) idx);
                vbxTotalMoney.getChildren().remove((int) idx);
                vbxTotalSaleMoney.getChildren().remove((int) idx);
                vbxTtMoney.getChildren().remove((int) idx);

            });
            this.txtBillTempTotalMoney.setText(String.valueOf(
                    Double.parseDouble(this.txtBillTempTotalMoney.getText().trim()) - sumTotalMoney.get()));

            this.txtBillTotalSaleMoney.setText(String.valueOf(
                    Double.parseDouble(this.txtBillTotalSaleMoney.getText().trim()) -
                            (sumTotalMoney.get() - sumTotalSaleMoney.get())));
            calBillActualTotalMoney();
        } else
            AlertUtils.showAlert("Kh??ng c?? s???n ph???m n??o ???????c ch???n!", Alert.AlertType.ERROR);

    }

    // ????ng xu???t
    private void signOut() {
        App.currentStaff = null;
        try {
            App.setRoot("sign-in");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
