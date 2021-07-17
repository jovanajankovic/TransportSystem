// 
// Decompiled by Procyon v0.5.36
// 

package rs.etf.sab.operations;

import java.math.BigDecimal;
import java.util.List;

public interface CourierOperations
{
    boolean insertCourier(final String p0, final String p1);
    
    boolean deleteCourier(final String p0);
    
    List<String> getCouriersWithStatus(final int p0);
    
    List<String> getAllCouriers();
    
    BigDecimal getAverageCourierProfit(final int p0);
}
