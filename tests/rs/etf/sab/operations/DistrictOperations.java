// 
// Decompiled by Procyon v0.5.36
// 

package rs.etf.sab.operations;

import java.util.List;

public interface DistrictOperations
{
    int insertDistrict(final String p0, final int p1, final int p2, final int p3);
    
    int deleteDistricts(final String... p0);
    
    boolean deleteDistrict(final int p0);
    
    int deleteAllDistrictsFromCity(final String p0);
    
    List<Integer> getAllDistrictsFromCity(final int p0);
    
    List<Integer> getAllDistricts();
}
