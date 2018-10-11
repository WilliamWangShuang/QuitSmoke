package clientservice.factory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.quitsmoke.william.quitsmokeappclient.Fragments.MainFragment;
import com.quitsmoke.william.quitsmokeappclient.Fragments.PartnerMainFragment;
import com.quitsmoke.william.quitsmokeappclient.Fragments.SmokerMainFragment;

public class SwitchMainAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public SwitchMainAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                SmokerMainFragment tab1 = new SmokerMainFragment();
                return tab1;
            case 1:
                PartnerMainFragment tab2 = new PartnerMainFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
