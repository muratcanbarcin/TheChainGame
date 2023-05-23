public class DoubleLinkedList {
    public DLLNode head;
    private DLLNode tail;
    private int size;

    /*
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
*/

    public void addSorted(String name, int score) {
        DLLNode newNode = new DLLNode(name + " " + score);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else if (score >= Integer.parseInt(tail.getData().toString().split(" ")[1])) {
            newNode.setPrev(tail);
            tail.setNext(newNode);
            tail = newNode;
        } else if (score <= Integer.parseInt(head.getData().toString().split(" ")[1])) {
            newNode.setNext(head);
            head.setPrev(newNode);
            head = newNode;
        } else {
            DLLNode currentNode = head.getNext();

            while (currentNode != null) {
                int currentScore = Integer.parseInt(currentNode.getData().toString().split(" ")[1]);

                if (score >= currentScore) {
                    DLLNode prevNode = currentNode.getPrev();
                    newNode.setPrev(prevNode);
                    newNode.setNext(currentNode);
                    prevNode.setNext(newNode);
                    currentNode.setPrev(newNode);
                    break;
                }

                currentNode = currentNode.getNext();
            }
        }

        size++;
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
