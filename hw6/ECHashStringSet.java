import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

/** A set of String values.
 *  @author Weijie Yuan
 */
class ECHashStringSet implements StringSet {

    public ECHashStringSet() {
        _size = 0;
        double MIN_LOAD = 0.2;
        _data = new LinkedList[(int) (1 / MIN_LOAD)];
    }

    private double load(){
        return ((double) _size) / ((double) _data.length);
    }

    private void resize(){
        LinkedList<String>[] oldList = _data;
        _data = new LinkedList[2 * oldList.length];
        _size = 0;

        for(LinkedList<String> list : oldList) {
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    this.put(list.get(i));
                }
            }
        }
    }

    @Override
    public void put(String s) {
        if(s != null) {
            double MAX_LOAD = 5;
            if(load() > MAX_LOAD)
                resize();

            int pos = (s.hashCode() & 0x7fffffff) % _data.length;
            if(_data[pos] == null)
                _data[pos] = new LinkedList<>();
            _data[pos].add(s);
            _size = _size + 1;
        }
    }

    @Override
    public boolean contains(String s) {
        if(s != null){
            int pos = (s.hashCode() & 0x7fffffff) % _data.length;
            if(_data[pos] == null)
                return false;
            else
                return _data[pos].contains(s);
        }
        else {
            return false;
        }
    }

    @Override
    public List<String> asList() {
        List<String> res = new ArrayList<>();
        for(LinkedList<String> list : _data) {
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    res.add(list.get(i));
                }
            }
        }
        return res;
    }

    private LinkedList<String>[] _data;
    private int _size;
}
