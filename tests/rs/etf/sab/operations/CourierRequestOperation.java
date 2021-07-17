// 
// Decompiled by Procyon v0.5.36
// 

package rs.etf.sab.operations;

import java.util.List;

public interface CourierRequestOperation
{
    boolean insertCourierRequest(final String p0, final String p1);
    
    boolean deleteCourierRequest(final String p0);
    
    boolean changeVehicleInCourierRequest(final String p0, final String p1);
    
    List<String> getAllCourierRequests();
    
    boolean grantRequest(final String p0);
}
