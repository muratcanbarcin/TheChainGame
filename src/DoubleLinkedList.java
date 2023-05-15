public class DoubleLinkedList {
    private DLLNode head;
    private DLLNode tail;
    private int size;

    public void add(Object data) {
        DLLNode newNode = new DLLNode(data);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.setPrev(tail);
            tail.setNext(newNode);
            tail = newNode;
        }

        size++;
    }

    public void display() {
        DLLNode currentNode = head;

        while (currentNode != null) {
            System.out.print(currentNode.getData() + " ");
            currentNode = currentNode.getNext();
        }
        System.out.println();
    }

    public int count() {
        return size;
    }

    public void delete(Object data) {
        DLLNode currentNode = head;

        while (currentNode != null) {
            if (currentNode.getData().equals(data)) {
                DLLNode prevNode = currentNode.getPrev();
                DLLNode nextNode = currentNode.getNext();

                if (prevNode != null) {
                    prevNode.setNext(nextNode);
                } else {
                    head = nextNode;
                }

                if (nextNode != null) {
                    nextNode.setPrev(prevNode);
                } else {
                    tail = prevNode;
                }

                size--;
                break;
            }

            currentNode = currentNode.getNext();
        }
    }
}
