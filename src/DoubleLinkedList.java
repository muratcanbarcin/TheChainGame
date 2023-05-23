import java.util.Arrays;

public class DoubleLinkedList {
    public DLLNode head;
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

    public void display() {
        DLLNode currentNode = head;

        while (currentNode != null) {
            System.out.print(currentNode.getData());
            currentNode = currentNode.getNext();
            System.out.println(" " + currentNode.getData());
            currentNode = currentNode.getNext();
        }
    }

    public void sorted() {
        String[] name = new String[count() / 2];
        int[] score = new int[count() / 2];
        int countData = 0;
        DLLNode currentNode = head;

        while (currentNode != null) {
            if (countData % 2 == 0) {
                name[countData / 2] = (String) currentNode.getData();
            } else {
                score[(countData - 1) / 2] = Integer.parseInt(currentNode.getData().toString());
            }

            DLLNode nextNode = currentNode.getNext();
            currentNode = nextNode;
            countData++;
        }

        // Sıralama işlemi
        for (int i = 0; i < score.length - 1; i++) {
            for (int j = 0; j < score.length - 1 - i; j++) {
                if (score[j] < score[j + 1]) {
                    int temp = score[j];
                    String temp2 =name[j];
                    score[j] = score[j + 1];
                    name[j] = name[j+1];
                    name[j+1] = temp2;
                    score[j + 1] = temp;
                }
            }
        }
        int sizeTemp = count();
        deleteAll();
        for(int i=0;i<(sizeTemp/2);i++){
            add(name[i]);
            add(score[i]);
        }
    }

    public void deleteAll() {
        head = null;
        tail = null;
        size = 0;
    }




}
