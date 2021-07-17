// 
// Decompiled by Procyon v0.5.36
// 

package rs.etf.sab.operations;

import java.util.List;

public interface CityOperations
{
    int insertCity(final String p0, final String p1);
    
    int deleteCity(final String... p0);
    
    boolean deleteCity(final int p0);
    
    List<Integer> getAllCities();
}
