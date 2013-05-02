
package com.joy.cloudshare.common;

import java.io.Serializable;

import com.igrs.base.android.util.IgrsType.FileType;

public class MediaInfo implements Serializable{


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MediaInfo() {
        super();
    }

    private String id;
    private String url;
    private String name;
    private String time;
    private String author;
    private String descrip;
    private String path;
    private String duration;
    private String fileSize;
    private FileType fileType;

    public MediaInfo(String id, String url, String name, String time,
            String author, String descrip, String path,String fileSize,FileType fileType) {
        super();
        this.id = id;
        this.url = url;
        this.name = name;
        this.time = time;
        this.author = author;
        this.descrip = descrip;
        this.path = path;
        this.fileSize = fileSize;
        this.fileType = fileType;
    }

    public String getId() {
        return id;
    }

    public FileType getFileType() {
		return fileType;
	}

	public void setFileType(FileType fileType) {
		this.fileType = fileType;
	}

	public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url == null ? "" : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAuthor() {
        return author == null ? "" : author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescrip() {
        return descrip == null ? "" : descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getPath() {
        return path == null ? "" : path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        
        this.duration = formatTime(duration);
    }
    
    

    public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	// Format time
    private String formatTime(int time) {
        int totalSecond = time / 1000;
        int minutes = totalSecond / 60;
        int second = totalSecond % 60;
        String secondTxt  = "";
        if(String.valueOf(second).length()==1){
            secondTxt = "0"+second;
        }else{
            secondTxt = String.valueOf(second);
        }
        String formatTime = minutes + ":" + secondTxt;
        return formatTime;
    }

}
