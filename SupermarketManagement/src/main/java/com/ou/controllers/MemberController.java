package com.ou.controllers;

import com.ou.pojos.Member;
import com.ou.pojos.MemberType;
import com.ou.services.MemberService;
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
import java.util.Objects;
import java.util.ResourceBundle;

public class MemberController implements Initializable {

    private static final MemberService MEMBER_SERVICE;
    private static final StringConverter<LocalDate> STRING_CONVERTER;
    static {
        MEMBER_SERVICE = new MemberService();
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
    TextField txtMemId;
    @FXML
    TextField txtMemFirstName;
    @FXML
    TextField txtMemLastName;
    @FXML
    ComboBox cbMemSex;
    @FXML
    TextField txtMemCardId;
    @FXML
    TextField txtMemPhoneNumber;
    @FXML
    TextField txtMemTotalPurchase;
    @FXML
    TextField txtSearchMemName;
    @FXML
    TextField txtMemType;
    @FXML
    TextField txtMemIsActive;
    @FXML
    DatePicker dpMemDoB;
    @FXML
    DatePicker dpMemJoinedDate;
    @FXML
    Button btnAdd;
    @FXML
    Button btnEdit;
    @FXML
    Button btnDelete;
    @FXML
    Button btnBack;
    @FXML
    TableView tbvMember;
    @FXML
    Text textMemAmount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ((Stage)App.window).setTitle("Qu???n l?? th??nh vi??n - OU Market");
        this.initInputData();
        this.initComboBoxSexData();
        this.initMemberTbv();
        this.loadMemberTbvColumns();
        this.loadMemberTbvData(txtSearchMemName.getText());
        this.loadMemberAmount();
        this.tbvMember.getSelectionModel().getSelectedItems()
                .addListener((ListChangeListener<? super Member>) e -> changeInputData());
        this.btnAdd.setOnMouseClicked(e -> addMember());
        this.btnEdit.setOnMouseClicked(e -> updateMember());
        this.btnDelete.setOnMouseClicked(e -> deleteMember());
        this.btnBack.setOnMouseClicked(e->backToMenu());
        this.txtSearchMemName.textProperty().addListener(e -> loadMemberTbvData(this.txtSearchMemName.getText().trim()));
    }

    // KH???i t???o c??c thu???c t??nh c???a v??ng input
    private void initInputData() {
        this.txtMemIsActive.setDisable(true);
        this.txtMemId.setDisable(true);
        this.txtMemType.setDisable(true);
        this.txtMemTotalPurchase.setDisable(true);
        this.dpMemJoinedDate.setDisable(true);
        this.dpMemJoinedDate.setConverter(STRING_CONVERTER);
        this.dpMemDoB.setConverter(STRING_CONVERTER);
    }

    //kh???i t???o gi?? tr??? combobox gi???i t??nh
    private void initComboBoxSexData(){
        cbMemSex.getItems().add("Nam");
        cbMemSex.getItems().add("N???");
        cbMemSex.getSelectionModel().selectFirst();
    }

