package com.mobilebox.repl.app;

import static com.google.common.base.Strings.nullToEmpty;

import java.io.File;
import java.util.Locale;

import com.mobilebox.repl.exceptions.CommandsException;

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
  public ApkData inspect(final String appPath) throws CommandsException {
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
           .setUsesFeature(data.getUsesFeatures())
           .setUserPermissions(data.getUsesPermissions());

      } catch (Exception e) {
        throw new CommandsException("Error: " + e.getMessage());
      }
    }

    return app;
  }

}
