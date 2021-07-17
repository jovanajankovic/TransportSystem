// 
// Decompiled by Procyon v0.5.36
// 

package rs.etf.sab.tests;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Assert;
import rs.etf.sab.operations.CityOperations;
import rs.etf.sab.operations.DistrictOperations;
import rs.etf.sab.operations.GeneralOperations;

public class DistrictOperationsTest
{
    private GeneralOperations generalOperations;
    private DistrictOperations districtOperations;
    private CityOperations cityOperations;
    private TestHandler testHandler;
    
    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull(this.testHandler = TestHandler.getInstance());
        Assert.assertNotNull(this.cityOperations = this.testHandler.getCityOperations());
        Assert.assertNotNull(this.districtOperations = this.testHandler.getDistrictOperations());
        Assert.assertNotNull(this.generalOperations = this.testHandler.getGeneralOperations());
        this.generalOperations.eraseAll();
    }
    
    @After
    public void tearDown() throws Exception {
        this.generalOperations.eraseAll();
    }
    
    @Test
    public void insertDistrict_ExistingCity() {
        final int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, idCity);
        final int idDistrict = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
        Assert.assertNotEquals(-1L, idDistrict);
        Assert.assertTrue(this.districtOperations.getAllDistrictsFromCity(idCity).contains(idDistrict));
    }
    
    @Test
    public void deleteDistricts_multiple_existing() {
        final int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, idCity);
        final String districtOneName = "Palilula";
        final String districtTwoName = "Vozdovac";
        final int idDistrict1 = this.districtOperations.insertDistrict(districtOneName, idCity, 10, 10);
        final int idDistrict2 = this.districtOperations.insertDistrict(districtTwoName, idCity, 1, 10);
        Assert.assertEquals(2L, this.districtOperations.deleteDistricts(districtOneName, districtTwoName));
    }
    
    @Test
    public void deleteDistrict() {
        final int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, idCity);
        final int idDistrict1 = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
        Assert.assertTrue(this.districtOperations.deleteDistrict(idDistrict1));
    }
    
    @Test
    public void deleteAllDistrictsFromCity() {
        final String cityName = "Belgrade";
        final int idCity = this.cityOperations.insertCity(cityName, "11000");
        Assert.assertNotEquals(-1L, idCity);
        final String districtOneName = "Palilula";
        final String districtTwoName = "Vozdovac";
        final int idDistrict1 = this.districtOperations.insertDistrict(districtOneName, idCity, 10, 10);
        final int idDistrict2 = this.districtOperations.insertDistrict(districtTwoName, idCity, 1, 10);
        Assert.assertEquals(2L, this.districtOperations.deleteAllDistrictsFromCity(cityName));
    }
    
    @Test
    public void getAllDistrictsFromCity() {
        final String cityName = "Belgrade";
        final int idCity = this.cityOperations.insertCity(cityName, "11000");
        Assert.assertNotEquals(-1L, idCity);
        final String districtOneName = "Palilula";
        final String districtTwoName = "Vozdovac";
        final int idDistrict1 = this.districtOperations.insertDistrict(districtOneName, idCity, 10, 10);
        final int idDistrict2 = this.districtOperations.insertDistrict(districtTwoName, idCity, 1, 10);
        Assert.assertTrue(this.districtOperations.getAllDistrictsFromCity(idCity).contains(idDistrict1));
        Assert.assertTrue(this.districtOperations.getAllDistrictsFromCity(idCity).contains(idDistrict2));
    }
}