    // L???y d??? li???u cho table view
    private void loadMemberTbvData(String kw) {
        try{
            this.tbvMember.setItems(FXCollections.observableList(MEMBER_SERVICE.getMembers(kw)));
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kh???i t???o c??c thu???c t??nh c???a table view th??nh vi??n
    private void initMemberTbv() {
        this.tbvMember.setPlaceholder(new Label("Kh??ng c?? th??nh vi??n n??o!"));
        this.tbvMember.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.tbvMember.setEditable(false);
    }

    // reset d??? li???u v??ng input
    private void clearInputData() {
        this.txtMemId.setText("");
        this.txtMemType.setText("");
        this.txtMemIsActive.setText("");
        this.txtMemFirstName.setText("");
        this.txtMemLastName.setText("");
        this.txtMemTotalPurchase.setText("0");
        this.txtMemPhoneNumber.setText("");
        this.txtMemCardId.setText("");
        this.cbMemSex.getSelectionModel().selectFirst();
        this.dpMemDoB.setValue(LocalDate.now());
        this.dpMemJoinedDate.setValue(LocalDate.now());
    }

    // Thi???t l???p v??ng d??? li???u input khi l???a ch???n thay ?????i d?????i table view
    private void changeInputData() {
        Member selectedMem = (Member) this.tbvMember.getSelectionModel().getSelectedItem();
        if (selectedMem != null) {
            this.txtMemId.setText(String.valueOf(selectedMem.getPersId()));
            this.txtMemLastName.setText(selectedMem.getPersLastName());
            this.txtMemFirstName.setText(selectedMem.getPersFirstName());
            this.txtMemType.setText(selectedMem.getMemberType().getMemtName() == null ? "Ch??a ????? ??i???m t??ch l??y" :
                    selectedMem.getMemberType().getMemtName());
            this.txtMemTotalPurchase.setText(String.valueOf(selectedMem.getMemTotalPurchase()));
            this.txtMemCardId.setText(selectedMem.getPersIdCard());
            this.txtMemPhoneNumber.setText(selectedMem.getPersPhoneNumber());
            this.cbMemSex.setValue(selectedMem.getPersSex() == 1 ? "Nam" : "N???");
            this.dpMemDoB.setValue(LocalDate.parse(selectedMem.getPersDateOfBirth().toString()));
            this.dpMemJoinedDate.setValue(LocalDate.parse(selectedMem.getPersJoinedDate().toString()));
            this.txtMemIsActive.setText(selectedMem.getPersIsActive() ? "??ang ho???t ?????ng" : "Ng??ng ho???t ?????ng");

        }
    }

    // T???o c??c c???t v?? match d??? li???u cho table view
    private void loadMemberTbvColumns() {
        TableColumn<Member, Integer> memIdColumn = new TableColumn<>("M??");
        TableColumn<Member, String> memLastNameColumn = new TableColumn<>("H??? v?? ?????m");
        TableColumn<Member, String> memFirstNameColumn = new TableColumn<>("T??n");
        TableColumn<Member, String> memSexColumn = new TableColumn<>("Gi???i t??nh");
        TableColumn<Member, String> memIsActiveColumn = new TableColumn<>("Tr???ng th??i");
        TableColumn<Member, String> memCardIdColumn = new TableColumn<>("S??? t??i kho???n");
        TableColumn<Member, String> memPhoneNumberColumn = new TableColumn<>("S??? ??i???n tho???i");
        TableColumn<Member, String> memDoBColumn = new TableColumn<>("Ng??y sinh");
        TableColumn<Member, String> memJoinedDateColumn = new TableColumn<>("Ng??y tham gia");
        TableColumn<Member, String> memTotalPurchaseColumn = new TableColumn<>("T???ng ti???n ???? mua");
        TableColumn<Member, MemberType> memTypeColumn = new TableColumn<>("Lo???i th??nh vi??n");
        memIdColumn.setCellValueFactory(new PropertyValueFactory<>("persId"));
        memFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("persFirstName"));
        memLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("persLastName"));
        memSexColumn.setCellValueFactory(new PropertyValueFactory<>("persSex"));
        memCardIdColumn.setCellValueFactory(new PropertyValueFactory<>("persIdCard"));
        memPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("persPhoneNumber"));
        memJoinedDateColumn.setCellValueFactory(new PropertyValueFactory<>("persJoinedDate"));
        memDoBColumn.setCellValueFactory(new PropertyValueFactory<>("persDateOfBirth"));
        memIsActiveColumn.setCellValueFactory(new PropertyValueFactory<>("persIsActive"));
        memTypeColumn.setCellValueFactory(new PropertyValueFactory<>("memberType"));
        memTotalPurchaseColumn.setCellValueFactory(new PropertyValueFactory<>("memTotalPurchase"));
        memIdColumn.setPrefWidth(50);
        memFirstNameColumn.setPrefWidth(100);
        memLastNameColumn.setPrefWidth(150);
        memSexColumn.setPrefWidth(100);
        memCardIdColumn.setPrefWidth(150);
        memPhoneNumberColumn.setPrefWidth(150);
        memJoinedDateColumn.setPrefWidth(150);
        memDoBColumn.setPrefWidth(150);
        memIsActiveColumn.setPrefWidth(100);
        memTypeColumn.setPrefWidth(250);
        memTotalPurchaseColumn.setPrefWidth(150);

