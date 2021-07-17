package student;

import rs.etf.sab.operations.PackageOperations;
import rs.etf.sab.operations.CourierRequestOperation;
import rs.etf.sab.operations.UserOperations;
import rs.etf.sab.operations.GeneralOperations;
import rs.etf.sab.operations.CityOperations;
import rs.etf.sab.operations.VehicleOperations;
import rs.etf.sab.operations.DistrictOperations;
import rs.etf.sab.operations.CourierOperations;
import rs.etf.sab.tests.TestRunner;
import rs.etf.sab.tests.TestHandler;

public class StudentMain {

    public static void main(String[] args) {
        CityOperations cityOperations = new jj170586_CityOperations(); // Change this to your implementation.
        DistrictOperations districtOperations = new jj170586_DistrictOperations(); // Do it for all classes.
        CourierOperations courierOperations = new jj170586_CourierOperations(); // e.g. = new MyDistrictOperations();
        CourierRequestOperation courierRequestOperation = new jj170586_CourierRequestOperation();
        GeneralOperations generalOperations = new jj170586_GeneralOperations();
        UserOperations userOperations = new jj170586_UserOperations();
        VehicleOperations vehicleOperations = new jj170586_VehicleOperations();
        PackageOperations packageOperations = new jj170586_PackageOperations();

        TestHandler.createInstance(
                cityOperations,
                courierOperations,
                courierRequestOperation,
                districtOperations,
                generalOperations,
                userOperations,
                vehicleOperations,
                packageOperations);

        TestRunner.runTests();
    }
}
