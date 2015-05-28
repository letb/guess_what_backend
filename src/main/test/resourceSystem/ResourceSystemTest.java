package resourceSystem;

import classesForTests.ResourceTest;
import org.junit.Test;
import resource.ResourceFactory;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class ResourceSystemTest {

    @Test
    public void testWork() throws Exception {
        ResourceFactory resourceFactory = ResourceFactory.instance();

        ResourceTest exist = (ResourceTest) resourceFactory.getResource("src/main/test/data/exist");
        assertNotNull(exist);
        assertEquals(20, exist.getIntField());
        assertEquals("hello world!", exist.getStrField());

        assertNull(resourceFactory.getResource("src/main/test/data/notExist"));
    }
}
