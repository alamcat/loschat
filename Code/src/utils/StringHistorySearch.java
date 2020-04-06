package utils;


public class StringHistorySearch {

	private String history;
	private String keyWord;
	/**
	 * @param history
	 * @param keyWord
	 */
	public StringHistorySearch(String history, String keyWord) {
		super();
		this.history = history;
		this.keyWord = keyWord;
	}
	
	
	public String searchInside() {
		String searchResult = "";
		String[] list = history.split("\r\n");
		for(String element:list) {
			if(element.contains(keyWord)) {
				searchResult += element + "\r\n";
			}
		}
		
		if(searchResult == "") {
			searchResult = "No history about " + keyWord;
		}
		return searchResult;
	}

}