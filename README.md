GetImageExample
===============

 * inspire from: https://github.com/nostra13/Android-Universal-Image-Loader/issues/451
 * description:
 *          使用 Android-Universal-Image-Loader 实现 android Html.fromHtml (String source, Html.ImageGetter imageGetter, Html.TagHandler tagHandler)中的imageGetter方法.
 *          后加载图片.
 * sample:
 *          String htmlSnippetCode = "如图，已知抛物线 y=ax<sup>2</sup>+bx+c（a≠0）的顶点坐标为（4，<img src=\"http://p2.qingguo.com/G1/M00/9F/B2/rBACE1PfJ7iQLN4bAAABOwdsNv0023.png\" height=\"42\" width=\"7\">），且与y轴交于点C，与x轴交于A，B两点（点A在点B的左边），且A点坐标为（2，0）。在抛物线的对称轴 <img src=\"http://p2.qingguo.com/G1/M00/9F/B2/rBACE1PfJ7iTXZhbAAABESQziLA683.png\" height=\"21\" width=\"6\">上是否存在一点P，使AP+CP的值最小？若存在，则AP+CP的最小值为（&nbsp;&nbsp;&nbsp;&nbsp;）<img src=\"http://p1.qingguo.com/G1/M00/62/2A/rBACFFPfJ7iiO8gTAAALHRPDATw26.jpeg\" height=\"165\" width=\"193\">";
 *          TextView TVMain = (TextView) this.findViewById(R.id.TVMain);
 *          UilImageGetter imageGetter = new UilImageGetter(TVMain,this);
 *          Spanned spanned = Html.fromHtml(htmlSnippetCode, imageGetter, null);
 *          TVMain.setText(spanned);
