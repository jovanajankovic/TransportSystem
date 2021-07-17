// 
// Decompiled by Procyon v0.5.36
// 

package rs.etf.sab.tests;

import org.junit.Test;
import java.math.BigDecimal;
import org.junit.After;
import org.junit.Before;
import org.junit.Assert;
import rs.etf.sab.operations.CourierRequestOperation;
import rs.etf.sab.operations.CourierOperations;
import rs.etf.sab.operations.VehicleOperations;
import rs.etf.sab.operations.CityOperations;
import rs.etf.sab.operations.DistrictOperations;
import rs.etf.sab.operations.PackageOperations;
import rs.etf.sab.operations.GeneralOperations;
import rs.etf.sab.operations.UserOperations;

public class PackageOperationsTest
{
    private UserOperations userOperations;
    private GeneralOperations generalOperations;
    private PackageOperations packageOperations;
    private TestHandler testHandler;
    private DistrictOperations districtOperations;
    private CityOperations cityOperations;
    private VehicleOperations vehicleOperations;
    private CourierOperations courierOperations;
    private CourierRequestOperation courierRequestOperation;
    
    public PackageOperationsTest() {
        this.vehicleOperations = null;
        this.courierOperations = null;
        this.courierRequestOperation = null;
    }
    
    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull(this.testHandler = TestHandler.getInstance());
        Assert.assertNotNull(this.cityOperations = this.testHandler.getCityOperations());
        Assert.assertNotNull(this.districtOperations = this.testHandler.getDistrictOperations());
        Assert.assertNotNull(this.userOperations = this.testHandler.getUserOperations());
        Assert.assertNotNull(this.packageOperations = this.testHandler.getPackageOperations());
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
    
    private void insertCourier(final String courierUsername) {
        final String firstName = "Svetislav";
        final String lastName = "Kisprdilov";
        final String password = "sisatovac123";
        this.userOperations.insertUser(courierUsername, firstName, lastName, password);
        final String licencePlate = "BG323WE";
        final int fuelType = 0;
        final BigDecimal fuelConsumption = new BigDecimal(8.3);
        this.testHandler.getVehicleOperations().insertVehicle(licencePlate, fuelType, fuelConsumption);
        this.testHandler.getCourierRequestOperation().insertCourierRequest(courierUsername, licencePlate);
        this.testHandler.getCourierRequestOperation().grantRequest(courierUsername);
    }
    
    private void insertUser(final String username) {
        final String firstName = "Svetislav";
        final String lastName = "Kisprdilov";
        final String password = "sisatovac123";
        Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
    }
    
