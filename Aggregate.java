/*
 * Click `Run` to execute the snippet below!
 */

import java.io.*;
import java.util.*;

/*
 * To execute Java, please define "static void main" on a class
 * named Solution.
 *
 * If you need more classes, simply define them inline.
 */

class Solution {
  public static List<String> aggregate(List<String> input) {
    List<String> result = new ArrayList<String>();
    
    int currentTime = -1;
    int currentFirst = -1;
    int currentLast = -1;
    int currentMax = Integer.MIN_VALUE;
    int currentMin = Integer.MAX_VALUE;
    for (String dp:input) {
      int time = Integer.parseInt(dp.split(":")[0]);
      int value = Integer.parseInt(dp.split(":")[1]);
      int timeId = time/10;
      System.out.println(timeId);
      // System.out.println(currentTime);
      if(timeId != currentTime && currentTime >= 0) {
        String agg = String.format("%d:%d,%d,%d,%d", currentTime, currentMax, currentMin, currentLast, currentFirst);
        
        result.add(agg);
        currentTime = timeId;
        currentFirst = value;
        currentLast = value;
        currentMax = value;
        currentMin = value;
      }
      currentTime = timeId;
      if (currentFirst == -1) {
        currentFirst = value;
      }
      if (value != currentLast) {
        currentLast = value;
      }
      if (value > currentMax) {
        currentMax = value;
      }
      if (value < currentMin) {
        currentMin = value;
      }
    }
    String agg = String.format("%d:%d,%d,%d,%d", currentTime, currentMax, currentMin, currentLast, currentFirst);
    result.add(agg);
    return result;
  }
  public static void main(String[] args) {
        List<String> input = new ArrayList<String>();
        input.add("0:10");
        input.add("2:8");
        input.add("5:9");
        input.add("11:6");
        input.add("17:7");
        input.add("23:8");
    
        System.out.println(aggregate(input));
  }
}
