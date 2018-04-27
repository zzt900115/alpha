/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zzt.demo.alpha.service.google;


/**
 * Contains global application configuration, which is required by all samples.
 */
public final class ApplicationConfig {

    private ApplicationConfig() {
        // no instance
    }

    /**
     * Specify the name of your application. If the application name is
     * {@code null} or blank, the application will log a warning. Suggested
     * format is "MyCompany-Application/1.0".
     */
    public static final String APPLICATION_NAME = "didi-didi/1.0";

    /**
     * Specify the package name of the app.
     */
    public static final String PACKAGE_NAME = "com.sdu.didi.psnger";

    /**
     * Authentication.
     * <p>
     * Installed application: Leave this string empty and copy or
     * edit resources/client_secrets.json.
     * </p>
     * <p>
     * Service accounts: Enter the service
     * account email and add your key.p12 file to the resources directory.
     * </p>
     */
   // static final String SERVICE_ACCOUNT_EMAIL = "didichuxing@gmail.com";
    public static final String SERVICE_ACCOUNT_EMAIL = "ee-publisher@api-9166226311661560187-225809.iam.gserviceaccount.com";

    /**
     * Specify the apk file path of the apk to upload, i.e. /resources/your_apk.apk
     * <p>
     * samples.
     * </p>
     */
    public static final String APK_FILE_PATH = "~/Desktop/data/didi_psngr_20.apk";

}
