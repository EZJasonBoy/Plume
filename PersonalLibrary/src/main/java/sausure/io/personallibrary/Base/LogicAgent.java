package sausure.io.personallibrary.Base;

import android.os.Bundle;

/**
 * Created by JOJO on 2015/9/2.
 *
 */
public interface LogicAgent
{
    /**
     * start activity
     * @param clazz
     */
    void readyGo(Class<?> clazz);

    /**
     * start activity
     * @param clazz
     * @param bundle
     */
    void readyGo(Class<?> clazz, Bundle bundle);

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
