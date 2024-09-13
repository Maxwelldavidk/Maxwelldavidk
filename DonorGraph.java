import java.util.*;

public class DonorGraph {
    private List<List<Match>> adjList;

    // The donatingTo array indicates which repient each donor is
    // affiliated with. Specifically, the donor at index i has volunteered
    // to donate a kidney on behalf of recipient donatingTo[i].
    // The matchScores 2d array gives the match scores associated with each
    // donor-recipient pair. Specificically, matchScores[x][y] gives the
    // HLA score for donor x and reciplient y.
    // Constructor to create the donor graph based on donor-to-beneficiary mappings and match scores.
    // donorToBenefit array indicates the recipient each donor is affiliated with.
    // matchScores array holds the HLA scores for each donor-recipient pair.
    public DonorGraph(int[] donorToBenefit, int[][] matchScores) {

        adjList = new ArrayList<>();
        for(int i = 0; i < matchScores[0].length; i++) {
            adjList.add(new ArrayList<>());
        }
        for(int donor = 0; donor < donorToBenefit.length; donor++) {
            for(int recepient = 0; recepient < matchScores[donor].length; recepient++) {
                if(matchScores[donor][recepient] >= 60) {
                    adjList.get(donorToBenefit[donor]).add(new Match(donor, donorToBenefit[donor], recepient));
                }
            }
        }
    }

    // Will be used by the autograder to verify your graph's structure.
    // It's probably also going to helpful for your debugging.
    public boolean isAdjacent(int start, int end) {
        for (Match m : adjList.get(start)) {
            if (m.recipient == end)
                return true;
        }
        return false;
    }

    // Will be used by the autograder to verify your graph's structure.
    // It's probably also going to helpful for your debugging.
    public int getDonor(int beneficiary, int recipient) {
        for (Match m : adjList.get(beneficiary)) {
            if (m.recipient == recipient)
                return m.donor;
        }
        return -1;
    }


    // returns a chain of matches to make a donor cycle
    // which includes the given recipient.
    // Returns an empty list if no cycle exists.
    // find a cycle in the graph starting from a given recipient.
    // Returns a list of matches forming the cycle or an empty list if no cycle exists.
    public List<Match> findCycle(int recipient) {

        Stack<Match> stack = new Stack<>();
        List<Match> result = new ArrayList<>();
        HashSet<Integer> visited = new HashSet<>();
        if (findCycleDfs(recipient, visited, stack, recipient)){
            while(!stack.isEmpty()) {
                result.add(stack.pop());
            }
            Collections.reverse(result);
        }
        return result;
    }
    // DFS to detect a cycle starting and ending at the target node.
    // Utilizes a stack to trace the cycle path and a visited set to avoid revisits.
    private boolean findCycleDfs(int current, HashSet<Integer> visited, Stack<Match> stack, int target) {
        if (visited.contains(current)){
            return current == target;
        }
        visited.add(current);
        for (Match match : adjList.get(current)) {
            stack.push(match);
            if (findCycleDfs(match.recipient, visited, stack, target)) {
                return true;
            }
            stack.pop();
        }
        return false;
    }
    // returns true or false to indicate whether there
    // is some cycle which includes the given recipient.
    // determine if any cycle exists that includes a specific recipient.
    public boolean hasCycle(int recipient) {

        HashSet<Integer> visited = new HashSet<>();
        return findCycleDfs(recipient, visited, new Stack<>(), recipient);
    }

    // recursively explores the graph to find a cycle,
    // marking nodes as visited and checking if it reconnects to the target node.
    private boolean hasCycleDfs(int current, HashSet<Integer> visited, int target) {
        if (visited.contains(current)){
            return current == target;
        }
        visited.add(current);
        for (Match match : adjList.get(current)) {
            if (hasCycleDfs(match.recipient, visited, target)) {
                return true;
            }
        }
        return false;
    }
}