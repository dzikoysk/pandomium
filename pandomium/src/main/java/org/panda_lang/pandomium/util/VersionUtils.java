/*
 * Copyright Osiris Team
 * All rights reserved.
 *
 * This software is copyrighted work licensed under the terms of the
 * AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package org.panda_lang.pandomium.util;

import org.jetbrains.annotations.Nullable;

/**
 * @author Osiris-Team
 */
public class VersionUtils {

    /**
     * Compares the current version with the latest
     * version and returns true if the latest version is
     * bigger than the current version. <br>
     * Note that the provided version strings may contain characters. <br>
     * These get removed before comparing the strings. Example: <br>
     * app-V1.0.45bT -> 1.0.45
     */
    public boolean compare(@Nullable String currentVersion, @Nullable String latestVersion) {
        try {
            if (currentVersion == null) throw new NullPointerException("Null currentVersion!");
            if (latestVersion == null) throw new NullPointerException("Null latestVersion!");

            // First duplicate the strings so the original ones don't get altered
            String currentVersionDUPLICATE = "" + currentVersion;
            String latestVersionDUPLICATE = "" + latestVersion;

            // Remove left and right spaces
            currentVersionDUPLICATE = currentVersionDUPLICATE.trim();
            latestVersionDUPLICATE = latestVersionDUPLICATE.trim();

            // Remove everything except numbers and dots
            currentVersionDUPLICATE = currentVersionDUPLICATE.replaceAll("[^0-9.]", "");
            latestVersionDUPLICATE = latestVersionDUPLICATE.replaceAll("[^0-9.]", "");

            if (currentVersionDUPLICATE.isEmpty()) throw new Exception("Empty currentVersion string!");
            if (latestVersionDUPLICATE.isEmpty()) throw new Exception("Empty latestVersion string!");

            // If there are dots in the string we split it up
            String[] currentVersionArray = null;
            String[] latestVersionArray = null;
            if (currentVersionDUPLICATE.contains(".")) currentVersionArray = currentVersionDUPLICATE.split("\\.");
            if (latestVersionDUPLICATE.contains(".")) latestVersionArray = latestVersionDUPLICATE.split("\\.");

            // Then we create a new String which contains all numbers but with one dot after the first number
            StringBuilder currentVersionBuilder = null;
            StringBuilder latestVersionBuilder = null;
            if (currentVersionArray != null) {
                currentVersionBuilder = new StringBuilder();
                currentVersionBuilder.append(currentVersionArray[0] + ".");
                for (int i = 1; i < currentVersionArray.length; i++) {
                    currentVersionBuilder.append(currentVersionArray[i]);
                }
            }

            if (latestVersionArray != null) {
                latestVersionBuilder = new StringBuilder();
                latestVersionBuilder.append(latestVersionArray[0] + ".");
                for (int i = 1; i < latestVersionArray.length; i++) {
                    latestVersionBuilder.append(latestVersionArray[i]);
                }
            }

            // Replace the main version strings
            if (currentVersionBuilder != null) currentVersionDUPLICATE = currentVersionBuilder.toString();
            if (latestVersionBuilder != null) latestVersionDUPLICATE = latestVersionBuilder.toString();

            // Finally we compare the versions and return true if the latest version is bigger than the current one
            return Double.parseDouble(currentVersionDUPLICATE) < Double.parseDouble(latestVersionDUPLICATE);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
