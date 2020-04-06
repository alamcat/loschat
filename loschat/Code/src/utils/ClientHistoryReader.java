package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * This class is to read the history of chat for both 1 on 1 chat and group chat.
 * @author Zongshi Li
 * @version 2020-03-15
 */
public class ClientHistoryReader {

	
	private  String fromId;
	private  String toId;
	private  boolean isGroup;
	
	/**
	 * The constructor of the class.
	 * @param fromId
	 * @param toId
	 * @param isGroup
	 */
	public ClientHistoryReader(String fromId,String toId, boolean isGroup) {
		super();
		this.fromId = fromId;
		this.toId = toId;
		this.isGroup = isGroup;
	}
	/**
	 * The empty constructor of the class.
	 */
	public ClientHistoryReader() {
		
	}

	/**
	 * This method is to get the file path of the specific chat history from history folder.
	 * @return path The path of the specific file according to the fromId,toId and isGroup.
	 */
	public String getFilePath() {
		String path;
		if(isGroup){
			String dirName = "src/history";
			String fileName = dirName + "/" + "GroupChatHistory" + toId + ".txt";
			File historyFile = new File(fileName);
			if(!historyFile.exists()){
				
				System.out.println("History don't exist.");
			}
			String absolutePath = historyFile.getAbsolutePath();
			path = absolutePath;
		} else {
			String dirName = "src/history";
			String fileName = dirName + "/" + "PersonalChatHistory" + fromId + "To" + toId + ".txt";
			File historyFile = new File(fileName);
			if(!historyFile.exists()){
				
				System.out.println("History don't exist.");
			}
			String absolutePath = historyFile.getAbsolutePath();
			path = absolutePath;
		}
		return path;
	}
	
	/**
	 * This method is to read the history record according to the info of id and isGroup.
	 * @return historyRecord The history record in string type.
	 */
	public String readHistoryRecord() {
		String historyRecord = null;
		String path = getFilePath();
		try {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			FileInputStream fileInputStream = new FileInputStream(new File(path));
			FileChannel fileChannel = fileInputStream.getChannel();
			FileLock fileLock = null;
			while (true) {
				try {
					// Use file lock function to avoid the happen of io clash.
					fileLock = fileChannel.tryLock(0L,Long.MAX_VALUE,true);
					break;
				} catch (Exception e) {
					System.out.println("History is updating...");
					e.printStackTrace();
					Thread.sleep(5);
				}
			}
			buffer.clear();		
			int length = fileChannel.read(buffer);
			historyRecord = new String(buffer.array(),0,length,"utf8");
			fileLock.release();
			fileChannel.close();
			fileInputStream.close();
			} catch (Exception e) {
				System.out.println("Can't read history now...");
				e.printStackTrace();
			}
		return historyRecord;
	}
	
	/**
	 * This method is to read the history record according to the info of path.
	 * @return history The history in string type.
	 */
	public String readHistory(String path) {
		String history = null;
		try {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			FileInputStream fileInputStream = new FileInputStream(new File(path));
			FileChannel fileChannel = fileInputStream.getChannel();
			FileLock fileLock = null;
			while (true) {
				try {
					// Use file lock function to avoid the happen of io clash.
					fileLock = fileChannel.tryLock(0L,Long.MAX_VALUE,true);
					break;
				} catch (Exception e) {
					System.out.println("History is updating...");
					e.printStackTrace();
					Thread.sleep(5);
				}
			}
			buffer.clear();		
			int length = fileChannel.read(buffer);
			history = new String(buffer.array(),0,length,"utf8");
			fileLock.release();
			fileChannel.close();
			fileInputStream.close();
			} catch (Exception e) {
				System.out.println("Can't read history now...");
				e.printStackTrace();
			}
		return history;
	}
}

