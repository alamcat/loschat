package utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import bean.Messenger;

/**
 * This class is to check the history of chat for both 1 on 1 chat and group chat.
 * @author Zongshi Li
 * @version 2020-03-15
 */
public class ClientHistoryWriter {

	private static String fromId;
	private static String toId;
	private static boolean isGroup;
	private static String message;
	// A boolean value represents the speaker of the message.
	// True means the current client is the sender of the message while false means the client is the receiver of the message.
	private static boolean talker;
	
	
	/**
	 * This method is to get the file path of chat history from history folder.
	 * If there is no file path in this folder, create a new one for future history write.
	 * @return path The path of the file associated with the input attributes.
	 */
	public static String getFilePath(String fromId,String toId,boolean isGroup,String message,boolean talker) {
		String path;
		String fileName;
		String dirName = "src/history";
		if(isGroup){
			if(talker) {
					fileName = dirName + "/" + "GroupChatHistory" + toId + ".txt";
				} else {
					fileName = dirName + "/" + "GroupChatHistory" + fromId + ".txt";
				}
				
				File historyFile = new File(fileName);
				if(!historyFile.exists()){
					try {
						historyFile.createNewFile();
						System.out.println("one group TXT create.");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				String absolutePath = historyFile.getAbsolutePath();
				path = absolutePath;
		} else {
			if(talker) {
					fileName = dirName + "/" + "PersonalChatHistory" + fromId + "To" + toId + ".txt";
				} else {
					fileName = dirName + "/" + "PersonalChatHistory" + toId + "To" + fromId + ".txt";
				}
				
				File historyFile = new File(fileName);
				if(!historyFile.exists()){
					try {
						historyFile.createNewFile();
						System.out.println("one personal TXT create.");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				String absolutePath = historyFile.getAbsolutePath();
				path = absolutePath;
		}
		return path;
	}
	
	/**
	 * This method is to merge time information with message.
	 * @return msg The new message contains both the message and current time.
	 */
	public static String getDateAndMessage(String fromId,String toId,boolean isGroup,String message,boolean talker) {
		LocalDateTime localDateTime = LocalDateTime.now();
		String msg;
		if(isGroup){
			if(talker) {
				msg = "From User-" + fromId  + " : ";
			} else {
				msg = "From Group-" + fromId + " : ";
			}
		} else {
			msg = "From User-" + fromId + " : ";
		}
		msg += message + "  |" + stringLocalDateTime(localDateTime) + "\r\n";
		return msg;
	}

	/**
	 * This method is to translate LocalDateTime into String type.
	 * @param date Current time.
	 * @return Current time in string type.
	 */
	public static String stringLocalDateTime(LocalDateTime date) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return date.format(dtf);
	}

	/**
	 * This method is to write new chat message into specific history file.
	 * @param msg The new chat message.
	 * @param talker A boolean value represents the speaker of the message.
	 */
	public static void writeHistory(Messenger msg,boolean talker) {
		fromId = msg.getMsgFrom();
		toId = msg.getMsgTo();
		boolean isGroup = msg.getGroup();
		String message = msg.getMsgDetail();
		System.out.println(getDateAndMessage(fromId,toId,isGroup,message,talker));
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(new File(getFilePath(fromId,toId,isGroup,message,talker)),true);
			FileChannel fileChannel = fileOutputStream.getChannel();
			FileLock fileLock = null;
	        while (true) {
	            try {
	            	// Use file lock function to avoid the happen of io clash.
	                fileLock = fileChannel.tryLock();
	                break;
	            } catch (Exception e) {
	                Thread.sleep(5);
	            }
	        }
	        String contentToWrite = getDateAndMessage(fromId,toId,isGroup,message,talker) + "\r\n";
			ByteBuffer byteBuffer = ByteBuffer.wrap(contentToWrite.getBytes("UTF-8"));
			fileChannel.write(byteBuffer);
			// Force buffer to write to hardware storage.
			fileChannel.force(false);
			fileLock.release();
			fileChannel.close();
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
