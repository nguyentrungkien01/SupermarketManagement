package com.ou.services;

import com.ou.pojos.*;
import com.ou.utils.DatabaseUtils;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductServiceTest {
    private static Connection connection;
    private static ProductService productService;
    private static ProductServiceForTest productServiceForTest;
    private static CategoryServiceForTest categoryServiceForTest;
    private static ManufacturerServiceForTest manufacturerServiceForTest;
    private static UnitServiceForTest unitServiceForTest;
    private static BranchServiceForTest branchServiceForTest;

    public ProductServiceTest() {

    }

    private Product generateProduct(Product product) throws SQLException {
        List<ProductBranch> productBranches = new ArrayList<>();
        List<ProductUnit> productUnits = new ArrayList<>();
        List<ProductLimitSale> productLimitSales = new ArrayList<>();
        Category category = categoryServiceForTest.getCategoryById(1);
        Manufacturer manufacturer = manufacturerServiceForTest.getManufacturerById(1);
        ProductBranch productBranch1 = new ProductBranch();
        ProductBranch productBranch2 = new ProductBranch();
        ProductUnit productUnit1 = new ProductUnit();
        ProductUnit productUnit2 = new ProductUnit();
        Branch branch1 = branchServiceForTest.getBranchById(1);
        Branch branch2 = branchServiceForTest.getBranchById(2);
        Unit unit1 = unitServiceForTest.getUnitById(1);
        Unit unit2 = unitServiceForTest.getUnitById(2);
        LimitSale limitSale1 = new LimitSale();
        LimitSale limitSale2 = new LimitSale();
        ProductLimitSale productLimitSale1 = new ProductLimitSale();
        ProductLimitSale productLimitSale2 = new ProductLimitSale();
        productBranch1.setBranch(branch1);
        productBranch2.setBranch(branch2);
        productUnit1.setUnit(unit1);
        productUnit1.setProPrice(BigDecimal.valueOf(100000));
        productUnit2.setUnit(unit2);
        productUnit2.setProPrice(BigDecimal.valueOf(200000));
        productBranches.add(productBranch1);
        productBranches.add(productBranch2);
        productUnits.add(productUnit1);
        productUnits.add(productUnit2);
        limitSale1.setSaleId(5);
        limitSale2.setSaleId(8);
        productLimitSale1.setLimitSale(limitSale1);
        productLimitSale2.setLimitSale(limitSale2);
        productLimitSales.add(productLimitSale1);
        productLimitSales.add(productLimitSale2);
        product.setProductBranches(productBranches);
        product.setProductUnits(productUnits);
        product.setProductLimitSales(productLimitSales);
        product.setCategory(category);
        product.setManufacturer(manufacturer);
        product.setProName("T??n s???n ph???m th??? 11");
        return product;
    }
    
    
    @BeforeAll
    public static void setUpClass() {
        try {
            connection = DatabaseUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        productService = new ProductService();
        productServiceForTest = new ProductServiceForTest();
        categoryServiceForTest = new CategoryServiceForTest();
        manufacturerServiceForTest = new ManufacturerServiceForTest();
        unitServiceForTest = new UnitServiceForTest();
        branchServiceForTest = new BranchServiceForTest();

    }

    @AfterAll
    public static void tearDownClass() {
        if (connection != null)
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    // Ki???m tra l???y th??ng tin s???n ph???m khi t??? kh??a truy???n v??o l?? null
    // Ph???i tr??? v??? t???t c??? c??c s???n ph???m c??n ho???t ?????ng (10 nh?? s???n xu???t)
    @Test
    public void testSelectAllProductByNullKw() {
        try {
            List<Product> products = productService.getProducts(null);
            int amount = productService.getProductAmount();
            Assertions.assertEquals(amount, products.size());
            for(int i= 0; i<10; i++)
                Assertions.assertEquals(i+1, products.get(i).getProId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ki???m tra l???y th??ng tin s???n ph???m khi t??? kh??a truy???n v??o l?? 1 chu???i r???ng
    // Ph???i tr??? v??? t???t c??? c??c s???n ph???m c??n ho???t ?????ng (10 nh?? s???n xu???t)
    @Test
    public void testSelectAllProductByEmptyKw() {
        try {
            List<Product> products = productService.getProducts("");
            int amount = productService.getProductAmount();
            Assertions.assertEquals(amount, products.size());
            for(int i= 0; i<10; i++)
                Assertions.assertEquals(i+1, products.get(i).getProId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ki???m tra l???y th??ng tin s???n ph???m khi t??? kh??a truy???n v??o l?? 1 chu???i r???ng
    // Ph???i tr??? v??? t???t c??? c??c s???n ph???m c??n ho???t ?????ng (10 nh?? s???n xu???t)
    @Test
    public void testSelectAllProductBySpacesKw() {
        try {
            List<Product> products = productService.getProducts("        ");
            int amount = productService.getProductAmount();
            Assertions.assertEquals(amount, products.size());
            for(int i= 0; i<10; i++)
                Assertions.assertEquals(i+1, products.get(i).getProId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ki???m tra l???y th??ng tin s???n ph???m khi t??? kh??a truy???n v??o l?? t??n 1 s???n ph???m d?????i database
    // C?? 2 nh?? s???n xu???t t??n "T??n s???n ph???m th??? 1","T??n s???n ph???m th??? 10"
    @Test
    public void testSelectAllProductByValidKw() {
        try {
            List<Product> products = productService.getProducts("T??n s???n ph???m th??? 1");
            Assertions.assertEquals(2, products.size());
            Assertions.assertEquals(1, products.get(0).getProId());
            Assertions.assertEquals(10, products.get(1).getProId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ki???m tra l???y th??ng tin s???n ph???m khi truy???n v??o l?? 1 s???n ph???m kh??ng t???n t???i d?????i database
    // Kh??ng c?? s???n ph???m t??n "T??n s???n ph???m th??? 99999999999999999"
    @Test
    public void testSelectAllProductByInValid() {
        try {
            List<Product> products = productService.getProducts("T??n s???n ph???m th??? 99999999999999999");
            Assertions.assertEquals(0,products.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ki???m tra s??? l???y s??? l?????ng s???n ph???m c??n ho???t ?????ng d?????i database
    // C?? 10 nh?? s???n xu???t d?????i database nh??ng ch??? c?? 9 s???n ph???m c??n ho???t ?????ng
    @Test
    public void testGetProductAmount() {
        try {
            Product product = productServiceForTest.getProductById(1);
            productService.deleteProduct(product);
            int amount = productService.getProductAmount();
            Assertions.assertEquals(9, amount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // L???y th??ng tin s??? l?????ng ????n v??? c???a s???n ph???m c?? id kh??ng t???n t???i
    // s???n ph???m c?? id l??  9999 c?? 0 lo???i ????n v???
    @Test
    public void testGetProductUnitsWithInValidId() {
        try {
            List<ProductUnit> productUnits = productService.getProductUnits(9999);
            Assertions.assertEquals(0, productUnits.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // L???y th??ng tin s??? l?????ng ????n v??? c???a s???n ph???m c?? id h???p l???
    // s???n ph???m c?? id l?? 1 c?? 3 lo???i ????n v??? nh??ng ????n v??? 1 c?? active = false n??n c??n 2 lo???i ????n v???
    @Test
    public void testGetProductUnitsWithValidId() {
        try {
            List<ProductUnit> productUnits = productService.getProductUnits(1);
            Assertions.assertEquals(2, productUnits.size());
            Assertions.assertEquals(2, productUnits.get(0).getPruId());
            Assertions.assertEquals(3, productUnits.get(1).getPruId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // L???y th??ng tin s??? l?????ng chi nh??nh ??ang b??n s???n ph???m c?? id kh??ng t???n t???i
    // s???n ph???m c?? id l??  9999 c?? 0 chi nh??nh ??ang b??n
    @Test
    public void testGetProductBranchesWithInValidId() {
        try {
            List<ProductBranch> productBranches = productService.getProductBranches(9999);
            Assertions.assertEquals(0, productBranches.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // L???y th??ng tin s??? l?????ng chi nh??nh ??ang b??n s???n ph???m c?? id h???p l???
    // s???n ph???m c?? id l?? 1 c?? 2 chi nh??nh
    @Test
    public void testGetProductBranchesWithValidId() {
        try {
            List<ProductBranch> productBranches = productService.getProductBranches(1);
            Assertions.assertEquals(2, productBranches.size());
            Assertions.assertEquals("T??n chi nh??nh th??? 1", productBranches.get(0).getBranch().getBraName());
            Assertions.assertEquals("T??n chi nh??nh th??? 2", productBranches.get(1).getBranch().getBraName());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // L???y th??ng tin s??? l?????ng chi nh??nh ??ang b??n s???n ph???m c?? id kh??ng t???n t???i
    // s???n ph???m c?? id l??  9999 c?? 0 chi nh??nh ??ang b??n
    @Test
    public void testGetProductBranchesAmountWithInValidId() {
        try {
            int braAmount = productService.getProductBranchAmount(9999);
            Assertions.assertEquals(0, braAmount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // L???y th??ng tin s??? l?????ng chi nh??nh ??ang b??n s???n ph???m c?? id h???p l???
    // s???n ph???m c?? id l?? 1 c?? 2 chi nh??nh
    @Test
    public void testGetProductBranchesAmountWithValidId() {
        try {
            int braAmount = productService.getProductBranchAmount(1);
            Assertions.assertEquals(2, braAmount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // L???y th??ng tin s??? l?????ng ????n v??? c???a s???n ph???m c?? id kh??ng t???n t???i
    // s???n ph???m c?? id l??  9999 c?? 0 ????n v???
    @Test
    public void testGetProductUnitsAmountWithInValidId() {
        try {
            int uniAmount = productService.getProductUnitAmount(9999);
            Assertions.assertEquals(0, uniAmount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // L???y th??ng tin s??? l?????ng ????n v??? c???a s???n ph???m c?? id h???p l???
    // s???n ph???m c?? id l?? 1 c?? 3 ????n v???
    @Test
    public void testGetProductUnitsAmountWithValidId() {
        try {
            int uniAmount = productService.getProductUnitAmount(1);
            Assertions.assertEquals(3, uniAmount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Ki???m tra th??m gi?? tr??? null khi th??m s???n ph???m
    // Tr??? v??? false
    @Test
    public void testAddProductWithNull() {
        try {
            Assertions.assertFalse(productService.addProduct(null));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ki???m tra th??m th??ng tin s???n ph???m khi th??ng tin kh??ng h???p l???
    //  Tr??? v??? false
    @Test
    public void testAddProductWithInvalidInformation() {
        try {
            Product product = new Product();
            product.setProName("");
            product.setProductUnits(null);
            product.setProductBranches(null);
            product.setManufacturer(null);
            product.setCategory(null);
            Assertions.assertFalse(productService.addProduct(product));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ki???m tra th??m th??ng tin s???n ph???m ???? t???n t???i
    // S???n ph???m c?? m?? l?? 2 ???? t???n t???i. Tr??? v??? false
    @Test
    public void testAddProductWithExist() {
        try {
            Product product = productServiceForTest.getProductById(2);
            Assertions.assertFalse(productService.addProduct(product));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ki???m tra th??m s???n ph???m m???i th??nh c??ng
    // Tr??? v??? true
    @Test
    public void testAddProductWithValidInformation() {
        try {
            Product product = new Product();
            product = generateProduct(product);
            int preAmo = productService.getProductAmount();
            Assertions.assertTrue(productService.addProduct(product));
            int nextAmo = productService.getProductAmount();
            Assertions.assertNotEquals(preAmo, nextAmo);

            Product productTest = productServiceForTest.getProductByName(product.getProName());
            Assertions.assertEquals(product.getProName(), productTest.getProName());
            Assertions.assertEquals(product.getCategory().getCatId(), productTest.getCategory().getCatId());
            Assertions.assertEquals(product.getManufacturer().getManId(), productTest.getManufacturer().getManId());
            for(int i =0 ; i< product.getProductUnits().size(); i++)
                Assertions.assertEquals(product.getProductUnits().get(i).getUnit().getUniId(),
                        productTest.getProductUnits().get(i).getUnit().getUniId());

            for(int i=0; i< product.getProductBranches().size(); i++)
                Assertions.assertEquals(product.getProductBranches().get(i).getBranch().getBraId(),
                        productTest.getProductBranches().get(i).getBranch().getBraId());

            for(int i=0; i<product.getProductLimitSales().size(); i++)
                Assertions.assertEquals(product.getProductLimitSales().get(i).getLimitSale().getSaleId(),
                        productTest.getProductLimitSales().get(i).getLimitSale().getSaleId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Ki???m tra th??m gi?? tr??? null khi s???a s???n ph???m
    // Tr??? v??? false
    @Test
    public void testUpdateProductWithNull() {
        try {
            Assertions.assertFalse(productService.updateProduct(null));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ki???m tra s???a th??ng tin s???n ph???m khi th??ng tin kh??ng h???p l???
    //  Tr??? v??? false
    @Test
    public void testUpdateProductWithInvalidInformation() {
        try {
            Product product = productServiceForTest.getProductById(1);
            product.setProName("");
            product.setProductUnits(null);
            product.setProductBranches(null);
            product.setManufacturer(null);
            product.setCategory(null);
            Assertions.assertFalse(productService.updateProduct(product));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ki???m tra s???a th??ng tin s???n ph???m ???? t???n t???i m?? th??ng tin v???a s???a
    // tr??ng v???i th??ng tin s???n ph???m kh??c ???? t???n t???i
    // S???a th??ng tin s???n ph???m 1 tr??ng v???i th??ng tin s???n ph???m 2 m??
    // s???n ph???m c?? m?? l?? 2 ???? t???n t???i. Tr??? v??? false
    @Test
    public void testUpdateProductWithExist() {
        try {
            Product product = productServiceForTest.getProductById(1);
            product.setProName("T??n s???n ph???m th??? 2");
            Assertions.assertFalse(productService.updateProduct(product));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ki???m tra s???a s???n ph???m m???i th??nh c??ng
    // Tr??? v??? true
    @Test
    public void testUpdateProductWithValidInformation() {
        try {
            Product product = productServiceForTest.getProductById(1);
            product = generateProduct(product);
            Assertions.assertTrue(productService.updateProduct(product));

            Product productTest = productServiceForTest.getProductByName(product.getProName());
            Assertions.assertEquals(product.getProName(), productTest.getProName());
            Assertions.assertEquals(product.getCategory().getCatId(), productTest.getCategory().getCatId());
            Assertions.assertEquals(product.getManufacturer().getManId(), productTest.getManufacturer().getManId());
            for(int i =0 ; i< product.getProductUnits().size(); i++)
                Assertions.assertEquals(product.getProductUnits().get(i).getUnit().getUniId(),
                        productTest.getProductUnits().get(i).getUnit().getUniId());

            for(int i=0; i< product.getProductBranches().size(); i++)
                Assertions.assertEquals(product.getProductBranches().get(i).getBranch().getBraId(),
                        productTest.getProductBranches().get(i).getBranch().getBraId());

            for(int i=0; i<product.getProductLimitSales().size(); i++)
                Assertions.assertEquals(product.getProductLimitSales().get(i).getLimitSale().getSaleId(),
                        productTest.getProductLimitSales().get(i).getLimitSale().getSaleId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ki???m tra gi?? tr??? null khi x??a s???n ph???m
    // Tr??? v??? false
    @Test
    public void testDeleteProductWithNull() {
        try {
            Assertions.assertFalse(productService.deleteProduct(null));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ki???m tra x??a th??ng tin s???n ph???m khi th??ng tin kh??ng h???p l???
    //  Tr??? v??? false
    @Test
    public void testDeleteProductWithInvalidInformation() {
        try {
            Product product = productServiceForTest.getProductById(1);
            product.setProId(null);
            Assertions.assertFalse(productService.deleteProduct(product));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ki???m tra x??a th??ng tin s???n ph???m kh??ng t???n t???i
    // s???n ph???m c?? m?? l?? 9999 kh??ng t???n t???i. Tr??? v??? false
    @Test
    public void testDeleteProductrWithExist() {
        try {
            Product product = productServiceForTest.getProductById(1);
            product.setProId(9999999);
            Assertions.assertFalse(productService.deleteProduct(product));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ki???m tra x??a s???n ph???m th??nh c??ng
    // Tr??? v??? true
    @Test
    public void testDeleteManufacturerWithValidInformation() {
        try {
            Product product = productServiceForTest.getProductById(1);
            int preAmo = productService.getProductAmount();
            Assertions.assertTrue(productService.deleteProduct(product));
            int nextAmo = productService.getProductAmount();
            Assertions.assertNotEquals(preAmo, nextAmo);
            Product productTest = productServiceForTest.getProductByName(product.getProName());
            Assertions.assertFalse(productTest.getProIsActive());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
