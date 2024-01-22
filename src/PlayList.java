import java.util.*;

//For this PlayList class, I keep the songs belonging to that playList here and the songs found and not found in that playList
// in each category in minHeap and maxHeap.
public class PlayList {
    int ID;
    public int blissFullSongNum=0;
    public int roadTripSongNum=0;
    public int heartAcheSongNum=0;
    public Queue<Song> songList=new LinkedList<>();
    public MinHeap songsInBlissful=new MinHeap("blissfulScore");
    public MinHeap songsInRoadTrip=new MinHeap("roadTripScore");
    public MinHeap songsInHeartache=new MinHeap("heartacheScore");

    public MaxHeap songsInMaxHeapBlissful=new MaxHeap("blissfulScore");
    public MaxHeap songsInMaxHeapRoadTrip=new MaxHeap("roadTripScore");
    public MaxHeap songsInMaxHeapHeartache=new MaxHeap("heartacheScore");


    public PlayList(int ID){
        this.ID=ID;
    }

    public void removeFromTypeOfBlend(Song song,String type) {
        if (type.equals("blissfulScore")) {
            songsInBlissful.removeBySong(song);
            blissFullSongNum--;
        }
        else if(type.equals("roadTripScore")) {
            songsInRoadTrip.removeBySong(song);
            roadTripSongNum--;
        }
        else if(type.equals("heartacheScore")) {
            songsInHeartache.removeBySong(song);
            heartAcheSongNum--;
        }
        }
    public void addTypeOfBlend(Song song,String type){
        if(type.equals("blissfulScore")){
            songsInBlissful.insert(song);
        }
        else if(type.equals("roadTripScore")){
            songsInRoadTrip.insert(song);
        }
        else if(type.equals("heartacheScore")){
            songsInHeartache.insert(song);
        }
    }
    public void addTypeOfMax(Song song,String type){
        if(type.equals("blissfulScore")){
            songsInMaxHeapBlissful.insert(song);
        }
        else if(type.equals("roadTripScore")){
            songsInMaxHeapRoadTrip.insert(song);
        }
        else if(type.equals("heartacheScore")){
            songsInMaxHeapHeartache.insert(song);
        }
    }
    public MaxHeap getTypeOfMax(String type){
        if(type.equals("blissfulScore")){
            return songsInMaxHeapBlissful;
        }
        else if(type.equals("roadTripScore")){
            return songsInMaxHeapRoadTrip;
        }
        else {
            return songsInMaxHeapHeartache;
        }
    }

    public Song getMinTypeOfHeap(String type){
        if(type.equals("blissfulScore")){
            return songsInBlissful.peek();
        }
        else if(type.equals("roadTripScore")){
            return songsInRoadTrip.peek();
        }
        else {
            return songsInHeartache.peek();
        }
    }
    public MinHeap getTypeOfHeap(String type){
        if(type.equals("blissfulScore")){
            return songsInBlissful;
        }
        else if(type.equals("roadTripScore")){
            return songsInRoadTrip;
        }
        else {
            return songsInHeartache;
        }
    }
    public int getTypeOfNum(String type) {
        if(type.equals("blissfulScore")){
            return blissFullSongNum;
        }
        else if(type.equals("heartacheScore")){
            return heartAcheSongNum;
        }
        else if(type.equals("roadTripScore")){
            return roadTripSongNum;
        }
        return 0;
    }

    public void setTypeOfNum(String type,int value){
        if(type.equals("blissfulScore")){
            blissFullSongNum+=value;
        }
        else if(type.equals("heartacheScore")){
            heartAcheSongNum+=value;
        }
        else if(type.equals("roadTripScore")){
            roadTripSongNum+=value;
        }
    }


}
