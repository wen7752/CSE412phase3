package com.wen.imdbsearch;

public class MovieList {
//m.movieid,m.cover,m.primary_title,m.original_title,m.release_year,m.rating,m.runtime_duration
	
	private int sno;
	private String movieid;
	private String cover;
	private String primary_title;
	private String original_title;
	private String release_year;
	private String rating;
	private String runtime_duration;
	private String director_name;
	private String casts;
	private String genre;
	

	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
	
	public String getMovieid() {
		return movieid;
	}
	public void setMovieid(String movieid) {
		this.movieid = movieid;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getPrimary_title() {
		return primary_title;
	}
	public void setPrimary_title(String primary_title) {
		this.primary_title = primary_title;
	}
	public String getOriginal_title() {
		return original_title;
	}
	public void setOriginal_title(String original_title) {
		this.original_title = original_title;
	}
	public String getRelease_year() {
		return (release_year==null)?"0":release_year;
	}
	public void setRelease_year(String release_year) {
		this.release_year = release_year;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getRuntime_duration() {
		return runtime_duration;
	}
	public void setRuntime_duration(String runtime_duration) {
		this.runtime_duration = runtime_duration;
	}
	public String getDirector_name() {
		return director_name;
	}
	public void setDirector_name(String director_name) {
		this.director_name = director_name;
	}
	public String getCasts() {
		return casts;
	}
	public void setCasts(String casts) {
		this.casts = casts;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	
	
}//end MovieList
