import static org.junit.Assert.*;

import org.jetbrains.annotations.TestOnly;
import org.junit.*;

public class CacheTest {
 
    @Test
    public void testHitorMissDirectMapped() {
    	Cache c = new Cache(64, 4, 1, 0, 1);
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
    	Cache c = new Cache(64, 8, 1, 0, 1);
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
    	Cache c = new Cache(64, 16, 1, 0, 1);
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
    	Cache c = new Cache(64, 4, 1, 0, 1);
    	int [] expected = {0,1,0};
        assertArrayEquals(expected,c.divide(4));
    	
    }
    @Test
    public void testDivide2() {
    	Cache c = new Cache(64, 4, 1, 0, 1);
    	 int [] expected1 = {2,9,0};
         assertArrayEquals(expected1,c.divide(41*4));
    }
    
    @Test
    public void testDivide3() {
    	Cache c = new Cache(64, 4, 1, 0, 1);
    	 int [] expected1 = {13,13,0};
         assertArrayEquals(expected1,c.divide(221*4));
    }

//	@Test
//	public void testMemoryHDMWRTH(){ // direct map write through
//		Cache c1 = new Cache(32, 4, 1, 0, 1);
//		Cache c2 = new Cache(64, 4, 1, 0, 4);
//		Cache main = new Cache(128,4,128,0,10);
//		String[] s = {"Hello","World"};
//		c1.write(1*4, s);
//		c1.write(134*4,s);
//		c1.write(212*4, s);
//		c1.write(1*4, s);
//		c1.write(135*4,s);
//		c1.write(213*4,s);
//		c1.write(162*4,s);
//		c1.write(161*4,s);
//		c1.write(2*4,s);
//		c1.write(44*4,s);
//		c1.write(41*4,s);
//		c1.write(221*4,s);
//
//		c2.write(1*4, s);
//		c2.write(134*4,s);
//		c2.write(212*4, s);
//		c2.write(1*4, s);
//		c2.write(135*4,s);
//		c2.write(213*4,s);
//		c2.write(162*4,s);
//		c2.write(161*4,s);
//		c2.write(2*4,s);
//		c2.write(44*4,s);
//		c2.write(41*4,s);
//		c2.write(221*4,s);
//		Processor.getCacheLevel().add(c1);
//		Processor.getCacheLevel().add(c2);
//		Processor.getCacheLevel().add(main);
//
//		//assertEquals(3,c1.getHitRate());
//
//	}
   @Test
   public void testRead() {
	   Cache c = new Cache(64, 4, 1, 0, 1);
	   String[] s = {"Hello", "World"};
	   c.writeDM(1 * 4, s);
	   c.writeDM(134 * 4, s);
	   c.writeDM(212 * 4, s);
	   c.writeDM(1 * 4, s);
	   c.writeDM(135 * 4, s);
	   c.writeDM(213 * 4, s);
	   c.writeDM(162 * 4, s);
	   c.writeDM(161 * 4, s);
	   c.writeDM(2 * 4, s);
	   c.writeDM(44 * 4, s);
	   c.writeDM(41 * 4, s);
	   c.writeDM(221 * 4, s);
   	   String[] expected = {"Hello","World","13","1"};
   	   assertEquals(expected,c.readDM(221*4));

	   String[] expected1 = {"Hello", "World", "2", "1"};
	   assertEquals(expected1, c.readDM(41 * 4));

	   assertNull(c.readDM(800 * 4));

   }
}
