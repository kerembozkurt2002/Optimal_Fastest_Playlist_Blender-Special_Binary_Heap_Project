public class Song {
    //This class is my Song class and I create my song objects in this class,
    // keep their scores according to each type and check whether they are in minHeaps.
    public int ID;
    public String name;
    public int playCount;
    public int heartacheScore;
    public int roadTripScore;
    public int blissfulScore;
    public int playlistSongIn;
    public boolean isItInBlissful=false;
    public boolean isItInRoadTrip=false;
    public boolean isItInHeartache=false;


    public Song(){
    }
    public Song(int ID, String name, int playCount, int heartacheScore, int roadTripScore, int blissfulScore) {
        this.ID = ID;
        this.name = name;
        this.playCount = playCount;
        this.heartacheScore = heartacheScore;
        this.roadTripScore = roadTripScore;
        this.blissfulScore = blissfulScore;
    }
    public void setIsItIn(String type,boolean flag) {
        if(type.equals("blissfulScore")){
            isItInBlissful=flag;
        }
        else if(type.equals("heartacheScore")){
            isItInHeartache=flag;
        }
        else if(type.equals("roadTripScore")){
            isItInRoadTrip=flag;
        }
    }
    public boolean getIsItIn(String type) {
        if(type.equals("blissfulScore")){
            return isItInBlissful;
        }
        else if(type.equals("heartacheScore")){
            return isItInHeartache;
        }
        else if(type.equals("roadTripScore")){
            return isItInRoadTrip;
        }
        return false;
    }
    public int compareToByType(Song other,String type) {
        if(type.equals("blissfulScore")){
            int result=Integer.compare(this.blissfulScore, other.blissfulScore);
            if(result!=0) {
                return result;
            }
            else{
                if(this.name.compareTo(other.name)<0){
                    return 1;
                }
                else {
                    return -1;
                }
            }
        }
        else if(type.equals("heartacheScore")){
            int result=Integer.compare(this.heartacheScore, other.heartacheScore);
            if(result!=0) {
                return result;
            }
            else{
                if(this.name.compareTo(other.name)<0){
                    return 1;
                }
                else {
                    return -1;
                }
            }
        }
        else if(type.equals("roadTripScore")){
            int result=Integer.compare(this.roadTripScore, other.roadTripScore);
            if(result!=0) {
                return result;
            }
            else {
                if (this.name.compareTo(other.name) < 0) {
                    return 1;
                } else {
                    return -1;
                }

            }
        }
        else if(type.equals("playCount")){
            int result=Integer.compare(this.playCount, other.playCount);
            if(result!=0) {
                return result;
            }
            else {
                if (this.name.compareTo(other.name) < 0) {
                    return 1;
                } else {
                    return -1;
                }

            }
        }
        return 0;
    }
}
