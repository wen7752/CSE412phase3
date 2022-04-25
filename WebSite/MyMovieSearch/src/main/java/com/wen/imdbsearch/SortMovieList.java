package com.wen.imdbsearch;

import java.util.Comparator;

public class SortMovieList implements Comparator<MovieList> {

	@Override
	public int compare(MovieList o1, MovieList o2) {
		
		 if (Integer.valueOf(o1.getRelease_year()) == Integer.valueOf(o2.getRelease_year()) )
	            return 0;
	        else if (Integer.valueOf(o1.getRelease_year()) > Integer.valueOf(o2.getRelease_year()))
	            return 1;
	        else
	            return -1;
		
	}

}
