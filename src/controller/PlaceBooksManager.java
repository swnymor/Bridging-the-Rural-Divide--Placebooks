package placebooks.controller;

import placebooks.model.*;

import java.util.*;
import javax.jdo.*;
import org.apache.log4j.*;


public class PlaceBooksManager
{
   	private static final Logger log = 
		Logger.getLogger(PlaceBooksManager.class.getName());

	private ArrayList<PlaceBook> pbs;

	public PlaceBooksManager()
	{
		pbs = new ArrayList<PlaceBook>();
	}

	public void newPlaceBook(int owner)
	{
		PlaceBook p = new PlaceBook(owner);
		pbs.add(p);
		
		PersistenceManager pm = PMFSingleton.get().getPersistenceManager();
		try
		{
			pm.currentTransaction().begin();
			pm.makePersistent(p);
			log.info("newPlaceBook key = "+p.getKey());
			pm.currentTransaction().commit();
		}
		finally
		{
			if (pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
			//pm.close();
		}
	}

	public ArrayList<PlaceBook> getPlaceBooks(int owner)
	{
		ArrayList<PlaceBook> pbs = new ArrayList<PlaceBook>();
		PersistenceManager pm = PMFSingleton.get().getPersistenceManager();
	
		try
		{
			pm.currentTransaction().begin();
			Query query = pm.newQuery(PlaceBook.class, "owner == " + owner);
			Collection result = (Collection)query.execute();
			Iterator i = result.iterator();
	        if (!i.hasNext())
        		return null;
			while (i.hasNext())
				pbs.add((PlaceBook)i.next());
			
			pm.currentTransaction().commit();
		}
		finally
		{
			if (pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
			//pm.close();
		}

		return pbs;
	}

	public static void deleteAllPlaceBooks()
	{
		PersistenceManager pm = PMFSingleton.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		tx.begin();
		pm.newQuery(PlaceBook.class).deletePersistentAll();
		tx.commit();

		log.info("Deleted all PlaceBooks");
	}

}