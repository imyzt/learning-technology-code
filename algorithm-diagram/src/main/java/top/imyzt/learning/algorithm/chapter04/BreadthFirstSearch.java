package top.imyzt.learning.algorithm.chapter04;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author imyzt
 * @date 2021/05/05
 * @description 广度优先搜索算法
 */
public class BreadthFirstSearch {

    private static HashMap<String, ArrayList<String>> graph = new HashMap<>();


    static {
        ArrayList<String> you = new ArrayList<>();
        you.add("f1");
        you.add("f2");
        you.add("f3");

        ArrayList<String> f1 = new ArrayList<>();
        f1.add("f1f1");
        f1.add("f1f2");
        f1.add("f1f3");
        f1.add("tom");

        ArrayList<String> f2 = new ArrayList<>();
        f2.add("f2f1");
        f2.add("f2f2");
        f2.add("tom");

        ArrayList<String> f3 = new ArrayList<>();
        f3.add("f3f1");
        f3.add("f3f2_target");

        graph.put("you", you);
        graph.put("f1", f1);
        graph.put("f2", f2);
        graph.put("f3", f3);
    }

    public static void main(String[] args) {

        breadthFirstSearch();

    }

    private static void breadthFirstSearch() {
        ArrayList<String> searched = new ArrayList<>();

        LinkedList<String> searchQueue = new LinkedList<>(graph.get("you"));

        while (!searchQueue.isEmpty()) {
            String curr = searchQueue.pop();
            if (searched.contains(curr)) {
                System.out.println("目标已查询过,跳过, " + curr);
                continue;
            }
            if (isTarget(curr)) {
                System.out.println("找到目标: + " + curr);
                break;
            } else {
                ArrayList<String> last = graph.get(curr);
                if (last != null && !last.isEmpty()) {
                    searchQueue.addAll(graph.get(curr));
                }
                searched.add(curr);
            }
        }
    }

    private static boolean isTarget(String name) {
        return name.contains("target");
    }
}