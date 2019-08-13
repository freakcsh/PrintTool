package com.freak.printtool.hardware.base;

/**
 * @author Freak
 * @date 2019/8/13.
 */

public interface IActivityStatusBar {

    /**
     * 返回状态栏颜色
     *
     * @return
     */
    int getStatusBarColor();

    /**
     * drawable文件渐变状态栏
     *
     * @return
     */
    int getDrawableStatusBar();

}
