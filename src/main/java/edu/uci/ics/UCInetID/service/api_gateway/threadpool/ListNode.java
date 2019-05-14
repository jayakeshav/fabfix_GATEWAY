package edu.uci.ics.UCInetID.service.api_gateway.threadpool;

public class ListNode {
    private ClientRequest clientRequest;
    private ListNode next;

    public ListNode(ClientRequest clientRequest, ListNode next) {

    }

    public ClientRequest getClientRequest() {
        return clientRequest;
    }

    public void setClientRequest(ClientRequest clientRequest) {
        this.clientRequest = clientRequest;
    }

    public ListNode getNext() {
        return next;
    }

    public void setNext(ListNode next) {
        this.next = next;
    }
}
