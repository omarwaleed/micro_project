import static org.junit.Assert.*;
import org.junit.Test;

public class CacheTest {
 
    @Test
    public void testHitorMissDirectMapped() {
    	Cache c = new Cache(32, 4, 1, 0, 1);
        String[] s = {"Hello","World"};
    	c.writeDM(1*4, s);
    	c.writeDM(134*4,s);
    	c.writeDM(212*4, s);
    	c.writeDM(1*4, s);
    	c.writeDM(135*4,s);
    	c.writeDM(213*4,s);
    	c.writeDM(162*4,s);
    	c.writeDM(161*4,s);
    	c.writeDM(2*4,s);
    	c.writeDM(44*4,s);
    	c.writeDM(41*4,s);
    	c.writeDM(221*4,s);
    	
    	assertEquals(1, c.getHitRate());
    }
    @Test
    public void testHitorMissDirectMapped2() {
    	Cache c = new Cache(32, 8, 1, 0, 1);
        String[] s = {"Hello","World","Hello"};
    	c.writeDM(1*4, s);
    	c.writeDM(134*4,s);
    	c.writeDM(212*4, s);
    	c.writeDM(1*4, s);
    	c.writeDM(135*4,s);
    	c.writeDM(213*4,s);
    	c.writeDM(162*4,s);
    	c.writeDM(161*4,s);
    	c.writeDM(2*4,s);
    	c.writeDM(44*4,s);
    	c.writeDM(41*4,s);
    	c.writeDM(221*4,s);
    	
    	assertEquals(3, c.getHitRate());
    }
    @Test
    public void testHitorMissDirectMappe3() {
    	Cache c = new Cache(32, 16, 1, 0, 1);
        String[] s = {"Hello","World","Hello"};
    	c.writeDM(1*4, s);
    	c.writeDM(134*4,s);
    	c.writeDM(212*4, s);
    	c.writeDM(1*4, s);
    	c.writeDM(135*4,s);
    	c.writeDM(213*4,s);
    	c.writeDM(162*4,s);
    	c.writeDM(161*4,s);
    	c.writeDM(2*4,s);
    	c.writeDM(44*4,s);
    	c.writeDM(41*4,s);
    	c.writeDM(221*4,s);
    	
    	assertEquals(2, c.getHitRate());
    }
    @Test 
    public void testDivide() {
    	Cache c = new Cache(32, 4, 1, 0, 1);
    	int [] expected = {0,1,0};
        assertArrayEquals(expected,c.divide(4));
    	
    }
    @Test
    public void testDivide2() {
    	Cache c = new Cache(32, 4, 1, 0, 1);
    	 int [] expected1 = {2,9,0};
         assertArrayEquals(expected1,c.divide(41*4));
    }
    
    @Test
    public void testDivide3() {
    	Cache c = new Cache(32, 4, 1, 0, 1);
    	 int [] expected1 = {13,13,0};
         assertArrayEquals(expected1,c.divide(221*4));
    }
   @Test 
   public void testRead() {
	   Cache c = new Cache(32,4,1,0,1);
	   String[] s = {"Hello","World"};
	   c.writeDM(1*4, s);
   	   c.writeDM(134*4,s);
   	   c.writeDM(212*4, s);
   	   c.writeDM(1*4, s);
   	   c.writeDM(135*4,s);
   	   c.writeDM(213*4,s);
   	   c.writeDM(162*4,s);
   	   c.writeDM(161*4,s);
   	   c.writeDM(2*4,s);
   	   c.writeDM(44*4,s);
   	   c.writeDM(41*4,s);
   	   c.writeDM(221*4,s);
//   	   String[] expected = {"Hello","World","13","1"};
//   	   assertEquals(expected,c.readDM(221*4));
   	   
   	String[] expected1 = {"Hello","World","2","1"};
       assertEquals(expected1,c.readDM(41*4));
       
       assertNull(c.readDM(800*4));
   	   
   }
}
