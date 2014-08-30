/**
 *
 *
 */
package com.example.gaoyong.getimageexample;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * @author gao yong (gaoyong06[at]qq[dot]com)
 */


public class MainActivity extends Activity {

    private GIEApplication application;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.application = (GIEApplication) this.getApplication();
        String htmlSnippetCode = "如图，已知抛物线 y=ax<sup>2</sup>+bx+c（a≠0）的顶点坐标为（4，<img src=\"http://p2.qingguo.com/G1/M00/9F/B2/rBACE1PfJ7iQLN4bAAABOwdsNv0023.png\" height=\"42\" width=\"7\">），且与y轴交于点C，与x轴交于A，B两点（点A在点B的左边），且A点坐标为（2，0）。在抛物线的对称轴 <img src=\"http://p2.qingguo.com/G1/M00/9F/B2/rBACE1PfJ7iTXZhbAAABESQziLA683.png\" height=\"21\" width=\"6\">上是否存在一点P，使AP+CP的值最小？若存在，则AP+CP的最小值为（&nbsp;&nbsp;&nbsp;&nbsp;）<img src=\"http://p1.qingguo.com/G1/M00/62/2A/rBACFFPfJ7iiO8gTAAALHRPDATw26.jpeg\" height=\"165\" width=\"193\">";
        TextView TVMain = (TextView) this.findViewById(R.id.TVMain);
        UilImageGetter imageGetter = new UilImageGetter(TVMain,this,this.application);
        Spanned spanned = Html.fromHtml(htmlSnippetCode, imageGetter, null);
        TVMain.setText(spanned);
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



    @Override
    public void onResume() {

        super.onResume();
    }
}
