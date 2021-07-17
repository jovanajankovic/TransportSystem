// 
// Decompiled by Procyon v0.5.36
// 

package rs.etf.sab.operations;

import java.util.List;

public interface UserOperations
{
    boolean insertUser(final String p0, final String p1, final String p2, final String p3);
    
    int declareAdmin(final String p0);
    
    Integer getSentPackages(final String... p0);
    
    int deleteUsers(final String... p0);
    
    List<String> getAllUsers();
}
