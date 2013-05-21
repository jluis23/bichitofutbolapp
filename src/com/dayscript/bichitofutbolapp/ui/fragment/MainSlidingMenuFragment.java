package com.dayscript.bichitofutbolapp.ui.fragment;

import java.util.Properties;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.dayscript.bichitofutbolapp.FacebookLoginActivity;
import com.dayscript.bichitofutbolapp.MainActivity;
import com.dayscript.bichitofutbolapp.R;
import com.dayscript.bichitofutbolapp.util.PreferencesHelper;
import com.facebook.widget.ProfilePictureView;

public class MainSlidingMenuFragment extends SherlockFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.main_sliding_menu, container, false);
		v.findViewById(R.id.teamslist).setOnClickListener(
				new MainSlidingMenuFragment.OnClickListener() {

					@Override
					public void onClick(View v) {
						super.onClick(v);
						//Intent i = new Intent(getActivity(),
							//	TeamsActivity.class);
						
						//startActivity(i);
					}
				});
		v.findViewById(R.id.positionstable).setOnClickListener(
				new MainSlidingMenuFragment.OnClickListener() {

					@Override
					public void onClick(View v) {
						super.onClick(v);
						/*Intent i = new Intent(getActivity(),
								PositionsTableActivity.class);
						startActivity(i);*/
					}
				});
		v.findViewById(R.id.loginButton).setOnClickListener(
				new MainSlidingMenuFragment.OnClickListener() {

					@Override
					public void onClick(View v) {
						super.onClick(v);
						Log.v("Iniciar sesion", "Iniciar Sesion en Facebook");
						Intent i = new Intent(getActivity(),FacebookLoginActivity.class);
						startActivity(i);
						// getSherlockActivity().finish();
					}
				});
		v.findViewById(R.id.logoutButton).setOnClickListener(
				new MainSlidingMenuFragment.OnClickListener() {

					@Override
					public void onClick(View v) {
						super.onClick(v);
						PreferencesHelper.deleteSession(getActivity());
						Intent i = new Intent(getActivity(),MainActivity.class);
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
						getActivity().finish();
					}
				});
		v.findViewById(R.id.golesFecha).setOnClickListener(
				new MainSlidingMenuFragment.OnClickListener() {

					@Override
					public void onClick(View v) {
						super.onClick(v);
						//Intent i = new Intent(getActivity(),
							//	AudioGolesActivity.class);
						//startActivity(i);
						// getSherlockActivity().finish();
					}
				});
		v.findViewById(R.id.calendario).setOnClickListener(
				new MainSlidingMenuFragment.OnClickListener() {

					@Override
					public void onClick(View v) {
						super.onClick(v);
					
						//Intent i = new Intent(getActivity(),
							//	CalendarActivity.class);
						//i.putExtra("mode", MatchResultsListFragment.MODE_CALENDAR);
						//startActivity(i);
					
					}
				});
		v.findViewById(R.id.claroTv).setOnClickListener(new MainSlidingMenuFragment.OnClickListener(){
			@Override
			public void onClick(View v) {
				super.onClick(v);
			
				//Toast t=Toast.makeText(MainSlidingMenuFragment.this.getActivity(), getActivity().getResources().getString(R.string.claro_tv), Toast.LENGTH_LONG);
				//t.setGravity(Gravity.CENTER, 0, 0);
				//t.show();
			}
		});
		v.findViewById(R.id.cabinaClaro).setOnClickListener(new MainSlidingMenuFragment.OnClickListener(){
			@Override
			public void onClick(View v) {
				super.onClick(v);
				
				
				    /*
				     * String id="cDvbBZqNVfA"; 
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
				     startActivity(intent);                 
				     }catch (ActivityNotFoundException ex){
				         Intent intent=new Intent(Intent.ACTION_VIEW, 
				         Uri.parse("http://www.youtube.com/watch?v="+id));
				         startActivity(intent);
				     }
				     */
					//Intent i = new Intent(getActivity(),
						//	CabinaClaroActivity.class);
					//startActivity(i);
			}
		});
		v.findViewById(R.id.quiniela).setOnClickListener(
				new MainSlidingMenuFragment.OnClickListener() {

					@Override
					public void onClick(View v) {
						super.onClick(v);
						/*if(PreferencesHelper.isAuthenticated(getActivity())==null)
						{
							Toast.makeText(getActivity(), getString(R.string.error_needs_login ), Toast.LENGTH_LONG).show();
						}
						else{
						Intent i = new Intent(getActivity(),
							QuinielaActivity.class);
						
						startActivity(i);
						}*/
					}
				});
		v.findViewById(R.id.social).setOnClickListener(
				new MainSlidingMenuFragment.OnClickListener() {

					@Override
					public void onClick(View v) {
						super.onClick(v);
						/*Intent i = new Intent(getActivity(),
								TwitterFeedActivity.class);
							
							startActivity(i);
						 */
					}
				});
		Properties usuario = PreferencesHelper.isAuthenticated(getActivity());
		if (usuario != null) {
			ProfilePictureView pic = new ProfilePictureView(
					this.getSherlockActivity());
			pic.setProfileId(usuario.getProperty("id"));
			v.findViewById(R.id.userContainer).setVisibility(View.VISIBLE);
			ViewGroup vg = (ViewGroup) v.findViewById(R.id.profileImageContainer);
			vg.addView(pic, 0);
			TextView tv=(TextView)v.findViewById(R.id.lblUsername);
			tv.setText(usuario.getProperty("name"));
			v.findViewById(R.id.loginButton).setVisibility(View.GONE);
		} else {
			
			v.findViewById(R.id.logoutButton).setVisibility(View.GONE);
		}
		return v;
	}

	class OnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (getSherlockActivity() instanceof MainActivity) {
				/*((MainActivity) getSherlockActivity()).getSlidingMenu()
						.toggle();
						*/
			}

		}

	}
}
