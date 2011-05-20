/**
 * 
 */
package placebooks.test;

import static org.junit.Assert.assertEquals;

import java.util.Vector;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;

import placebooks.controller.EMFSingleton;
import placebooks.controller.EverytrailHelper;
import placebooks.controller.ItemFactory;
import placebooks.controller.UserManager;
import placebooks.controller.YouTubeHelper;
import placebooks.model.EverytrailLoginResponse;
import placebooks.model.EverytrailPicturesResponse;
import placebooks.model.ImageItem;
import placebooks.model.LoginDetails;
import placebooks.model.User;
import placebooks.model.VideoItem;
import placebooks.utils.InitializeDatabase;

import com.google.gdata.data.youtube.VideoFeed;

/**
 * @author pszmp
 *
 */
public class PlacebooksIntegrationTests
{
	private static final Logger log = 
		Logger.getLogger(PlacebooksIntegrationTests.class.getName());

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
		
		Vector<Node> pictures = picturesResponse.getPictures();
		
		//assertEquals(800, imageItem.getImage().getWidth());
		//assertEquals(479, imageItem.getImage().getHeight());
		
		final EntityManager pm = EMFSingleton.getEntityManager();				
		try
		{
			pm.getTransaction().begin();
			ImageItem imageItem = new ImageItem(testUser, null, null, null);
			pm.persist(imageItem);
			pm.getTransaction().commit();
			pm.getTransaction().begin();
			ItemFactory.toImageItem(testUser, pictures.firstElement(), imageItem);
			pm.getTransaction().commit();
		}
		finally
		{
			if (pm.getTransaction().isActive())
			{
				pm.getTransaction().rollback();
				log.error("Rolling current persist transaction back");
			}
			pm.close();
		}
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
	}
