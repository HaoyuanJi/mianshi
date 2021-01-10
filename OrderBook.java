import java.util.*;

public class HelloWorld{
    private static class Node {
        public int price;
        public int share;
        public Node(int price, int share) {
            this.price = price;
            this.share = share;
        }
    }
    
    static int order_book(String[][] orders) {
        if(orders == null || orders.length == 0 || orders[0].length == 0) {
            return 0;
        }
        int res = 0;
        PriorityQueue<Node> buypq = new PriorityQueue<Node>((b1, b2) -> b2.price - b1.price);
        PriorityQueue<Node> sellpq = new PriorityQueue<Node>((s1, s2) -> s2.price - s1.price);

        for(int i = 0; i < orders.length; i++) {
            int price = Integer.parseInt(orders[i][0]);
            int share = Integer.parseInt(orders[i][1]);

            if(orders[i][2].equals("buy")) {
                Node buynode = new Node(price, share);
                while(!sellpq.isEmpty() && sellpq.peek().price <= buynode.price && buynode.share > 0) {
                    Node sellnode = sellpq.poll();
                    if(sellnode.share > buynode.share) {
                        res += buynode.share;
                        sellnode.share = sellnode.share - buynode.share;
                        sellpq.offer(sellnode);
                        buynode.share = 0;
                 } else {
                     // sellnode.share <= buynode.share;
                    res += sellnode.share;
                    buynode.share -= sellnode.share;
                }
            }
            if(buynode.share > 0) {
                buypq.offer(buynode);
            }
        } else {
            // “SELL” buy price >= sell price;
            Node sellnode = new Node(price, share);
            while(!buypq.isEmpty() && buypq.peek().price >= sellnode.price && sellnode.share > 0) {
                Node buynode = buypq.poll();
                if(buynode.share > sellnode.share) {
                    res += sellnode.share;
                    buynode.share = buynode.share - sellnode.share;
                    buypq.offer(buynode);
                    sellnode.share = 0;
                } else {
                    // buynode.share >= sellnode.share;
                    res += buynode.share;
                    sellnode.share -= buynode.share;
                }
            }
            if(sellnode.share > 0) {
                sellpq.offer(sellnode);
            }
        }
    }
    return res;
}
     public static void main(String []args){
        System.out.println("Hello World");
     }
}
