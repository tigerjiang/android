package com.joy.cloudshare.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.igrs.base.android.util.IgrsType.FileType;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class CommonResource {
    
    private static final Uri               AUDIO_CONTENT_URI  = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private static final Uri              PIC_CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private static final Uri              VEDIO_CONTENT_URI = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    private static HashMap<FileType, Uri> resourceMap       = new HashMap<FileType, Uri>();
    static {
        resourceMap.put(FileType.pic, PIC_CONTENT_URI);
        resourceMap.put(FileType.music, AUDIO_CONTENT_URI);
        resourceMap.put(FileType.video, VEDIO_CONTENT_URI);
    };
    
    public static Cursor getFilesCursor(Context context, FileType type,String sharePath) {
        Cursor cursor = null;
        String selection = null;
        String sortOrder = null;
        
        if (type == FileType.pic) {
            selection = MediaStore.Images.ImageColumns.DATA+" like ?";
            sortOrder = MediaStore.Images.ImageColumns.DATE_ADDED;
        } else if (type == FileType.music) {
            selection = MediaStore.Audio.AudioColumns.DATA+" like ?";
            sortOrder = MediaStore.Audio.AudioColumns.DATE_ADDED;
        } else {
            selection = MediaStore.Video.VideoColumns.DATA+" like ?";
            sortOrder = MediaStore.Video.VideoColumns.DATE_ADDED+" desc";
        }
        try {
            cursor = context.getContentResolver().query(resourceMap.get(type),
                    null, selection, new String[]{sharePath+"%"}, sortOrder);
        } catch (Exception err) {
            err.printStackTrace();
        }
        return cursor;
    }
    
    public static List<MediaInfo> getPicResourceList(Cursor cursor) {
        List<MediaInfo> tmpList  = new ArrayList<MediaInfo>();
        if(cursor!=null&&cursor.moveToFirst()){
            do{
                MediaInfo info = new MediaInfo();
                info.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID)));
                info.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.TITLE)));
                info.setTime(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_ADDED)));
//                info.setDuration(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE)));
                info.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)));
                info.setUrl("http://127.0.0.1:2001/"+info.getPath());
                info.setFileType(FileType.pic);
                tmpList.add(info);
            }while(cursor.moveToNext());
            cursor.close();
        }
        return tmpList;
    }
    
    public static List<MediaInfo> getAudioResourceList(Cursor cursor) {
        List<MediaInfo> tmpList  = new ArrayList<MediaInfo>();
        if(cursor!=null&&cursor.moveToFirst()){
            do{
                MediaInfo info = new MediaInfo();
                info.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns._ID)));
                info.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)));
                info.setTime(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_ADDED)));
                info.setAuthor(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST)));
                info.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)));
                info.setDuration(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION)));
                info.setUrl("http://127.0.0.1:2001/"+info.getPath());
                info.setFileType(FileType.music);
                tmpList.add(info);
            }while(cursor.moveToNext());
        }
        return tmpList;
    }
    
    public static List<MediaInfo> getVideoResourceList(Cursor cursor) {
        List<MediaInfo> tmpList  = new ArrayList<MediaInfo>();
        if(cursor!=null&&cursor.moveToFirst()){
            do{
                MediaInfo info = new MediaInfo();
                info.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID)));
                info.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.TITLE)));
                info.setTime(cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_ADDED)));
                info.setAuthor(cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.ALBUM)));
                info.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA)));
                info.setDuration(cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION)));
                info.setUrl("http://127.0.0.1:2001/"+info.getPath());
                info.setFileType(FileType.video);
                tmpList.add(info);
            }while(cursor.moveToNext());
        }
        return tmpList;
    }
}