        memIdColumn.setSortType(TableColumn.SortType.DESCENDING);
        this.tbvMember.getColumns().addAll(memIdColumn, memLastNameColumn, memFirstNameColumn, memSexColumn,
                memIsActiveColumn, memCardIdColumn, memPhoneNumberColumn, memDoBColumn, memJoinedDateColumn,
                memTotalPurchaseColumn, memTypeColumn);
    }

    // load data
    private void reloadData() throws SQLException {
        loadMemberTbvData(this.txtSearchMemName.getText());
        loadMemberAmount();
        clearInputData();
    }

    // L???y s??? l?????ng th??nh vi??n
    private void loadMemberAmount() {
        try {
            this.textMemAmount.setText(String.valueOf(MEMBER_SERVICE.getMemberAmount()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // th??m m???i 1 th??nh vi??n
    private void addMember() {
        Member member = new Member();
        if(txtMemFirstName.getText().length() > 20){
            AlertUtils.showAlert("Th??m th???t b???i! T??n ch??? ???????c ph??p 20 k?? t???", Alert.AlertType.ERROR);
            return;
        }
        if(txtMemLastName.getText().length() > 50){
            AlertUtils.showAlert("Th??m th???t b???i! H??? v?? ?????m ch??? ???????c ph??p 50 k?? t???", Alert.AlertType.ERROR);
            return;
        }
        setInfo(member);
        if(dpMemDoB.getValue() != null)
            member.setPersDateOfBirth(Date.valueOf(dpMemDoB.getValue()));
        try {
            if (MEMBER_SERVICE.addMember(member)) {
                AlertUtils.showAlert("Th??m th??nh c??ng", Alert.AlertType.INFORMATION);
                reloadData();
            } else {
                AlertUtils.showAlert("Th??m th???t b???i!", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // s???a th??ng tin th??nh vi??n
    private void updateMember(){
        Member member = new Member();
        if(txtMemFirstName.getText().length() > 20){
            AlertUtils.showAlert("S???a th???t b???i! T??n ch??? ???????c ph??p 20 k?? t???", Alert.AlertType.ERROR);
            return;
        }
        if(txtMemLastName.getText().length() > 50){
            AlertUtils.showAlert("S???a th???t b???i! H??? v?? ?????m ch??? ???????c ph??p 50 k?? t???", Alert.AlertType.ERROR);
            return;
        }
        if(txtMemId.getText() != null && !txtMemId.getText().isEmpty())
            member.setPersId(Integer.parseInt(txtMemId.getText()));
        setInfo(member);
        member.setPersIsActive(Objects.equals(txtMemIsActive.getText(), "??ang ho???t ?????ng"));
        if(dpMemDoB.getValue() != null)
            member.setPersDateOfBirth(Date.valueOf(dpMemDoB.getValue()));
        try {
            if(Objects.equals(txtMemIsActive.getText(), "Ng??ng ho???t ?????ng"))
                AlertUtils.showAlert("C???p nh???t th???t b???i! v?? th??nh vi??n ???? ng??ng ho???t ?????ng!!!", Alert.AlertType.ERROR);
            else
                if (MEMBER_SERVICE.updateMember(member)) {
                    AlertUtils.showAlert("C???p nh???t th??nh c??ng", Alert.AlertType.INFORMATION);
                    reloadData();
                } else {
                    AlertUtils.showAlert("C???p nh???t th???t b???i!", Alert.AlertType.ERROR);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //xoa 1 thanh vien
    private void deleteMember() {
        Member member = new Member();
        if(Objects.equals(txtMemIsActive.getText(), "Ng??ng ho???t ?????ng")){
            AlertUtils.showAlert("X??a th???t b???i! Th??nh vi??n ???? ng??ng ho???t ?????ng!", Alert.AlertType.ERROR);
            return;
        }
        try {
            member.setPersId(Integer.parseInt(this.txtMemId.getText()));
        } catch (NumberFormatException inputMismatchException) {
            AlertUtils.showAlert("X??a th???t b???i! Vui l??ng ch???n th??nh vi??n c???n x??a!", Alert.AlertType.ERROR);
            return;
        }
        try {
            if (MEMBER_SERVICE.deleteMember(member)) {
                AlertUtils.showAlert("Xo?? th??nh c??ng", Alert.AlertType.INFORMATION);
                reloadData();
            } else {
                AlertUtils.showAlert("X??a th???t b???i!", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void backToMenu() {
        try {
            App.setRoot("homepage-admin");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setInfo(Member member){
        member.setPersFirstName(txtMemFirstName.getText().trim());
        member.setPersLastName(txtMemLastName.getText().trim());
        String phoneNumber = txtMemPhoneNumber.getText().trim();
        String idCard = txtMemCardId.getText().trim();
        if(phoneNumber.matches("\\d+") && idCard.matches("\\d+") &&
                phoneNumber.length() == 10 && idCard.length() <= 12 && idCard.length() >= 9) {
            member.setPersPhoneNumber(phoneNumber);
            member.setPersIdCard(idCard);
        }
        member.setPersSex((byte) (Objects.equals(cbMemSex.getValue().toString(), "Nam") ? 1 : 0));
        member.setPersIsActive(!Objects.equals(txtMemIsActive.getText(), "Ng??ng ho???t ?????ng"));
    }
}
