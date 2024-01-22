import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.PrintStream;
import java.io.FileOutputStream;


/**
 * In this project, I am trying to create the most accurate playlist by comparing the song choices of the people in a group we traveled with,
 * and I mostly did this by using the minHeap and maxHeap algorithms.
 * @author Kerem Bozkurt, Student ID: 2020400177
 * @since Date: 13.12.2023
 */

public class Project3 {
    public static void main(String[] args) throws FileNotFoundException {

        PrintStream out = new PrintStream(new FileOutputStream(args[2]));
        System.setOut(out);
        File songText=new File(args[0]);
        File testCaseFile=new File(args[1]);

        Scanner songScanner=new Scanner(songText);
        int size=Integer.parseInt(songScanner.nextLine().strip());
        ArrayList<Song> allSongsList=new ArrayList<>(size+1);
        allSongsList.add(null);

        // First, I create song objects that I will use throughout the trip by reading the information from my song file.
        while (songScanner.hasNextLine()){
            String [] temp=songScanner.nextLine().split(" ");
            Song tempSong=new Song(Integer.parseInt(temp[0]),temp[1],Integer.parseInt(temp[2]),Integer.parseInt(temp[3]),Integer.parseInt(temp[4]),Integer.parseInt(temp[5]));
            allSongsList.add(tempSong);
        }
        songScanner.close();

        //
        Scanner caseScanner=new Scanner(testCaseFile);
        String [] firstInfo=caseScanner.nextLine().split(" ");

        int maxContributeSong=Integer.parseInt(firstInfo[0]);
        int heartacheNum=Integer.parseInt(firstInfo[1]);
        int roadTripNum=Integer.parseInt(firstInfo[2]);
        int blissfulNum=Integer.parseInt(firstInfo[3]);

        int numOfPlaylist=Integer.parseInt(caseScanner.nextLine());
        PlayList [] playListList=new PlayList[numOfPlaylist+1];

        //I create my playlists here
        for (int i=1;i<numOfPlaylist+1;i++){
            caseScanner.nextLine();
            String info=caseScanner.nextLine();
            PlayList temp=new PlayList(i);
            if(info.length()==0){
                playListList[i]=temp;
            }
            else {
                String [] songsInPlaylist=info.split(" ");
                for (String tempSogID : songsInPlaylist) {
                    Song tempSong = allSongsList.get(Integer.parseInt(tempSogID));
                    tempSong.playlistSongIn=i;
                    temp.songList.add(tempSong);
                }
                playListList[i] = temp;
            }
        }

        //I create my minHeaps to keep the songs in Epic Blend according to the categories I will use throughout the project and create their initial state.
        MinHeap blissfulMinHeap=new MinHeap("blissfulScore");
        MinHeap roadtripMinHeap=new MinHeap("roadTripScore");
        MinHeap heartacheMinHeap=new MinHeap("heartacheScore");

        buildList(blissfulMinHeap,blissfulNum,playListList,maxContributeSong);
        buildList(roadtripMinHeap,roadTripNum,playListList,maxContributeSong);
        buildList(heartacheMinHeap,heartacheNum,playListList,maxContributeSong);



        // Here, I take the necessary action according to the desired command.
        while (caseScanner.hasNextLine()){
            String [] tempCase=caseScanner.nextLine().split(" ");
            if(tempCase[0].equals("ADD")){
                int playlistID=Integer.parseInt(tempCase[2]);
                int songID=Integer.parseInt(tempCase[1]);
                Song addedSong=allSongsList.get(songID);
                addedSong.playlistSongIn=playlistID;
                PlayList playList=playListList[addedSong.playlistSongIn];
                playList.songList.add(addedSong);

                Integer[] resultHeartache=addSong(playlistID,songID,heartacheMinHeap,heartacheNum,playListList,maxContributeSong,allSongsList);
                Integer[] resultRoadTrip=addSong(playlistID,songID,roadtripMinHeap,roadTripNum,playListList,maxContributeSong,allSongsList);
                Integer[] resultBlissful=addSong(playlistID,songID,blissfulMinHeap,blissfulNum,playListList,maxContributeSong,allSongsList);
                System.out.println(resultHeartache[0]+" "+resultRoadTrip[0]+" "+resultBlissful[0]);
                System.out.println(resultHeartache[1]+" "+resultRoadTrip[1]+" "+resultBlissful[1]);

            }
            else if(tempCase[0].equals("REM")){
                int playListID=Integer.parseInt(tempCase[2]);
                int songID=Integer.parseInt(tempCase[1]);
                Song removedSong=allSongsList.get(songID);
                PlayList songInPlayList=playListList[playListID];

                Integer[] resultHeartache=remove(playListList,heartacheMinHeap,songInPlayList,removedSong,maxContributeSong);
                Integer[] resultRoadTrip=remove(playListList,roadtripMinHeap,songInPlayList,removedSong,maxContributeSong);
                Integer[] resultBlissful=remove(playListList,blissfulMinHeap,songInPlayList,removedSong,maxContributeSong);

                System.out.println(resultHeartache[1]+" "+resultRoadTrip[1]+" "+resultBlissful[1]);
                System.out.println(resultHeartache[0]+" "+resultRoadTrip[0]+" "+resultBlissful[0]);

            }
            else if(tempCase[0].equals("ASK")){
                askEpicBlend(blissfulMinHeap,roadtripMinHeap,heartacheMinHeap);

            }
        }





    }
    public static Integer[] remove(PlayList [] allPlayList,MinHeap generalMinHeap, PlayList removedPlayList, Song removedSong, int maxPlayListSize){
        //I'm doing the REM operation here.
        //For whatever type I remove, I delete that song from the minHeap of that type and the minHeap and maxHeap of the PlayList containing that song.
        Integer [] result=new Integer[2];
        result[0]=0;
        result[1]=0;

        String type=generalMinHeap.type;
        boolean deletedGeneralMin=generalMinHeap.removeBySong(removedSong);
        removedPlayList.getTypeOfHeap(type).removeBySong(removedSong);
        removedPlayList.getTypeOfMax(type).removeBySong(removedSong);
        if(deletedGeneralMin && removedSong.getIsItIn(type)) {
            removedSong.setIsItIn(type,false);
            removedPlayList.setTypeOfNum(type,-1);
            Song idealSong = getIdealSongFromMax(allPlayList, maxPlayListSize, type);
            result[0] = removedSong.ID;
            if (idealSong.ID != (-1)) {
                result[1] = idealSong.ID;
                PlayList addedPlaylist=allPlayList[idealSong.playlistSongIn];
                addedPlaylist.getTypeOfMax(type).removeBySong(idealSong);
                idealSong.setIsItIn(type,true);
                addedPlaylist.setTypeOfNum(type,+1);
                addedPlaylist.getTypeOfHeap(type).insert(idealSong);
                generalMinHeap.insert(idealSong);

            }
        }
        return result;

    }

