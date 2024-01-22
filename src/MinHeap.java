import java.util.ArrayList;

//This class is my minHeap class and it allows me to create minHeap objects that I will use
// throughout the assignment for the songs added to epicBlend.
public class MinHeap {
    public ArrayList<Song> array;
    public int size;
    public int realSize=0;
    public String type;
    public MinHeap(String type){
        this.type=type;
        size = 0;
        array = new ArrayList<Song>();
        array.add(null);
    }

    public void insert(Song value) {
        size++;
        realSize++;
        array.add(value);
        int hole = size;
        while (hole > 1 && value.compareToByType(array.get(hole / 2),type) < 0) {
            Song parent = array.get(hole / 2);
            array.set(hole / 2, value);
            array.set(hole, parent);
            hole = hole / 2;
        }
    }
    public void percolateUp(int index){
        Song value=array.get(index);
        int hole=index;
        while (hole > 1 && value.compareToByType(array.get(hole / 2),type) < 0) {
            Song parent = array.get(hole / 2);
            array.set(hole / 2, value);
            array.set(hole, parent);
            hole = hole / 2;
        }
    }
    public boolean removeBySong(Song targetSong){
        for (int i=1;i<size+1;i++){
            Song currentSong=array.get(i);
            if(currentSong.name.equals(targetSong.name)){
                remove(i);
                return true;
            }
        }
        return false;
    }
    public void remove(int index){
        if(index==size){
            array.remove(size);
            size--;
            realSize--;
        }
        else {
            Song lastSong = array.get(size);
            array.set(index, lastSong);
            array.remove(size);
            size--;
            realSize--;
            if (index != size + 1) {
                percolateDown(index);
            }
            if (index != 1) {
                percolateUp(index);
            }
        }
    }
    public Song peek() {
        if(size==0){
            return null;
        }
        return array.get(1);
    }



    public Song pop() {
        Song minItem = peek();
        array.set(1, array.get(size));
        size--;
        realSize--;
        percolateDown(1);
        array.remove(size+1);
        return minItem;
    }

    private void percolateDown(int hole) {
        int child;
        Song temp = array.get(hole);

        while (hole * 2 <= size) {
            child = hole * 2;
            if (child != size && array.get(child + 1).compareToByType(array.get(child),type) < 0) {
                child++;
            }
            if (array.get(child).compareToByType(temp,type) < 0) {
                array.set(hole, array.get(child));
            } else {
                break;
            }

            hole = child;
        }
        array.set(hole, temp);
    }

}
