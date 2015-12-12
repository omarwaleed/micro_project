import static org.junit.Assert.*;

import org.jetbrains.annotations.TestOnly;
import org.junit.*;

import java.util.Arrays;

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
		assertEquals(11,c.missRate);
    }

    @Test
    public void testHitorMissDirectMappe3() {
    	Cache c2 = new Cache(64, 16, 1, 0, 1);
        String[] s = {"Hello","World","Hello"};
    	c2.writeDM(1*4, s);
    	c2.writeDM(134*4,s);
    	c2.writeDM(212*4, s);
    	c2.writeDM(1*4, s);
    	c2.writeDM(135*4,s);
    	c2.writeDM(213*4,s);
    	c2.writeDM(162*4,s);
    	c2.writeDM(161*4,s);
    	c2.writeDM(2*4,s);
    	c2.writeDM(44*4,s);
    	c2.writeDM(41*4,s);
    	c2.writeDM(221*4,s);
    	
    	assertEquals(2, c2.getHitRate());
		assertEquals(10,c2.missRate);
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
	@Test
	public void testHitorMissDirectMapped2() {
		Cache c1 = new Cache(64, 8, 1, 0, 1);
		String[] s = {"Hello","World","Hello"};
		c1.writeDM(1*4, s);
		c1.writeDM(134*4,s);
		c1.writeDM(212*4, s);
		c1.writeDM(1*4, s);
		c1.writeDM(135*4,s);
		c1.writeDM(213*4,s);
		c1.writeDM(162*4,s);
		c1.writeDM(161*4,s);
		c1.writeDM(2*4,s);
		c1.writeDM(44*4,s);
		c1.writeDM(41*4,s);
		c1.writeDM(221*4,s);

		assertEquals(3, c1.getHitRate());
		assertEquals(9,c1.missRate);
	}
	@Test
	public void MemoryHierarchyALLDM() {
		Cache L1 = new Cache(32,4,1,0,1);
		Cache RAM = new Cache(64,4,16,0,10);
		RAM.setMainMemory(true);
		String[] s = {"Hello","World","Hello"};
		String[] betngan = {"please","find","me"};
		RAM.write(1*4, s);
		RAM.write(134*4,s);
		RAM.write(212*4, s);
		RAM.write(1*4, betngan);
		RAM.write(135*4,s);
		RAM.write(213*4,s);
		RAM.write(162*4,s);
		RAM.write(161*4,s);
		RAM.write(2*4,s);
		RAM.write(44*4,s);
		RAM.write(41*4,s);
		RAM.write(221*4,s);
        System.out.println(Arrays.toString(RAM.getContentOf(2)));
        Processor.iCache.add(L1);
		Processor.iCache.add(RAM);
		assertEquals(11,Processor.cacheAccessWrite(1*4,s,false));
		System.out.println(Arrays.toString(Processor.iCache.get(0).getContentOf(1)));
		assertEquals("Hello,1",Processor.cacheAccesRead(1*4,false));

	}
}
