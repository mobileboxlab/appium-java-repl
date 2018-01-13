package com.mobilebox.repl.app;

import static com.google.common.base.Strings.nullToEmpty;
import static java.util.UUID.randomUUID;
import static org.zeroturnaround.zip.ZipUtil.containsEntry;
import static org.zeroturnaround.zip.ZipUtil.unpackEntry;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;

import net.dongliu.apk.parser.ApkParser;
import net.dongliu.apk.parser.bean.ApkMeta;

/**
 * A Simple APK inspector.
 */
@SuppressWarnings("resource")
public class APKInspector {

  /**
   * Retrieve basic apk metas, such as title, icon, package name, version, etc.
   * 
   * @param appPath The app path.
   * @return An {@link ApkData} instance.
   * @throws CommandsException
   */
  public ApkData inspect(final String appPath) {
    File apkFile = new File(appPath);
    ApkData app = new ApkData();

    if (apkFile.exists()) {
      try {
        ApkParser parser = new ApkParser(appPath);
        parser.setPreferredLocale(Locale.getDefault());
        ApkMeta data = parser.getApkMeta();

        app.setPackageName(nullToEmpty(data.getPackageName()))
            .setLabel(nullToEmpty(data.getLabel()))
            .setVersionName(nullToEmpty(data.getVersionName()))
            .setMaxSdkVersion(nullToEmpty(data.getMaxSdkVersion()))
            .setMinSdkVersion(nullToEmpty(data.getMinSdkVersion()))
            .setTargetSdkVersion(nullToEmpty(data.getTargetSdkVersion()))
            .setUsesFeature(data.getUsesFeatures()).setUserPermissions(data.getUsesPermissions())
            .setActivities(getActivities(parser.getManifestXml()))
            .setIcon(icon2Base64(appPath, data.getIcon()));

      } catch (Exception e) {
        throw new RuntimeException("Error: " + e.getMessage());
      }
    }

    return app;
  }

  /**
   * Return information about activities in the package in activities.
   * 
   * @param manifest The app manifest.
   * @return List of activities
   */
  private List<String> getActivities(final String manifest) {
    List<String> activities = new ArrayList<String>();
    Pattern p = Pattern.compile("android:name=\"(.*?)\"");

    XML xml = new XMLDocument(manifest);
    XML je = new XMLDocument(xml.toString());
    List<XML> name = je.nodes("//activity");

    for (XML node : name) {
      Matcher m = p.matcher(node.toString());
      if (m.find()) {
        activities.add(m.group(1));
      }
    }
    return activities;
  }

