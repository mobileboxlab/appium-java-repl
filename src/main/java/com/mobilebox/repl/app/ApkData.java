package com.mobilebox.repl.app;

import static com.mobilebox.repl.misc.Utils.console;

import java.util.List;

import com.google.gson.Gson;

import net.dongliu.apk.parser.bean.UseFeature;

public class ApkData {

  private String packageName;

  private String label;

  private String icon;

  private String versionName;

  private String minSdkVersion;

  private String targetSdkVersion;

  private String maxSdkVersion;

  private List<UseFeature> usesFeature;

  private List<String> userPermissions;

  private List<String> activities;


  public String getPackageName() {
    return packageName;
  }


  public ApkData setPackageName(String packageName) {
    this.packageName = packageName;
    return this;
  }


  public String getLabel() {
    return label;
  }

  public ApkData setLabel(String label) {
    this.label = label;
    return this;
  }

  public String getIcon() {
    return icon;
  }


  public ApkData setIcon(String icon) {
    this.icon = icon;
    return this;
  }


  public String getVersionName() {
    return versionName;
  }


  public ApkData setVersionName(String versionName) {
    this.versionName = versionName;
    return this;
  }

  public String getMinSdkVersion() {
    return minSdkVersion;
  }


  public ApkData setMinSdkVersion(String minSdkVersion) {
    this.minSdkVersion = minSdkVersion;
    return this;
  }


  public String getTargetSdkVersion() {
    return targetSdkVersion;
  }


  public ApkData setTargetSdkVersion(String targetSdkVersion) {
    this.targetSdkVersion = targetSdkVersion;
    return this;
  }


  public String getMaxSdkVersion() {
    return maxSdkVersion;
  }


  public ApkData setMaxSdkVersion(String maxSdkVersion) {
    this.maxSdkVersion = maxSdkVersion;
    return this;
  }


  public String toJson() {
    return new Gson().toJson(this);
  }

  public List<UseFeature> getUsesFeature() {
    return usesFeature;
  }


  public ApkData setUsesFeature(List<UseFeature> usesFeature) {
    this.usesFeature = usesFeature;
    return this;
  }

  public List<String> getUserPermissions() {
    return userPermissions;
  }

  public ApkData setUserPermissions(List<String> userPermissions) {
    this.userPermissions = userPermissions;
    return this;
  }

  public List<String> getActivities() {
    return activities;
  }

  public ApkData setActivities(List<String> activities) {
    this.activities = activities;
    return this;
  }

  public void toConsole() {
    console("---> Package name: " + packageName);
    console("---> Label: " + label);
    console("---> Version name: " + versionName);
    console("---> Min SDK version: " + minSdkVersion);
    console("---> Target SDK version: " + targetSdkVersion);
    console("---> Max SDK version: " + maxSdkVersion);
    console("---> Uses Feature: " + usesFeature);
    console("---> Uses Permissions: " + userPermissions);
  }

}
