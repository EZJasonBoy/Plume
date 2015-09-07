package sausure.io.personallibrary.Helper;

import android.os.Bundle;

/**
 * Created by JOJO on 2015/9/2.
 *
 */
public interface StartComponentHelper
{
    /**
     * start activity
     * @param clazz
     */
    void readyGo(Class<?> clazz);

    /**
     * start activity before finish or not
     * @param clazz
     * @param finish
     */
    void readyGo(Class<?> clazz,boolean finish);

    /**
     * start activity
     * @param clazz
     * @param bundle
     */
    void readyGo(Class<?> clazz, Bundle bundle);

    /**
     * start activity before finish or not
     * @param clazz
     * @param bundle
     * @param finish
     */
    void readyGo(Class<?> clazz,Bundle bundle,boolean finish);

    /**
     * start activity
     * @param clazz
     * @param requestCode
     */
    void readyGoForResult(Class<?> clazz, int requestCode);

    /**
     * start activity
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle);
}