  /**
   * Retrieve the icon associated with an application. If it has not defined an icon, the default
   * app icon is returned.
   * 
   * @param appPath The aplication under test path.
   * @iconPath The path to the icon on the APK file.
   * @return The image of the icon, or the default application icon if it could not be found in
   *         base64.
   * @throws MoneyException
   */
  public String icon2Base64(final String appPath, final String iconPath) {
    String icon =
        "iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAALEwAACxMBAJqcGAAABRZJREFUeJztm89vVFUUxz/3vJm22FakZVoTRFaGUliI8QcsXICu5EcTE2ICf4BGggYTaBDoy0AFiyILjZq6xehKEH+wsZi40BohkShWlmpIoENLkZZOmTfnuGjHFKaFDn1vXgnzWU0mZ84595tz37333DdQoUKFChXuX9xUXzb62RZn8rbD1oLURxPaXsvsq3o/Gt8zp0iARj/bIuZ6QeZHG1rzZtJ2eX/ym2jj3J4iARZ23DjucG1mdtyTxMuX0tIfZsBUR84Kn1V1WDyezaSrfw0zRinIrV+Mlz1EMfjJGHwgInWY+7rhTV0UVZw7USRAYc5HOXiAyy6xHfQkuEVeIv9Vyre6KONNxxQClIm0C3Q0+ZLCb8BK9MZnbDKv3GnEJwAw8I5cU5dYr6oXcbK+sSU4Uu4cYhUA4Era/Y3nNig6Ko5tqY4b28oZP3YBAAbSVac9ky2gBnZk4d7cunLFnhMCAPTvTx7D3E4Qz0w/T/ljj5cj7pwRACCzP3kY5ZNyLo9zSgBwlulPbDXT78q1PJZdAEVHAJp2afOUBt0ul5OqTSh/UIblsewCCO4UgCWC7ulEuJp2Q2LBekUzUS+PRWeBwl49sy855UlxtjTsyS7zxP1U6mErqnzKXgGDnTV9ebXVmJ0oTIc4ScQRdLCzpg9om4nt5NNjFMyxVaD8VASIO4G4Cf0ZsMS3mmHLbRXcFkVbBakOw+9snwWKjonKORM7WuuSH/2VdlkIuQIW+Pbo9XxwRnDvAivDGnwYCFKN8ITDvTdiwekFu3UxhFgBj2zXeWP54CRCK9CnRnviunfq0mGJfakDSPlWpwRrxTjkYLnn8t8u8e2p0AQYqwu24lwr0BeMJVZf6XJXw/IdBpm0GwZOzPfthyoLep2wYkRzr4Y3BZzbDGCOnXNt8JO5mnZDzmgHcM5tDk0ARVsBvGHv+7B8RoVJogdAVZeHNgUKD7yp5nyzP7ZClS51rBFk3kTw6yKuJ6/aPrEzBKZ/2is6IuZ6VLR9IF3z593aw/h0SHXkEJGayPcBzf7YCjV+xMkLhcEDiMgD4DZ4uN6GPdlld/IjSC3ObRRzvY1+tiUs+8jPAqp04aTezL5IBIlXLh6UDECzr015C7qdSJuHdQEbJ//u1tPf//ZIm6h1cctZolT7ApFXgDrWAEwePIxfvEgu8TKATtxG3Y6b7C08+8gFKJT95MEX6D8olyZsamfiy6pcST2BmdjfM2eBlG8PY7luACeuJyz7svUDSt3LF9lbADhQHTKsfbb2Be6ZCgC9Ztgx8+yZgc6a82HZl60CpuvpTVcZpfYA77ZneA9VQDRUBIg7gbipCBB3AnET2s1QY8dYVpBqGfHq5koXaDoad2i9zMv/q+hoaBUgKucAgrr8c2H5jAqvJv88gCC/hyaAOfsUQIxD8317KCy/YfOgrw3m6AIws6OhCVAryQ8NzgFLqyzobdqba4vr1bepaNyh9Sk/92Iyn/8ZeEzh7GVJfhzq7fCC3brY8/InHSwPIefIUDhrgbdu8IBcCHUVuPKW/FPrEk8a9gZwRtHRMP3PholcfjGz1wdc4unBA3JhSsNUR86ivpEthajzue/3ARUB4k4gbooEKLy20uxrU/nTuZnCS1SqOhxVjCIBxMb7Z3kLuuMUoWmXNlsimHEP8G4p/svMnuxSwfUiMjd2c6pDiq2aWRusdIoqYKCz5rxiqwz7Ms63uFR12MyOz7wHWKFChQql8x+anVpT1E4upAAAAABJRU5ErkJggg==";
    File apkFile = new File(appPath);
    if (apkFile.exists()) {
      if (containsEntry(apkFile, iconPath)) {
        String tempIcon = randomUUID().toString() + ".png";
        File icono = new File(tempIcon);
        unpackEntry(apkFile, iconPath, icono);
        icon = imageToBase64(icono.getAbsolutePath());
        icono.delete();
      }
    }
    return icon;
  }

  /**
   * Image to Base64 converter
   * 
   * @param imagePath - The image path.
   * @return The image as base64 data.
   * @throws MonkeyException
   */
  private String imageToBase64(final String imagePath) {
    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    try {
      FileInputStream inputStream = new FileInputStream(imagePath);
      BufferedImage inputImage = ImageIO.read(inputStream);
      ImageIO.write(inputImage, "png", Base64.getEncoder().wrap(os));
      return os.toString(StandardCharsets.ISO_8859_1.name());
    } catch (final IOException e) {
      throw new RuntimeException(
          "We have an error with pseudo image to 64 route converter: " + e.getMessage());
    }
  }

}
