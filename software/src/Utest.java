import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.Times;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class Utest {
	public Product   p;
	
	@InjectMocks
	private DAOImpl daoImpl;
	@Mock
	private  Connection con;
	@Mock 
    private  PreparedStatement psmt;
	

	//-----------------------------------------
   //1- 
      //a. constructor TEST
    @Before
    public void setup() {p = new Product(24);}

    @Test
	 public void Product () throws SQLException, DAOException {
     //P IS PUPLIC VARIABLE WITH ID=24
     assertEquals(24, p.getId());

	 }
	//---------------------------------------------------------------
    
       //b.setters TESTS
	@Test 
	public void setType() {
		
		p.setType("mobile");
		assertTrue(p.getType().equals("mobile"));

	}
	
	
	@Test 
	
	public void setManfacturer(){
		p.setManufacturer("sony");
		assertTrue(p.getManufacturer().equals("sony"));
		
	}
	
	@Test
	public void setProductionDate(){
		p.setProductionDate("2010");
		assertTrue(p.getProductionDate().equals("2010"));	
	}
	
	
	@Test 
	public void setExpirydate (){
		p.setExpiryDate("2012");
		assertTrue(p.getExpiryDate().equals("2012"));
		
	}
	//---------------------------------------------------------------------------------------------------------
	//2-test Method insertProduct
	
	//a. exception case 
		@Test(expected = DAOException.class)
		public void insertproduct_exception()throws SQLException,DAOException
		{
			when(con.prepareStatement(anyString())).thenReturn(psmt);
			when (psmt.executeUpdate()).thenThrow(new SQLException());
			Product N=new Product(26);
			daoImpl.insertProduct(N);
			
		}
	
	//b.happycase
	@Test //happy case insert (argument capture + assert statements)
	 public void insertProduct()throws SQLException, DAOException{
		 
		when(con.prepareStatement(anyString())).thenReturn(psmt);
		when(psmt.executeUpdate()).thenReturn(1);
		Product N = new Product(25);
		N.setType ("TV");
		N.setManufacturer("LG");
		N.setProductionDate("22");
		N.setExpiryDate("28");
		daoImpl.insertProduct(N);
		
		verify (psmt,times(1)).executeUpdate();
		ArgumentCaptor<Integer> idCaptor= ArgumentCaptor.forClass(Integer.class); 
	    verify(psmt,times(1)).setInt(anyInt(), idCaptor.capture());;
	    Assert.assertTrue(idCaptor.getAllValues().get(0).equals(25));
		ArgumentCaptor<String>Captor= ArgumentCaptor.forClass(String.class); 
	    verify(psmt,times(4)).setString(anyInt(), Captor.capture());
	    Assert.assertTrue(Captor.getAllValues().get(0).equals("TV"));
	    Assert.assertTrue(Captor.getAllValues().get(1).equals("LG"));
	    Assert.assertTrue(Captor.getAllValues().get(2).equals("22"));
	    Assert.assertTrue(Captor.getAllValues().get(3).equals("28"));

	}
	
	//-----------------------------------------------------------------------------
	//integration Test for happy case//no Mocks
	
	@Test
	public void deleteProduct() throws SQLException, DAOException{
		
		  DAOImpl L= new DAOImpl();
		  Product K=new Product(54);// insert int id 
		  L.insertProduct(K); //pass If new record only, but if the recored is already exist we just use getproduct
		  Product m= L.getProduct(K.getId());
          Assert.assertNotNull(m);
		  L.deleteProduct(54);//insert int id 
		  Product R= L.getProduct(K.getId());
		  Assert.assertNull(R);
         

	}
	
	
	
	
	
	
	
}
