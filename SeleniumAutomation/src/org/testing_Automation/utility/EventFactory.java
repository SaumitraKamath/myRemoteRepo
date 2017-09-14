package org.testing_Automation.utility;

/*import com.intellect.entity.event.Add;
 import com.intellect.entity.event.Add_Instruction;
 import com.intellect.entity.event.AuditDetails;
 import com.intellect.entity.event.Authorize;
 import com.intellect.entity.event.CaptureEvent;
 import com.intellect.entity.event.Close;
 import com.intellect.entity.event.EventHandler;
 import com.intellect.entity.event.Fill;
 import com.intellect.entity.event.GridAdd;
 import com.intellect.entity.event.GridDelete;
 import com.intellect.entity.event.GridModify;
 import com.intellect.entity.event.Home;
 import com.intellect.entity.event.Login;
 import com.intellect.entity.event.LoginHUB;
 import com.intellect.entity.event.Logout;
 import com.intellect.entity.event.Save;
 import com.intellect.entity.event.Search;
 import com.intellect.entity.event.Switch;
 import com.intellect.entity.event.TDEvent;*/

public class EventFactory
{

	// use getEvent method to get object of type Event

	public EventHandler getEvent(String event)
	{

		if (event == null)
		{
			return null;
		}

		/*
		 * if (event.equalsIgnoreCase("Login")) { return new Login(); }
		 * 
		 * if (event.equalsIgnoreCase("LoginHUB")) { return new LoginHUB(); }
		 * 
		 * if (event.equalsIgnoreCase("Logout")) { return new Logout(); }
		 * 
		 * if (event.equalsIgnoreCase("Home")) { return new Home(); }
		 * 
		 * if (event.equalsIgnoreCase("CaptureEvent")) { return new
		 * CaptureEvent(); }
		 * 
		 * if (event.equalsIgnoreCase("GridAdd")) { return new GridAdd(); }
		 * 
		 * if (event.equalsIgnoreCase("GridModify")) { return new GridModify();
		 * }
		 * 
		 * if (event.equalsIgnoreCase("GridDelete")) { return new GridDelete();
		 * }
		 * 
		 * if (event.equalsIgnoreCase("Add")) { return new Add(); }
		 * 
		 * if (event.equalsIgnoreCase("Update")) { return new Add(); }
		 * 
		 * if (event.equalsIgnoreCase("Delete")) { return new Add(); } if
		 * (event.equalsIgnoreCase("Search")) { return new Search(); }
		 * 
		 * if (event.equalsIgnoreCase("Save")) { return new Save(); }
		 * 
		 * if (event.equalsIgnoreCase("Switch")) { return new Switch(); }
		 * 
		 * if (event.equalsIgnoreCase("TDEvent")) { return new TDEvent(); }
		 * 
		 * if (event.equalsIgnoreCase("Fill")) { return new Fill(); }
		 * 
		 * if (event.equalsIgnoreCase("AuditDetails")) { return new
		 * AuditDetails(); }
		 * 
		 * if (event.equalsIgnoreCase("Add_Instruction")) { return new
		 * Add_Instruction(); } if (event.equalsIgnoreCase("Close")) { return
		 * new Close(); } if (event.equalsIgnoreCase("Authorize")) { return new
		 * Authorize(); }
		 */
		if (event.equalsIgnoreCase("Search"))
		{
			return new Search();
		}
		if (event.equalsIgnoreCase("Add"))
		{
			return new Add();
		}
		return null;

	}

}
