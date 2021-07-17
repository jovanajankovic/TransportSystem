// 
// Decompiled by Procyon v0.5.36
// 

package rs.etf.sab.operations;

import java.util.List;
import java.math.BigDecimal;

public interface VehicleOperations
{
    boolean insertVehicle(final String p0, final int p1, final BigDecimal p2);
    
    int deleteVehicles(final String... p0);
    
    List<String> getAllVehichles();
    
    boolean changeFuelType(final String p0, final int p1);
    
    boolean changeConsumption(final String p0, final BigDecimal p1);
}
