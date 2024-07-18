package com.ccabank.memoservice.provider.file;

/**
 * @author : <a href="mailto:patrick.simo@cca-bank.com">Patrick SIMO</a>
 * @project : entity-service
 * @Package : com.ccabank.userservice.provider.file
 * <p>
 * @date: 19/06/2023
 * @time: 09:54
 * <p>
 * Created with IntelliJ IDEA To change this template use File | Settings | File Templates.
 */
public class FileConstant {

    // The `public static final String TEMP_PROFILE_IMAGE_BASE_URL = "https://robohash.org/";` line of
    // code is creating a constant variable named `TEMP_PROFILE_IMAGE_BASE_URL` that stores the base URL
    // for generating temporary profile images using the Robohash service. This URL is used to generate
    // unique profile images for users who have not uploaded their own profile picture. The `public
    // static final` keywords indicate that this variable is a constant and can be accessed from other
    // classes.
    public static final String TEMP_PROFILE_IMAGE_BASE_URL = "https://robohash.org/";
    // The `public static final String USER_URL_PATH = "/myfile/images/";` line of code is creating a
    // constant variable named `USER_URL_PATH` that stores the URL path for accessing user images. This
    // variable is used by the `FileUserService` class to construct the URL for accessing user images
    // stored in the file system. The `public static final` keywords indicate that this variable is a
    // constant and can be accessed from other classes.
    public static final String USER_URL_PATH = "/myfile/images/";
    //	private static final String BASE_FOLDER = "E:/PetStoreImage";
    // This line of code is setting the base folder path for the file provider to the "resources" folder
    // in the project's main directory. The `System.getProperty("user.dir")` method returns the current
    // working directory of the project, and the "/src/main/resources" string is appended to it to create
    // the full path to the resources folder. The `BASE_FOLDER` variable is declared as private, static,
    // and final, meaning it cannot be changed and is accessible only within the `FileConstant` class.
    private static final String BASE_FOLDER = System.getProperty("user.dir") + "/src/main/resources";
    // The line of code is creating a constant variable named `IMAGE_FOLDER` that stores the full path to
    // the "images" folder within the `BASE_FOLDER`. The `BASE_FOLDER` variable is defined as the
    // project's "resources" folder path, and the `IMAGE_FOLDER` variable is created by appending
    // "/images/" to the `BASE_FOLDER` path. The `public static final` keywords indicate that this
    // variable is a constant and can be accessed from other classes.
    public static final String IMAGE_FOLDER = BASE_FOLDER + "/images/";
}