    public int insertPackageH(final int packageType) {
        final int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, idCity);
        final int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
        final int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
        final String username = "rope";
        final String firstName = "Pero";
        final String lastName = "Simic";
        final String password = "tralalalala123";
        Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
        final BigDecimal weight = new BigDecimal(0.2);
        return this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
    }
    
    @Test
    public void insertPackage() {
        final int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, idCity);
        final int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
        final int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
        final String username = "rope";
        final String firstName = "Pero";
        final String lastName = "Simic";
        final String password = "tralalalala123";
        Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
        final BigDecimal weight = new BigDecimal(0.2);
        final int packageType = 1;
        Assert.assertNotEquals(-1L, this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight));
    }
    
    @Test
    public void insertTransportOffer() {
        final int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, idCity);
        final int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
        final int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
        final String username = "rope";
        final String firstName = "Pero";
        final String lastName = "Simic";
        final String password = "tralalalala123";
        Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
        final BigDecimal weight = new BigDecimal(0.2);
        final int packageType = 1;
        final int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
        Assert.assertNotEquals(-1L, idPackage);
        final String usernameCourier = "Alpi";
        final String firstNameCourier = "Pero";
        final String lastNameCourier = "Simic";
        final String passwordCourier = "tralalalala123";
        Assert.assertTrue(this.userOperations.insertUser(usernameCourier, firstNameCourier, lastNameCourier, passwordCourier));
        final String licencePlate = "BG213KH";
        final int fuelType = 1;
        final BigDecimal fuelConsumption = new BigDecimal(12.3);
        Assert.assertTrue(this.vehicleOperations.insertVehicle(licencePlate, fuelType, fuelConsumption));
        Assert.assertTrue(this.courierRequestOperation.insertCourierRequest(usernameCourier, licencePlate));
        Assert.assertTrue(this.courierRequestOperation.grantRequest(usernameCourier));
        final BigDecimal pricePercentage = new BigDecimal(3.3);
        Assert.assertNotEquals(-1L, this.packageOperations.insertTransportOffer(usernameCourier, idPackage, pricePercentage));
    }
    
    @Test
    public void acceptAnOffer() {
        final int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, idCity);
        final int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
        final int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
        final String username = "rope";
        final String firstName = "Pero";
        final String lastName = "Simic";
        final String password = "tralalalala123";
        Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
        final BigDecimal weight = new BigDecimal(0.2);
        final int packageType = 1;
        final int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
        Assert.assertNotEquals(-1L, idPackage);
        final String usernameCourier = "Alpi";
        final String firstNameCourier = "Pero";
        final String lastNameCourier = "Simic";
        final String passwordCourier = "tralalalala123";
        Assert.assertTrue(this.userOperations.insertUser(usernameCourier, firstNameCourier, lastNameCourier, passwordCourier));
        final String licencePlate = "BG213KH";
        final int fuelType = 1;
        final BigDecimal fuelConsumption = new BigDecimal(12.3);
        Assert.assertTrue(this.vehicleOperations.insertVehicle(licencePlate, fuelType, fuelConsumption));
        Assert.assertTrue(this.courierRequestOperation.insertCourierRequest(usernameCourier, licencePlate));
        Assert.assertTrue(this.courierRequestOperation.grantRequest(usernameCourier));
        final BigDecimal pricePercentage = new BigDecimal(3.3);
        final int offerId = this.packageOperations.insertTransportOffer(usernameCourier, idPackage, pricePercentage);
        Assert.assertNotEquals(-1L, offerId);
        Assert.assertTrue(this.packageOperations.acceptAnOffer(offerId));
    }
    
    @Test
    public void getAllOffers() {
        final int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, idCity);
        final int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
        final int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
        final String username = "rope";
        final String firstName = "Pero";
        final String lastName = "Simic";
        final String password = "tralalalala123";
        Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
        final BigDecimal weight = new BigDecimal(0.2);
        final int packageType = 1;
        final int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
        Assert.assertNotEquals(-1L, idPackage);
        final String usernameCourier = "Alpi";
        final String firstNameCourier = "Pero";
        final String lastNameCourier = "Simic";
        final String passwordCourier = "tralalalala123";
        Assert.assertTrue(this.userOperations.insertUser(usernameCourier, firstNameCourier, lastNameCourier, passwordCourier));
        final String licencePlate = "BG213KH";
        final int fuelType = 1;
        final BigDecimal fuelConsumption = new BigDecimal(12.3);
        Assert.assertTrue(this.vehicleOperations.insertVehicle(licencePlate, fuelType, fuelConsumption));
        Assert.assertTrue(this.courierRequestOperation.insertCourierRequest(usernameCourier, licencePlate));
        Assert.assertTrue(this.courierRequestOperation.grantRequest(usernameCourier));
        final BigDecimal pricePercentage = new BigDecimal(3.3);
        final int offerId = this.packageOperations.insertTransportOffer(usernameCourier, idPackage, pricePercentage);
        Assert.assertNotEquals(-1L, offerId);
        Assert.assertEquals(1L, this.packageOperations.getAllOffers().size());
    }
    
    @Test
    public void getAllOffersForPackage() {
        final int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, idCity);
        final int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
        final int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
        final String username = "rope";
        final String firstName = "Pero";
        final String lastName = "Simic";
        final String password = "tralalalala123";
        Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
        final BigDecimal weight = new BigDecimal(0.2);
        final int packageType = 1;
        final int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
        Assert.assertNotEquals(-1L, idPackage);
        final String usernameCourier = "Alpi";
        final String firstNameCourier = "Pero";
        final String lastNameCourier = "Simic";
        final String passwordCourier = "tralalalala123";
        Assert.assertTrue(this.userOperations.insertUser(usernameCourier, firstNameCourier, lastNameCourier, passwordCourier));
        final String licencePlate = "BG213KH";
        final int fuelType = 1;
        final BigDecimal fuelConsumption = new BigDecimal(12.3);
        Assert.assertTrue(this.vehicleOperations.insertVehicle(licencePlate, fuelType, fuelConsumption));
        Assert.assertTrue(this.courierRequestOperation.insertCourierRequest(usernameCourier, licencePlate));
        Assert.assertTrue(this.courierRequestOperation.grantRequest(usernameCourier));
        final BigDecimal pricePercentage = new BigDecimal(3.3);
        final int offerId = this.packageOperations.insertTransportOffer(usernameCourier, idPackage, pricePercentage);
        Assert.assertNotEquals(-1L, offerId);
        Assert.assertEquals(1L, this.packageOperations.getAllOffersForPackage(idPackage).size());
    }
    
    @Test
    public void deletePackage() {
        final int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, idCity);
        final int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
        final int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
        final String username = "rope";
        final String firstName = "Pero";
        final String lastName = "Simic";
        final String password = "tralalalala123";
        Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
        final BigDecimal weight = new BigDecimal(0.2);
        final int packageType = 1;
        final int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
        Assert.assertNotEquals(-1L, idPackage);
        Assert.assertTrue(this.packageOperations.deletePackage(idPackage));
    }
    
    @Test
    public void changeWeight() {
        final int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, idCity);
        final int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
        final int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
        final String username = "rope";
        final String firstName = "Pero";
        final String lastName = "Simic";
        final String password = "tralalalala123";
        Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
        final BigDecimal weight = new BigDecimal(0.2);
        final int packageType = 1;
        final int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
        Assert.assertNotEquals(-1L, idPackage);
        Assert.assertTrue(this.packageOperations.changeWeight(idPackage, new BigDecimal(0.4)));
    }
    
    @Test
    public void changeType() {
        final int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, idCity);
        final int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
        final int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
        final String username = "rope";
        final String firstName = "Pero";
        final String lastName = "Simic";
        final String password = "tralalalala123";
        Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
        final BigDecimal weight = new BigDecimal(0.2);
        final int packageType = 1;
        final int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
        Assert.assertNotEquals(-1L, idPackage);
        Assert.assertTrue(this.packageOperations.changeType(idPackage, 2));
        Assert.assertTrue(this.packageOperations.changeType(idPackage, 1));
        Assert.assertTrue(this.packageOperations.changeType(idPackage, 0));
    }
    
    @Test
    public void changeType_wrongType() {
        final int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, idCity);
        final int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
        final int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
        final String username = "rope";
        final String firstName = "Pero";
        final String lastName = "Simic";
        final String password = "tralalalala123";
        Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
        final BigDecimal weight = new BigDecimal(0.2);
        final int packageType = 1;
        final int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
        Assert.assertNotEquals(-1L, idPackage);
        Assert.assertFalse(this.packageOperations.changeType(idPackage, 3));
        Assert.assertFalse(this.packageOperations.changeType(idPackage, -1));
        Assert.assertFalse(this.packageOperations.changeType(idPackage, 323));
    }
    
    @Test
    public void getPriceOfDelivery() {
        final int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, idCity);
        final int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
        final int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
        final String username = "rope";
        final String firstName = "Pero";
        final String lastName = "Simic";
        final String password = "tralalalala123";
        Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
        final BigDecimal weight = new BigDecimal(0.2);
        final int packageType = 1;
        final int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
        Assert.assertNotEquals(-1L, idPackage);
        final String usernameCourier = "Alpi";
        final String firstNameCourier = "Pero";
        final String lastNameCourier = "Simic";
        final String passwordCourier = "tralalalala123";
        Assert.assertTrue(this.userOperations.insertUser(usernameCourier, firstNameCourier, lastNameCourier, passwordCourier));
        final String licencePlate = "BG213KH";
        final int fuelType = 1;
        final BigDecimal fuelConsumption = new BigDecimal(12.3);
        Assert.assertTrue(this.vehicleOperations.insertVehicle(licencePlate, fuelType, fuelConsumption));
        Assert.assertTrue(this.courierRequestOperation.insertCourierRequest(usernameCourier, licencePlate));
        Assert.assertTrue(this.courierRequestOperation.grantRequest(usernameCourier));
        final BigDecimal pricePercentage = new BigDecimal(3.3);
        final int offerId = this.packageOperations.insertTransportOffer(usernameCourier, idPackage, pricePercentage);
        Assert.assertNotEquals(-1L, offerId);
        Assert.assertTrue(this.packageOperations.acceptAnOffer(offerId));
        Assert.assertNotNull(this.packageOperations.getPriceOfDelivery(idPackage));
    }
    
    @Test
    public void getAcceptanceTime() {
        final int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, idCity);
        final int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
        final int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
        final String username = "rope";
        final String firstName = "Pero";
        final String lastName = "Simic";
        final String password = "tralalalala123";
        Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
        final BigDecimal weight = new BigDecimal(0.2);
        final int packageType = 1;
        final int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
        Assert.assertNotEquals(-1L, idPackage);
        final String usernameCourier = "Alpi";
        final String firstNameCourier = "Pero";
        final String lastNameCourier = "Simic";
        final String passwordCourier = "tralalalala123";
        Assert.assertTrue(this.userOperations.insertUser(usernameCourier, firstNameCourier, lastNameCourier, passwordCourier));
        final String licencePlate = "BG213KH";
        final int fuelType = 1;
        final BigDecimal fuelConsumption = new BigDecimal(12.3);
        Assert.assertTrue(this.vehicleOperations.insertVehicle(licencePlate, fuelType, fuelConsumption));
        Assert.assertTrue(this.courierRequestOperation.insertCourierRequest(usernameCourier, licencePlate));
        Assert.assertTrue(this.courierRequestOperation.grantRequest(usernameCourier));
        final BigDecimal pricePercentage = new BigDecimal(3.3);
        final int offerId = this.packageOperations.insertTransportOffer(usernameCourier, idPackage, pricePercentage);
        Assert.assertNotEquals(-1L, offerId);
        Assert.assertTrue(this.packageOperations.acceptAnOffer(offerId));
        Assert.assertNotNull(this.packageOperations.getAcceptanceTime(idPackage));
    }
    
    @Test
    public void getAllPackagesWithSpecificType() {
        final int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, idCity);
        final int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
        final int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
        String username = "rope";
        String firstName = "Pero";
        String lastName = "Simic";
        String password = "tralalalala123";
        Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
        final BigDecimal weight = new BigDecimal(0.2);
        int packageType = 1;
        int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
        Assert.assertNotEquals(-1L, idPackage);
        username = "rope123";
        firstName = "Pero";
        lastName = "Simic";
        password = "tralalalala123";
        Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
        packageType = 2;
        idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
        Assert.assertNotEquals(-1L, idPackage);
        Assert.assertEquals(1L, this.packageOperations.getAllPackagesWithSpecificType(2).size());
        Assert.assertEquals(1L, this.packageOperations.getAllPackagesWithSpecificType(1).size());
    }
    
    @Test
    public void getAllPackages() {
        final int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, idCity);
        final int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
        final int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
        String username = "rope";
        String firstName = "Pero";
        String lastName = "Simic";
        String password = "tralalalala123";
        Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
        final BigDecimal weight = new BigDecimal(0.2);
        int packageType = 1;
        int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
        Assert.assertNotEquals(-1L, idPackage);
        username = "rope123";
        firstName = "Pero";
        lastName = "Simic";
        password = "tralalalala123";
        Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
        packageType = 2;
        idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
        Assert.assertNotEquals(-1L, idPackage);
        Assert.assertEquals(2L, this.packageOperations.getAllPackages().size());
    }
}
