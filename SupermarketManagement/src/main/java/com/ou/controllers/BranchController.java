package com.ou.controllers;

import com.ou.pojos.Branch;
import com.ou.services.BranchService;
import com.ou.utils.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BranchController implements Initializable {
    private final static BranchService BRANCH_SERVICE;

    static {
        BRANCH_SERVICE = new BranchService();
    }

    @FXML
    private TableView<Branch> tbvBranch;

    @FXML
    private TextField txtSearchBraName;

    @FXML
    private TextField txtBraId;

    @FXML
    private TextField txtBraName;

    @FXML
    private TextField txtBraAddress;

    @FXML
    private TextField txtBraStaffAmount;

    @FXML
    private TextField txtBraProductAmount;

    @FXML
    private Text textBraAmount;

    @FXML
    private Button btnAddBra;

    @FXML
    private Button btnEditBra;

    @FXML
    private Button btnDelBra;

    @FXML
    private Button btnBack;

    // Khởi tạo trước khi giao diện hiển thị
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initInputData();
        this.initBranchTbv();
        this.loadBranchTbvColumns();
        this.loadBranchTbvData(this.txtSearchBraName.getText());
        this.loadBranchAmount();
        this.tbvBranch.getSelectionModel().getSelectedItems()
                .addListener((ListChangeListener<? super Branch>) e -> changeInputData());
        this.btnAddBra.setOnMouseClicked(e -> addBranch());
        this.btnEditBra.setOnMouseClicked(e -> updateBranch());
        this.btnDelBra.setOnMouseClicked(e -> deleteBranch());
        this.btnBack.setOnMouseClicked(e -> backMenu());
        this.txtSearchBraName.textProperty().addListener(e->loadBranchTbvData(this.txtSearchBraName.getText()));
    }

    // KHởi tạo các thuộc tính của vùng input
    private void initInputData() {
        this.txtBraId.setEditable(false);
        this.txtBraProductAmount.setEditable(false);
        this.txtBraStaffAmount.setEditable(false);
    }

    // Khởi tạo các thuộc tính của table view liệu chi nhánh
    private void initBranchTbv() {
        this.tbvBranch.setPlaceholder(new Label("Không có chi nhánh nào!"));
        this.tbvBranch.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.tbvBranch.setEditable(false);
    }

    // Lấy dữ liệu cho table view
    private void loadBranchTbvData(String kw) {
        try {
            this.tbvBranch.setItems(FXCollections.observableList(BRANCH_SERVICE.getBranches(kw)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tạo các cột và match dữ liệu cho table view
    private void loadBranchTbvColumns() {
        TableColumn<Branch, Integer> braIdColumn = new TableColumn<>("Mã chi nhánh");
        TableColumn<Branch, String> braNameColumn = new TableColumn<>("Tên chi nhánh");
        TableColumn<Branch, String> braAddressColumn = new TableColumn<>("Địa chỉ chi nhánh");
        TableColumn<Branch, Integer> braStaffAmountColumn = new TableColumn<>("Số lượng nhân viên");
        TableColumn<Branch, String> braProductAmountColumn = new TableColumn<>("Số lượng sản phẩm");
        braIdColumn.setCellValueFactory(new PropertyValueFactory<>("braId"));
        braNameColumn.setCellValueFactory(new PropertyValueFactory<>("braName"));
        braAddressColumn.setCellValueFactory(new PropertyValueFactory<>("braAddress"));
        braStaffAmountColumn.setCellValueFactory(new PropertyValueFactory<>("staffAmount"));
        braProductAmountColumn.setCellValueFactory(new PropertyValueFactory<>("productAmount"));
        braIdColumn.setPrefWidth(200);
        braNameColumn.setPrefWidth(400);
        braAddressColumn.setPrefWidth(600);
        braStaffAmountColumn.setPrefWidth(150);
        braProductAmountColumn.setPrefWidth(150);
        braIdColumn.setSortType(TableColumn.SortType.DESCENDING);
        this.tbvBranch.getColumns().addAll(braIdColumn, braNameColumn, braAddressColumn,
                braStaffAmountColumn, braProductAmountColumn);
    }

    // Lấy số lượng chi nhánh
    private void loadBranchAmount() {
        try {
            this.textBraAmount.setText(String.valueOf(BRANCH_SERVICE.getBranchAmount()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Thiết lập vùng dữ liệu input khi lựa chọn thay đổi dưới table view
    private void changeInputData() {
        Branch selectedBra = this.tbvBranch.getSelectionModel().getSelectedItem();
        if (selectedBra != null) {
            this.txtBraId.setText(String.valueOf(selectedBra.getBraId()));
            this.txtBraName.setText(selectedBra.getBraName());
            this.txtBraAddress.setText(selectedBra.getBraAddress());
            this.txtBraStaffAmount.setText(String.valueOf(selectedBra.getStaffAmount()));
            this.txtBraProductAmount.setText(String.valueOf(selectedBra.getProductAmount()));
        }
    }

    // reset dữ liệu vùng input
    private void clearInputData() {
        this.txtBraId.setText("");
        this.txtBraName.setText("");
        this.txtBraAddress.setText("");
        this.txtBraStaffAmount.setText("");
        this.txtBraProductAmount.setText("");
    }

    // Thêm một chi nhánh mới
    private void addBranch() {
        Branch branch = new Branch();
        branch.setBraName(this.txtBraName.getText());
        branch.setBraAddress(this.txtBraAddress.getText());
        try {
            if (BRANCH_SERVICE.addBranch(branch)) {
                AlertUtils.showAlert("Thêm thành công", Alert.AlertType.INFORMATION);
                loadBranchTbvData(this.txtSearchBraName.getText());
                loadBranchAmount();
                clearInputData();
            } else {
                AlertUtils.showAlert("Thêm thất bại!", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật thông tin một chi nhánh
    private void updateBranch() {
        Branch branch = new Branch();
        try {
            branch.setBraId(Integer.parseInt(this.txtBraId.getText()));
        } catch (NumberFormatException e) {
            AlertUtils.showAlert("Sửa thất bại! Vui lòng chọn chi nhánh để sửa", Alert.AlertType.ERROR);
            return;
        }
        branch.setBraName(this.txtBraName.getText());
        branch.setBraAddress(this.txtBraAddress.getText());
        try {
            if (BRANCH_SERVICE.updateBranch(branch)) {
                AlertUtils.showAlert("Sửa thành công", Alert.AlertType.INFORMATION);
                loadBranchTbvData(this.txtSearchBraName.getText());
                loadBranchAmount();
                clearInputData();
            } else {
                AlertUtils.showAlert("Sửa thất bại!", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa một chi nhánh
    private void deleteBranch() {
        Branch branch = new Branch();
        try {
            branch.setBraId(Integer.parseInt(this.txtBraId.getText()));
        } catch (NumberFormatException inputMismatchException) {
            AlertUtils.showAlert("Xóa thất bại! Vui lòng chọn chi nhánh cần xóa!", Alert.AlertType.ERROR);
            return;
        }
        try {
            if (BRANCH_SERVICE.deleteBranch(branch)) {
                AlertUtils.showAlert("Xoá thành công", Alert.AlertType.INFORMATION);
                loadBranchTbvData(this.txtSearchBraName.getText());
                loadBranchAmount();
                clearInputData();
            } else {
                AlertUtils.showAlert("Xóa thất bại!", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Trở về giao diện ban đầu
    private void backMenu() {
        // Back to menu here
    }

}
