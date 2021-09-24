import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Weijie Yuan
 */
public class ECHashStringSetTest  {

    @Test
    public void testHashPut() {
        _S.put("tab");
        _S.put("lut");
        _S.put("git");
        _S.put("let");
        assertEquals(new HashSet<>(_S.asList()), new HashSet<>(List.of("git", "let", "lut", "tab")));
    }

    @Test
    public void testHashContains() {
        _S.put("weijie");
        _S.put("git");
        assertTrue(_S.contains("weijie"));
        assertTrue(_S.contains("git"));
        assertFalse(_S.contains("gitlet"));
    }

    private static ECHashStringSet _S = new ECHashStringSet();
}
