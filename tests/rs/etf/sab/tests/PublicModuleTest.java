// 
// Decompiled by Procyon v0.5.36
// 

package rs.etf.sab.tests;

import org.junit.Test;
import java.math.BigDecimal;
import org.junit.After;
import org.junit.Before;
import org.junit.Assert;

public class PublicModuleTest
{
    private TestHandler testHandler;
    
    @Before
    public void setUp() {
        Assert.assertNotNull(this.testHandler = TestHandler.getInstance());
        Assert.assertNotNull(this.testHandler.getGeneralOperations());
        this.testHandler.getGeneralOperations().eraseAll();
    }
    
    @After
    public void tearUp() {
        this.testHandler.getGeneralOperations().eraseAll();
    }
    
    @Test
    public void publicOne() {
        final String courierLastName = "Ckalja";
        final String courierFirstName = "Pero";
        final String courierUsername = "perkan";
        String password = "sabi2018";
        this.testHandler.getUserOperations().insertUser(courierUsername, courierFirstName, courierLastName, password);
        final String licencePlate = "BG323WE";
        final int fuelType = 0;
        final BigDecimal fuelConsumption = new BigDecimal(8.3);
        this.testHandler.getVehicleOperations().insertVehicle(licencePlate, fuelType, fuelConsumption);
        this.testHandler.getCourierRequestOperation().insertCourierRequest(courierUsername, licencePlate);
        this.testHandler.getCourierRequestOperation().grantRequest(courierUsername);
        Assert.assertTrue(this.testHandler.getCourierOperations().getAllCouriers().contains(courierUsername));
        final String senderUsername = "masa";
        final String senderFirstName = "Masana";
        final String senderLastName = "Leposava";
        password = "lepasampasta1";
        this.testHandler.getUserOperations().insertUser(senderUsername, senderFirstName, senderLastName, password);
        final int cityId = this.testHandler.getCityOperations().insertCity("Novo Milosevo", "21234");
        final int cordXd1 = 10;
        final int cordYd1 = 2;
        final int districtIdOne = this.testHandler.getDistrictOperations().insertDistrict("Novo Milosevo", cityId, cordXd1, cordYd1);
        final int cordXd2 = 2;
        final int cordYd2 = 10;
        final int districtIdTwo = this.testHandler.getDistrictOperations().insertDistrict("Vojinovica", cityId, cordXd2, cordYd2);
        final int type1 = 0;
        final BigDecimal weight1 = new BigDecimal(123);
        final int packageId1 = this.testHandler.getPackageOperations().insertPackage(districtIdOne, districtIdTwo, courierUsername, type1, weight1);
        final BigDecimal packageOnePrice = Util.getPackagePrice(type1, weight1, Util.euclidean(cordXd1, cordYd1, cordXd2, cordYd2), new BigDecimal(5));
        int offerId = this.testHandler.getPackageOperations().insertTransportOffer(courierUsername, packageId1, new BigDecimal(5));
        this.testHandler.getPackageOperations().acceptAnOffer(offerId);
        final int type2 = 1;
        final BigDecimal weight2 = new BigDecimal(321);
        final int packageId2 = this.testHandler.getPackageOperations().insertPackage(districtIdTwo, districtIdOne, courierUsername, type2, weight2);
        final BigDecimal packageTwoPrice = Util.getPackagePrice(type2, weight2, Util.euclidean(cordXd1, cordYd1, cordXd2, cordYd2), new BigDecimal(5));
        offerId = this.testHandler.getPackageOperations().insertTransportOffer(courierUsername, packageId2, new BigDecimal(5));
        this.testHandler.getPackageOperations().acceptAnOffer(offerId);
        final int type3 = 1;
        final BigDecimal weight3 = new BigDecimal(222);
        final int packageId3 = this.testHandler.getPackageOperations().insertPackage(districtIdTwo, districtIdOne, courierUsername, type3, weight3);
        final BigDecimal packageThreePrice = Util.getPackagePrice(type3, weight3, Util.euclidean(cordXd1, cordYd1, cordXd2, cordYd2), new BigDecimal(5));
        offerId = this.testHandler.getPackageOperations().insertTransportOffer(courierUsername, packageId3, new BigDecimal(5));
        this.testHandler.getPackageOperations().acceptAnOffer(offerId);
        Assert.assertEquals(1L, (long)this.testHandler.getPackageOperations().getDeliveryStatus(packageId1));
        Assert.assertEquals(packageId1, this.testHandler.getPackageOperations().driveNextPackage(courierUsername));
        Assert.assertEquals(3L, (long)this.testHandler.getPackageOperations().getDeliveryStatus(packageId1));
        Assert.assertEquals(2L, (long)this.testHandler.getPackageOperations().getDeliveryStatus(packageId2));
        Assert.assertEquals(packageId2, this.testHandler.getPackageOperations().driveNextPackage(courierUsername));
        Assert.assertEquals(3L, (long)this.testHandler.getPackageOperations().getDeliveryStatus(packageId2));
        Assert.assertEquals(2L, (long)this.testHandler.getPackageOperations().getDeliveryStatus(packageId3));
        Assert.assertEquals(packageId3, this.testHandler.getPackageOperations().driveNextPackage(courierUsername));
        Assert.assertEquals(3L, (long)this.testHandler.getPackageOperations().getDeliveryStatus(packageId3));
        final BigDecimal gain = packageOnePrice.add(packageTwoPrice).add(packageThreePrice);
        final BigDecimal loss = new BigDecimal(Util.euclidean(cordXd1, cordYd1, cordXd2, cordYd2) * 4.0 * 15.0).multiply(fuelConsumption);
        final BigDecimal actual = this.testHandler.getCourierOperations().getAverageCourierProfit(0);
        Assert.assertTrue(gain.subtract(loss).compareTo(actual.multiply(new BigDecimal(1.001))) < 0);
        Assert.assertTrue(gain.subtract(loss).compareTo(actual.multiply(new BigDecimal(0.999))) > 0);
    }
}
