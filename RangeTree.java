import java.util.List;

public class RangeTree {
    private OrderedDeletelessDictionary<Double, Range> byStart;
    private OrderedDeletelessDictionary<Double, Range> byEnd;
    private int size;

    public RangeTree() {
        byStart = new AVLTree<>();
        byEnd = new AVLTree<>();
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // Return the Range which starts at the given time
    // The running time is O(log n)
    public Range findByStart(Double start) {
        return byStart.find(start);
    }

    // Return the Range which ends at the given time
    // The running time is O(log n)
    public Range findByEnd(Double end) {
        return byEnd.find(end);
    }

    // Gives a list of Ranges sorted by start time.
    // Useful for testing and debugging.
    // The running time is O(n), so it should not
    // be used for implementing other methods.
    public List<Range> getRanges() {
        return byStart.getValues();
    }

    // Gives a sorted list of start times.
    // Useful for testing and debugging.
    // The running time is O(n), so it should not
    // be used for implementing other methods.
    public List<Double> getStartTimes() {
        return byStart.getKeys();
    }

    // Gives a sorted list of end times.
    // Useful for testing and debugging.
    // The running time is O(n), so it should not
    // be used for implementing other methods.
    public List<Double> getEndTimes() {
        return byEnd.getKeys();
    }

    // Identifies whether or not the given range conflicts with any
    // ranges that are already in the data structure.
    // If the data structure is empty, then it does not conflict
    // with any ranges, so we should return false.
    // The running time of this method should be O(log n)
    public boolean hasConflict(Range query) {
        if (size == 0) return false;  // If the tree is empty
        Double start = byStart.findPrevKey(query.start);  // Find the largest start time less than the query start
        Double end = byEnd.findNextKey(query.end);  // Find the smallest end time greater than the query end

        if (start != null) {
            Range startRange = byStart.find(start);  // Get the range that ends just before the query starts
            if (startRange.end > query.start) {
                return true;  // If it overlaps with the query
            }
        }
        if (end != null) {
            Range endRange = byEnd.find(end);  // Get the range that starts just after the query ends
            if (endRange.start < query.end) {
                return true;  // If it overlaps with the query
            }
        }
        // If there are ranges in the tree but both checks returned null
        if (start == null && end == null && size != 0){
            return true;
        }
        return false;  // If no conflicts are found
    }

    // Inserts the given range into the data structure if it has no conflict.
    // Does not modify the data structure if it does have a conflict.
    // Return value indicates whether or not the item was successfully
    // added to the data structure.
    // Running time should be O(log n)
    public boolean insert (Range query){
        if (hasConflict(query)) {  // Check if the query conflicts with any existing ranges
            return false;
        }
        byStart.insert(query.start, query);  // Insert the range into byStart dictionary
        byEnd.insert(query.end, query);  // Insert the range into byEnd dictionary
        size++;  // Increment the size of the tree
        return true;

    }

}
