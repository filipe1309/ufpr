package br.ufpr.inf.bcc.tabsapp;

import br.ufpr.inf.bcc.tabsapp.MainActivity.PlaceholderFragment;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class Activity2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final ActionBar actionBar = getActionBar();

		// Specify that tabs should be displayed in the action bar.
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	    // Create a tab listener that is called when the user changes tabs.
	    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
	        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
	            // show the given tab
	        	 Log.d("SimpleActionBarTabsActivity","tab "
	        			 + String.valueOf(tab.getPosition()) + " clicked");
	        	 
	        	 if (tab.getPosition() == 0) {
	        		 Intent  intent = new Intent(getBaseContext(), MainActivity.class);startActivity(intent);
	        	 }
	        	 if (tab.getPosition() == 1) {
	        		 Intent  intent = new Intent(getBaseContext(), Activity1.class);startActivity(intent);
	        	 }
	        	 if (tab.getPosition() == 2) {
	        		// intent = new Intent(Activity2.this, Activity2.class);
	        	 }
	        }

	        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
	            // hide the given tab
	        	 Log.d("SimpleActionBarTabsActivity","tab "
	        			 + String.valueOf(tab.getPosition()) + " un-clicked");
	        }

	        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
	            // probably ignore this event
	        	 Log.d("SimpleActionBarTabsActivity","tab "
	        			 + String.valueOf(tab.getPosition()) + " re-clicked");
	        }
	    };

	    // Add 3 tabs, specifying the tab's text and TabListener
	    for (int i = 0; i < 3; i++) {
	        actionBar.addTab(
	                actionBar.newTab()
	                        .setText("Tab " + (i + 1))
	                        .setTabListener(tabListener));
	    }

		setContentView(R.layout.activity_activity2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
