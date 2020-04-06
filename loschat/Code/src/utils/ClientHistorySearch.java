package utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class is to search the history of chat according to the keyword.
 * @author Zongshi Li
 * @version 2020-03-15
 */
public class ClientHistorySearch {

	
	// This folder path may need change.
	private final static String folderPath = "src/history";
	// In each element of this list, index = 0 will store the detail of each search result,
	// And index = 1 will store the path of the file associated with the search result.
	static List<List<String>> searchResult = new ArrayList<>();
	static int number = 0;
	private String keyWord;
	
	/**
	 * The constructor of the class.
	 * @param keyWord
	 */
	public ClientHistorySearch(String keyWord) {
		super();
		this.keyWord = keyWord;
	}
	
	/**
	 * The getter method to for other class to get the list getSearchResult.
	 * @return the searchResult
	 */
	public static List<List<String>> getSearchResult() {
		return searchResult;
	}

	public static void resetList() {
		searchResult.clear();
		number = 0;
	}
	/**
	 * This method is to search the keyword from a file and add it to the result list.
	 * @param file
	 * @param keyWord
	 * @throws IOException
	 */
	public void search(File file) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		FileChannel fileChannel = fileInputStream.getChannel();
		FileLock fileLock = null;
		while (true) {
            try {
            	// Use file lock function to avoid the happen of io clash.
                fileLock = fileChannel.tryLock(0L,Long.MAX_VALUE,true);
                break;
            } catch (Exception e) {
            	e.printStackTrace();
            	System.out.println("The file is unavailable now.");
                try {
					Thread.sleep(5);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
            }
        }
		Scanner scan = new Scanner(file,"utf8");
        int row = 0;  
            while(scan.hasNext()) {
            	String rowDetail = scan.nextLine();
            	row++;
            	if(rowDetail.contains(keyWord)){
            		String resultDisplay =rowDetail + "\r\nRow:" + row +"|File Name:"+ file.getName();
            		searchResult.add(new ArrayList<>());
            		searchResult.get(number).add(resultDisplay);
            		searchResult.get(number).add(file.getAbsolutePath());
            		number++;
            	}
            }
       scan.close();
       fileLock.release();
       fileChannel.close();
       fileInputStream.close();
	}
	
	/**
	 * This method is to traverse the whole folder.
	 * @param folder
	 * @param keyWord
	 * @throws IOException
	 */
    public void folderTraverse() throws IOException {
    	File folder = new File(folderPath);
        File[] fileList = folder.listFiles();
        if(fileList == null) {
        	System.out.println("No history file exists.");
        	return;
        }
        for(File file : fileList) {
            if(file.isFile()) {
                search(file);
            }
        }        
    }
   
	

}
