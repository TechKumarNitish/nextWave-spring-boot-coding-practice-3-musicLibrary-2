/*

 * You can use the following import statements
  
 * import org.springframework.http.HttpStatus;
 * import org.springframework.web.server.ResponseStatusException;

 */

package com.example.song.service;

import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.song.model.Song;
import com.example.song.repository.SongRepository;
import com.example.song.model.SongRowMapper;

@Service
public class SongH2Service implements SongRepository {

    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Song> getSongs() {
        List<Song> allSongs = db.query("select * from playlist", new SongRowMapper());
        ArrayList<Song> songs = new ArrayList<>(allSongs);
        return songs;
    }

    @Override
    public Song getSongById(int songId) {
        try {
            Song song = db.queryForObject("select * from playlist where songid= ?", new SongRowMapper(), songId);
            return song;
        } catch (Exception e) {
            System.out.println(e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Song addSong(Song song) {
        db.update("insert into playlist (songname, lyricist, singer, musicdirector)values(?, ?, ?, ?)",
                song.getSongName(), song.getLyricist(), song.getSinger(),
                song.getMusicDirector());
        Song savedSong = db.queryForObject(
                "select * from playlist where songname = ? and lyricist = ? and singer = ?and musicdirector = ?",
                new SongRowMapper(), song.getSongName(), song.getLyricist(),
                song.getSinger(), song.getMusicDirector());
        return savedSong;
    }

    @Override
    public Song updateSong(int songId, Song song) {
        if (song.getSongName() != null)
            db.update("update playlist set songname = ? where songid = ?", song.getSongName(), songId);
        if (song.getLyricist() != null)
            db.update("update playlist set lyricist = ? where songid = ?", song.getLyricist(), songId);
        if (song.getSinger() != null)
            db.update("update playlist set singer = ? where songid = ?", song.getSinger(), songId);
        if (song.getMusicDirector() != null)
            db.update("update playlist set musicdirector = ? where songid = ?", song.getMusicDirector(), songId);
        return getSongById(songId);
    }

    @Override
    public void deleteSong(int songId) {
        db.update("delete from playlist where songid =  ?", songId);

    }

}