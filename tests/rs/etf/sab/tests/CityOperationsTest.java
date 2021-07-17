// 
// Decompiled by Procyon v0.5.36
// 

package rs.etf.sab.tests;

import java.util.Random;
import org.junit.Test;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Assert;
import rs.etf.sab.operations.CourierRequestOperation;
import rs.etf.sab.operations.CourierOperations;
import rs.etf.sab.operations.VehicleOperations;
import rs.etf.sab.operations.CityOperations;
import rs.etf.sab.operations.GeneralOperations;

public class CityOperationsTest
{
    private TestHandler testHandler;
    private GeneralOperations generalOperations;
    private CityOperations cityOperations;
    private VehicleOperations vehicleOperations;
    private CourierOperations courierOperations;
    private CourierRequestOperation courierRequestOperation;
    
    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull(this.testHandler = TestHandler.getInstance());
        Assert.assertNotNull(this.cityOperations = this.testHandler.getCityOperations());
        Assert.assertNotNull(this.generalOperations = this.testHandler.getGeneralOperations());
        Assert.assertNotNull(this.courierOperations = this.testHandler.getCourierOperations());
        Assert.assertNotNull(this.vehicleOperations = this.testHandler.getVehicleOperations());
        Assert.assertNotNull(this.courierRequestOperation = this.testHandler.getCourierRequestOperation());
        this.generalOperations.eraseAll();
    }
    
    @After
    public void tearDown() throws Exception {
        this.generalOperations.eraseAll();
    }
    
    @Test
    public void insertCity_OnlyOne() throws Exception {
        final String name = "Tokyo";
        final String postalCode = "100";
        final int rowId = this.cityOperations.insertCity(name, postalCode);
        final List<Integer> list = this.cityOperations.getAllCities();
        Assert.assertEquals(1L, list.size());
        Assert.assertTrue(list.contains(rowId));
    }
    
    @Test
    public void insertCity_TwoCities_SameBothNameAndPostalCode() throws Exception {
        final String name = "Tokyo";
        final String postalCode = "100";
        final int rowIdValid = this.cityOperations.insertCity(name, postalCode);
        final int rowIdInvalid = this.cityOperations.insertCity(name, postalCode);
        Assert.assertEquals(-1L, rowIdInvalid);
        final List<Integer> list = this.cityOperations.getAllCities();
        Assert.assertEquals(1L, list.size());
        Assert.assertTrue(list.contains(rowIdValid));
    }
    
    @Test
    public void insertCity_TwoCities_SameName() throws Exception {
        final String name = "Tokyo";
        final String postalCode1 = "100";
        final String postalCode2 = "1020";
        final int rowIdValid = this.cityOperations.insertCity(name, postalCode1);
        final int rowIdInvalid = this.cityOperations.insertCity(name, postalCode2);
        Assert.assertEquals(-1L, rowIdInvalid);
        final List<Integer> list = this.cityOperations.getAllCities();
        Assert.assertEquals(1L, list.size());
        Assert.assertTrue(list.contains(rowIdValid));
    }
    
    @Test
    public void insertCity_TwoCities_SamePostalCode() throws Exception {
        final String name1 = "Tokyo";
        final String name2 = "Beijing";
        final String postalCode = "100";
        final int rowIdValid = this.cityOperations.insertCity(name1, postalCode);
        final int rowIdInvalid = this.cityOperations.insertCity(name2, postalCode);
        Assert.assertEquals(-1L, rowIdInvalid);
        final List<Integer> list = this.cityOperations.getAllCities();
        Assert.assertEquals(1L, list.size());
        Assert.assertTrue(list.contains(rowIdValid));
    }
    
    @Test
    public void insertCity_MultipleCities() throws Exception {
        final String name1 = "Tokyo";
        final String name2 = "Beijing";
        final String postalCode1 = "100";
        final String postalCode2 = "065001";
        final int rowId1 = this.cityOperations.insertCity(name1, postalCode1);
        final int rowId2 = this.cityOperations.insertCity(name2, postalCode2);
        final List<Integer> list = this.cityOperations.getAllCities();
        Assert.assertEquals(2L, list.size());
        Assert.assertTrue(list.contains(rowId1));
        Assert.assertTrue(list.contains(rowId2));
    }
    
    @Test
    public void deleteCity_WithId_OnlyOne() {
        final String name = "Beijing";
        final String postalCode = "065001";
        final int rowId = this.cityOperations.insertCity(name, postalCode);
        Assert.assertNotEquals(-1L, rowId);
        Assert.assertTrue(this.cityOperations.deleteCity(rowId));
        Assert.assertEquals(0L, this.cityOperations.getAllCities().size());
    }
    
    @Test
    public void deleteCity_WithId_OnlyOne_NotExisting() {
        final Random random = new Random();
        final int rowId = random.nextInt();
        Assert.assertFalse(this.cityOperations.deleteCity(rowId));
        Assert.assertEquals(0L, this.cityOperations.getAllCities().size());
    }
    
    @Test
    public void deleteCity_WithName_One() {
        final String name = "Beijing";
        final String postalCode = "065001";
        final int rowId = this.cityOperations.insertCity(name, postalCode);
        Assert.assertNotEquals(-1L, rowId);
        Assert.assertEquals(1L, this.cityOperations.deleteCity(name));
        Assert.assertEquals(0L, this.cityOperations.getAllCities().size());
    }
    
    @Test
    public void deleteCity_WithName_MultipleCities() throws Exception {
        final String name1 = "Tokyo";
        final String name2 = "Beijing";
        final String postalCode1 = "100";
        final String postalCode2 = "065001";
        final int rowId1 = this.cityOperations.insertCity(name1, postalCode1);
        final int rowId2 = this.cityOperations.insertCity(name2, postalCode2);
        final List<Integer> list = this.cityOperations.getAllCities();
        Assert.assertEquals(2L, list.size());
        Assert.assertEquals(2L, this.cityOperations.deleteCity(name1, name2));
    }
    
    @Test
    public void deleteCity_WithName_OnlyOne_NotExisting() {
        final String name = "Tokyo";
        Assert.assertEquals(0L, this.cityOperations.deleteCity(name));
        Assert.assertEquals(0L, this.cityOperations.getAllCities().size());
    }
}