    public static void askEpicBlend(MinHeap blissFullMinHeap,MinHeap roadTripMinHeap,MinHeap heartacheMinHeap){
        //In this section, I perform the ASK process.
        //I take the songs in epicBlend categories and keep them all in a maxHeap and sort them all by doing deleteMin.
        Hashtable<String,Boolean> isItInEpicBlend=new Hashtable<>();
        MaxHeap epicBlend=new MaxHeap("playCount");

        ArrayList<Song> arrayBlissful=blissFullMinHeap.array;
        ArrayList<Song> arrayRoadTrip=roadTripMinHeap.array;
        ArrayList<Song> arrayHeartache=heartacheMinHeap.array;
        for (Song song:arrayBlissful){
            if(song!=null) {
                Boolean flag = isItInEpicBlend.get(song.name);
                if (flag == null  && song.getIsItIn("blissfulScore")) {
                    epicBlend.insert(song);
                    isItInEpicBlend.put(song.name, true);
                }
            }
        }
        for (Song song:arrayRoadTrip){
            if(song!=null) {
                Boolean flag = isItInEpicBlend.get(song.name);
                if (flag == null  && song.getIsItIn("roadTripScore")) {
                    epicBlend.insert(song);
                    isItInEpicBlend.put(song.name, true);
                }
            }
        }
        for (Song song: arrayHeartache){
            if(song!=null) {
                Boolean flag = isItInEpicBlend.get(song.name);
                if (flag == null  && song.getIsItIn("heartacheScore")) {
                    epicBlend.insert(song);
                    isItInEpicBlend.put(song.name, true);
                }
            }
        }

        while (epicBlend.peek()!=null){
            System.out.print(epicBlend.pop().ID);
            if(epicBlend.peek()!=null){
                System.out.print(" ");
            }
        }
        System.out.println();
    }


