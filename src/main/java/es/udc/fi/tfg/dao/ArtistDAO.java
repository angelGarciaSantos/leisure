package es.udc.fi.tfg.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import es.udc.fi.tfg.model.Artist;

@Component
public class ArtistDAO {
	// Dummy database. Initialize with some dummy values.
	private static List<Artist> artists;
	{
		artists = new ArrayList<Artist>();
		artists.add(new Artist(101,"Metallica","Grupo de trash metal",0.0));
		artists.add(new Artist(201,"Red Hot Chili Peppers","Grupo de grunge/funk",0.0));
		artists.add(new Artist(301,"Ernesto Sevilla","Cómico",0.0));
	}

	/**
	 * Returns list of artists from dummy database.
	 * 
	 * @return list of artists
	 */
	public List<Artist> list() {
		return artists;
	}

	/**
	 * Return customer object for given id from dummy database. If customer is
	 * not found for id, returns null.
	 * 
	 * @param id
	 *            customer id
	 * @return customer object for given id
	 */
	public Artist get(Long id) {

		for (Artist a : artists) {
			if (a.getId().equals(id)) {
				return a;
			}
		}
		return null;
	}

	/**
	 * Create new artist in dummy database. Updates the id and insert new
	 * customer in list.
	 * 
	 * @param customer
	 *            Customer object
	 * @return customer object with updated id
	 */
	public Artist create(Artist artist) {
		artist.setId(System.currentTimeMillis());
		artists.add(artist);
		return artist;
	}

	/**
	 * Delete the artist object from dummy database. If artist not found for
	 * given id, returns null.
	 * 
	 * @param id
	 *            the customer id
	 * @return id of deleted customer object
	 */
	public Long delete(Long id) {

		for (Artist c : artists) {
			if (c.getId().equals(id)) {
				artists.remove(c);
				return id;
			}
		}

		return null;
	}

	/**
	 * Update the customer object for given id in dummy database. If customer
	 * not exists, returns null
	 * 
	 * @param id
	 * @param customer
	 * @return customer object with id
	 */
	public Artist update(Long id, Artist artist) {

		for (Artist a : artists) {
			if (a.getId().equals(id)) {
				artist.setId(a.getId());
				artists.remove(a);
				artists.add(artist);
				return artist;
			}
		}

		return null;
	}
}
