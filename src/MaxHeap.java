import java.util.ArrayList;

//This class is my maxHeap class and it allows me to create maxHeap objects that I will use for the songs that could not be added to epicBlend
// throughout the assignment and to perform the ASK operation.
public class MaxHeap {
    public ArrayList<Song> array;
    private int size;
    public String type;


    public MaxHeap(String type){
        this.type=type;
        size = 0;
        array = new ArrayList<Song>();
        array.add(null);
    }

    public void insert(Song value) {
        size++;
        array.add(value);
        int hole = size;
        while (hole > 1 && value.compareToByType(array.get(hole / 2),type) > 0) {
            Song parent = array.get(hole / 2);
            array.set(hole / 2, value);
            array.set(hole, parent);
            hole = hole / 2;
        }
    }
    public void percolateUp(int index){
        Song value=array.get(index);
        int hole = index;
        while (hole > 1 && value.compareToByType(array.get(hole / 2),type) > 0) {
            Song parent = array.get(hole / 2);
            array.set(hole / 2, value);
            array.set(hole, parent);
            hole = hole / 2;
        }
    }
    public void removeBySong(Song targetSong){
        for (int i=1;i<size+1;i++){
            Song currentSong=array.get(i);
            if(currentSong.name.equals(targetSong.name)){
                remove(i);
            }
        }
    }
    public void remove(int index){
        if(index==size){
            array.remove(index);
            size--;
        }
        else {
            Song lastSong = array.get(size);
            array.set(index, lastSong);
            array.remove(size);
            size--;
            if (index != size + 1) {
                percolateDown(index);
            }
            if (index != 1) {
                percolateUp(index);
            }

        }

    }

    public Song peek() {
        if(size==0) {
            return null;
        }
        return array.get(1);
    }

    public Song pop() {
        Song maxItem = peek();
        array.set(1, array.get(size));
        size--;
        percolateDown(1);
        array.remove(size+1);
        return maxItem;
    }


    private void percolateDown(int hole) {
        int child;
        Song temp = array.get(hole);

        while (hole * 2 <= size) {
            child = hole * 2;
            if (child != size && array.get(child + 1).compareToByType(array.get(child),type) > 0) {
                child++;
            }
            if (array.get(child).compareToByType(temp,type) > 0) {
                array.set(hole, array.get(child));
            } else {
                break;
            }

            hole = child;
        }
        array.set(hole, temp);
    }
}
