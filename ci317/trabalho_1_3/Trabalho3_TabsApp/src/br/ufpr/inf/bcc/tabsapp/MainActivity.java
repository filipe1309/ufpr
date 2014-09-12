package br.ufpr.inf.bcc.tabsapp;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends Activity {

	//TextView messageTextView; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//messageTextView = (TextView) findViewById(R.id.tv_frag);
		final ActionBar actionBar = getActionBar();
		
		// Specify that tabs should be displayed in the action bar.
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	    // Create a tab listener that is called when the user changes tabs.
	    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
	        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
	            // show the given tab
	        	 Log.d("SimpleActionBarTabsActivity","tab "
	        			 + String.valueOf(tab.getPosition()) + " clicked 0");
	        	
	        	 if (tab.getPosition() == 0) {
	        		// Intent intent = new Intent(getBaseContext(), MainActivity.class);startActivity(intent);	     	        		 
	        		 TextView messageTextView = (TextView) findViewById(R.id.tv_frag);
	        		 TextView messageTextView1 = (TextView) findViewById(R.id.tv_frag1);
	        		 TextView messageTextView2 = (TextView) findViewById(R.id.tv_frag2);
	        		 TextView messageTextView3 = (TextView) findViewById(R.id.tv_frag3);
	        		 TextView messageTextView4 = (TextView) findViewById(R.id.tv_frag4);
	        		 if(messageTextView != null)
	        			 messageTextView.setText(R.string.tv_frag0);
	        		 Log.d("SimpleActionBarTabsActivity","tab "
		        			 + String.valueOf(tab.getPosition()) + " if 0, Main");
	        		 
	        	 }
	        	 if (tab.getPosition() == 1) {
	        		 //Intent intent = new Intent(getBaseContext(), Activity1.class);startActivity(intent);
	        		 TextView messageTextView = (TextView) findViewById(R.id.tv_frag);
	        		 messageTextView.setText(R.string.tv_frag1);
	        		 Log.d("SimpleActionBarTabsActivity","tab "
		        			 + String.valueOf(tab.getPosition()) + " if 1, Main");
	        	 }
	        	 if (tab.getPosition() == 2) {	
	        		 //Intent intent = new Intent(getBaseContext(), Activity2.class);startActivity(intent);
	        		 TextView messageTextView = (TextView) findViewById(R.id.tv_frag);
	        		 messageTextView.setText(R.string.tv_frag2);
	        		 Log.d("SimpleActionBarTabsActivity","tab "
		        			 + String.valueOf(tab.getPosition()) + " if 2, Main");
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

	    //String tab = R.string.tab_frag;
	    // Add 3 tabs, specifying the tab's text and TabListener
	    for (int i = 0; i < 3; i++) {
	        actionBar.addTab(
	                actionBar.newTab()
	                        .setText("Tab " + i)
	                        .setTabListener(tabListener));
	    }

		
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
}
