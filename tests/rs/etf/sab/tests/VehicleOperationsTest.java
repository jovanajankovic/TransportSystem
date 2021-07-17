// 
// Decompiled by Procyon v0.5.36
// 

package rs.etf.sab.tests;

import org.junit.Test;
import java.math.BigDecimal;
import org.junit.After;
import org.junit.Before;
import org.junit.Assert;
import rs.etf.sab.operations.VehicleOperations;
import rs.etf.sab.operations.GeneralOperations;

public class VehicleOperationsTest
{
    private GeneralOperations generalOperations;
    private VehicleOperations vehicleOperations;
    private TestHandler testHandler;
    
    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull(this.testHandler = TestHandler.getInstance());
        Assert.assertNotNull(this.vehicleOperations = this.testHandler.getVehicleOperations());
        Assert.assertNotNull(this.generalOperations = this.testHandler.getGeneralOperations());
        this.generalOperations.eraseAll();
    }
    
    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void insertVehicle() {
        final String licencePlateNumber = "BG234DU";
        final BigDecimal fuelConsumption = new BigDecimal(6.3);
        final int fuelType = 1;
        Assert.assertTrue(this.vehicleOperations.insertVehicle(licencePlateNumber, fuelType, fuelConsumption));
    }
    
    @Test
    public void deleteVehicles() {
        final String licencePlateNumber = "BG234DU";
        final BigDecimal fuelConsumption = new BigDecimal(6.3);
        final int fuelType = 1;
        this.vehicleOperations.insertVehicle(licencePlateNumber, fuelType, fuelConsumption);
        Assert.assertEquals(1L, this.vehicleOperations.deleteVehicles(licencePlateNumber));
    }
    
    @Test
    public void getAllVehichles() {
        final String licencePlateNumber = "BG234DU";
        final BigDecimal fuelConsumption = new BigDecimal(6.3);
        final int fuelType = 1;
        this.vehicleOperations.insertVehicle(licencePlateNumber, fuelType, fuelConsumption);
        Assert.assertTrue(this.vehicleOperations.getAllVehichles().contains(licencePlateNumber));
    }
    
    @Test
    public void changeFuelType() {
        final String licencePlateNumber = "BG234DU";
        final BigDecimal fuelConsumption = new BigDecimal(6.3);
        final int fuelType = 1;
        this.vehicleOperations.insertVehicle(licencePlateNumber, fuelType, fuelConsumption);
        Assert.assertTrue(this.vehicleOperations.changeFuelType(licencePlateNumber, 2));
    }
    
    @Test
    public void changeConsumption() {
        final String licencePlateNumber = "BG234DU";
        final BigDecimal fuelConsumption = new BigDecimal(6.3);
        final int fuelType = 1;
        this.vehicleOperations.insertVehicle(licencePlateNumber, fuelType, fuelConsumption);
        Assert.assertTrue(this.vehicleOperations.changeConsumption(licencePlateNumber, new BigDecimal(7.3)));
    }
}
