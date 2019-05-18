package edu.uci.ics.jkotha.service.api_gateway.threadpool;

public class ClientRequestQueue {
    private ListNode head;
    private ListNode tail;

    public ClientRequestQueue() {
        head=null;
        tail=null;
    }

    public synchronized void enqueue(ClientRequest clientRequest) {
        ListNode node = new ListNode(clientRequest,null);

        //if list is empty
        if (isEmpty()) {
            head = tail=node;
            notify();
            return;
        }

        //else
        tail.setNext(node);
        tail = node;
    }

    public synchronized ClientRequest dequeue() throws InterruptedException {
        if (isEmpty()) {
            wait();
        }
       //get head
        ListNode node = head;

        //rebase head
        head = head.getNext();

        return node.getClientRequest();
    }


    boolean isEmpty() {
        if (head==null){
            return true;
        }
        return false;
    }
}
