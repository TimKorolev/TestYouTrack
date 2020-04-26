import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

import static measurer.Measurer.*;

public class Main {
    public static Logger logger = Logger.getLogger("main");

    public static void main(String[] args) {

        ArrayList<HashMap<Long, ArrayList<Long>>> IssueCreatingDataSets = new ArrayList<>();
        ArrayList<HashMap<Long, ArrayList<Long>>> IssueDataGettingDataSets = new ArrayList<>();
        long[] delaySet = {0, 10, 20, 50, 100, 200, 500};

        Arrays.stream(delaySet).forEach(delay -> IssueCreatingDataSets.add(measureIssueCreating(delay)));
        Arrays.stream(delaySet).forEach(delay -> IssueDataGettingDataSets.add(measureGettingIssueData(delay)));

        IssueCreatingDataSets.forEach(entrySet -> entrySet.entrySet().forEach(entry -> System.out.println("Issue creating, delay: " + entry.getKey() + ", 90 percentile: " + get90Percentile(entry.getValue()) + " milliseconds")));
        System.out.println();
        IssueDataGettingDataSets.forEach(entrySet -> entrySet.entrySet().forEach(entry -> System.out.println("Getting of issue data , delay: " + entry.getKey() + ", 90 percentile: " + get90Percentile(entry.getValue()) + " milliseconds")));
    }
}
