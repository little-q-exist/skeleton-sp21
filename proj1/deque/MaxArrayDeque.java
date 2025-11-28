package deque;

import java.util.Comparator;
import java.util.Iterator;

public class MaxArrayDeque<Item> extends ArrayDeque<Item> {
    private final Comparator<Item> comparator;
    public MaxArrayDeque(Comparator<Item> c) {
        comparator = c;
    }

    public Item max() {
        return max(comparator);
    }

    public Item max(Comparator<Item> c) {
        if (isEmpty()) {
            return null;
        }
        Iterator<Item> it = iterator();
        Item maxItem = it.next();
        while (it.hasNext()) {
            Item item = it.next();
            if (c.compare(item, maxItem) > 0) {
                maxItem = item;
            }
        }
        return maxItem;
    }
}
