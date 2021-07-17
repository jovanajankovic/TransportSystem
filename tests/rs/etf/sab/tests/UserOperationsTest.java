// 
// Decompiled by Procyon v0.5.36
// 

package rs.etf.sab.tests;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Assert;
import rs.etf.sab.operations.UserOperations;
import rs.etf.sab.operations.GeneralOperations;

public class UserOperationsTest
{
    private GeneralOperations generalOperations;
    private UserOperations userOperations;
    private TestHandler testHandler;
    
    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull(this.testHandler = TestHandler.getInstance());
        Assert.assertNotNull(this.userOperations = this.testHandler.getUserOperations());
        Assert.assertNotNull(this.generalOperations = this.testHandler.getGeneralOperations());
        this.generalOperations.eraseAll();
    }
    
    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void insertUser() {
        final String username = "crno.dete";
        final String firstName = "Svetislav";
        final String lastName = "Kisprdilov";
        final String password = "sisatovac123";
        Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
    }
    
    @Test
    public void declareAdmin() {
        final String username = "rope";
        final String firstName = "Pero";
        final String lastName = "Simic";
        final String password = "tralalalala123";
        this.userOperations.insertUser(username, firstName, lastName, password);
        Assert.assertEquals(0L, this.userOperations.declareAdmin(username));
    }
    
    @Test
    public void declareAdmin_NoSuchUser() {
        Assert.assertEquals(2L, this.userOperations.declareAdmin("Nana"));
    }
    
    @Test
    public void declareAdmin_AlreadyAdmin() {
        final String username = "rope";
        final String firstName = "Pero";
        final String lastName = "Simic";
        final String password = "tralalalala123";
        this.userOperations.insertUser(username, firstName, lastName, password);
        this.userOperations.declareAdmin(username);
        Assert.assertEquals(1L, this.userOperations.declareAdmin(username));
    }
    
    @Test
    public void getSentPackages_userExisting() {
        final String username = "rope";
        final String firstName = "Pero";
        final String lastName = "Simic";
        final String password = "tralalalala123";
        this.userOperations.insertUser(username, firstName, lastName, password);
        Assert.assertEquals(new Integer(0), this.userOperations.getSentPackages(username));
    }
    
    @Test
    public void getSentPackages_userNotExisting() {
        final String username = "rope";
        Assert.assertNull(this.userOperations.getSentPackages(username));
    }
    
    @Test
    public void deleteUsers() {
        final String username1 = "rope";
        final String firstName1 = "Pero";
        final String lastName1 = "Simic";
        final String password1 = "tralalalala123";
        this.userOperations.insertUser(username1, firstName1, lastName1, password1);
        final String username2 = "rope_2";
        final String firstName2 = "Pero";
        final String lastName2 = "Simic";
        final String password2 = "tralalalala321";
        this.userOperations.insertUser(username2, firstName2, lastName2, password2);
        Assert.assertEquals(2L, this.userOperations.deleteUsers(username1, username2));
    }
    
    @Test
    public void getAllUsers() {
        final String username1 = "rope";
        final String firstName1 = "Pero";
        final String lastName1 = "Simic";
        final String password1 = "tralalalala221";
        this.userOperations.insertUser(username1, firstName1, lastName1, password1);
        final String username2 = "rope_2";
        final String firstName2 = "Pero";
        final String lastName2 = "Simic";
        final String password2 = "tralalalala222";
        this.userOperations.insertUser(username2, firstName2, lastName2, password2);
        Assert.assertEquals(2L, this.userOperations.getAllUsers().size());
        Assert.assertTrue(this.userOperations.getAllUsers().contains(username1));
        Assert.assertTrue(this.userOperations.getAllUsers().contains(username2));
    }
}
