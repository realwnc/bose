package com.mids.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程执行器工具类
 * 
 * @authorwncheng
 * @version v1.0
 * @time 2016年8月23日
 * @since
 */
public class ExecuteSingleThreadUtils {

	/**
	 * 获取Executor执行器
	 * 
	 * @return Executor执行器
	 * @sine
	 */
	final public static ExecutorService getExecutor() {

		return SingletonHolder.executor;
	}

	/**
	 * 单例懒加载帮助类
	 * 
	 * @author wncheng
	 * @version v1.0
	 * @time 2016年8月23日
	 * @since
	 */
	private static class SingletonHolder {
		final private static ExecutorService executor = Executors.newSingleThreadExecutor();
	}
}
