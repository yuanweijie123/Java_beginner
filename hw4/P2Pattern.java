/** P2Pattern class
 *  @author Josh Hug & Vivant Sakore
 */

public class P2Pattern {
    /* Pattern to match a valid date of the form MM/DD/YYYY. Eg: 9/22/2019 */
    public static final String P1 = "(0?[1-9]|1[012])\\/(0?[1-9]|[12][0-9]|3[01])\\/(19\\d{2}|2\\d{3})";

    /* Pattern to match 61b notation for literal IntLists. */
    public static final String P2 = "\\((\\d+\\,\\s+)+\\d+\\)";

    /* Pattern to match a valid domain name. Eg: www.support.facebook-login.com */
    public static final String P3 = "^(?![\\.\\-])(?:[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?\\.)+[a-z0-9][a-z0-9-]{0,6}[a-z0-9](?<![\\.\\-])$";

    /* Pattern to match a valid java variable name. Eg: _child13$ */
    public static final String P4 = "^(?!\\d)[\\w$]+";

    /* Pattern to match a valid IPv4 address. Eg: 127.0.0.1 */
    public static final String P5 = "((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.)){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

}
