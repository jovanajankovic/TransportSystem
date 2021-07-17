// 
// Decompiled by Procyon v0.5.36
// 

package rs.etf.sab.operations;

import java.sql.Date;
import java.util.List;
import java.math.BigDecimal;

public interface PackageOperations
{
    int insertPackage(final int p0, final int p1, final String p2, final int p3, final BigDecimal p4);
    
    int insertTransportOffer(final String p0, final int p1, final BigDecimal p2);
    
    boolean acceptAnOffer(final int p0);
    
    List<Integer> getAllOffers();
    
    List<Pair<Integer, BigDecimal>> getAllOffersForPackage(final int p0);
    
    boolean deletePackage(final int p0);
    
    boolean changeWeight(final int p0, final BigDecimal p1);
    
    boolean changeType(final int p0, final int p1);
    
    Integer getDeliveryStatus(final int p0);
    
    BigDecimal getPriceOfDelivery(final int p0);
    
    Date getAcceptanceTime(final int p0);
    
    List<Integer> getAllPackagesWithSpecificType(final int p0);
    
    List<Integer> getAllPackages();
    
    List<Integer> getDrive(final String p0);
    
    int driveNextPackage(final String p0);
    
    public interface Pair<A, B>
    {
        A getFirstParam();
        
        B getSecondParam();
        
        default boolean equals(final Pair a, final Pair b) {
            return a.getFirstParam().equals(b.getFirstParam()) && a.getSecondParam().equals(b.getSecondParam());
        }
    }
}
