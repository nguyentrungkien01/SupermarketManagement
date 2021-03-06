package com.ou.controllers;

import com.ou.pojos.Staff;
import com.ou.services.StaffService;
import com.ou.utils.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class StaffController implements Initializable {
    private final static StaffService STAFF_SERVICE;
    private final static StringConverter<LocalDate> STRING_CONVERTER;
    static {
        STAFF_SERVICE = new StaffService();
        STRING_CONVERTER = new StringConverter<>() {
            private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null)
                    return "";
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
    private AnchorPane apId;

    @FXML
    private Label lblId;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtIdCard;

    @FXML
    private TextField txtPhoneNumber;

    @FXML
    private ComboBox cbSex;

    @FXML
    private DatePicker dpDateOfBirth;

    @FXML
    private TextField txtActive;

    @FXML
    private ComboBox cbBranch;

    @FXML
    private ComboBox cbRole;

    @FXML
    private TextField txtSearch;

    @FXML
    private TableView<Staff> tbvStaff;

    @FXML
    private TextField txtUsername;

    @FXML
    private CheckBox chbAdd;

    @FXML
    private CheckBox chbEdit;

    @FXML
    private CheckBox chbDelete;

    @FXML
    private PasswordField pwPassword;

    @FXML
    private PasswordField pwConfirmPw;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    @FXML
    private  Button btnBack;

    @FXML
    private CheckBox resetPw;

    @FXML
    private AnchorPane apPassword;

    private Integer id = 0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ((Stage)App.window).setTitle("Qu???n l?? nh??n vi??n - OU Market");
        this.loadStaffTbvColumns();
        this.setTbvStaff();
        this.loadTbvData();
        this.setCbRole();
        this.setCbSex();
        this.loadListBranch();
        this.tbvStaff.getSelectionModel().getSelectedItems()
                .addListener((ListChangeListener<? super Staff>) e -> setTextData());

        // g??n s??? ki???n cho c??c button th??m, s???a , x??a
        this.btnAdd.setOnMouseClicked(mouseEvent -> addStaff());
        this.btnEdit.setOnMouseClicked(mouseEvent -> updateStaff());
        this.btnDelete.setOnMouseClicked(mouseEvent -> deleteStaff());

        // g??n s??? ki???n cho n??t back
        this.btnBack.setOnMouseClicked(mouseEvent -> goToBack());

        this.apPassword.setDisable(true);

        // g??n s??? ki???n cho 3 ?? checkbox
        this.chbAdd.setOnMouseClicked(mouseEvent -> setCheckBoxAdd());
        this.chbEdit.setOnMouseClicked(mouseEvent -> setCheckBoxEdit());
        this.chbDelete.setOnMouseClicked(mouseEvent -> setCheckBoxDelete());
        this.txtSearch.textProperty().addListener(e -> loadTbvData());
        this.dpDateOfBirth.setConverter(STRING_CONVERTER);
        setEditableFalse();
    }

    private void goToBack(){
        try {
            App.setRoot("homepage-admin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // g??n gi?? tr??? cho combobox quy???n
    private  void setCbRole() {
        ObservableList<String> listRole = FXCollections.observableArrayList("Nh??n vi??n", "Qu???n tr??? vi??n");
        cbRole.setItems(listRole);
    }

    // g??n gi?? tr??? cho combobox quy???n
    private  void setCbSex() {
        ObservableList<String> listRole = FXCollections.observableArrayList("Nam", "N???");
        cbSex.setItems(listRole);
    }

    // load danh s??ch t??n nh??nh
    private void loadListBranch() {
        try {
            List<String> listBr =  STAFF_SERVICE.getListBranch();
            ObservableList<String> listBra = FXCollections.observableArrayList(listBr);
            cbBranch.setItems(listBra);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // c???m ch???nh s???a cac ?? input
    private void setEditableFalse() {
        this.txtLastName.setEditable(false);
        this.txtFirstName.setEditable(false);
        this.txtIdCard.setEditable(false);
        this.txtPhoneNumber.setEditable(false);
        this.dpDateOfBirth.setDisable(true);
        this.txtActive.setEditable(false);
        this.txtUsername.setEditable(false);
        this.cbSex.setDisable(true);
        this.cbBranch.setDisable(true);
        this.cbRole.setDisable(true);
        this.apPassword.setDisable(true);
        this.resetPw.setVisible(false);
    }
    // b???t setEditable = true cho c??c ?? input
    public void setEditableTrue() {
        this.txtLastName.setEditable(true);
        this.txtFirstName.setEditable(true);
        this.txtIdCard.setEditable(true);
        this.txtPhoneNumber.setEditable(true);
        this.dpDateOfBirth.setDisable(false);
        this.txtUsername.setEditable(true);
        this.cbSex.setDisable(false);
        this.cbBranch.setDisable(false);
        this.cbRole.setDisable(false);
    }

    // x??? l?? ?? checkbox add
    private void setCheckBoxAdd() {
        if (this.chbAdd.isSelected()) {
            // ???n n??t th??m v?? s???a
            this.chbEdit.setSelected(false);
            this.chbDelete.setSelected(false);
            this.btnEdit.setDisable(true);
            this.btnDelete.setDisable(true);
            this.apId.setVisible(false);
            this.txtActive.setDisable(true);
            this.btnAdd.setDisable(false);
            this.apPassword.setDisable(false);
            setHideCheckboxPw();
            setEditableTrue();
        }
        else {
            this.apId.setVisible(true);
            this.btnAdd.setDisable(true);
            setEditableFalse();
        }
    }

    // X??? l?? ?? checkbox edit
    private void setCheckBoxEdit() {
        if(this.chbEdit.isSelected()) {
            // ???n 2 n??t th??m v?? x??a
            this.chbAdd.setSelected(false);
            this.chbDelete.setSelected(false);
            this.btnAdd.setDisable(true);
            this.btnDelete.setDisable(true);

            this.apId.setVisible(true);
            this.btnEdit.setDisable(false);
            this.apPassword.setDisable(true);
            this.resetPw.setVisible(true);
            this.resetPw.setSelected(false);
            setTextData();
            setEditableTrue();
            this.txtUsername.setEditable(false);
            if (id == 0) {
                clearDataInput();
                AlertUtils.showAlert("Vui l??ng ch???n t??i kho???n mu???n s???a!", Alert.AlertType.INFORMATION);
            }
            else{
                setTextData();
            }
        }
        else {
            this.btnEdit.setDisable(true);
            setEditableFalse();
        }
    }

    // x??? l?? ?? checkbox delete
    private void setCheckBoxDelete() {
        if(this.chbDelete.isSelected()) {
            // ???n 2 n??t th??m v?? s???a
            this.chbAdd.setSelected(false);
            this.chbEdit.setSelected(false);
            this.btnAdd.setDisable(true);
            this.btnEdit.setDisable(true);
            this.apId.setVisible(true);            // hi???n Id
            this.btnDelete.setDisable(false);
            this.apPassword.setDisable(true);
            setHideCheckboxPw();
            setEditableFalse();
            if (id == 0) {
                clearDataInput();
                AlertUtils.showAlert("Vui l??ng ch???n t??i kho???n mu???n x??a!", Alert.AlertType.INFORMATION);
            }
            else{
                setTextData();
            }
        }
        else {
            this.btnDelete.setDisable(true);
        }
    }

    // ???n ?? reset password
    public void setHideCheckboxPw(){
        this.resetPw.setVisible(false);
        this.resetPw.setSelected(false);
    }

    // set label cho tr?????ng h???p kh??ng c?? nh??n vi??n
    private void setTbvStaff() {
        this.tbvStaff.setPlaceholder(new Label("Kh??ng c?? nh??n vi??n n??o!"));
        this.tbvStaff.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.tbvStaff.setEditable(false);
    }

    // T???o c??c c???t v?? match d??? li???u cho table view
    private void loadStaffTbvColumns() {
        TableColumn<Staff, Integer> idColumn = new TableColumn<>("M??");
        TableColumn<Staff, Integer> nameStaffColumn = new TableColumn<>("T??n nh??n vi??n");
        TableColumn<Staff, String> lastNameColumn = new TableColumn<>("H???");
        TableColumn<Staff, String> firstNameColumn = new TableColumn<>("T??n");
        nameStaffColumn.getColumns().addAll(lastNameColumn, firstNameColumn);
        TableColumn<Staff, String> cardColumn = new TableColumn<>("S??? CMND");
        TableColumn<Staff, String> usernameColum = new TableColumn<>("T??n t??i kho???n");
        TableColumn<Staff, String> phoneNumberColumn = new TableColumn<>("S??? ??i???n tho???i");
        TableColumn<Staff, String> sexColumn = new TableColumn<>("Gi???i t??nh");
        TableColumn<Staff, Date> dateOfBirthColumn = new TableColumn<>("Ng??y sinh");
        TableColumn<Staff, Date> joinedDateColumn = new TableColumn<>("Ng??y tham gia");
        TableColumn<Staff, String> branchColumn = new TableColumn<>("Nh??nh");
        TableColumn<Staff, String> roleColumn = new TableColumn<>("Quy???n");
        TableColumn<Staff, Boolean> activeColumn = new TableColumn<>("Tr???ng th??i");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("persId"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("persLastName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("persFirstName"));
        usernameColum.setCellValueFactory(new PropertyValueFactory<>("staUsername"));
        cardColumn.setCellValueFactory(new PropertyValueFactory<>("persIdCard"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("persPhoneNumber"));
        sexColumn.setCellValueFactory(new PropertyValueFactory<>("sex"));
        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("persDateOfBirth"));
        joinedDateColumn.setCellValueFactory(new PropertyValueFactory<>("persJoinedDate"));
        branchColumn.setCellValueFactory(new PropertyValueFactory<>("branchName"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("roleName"));
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("activeName"));

        idColumn.setPrefWidth(50);
        lastNameColumn.setPrefWidth(120);
        firstNameColumn.setPrefWidth(120);
        usernameColum.setPrefWidth(120);
        cardColumn.setPrefWidth(150);
        phoneNumberColumn.setPrefWidth(150);
        sexColumn.setPrefWidth(80);
        dateOfBirthColumn.setPrefWidth(120);
        joinedDateColumn.setPrefWidth(120);
        branchColumn.setPrefWidth(200);
        roleColumn.setPrefWidth(120);
        activeColumn.setPrefWidth(150);
        idColumn.setSortType(TableColumn.SortType.DESCENDING);
        this.tbvStaff.getColumns().addAll(idColumn,nameStaffColumn, usernameColum, cardColumn, phoneNumberColumn,
                sexColumn, dateOfBirthColumn, joinedDateColumn, activeColumn, branchColumn, roleColumn);
    }

    // load du lieu cho tableview
    @FXML
    private void loadTbvData() {
        try {
            this.tbvStaff.setItems(FXCollections.observableList(STAFF_SERVICE.getListStaff(this.txtSearch.getText().trim())));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // thi???t l???p d??? li???u cho c??c th??? input
    private void setTextData() {
        Staff selected = this.tbvStaff.getSelectionModel().getSelectedItem();
        if (selected != null) {
            this.id = selected.getPersId();
            this.lblId.setText(String.valueOf(selected.getPersId()));
            this.txtLastName.setText(String.valueOf(selected.getPersLastName()));
            this.txtFirstName.setText(String.valueOf(selected.getPersFirstName()));
            this.txtIdCard.setText(String.valueOf(selected.getPersIdCard()));
            this.txtPhoneNumber.setText(String.valueOf(selected.getPersPhoneNumber()));
            this.cbSex.setValue(String.valueOf(selected.getSex()));
            this.dpDateOfBirth.setValue(LocalDate.parse(String.valueOf(selected.getPersDateOfBirth())));
            this.txtActive.setText(String.valueOf(selected.getActiveName()));
            this.cbBranch.setValue(String.valueOf(selected.getBranchName()));
            this.cbRole.setValue(String.valueOf(selected.getRoleName()));
            this.txtUsername.setText(String.valueOf(selected.getStaUsername()));

        }
    }

    // x??a d??? li???u c??c th??? input
    private void clearDataInput() {
        this.lblId.setText("");
        this.txtLastName.setText("");
        this.txtFirstName.setText("");
        this.txtIdCard.setText("");
        this.txtPhoneNumber.setText("");
        this.cbSex.setValue(null);
        this.cbSex.setPromptText("Ch???n gi???i t??nh");
        this.dpDateOfBirth.setValue(LocalDate.now());
        this.cbBranch.setValue(null);
        this.cbBranch.setPromptText("Ch???n nh??nh");
        this.cbRole.setValue(null);
        this.cbRole.setPromptText("Ch???n quy???n");
        this.txtActive.setText("");
        this.txtUsername.setText("");
        this.pwPassword.setText("");
        this.pwConfirmPw.setText("");
    }

    // ki???m tra d??? li???u nh???p d??? th??m staff
    private boolean checkInvalidStaff() {
        if(isInvalid()) {
            if(isUsername()) {
                if (isActive()) {
                    AlertUtils.showAlert("T??n t??i kho???n ???? t???n t???i!", Alert.AlertType.ERROR);
                    return false;
                }
                else {
                    // s???a active
                    try {
                        if(STAFF_SERVICE.setActive(this.txtUsername.getText().trim())) {
                            AlertUtils.showAlert("H??? th???ng ???? k??ch ho???t t??i kho???n " + this.txtUsername.getText().trim()
                                    , Alert.AlertType.INFORMATION);
                            loadTbvData();
                            clearDataInput();
                            id =0;
                            return false;
                        }
                        else {
                            AlertUtils.showAlert("Vui l??ng ki???m tra l???i!", Alert.AlertType.ERROR);
                            return false;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        }
                }
            }
            else {
                if (isExistCardId()) {
                    AlertUtils.showAlert("S??? CMND ???? t???n t???i!", Alert.AlertType.ERROR);
                    return false;
                }
                else{
                    return true;
                }
            }
        }
        return false;
    }

    // th??m staff
    public void addStaff(){
        if(checkInvalidStaff()) {
            if (isPasswordMatch()) {
                Staff staff = new Staff();
                staff = createStaff();

                try {
                    if (!STAFF_SERVICE.addStaff(staff)) {
                        AlertUtils.showAlert("Vui l??ng ki???m tra l???i th??ng tin!", Alert.AlertType.ERROR);
                    } else {
                        AlertUtils.showAlert("Th??m th??nh c??ng!", Alert.AlertType.INFORMATION);
                        loadTbvData();
                        clearDataInput();
                        id = 0;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ki???m tra m???t kh???u c?? tr??ng kh???p, h???p l??? kh??ng
    public boolean isPasswordMatch() {
        if (this.pwPassword.getText().isEmpty() || this.pwConfirmPw.getText().isEmpty()){
            AlertUtils.showAlert("M???t kh???u kh??ng ???????c ????? tr???ng !", Alert.AlertType.ERROR);
            return false;
        }
        else if (this.pwPassword.getText().length() < 6) {
            AlertUtils.showAlert("????? d??i m???t kh???u ph???i t??? 6 k?? t??? tr??? l??n!", Alert.AlertType.ERROR);
            return false;
        }
        else if(!this.pwPassword.getText().equals(this.pwConfirmPw.getText())){
            AlertUtils.showAlert("M???t kh???u kh??ng kh???p !", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    // t???o ?????i t?????ng staff
    private Staff createStaff() {
        Staff staff = new Staff();

        staff.setPersLastName(this.txtLastName.getText().trim());
        staff.setPersFirstName(this.txtFirstName.getText().trim());
        staff.setPersIdCard(this.txtIdCard.getText().trim());
        staff.setPersPhoneNumber(this.txtPhoneNumber.getText().trim());
        staff.setPersSex(this.cbSex.getValue() == "Nam" ? Byte.parseByte("1") : Byte.parseByte("0"));
        Date dateOB = new Date(this.dpDateOfBirth.getValue().atStartOfDay(ZoneId.of("Asia/Ho_Chi_Minh")).toEpochSecond() * 1000);
        staff.setPersDateOfBirth(dateOB);
        //
        staff.setBranchName(this.cbBranch.getValue().toString());
        staff.setStaIsAdmin(this.cbRole.getValue().toString() == "Qu???n tr??? vi??n" ? true : false);
        staff.setStaUsername(this.txtUsername.getText().trim());
        staff.setStaPassword(this.pwPassword.getText());

        return staff;
    }

    // ki???m tra d??? li???u nh???p
    private boolean isInvalid() {
        if(this.txtLastName.getText().trim().isEmpty() ||
            this.txtFirstName.getText().trim().isEmpty() || this.txtIdCard.getText().trim().isEmpty() ||
                this.cbSex.getValue() == null || this.dpDateOfBirth.getValue() == null ||
            this.cbBranch.getValue() == null || this.cbRole.getValue() == null){
                AlertUtils.showAlert("Vui l??ng ki???m tra l???i!", Alert.AlertType.ERROR);
                return false;
        }
        if (this.txtIdCard.getText().trim().length() > 12 || this.txtIdCard.getText().trim().length() < 9) {
                AlertUtils.showAlert("S??? CMND ph???i t??? 9 t???i 12 k?? t???!", Alert.AlertType.ERROR);
                return false;
        }
        if (this.txtPhoneNumber.getText().trim().length() != 10) {
                    AlertUtils.showAlert("S??? ??i???n tho???i ph???i c?? 10 ch??? s???!", Alert.AlertType.ERROR);
                    return false;
        }
        if (this.txtUsername.getText().trim().isEmpty() ) {
            AlertUtils.showAlert("Vui l??ng nh???p t??n t??i kho???n!", Alert.AlertType.ERROR);
            return false;
        }
        if (this.txtUsername.getText().trim().length() > 10) {
            AlertUtils.showAlert("T??n t??i kho???n kh??ng v?????t qu?? 10 k?? t???!", Alert.AlertType.ERROR);
            return false;
        }
        if (this.txtUsername.getText().trim().length() < 6) {
            AlertUtils.showAlert("T??n t??i kho???n ??t nh???t c?? 6 k?? t???!", Alert.AlertType.ERROR);
            return false;
        }
        try {
            Long cmnd = Long.parseLong(this.txtIdCard.getText().trim());
        }catch(NumberFormatException e) {
            AlertUtils.showAlert("S??? CMND sai ?????nh d???ng!", Alert.AlertType.ERROR);
            return false;
        }

        try {
            Integer phoneNumber = Integer.parseInt(this.txtPhoneNumber.getText().trim());
        }catch(NumberFormatException e) {
            AlertUtils.showAlert("S??? ??i???n tho???i sai ?????nh d???ng!", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    // ki???m tra username c?? t???n t???i hay kh??ng
    private boolean isUsername() {
        try {
            return STAFF_SERVICE.isUsername(this.txtUsername.getText().trim());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Ki???m tra t??i kho???n c?? ??ang c??n ho???t ?????ng hay kh??ng
    private boolean isActive(){
        try {
            return STAFF_SERVICE.isActive(this.txtUsername.getText().trim());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // kiem tra so CMND trung khong
    private boolean isExistCardId() {
        try {
            return  STAFF_SERVICE.isExistCardId(this.txtIdCard.getText().trim());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // so s??nh 2 staff
    public boolean isCompareStaff(Staff staffInput, Staff staffDatabase) {
        if (staffInput.getPersLastName().equals(staffDatabase.getPersLastName()) &&
                staffInput.getPersFirstName().equals(staffDatabase.getPersFirstName()) &&
                staffInput.getPersIdCard().equals(staffDatabase.getPersIdCard()) &&
                staffInput.getPersPhoneNumber().equals(staffDatabase.getPersPhoneNumber()) &&
                staffInput.getPersSex() == staffDatabase.getPersSex() &&
                staffInput.getPersDateOfBirth().compareTo(staffDatabase.getPersDateOfBirth()) == 0 &&
                staffInput.getBranchName().equals(staffDatabase.getBranchName()) &&
                staffInput.getStaIsAdmin() == staffDatabase.getStaIsAdmin())
            return true;
        return false;
    }

    // s???a
    public void updateStaff() {
        Staff staffInData = new Staff();
        staffInData = STAFF_SERVICE.getStaffDataById(id);

        if (id != 0) {
            if(isInvalid()) {
                if(staffInData.getPersIsActive()) {
                    Staff staff = new Staff();
                    staff = createStaff();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    staff.setPersDateOfBirth(java.sql.Date.valueOf(sdf.format(staff.getPersDateOfBirth())));

                    if (isCompareStaff(staff, staffInData) && this.resetPw.isSelected() == false) {
                        AlertUtils.showAlert("Th??ng tin kh??ng c?? g?? thay ?????i!", Alert.AlertType.ERROR);
                    }
                    else {
                        if (!staff.getPersIdCard().equals(staffInData.getPersIdCard())) {
                            if (!isExistCardId()) {
                                if (STAFF_SERVICE.updateStaff(staff)) {
                                    AlertUtils.showAlert("C???p nh???t th??nh c??ng", Alert.AlertType.INFORMATION);
                                    this.resetPw.setSelected(false);
                                    clearDataInput();
                                    loadTbvData();
                                    id = 0;
                                }
                                else
                                    AlertUtils.showAlert("H??? th???ng ??ang b???o tr??!", Alert.AlertType.ERROR);
                            }
                            else
                                AlertUtils.showAlert("S??? CMND ???? t???n t???i!", Alert.AlertType.ERROR);
                        }
                        else {
                            if (resetPw.isSelected()) {
                                STAFF_SERVICE.resetPassword(staff.getStaUsername());
                            }
                            if (STAFF_SERVICE.updateStaff(staff)) {
                                AlertUtils.showAlert("C???p nh???t th??nh c??ng", Alert.AlertType.INFORMATION);
                                this.resetPw.setSelected(false);
                                clearDataInput();
                                loadTbvData();
                                id = 0;
                            }
                            else
                                AlertUtils.showAlert("H??? th???ng ??ang b???o tr??!", Alert.AlertType.ERROR);
                        }
                    }
                }
                else
                    AlertUtils.showAlert("T??i kho???n n??y ???? ng??ng ho???t ?????ng!", Alert.AlertType.ERROR);
            }
        }
        else
            AlertUtils.showAlert("Vui l??ng ch???n t??i kho???n mu???n s???a!", Alert.AlertType.ERROR);
    }

    // x??a
    public void deleteStaff() {
        if (id != 0) {
            if(isActive()) {
                if (STAFF_SERVICE.deleteSatff(id)) {
                    AlertUtils.showAlert("X??a th??nh c??ng !", Alert.AlertType.INFORMATION);
                    loadTbvData();
                    clearDataInput();
                    id= 0;
                }
                else
                    AlertUtils.showAlert("H??? th???ng ??ang b???o tr??!", Alert.AlertType.ERROR);
            }
            else {
                AlertUtils.showAlert("T??i kho???n n??y ???? n??y ???? x??a t??? tr?????c!", Alert.AlertType.ERROR);
            }
        }
        else
            AlertUtils.showAlert("Vui l??ng ch???n t??i kho???n mu???n x??a!", Alert.AlertType.ERROR);
    }
}
