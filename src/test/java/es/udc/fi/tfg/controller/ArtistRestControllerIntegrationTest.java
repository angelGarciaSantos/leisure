package es.udc.fi.tfg.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import es.udc.fi.tfg.config.AppConfig;
import es.udc.fi.tfg.model.Artist;

//TODO: para ejecutar los tests descomentar el RunWith y los test

@WebAppConfiguration
//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class ArtistRestControllerIntegrationTest {
	private static final String BASE_URI = "http://localhost:8080/leisure/artists";
    private static final int UNKNOWN_ID = Integer.MAX_VALUE;

    @Autowired
    private RestTemplate template;

    // =========================================== Get All Users ==========================================

    //@Test
    public void test_get_all_success(){
        ResponseEntity<Artist[]> response = template.getForEntity(BASE_URI, Artist[].class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        //validateCORSHttpHeaders(response.getHeaders());
    }

    // =========================================== Get Artist By ID =========================================

    //@Test
    public void test_get_by_id_success(){
        ResponseEntity<Artist> response = template.getForEntity(BASE_URI + "/1", Artist.class);
        Artist artist = response.getBody();
        assertThat(artist.getId(), is(1));
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        //validateCORSHttpHeaders(response.getHeaders());
    }

    //@Test
    public void test_get_by_id_failure_not_found(){
        try {
            ResponseEntity<Artist> response = template.getForEntity(BASE_URI + "/" + UNKNOWN_ID, Artist.class);
            fail("should return 404 not found");
        } catch (HttpClientErrorException e){
            assertThat(e.getStatusCode(), is(HttpStatus.NOT_FOUND));
            //validateCORSHttpHeaders(e.getResponseHeaders());
        }
    }

    // =========================================== Create New Artist ========================================

    //@Test
    public void test_create_new_artist_success(){
        Artist newArtist = new Artist("new name_" + Math.random(), "Description", 8);
        URI location = template.postForLocation(BASE_URI, newArtist, Artist.class);
        assertThat(location, notNullValue());
        //TODO: detecta un id incorrecto.
    }
//
//    @Test
//    public void test_create_new_user_fail_exists(){
//        User existingUser = new User("Arya Stark");
//        try {
//            URI location = template.postForLocation(BASE_URI, existingUser, User.class);
//            fail("should return 409 conflict");
//        } catch (HttpClientErrorException e){
//            assertThat(e.getStatusCode(), is(HttpStatus.CONFLICT));
//            validateCORSHttpHeaders(e.getResponseHeaders());
//        }
//    }
//
//    // =========================================== Update Existing User ===================================
//
//    @Test
//    public void test_update_user_success(){
//        User existingUser = new User(2, "John Snow Updated");
//        template.put(BASE_URI + "/" + existingUser.getId(), existingUser);
//    }
//
//    @Test
//    public void test_update_user_fail(){
//        User existingUser = new User(UNKNOWN_ID, "update");
//        try {
//            template.put(BASE_URI + "/" + existingUser.getId(), existingUser);
//            fail("should return 404 not found");
//        } catch (HttpClientErrorException e){
//            assertThat(e.getStatusCode(), is(HttpStatus.NOT_FOUND));
//            validateCORSHttpHeaders(e.getResponseHeaders());
//        }
//    }
//
//    // =========================================== Delete User ============================================
//
//    @Test
//    public void test_delete_user_success(){
//        template.delete(BASE_URI + "/" + getLastUser().getId());
//    }
//
//    @Test
//    public void test_delete_user_fail(){
//        try {
//            template.delete(BASE_URI + "/" + UNKNOWN_ID);
//            fail("should return 404 not found");
//        } catch (HttpClientErrorException e){
//            assertThat(e.getStatusCode(), is(HttpStatus.NOT_FOUND));
//            validateCORSHttpHeaders(e.getResponseHeaders());
//        }
//    }
//
//    private User getLastUser(){
//        ResponseEntity<User[]> response = template.getForEntity(BASE_URI, User[].class);
//        User[] users = response.getBody();
//        return users[users.length - 1];
//    }
//
//    // =========================================== CORS Headers ===========================================
//
//    public void validateCORSHttpHeaders(HttpHeaders headers){
//        assertThat(headers.getAccessControlAllowOrigin(), is("*"));
//        assertThat(headers.getAccessControlAllowHeaders(), hasItem("*"));
//        assertThat(headers.getAccessControlMaxAge(), is(3600L));
//        assertThat(headers.getAccessControlAllowMethods(), hasItems(
//                HttpMethod.GET,
//                HttpMethod.POST,
//                HttpMethod.PUT,
//                HttpMethod.OPTIONS,
//                HttpMethod.DELETE));
//    }
}
