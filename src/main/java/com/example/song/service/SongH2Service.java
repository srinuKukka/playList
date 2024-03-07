/*
 * 
 * You can use the following import statements
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 * 
 */

// Write your code here
package com.example.song.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

import com.example.song.repository.SongRepository;
import com.example.song.model.Song;
import com.example.song.model.SongRowMapper;

@Service
public class SongH2Service implements SongRepository {

	@Autowired
	private JdbcTemplate db;

	@Override
	public ArrayList<Song> getSongs() {
		List<Song> songCollection = db.query("SELECT * FROM playList", new SongRowMapper());
		ArrayList<Song> playList = new ArrayList<>(songCollection);
		return playList;
	}

	@Override
	public Song getSongById(int songId) {
		try {
			Song song = db.queryForObject("SELECT * FROM PlayList WHERE songId = ?", new SongRowMapper(), songId);
			return song;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

	}

	@Override
	public Song addSong(Song song) {
		db.update("INSERT into PlayList(songName, lyricist, singer, musicDirector) values(?, ?, ?, ?)",
				song.getSongName(), song.getLyricist(), song.getSinger(), song.getMusicDirector());
		Song addSong = db.queryForObject(
				"SELECT * FROM PlayList WHERE songName=? and lyricist=? and singer=? and musicDirctor=?",
				new SongRowMapper(), song.getSongName(), song.getLyricist(), song.getSinger(), song.getMusicDirector());
		return addSong;
	}

	@Override
	public Song updateSong(int songId, Song song) {

		if (song.getSongName() != null) {
			db.update("update PlayList Set songName = ? Where songId = ?", song.getSongName(), songId);
		}
		if (song.getLyricist() != null) {
			db.update("update PlayList Set lyricist = ? Where songId = ?", song.getLyricist(), songId);
		}
		if (song.getSinger() != null) {
			db.update("update PlayList Set singer = ? Where songId = ?", song.getSinger(), songId);
		}
		if (song.getMusicDirector() != null) {
			db.update("update PlayList Set musicDirector = ? Where songId = ?", song.getMusicDirector(), songId);
		}
		return getSongById(songId);
	}

	@Override
	public void deleteSong(int songId) {
		db.update("DELETE FROM PlayList WHERE songId = ?", songId);

	}
}