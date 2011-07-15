/**
 * 
 */
package placebooks.test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.log.Log;
import org.w3c.dom.Node;

import placebooks.controller.EMFSingleton;
import placebooks.controller.EverytrailHelper;
import placebooks.controller.ItemFactory;
import placebooks.controller.PlaceBooksAdminControllerDebug;
import placebooks.controller.UserManager;
import placebooks.model.EverytrailLoginResponse;
import placebooks.model.EverytrailPicturesResponse;
import placebooks.model.EverytrailTracksResponse;
import placebooks.model.GPSTraceItem;
import placebooks.model.ImageItem;
import placebooks.model.LoginDetails;
import placebooks.model.User;

/**
 * @author pszmp
 *
 */
public class PlacebooksIntegrationTests
{
//	private static final Logger log = 
//		Logger.getLogger(PlacebooksIntegrationTests.class.getName());

	final EntityManager pm = EMFSingleton.getEntityManager();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		// Populate the database with test data
		//InitializeDatabase.main(null);		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testUserLogin()
	{
		placebooks.model.User testUser1 = UserManager.getUser(pm, "everytrail_test@live.co.uk");
		assertEquals("everytrail_test@live.co.uk", testUser1.getEmail());
		assertEquals("Everytrail Test User", testUser1.getName());

		placebooks.model.User testUser2 = UserManager.getUser(pm, "placebooks.test@gmail.com");
		assertEquals("placebooks.test@gmail.com", testUser2.getEmail());
		assertEquals("Youtube Test User", testUser2.getName());
	}

	@Test
	public void testToImageItemFromEverytrail()
	{
		User testUser = UserManager.getUser(pm, "everytrail_test@live.co.uk");
		LoginDetails details = testUser.getLoginDetails("Everytrail");		

		EverytrailLoginResponse loginResponse =  EverytrailHelper.UserLogin(details.getUsername(), details.getPassword());
		assertEquals("success", loginResponse.getStatus());
		assertEquals(details.getUserID(), loginResponse.getValue());

		EverytrailPicturesResponse picturesResponse = EverytrailHelper.Pictures(loginResponse.getValue());

		HashMap<String, Node> pictures = picturesResponse.getPicturesMap();
		assertEquals(36, pictures.size());
		HashMap<String, String> pictureTrip = picturesResponse.getPictureTrips();
		HashMap<String, String> tripNames = picturesResponse.getTripNames();

		ImageItem imageItem = new ImageItem(testUser, null, null, null);
		Set<String> keys = pictures.keySet();
		String id = keys.iterator().next();
		Node n = pictures.get(id);
		String tripId = pictureTrip.get(id);
		String tripName =  tripNames.get(pictureTrip.get(id));
		ItemFactory.toImageItem(testUser, n, imageItem, tripId, tripName);
		//assertEquals(800, imageItem.getImage().getWidth());
		//assertEquals(479, imageItem.getImage().getHeight());

		//ItemFactory.toImageItem(testUser, n, imageItem, pictureTrip.get(id), tripNames.get(pictureTrip.get(id)));
		imageItem.saveUpdatedItem();
	}

	@Test
	public void testToGPSTraceItem()
	{
		//Log user in after getting details from db
		User testUser = UserManager.getUser(pm, "everytrail_test@live.co.uk");
		LoginDetails details = testUser.getLoginDetails("Everytrail");		

		// Check login is ok then use the userid 
		EverytrailLoginResponse loginResponse =  EverytrailHelper.UserLogin(details.getUsername(), details.getPassword());
		assertEquals("success", loginResponse.getStatus());
		assertEquals(details.getUserID(), loginResponse.getValue());



		EverytrailTracksResponse tracksResponse = EverytrailHelper.Tracks("1017230");
		assertEquals("success", tracksResponse.getStatus());
		assertEquals(2, tracksResponse.getTracks().size());

		GPSTraceItem gpsTrace = new GPSTraceItem(testUser, null, null);

		Node trackToUse = tracksResponse.getTracks().lastElement();
		ItemFactory.toGPSTraceItem(testUser, trackToUse, gpsTrace, "1", "Test");
		/*GPSTraceItem saved = (GPSTraceItem)*/ gpsTrace.saveUpdatedItem();
	}

	/*	@Test
	public void testToVideoItemFromYouTube()
	{
		User testUser = UserManager.getUser(pm, "placebooks.test@gmail.com");
		LoginDetails details = testUser.getLoginDetails("YouTube");		

		VideoFeed feed = YouTubeHelper.UserVideos(details.getUsername());

		VideoItem videoItem = ItemFactory.toVideoItem(testUser, feed.getEntries().get(0));
		log.debug(videoItem.getSourceURL());
		//assertEquals(800, videoItem.getSourceURL());	
	}*/

	@Test
	public void testGetEverytrailData() throws Exception
	{
		User testUser = UserManager.getUser(pm, "everytrail_test@live.co.uk");
		PlaceBooksAdminControllerDebug pacd = new PlaceBooksAdminControllerDebug();
		pacd.getEverytrailData();
	}

}