    public static Song getIdealSongFromMax(PlayList [] allPlayList,int maxPlaylistSize,String type){
        //Here I find out if there is a new song that can be added to epicBlend after the removal process.

        Song idealSong=new Song(-1,"ideal",Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE);
        for (PlayList playList:allPlayList){
            if (playList == null) {
                continue;
            }
            MaxHeap tempMax=playList.getTypeOfMax(type);
            if(tempMax.peek()!=null){
                while (tempMax.peek()!=null && tempMax.peek().getIsItIn(type)){
                    tempMax.pop();
                }
            }
            Song maxSong=tempMax.peek();
            if(maxSong!=null && idealSong.compareToByType(maxSong,type)<0 && playList.getTypeOfNum(type)<maxPlaylistSize && !maxSong.getIsItIn(type)){
                idealSong=maxSong;
            }
        }
        return idealSong;
    }

    public static Integer[] addSong(int playlistID, int songID,MinHeap minHeap, int maxSizeHeap, PlayList [] allPlayList, int maxPlaylistSize,ArrayList<Song> allSongsList){
        //Here, I first put the song in whichever PlayList it belongs to.
        //Then, for whatever type, I put that song in the epicBlend minHeap of the desired type if I can, and if not, I put it in the maxHeap.

        Song addedSong=allSongsList.get(songID);
        addedSong.playlistSongIn=playlistID;
        PlayList playList=allPlayList[addedSong.playlistSongIn];
        playList.songList.add(addedSong);
        String type=minHeap.type;
        Integer[] result=new Integer[2];
        result[0]=0;
        result[1]=0;

        if(minHeap.peek()!=null) {
            while (!minHeap.peek().getIsItIn(type)) {
                minHeap.pop();
            }
        }

        if(minHeap.size<maxSizeHeap && (playList.getTypeOfNum(type) < maxPlaylistSize )){
            addedSong.setIsItIn(type,true);
            minHeap.insert(addedSong);
            playList.addTypeOfBlend(addedSong,type);
            playList.setTypeOfNum(type,1);

            result[0]=songID;
            result[1]=0;
            return  result;
        }
        else if((playList.getTypeOfNum(type) <maxPlaylistSize ) && addedSong.compareToByType(minHeap.peek(),type)>0){
            Song currentMinSong=minHeap.peek();
            PlayList currentMinSongPlaylist=allPlayList[currentMinSong.playlistSongIn];
            Song poppedSong=minHeap.pop();
            poppedSong.setIsItIn(type,false);
            addedSong.setIsItIn(type,true);
            minHeap.insert(addedSong);
            playList.addTypeOfBlend(addedSong,type);
            currentMinSongPlaylist.removeFromTypeOfBlend(poppedSong,type);
            currentMinSongPlaylist.addTypeOfMax(poppedSong,type);
            playList.setTypeOfNum(type,+1);

            result[0]=addedSong.ID;
            result[1]=poppedSong.ID;
            return  result;
        }
        else {
            Song minInBlendInPlaylist = playList.getMinTypeOfHeap(type);
            if (minInBlendInPlaylist != null && minInBlendInPlaylist.compareToByType(addedSong,type)>0) {
                playList.addTypeOfMax(addedSong,type);
            }
            else {
                Song currentMinSong = minHeap.peek();
                if (currentMinSong.compareToByType(addedSong,type)<0) {
                    addedSong.setIsItIn(type,true);
                    minHeap.insert(addedSong);
                    playList.addTypeOfBlend(addedSong, type);

                    if (minHeap.peek().playlistSongIn == addedSong.playlistSongIn) {
                        Song poppedSong = minHeap.pop();
                        poppedSong.setIsItIn(type,false);
                        playList.removeFromTypeOfBlend(poppedSong, type);
                        playList.addTypeOfMax(poppedSong,type);
                        playList.setTypeOfNum(type, +1);

                        result[0] = addedSong.ID;
                        result[1] = poppedSong.ID;
                        return result;
                    }
                    else {
                        Song minInThatPlaylist=playList.getMinTypeOfHeap(type);
                        minInThatPlaylist.setIsItIn(type,false);
                        playList.getTypeOfHeap(type).pop();
                        playList.addTypeOfMax(minInThatPlaylist,type);


                        result[0] = addedSong.ID;
                        result[1] = minInThatPlaylist.ID;
                        return result;
                    }
                }
                else {
                    playList.addTypeOfMax(addedSong,type);
                }
            }
        }
        return result;
    }
    public static MinHeap buildList(MinHeap minHeap, int maxSizeHeap, PlayList [] allPlayList, int maxPlaylistSize){
        //Here I create the version of Epic Blend for whichever category it is before any processing occurs.

        for (PlayList playList: allPlayList) {
            if (playList == null) {
                continue;
            }
            Iterator<Song> songIterator=playList.songList.iterator();
            while (songIterator.hasNext()) {
                Song tempSong = songIterator.next();
                String type = minHeap.type;
                if(minHeap.peek()!=null) {
                    while (!minHeap.peek().getIsItIn(type)) {
                        minHeap.pop();
                    }
                }
                if(minHeap.size<maxSizeHeap && (playList.getTypeOfNum(type) <maxPlaylistSize )){
                    tempSong.setIsItIn(type,true);
                    minHeap.insert(tempSong);
                    playList.addTypeOfBlend(tempSong,type);
                    playList.setTypeOfNum(type,1);
                }
                else if((playList.getTypeOfNum(type) <maxPlaylistSize ) && tempSong.compareToByType(minHeap.peek(),type)>0 &&minHeap.peek().getIsItIn(type)){
                    Song currentMinSong=minHeap.peek();
                    PlayList currentMinSongPlaylist=allPlayList[currentMinSong.playlistSongIn];
                    Song poppedSong=minHeap.pop();
                    poppedSong.setIsItIn(type,false);
                    tempSong.setIsItIn(type,true);
                    minHeap.insert(tempSong);
                    playList.addTypeOfBlend(tempSong,type);
                    currentMinSongPlaylist.removeFromTypeOfBlend(poppedSong,type);
                    currentMinSongPlaylist.addTypeOfMax(poppedSong,type);
                    playList.setTypeOfNum(type,+1);
                }
                else {
                    Song minInBlendInPlaylist = playList.getMinTypeOfHeap(type);
                    if (minInBlendInPlaylist != null && minInBlendInPlaylist.compareToByType(tempSong,type)>0) {
                        playList.addTypeOfMax(tempSong,type);
                    }
                    else {
                        Song currentMinSong = minHeap.peek();
                        if (currentMinSong.compareToByType(tempSong,type)<0) {
                            tempSong.setIsItIn(type,true);
                            minHeap.insert(tempSong);
                            playList.addTypeOfBlend(tempSong, type);

                            if (minHeap.peek().playlistSongIn == tempSong.playlistSongIn) {
                                Song poppedSong = minHeap.pop();
                                poppedSong.setIsItIn(type,false);
                                playList.removeFromTypeOfBlend(poppedSong, type);
                                playList.addTypeOfMax(poppedSong,type);
                                playList.setTypeOfNum(type, +1);
                            }
                            else {
                                Song minInThatPlaylist=playList.getMinTypeOfHeap(type);
                                minInThatPlaylist.setIsItIn(type,false);
                                playList.getTypeOfHeap(type).pop();
                                playList.addTypeOfMax(minInThatPlaylist,type);

                            }
                        }
                        else {
                            playList.addTypeOfMax(tempSong,type);
                        }
                    }
                }

            }
        }
        return minHeap;

    }
}
