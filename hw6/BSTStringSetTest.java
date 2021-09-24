import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Weijie Yuan
 */
public class BSTStringSetTest  {

    @Test
    public void testPut() {
        _S.put("tab");
        _S.put("lut");
        _S.put("git");
        _S.put("let");
        assertEquals(_S.asList(), new ArrayList<>(List.of("git", "let", "lut", "tab")));
    }

    @Test
    public void testContains() {
        _S.put("weijie");
        _S.put("git");
        assertTrue(_S.contains("weijie"));
        assertTrue(_S.contains("git"));
        assertFalse(_S.contains("gitlet"));
    }

    private static BSTStringSet _S = new BSTStringSet();
}
