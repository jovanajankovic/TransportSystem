// 
// Decompiled by Procyon v0.5.36
// 

package rs.etf.sab.tests;

import rs.etf.sab.operations.PackageOperations;
import rs.etf.sab.operations.VehicleOperations;
import rs.etf.sab.operations.UserOperations;
import rs.etf.sab.operations.GeneralOperations;
import rs.etf.sab.operations.DistrictOperations;
import rs.etf.sab.operations.CourierRequestOperation;
import rs.etf.sab.operations.CourierOperations;
import rs.etf.sab.operations.CityOperations;

public class TestHandler
{
    private static TestHandler testHandler;
    private CityOperations cityOperations;
    private CourierOperations courierOperations;
    private CourierRequestOperation courierRequestOperation;
    private DistrictOperations districtOperations;
    private GeneralOperations generalOperations;
    private UserOperations userOperations;
    private VehicleOperations vehicleOperations;
    private PackageOperations packageOperations;
    
    private TestHandler(final CityOperations cityOperations, final CourierOperations courierOperations, final CourierRequestOperation courierRequestOperation, final DistrictOperations districtOperations, final GeneralOperations generalOperations, final UserOperations userOperations, final VehicleOperations vehicleOperations, final PackageOperations packageOperations) {
        this.cityOperations = cityOperations;
        this.courierOperations = courierOperations;
        this.courierRequestOperation = courierRequestOperation;
        this.districtOperations = districtOperations;
        this.generalOperations = generalOperations;
        this.userOperations = userOperations;
        this.vehicleOperations = vehicleOperations;
        this.packageOperations = packageOperations;
    }
    
    public static void createInstance(final CityOperations cityOperations, final CourierOperations courierOperations, final CourierRequestOperation courierRequestOperation, final DistrictOperations districtOperations, final GeneralOperations generalOperations, final UserOperations userOperations, final VehicleOperations vehicleOperations, final PackageOperations packageOperations) {
        TestHandler.testHandler = new TestHandler(cityOperations, courierOperations, courierRequestOperation, districtOperations, generalOperations, userOperations, vehicleOperations, packageOperations);
    }
    
    static TestHandler getInstance() {
        return TestHandler.testHandler;
    }
    
    CityOperations getCityOperations() {
        return this.cityOperations;
    }
    
    CourierOperations getCourierOperations() {
        return this.courierOperations;
    }
    
    CourierRequestOperation getCourierRequestOperation() {
        return this.courierRequestOperation;
    }
    
    DistrictOperations getDistrictOperations() {
        return this.districtOperations;
    }
    
    GeneralOperations getGeneralOperations() {
        return this.generalOperations;
    }
    
    UserOperations getUserOperations() {
        return this.userOperations;
    }
    
    VehicleOperations getVehicleOperations() {
        return this.vehicleOperations;
    }
    
    PackageOperations getPackageOperations() {
        return this.packageOperations;
    }
    
    static {
        TestHandler.testHandler = null;
    }
}
